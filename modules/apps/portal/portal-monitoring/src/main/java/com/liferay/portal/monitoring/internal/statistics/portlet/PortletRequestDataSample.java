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

package com.liferay.portal.monitoring.internal.statistics.portlet;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.monitoring.PortletRequestType;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GroupThreadLocal;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.monitoring.internal.BaseDataSample;
import com.liferay.portal.monitoring.internal.MonitorNames;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Karthik Sudarshan
 * @author Michael C. Han
 * @author Brian Wing Shun Chan
 */
public class PortletRequestDataSample extends BaseDataSample {

	public PortletRequestDataSample(
		PortletRequestType requestType, PortletRequest portletRequest,
		PortletResponse portletResponse, Portal portal) {

		_requestType = requestType;

		LiferayPortletResponse liferayPortletResponse =
			portal.getLiferayPortletResponse(portletResponse);

		Portlet portlet = liferayPortletResponse.getPortlet();

		setCompanyId(portlet.getCompanyId());

		_setGroupId(portletRequest, portal);
		setName(portlet.getPortletName());
		setNamespace(MonitorNames.PORTLET);
		setUser(portletRequest.getRemoteUser());

		_displayName = portlet.getDisplayName();
		_portletId = portlet.getPortletId();
	}

	public String getDisplayName() {
		return _displayName;
	}

	public String getPortletId() {
		return _portletId;
	}

	public PortletRequestType getRequestType() {
		return _requestType;
	}

	@Override
	public String toString() {
		return StringBundler.concat(
			"{displayName=", _displayName, ", portletId=", _portletId,
			", requestType=", _requestType, ", ", super.toString(), "}");
	}

	private void _setGroupId(PortletRequest portletRequest, Portal portal) {
		long groupId = GroupThreadLocal.getGroupId();

		if (groupId != 0) {
			setGroupId(groupId);

			return;
		}

		HttpServletRequest httpServletRequest = portal.getHttpServletRequest(
			portletRequest);

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		if (themeDisplay != null) {
			setGroupId(themeDisplay.getScopeGroupId());

			return;
		}

		try {
			setGroupId(portal.getScopeGroupId(portletRequest));
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug("Unable to obtain scope group ID", portalException);
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PortletRequestDataSample.class);

	private final String _displayName;
	private final String _portletId;
	private final PortletRequestType _requestType;

}