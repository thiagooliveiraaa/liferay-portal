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

package com.liferay.layout.type.controller.collection.internal.display.context;

import com.liferay.asset.list.asset.entry.provider.AssetListAssetEntryProvider;
import com.liferay.asset.list.model.AssetListEntry;
import com.liferay.asset.list.service.AssetListEntryLocalService;
import com.liferay.asset.util.AssetPublisherAddItemHolder;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.info.collection.provider.CollectionQuery;
import com.liferay.info.collection.provider.InfoCollectionProvider;
import com.liferay.info.item.InfoItemServiceRegistry;
import com.liferay.info.list.provider.item.selector.criterion.InfoListProviderItemSelectorReturnType;
import com.liferay.info.pagination.InfoPage;
import com.liferay.item.selector.criteria.InfoListItemSelectorReturnType;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.LiferayRenderRequest;
import com.liferay.portal.kernel.portlet.LiferayRenderResponse;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.segments.constants.SegmentsEntryConstants;

import java.util.List;
import java.util.Objects;

import javax.portlet.PortletURL;
import javax.portlet.WindowStateException;

/**
 * @author Jürgen Kappler
 */
public class CollectionItemsDetailDisplayContext {

	public CollectionItemsDetailDisplayContext(
		AssetListEntryLocalService assetListEntryLocalService,
		AssetListAssetEntryProvider assetListAssetEntryProvider,
		InfoItemServiceRegistry infoItemServiceRegistry,
		LiferayRenderRequest liferayRenderRequest,
		LiferayRenderResponse liferayRenderResponse,
		ThemeDisplay themeDisplay) {

		_assetListEntryLocalService = assetListEntryLocalService;
		_assetListAssetEntryProvider = assetListAssetEntryProvider;
		_infoItemServiceRegistry = infoItemServiceRegistry;
		_liferayRenderRequest = liferayRenderRequest;
		_liferayRenderResponse = liferayRenderResponse;
		_themeDisplay = themeDisplay;
	}

	public long getCollectionItemsCount() {
		Layout layout = _themeDisplay.getLayout();

		String collectionPK = layout.getTypeSettingsProperty("collectionPK");
		String collectionType = layout.getTypeSettingsProperty(
			"collectionType");

		if (Validator.isNull(collectionType) ||
			Validator.isNull(collectionPK)) {

			return 0;
		}

		if (Objects.equals(
				collectionType,
				InfoListProviderItemSelectorReturnType.class.getName())) {

			return _getInfoCollectionProviderItemCount(collectionPK);
		}
		else if (Objects.equals(
					collectionType,
					InfoListItemSelectorReturnType.class.getName())) {

			return _getAssetListEntryItemCount(collectionPK);
		}

		return 0;
	}

	public List<DropdownItem> getDropdownItems(
		List<AssetPublisherAddItemHolder> assetPublisherAddItemHolders) {

		return new DropdownItemList() {
			{
				for (AssetPublisherAddItemHolder assetPublisherAddItemHolder :
						assetPublisherAddItemHolders) {

					add(
						dropdownItem -> {
							dropdownItem.setHref(
								String.valueOf(
									assetPublisherAddItemHolder.
										getPortletURL()));
							dropdownItem.setLabel(
								assetPublisherAddItemHolder.getModelResource());
						});
				}
			}
		};
	}

	public String getNamespace() {
		return _liferayRenderResponse.getNamespace();
	}

	public String getViewCollectionItemsURL()
		throws PortalException, WindowStateException {

		PortletURL portletURL = PortletProviderUtil.getPortletURL(
			_liferayRenderRequest, AssetListEntry.class.getName(),
			PortletProvider.Action.BROWSE);

		if (portletURL == null) {
			return StringPool.BLANK;
		}

		Layout layout = _themeDisplay.getLayout();

		String collectionPK = layout.getTypeSettingsProperty("collectionPK");
		String collectionType = layout.getTypeSettingsProperty(
			"collectionType");

		if (Validator.isNull(collectionType) ||
			Validator.isNull(collectionPK)) {

			return StringPool.BLANK;
		}

		portletURL.setParameter("redirect", _themeDisplay.getURLCurrent());
		portletURL.setParameter("collectionPK", collectionPK);
		portletURL.setParameter("collectionType", collectionType);
		portletURL.setParameter("showActions", String.valueOf(Boolean.TRUE));

		portletURL.setWindowState(LiferayWindowState.POP_UP);

		return portletURL.toString();
	}

	private long _getAssetListEntryItemCount(String classPK) {
		AssetListEntry assetListEntry =
			_assetListEntryLocalService.fetchAssetListEntry(
				GetterUtil.getLong(classPK));

		if (assetListEntry == null) {
			return 0;
		}

		return _assetListAssetEntryProvider.getAssetEntriesCount(
			assetListEntry, new long[] {SegmentsEntryConstants.ID_DEFAULT},
			null, null, StringPool.BLANK, StringPool.BLANK);
	}

	private long _getInfoCollectionProviderItemCount(String collectionPK) {
		for (InfoCollectionProvider<?> infoCollectionProvider :
				(List<InfoCollectionProvider<?>>)
					(List<?>)_infoItemServiceRegistry.getAllInfoItemServices(
						InfoCollectionProvider.class)) {

			if (!Objects.equals(
					infoCollectionProvider.getKey(), collectionPK)) {

				continue;
			}

			InfoPage<?> infoPage = infoCollectionProvider.getCollectionInfoPage(
				new CollectionQuery());

			return infoPage.getTotalCount();
		}

		return 0;
	}

	private final AssetListAssetEntryProvider _assetListAssetEntryProvider;
	private final AssetListEntryLocalService _assetListEntryLocalService;
	private final InfoItemServiceRegistry _infoItemServiceRegistry;
	private final LiferayRenderRequest _liferayRenderRequest;
	private final LiferayRenderResponse _liferayRenderResponse;
	private final ThemeDisplay _themeDisplay;

}