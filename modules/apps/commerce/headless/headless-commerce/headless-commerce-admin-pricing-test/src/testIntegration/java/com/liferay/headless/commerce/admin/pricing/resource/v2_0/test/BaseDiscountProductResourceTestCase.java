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

package com.liferay.headless.commerce.admin.pricing.resource.v2_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.commerce.admin.pricing.client.dto.v2_0.DiscountProduct;
import com.liferay.headless.commerce.admin.pricing.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.pricing.client.pagination.Page;
import com.liferay.headless.commerce.admin.pricing.client.pagination.Pagination;
import com.liferay.headless.commerce.admin.pricing.client.resource.v2_0.DiscountProductResource;
import com.liferay.headless.commerce.admin.pricing.client.serdes.v2_0.DiscountProductSerDes;
import com.liferay.petra.function.UnsafeTriConsumer;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringBundler;
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
import com.liferay.portal.search.test.util.SearchTestRule;
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

import org.apache.commons.lang.time.DateUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Zoltán Takács
 * @generated
 */
@Generated("")
public abstract class BaseDiscountProductResourceTestCase {

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

		_discountProductResource.setContextCompany(testCompany);

		DiscountProductResource.Builder builder =
			DiscountProductResource.builder();

		discountProductResource = builder.authentication(
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

		DiscountProduct discountProduct1 = randomDiscountProduct();

		String json = objectMapper.writeValueAsString(discountProduct1);

		DiscountProduct discountProduct2 = DiscountProductSerDes.toDTO(json);

		Assert.assertTrue(equals(discountProduct1, discountProduct2));
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

		DiscountProduct discountProduct = randomDiscountProduct();

		String json1 = objectMapper.writeValueAsString(discountProduct);
		String json2 = DiscountProductSerDes.toJSON(discountProduct);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		DiscountProduct discountProduct = randomDiscountProduct();

		discountProduct.setDiscountExternalReferenceCode(regex);
		discountProduct.setProductExternalReferenceCode(regex);

		String json = DiscountProductSerDes.toJSON(discountProduct);

		Assert.assertFalse(json.contains(regex));

		discountProduct = DiscountProductSerDes.toDTO(json);

		Assert.assertEquals(
			regex, discountProduct.getDiscountExternalReferenceCode());
		Assert.assertEquals(
			regex, discountProduct.getProductExternalReferenceCode());
	}

