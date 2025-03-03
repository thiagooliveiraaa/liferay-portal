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

package com.liferay.commerce.tax.engine.fixed.web.internal.frontend.data.set.provider;

import com.liferay.commerce.constants.CommercePortletKeys;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CommerceChannelService;
import com.liferay.commerce.tax.engine.fixed.web.internal.constants.CommerceTaxRateSettingFDSNames;
import com.liferay.commerce.tax.engine.fixed.web.internal.model.TaxRate;
import com.liferay.commerce.tax.model.CommerceTaxMethod;
import com.liferay.frontend.data.set.provider.FDSActionProvider;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemListBuilder;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;

import java.util.List;

import javax.portlet.PortletRequest;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pedro Victor Silvestre
 */
@Component(
	property = "fds.data.provider.key=" + CommerceTaxRateSettingFDSNames.TAX_RATES,
	service = FDSActionProvider.class
)
public class CommerceTaxRateFDSActionProvider implements FDSActionProvider {

	@Override
	public List<DropdownItem> getDropdownItems(
			long groupId, HttpServletRequest httpServletRequest, Object model)
		throws PortalException {

		TaxRate taxRate = (TaxRate)model;

		long commerceChannelId = ParamUtil.getLong(
			httpServletRequest, "commerceChannelId");

		CommerceChannel commerceChannel =
			_commerceChannelService.getCommerceChannel(commerceChannelId);

		return DropdownItemListBuilder.add(
			() -> _commerceChannelModelResourcePermission.contains(
				PermissionThreadLocal.getPermissionChecker(), commerceChannel,
				ActionKeys.UPDATE),
			dropdownItem -> {
				dropdownItem.setHref(
					_getTaxRateEditURL(
						httpServletRequest, taxRate.getTaxRateId()));
				dropdownItem.setLabel(
					_language.get(httpServletRequest, "edit"));
				dropdownItem.setTarget("sidePanel");
			}
		).add(
			() -> _commerceChannelModelResourcePermission.contains(
				PermissionThreadLocal.getPermissionChecker(), commerceChannel,
				ActionKeys.UPDATE),
			dropdownItem -> {
				dropdownItem.setHref(
					_getTaxRateDeleteURL(
						httpServletRequest, taxRate.getTaxRateId()));
				dropdownItem.setLabel(
					_language.get(httpServletRequest, "delete"));
			}
		).build();
	}

	private String _getTaxRateDeleteURL(
		HttpServletRequest httpServletRequest, long taxRateId) {

		return PortletURLBuilder.create(
			_portal.getControlPanelPortletURL(
				httpServletRequest, CommercePortletKeys.COMMERCE_TAX_METHODS,
				PortletRequest.ACTION_PHASE)
		).setActionName(
			"/commerce_tax_methods/edit_commerce_tax_fixed_rate"
		).setCMD(
			Constants.DELETE
		).setRedirect(
			ParamUtil.getString(
				httpServletRequest, "currentUrl",
				_portal.getCurrentURL(httpServletRequest))
		).setParameter(
			"commerceTaxFixedRateId", taxRateId
		).buildString();
	}

	private String _getTaxRateEditURL(
			HttpServletRequest httpServletRequest, long taxRateId)
		throws Exception {

		return PortletURLBuilder.create(
			PortletProviderUtil.getPortletURL(
				httpServletRequest, CommerceTaxMethod.class.getName(),
				PortletProvider.Action.EDIT)
		).setMVCRenderCommandName(
			"/commerce_tax_methods/edit_commerce_tax_fixed_rate"
		).setParameter(
			"commerceTaxFixedRateId", taxRateId
		).setParameter(
			"commerceTaxMethodId",
			ParamUtil.getLong(httpServletRequest, "commerceTaxMethodId")
		).setWindowState(
			LiferayWindowState.POP_UP
		).buildString();
	}

	@Reference(
		target = "(model.class.name=com.liferay.commerce.product.model.CommerceChannel)"
	)
	private ModelResourcePermission<CommerceChannel>
		_commerceChannelModelResourcePermission;

	@Reference
	private CommerceChannelService _commerceChannelService;

	@Reference
	private Language _language;

	@Reference
	private Portal _portal;

}