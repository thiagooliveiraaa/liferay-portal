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

package com.liferay.headless.commerce.admin.pricing.internal.dto.v2_0.converter;

import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.service.CommerceCurrencyService;
import com.liferay.commerce.currency.util.CommercePriceFormatter;
import com.liferay.commerce.discount.constants.CommerceDiscountConstants;
import com.liferay.commerce.discount.model.CommerceDiscount;
import com.liferay.commerce.discount.service.CommerceDiscountService;
import com.liferay.commerce.percentage.PercentageFormatter;
import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.headless.commerce.admin.pricing.dto.v2_0.Discount;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.language.LanguageResources;
import com.liferay.portal.vulcan.dto.converter.DTOConverter;
import com.liferay.portal.vulcan.dto.converter.DTOConverterContext;

import java.math.BigDecimal;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Alberti
 */
@Component(
	property = "dto.class.name=com.liferay.commerce.discount.model.CommerceDiscount",
	service = DTOConverter.class
)
public class DiscountDTOConverter
	implements DTOConverter<CommerceDiscount, Discount> {

	@Override
	public String getContentType() {
		return Discount.class.getSimpleName();
	}

	@Override
	public Discount toDTO(DTOConverterContext dtoConverterContext)
		throws Exception {

		CommerceDiscount commerceDiscount =
			_commerceDiscountService.getCommerceDiscount(
				(Long)dtoConverterContext.getId());

		ExpandoBridge expandoBridge = commerceDiscount.getExpandoBridge();

		Locale locale = dtoConverterContext.getLocale();

		ResourceBundle resourceBundle = LanguageResources.getResourceBundle(
			locale);

		return new Discount() {
			{
				actions = dtoConverterContext.getActions();
				active = commerceDiscount.isActive();
				amountFormatted = _getAmountFormatted(commerceDiscount, locale);
				couponCode = commerceDiscount.getCouponCode();
				customFields = expandoBridge.getAttributes();
				displayDate = commerceDiscount.getDisplayDate();
				expirationDate = commerceDiscount.getExpirationDate();
				externalReferenceCode =
					commerceDiscount.getExternalReferenceCode();
				id = commerceDiscount.getCommerceDiscountId();
				level = commerceDiscount.getLevel();
				limitationTimes = commerceDiscount.getLimitationTimes();
				limitationTimesPerAccount =
					commerceDiscount.getLimitationTimesPerAccount();
				limitationType = commerceDiscount.getLimitationType();
				maximumDiscountAmount =
					commerceDiscount.getMaximumDiscountAmount();
				numberOfUse = commerceDiscount.getNumberOfUse();
				percentageLevel1 = commerceDiscount.getLevel1();
				percentageLevel2 = commerceDiscount.getLevel2();
				percentageLevel3 = commerceDiscount.getLevel3();
				percentageLevel4 = commerceDiscount.getLevel4();
				rulesConjunction = commerceDiscount.isRulesConjunction();
				target = _language.get(
					resourceBundle, commerceDiscount.getTarget());
				title = commerceDiscount.getTitle();
				useCouponCode = commerceDiscount.isUseCouponCode();
				usePercentage = commerceDiscount.isUsePercentage();
			}
		};
	}

	private BigDecimal _getAmount(CommerceDiscount commerceDiscount) {
		if (Objects.equals(
				commerceDiscount.getLevel(),
				CommerceDiscountConstants.LEVEL_L1)) {

			return commerceDiscount.getLevel1();
		}

		if (Objects.equals(
				commerceDiscount.getLevel(),
				CommerceDiscountConstants.LEVEL_L2)) {

			return commerceDiscount.getLevel2();
		}

		if (Objects.equals(
				commerceDiscount.getLevel(),
				CommerceDiscountConstants.LEVEL_L3)) {

			return commerceDiscount.getLevel3();
		}

		if (Objects.equals(
				commerceDiscount.getLevel(),
				CommerceDiscountConstants.LEVEL_L4)) {

			return commerceDiscount.getLevel4();
		}

		return BigDecimal.ZERO;
	}

	private String _getAmountFormatted(
			CommerceDiscount commerceDiscount, Locale locale)
		throws Exception {

		BigDecimal amount = _getAmount(commerceDiscount);

		if (amount == null) {
			amount = BigDecimal.ZERO;
		}

		CommerceCurrency commerceCurrency =
			_commerceCurrencyService.fetchPrimaryCommerceCurrency(
				commerceDiscount.getCompanyId());

		if (commerceDiscount.isUsePercentage()) {
			return _percentageFormatter.getLocalizedPercentage(
				locale, commerceCurrency.getMaxFractionDigits(),
				commerceCurrency.getMinFractionDigits(), amount);
		}

		return _commercePriceFormatter.format(commerceCurrency, amount, locale);
	}

	@Reference
	private CommerceCurrencyService _commerceCurrencyService;

	@Reference
	private CommerceDiscountService _commerceDiscountService;

	@Reference
	private CommercePriceFormatter _commercePriceFormatter;

	@Reference
	private Language _language;

	@Reference
	private PercentageFormatter _percentageFormatter;

}