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

package com.liferay.headless.commerce.delivery.catalog.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.commerce.delivery.catalog.client.dto.v1_0.WishListItem;
import com.liferay.headless.commerce.delivery.catalog.client.http.HttpInvoker;
import com.liferay.headless.commerce.delivery.catalog.client.pagination.Page;
import com.liferay.headless.commerce.delivery.catalog.client.pagination.Pagination;
import com.liferay.headless.commerce.delivery.catalog.client.resource.v1_0.WishListItemResource;
import com.liferay.headless.commerce.delivery.catalog.client.serdes.v1_0.WishListItemSerDes;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.vulcan.resource.EntityModelResource;

import java.lang.reflect.Method;

import java.text.DateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Generated;

import javax.ws.rs.core.MultivaluedHashMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Andrea Sbarra
 * @generated
 */
@Generated("")
public abstract class BaseWishListItemResourceTestCase {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() throws Exception {
		_dateFormat = DateFormatFactoryUtil.getSimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");
	}

	@Before
	public void setUp() throws Exception {
		irrelevantGroup = GroupTestUtil.addGroup();
		testGroup = GroupTestUtil.addGroup();

		testCompany = CompanyLocalServiceUtil.getCompany(
			testGroup.getCompanyId());

		_wishListItemResource.setContextCompany(testCompany);

		WishListItemResource.Builder builder = WishListItemResource.builder();

		wishListItemResource = builder.authentication(
			"test@liferay.com", "test"
		).locale(
			LocaleUtil.getDefault()
		).build();
	}

	@After
	public void tearDown() throws Exception {
		GroupTestUtil.deleteGroup(irrelevantGroup);
		GroupTestUtil.deleteGroup(testGroup);
	}

