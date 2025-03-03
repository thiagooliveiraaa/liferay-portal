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

package com.liferay.content.dashboard.web.internal.display.context;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetTag;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.content.dashboard.item.action.exception.ContentDashboardItemActionException;
import com.liferay.content.dashboard.item.filter.ContentDashboardItemFilter;
import com.liferay.content.dashboard.item.filter.provider.ContentDashboardItemFilterProvider;
import com.liferay.content.dashboard.item.type.ContentDashboardItemSubtype;
import com.liferay.content.dashboard.web.internal.item.filter.ContentDashboardItemFilterProviderRegistry;
import com.liferay.frontend.taglib.clay.servlet.taglib.display.context.SearchContainerManagementToolbarDisplayContext;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemBuilder;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemListBuilder;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.LabelItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.LabelItemListBuilder;
import com.liferay.info.item.InfoItemReference;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.petra.string.StringUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import javax.portlet.PortletException;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Cristina González Castellano
 */
public class ContentDashboardAdminManagementToolbarDisplayContext
	extends SearchContainerManagementToolbarDisplayContext {

	public ContentDashboardAdminManagementToolbarDisplayContext(
		AssetCategoryLocalService assetCategoryLocalService,
		AssetVocabularyLocalService assetVocabularyLocalService,
		ContentDashboardAdminDisplayContext contentDashboardAdminDisplayContext,
		ContentDashboardItemFilterProviderRegistry
			contentDashboardItemFilterProviderRegistry,
		GroupLocalService groupLocalService,
		HttpServletRequest httpServletRequest, Language language,
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse, Locale locale,
		UserLocalService userLocalService) {

		super(
			httpServletRequest, liferayPortletRequest, liferayPortletResponse,
			contentDashboardAdminDisplayContext.getSearchContainer());

		_assetCategoryLocalService = assetCategoryLocalService;
		_assetVocabularyLocalService = assetVocabularyLocalService;
		_contentDashboardAdminDisplayContext =
			contentDashboardAdminDisplayContext;
		_contentDashboardItemFilterProviderRegistry =
			contentDashboardItemFilterProviderRegistry;
		_groupLocalService = groupLocalService;
		_language = language;
		_liferayPortletRequest = liferayPortletRequest;
		_liferayPortletResponse = liferayPortletResponse;
		_locale = locale;
		_userLocalService = userLocalService;
	}

	@Override
	public String getClearResultsURL() {
		PortletURLBuilder.AfterParameterStep afterParameterStep =
			PortletURLBuilder.create(
				getPortletURL()
			).setKeywords(
				StringPool.BLANK
			).setParameter(
				"assetCategoryId", (String)null
			).setParameter(
				"assetTagId", (String)null
			).setParameter(
				"authorIds", (String)null
			).setParameter(
				"contentDashboardItemSubtypePayload", (String)null
			).setParameter(
				"scopeId", (String)null
			).setParameter(
				"status", WorkflowConstants.STATUS_ANY
			);

		List<ContentDashboardItemFilterProvider>
			contentDashboardItemFilterProviders =
				_contentDashboardItemFilterProviderRegistry.
					getContentDashboardItemFilterProviders();

		try {
			for (ContentDashboardItemFilterProvider
					contentDashboardItemFilterProvider :
						contentDashboardItemFilterProviders) {

				ContentDashboardItemFilter contentDashboardItemFilter =
					contentDashboardItemFilterProvider.
						getContentDashboardItemFilter(
							_liferayPortletRequest.getHttpServletRequest());

				afterParameterStep.setParameter(
					contentDashboardItemFilter.getParameterName(),
					(String)null);
			}
		}
		catch (ContentDashboardItemActionException
					contentDashboardItemActionException) {

			_log.error(contentDashboardItemActionException);
		}

		return afterParameterStep.buildString();
	}

	@Override
	public List<DropdownItem> getFilterDropdownItems() {
		return DropdownItemListBuilder.addGroup(
			dropdownGroupItem -> {
				dropdownGroupItem.setDropdownItems(_getFilterDropdownItems());
				dropdownGroupItem.setLabel(
					_language.get(httpServletRequest, "filter-by") +
						StringPool.TRIPLE_PERIOD);
			}
		).addGroup(
			dropdownGroupItem -> {
				dropdownGroupItem.setDropdownItems(
					_getFilterAuthorDropdownItems());
				dropdownGroupItem.setLabel(
					_language.get(httpServletRequest, "filter-by-author"));
			}
		).addGroup(
			dropdownGroupItem -> {
				dropdownGroupItem.setDropdownItems(
					_getFilterStatusDropdownItems());
				dropdownGroupItem.setLabel(
					_language.get(httpServletRequest, "filter-by-status"));
			}
		).addGroup(
			dropdownGroupItem -> {
				dropdownGroupItem.setDropdownItems(
					_getFilterByReviewDateDropdownItems());
				dropdownGroupItem.setLabel(
					_language.get(httpServletRequest, "filter-by-review-date"));
			}
		).addGroup(
			dropdownGroupItem -> {
				dropdownGroupItem.setDropdownItems(getOrderByDropdownItems());
				dropdownGroupItem.setLabel(getOrderByDropdownItemsLabel());
			}
		).build();
	}

	@Override
	public List<LabelItem> getFilterLabelItems() {
		List<Long> assetCategoryIds =
			_contentDashboardAdminDisplayContext.getAssetCategoryIds();

		LabelItemListBuilder.LabelItemListWrapper labelItemListWrapper =
			new LabelItemListBuilder.LabelItemListWrapper();

		_addContentDashboardItemFilterProviders(labelItemListWrapper);

		for (Long assetCategoryId : assetCategoryIds) {
			labelItemListWrapper.add(
				labelItem -> {
					labelItem.putData(
						"removeLabelURL",
						_getRemoveLabelURL(
							"assetCategoryId",
							() -> TransformUtil.transformToArray(
								assetCategoryIds,
								curAssetCategoryId -> {
									if (Objects.equals(
											assetCategoryId,
											curAssetCategoryId)) {

										return null;
									}

									return String.valueOf(curAssetCategoryId);
								},
								String.class)));
					labelItem.setCloseable(true);

					String title = StringPool.BLANK;

					AssetCategory assetCategory =
						_assetCategoryLocalService.fetchAssetCategory(
							assetCategoryId);

					if (assetCategory != null) {
						title = assetCategory.getTitle(_locale);
					}

					labelItem.setLabel(_getLabel("category", title));
				});
		}

		long scopeId = _contentDashboardAdminDisplayContext.getScopeId();

		labelItemListWrapper.add(
			() -> scopeId > 0,
			labelItem -> {
				labelItem.putData(
					"removeLabelURL",
					_getRemoveLabelURL("scopeId", (String)null));
				labelItem.setCloseable(true);
				labelItem.setLabel(
					_getLabel(
						"site-or-asset-library", _getScopeLabel(scopeId)));
			});

		List<? extends ContentDashboardItemSubtype>
			contentDashboardItemSubtypes =
				_contentDashboardAdminDisplayContext.
					getContentDashboardItemSubtypes();

		for (ContentDashboardItemSubtype contentDashboardItemSubtype :
				contentDashboardItemSubtypes) {

			labelItemListWrapper.add(
				labelItem -> {
					labelItem.putData(
						"removeLabelURL",
						_getRemoveContentDashboardItemSubtypePayloadsURL(
							contentDashboardItemSubtypes,
							contentDashboardItemSubtype));
					labelItem.setCloseable(true);
					labelItem.setLabel(
						_getLabel(
							"subtype",
							contentDashboardItemSubtype.getFullLabel(_locale)));
				});
		}

		List<Long> authorIds =
			_contentDashboardAdminDisplayContext.getAuthorIds();

		for (Long authorId : authorIds) {
			labelItemListWrapper.add(
				labelItem -> {
					labelItem.putData(
						"removeLabelURL",
						_getRemoveLabelURL(
							"authorIds",
							() -> TransformUtil.transformToArray(
								authorIds,
								curAuthorId -> {
									if (Objects.equals(authorId, curAuthorId)) {
										return null;
									}

									return String.valueOf(curAuthorId);
								},
								String.class)));
					labelItem.setCloseable(true);

					String fullName = StringPool.BLANK;

					User user = _userLocalService.fetchUser(authorId);

					if (user != null) {
						fullName = user.getFullName();
					}

					labelItem.setLabel(
						_getLabel(
							"author",
							_language.get(httpServletRequest, fullName)));
				});
		}

		int status = _contentDashboardAdminDisplayContext.getStatus();

		labelItemListWrapper.add(
			() -> status != WorkflowConstants.STATUS_ANY,
			labelItem -> {
				labelItem.putData(
					"removeLabelURL",
					_getRemoveLabelURL("status", (String)null));
				labelItem.setCloseable(true);
				labelItem.setLabel(
					_getLabel("status", _getStatusLabel(status)));
			});

		labelItemListWrapper.add(
			() -> Validator.isNotNull(
				_contentDashboardAdminDisplayContext.getReviewDateString()),
			labelItem -> {
				labelItem.putData(
					"removeLabelURL",
					_getRemoveLabelURL("reviewDate", (String)null));
				labelItem.setCloseable(true);
				labelItem.setLabel(
					_getLabel(
						"review-date",
						_language.get(httpServletRequest, "to-be-reviewed")));
			});

		Set<String> assetTagIds =
			_contentDashboardAdminDisplayContext.getAssetTagIds();

		for (String assetTagId : assetTagIds) {
			labelItemListWrapper.add(
				labelItem -> {
					labelItem.putData(
						"removeLabelURL",
						_getRemoveLabelURL(
							"assetTagId",
							() -> TransformUtil.transformToArray(
								assetTagIds,
								curAssetTagId -> {
									if (Objects.equals(
											assetTagId, curAssetTagId)) {

										return null;
									}

									return curAssetTagId;
								},
								String.class)));
					labelItem.setCloseable(true);
					labelItem.setLabel(_getLabel("tag", assetTagId));
				});
		}

		return labelItemListWrapper.build();
	}

	@Override
	public PortletURL getPortletURL() {
		try {
			return PortletURLUtil.clone(currentURLObj, liferayPortletResponse);
		}
		catch (Exception exception) {
			if (_log.isWarnEnabled()) {
				_log.warn(exception);
			}

			return liferayPortletResponse.createRenderURL();
		}
	}

	@Override
	public String getSearchActionURL() {
		PortletURL portletURL = liferayPortletResponse.createRenderURL();

		List<Long> assetCategoryIds =
			_contentDashboardAdminDisplayContext.getAssetCategoryIds();

		if (ListUtil.isNotEmpty(assetCategoryIds)) {
			portletURL.setParameter(
				"assetCategoryId",
				TransformUtil.transformToArray(
					assetCategoryIds, String::valueOf, String.class));
		}

		Set<String> assetTagIds =
			_contentDashboardAdminDisplayContext.getAssetTagIds();

		if (SetUtil.isNotEmpty(assetTagIds)) {
			portletURL.setParameter(
				"assetTagId", assetTagIds.toArray(new String[0]));
		}

		List<Long> authorIds =
			_contentDashboardAdminDisplayContext.getAuthorIds();

		if (ListUtil.isNotEmpty(authorIds)) {
			portletURL.setParameter(
				"authorIds",
				TransformUtil.transformToArray(
					authorIds, String::valueOf, String.class));
		}

		List<? extends ContentDashboardItemSubtype>
			contentDashboardItemSubtypes =
				_contentDashboardAdminDisplayContext.
					getContentDashboardItemSubtypes();

		if (ListUtil.isNotEmpty(contentDashboardItemSubtypes)) {
			portletURL.setParameter(
				"contentDashboardItemSubtypePayload",
				TransformUtil.transformToArray(
					contentDashboardItemSubtypes,
					contentDashboardItemSubtype ->
						contentDashboardItemSubtype.toJSONString(_locale),
					String.class));
		}

		portletURL.setParameter("orderByCol", getOrderByCol());
		portletURL.setParameter("orderByType", getOrderByType());
		portletURL.setParameter(
			"reviewDate",
			_contentDashboardAdminDisplayContext.getReviewDateString());
		portletURL.setParameter(
			"scopeId",
			String.valueOf(_contentDashboardAdminDisplayContext.getScopeId()));
		portletURL.setParameter(
			"status",
			String.valueOf(_contentDashboardAdminDisplayContext.getStatus()));

		return String.valueOf(portletURL);
	}

	@Override
	public String getSearchContainerId() {
		return "content";
	}

	@Override
	public Boolean isSelectable() {
		return false;
	}

	@Override
	protected String[] getOrderByKeys() {
		return new String[] {"title", "modified-date", "review-date"};
	}

	private void _addContentDashboardItemFilterProviders(
		LabelItemListBuilder.LabelItemListWrapper labelItemListWrapper) {

		List<ContentDashboardItemFilterProvider>
			contentDashboardItemFilterProviders =
				_contentDashboardItemFilterProviderRegistry.
					getContentDashboardItemFilterProviders();

		for (ContentDashboardItemFilterProvider
				contentDashboardItemFilterProvider :
					contentDashboardItemFilterProviders) {

			try {
				ContentDashboardItemFilter contentDashboardItemFilter =
					contentDashboardItemFilterProvider.
						getContentDashboardItemFilter(
							_liferayPortletRequest.getHttpServletRequest());

				List<String> parameterValues =
					contentDashboardItemFilter.getParameterValues();

				for (String parameterValue : parameterValues) {
					labelItemListWrapper.add(
						labelItem -> {
							labelItem.putData(
								"removeLabelURL",
								_getRemoveLabelURL(
									contentDashboardItemFilter.
										getParameterName(),
									() -> TransformUtil.transformToArray(
										parameterValues,
										curParameterValue -> {
											if (Objects.equals(
													parameterValue,
													curParameterValue)) {

												return null;
											}

											return curParameterValue;
										},
										String.class)));
							labelItem.setCloseable(true);
							labelItem.setLabel(
								_getLabel(
									contentDashboardItemFilter.
										getParameterLabel(_locale),
									parameterValue));
						});
				}
			}
			catch (ContentDashboardItemActionException
						contentDashboardItemActionException) {

				_log.error(contentDashboardItemActionException);
			}
		}
	}

	private PortletURL _getAssetCategorySelectorURL() throws PortalException {
		return PortletURLBuilder.create(
			PortletProviderUtil.getPortletURL(
				_liferayPortletRequest, AssetCategory.class.getName(),
				PortletProvider.Action.BROWSE)
		).setParameter(
			"eventName",
			_liferayPortletResponse.getNamespace() + "selectedAssetCategory"
		).setParameter(
			"selectedCategories",
			StringUtil.merge(
				_contentDashboardAdminDisplayContext.getAssetCategoryIds(),
				StringPool.COMMA)
		).setParameter(
			"showSelectedCounter", true
		).setParameter(
			"singleSelect", false
		).setParameter(
			"vocabularyIds",
			() -> {
				ThemeDisplay themeDisplay =
					(ThemeDisplay)_liferayPortletRequest.getAttribute(
						WebKeys.THEME_DISPLAY);

				return StringUtil.merge(
					_assetVocabularyLocalService.getCompanyVocabularies(
						themeDisplay.getCompanyId()),
					assetVocabulary -> String.valueOf(
						assetVocabulary.getVocabularyId()),
					StringPool.COMMA);
			}
		).setWindowState(
			LiferayWindowState.POP_UP
		).buildPortletURL();
	}

	private PortletURL _getAssetTagSelectorURL() throws PortalException {
		return PortletURLBuilder.create(
			PortletProviderUtil.getPortletURL(
				_liferayPortletRequest, AssetTag.class.getName(),
				PortletProvider.Action.BROWSE)
		).setParameter(
			"eventName",
			_liferayPortletResponse.getNamespace() + "selectedAssetTag"
		).setParameter(
			"groupIds",
			() -> {
				ThemeDisplay themeDisplay =
					(ThemeDisplay)_liferayPortletRequest.getAttribute(
						WebKeys.THEME_DISPLAY);

				return StringUtil.merge(
					_groupLocalService.getGroupIds(
						themeDisplay.getCompanyId(), true),
					StringPool.COMMA);
			}
		).setParameter(
			"selectedTagNames",
			StringUtil.merge(
				_contentDashboardAdminDisplayContext.getAssetTagIds(),
				StringPool.COMMA)
		).setWindowState(
			LiferayWindowState.POP_UP
		).buildPortletURL();
	}

	private List<DropdownItem>
		_getContentDashboardItemFilterProviderDropdownItems() {

		return TransformUtil.transform(
			_contentDashboardItemFilterProviderRegistry.
				getContentDashboardItemFilterProviders(),
			contentDashboardItemFilterProvider -> {
				try {
					ContentDashboardItemFilter contentDashboardItemFilter =
						contentDashboardItemFilterProvider.
							getContentDashboardItemFilter(
								_liferayPortletRequest.getHttpServletRequest());

					if (contentDashboardItemFilter == null) {
						return null;
					}

					return contentDashboardItemFilter.getDropdownItem();
				}
				catch (ContentDashboardItemActionException
							contentDashboardItemActionException) {

					_log.error(contentDashboardItemActionException);
				}

				return null;
			});
	}

	private List<DropdownItem> _getFilterAuthorDropdownItems() {
		List<Long> authorIds =
			_contentDashboardAdminDisplayContext.getAuthorIds();

		return DropdownItemList.of(
			() -> DropdownItemBuilder.setActive(
				authorIds.isEmpty()
			).setHref(
				PortletURLBuilder.create(
					getPortletURL()
				).setParameter(
					"authorIds", (String)null
				).buildPortletURL()
			).setLabel(
				_language.get(httpServletRequest, "all")
			).build(),
			() -> {
				DropdownItem dropdownItem = new DropdownItem();

				if ((authorIds.size() == 1) &&
					authorIds.contains(
						_contentDashboardAdminDisplayContext.getUserId())) {

					dropdownItem.setActive(true);
				}

				dropdownItem.setHref(
					getPortletURL(), "authorIds",
					_contentDashboardAdminDisplayContext.getUserId());
				dropdownItem.setLabel(_language.get(httpServletRequest, "me"));

				return dropdownItem;
			},
			() -> DropdownItemBuilder.putData(
				"action", "selectAuthor"
			).putData(
				"dialogTitle",
				_language.get(httpServletRequest, "select-author")
			).putData(
				"redirectURL",
				PortletURLBuilder.create(
					getPortletURL()
				).setParameter(
					"authorIds", (String)null
				).buildString()
			).putData(
				"selectAuthorURL",
				String.valueOf(
					_contentDashboardAdminDisplayContext.
						getAuthorItemSelectorURL())
			).setActive(
				() -> {
					if (((authorIds.size() == 1) &&
						 !authorIds.contains(
							 _contentDashboardAdminDisplayContext.
								 getUserId())) ||
						(authorIds.size() > 1)) {

						return true;
					}

					return null;
				}
			).setLabel(
				_language.get(httpServletRequest, "author") +
					StringPool.TRIPLE_PERIOD
			).build());
	}

	private List<DropdownItem> _getFilterByReviewDateDropdownItems() {
		String reviewDate =
			_contentDashboardAdminDisplayContext.getReviewDateString();

		return DropdownItemListBuilder.add(
			dropdownItem -> {
				dropdownItem.setActive(Validator.isNull(reviewDate));
				dropdownItem.setHref(getPortletURL(), "reviewDate", null);
				dropdownItem.setLabel(_language.get(httpServletRequest, "all"));
			}
		).add(
			dropdownItem -> {
				dropdownItem.setActive(Validator.isNotNull(reviewDate));
				dropdownItem.setHref(
					getPortletURL(), "reviewDate", "toBeReviewed");
				dropdownItem.setLabel(
					_language.get(httpServletRequest, "to-be-reviewed"));
			}
		).build();
	}

	private List<DropdownItem> _getFilterDropdownItems() {
		DropdownItemList dropdownItemList = DropdownItemList.of(
			() -> DropdownItemBuilder.putData(
				"action", "selectAssetCategory"
			).putData(
				"dialogTitle",
				_language.get(httpServletRequest, "select-categories")
			).putData(
				"redirectURL",
				PortletURLBuilder.create(
					getPortletURL()
				).setParameter(
					"assetCategoryId", (String)null
				).buildString()
			).putData(
				"selectAssetCategoryURL",
				String.valueOf(_getAssetCategorySelectorURL())
			).setActive(
				ListUtil.isNotEmpty(
					_contentDashboardAdminDisplayContext.getAssetCategoryIds())
			).setLabel(
				_language.get(httpServletRequest, "categories") +
					StringPool.TRIPLE_PERIOD
			).build(),
			() -> {
				String label = _language.get(
					httpServletRequest, "site-or-asset-library");

				return DropdownItemBuilder.putData(
					"action", "selectScope"
				).putData(
					"dialogTitle",
					_language.get(
						httpServletRequest, "select-site-or-asset-library")
				).putData(
					"redirectURL",
					PortletURLBuilder.create(
						getPortletURL()
					).setParameter(
						"scopeId", (String)null
					).buildString()
				).putData(
					"selectScopeURL",
					String.valueOf(
						_contentDashboardAdminDisplayContext.
							getScopeIdItemSelectorURL())
				).setActive(
					_contentDashboardAdminDisplayContext.getScopeId() > 0
				).setLabel(
					label + StringPool.TRIPLE_PERIOD
				).build();
			},
			() -> DropdownItemBuilder.putData(
				"action", "selectContentDashboardItemSubtype"
			).putData(
				"dialogTitle",
				_language.get(httpServletRequest, "filter-by-type")
			).putData(
				"redirectURL",
				PortletURLBuilder.create(
					getPortletURL()
				).setParameter(
					"contentDashboardItemSubtypePayload", (String)null
				).buildString()
			).putData(
				"selectContentDashboardItemSubtypeURL",
				String.valueOf(
					_contentDashboardAdminDisplayContext.
						getContentDashboardItemSubtypeItemSelectorURL())
			).setActive(
				ListUtil.isNotEmpty(
					_contentDashboardAdminDisplayContext.
						getContentDashboardItemSubtypes())
			).setLabel(
				_language.get(httpServletRequest, "type") +
					StringPool.TRIPLE_PERIOD
			).build(),
			() -> DropdownItemBuilder.putData(
				"action", "selectAssetTag"
			).putData(
				"dialogTitle", _language.get(httpServletRequest, "select-tags")
			).putData(
				"redirectURL",
				PortletURLBuilder.create(
					getPortletURL()
				).setParameter(
					"assetTagId", (String)null
				).buildString()
			).putData(
				"selectTagURL", String.valueOf(_getAssetTagSelectorURL())
			).setActive(
				SetUtil.isNotEmpty(
					_contentDashboardAdminDisplayContext.getAssetTagIds())
			).setLabel(
				_language.get(httpServletRequest, "tags") +
					StringPool.TRIPLE_PERIOD
			).build());

		dropdownItemList.addAll(
			_getContentDashboardItemFilterProviderDropdownItems());

		return dropdownItemList;
	}

	private List<DropdownItem> _getFilterStatusDropdownItems() {
		return new DropdownItemList() {
			{
				Integer curStatus =
					_contentDashboardAdminDisplayContext.getStatus();

				for (int status : _getStatuses()) {
					add(
						dropdownItem -> {
							dropdownItem.setActive(curStatus == status);
							dropdownItem.setHref(
								getPortletURL(), "status",
								String.valueOf(status));
							dropdownItem.setLabel(_getStatusLabel(status));
						});
				}
			}
		};
	}

	private String _getLabel(String key, String value) {
		return StringBundler.concat(
			_language.get(httpServletRequest, key), StringPool.COLON,
			StringPool.SPACE, value);
	}

	private String _getRemoveContentDashboardItemSubtypePayloadsURL(
			List<? extends ContentDashboardItemSubtype>
				contentDashboardItemSubtypes,
			ContentDashboardItemSubtype<?> contentDashboardItemSubtype)
		throws PortletException {

		InfoItemReference infoItemReference =
			contentDashboardItemSubtype.getInfoItemReference();

		return PortletURLBuilder.create(
			PortletURLUtil.clone(currentURLObj, liferayPortletResponse)
		).setParameter(
			"contentDashboardItemSubtypePayload",
			() -> TransformUtil.transformToArray(
				contentDashboardItemSubtypes,
				curContentDashboardItemSubtype -> {
					InfoItemReference curInfoItemReference =
						curContentDashboardItemSubtype.getInfoItemReference();

					if (Objects.equals(
							infoItemReference, curInfoItemReference)) {

						return null;
					}

					return curContentDashboardItemSubtype.toJSONString(_locale);
				},
				String.class)
		).buildString();
	}

	private String _getRemoveLabelURL(
			String name,
			PortletURLBuilder.UnsafeSupplier<Object, Exception>
				valueUnsafeSupplier)
		throws PortletException {

		return PortletURLBuilder.create(
			PortletURLUtil.clone(currentURLObj, liferayPortletResponse)
		).setParameter(
			name, valueUnsafeSupplier
		).buildString();
	}

	private String _getRemoveLabelURL(String name, String value)
		throws PortletException {

		return PortletURLBuilder.create(
			PortletURLUtil.clone(currentURLObj, liferayPortletResponse)
		).setParameter(
			name, value
		).buildString();
	}

	private String _getScopeLabel(long scopeId) {
		Group group = _groupLocalService.fetchGroup(scopeId);

		if (group == null) {
			return StringPool.BLANK;
		}

		try {
			return group.getDescriptiveName(_locale);
		}
		catch (PortalException portalException) {
			_log.error(portalException);

			return group.getName(_locale);
		}
	}

	private List<Integer> _getStatuses() {
		return Arrays.asList(
			WorkflowConstants.STATUS_ANY, WorkflowConstants.STATUS_APPROVED,
			WorkflowConstants.STATUS_DRAFT, WorkflowConstants.STATUS_EXPIRED,
			WorkflowConstants.STATUS_PENDING,
			WorkflowConstants.STATUS_SCHEDULED);
	}

	private String _getStatusLabel(int status) {
		String label = WorkflowConstants.getStatusLabel(status);

		return _language.get(httpServletRequest, label);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ContentDashboardAdminManagementToolbarDisplayContext.class);

	private final AssetCategoryLocalService _assetCategoryLocalService;
	private final AssetVocabularyLocalService _assetVocabularyLocalService;
	private final ContentDashboardAdminDisplayContext
		_contentDashboardAdminDisplayContext;
	private final ContentDashboardItemFilterProviderRegistry
		_contentDashboardItemFilterProviderRegistry;
	private final GroupLocalService _groupLocalService;
	private final Language _language;
	private final LiferayPortletRequest _liferayPortletRequest;
	private final LiferayPortletResponse _liferayPortletResponse;
	private final Locale _locale;
	private final UserLocalService _userLocalService;

}