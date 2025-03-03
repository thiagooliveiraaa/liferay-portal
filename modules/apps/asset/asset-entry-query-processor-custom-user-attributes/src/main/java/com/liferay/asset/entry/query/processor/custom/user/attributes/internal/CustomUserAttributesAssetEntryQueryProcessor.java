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

package com.liferay.asset.entry.query.processor.custom.user.attributes.internal;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.asset.kernel.service.persistence.AssetEntryQuery;
import com.liferay.asset.publisher.constants.AssetPublisherPortletKeys;
import com.liferay.asset.util.AssetEntryQueryProcessor;
import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PrimitiveLongList;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.Serializable;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.portlet.PortletPreferences;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jorge Ferrer
 */
@Component(
	property = "javax.portlet.name=" + AssetPublisherPortletKeys.ASSET_PUBLISHER,
	service = AssetEntryQueryProcessor.class
)
public class CustomUserAttributesAssetEntryQueryProcessor
	implements AssetEntryQueryProcessor {

	@Override
	public void processAssetEntryQuery(
			User user, PortletPreferences preferences,
			AssetEntryQuery assetEntryQuery)
		throws Exception {

		String customUserAttributes = GetterUtil.getString(
			preferences.getValue("customUserAttributes", StringPool.BLANK));

		_addUserAttributes(
			user, StringUtil.split(customUserAttributes), assetEntryQuery);
	}

	private void _addUserAttributes(
		User user, String[] customUserAttributeNames,
		AssetEntryQuery assetEntryQuery) {

		if ((user == null) || (customUserAttributeNames.length == 0)) {
			return;
		}

		Group companyGroup = _groupLocalService.fetchCompanyGroup(
			user.getCompanyId());

		long[] allCategoryIds = assetEntryQuery.getAllCategoryIds();

		PrimitiveLongList allCategoryIdsList = new PrimitiveLongList(
			allCategoryIds.length + customUserAttributeNames.length);

		allCategoryIdsList.addAll(allCategoryIds);

		for (String customUserAttributeName : customUserAttributeNames) {
			ExpandoBridge userCustomAttributes = user.getExpandoBridge();

			Serializable userCustomFieldValue = null;

			try {
				userCustomFieldValue = userCustomAttributes.getAttribute(
					customUserAttributeName);
			}
			catch (Exception exception) {
				if (_log.isDebugEnabled()) {
					_log.debug(exception);
				}
			}

			if (userCustomFieldValue == null) {
				continue;
			}

			if (userCustomFieldValue instanceof Map) {
				Map<Locale, String> userCustomFieldValueMap =
					(Map<Locale, String>)userCustomFieldValue;

				userCustomFieldValue = userCustomFieldValueMap.get(
					LocaleUtil.getMostRelevantLocale());
			}

			if (userCustomFieldValue == null) {
				continue;
			}

			String userCustomFieldValueString = userCustomFieldValue.toString();

			List<AssetCategory> assetCategories =
				_assetCategoryLocalService.search(
					companyGroup.getGroupId(), userCustomFieldValueString,
					new String[0], QueryUtil.ALL_POS, QueryUtil.ALL_POS);

			for (AssetCategory assetCategory : assetCategories) {
				AssetVocabulary assetVocabulary =
					_assetVocabularyLocalService.fetchAssetVocabulary(
						assetCategory.getVocabularyId());

				if (Objects.equals(
						StringUtil.toLowerCase(customUserAttributeName),
						StringUtil.toLowerCase(assetVocabulary.getName()))) {

					allCategoryIdsList.add(assetCategory.getCategoryId());
				}
			}
		}

		assetEntryQuery.setAllCategoryIds(allCategoryIdsList.getArray());
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CustomUserAttributesAssetEntryQueryProcessor.class);

	@Reference
	private AssetCategoryLocalService _assetCategoryLocalService;

	@Reference
	private AssetVocabularyLocalService _assetVocabularyLocalService;

	@Reference
	private GroupLocalService _groupLocalService;

}