	@Test
	public void testClientSerDesToDTO() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper() {
			{
				configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
				configure(
					SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
				enable(SerializationFeature.INDENT_OUTPUT);
				setDateFormat(new ISO8601DateFormat());
				setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
				setSerializationInclusion(JsonInclude.Include.NON_NULL);
				setVisibility(
					PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
				setVisibility(
					PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
			}
		};

		WishListItem wishListItem1 = randomWishListItem();

		String json = objectMapper.writeValueAsString(wishListItem1);

		WishListItem wishListItem2 = WishListItemSerDes.toDTO(json);

		Assert.assertTrue(equals(wishListItem1, wishListItem2));
	}

	@Test
	public void testClientSerDesToJSON() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper() {
			{
				configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
				configure(
					SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
				setDateFormat(new ISO8601DateFormat());
				setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
				setSerializationInclusion(JsonInclude.Include.NON_NULL);
				setVisibility(
					PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
				setVisibility(
					PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
			}
		};

		WishListItem wishListItem = randomWishListItem();

		String json1 = objectMapper.writeValueAsString(wishListItem);
		String json2 = WishListItemSerDes.toJSON(wishListItem);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		WishListItem wishListItem = randomWishListItem();

		wishListItem.setFinalPrice(regex);
		wishListItem.setFriendlyURL(regex);
		wishListItem.setIcon(regex);
		wishListItem.setProductName(regex);

		String json = WishListItemSerDes.toJSON(wishListItem);

		Assert.assertFalse(json.contains(regex));

		wishListItem = WishListItemSerDes.toDTO(json);

		Assert.assertEquals(regex, wishListItem.getFinalPrice());
		Assert.assertEquals(regex, wishListItem.getFriendlyURL());
		Assert.assertEquals(regex, wishListItem.getIcon());
		Assert.assertEquals(regex, wishListItem.getProductName());
	}

	@Test
	public void testDeleteWishListItem() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		WishListItem wishListItem = testDeleteWishListItem_addWishListItem();

		assertHttpResponseStatusCode(
			204,
			wishListItemResource.deleteWishListItemHttpResponse(
				wishListItem.getId()));

		assertHttpResponseStatusCode(
			404,
			wishListItemResource.getWishListItemHttpResponse(
				wishListItem.getId(), testDeleteWishListItem_getAccountId()));

		assertHttpResponseStatusCode(
			404,
			wishListItemResource.getWishListItemHttpResponse(
				0L, testDeleteWishListItem_getAccountId()));
	}

	protected Long testDeleteWishListItem_getAccountId() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected WishListItem testDeleteWishListItem_addWishListItem()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteWishListItem() throws Exception {
		WishListItem wishListItem =
			testGraphQLDeleteWishListItem_addWishListItem();

		Assert.assertTrue(
			JSONUtil.getValueAsBoolean(
				invokeGraphQLMutation(
					new GraphQLField(
						"deleteWishListItem",
						new HashMap<String, Object>() {
							{
								put("wishListItemId", wishListItem.getId());
							}
						})),
				"JSONObject/data", "Object/deleteWishListItem"));
		JSONArray errorsJSONArray = JSONUtil.getValueAsJSONArray(
			invokeGraphQLQuery(
				new GraphQLField(
					"wishListItem",
					new HashMap<String, Object>() {
						{
							put("wishListItemId", wishListItem.getId());
						}
					},
					new GraphQLField("id"))),
			"JSONArray/errors");

		Assert.assertTrue(errorsJSONArray.length() > 0);
	}

	protected WishListItem testGraphQLDeleteWishListItem_addWishListItem()
		throws Exception {

		return testGraphQLWishListItem_addWishListItem();
	}

	@Test
	public void testGetWishListItem() throws Exception {
		WishListItem postWishListItem = testGetWishListItem_addWishListItem();

		WishListItem getWishListItem = wishListItemResource.getWishListItem(
			postWishListItem.getId(), null);

		assertEquals(postWishListItem, getWishListItem);
		assertValid(getWishListItem);
	}

	protected WishListItem testGetWishListItem_addWishListItem()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetWishListItem() throws Exception {
		WishListItem wishListItem =
			testGraphQLGetWishListItem_addWishListItem();

		Assert.assertTrue(
			equals(
				wishListItem,
				WishListItemSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"wishListItem",
								new HashMap<String, Object>() {
									{
										put(
											"wishListItemId",
											wishListItem.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/wishListItem"))));
	}

	@Test
	public void testGraphQLGetWishListItemNotFound() throws Exception {
		Long irrelevantWishListItemId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"wishListItem",
						new HashMap<String, Object>() {
							{
								put("wishListItemId", irrelevantWishListItemId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected WishListItem testGraphQLGetWishListItem_addWishListItem()
		throws Exception {

		return testGraphQLWishListItem_addWishListItem();
	}

	@Test
	public void testGetWishlistWishListWishListItemsPage() throws Exception {
		Long wishListId =
			testGetWishlistWishListWishListItemsPage_getWishListId();
		Long irrelevantWishListId =
			testGetWishlistWishListWishListItemsPage_getIrrelevantWishListId();

		Page<WishListItem> page =
			wishListItemResource.getWishlistWishListWishListItemsPage(
				wishListId, null, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantWishListId != null) {
			WishListItem irrelevantWishListItem =
				testGetWishlistWishListWishListItemsPage_addWishListItem(
					irrelevantWishListId, randomIrrelevantWishListItem());

			page = wishListItemResource.getWishlistWishListWishListItemsPage(
				irrelevantWishListId, null, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantWishListItem),
				(List<WishListItem>)page.getItems());
			assertValid(
				page,
				testGetWishlistWishListWishListItemsPage_getExpectedActions(
					irrelevantWishListId));
		}

		WishListItem wishListItem1 =
			testGetWishlistWishListWishListItemsPage_addWishListItem(
				wishListId, randomWishListItem());

		WishListItem wishListItem2 =
			testGetWishlistWishListWishListItemsPage_addWishListItem(
				wishListId, randomWishListItem());

		page = wishListItemResource.getWishlistWishListWishListItemsPage(
			wishListId, null, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(wishListItem1, wishListItem2),
			(List<WishListItem>)page.getItems());
		assertValid(
			page,
			testGetWishlistWishListWishListItemsPage_getExpectedActions(
				wishListId));

		wishListItemResource.deleteWishListItem(wishListItem1.getId());

		wishListItemResource.deleteWishListItem(wishListItem2.getId());
	}

	protected Map<String, Map<String, String>>
			testGetWishlistWishListWishListItemsPage_getExpectedActions(
				Long wishListId)
		throws Exception {

		Map<String, Map<String, String>> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetWishlistWishListWishListItemsPageWithPagination()
		throws Exception {

		Long wishListId =
			testGetWishlistWishListWishListItemsPage_getWishListId();

		WishListItem wishListItem1 =
			testGetWishlistWishListWishListItemsPage_addWishListItem(
				wishListId, randomWishListItem());

		WishListItem wishListItem2 =
			testGetWishlistWishListWishListItemsPage_addWishListItem(
				wishListId, randomWishListItem());

		WishListItem wishListItem3 =
			testGetWishlistWishListWishListItemsPage_addWishListItem(
				wishListId, randomWishListItem());

		Page<WishListItem> page1 =
			wishListItemResource.getWishlistWishListWishListItemsPage(
				wishListId, null, Pagination.of(1, 2));

		List<WishListItem> wishListItems1 =
			(List<WishListItem>)page1.getItems();

		Assert.assertEquals(
			wishListItems1.toString(), 2, wishListItems1.size());

		Page<WishListItem> page2 =
			wishListItemResource.getWishlistWishListWishListItemsPage(
				wishListId, null, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<WishListItem> wishListItems2 =
			(List<WishListItem>)page2.getItems();

		Assert.assertEquals(
			wishListItems2.toString(), 1, wishListItems2.size());

		Page<WishListItem> page3 =
			wishListItemResource.getWishlistWishListWishListItemsPage(
				wishListId, null, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(wishListItem1, wishListItem2, wishListItem3),
			(List<WishListItem>)page3.getItems());
	}

	protected WishListItem
			testGetWishlistWishListWishListItemsPage_addWishListItem(
				Long wishListId, WishListItem wishListItem)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetWishlistWishListWishListItemsPage_getWishListId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetWishlistWishListWishListItemsPage_getIrrelevantWishListId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostWishlistWishListWishListItem() throws Exception {
		WishListItem randomWishListItem = randomWishListItem();

		WishListItem postWishListItem =
			testPostWishlistWishListWishListItem_addWishListItem(
				randomWishListItem);

		assertEquals(randomWishListItem, postWishListItem);
		assertValid(postWishListItem);
	}

	protected WishListItem testPostWishlistWishListWishListItem_addWishListItem(
			WishListItem wishListItem)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected WishListItem testGraphQLWishListItem_addWishListItem()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		WishListItem wishListItem, List<WishListItem> wishListItems) {

		boolean contains = false;

		for (WishListItem item : wishListItems) {
			if (equals(wishListItem, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			wishListItems + " does not contain " + wishListItem, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		WishListItem wishListItem1, WishListItem wishListItem2) {

		Assert.assertTrue(
			wishListItem1 + " does not equal " + wishListItem2,
			equals(wishListItem1, wishListItem2));
	}

	protected void assertEquals(
		List<WishListItem> wishListItems1, List<WishListItem> wishListItems2) {

		Assert.assertEquals(wishListItems1.size(), wishListItems2.size());

		for (int i = 0; i < wishListItems1.size(); i++) {
			WishListItem wishListItem1 = wishListItems1.get(i);
			WishListItem wishListItem2 = wishListItems2.get(i);

			assertEquals(wishListItem1, wishListItem2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<WishListItem> wishListItems1, List<WishListItem> wishListItems2) {

		Assert.assertEquals(wishListItems1.size(), wishListItems2.size());

		for (WishListItem wishListItem1 : wishListItems1) {
			boolean contains = false;

			for (WishListItem wishListItem2 : wishListItems2) {
				if (equals(wishListItem1, wishListItem2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				wishListItems2 + " does not contain " + wishListItem1,
				contains);
		}
	}

	protected void assertValid(WishListItem wishListItem) throws Exception {
		boolean valid = true;

		if (wishListItem.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("finalPrice", additionalAssertFieldName)) {
				if (wishListItem.getFinalPrice() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("friendlyURL", additionalAssertFieldName)) {
				if (wishListItem.getFriendlyURL() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("icon", additionalAssertFieldName)) {
				if (wishListItem.getIcon() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("productId", additionalAssertFieldName)) {
				if (wishListItem.getProductId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("productName", additionalAssertFieldName)) {
				if (wishListItem.getProductName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("skuId", additionalAssertFieldName)) {
				if (wishListItem.getSkuId() == null) {
					valid = false;
				}

				continue;
			}

			throw new IllegalArgumentException(
				"Invalid additional assert field name " +
					additionalAssertFieldName);
		}

		Assert.assertTrue(valid);
	}

	protected void assertValid(Page<WishListItem> page) {
		assertValid(page, Collections.emptyMap());
	}

	protected void assertValid(
		Page<WishListItem> page,
		Map<String, Map<String, String>> expectedActions) {

		boolean valid = false;

		java.util.Collection<WishListItem> wishListItems = page.getItems();

		int size = wishListItems.size();

		if ((page.getLastPage() > 0) && (page.getPage() > 0) &&
			(page.getPageSize() > 0) && (page.getTotalCount() > 0) &&
			(size > 0)) {

			valid = true;
		}

		Assert.assertTrue(valid);

		Map<String, Map<String, String>> actions = page.getActions();

		for (String key : expectedActions.keySet()) {
			Map action = actions.get(key);

			Assert.assertNotNull(key + " does not contain an action", action);

			Map expectedAction = expectedActions.get(key);

			Assert.assertEquals(
				expectedAction.get("method"), action.get("method"));
			Assert.assertEquals(expectedAction.get("href"), action.get("href"));
		}
	}

	protected String[] getAdditionalAssertFieldNames() {
		return new String[0];
	}

	protected List<GraphQLField> getGraphQLFields() throws Exception {
		List<GraphQLField> graphQLFields = new ArrayList<>();

		for (java.lang.reflect.Field field :
				getDeclaredFields(
					com.liferay.headless.commerce.delivery.catalog.dto.v1_0.
						WishListItem.class)) {

			if (!ArrayUtil.contains(
					getAdditionalAssertFieldNames(), field.getName())) {

				continue;
			}

			graphQLFields.addAll(getGraphQLFields(field));
		}

		return graphQLFields;
	}

	protected List<GraphQLField> getGraphQLFields(
			java.lang.reflect.Field... fields)
		throws Exception {

		List<GraphQLField> graphQLFields = new ArrayList<>();

		for (java.lang.reflect.Field field : fields) {
			com.liferay.portal.vulcan.graphql.annotation.GraphQLField
				vulcanGraphQLField = field.getAnnotation(
					com.liferay.portal.vulcan.graphql.annotation.GraphQLField.
						class);

			if (vulcanGraphQLField != null) {
				Class<?> clazz = field.getType();

				if (clazz.isArray()) {
					clazz = clazz.getComponentType();
				}

				List<GraphQLField> childrenGraphQLFields = getGraphQLFields(
					getDeclaredFields(clazz));

				graphQLFields.add(
					new GraphQLField(field.getName(), childrenGraphQLFields));
			}
		}

		return graphQLFields;
	}

	protected String[] getIgnoredEntityFieldNames() {
		return new String[0];
	}

	protected boolean equals(
		WishListItem wishListItem1, WishListItem wishListItem2) {

		if (wishListItem1 == wishListItem2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("finalPrice", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						wishListItem1.getFinalPrice(),
						wishListItem2.getFinalPrice())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("friendlyURL", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						wishListItem1.getFriendlyURL(),
						wishListItem2.getFriendlyURL())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("icon", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						wishListItem1.getIcon(), wishListItem2.getIcon())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						wishListItem1.getId(), wishListItem2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("productId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						wishListItem1.getProductId(),
						wishListItem2.getProductId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("productName", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						wishListItem1.getProductName(),
						wishListItem2.getProductName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("skuId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						wishListItem1.getSkuId(), wishListItem2.getSkuId())) {

					return false;
				}

				continue;
			}

			throw new IllegalArgumentException(
				"Invalid additional assert field name " +
					additionalAssertFieldName);
		}

		return true;
	}

	protected boolean equals(
		Map<String, Object> map1, Map<String, Object> map2) {

		if (Objects.equals(map1.keySet(), map2.keySet())) {
			for (Map.Entry<String, Object> entry : map1.entrySet()) {
				if (entry.getValue() instanceof Map) {
					if (!equals(
							(Map)entry.getValue(),
							(Map)map2.get(entry.getKey()))) {

						return false;
					}
				}
				else if (!Objects.deepEquals(
							entry.getValue(), map2.get(entry.getKey()))) {

					return false;
				}
			}

			return true;
		}

		return false;
	}

	protected java.lang.reflect.Field[] getDeclaredFields(Class clazz)
		throws Exception {

		return TransformUtil.transform(
			ReflectionUtil.getDeclaredFields(clazz),
			field -> {
				if (field.isSynthetic()) {
					return null;
				}

				return field;
			},
			java.lang.reflect.Field.class);
	}

	protected java.util.Collection<EntityField> getEntityFields()
		throws Exception {

		if (!(_wishListItemResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_wishListItemResource;

		EntityModel entityModel = entityModelResource.getEntityModel(
			new MultivaluedHashMap());

		if (entityModel == null) {
			return Collections.emptyList();
		}

		Map<String, EntityField> entityFieldsMap =
			entityModel.getEntityFieldsMap();

		return entityFieldsMap.values();
	}

	protected List<EntityField> getEntityFields(EntityField.Type type)
		throws Exception {

		return TransformUtil.transform(
			getEntityFields(),
			entityField -> {
				if (!Objects.equals(entityField.getType(), type) ||
					ArrayUtil.contains(
						getIgnoredEntityFieldNames(), entityField.getName())) {

					return null;
				}

				return entityField;
			});
	}

	protected String getFilterString(
		EntityField entityField, String operator, WishListItem wishListItem) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("finalPrice")) {
			sb.append("'");
			sb.append(String.valueOf(wishListItem.getFinalPrice()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("friendlyURL")) {
			sb.append("'");
			sb.append(String.valueOf(wishListItem.getFriendlyURL()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("icon")) {
			sb.append("'");
			sb.append(String.valueOf(wishListItem.getIcon()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("productId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("productName")) {
			sb.append("'");
			sb.append(String.valueOf(wishListItem.getProductName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("skuId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		throw new IllegalArgumentException(
			"Invalid entity field " + entityFieldName);
	}

	protected String invoke(String query) throws Exception {
		HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

		httpInvoker.body(
			JSONUtil.put(
				"query", query
			).toString(),
			"application/json");
		httpInvoker.httpMethod(HttpInvoker.HttpMethod.POST);
		httpInvoker.path("http://localhost:8080/o/graphql");
		httpInvoker.userNameAndPassword("test@liferay.com:test");

		HttpInvoker.HttpResponse httpResponse = httpInvoker.invoke();

		return httpResponse.getContent();
	}

	protected JSONObject invokeGraphQLMutation(GraphQLField graphQLField)
		throws Exception {

		GraphQLField mutationGraphQLField = new GraphQLField(
			"mutation", graphQLField);

		return JSONFactoryUtil.createJSONObject(
			invoke(mutationGraphQLField.toString()));
	}

	protected JSONObject invokeGraphQLQuery(GraphQLField graphQLField)
		throws Exception {

		GraphQLField queryGraphQLField = new GraphQLField(
			"query", graphQLField);

		return JSONFactoryUtil.createJSONObject(
			invoke(queryGraphQLField.toString()));
	}

	protected WishListItem randomWishListItem() throws Exception {
		return new WishListItem() {
			{
				finalPrice = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				friendlyURL = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				icon = StringUtil.toLowerCase(RandomTestUtil.randomString());
				id = RandomTestUtil.randomLong();
				productId = RandomTestUtil.randomLong();
				productName = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				skuId = RandomTestUtil.randomLong();
			}
		};
	}

	protected WishListItem randomIrrelevantWishListItem() throws Exception {
		WishListItem randomIrrelevantWishListItem = randomWishListItem();

		return randomIrrelevantWishListItem;
	}

	protected WishListItem randomPatchWishListItem() throws Exception {
		return randomWishListItem();
	}

	protected WishListItemResource wishListItemResource;
	protected Group irrelevantGroup;
	protected Company testCompany;
	protected Group testGroup;

	protected static class BeanTestUtil {

		public static void copyProperties(Object source, Object target)
			throws Exception {

			Class<?> sourceClass = _getSuperClass(source.getClass());

			Class<?> targetClass = target.getClass();

			for (java.lang.reflect.Field field :
					sourceClass.getDeclaredFields()) {

				if (field.isSynthetic()) {
					continue;
				}

				Method getMethod = _getMethod(
					sourceClass, field.getName(), "get");

				Method setMethod = _getMethod(
					targetClass, field.getName(), "set",
					getMethod.getReturnType());

				setMethod.invoke(target, getMethod.invoke(source));
			}
		}

		public static boolean hasProperty(Object bean, String name) {
			Method setMethod = _getMethod(
				bean.getClass(), "set" + StringUtil.upperCaseFirstLetter(name));

			if (setMethod != null) {
				return true;
			}

			return false;
		}

		public static void setProperty(Object bean, String name, Object value)
			throws Exception {

			Class<?> clazz = bean.getClass();

			Method setMethod = _getMethod(
				clazz, "set" + StringUtil.upperCaseFirstLetter(name));

			if (setMethod == null) {
				throw new NoSuchMethodException();
			}

			Class<?>[] parameterTypes = setMethod.getParameterTypes();

			setMethod.invoke(bean, _translateValue(parameterTypes[0], value));
		}

		private static Method _getMethod(Class<?> clazz, String name) {
			for (Method method : clazz.getMethods()) {
				if (name.equals(method.getName()) &&
					(method.getParameterCount() == 1) &&
					_parameterTypes.contains(method.getParameterTypes()[0])) {

					return method;
				}
			}

			return null;
		}

		private static Method _getMethod(
				Class<?> clazz, String fieldName, String prefix,
				Class<?>... parameterTypes)
			throws Exception {

			return clazz.getMethod(
				prefix + StringUtil.upperCaseFirstLetter(fieldName),
				parameterTypes);
		}

		private static Class<?> _getSuperClass(Class<?> clazz) {
			Class<?> superClass = clazz.getSuperclass();

			if ((superClass == null) || (superClass == Object.class)) {
				return clazz;
			}

			return superClass;
		}

		private static Object _translateValue(
			Class<?> parameterType, Object value) {

			if ((value instanceof Integer) &&
				parameterType.equals(Long.class)) {

				Integer intValue = (Integer)value;

				return intValue.longValue();
			}

			return value;
		}

		private static final Set<Class<?>> _parameterTypes = new HashSet<>(
			Arrays.asList(
				Boolean.class, Date.class, Double.class, Integer.class,
				Long.class, Map.class, String.class));

	}

	protected class GraphQLField {

		public GraphQLField(String key, GraphQLField... graphQLFields) {
			this(key, new HashMap<>(), graphQLFields);
		}

		public GraphQLField(String key, List<GraphQLField> graphQLFields) {
			this(key, new HashMap<>(), graphQLFields);
		}

		public GraphQLField(
			String key, Map<String, Object> parameterMap,
			GraphQLField... graphQLFields) {

			_key = key;
			_parameterMap = parameterMap;
			_graphQLFields = Arrays.asList(graphQLFields);
		}

		public GraphQLField(
			String key, Map<String, Object> parameterMap,
			List<GraphQLField> graphQLFields) {

			_key = key;
			_parameterMap = parameterMap;
			_graphQLFields = graphQLFields;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(_key);

			if (!_parameterMap.isEmpty()) {
				sb.append("(");

				for (Map.Entry<String, Object> entry :
						_parameterMap.entrySet()) {

					sb.append(entry.getKey());
					sb.append(": ");
					sb.append(entry.getValue());
					sb.append(", ");
				}

				sb.setLength(sb.length() - 2);

				sb.append(")");
			}

			if (!_graphQLFields.isEmpty()) {
				sb.append("{");

				for (GraphQLField graphQLField : _graphQLFields) {
					sb.append(graphQLField.toString());
					sb.append(", ");
				}

				sb.setLength(sb.length() - 2);

				sb.append("}");
			}

			return sb.toString();
		}

		private final List<GraphQLField> _graphQLFields;
		private final String _key;
		private final Map<String, Object> _parameterMap;

	}

	private static final com.liferay.portal.kernel.log.Log _log =
		LogFactoryUtil.getLog(BaseWishListItemResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.delivery.catalog.resource.v1_0.
		WishListItemResource _wishListItemResource;

}