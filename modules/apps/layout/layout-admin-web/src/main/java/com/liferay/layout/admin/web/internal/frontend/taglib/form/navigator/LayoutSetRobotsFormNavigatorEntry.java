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

package com.liferay.layout.admin.web.internal.frontend.taglib.form.navigator;

import com.liferay.frontend.taglib.form.navigator.FormNavigatorEntry;
import com.liferay.frontend.taglib.form.navigator.constants.FormNavigatorConstants;
import com.liferay.layout.admin.web.internal.constants.LayoutAdminFormNavigatorConstants;

import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Sergio González
 */
@Component(
	property = "form.navigator.entry.order:Integer=170",
	service = FormNavigatorEntry.class
)
public class LayoutSetRobotsFormNavigatorEntry
	extends BaseLayoutSetFormNavigatorEntry {

	@Override
	public String getCategoryKey() {
		return FormNavigatorConstants.CATEGORY_KEY_LAYOUT_SET_ADVANCED;
	}

	@Override
	public String getFormNavigatorId() {
		return LayoutAdminFormNavigatorConstants.
			FORM_NAVIGATOR_ID_LAYOUT_SET_ADVANCED;
	}

	@Override
	public String getKey() {
		return "robots";
	}

	@Override
	public ServletContext getServletContext() {
		return _servletContext;
	}

	@Override
	protected String getJspPath() {
		return "/layout_set/robots.jsp";
	}

	@Reference(target = "(osgi.web.symbolicname=com.liferay.layout.admin.web)")
	private ServletContext _servletContext;

}