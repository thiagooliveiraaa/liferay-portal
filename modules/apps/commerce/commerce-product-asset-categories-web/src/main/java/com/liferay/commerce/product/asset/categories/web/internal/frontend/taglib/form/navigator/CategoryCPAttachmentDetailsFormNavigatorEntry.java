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

package com.liferay.commerce.product.asset.categories.web.internal.frontend.taglib.form.navigator;

import com.liferay.commerce.product.asset.categories.web.internal.servlet.taglib.ui.constants.CategoryCPAttachmentFormNavigatorConstants;
import com.liferay.commerce.product.model.CPAttachmentFileEntry;
import com.liferay.frontend.taglib.form.navigator.BaseJSPFormNavigatorEntry;
import com.liferay.frontend.taglib.form.navigator.FormNavigatorEntry;
import com.liferay.portal.kernel.language.Language;

import java.util.Locale;

import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	property = "form.navigator.entry.order:Integer=300",
	service = FormNavigatorEntry.class
)
public class CategoryCPAttachmentDetailsFormNavigatorEntry
	extends BaseJSPFormNavigatorEntry<CPAttachmentFileEntry> {

	@Override
	public String getCategoryKey() {
		return CategoryCPAttachmentFormNavigatorConstants.
			CATEGORY_KEY_CP_ATTACHMENT_FILE_ENTRY_DETAILS;
	}

	@Override
	public String getFormNavigatorId() {
		return CategoryCPAttachmentFormNavigatorConstants.
			FORM_NAVIGATOR_ID_COMMERCE_CP_ATTACHMENT_FILE_ENTRY;
	}

	@Override
	public String getKey() {
		return "details";
	}

	@Override
	public String getLabel(Locale locale) {
		return _language.get(locale, "details");
	}

	@Override
	public ServletContext getServletContext() {
		return _servletContext;
	}

	@Override
	protected String getJspPath() {
		return "/image/details.jsp";
	}

	@Reference
	private Language _language;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.commerce.product.asset.categories.web)"
	)
	private ServletContext _servletContext;

}