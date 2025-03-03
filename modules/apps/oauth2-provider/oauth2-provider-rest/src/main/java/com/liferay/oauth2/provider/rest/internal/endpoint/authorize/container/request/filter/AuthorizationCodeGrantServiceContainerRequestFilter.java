/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.oauth2.provider.rest.internal.endpoint.authorize.container.request.filter;

import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.oauth2.provider.redirect.OAuth2RedirectURIInterpolator;
import com.liferay.oauth2.provider.rest.internal.endpoint.authorize.configuration.AuthorizeScreenConfiguration;
import com.liferay.oauth2.provider.service.OAuth2ApplicationLocalService;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactory;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.servlet.ProtectedPrincipal;
import com.liferay.portal.kernel.settings.CompanyServiceSettingsLocator;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.net.URI;

import java.security.Principal;

import java.util.List;
import java.util.Objects;

import javax.annotation.Priority;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Tomas Polesovsky
 */
@Component(
	property = {
		"osgi.jaxrs.application.select=(osgi.jaxrs.name=Liferay.OAuth2.Application)",
		"osgi.jaxrs.extension=true",
		"osgi.jaxrs.name=AuthorizationCodeGrantServiceContainerRequestFilter"
	},
	service = ContainerRequestFilter.class
)
@PreMatching
@Priority(Priorities.AUTHENTICATION)
@Provider
public class AuthorizationCodeGrantServiceContainerRequestFilter
	implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext containerRequestContext) {
		UriInfo uriInfo = containerRequestContext.getUriInfo();

		if (!StringUtil.startsWith(uriInfo.getPath(), "authorize")) {
			return;
		}

		User user = _getUser();
		String clientId = ParamUtil.getString(_httpServletRequest, "client_id");

		OAuth2Application oAuth2Application =
			_oAuth2ApplicationLocalService.fetchOAuth2Application(
				user.getCompanyId(), clientId);

		boolean promptNone = _isPromptNone(oAuth2Application);

		try {
			boolean guestAuthorized = false;

			if (user.isGuestUser() && !Validator.isBlank(clientId)) {
				guestAuthorized = _containsOAuth2ApplicationViewPermission(
					oAuth2Application, user);
			}

			if (!user.isGuestUser() || guestAuthorized) {
				long userId = user.getUserId();

				containerRequestContext.setSecurityContext(
					new PortalCXFSecurityContext() {

						@Override
						public Principal getUserPrincipal() {
							return new ProtectedPrincipal(
								String.valueOf(userId));
						}

						@Override
						public boolean isSecure() {
							return _portal.isSecure(_httpServletRequest);
						}

					});

				return;
			}
		}
		catch (Exception exception) {
			_log.error("Unable to resolve authenticated user", exception);

			if (promptNone) {
				_abortWithoutPrompt(
					containerRequestContext, "interaction_required",
					oAuth2Application);
			}
			else {
				containerRequestContext.abortWith(
					Response.status(
						Response.Status.INTERNAL_SERVER_ERROR
					).build());
			}

			return;
		}

		if (promptNone) {
			_abortWithoutPrompt(
				containerRequestContext, "login_required", oAuth2Application);

			return;
		}

		String loginURL = null;

		try {
			loginURL = _getLoginURL();
		}
		catch (ConfigurationException configurationException) {
			_log.error(
				"Unable to locate configuration", configurationException);

			throw new WebApplicationException(
				Response.status(
					Response.Status.INTERNAL_SERVER_ERROR
				).build());
		}

		URI requestURI = uriInfo.getRequestUri();

		String requestURIString = requestURI.toASCIIString();

		String portalURL = _portal.getPortalURL(_httpServletRequest);

		if (requestURIString.startsWith(portalURL)) {
			requestURIString = requestURIString.substring(portalURL.length());
		}

		// Workaround for LPS-94559

		requestURIString = requestURIString.replaceFirst(
			"\\?.*",
			StringUtil.replace(
				"?" + requestURI.getRawQuery(), CharPool.COLON, "%3a"));

		loginURL = HttpComponentsUtil.addParameter(
			loginURL, "redirect", requestURIString);

		containerRequestContext.abortWith(
			Response.status(
				Response.Status.FOUND
			).location(
				URI.create(loginURL)
			).build());
	}

	private void _abortWithoutPrompt(
		ContainerRequestContext containerRequestContext, String error,
		OAuth2Application oAuth2Application) {

		if (oAuth2Application == null) {
			return;
		}

		StringBundler sb = new StringBundler(5);

		List<String> redirectURIsList =
			OAuth2RedirectURIInterpolator.interpolateRedirectURIsList(
				_httpServletRequest, oAuth2Application.getRedirectURIsList(),
				_portal);

		sb.append(redirectURIsList.get(0));

		sb.append("?error=");
		sb.append(error);

		String state = ParamUtil.getString(_httpServletRequest, "state");

		if (Validator.isNotNull(state)) {
			sb.append("&state=");
			sb.append(state);
		}

		containerRequestContext.abortWith(
			Response.temporaryRedirect(
				URI.create(sb.toString())
			).build());
	}

	private boolean _containsOAuth2ApplicationViewPermission(
			OAuth2Application oAuth2Application, User user)
		throws Exception {

		if (oAuth2Application == null) {
			return false;
		}

		if (_oAuth2ApplicationModelResourcePermission.contains(
				_permissionCheckerFactory.create(user), oAuth2Application,
				ActionKeys.VIEW)) {

			return true;
		}

		return false;
	}

	private String _getLoginURL() throws ConfigurationException {
		AuthorizeScreenConfiguration authorizeScreenConfiguration =
			_configurationProvider.getConfiguration(
				AuthorizeScreenConfiguration.class,
				new CompanyServiceSettingsLocator(
					_portal.getCompanyId(_httpServletRequest),
					AuthorizeScreenConfiguration.class.getName()));

		String loginURL = authorizeScreenConfiguration.loginURL();

		if (Validator.isBlank(loginURL)) {
			loginURL = StringBundler.concat(
				_portal.getPortalURL(_httpServletRequest),
				_portal.getPathContext(), _portal.getPathMain(),
				"/portal/login");
		}
		else if (!HttpComponentsUtil.hasDomain(loginURL)) {
			String portalURL = _portal.getPortalURL(_httpServletRequest);

			loginURL = portalURL + loginURL;
		}

		return loginURL;
	}

	private User _getUser() {
		try {
			User user = _portal.getUser(_httpServletRequest);

			if (user == null) {
				user = _userLocalService.getGuestUser(
					_portal.getCompanyId(_httpServletRequest));
			}

			return user;
		}
		catch (PortalException portalException) {
			return ReflectionUtil.throwException(portalException);
		}
	}

	private boolean _isPromptNone(OAuth2Application oAuth2Application) {
		if (oAuth2Application == null) {
			return false;
		}

		String prompt = ParamUtil.getString(_httpServletRequest, "prompt");

		if (oAuth2Application.isTrustedApplication() &&
			Objects.equals(prompt, "none")) {

			return true;
		}

		return false;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AuthorizationCodeGrantServiceContainerRequestFilter.class);

	@Reference
	private ConfigurationProvider _configurationProvider;

	@Context
	private HttpServletRequest _httpServletRequest;

	@Reference
	private OAuth2ApplicationLocalService _oAuth2ApplicationLocalService;

	@Reference(
		target = "(model.class.name=com.liferay.oauth2.provider.model.OAuth2Application)"
	)
	private ModelResourcePermission<OAuth2Application>
		_oAuth2ApplicationModelResourcePermission;

	@Reference
	private PermissionCheckerFactory _permissionCheckerFactory;

	@Reference
	private Portal _portal;

	@Reference
	private UserLocalService _userLocalService;

	private abstract static class PortalCXFSecurityContext
		implements org.apache.cxf.security.SecurityContext, SecurityContext {

		@Override
		public String getAuthenticationScheme() {
			return "session";
		}

		@Override
		public boolean isUserInRole(String role) {
			return false;
		}

	}

}