	@Test
	public void testDeleteDiscountProduct() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGraphQLDeleteDiscountProduct() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGetDiscountByExternalReferenceCodeDiscountProductsPage()
		throws Exception {

		String externalReferenceCode =
			testGetDiscountByExternalReferenceCodeDiscountProductsPage_getExternalReferenceCode();
		String irrelevantExternalReferenceCode =
			testGetDiscountByExternalReferenceCodeDiscountProductsPage_getIrrelevantExternalReferenceCode();

		Page<DiscountProduct> page =
			discountProductResource.
				getDiscountByExternalReferenceCodeDiscountProductsPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantExternalReferenceCode != null) {
			DiscountProduct irrelevantDiscountProduct =
				testGetDiscountByExternalReferenceCodeDiscountProductsPage_addDiscountProduct(
					irrelevantExternalReferenceCode,
					randomIrrelevantDiscountProduct());

			page =
				discountProductResource.
					getDiscountByExternalReferenceCodeDiscountProductsPage(
						irrelevantExternalReferenceCode, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantDiscountProduct),
				(List<DiscountProduct>)page.getItems());
			assertValid(
				page,
				testGetDiscountByExternalReferenceCodeDiscountProductsPage_getExpectedActions(
					irrelevantExternalReferenceCode));
		}

		DiscountProduct discountProduct1 =
			testGetDiscountByExternalReferenceCodeDiscountProductsPage_addDiscountProduct(
				externalReferenceCode, randomDiscountProduct());

		DiscountProduct discountProduct2 =
			testGetDiscountByExternalReferenceCodeDiscountProductsPage_addDiscountProduct(
				externalReferenceCode, randomDiscountProduct());

		page =
			discountProductResource.
				getDiscountByExternalReferenceCodeDiscountProductsPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(discountProduct1, discountProduct2),
			(List<DiscountProduct>)page.getItems());
		assertValid(
			page,
			testGetDiscountByExternalReferenceCodeDiscountProductsPage_getExpectedActions(
				externalReferenceCode));
	}

	protected Map<String, Map<String, String>>
			testGetDiscountByExternalReferenceCodeDiscountProductsPage_getExpectedActions(
				String externalReferenceCode)
		throws Exception {

		Map<String, Map<String, String>> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetDiscountByExternalReferenceCodeDiscountProductsPageWithPagination()
		throws Exception {

		String externalReferenceCode =
			testGetDiscountByExternalReferenceCodeDiscountProductsPage_getExternalReferenceCode();

		DiscountProduct discountProduct1 =
			testGetDiscountByExternalReferenceCodeDiscountProductsPage_addDiscountProduct(
				externalReferenceCode, randomDiscountProduct());

		DiscountProduct discountProduct2 =
			testGetDiscountByExternalReferenceCodeDiscountProductsPage_addDiscountProduct(
				externalReferenceCode, randomDiscountProduct());

		DiscountProduct discountProduct3 =
			testGetDiscountByExternalReferenceCodeDiscountProductsPage_addDiscountProduct(
				externalReferenceCode, randomDiscountProduct());

		Page<DiscountProduct> page1 =
			discountProductResource.
				getDiscountByExternalReferenceCodeDiscountProductsPage(
					externalReferenceCode, Pagination.of(1, 2));

		List<DiscountProduct> discountProducts1 =
			(List<DiscountProduct>)page1.getItems();

		Assert.assertEquals(
			discountProducts1.toString(), 2, discountProducts1.size());

		Page<DiscountProduct> page2 =
			discountProductResource.
				getDiscountByExternalReferenceCodeDiscountProductsPage(
					externalReferenceCode, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<DiscountProduct> discountProducts2 =
			(List<DiscountProduct>)page2.getItems();

		Assert.assertEquals(
			discountProducts2.toString(), 1, discountProducts2.size());

		Page<DiscountProduct> page3 =
			discountProductResource.
				getDiscountByExternalReferenceCodeDiscountProductsPage(
					externalReferenceCode, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(discountProduct1, discountProduct2, discountProduct3),
			(List<DiscountProduct>)page3.getItems());
	}

	protected DiscountProduct
			testGetDiscountByExternalReferenceCodeDiscountProductsPage_addDiscountProduct(
				String externalReferenceCode, DiscountProduct discountProduct)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetDiscountByExternalReferenceCodeDiscountProductsPage_getExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetDiscountByExternalReferenceCodeDiscountProductsPage_getIrrelevantExternalReferenceCode()
		throws Exception {

		return null;
	}

	@Test
	public void testPostDiscountByExternalReferenceCodeDiscountProduct()
		throws Exception {

		DiscountProduct randomDiscountProduct = randomDiscountProduct();

		DiscountProduct postDiscountProduct =
			testPostDiscountByExternalReferenceCodeDiscountProduct_addDiscountProduct(
				randomDiscountProduct);

		assertEquals(randomDiscountProduct, postDiscountProduct);
		assertValid(postDiscountProduct);
	}

	protected DiscountProduct
			testPostDiscountByExternalReferenceCodeDiscountProduct_addDiscountProduct(
				DiscountProduct discountProduct)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetDiscountIdDiscountProductsPage() throws Exception {
		Long id = testGetDiscountIdDiscountProductsPage_getId();
		Long irrelevantId =
			testGetDiscountIdDiscountProductsPage_getIrrelevantId();

		Page<DiscountProduct> page =
			discountProductResource.getDiscountIdDiscountProductsPage(
				id, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantId != null) {
			DiscountProduct irrelevantDiscountProduct =
				testGetDiscountIdDiscountProductsPage_addDiscountProduct(
					irrelevantId, randomIrrelevantDiscountProduct());

			page = discountProductResource.getDiscountIdDiscountProductsPage(
				irrelevantId, null, null, Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantDiscountProduct),
				(List<DiscountProduct>)page.getItems());
			assertValid(
				page,
				testGetDiscountIdDiscountProductsPage_getExpectedActions(
					irrelevantId));
		}

		DiscountProduct discountProduct1 =
			testGetDiscountIdDiscountProductsPage_addDiscountProduct(
				id, randomDiscountProduct());

		DiscountProduct discountProduct2 =
			testGetDiscountIdDiscountProductsPage_addDiscountProduct(
				id, randomDiscountProduct());

		page = discountProductResource.getDiscountIdDiscountProductsPage(
			id, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(discountProduct1, discountProduct2),
			(List<DiscountProduct>)page.getItems());
		assertValid(
			page, testGetDiscountIdDiscountProductsPage_getExpectedActions(id));
	}

	protected Map<String, Map<String, String>>
			testGetDiscountIdDiscountProductsPage_getExpectedActions(Long id)
		throws Exception {

		Map<String, Map<String, String>> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetDiscountIdDiscountProductsPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id = testGetDiscountIdDiscountProductsPage_getId();

		DiscountProduct discountProduct1 = randomDiscountProduct();

		discountProduct1 =
			testGetDiscountIdDiscountProductsPage_addDiscountProduct(
				id, discountProduct1);

		for (EntityField entityField : entityFields) {
			Page<DiscountProduct> page =
				discountProductResource.getDiscountIdDiscountProductsPage(
					id, null,
					getFilterString(entityField, "between", discountProduct1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(discountProduct1),
				(List<DiscountProduct>)page.getItems());
		}
	}

	@Test
	public void testGetDiscountIdDiscountProductsPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id = testGetDiscountIdDiscountProductsPage_getId();

		DiscountProduct discountProduct1 =
			testGetDiscountIdDiscountProductsPage_addDiscountProduct(
				id, randomDiscountProduct());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		DiscountProduct discountProduct2 =
			testGetDiscountIdDiscountProductsPage_addDiscountProduct(
				id, randomDiscountProduct());

		for (EntityField entityField : entityFields) {
			Page<DiscountProduct> page =
				discountProductResource.getDiscountIdDiscountProductsPage(
					id, null,
					getFilterString(entityField, "eq", discountProduct1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(discountProduct1),
				(List<DiscountProduct>)page.getItems());
		}
	}

	@Test
	public void testGetDiscountIdDiscountProductsPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id = testGetDiscountIdDiscountProductsPage_getId();

		DiscountProduct discountProduct1 =
			testGetDiscountIdDiscountProductsPage_addDiscountProduct(
				id, randomDiscountProduct());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		DiscountProduct discountProduct2 =
			testGetDiscountIdDiscountProductsPage_addDiscountProduct(
				id, randomDiscountProduct());

		for (EntityField entityField : entityFields) {
			Page<DiscountProduct> page =
				discountProductResource.getDiscountIdDiscountProductsPage(
					id, null,
					getFilterString(entityField, "eq", discountProduct1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(discountProduct1),
				(List<DiscountProduct>)page.getItems());
		}
	}

	@Test
	public void testGetDiscountIdDiscountProductsPageWithPagination()
		throws Exception {

		Long id = testGetDiscountIdDiscountProductsPage_getId();

		DiscountProduct discountProduct1 =
			testGetDiscountIdDiscountProductsPage_addDiscountProduct(
				id, randomDiscountProduct());

		DiscountProduct discountProduct2 =
			testGetDiscountIdDiscountProductsPage_addDiscountProduct(
				id, randomDiscountProduct());

		DiscountProduct discountProduct3 =
			testGetDiscountIdDiscountProductsPage_addDiscountProduct(
				id, randomDiscountProduct());

		Page<DiscountProduct> page1 =
			discountProductResource.getDiscountIdDiscountProductsPage(
				id, null, null, Pagination.of(1, 2), null);

		List<DiscountProduct> discountProducts1 =
			(List<DiscountProduct>)page1.getItems();

		Assert.assertEquals(
			discountProducts1.toString(), 2, discountProducts1.size());

		Page<DiscountProduct> page2 =
			discountProductResource.getDiscountIdDiscountProductsPage(
				id, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<DiscountProduct> discountProducts2 =
			(List<DiscountProduct>)page2.getItems();

		Assert.assertEquals(
			discountProducts2.toString(), 1, discountProducts2.size());

		Page<DiscountProduct> page3 =
			discountProductResource.getDiscountIdDiscountProductsPage(
				id, null, null, Pagination.of(1, 3), null);

		assertEqualsIgnoringOrder(
			Arrays.asList(discountProduct1, discountProduct2, discountProduct3),
			(List<DiscountProduct>)page3.getItems());
	}

	@Test
	public void testGetDiscountIdDiscountProductsPageWithSortDateTime()
		throws Exception {

		testGetDiscountIdDiscountProductsPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, discountProduct1, discountProduct2) -> {
				BeanTestUtil.setProperty(
					discountProduct1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetDiscountIdDiscountProductsPageWithSortDouble()
		throws Exception {

		testGetDiscountIdDiscountProductsPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, discountProduct1, discountProduct2) -> {
				BeanTestUtil.setProperty(
					discountProduct1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					discountProduct2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetDiscountIdDiscountProductsPageWithSortInteger()
		throws Exception {

		testGetDiscountIdDiscountProductsPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, discountProduct1, discountProduct2) -> {
				BeanTestUtil.setProperty(
					discountProduct1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					discountProduct2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetDiscountIdDiscountProductsPageWithSortString()
		throws Exception {

		testGetDiscountIdDiscountProductsPageWithSort(
			EntityField.Type.STRING,
			(entityField, discountProduct1, discountProduct2) -> {
				Class<?> clazz = discountProduct1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						discountProduct1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						discountProduct2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						discountProduct1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						discountProduct2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						discountProduct1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						discountProduct2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetDiscountIdDiscountProductsPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer
				<EntityField, DiscountProduct, DiscountProduct, Exception>
					unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id = testGetDiscountIdDiscountProductsPage_getId();

		DiscountProduct discountProduct1 = randomDiscountProduct();
		DiscountProduct discountProduct2 = randomDiscountProduct();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(
				entityField, discountProduct1, discountProduct2);
		}

		discountProduct1 =
			testGetDiscountIdDiscountProductsPage_addDiscountProduct(
				id, discountProduct1);

		discountProduct2 =
			testGetDiscountIdDiscountProductsPage_addDiscountProduct(
				id, discountProduct2);

		for (EntityField entityField : entityFields) {
			Page<DiscountProduct> ascPage =
				discountProductResource.getDiscountIdDiscountProductsPage(
					id, null, null, Pagination.of(1, 2),
					entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(discountProduct1, discountProduct2),
				(List<DiscountProduct>)ascPage.getItems());

			Page<DiscountProduct> descPage =
				discountProductResource.getDiscountIdDiscountProductsPage(
					id, null, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(discountProduct2, discountProduct1),
				(List<DiscountProduct>)descPage.getItems());
		}
	}

	protected DiscountProduct
			testGetDiscountIdDiscountProductsPage_addDiscountProduct(
				Long id, DiscountProduct discountProduct)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetDiscountIdDiscountProductsPage_getId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetDiscountIdDiscountProductsPage_getIrrelevantId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostDiscountIdDiscountProduct() throws Exception {
		DiscountProduct randomDiscountProduct = randomDiscountProduct();

		DiscountProduct postDiscountProduct =
			testPostDiscountIdDiscountProduct_addDiscountProduct(
				randomDiscountProduct);

		assertEquals(randomDiscountProduct, postDiscountProduct);
		assertValid(postDiscountProduct);
	}

	protected DiscountProduct
			testPostDiscountIdDiscountProduct_addDiscountProduct(
				DiscountProduct discountProduct)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Rule
	public SearchTestRule searchTestRule = new SearchTestRule();

	protected void assertContains(
		DiscountProduct discountProduct,
		List<DiscountProduct> discountProducts) {

		boolean contains = false;

		for (DiscountProduct item : discountProducts) {
			if (equals(discountProduct, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			discountProducts + " does not contain " + discountProduct,
			contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		DiscountProduct discountProduct1, DiscountProduct discountProduct2) {

		Assert.assertTrue(
			discountProduct1 + " does not equal " + discountProduct2,
			equals(discountProduct1, discountProduct2));
	}

	protected void assertEquals(
		List<DiscountProduct> discountProducts1,
		List<DiscountProduct> discountProducts2) {

		Assert.assertEquals(discountProducts1.size(), discountProducts2.size());

		for (int i = 0; i < discountProducts1.size(); i++) {
			DiscountProduct discountProduct1 = discountProducts1.get(i);
			DiscountProduct discountProduct2 = discountProducts2.get(i);

			assertEquals(discountProduct1, discountProduct2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<DiscountProduct> discountProducts1,
		List<DiscountProduct> discountProducts2) {

		Assert.assertEquals(discountProducts1.size(), discountProducts2.size());

		for (DiscountProduct discountProduct1 : discountProducts1) {
			boolean contains = false;

			for (DiscountProduct discountProduct2 : discountProducts2) {
				if (equals(discountProduct1, discountProduct2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				discountProducts2 + " does not contain " + discountProduct1,
				contains);
		}
	}

	protected void assertValid(DiscountProduct discountProduct)
		throws Exception {

		boolean valid = true;

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (discountProduct.getActions() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"discountExternalReferenceCode",
					additionalAssertFieldName)) {

				if (discountProduct.getDiscountExternalReferenceCode() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals("discountId", additionalAssertFieldName)) {
				if (discountProduct.getDiscountId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"discountProductId", additionalAssertFieldName)) {

				if (discountProduct.getDiscountProductId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("product", additionalAssertFieldName)) {
				if (discountProduct.getProduct() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"productExternalReferenceCode",
					additionalAssertFieldName)) {

				if (discountProduct.getProductExternalReferenceCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("productId", additionalAssertFieldName)) {
				if (discountProduct.getProductId() == null) {
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

	protected void assertValid(Page<DiscountProduct> page) {
		assertValid(page, Collections.emptyMap());
	}

	protected void assertValid(
		Page<DiscountProduct> page,
		Map<String, Map<String, String>> expectedActions) {

		boolean valid = false;

		java.util.Collection<DiscountProduct> discountProducts =
			page.getItems();

		int size = discountProducts.size();

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
					com.liferay.headless.commerce.admin.pricing.dto.v2_0.
						DiscountProduct.class)) {

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
		DiscountProduct discountProduct1, DiscountProduct discountProduct2) {

		if (discountProduct1 == discountProduct2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (!equals(
						(Map)discountProduct1.getActions(),
						(Map)discountProduct2.getActions())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"discountExternalReferenceCode",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						discountProduct1.getDiscountExternalReferenceCode(),
						discountProduct2.getDiscountExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("discountId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						discountProduct1.getDiscountId(),
						discountProduct2.getDiscountId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"discountProductId", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						discountProduct1.getDiscountProductId(),
						discountProduct2.getDiscountProductId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("product", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						discountProduct1.getProduct(),
						discountProduct2.getProduct())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"productExternalReferenceCode",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						discountProduct1.getProductExternalReferenceCode(),
						discountProduct2.getProductExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("productId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						discountProduct1.getProductId(),
						discountProduct2.getProductId())) {

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

		if (!(_discountProductResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_discountProductResource;

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
		EntityField entityField, String operator,
		DiscountProduct discountProduct) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("actions")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("discountExternalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(
					discountProduct.getDiscountExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("discountId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("discountProductId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("product")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("productExternalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(
					discountProduct.getProductExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("productId")) {
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

	protected DiscountProduct randomDiscountProduct() throws Exception {
		return new DiscountProduct() {
			{
				discountExternalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				discountId = RandomTestUtil.randomLong();
				discountProductId = RandomTestUtil.randomLong();
				productExternalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				productId = RandomTestUtil.randomLong();
			}
		};
	}

	protected DiscountProduct randomIrrelevantDiscountProduct()
		throws Exception {

		DiscountProduct randomIrrelevantDiscountProduct =
			randomDiscountProduct();

		return randomIrrelevantDiscountProduct;
	}

	protected DiscountProduct randomPatchDiscountProduct() throws Exception {
		return randomDiscountProduct();
	}

	protected DiscountProductResource discountProductResource;
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
		LogFactoryUtil.getLog(BaseDiscountProductResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.admin.pricing.resource.v2_0.
		DiscountProductResource _discountProductResource;

}