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

package com.liferay.commerce.product.content.search.web.internal.portlet.shared.search;

import com.liferay.account.model.AccountEntry;
import com.liferay.account.service.AccountGroupLocalService;
import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.commerce.product.constants.CPField;
import com.liferay.commerce.product.constants.CPPortletKeys;
import com.liferay.commerce.product.content.search.web.internal.util.CPOptionFacetsUtil;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPOption;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CPOptionLocalService;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.search.facet.SerializableFacet;
import com.liferay.commerce.util.CommerceAccountHelper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.search.facet.MultiValueFacet;
import com.liferay.portal.kernel.search.facet.SimpleFacet;
import com.liferay.portal.kernel.search.facet.collector.FacetCollector;
import com.liferay.portal.kernel.search.facet.collector.TermCollector;
import com.liferay.portal.kernel.search.facet.config.FacetConfiguration;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.search.web.portlet.shared.search.PortletSharedSearchContributor;
import com.liferay.portal.search.web.portlet.shared.search.PortletSharedSearchSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Shuyang Zhou
 */
@Component(
	property = "javax.portlet.name=" + CPPortletKeys.CP_OPTION_FACETS,
	service = PortletSharedSearchContributor.class
)
public class CPOptionFacetsPortletSharedSearchContributor
	implements PortletSharedSearchContributor {

	@Override
	public void contribute(
		PortletSharedSearchSettings portletSharedSearchSettings) {

		try {
			SearchContext searchContext =
				portletSharedSearchSettings.getSearchContext();

			SerializableFacet serializableFacet = new SerializableFacet(
				CPField.OPTION_NAMES, searchContext);

			String[] parameterValues =
				portletSharedSearchSettings.getParameterValues(
					CPField.OPTION_NAMES);

			if (ArrayUtil.isNotEmpty(parameterValues)) {
				serializableFacet.select(parameterValues);

				searchContext.setAttribute(
					CPField.OPTION_NAMES, parameterValues);
			}

			RenderRequest renderRequest =
				portletSharedSearchSettings.getRenderRequest();

			ThemeDisplay themeDisplay =
				(ThemeDisplay)renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

			int frequencyThreshold = 1;
			int maxTerms = 10;

			Optional<PortletPreferences> portletPreferencesOptional =
				portletSharedSearchSettings.getPortletPreferencesOptional();

			if (portletPreferencesOptional.isPresent()) {
				PortletPreferences portletPreferences =
					portletPreferencesOptional.get();

				frequencyThreshold = GetterUtil.getInteger(
					portletPreferences.getValue("frequencyThreshold", null),
					frequencyThreshold);
				maxTerms = GetterUtil.getInteger(
					portletPreferences.getValue("maxTerms", null), maxTerms);
			}

			serializableFacet.setFacetConfiguration(
				buildFacetConfiguration(
					frequencyThreshold, maxTerms, serializableFacet));

			portletSharedSearchSettings.addFacet(serializableFacet);

			for (Facet facet : getFacets(renderRequest)) {
				String cpOptionKey =
					CPOptionFacetsUtil.getCPOptionKeyFromIndexFieldName(
						facet.getFieldName());

				parameterValues =
					portletSharedSearchSettings.getParameterValues(cpOptionKey);

				serializableFacet = new SerializableFacet(
					facet.getFieldName(), searchContext);

				serializableFacet.setFacetConfiguration(
					buildFacetConfiguration(
						frequencyThreshold, maxTerms, serializableFacet));

				if (ArrayUtil.isNotEmpty(parameterValues)) {
					serializableFacet.select(parameterValues);

					searchContext.setAttribute(
						facet.getFieldName(), parameterValues);
				}

				portletSharedSearchSettings.addFacet(serializableFacet);
			}

			long commerceChannelGroupId = 0;

			CommerceChannel commerceChannel =
				_commerceChannelLocalService.fetchCommerceChannelBySiteGroupId(
					themeDisplay.getScopeGroupId());

			if (commerceChannel != null) {
				commerceChannelGroupId = commerceChannel.getGroupId();
			}

			if (commerceChannelGroupId > 0) {
				searchContext.setAttribute(
					CPField.COMMERCE_CHANNEL_GROUP_ID, commerceChannelGroupId);
				searchContext.setAttribute("secure", Boolean.TRUE);

				AccountEntry accountEntry =
					_commerceAccountHelper.getCurrentAccountEntry(
						commerceChannelGroupId, themeDisplay.getRequest());

				long[] commerceAccountGroupIds = null;

				if (accountEntry != null) {
					commerceAccountGroupIds =
						_accountGroupLocalService.getAccountGroupIds(
							accountEntry.getAccountEntryId());
				}

				searchContext.setAttribute(
					"commerceAccountGroupIds", commerceAccountGroupIds);
			}
		}
		catch (Exception exception) {
			_log.error(exception);
		}
	}

	protected FacetConfiguration buildFacetConfiguration(
		int frequencyThreshold, int maxTerms,
		SerializableFacet serializableFacet) {

		FacetConfiguration facetConfiguration = new FacetConfiguration();

		facetConfiguration.setFieldName(serializableFacet.getFieldName());

		JSONObject jsonObject = facetConfiguration.getData();

		jsonObject.put(
			"frequencyThreshold", frequencyThreshold
		).put(
			"maxTerms", maxTerms
		);

		return facetConfiguration;
	}

	protected SearchContext buildSearchContext(RenderRequest renderRequest)
		throws PortalException {

		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		SearchContext searchContext = new SearchContext();

		searchContext.setCompanyId(themeDisplay.getCompanyId());
		searchContext.setLayout(themeDisplay.getLayout());
		searchContext.setLocale(themeDisplay.getLocale());
		searchContext.setTimeZone(themeDisplay.getTimeZone());
		searchContext.setUserId(themeDisplay.getUserId());

		QueryConfig queryConfig = searchContext.getQueryConfig();

		queryConfig.setLocale(themeDisplay.getLocale());

		searchContext.setAttribute(CPField.PUBLISHED, Boolean.TRUE);

		CommerceChannel commerceChannel =
			_commerceChannelLocalService.fetchCommerceChannelBySiteGroupId(
				themeDisplay.getScopeGroupId());

		if (commerceChannel != null) {
			searchContext.setAttribute(
				"commerceChannelGroupId", commerceChannel.getGroupId());

			AccountEntry accountEntry =
				_commerceAccountHelper.getCurrentAccountEntry(
					commerceChannel.getGroupId(),
					_portal.getHttpServletRequest(renderRequest));

			if (accountEntry != null) {
				searchContext.setAttribute(
					"commerceAccountGroupIds",
					_accountGroupLocalService.getAccountGroupIds(
						accountEntry.getAccountEntryId()));
			}
		}

		searchContext.setAttribute("secure", Boolean.TRUE);

		return searchContext;
	}

	protected List<Facet> getFacets(RenderRequest renderRequest)
		throws PortalException {

		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		List<Facet> facets = new ArrayList<>();

		AssetCategory assetCategory = (AssetCategory)renderRequest.getAttribute(
			WebKeys.ASSET_CATEGORY);

		SearchContext searchContext = buildSearchContext(renderRequest);

		if (assetCategory != null) {
			searchContext.setAssetCategoryIds(
				new long[] {assetCategory.getCategoryId()});
		}

		Facet facet = new SimpleFacet(searchContext);

		facet.setFieldName(CPField.OPTION_NAMES);

		searchContext.addFacet(facet);

		QueryConfig queryConfig = searchContext.getQueryConfig();

		queryConfig.addSelectedFieldNames(CPField.OPTION_NAMES);

		queryConfig.setHighlightEnabled(false);
		queryConfig.setScoreEnabled(false);

		Indexer<CPDefinition> indexer = IndexerRegistryUtil.nullSafeGetIndexer(
			CPDefinition.class);

		indexer.search(searchContext);

		FacetCollector facetCollector = facet.getFacetCollector();

		for (TermCollector termCollector : facetCollector.getTermCollectors()) {
			CPOption cpOption = _cpOptionLocalService.getCPOption(
				searchContext.getCompanyId(), termCollector.getTerm());

			if (cpOption.isFacetable()) {
				MultiValueFacet multiValueFacet = new MultiValueFacet(
					searchContext);

				multiValueFacet.setFieldName(
					CPOptionFacetsUtil.getIndexFieldName(
						termCollector.getTerm(), themeDisplay.getLanguageId()));

				facets.add(multiValueFacet);
			}
		}

		return facets;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CPOptionFacetsPortletSharedSearchContributor.class);

	@Reference
	private AccountGroupLocalService _accountGroupLocalService;

	@Reference
	private CommerceAccountHelper _commerceAccountHelper;

	@Reference
	private CommerceChannelLocalService _commerceChannelLocalService;

	@Reference
	private CPOptionLocalService _cpOptionLocalService;

	@Reference
	private Portal _portal;

}