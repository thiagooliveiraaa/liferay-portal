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

package com.liferay.headless.commerce.delivery.catalog.internal.dto.v1_0.converter;

import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPDefinitionSpecificationOptionValue;
import com.liferay.commerce.product.model.CPOptionCategory;
import com.liferay.commerce.product.model.CPSpecificationOption;
import com.liferay.commerce.product.service.CPDefinitionSpecificationOptionValueLocalService;
import com.liferay.headless.commerce.delivery.catalog.dto.v1_0.ProductSpecification;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.vulcan.dto.converter.DTOConverter;
import com.liferay.portal.vulcan.dto.converter.DTOConverterContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Andrea Sbarra
 */
@Component(
	property = "dto.class.name=CPDefinitionSpecificationOptionValue",
	service = DTOConverter.class
)
public class ProductSpecificationDTOConverter
	implements DTOConverter
		<CPDefinitionSpecificationOptionValue, ProductSpecification> {

	@Override
	public String getContentType() {
		return ProductSpecification.class.getSimpleName();
	}

	@Override
	public ProductSpecification toDTO(DTOConverterContext dtoConverterContext)
		throws Exception {

		CPDefinitionSpecificationOptionValue
			cpDefinitionSpecificationOptionValue =
				_cpDefinitionSpecificationOptionValueLocalService.
					getCPDefinitionSpecificationOptionValue(
						(Long)dtoConverterContext.getId());

		CPDefinition cpDefinition =
			cpDefinitionSpecificationOptionValue.getCPDefinition();

		CPSpecificationOption cpSpecificationOption =
			cpDefinitionSpecificationOptionValue.getCPSpecificationOption();

		CPOptionCategory cpOptionCategory =
			cpSpecificationOption.getCPOptionCategory();

		String languageId = _language.getLanguageId(
			dtoConverterContext.getLocale());

		return new ProductSpecification() {
			{
				id =
					cpDefinitionSpecificationOptionValue.
						getCPDefinitionSpecificationOptionValueId();
				optionCategoryId =
					cpDefinitionSpecificationOptionValue.
						getCPOptionCategoryId();
				priority = cpDefinitionSpecificationOptionValue.getPriority();
				productId = cpDefinition.getCProductId();
				specificationGroupKey = cpOptionCategory.getKey();
				specificationGroupTitle = cpOptionCategory.getTitle(languageId);
				specificationId =
					cpSpecificationOption.getCPSpecificationOptionId();
				specificationKey = cpSpecificationOption.getKey();
				specificationTitle = cpSpecificationOption.getTitle(languageId);
				value = cpDefinitionSpecificationOptionValue.getValue(
					languageId);
			}
		};
	}

	@Reference
	private CPDefinitionSpecificationOptionValueLocalService
		_cpDefinitionSpecificationOptionValueLocalService;

	@Reference
	private Language _language;

}