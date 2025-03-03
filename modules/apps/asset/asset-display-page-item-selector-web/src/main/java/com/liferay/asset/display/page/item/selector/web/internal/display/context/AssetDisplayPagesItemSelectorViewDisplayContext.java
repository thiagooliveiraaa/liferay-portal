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

package com.liferay.asset.display.page.item.selector.web.internal.display.context;

import com.liferay.asset.display.page.item.selector.criterion.AssetDisplayPageSelectorCriterion;
import com.liferay.layout.page.template.constants.LayoutPageTemplateEntryTypeConstants;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryServiceUtil;
import com.liferay.layout.page.template.util.comparator.LayoutPageTemplateEntryCreateDateComparator;
import com.liferay.layout.page.template.util.comparator.LayoutPageTemplateEntryNameComparator;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Jürgen Kappler
 */
public class AssetDisplayPagesItemSelectorViewDisplayContext {

	public AssetDisplayPagesItemSelectorViewDisplayContext(
		HttpServletRequest httpServletRequest,
		AssetDisplayPageSelectorCriterion assetDisplayPageSelectorCriterion,
		PortletURL portletURL) {

		_httpServletRequest = httpServletRequest;
		_assetDisplayPageSelectorCriterion = assetDisplayPageSelectorCriterion;
		_portletURL = portletURL;

		_portletRequest = (PortletRequest)httpServletRequest.getAttribute(
			JavaConstants.JAVAX_PORTLET_REQUEST);
		_portletResponse = (PortletResponse)httpServletRequest.getAttribute(
			JavaConstants.JAVAX_PORTLET_RESPONSE);
		_themeDisplay = (ThemeDisplay)httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public SearchContainer<LayoutPageTemplateEntry>
			getAssetDisplayPageSearchContainer()
		throws PortletException {

		if (_assetDisplayPageSearchContainer != null) {
			return _assetDisplayPageSearchContainer;
		}

		SearchContainer<LayoutPageTemplateEntry>
			assetDisplayPageSearchContainer = new SearchContainer<>(
				_portletRequest, _getPortletURL(), null,
				"there-are-no-display-page-templates");

		assetDisplayPageSearchContainer.setOrderByCol(_getOrderByCol());
		assetDisplayPageSearchContainer.setOrderByComparator(
			_getLayoutPageTemplateEntryOrderByComparator(
				_getOrderByCol(), getOrderByType()));
		assetDisplayPageSearchContainer.setOrderByType(getOrderByType());

		if (Validator.isNotNull(_getKeywords())) {
			assetDisplayPageSearchContainer.setResultsAndTotal(
				() ->
					LayoutPageTemplateEntryServiceUtil.
						getLayoutPageTemplateEntries(
							_getGroupId(),
							_assetDisplayPageSelectorCriterion.getClassNameId(),
							_assetDisplayPageSelectorCriterion.getClassTypeId(),
							_getKeywords(),
							LayoutPageTemplateEntryTypeConstants.
								TYPE_DISPLAY_PAGE,
							WorkflowConstants.STATUS_APPROVED,
							assetDisplayPageSearchContainer.getStart(),
							assetDisplayPageSearchContainer.getEnd(),
							assetDisplayPageSearchContainer.
								getOrderByComparator()),
				LayoutPageTemplateEntryServiceUtil.
					getLayoutPageTemplateEntriesCount(
						_getGroupId(),
						_assetDisplayPageSelectorCriterion.getClassNameId(),
						_assetDisplayPageSelectorCriterion.getClassTypeId(),
						_getKeywords(),
						LayoutPageTemplateEntryTypeConstants.TYPE_DISPLAY_PAGE,
						WorkflowConstants.STATUS_APPROVED));
		}
		else {
			assetDisplayPageSearchContainer.setResultsAndTotal(
				() ->
					LayoutPageTemplateEntryServiceUtil.
						getLayoutPageTemplateEntries(
							_getGroupId(),
							_assetDisplayPageSelectorCriterion.getClassNameId(),
							_assetDisplayPageSelectorCriterion.getClassTypeId(),
							LayoutPageTemplateEntryTypeConstants.
								TYPE_DISPLAY_PAGE,
							WorkflowConstants.STATUS_APPROVED,
							assetDisplayPageSearchContainer.getStart(),
							assetDisplayPageSearchContainer.getEnd(),
							assetDisplayPageSearchContainer.
								getOrderByComparator()),
				LayoutPageTemplateEntryServiceUtil.
					getLayoutPageTemplateEntriesCount(
						_getGroupId(),
						_assetDisplayPageSelectorCriterion.getClassNameId(),
						_assetDisplayPageSelectorCriterion.getClassTypeId(),
						LayoutPageTemplateEntryTypeConstants.TYPE_DISPLAY_PAGE,
						WorkflowConstants.STATUS_APPROVED));
		}

		_assetDisplayPageSearchContainer = assetDisplayPageSearchContainer;

		return _assetDisplayPageSearchContainer;
	}

	public String getOrderByType() {
		if (Validator.isNotNull(_orderByType)) {
			return _orderByType;
		}

		_orderByType = ParamUtil.getString(
			_httpServletRequest, "orderByType", "asc");

		return _orderByType;
	}

	private long _getGroupId() {
		if (_groupId != null) {
			return _groupId;
		}

		_groupId = ParamUtil.getLong(
			_httpServletRequest, "groupId", _themeDisplay.getScopeGroupId());

		return _groupId;
	}

	private String _getKeywords() {
		if (Validator.isNotNull(_keywords)) {
			return _keywords;
		}

		_keywords = ParamUtil.getString(_httpServletRequest, "keywords");

		return _keywords;
	}

	private OrderByComparator<LayoutPageTemplateEntry>
		_getLayoutPageTemplateEntryOrderByComparator(
			String orderByCol, String orderByType) {

		boolean orderByAsc = false;

		if (orderByType.equals("asc")) {
			orderByAsc = true;
		}

		OrderByComparator<LayoutPageTemplateEntry> orderByComparator = null;

		if (orderByCol.equals("create-date")) {
			orderByComparator = new LayoutPageTemplateEntryCreateDateComparator(
				orderByAsc);
		}
		else if (orderByCol.equals("name")) {
			orderByComparator = new LayoutPageTemplateEntryNameComparator(
				orderByAsc);
		}

		return orderByComparator;
	}

	private String _getOrderByCol() {
		if (Validator.isNotNull(_orderByCol)) {
			return _orderByCol;
		}

		_orderByCol = ParamUtil.getString(
			_httpServletRequest, "orderByCol", "create-date");

		return _orderByCol;
	}

	private PortletURL _getPortletURL() throws PortletException {
		return PortletURLBuilder.create(
			PortletURLUtil.clone(
				_portletURL,
				PortalUtil.getLiferayPortletResponse(_portletResponse))
		).setParameter(
			"orderByCol", _getOrderByCol()
		).setParameter(
			"orderByType", getOrderByType()
		).buildPortletURL();
	}

	private SearchContainer<LayoutPageTemplateEntry>
		_assetDisplayPageSearchContainer;
	private final AssetDisplayPageSelectorCriterion
		_assetDisplayPageSelectorCriterion;
	private Long _groupId;
	private final HttpServletRequest _httpServletRequest;
	private String _keywords;
	private String _orderByCol;
	private String _orderByType;
	private final PortletRequest _portletRequest;
	private final PortletResponse _portletResponse;
	private final PortletURL _portletURL;
	private final ThemeDisplay _themeDisplay;

}