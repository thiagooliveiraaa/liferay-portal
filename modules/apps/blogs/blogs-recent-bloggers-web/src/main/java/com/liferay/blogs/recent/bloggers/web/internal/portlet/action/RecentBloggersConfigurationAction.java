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

package com.liferay.blogs.recent.bloggers.web.internal.portlet.action;

import com.liferay.blogs.recent.bloggers.constants.RecentBloggersPortletKeys;
import com.liferay.blogs.recent.bloggers.web.internal.constants.RecentBloggersWebKeys;
import com.liferay.item.selector.ItemSelector;
import com.liferay.item.selector.criteria.UUIDItemSelectorReturnType;
import com.liferay.organizations.item.selector.OrganizationItemSelectorCriterion;
import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactoryUtil;
import com.liferay.portal.kernel.util.JavaConstants;

import javax.portlet.PortletConfig;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Sergio González
 */
@Component(
	property = "javax.portlet.name=" + RecentBloggersPortletKeys.RECENT_BLOGGERS,
	service = ConfigurationAction.class
)
public class RecentBloggersConfigurationAction
	extends DefaultConfigurationAction {

	@Override
	public String getJspPath(HttpServletRequest httpServletRequest) {
		return "/configuration.jsp";
	}

	@Override
	public void include(
			PortletConfig portletConfig, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		httpServletRequest.setAttribute(
			RecentBloggersWebKeys.ORGANIZATION_ITEM_SELECTOR_URL,
			_getOrganizationItemSelectorURL(httpServletRequest));

		super.include(portletConfig, httpServletRequest, httpServletResponse);
	}

	private String _getOrganizationItemSelectorURL(
		HttpServletRequest httpServletRequest) {

		RenderResponse renderResponse =
			(RenderResponse)httpServletRequest.getAttribute(
				JavaConstants.JAVAX_PORTLET_RESPONSE);

		OrganizationItemSelectorCriterion organizationItemSelectorCriterion =
			new OrganizationItemSelectorCriterion();

		organizationItemSelectorCriterion.setDesiredItemSelectorReturnTypes(
			new UUIDItemSelectorReturnType());

		return String.valueOf(
			_itemSelector.getItemSelectorURL(
				RequestBackedPortletURLFactoryUtil.create(httpServletRequest),
				renderResponse.getNamespace() + "selectOrganization",
				organizationItemSelectorCriterion));
	}

	@Reference
	private ItemSelector _itemSelector;

}