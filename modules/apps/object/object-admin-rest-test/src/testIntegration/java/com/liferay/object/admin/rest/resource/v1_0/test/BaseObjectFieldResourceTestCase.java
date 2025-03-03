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

package com.liferay.object.admin.rest.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.object.admin.rest.client.dto.v1_0.ObjectField;
import com.liferay.object.admin.rest.client.http.HttpInvoker;
import com.liferay.object.admin.rest.client.pagination.Page;
import com.liferay.object.admin.rest.client.pagination.Pagination;
import com.liferay.object.admin.rest.client.resource.v1_0.ObjectFieldResource;
import com.liferay.object.admin.rest.client.serdes.v1_0.ObjectFieldSerDes;
import com.liferay.petra.function.UnsafeTriConsumer;
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
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public abstract class BaseObjectFieldResourceTestCase {

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

		_objectFieldResource.setContextCompany(testCompany);

		ObjectFieldResource.Builder builder = ObjectFieldResource.builder();

		objectFieldResource = builder.authentication(
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

		ObjectField objectField1 = randomObjectField();

		String json = objectMapper.writeValueAsString(objectField1);

		ObjectField objectField2 = ObjectFieldSerDes.toDTO(json);

		Assert.assertTrue(equals(objectField1, objectField2));
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

		ObjectField objectField = randomObjectField();

		String json1 = objectMapper.writeValueAsString(objectField);
		String json2 = ObjectFieldSerDes.toJSON(objectField);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		ObjectField objectField = randomObjectField();

		objectField.setDefaultValue(regex);
		objectField.setExternalReferenceCode(regex);
		objectField.setIndexedLanguageId(regex);
		objectField.setListTypeDefinitionExternalReferenceCode(regex);
		objectField.setName(regex);
		objectField.setReadOnlyConditionExpression(regex);

		String json = ObjectFieldSerDes.toJSON(objectField);

		Assert.assertFalse(json.contains(regex));

		objectField = ObjectFieldSerDes.toDTO(json);

		Assert.assertEquals(regex, objectField.getDefaultValue());
		Assert.assertEquals(regex, objectField.getExternalReferenceCode());
		Assert.assertEquals(regex, objectField.getIndexedLanguageId());
		Assert.assertEquals(
			regex, objectField.getListTypeDefinitionExternalReferenceCode());
		Assert.assertEquals(regex, objectField.getName());
		Assert.assertEquals(
			regex, objectField.getReadOnlyConditionExpression());
	}

	@Test
	public void testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPage()
		throws Exception {

		String externalReferenceCode =
			testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPage_getExternalReferenceCode();
		String irrelevantExternalReferenceCode =
			testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPage_getIrrelevantExternalReferenceCode();

		Page<ObjectField> page =
			objectFieldResource.
				getObjectDefinitionByExternalReferenceCodeObjectFieldsPage(
					externalReferenceCode, null, null, Pagination.of(1, 10),
					null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantExternalReferenceCode != null) {
			ObjectField irrelevantObjectField =
				testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPage_addObjectField(
					irrelevantExternalReferenceCode,
					randomIrrelevantObjectField());

			page =
				objectFieldResource.
					getObjectDefinitionByExternalReferenceCodeObjectFieldsPage(
						irrelevantExternalReferenceCode, null, null,
						Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantObjectField),
				(List<ObjectField>)page.getItems());
			assertValid(
				page,
				testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPage_getExpectedActions(
					irrelevantExternalReferenceCode));
		}

		ObjectField objectField1 =
			testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPage_addObjectField(
				externalReferenceCode, randomObjectField());

		ObjectField objectField2 =
			testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPage_addObjectField(
				externalReferenceCode, randomObjectField());

		page =
			objectFieldResource.
				getObjectDefinitionByExternalReferenceCodeObjectFieldsPage(
					externalReferenceCode, null, null, Pagination.of(1, 10),
					null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(objectField1, objectField2),
			(List<ObjectField>)page.getItems());
		assertValid(
			page,
			testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPage_getExpectedActions(
				externalReferenceCode));

		objectFieldResource.deleteObjectField(objectField1.getId());

		objectFieldResource.deleteObjectField(objectField2.getId());
	}

	protected Map<String, Map<String, String>>
			testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPage_getExpectedActions(
				String externalReferenceCode)
		throws Exception {

		Map<String, Map<String, String>> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		String externalReferenceCode =
			testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPage_getExternalReferenceCode();

		ObjectField objectField1 = randomObjectField();

		objectField1 =
			testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPage_addObjectField(
				externalReferenceCode, objectField1);

		for (EntityField entityField : entityFields) {
			Page<ObjectField> page =
				objectFieldResource.
					getObjectDefinitionByExternalReferenceCodeObjectFieldsPage(
						externalReferenceCode, null,
						getFilterString(entityField, "between", objectField1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(objectField1),
				(List<ObjectField>)page.getItems());
		}
	}

	@Test
	public void testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		String externalReferenceCode =
			testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPage_getExternalReferenceCode();

		ObjectField objectField1 =
			testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPage_addObjectField(
				externalReferenceCode, randomObjectField());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		ObjectField objectField2 =
			testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPage_addObjectField(
				externalReferenceCode, randomObjectField());

		for (EntityField entityField : entityFields) {
			Page<ObjectField> page =
				objectFieldResource.
					getObjectDefinitionByExternalReferenceCodeObjectFieldsPage(
						externalReferenceCode, null,
						getFilterString(entityField, "eq", objectField1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(objectField1),
				(List<ObjectField>)page.getItems());
		}
	}

	@Test
	public void testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		String externalReferenceCode =
			testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPage_getExternalReferenceCode();

		ObjectField objectField1 =
			testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPage_addObjectField(
				externalReferenceCode, randomObjectField());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		ObjectField objectField2 =
			testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPage_addObjectField(
				externalReferenceCode, randomObjectField());

		for (EntityField entityField : entityFields) {
			Page<ObjectField> page =
				objectFieldResource.
					getObjectDefinitionByExternalReferenceCodeObjectFieldsPage(
						externalReferenceCode, null,
						getFilterString(entityField, "eq", objectField1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(objectField1),
				(List<ObjectField>)page.getItems());
		}
	}

	@Test
	public void testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPageWithPagination()
		throws Exception {

		String externalReferenceCode =
			testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPage_getExternalReferenceCode();

		ObjectField objectField1 =
			testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPage_addObjectField(
				externalReferenceCode, randomObjectField());

		ObjectField objectField2 =
			testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPage_addObjectField(
				externalReferenceCode, randomObjectField());

		ObjectField objectField3 =
			testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPage_addObjectField(
				externalReferenceCode, randomObjectField());

		Page<ObjectField> page1 =
			objectFieldResource.
				getObjectDefinitionByExternalReferenceCodeObjectFieldsPage(
					externalReferenceCode, null, null, Pagination.of(1, 2),
					null);

		List<ObjectField> objectFields1 = (List<ObjectField>)page1.getItems();

		Assert.assertEquals(objectFields1.toString(), 2, objectFields1.size());

		Page<ObjectField> page2 =
			objectFieldResource.
				getObjectDefinitionByExternalReferenceCodeObjectFieldsPage(
					externalReferenceCode, null, null, Pagination.of(2, 2),
					null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<ObjectField> objectFields2 = (List<ObjectField>)page2.getItems();

		Assert.assertEquals(objectFields2.toString(), 1, objectFields2.size());

		Page<ObjectField> page3 =
			objectFieldResource.
				getObjectDefinitionByExternalReferenceCodeObjectFieldsPage(
					externalReferenceCode, null, null, Pagination.of(1, 3),
					null);

		assertEqualsIgnoringOrder(
			Arrays.asList(objectField1, objectField2, objectField3),
			(List<ObjectField>)page3.getItems());
	}

	@Test
	public void testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPageWithSortDateTime()
		throws Exception {

		testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, objectField1, objectField2) -> {
				BeanTestUtil.setProperty(
					objectField1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPageWithSortDouble()
		throws Exception {

		testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, objectField1, objectField2) -> {
				BeanTestUtil.setProperty(
					objectField1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					objectField2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPageWithSortInteger()
		throws Exception {

		testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, objectField1, objectField2) -> {
				BeanTestUtil.setProperty(
					objectField1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					objectField2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPageWithSortString()
		throws Exception {

		testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPageWithSort(
			EntityField.Type.STRING,
			(entityField, objectField1, objectField2) -> {
				Class<?> clazz = objectField1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						objectField1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						objectField2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						objectField1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						objectField2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						objectField1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						objectField2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void
			testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPageWithSort(
				EntityField.Type type,
				UnsafeTriConsumer
					<EntityField, ObjectField, ObjectField, Exception>
						unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		String externalReferenceCode =
			testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPage_getExternalReferenceCode();

		ObjectField objectField1 = randomObjectField();
		ObjectField objectField2 = randomObjectField();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(entityField, objectField1, objectField2);
		}

		objectField1 =
			testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPage_addObjectField(
				externalReferenceCode, objectField1);

		objectField2 =
			testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPage_addObjectField(
				externalReferenceCode, objectField2);

		for (EntityField entityField : entityFields) {
			Page<ObjectField> ascPage =
				objectFieldResource.
					getObjectDefinitionByExternalReferenceCodeObjectFieldsPage(
						externalReferenceCode, null, null, Pagination.of(1, 2),
						entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(objectField1, objectField2),
				(List<ObjectField>)ascPage.getItems());

			Page<ObjectField> descPage =
				objectFieldResource.
					getObjectDefinitionByExternalReferenceCodeObjectFieldsPage(
						externalReferenceCode, null, null, Pagination.of(1, 2),
						entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(objectField2, objectField1),
				(List<ObjectField>)descPage.getItems());
		}
	}

	protected ObjectField
			testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPage_addObjectField(
				String externalReferenceCode, ObjectField objectField)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPage_getExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetObjectDefinitionByExternalReferenceCodeObjectFieldsPage_getIrrelevantExternalReferenceCode()
		throws Exception {

		return null;
	}

	@Test
	public void testPostObjectDefinitionByExternalReferenceCodeObjectField()
		throws Exception {

		ObjectField randomObjectField = randomObjectField();

		ObjectField postObjectField =
			testPostObjectDefinitionByExternalReferenceCodeObjectField_addObjectField(
				randomObjectField);

		assertEquals(randomObjectField, postObjectField);
		assertValid(postObjectField);
	}

	protected ObjectField
			testPostObjectDefinitionByExternalReferenceCodeObjectField_addObjectField(
				ObjectField objectField)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetObjectDefinitionObjectFieldsPage() throws Exception {
		Long objectDefinitionId =
			testGetObjectDefinitionObjectFieldsPage_getObjectDefinitionId();
		Long irrelevantObjectDefinitionId =
			testGetObjectDefinitionObjectFieldsPage_getIrrelevantObjectDefinitionId();

		Page<ObjectField> page =
			objectFieldResource.getObjectDefinitionObjectFieldsPage(
				objectDefinitionId, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantObjectDefinitionId != null) {
			ObjectField irrelevantObjectField =
				testGetObjectDefinitionObjectFieldsPage_addObjectField(
					irrelevantObjectDefinitionId,
					randomIrrelevantObjectField());

			page = objectFieldResource.getObjectDefinitionObjectFieldsPage(
				irrelevantObjectDefinitionId, null, null, Pagination.of(1, 2),
				null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantObjectField),
				(List<ObjectField>)page.getItems());
			assertValid(
				page,
				testGetObjectDefinitionObjectFieldsPage_getExpectedActions(
					irrelevantObjectDefinitionId));
		}

		ObjectField objectField1 =
			testGetObjectDefinitionObjectFieldsPage_addObjectField(
				objectDefinitionId, randomObjectField());

		ObjectField objectField2 =
			testGetObjectDefinitionObjectFieldsPage_addObjectField(
				objectDefinitionId, randomObjectField());

		page = objectFieldResource.getObjectDefinitionObjectFieldsPage(
			objectDefinitionId, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(objectField1, objectField2),
			(List<ObjectField>)page.getItems());
		assertValid(
			page,
			testGetObjectDefinitionObjectFieldsPage_getExpectedActions(
				objectDefinitionId));

		objectFieldResource.deleteObjectField(objectField1.getId());

		objectFieldResource.deleteObjectField(objectField2.getId());
	}

	protected Map<String, Map<String, String>>
			testGetObjectDefinitionObjectFieldsPage_getExpectedActions(
				Long objectDefinitionId)
		throws Exception {

		Map<String, Map<String, String>> expectedActions = new HashMap<>();

		Map createBatchAction = new HashMap<>();
		createBatchAction.put("method", "POST");
		createBatchAction.put(
			"href",
			"http://localhost:8080/o/object-admin/v1.0/object-definitions/{objectDefinitionId}/object-fields/batch".
				replace(
					"{objectDefinitionId}",
					String.valueOf(objectDefinitionId)));

		expectedActions.put("createBatch", createBatchAction);

		return expectedActions;
	}

	@Test
	public void testGetObjectDefinitionObjectFieldsPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long objectDefinitionId =
			testGetObjectDefinitionObjectFieldsPage_getObjectDefinitionId();

		ObjectField objectField1 = randomObjectField();

		objectField1 = testGetObjectDefinitionObjectFieldsPage_addObjectField(
			objectDefinitionId, objectField1);

		for (EntityField entityField : entityFields) {
			Page<ObjectField> page =
				objectFieldResource.getObjectDefinitionObjectFieldsPage(
					objectDefinitionId, null,
					getFilterString(entityField, "between", objectField1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(objectField1),
				(List<ObjectField>)page.getItems());
		}
	}

	@Test
	public void testGetObjectDefinitionObjectFieldsPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		Long objectDefinitionId =
			testGetObjectDefinitionObjectFieldsPage_getObjectDefinitionId();

		ObjectField objectField1 =
			testGetObjectDefinitionObjectFieldsPage_addObjectField(
				objectDefinitionId, randomObjectField());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		ObjectField objectField2 =
			testGetObjectDefinitionObjectFieldsPage_addObjectField(
				objectDefinitionId, randomObjectField());

		for (EntityField entityField : entityFields) {
			Page<ObjectField> page =
				objectFieldResource.getObjectDefinitionObjectFieldsPage(
					objectDefinitionId, null,
					getFilterString(entityField, "eq", objectField1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(objectField1),
				(List<ObjectField>)page.getItems());
		}
	}

	@Test
	public void testGetObjectDefinitionObjectFieldsPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long objectDefinitionId =
			testGetObjectDefinitionObjectFieldsPage_getObjectDefinitionId();

		ObjectField objectField1 =
			testGetObjectDefinitionObjectFieldsPage_addObjectField(
				objectDefinitionId, randomObjectField());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		ObjectField objectField2 =
			testGetObjectDefinitionObjectFieldsPage_addObjectField(
				objectDefinitionId, randomObjectField());

		for (EntityField entityField : entityFields) {
			Page<ObjectField> page =
				objectFieldResource.getObjectDefinitionObjectFieldsPage(
					objectDefinitionId, null,
					getFilterString(entityField, "eq", objectField1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(objectField1),
				(List<ObjectField>)page.getItems());
		}
	}

	@Test
	public void testGetObjectDefinitionObjectFieldsPageWithPagination()
		throws Exception {

		Long objectDefinitionId =
			testGetObjectDefinitionObjectFieldsPage_getObjectDefinitionId();

		ObjectField objectField1 =
			testGetObjectDefinitionObjectFieldsPage_addObjectField(
				objectDefinitionId, randomObjectField());

		ObjectField objectField2 =
			testGetObjectDefinitionObjectFieldsPage_addObjectField(
				objectDefinitionId, randomObjectField());

		ObjectField objectField3 =
			testGetObjectDefinitionObjectFieldsPage_addObjectField(
				objectDefinitionId, randomObjectField());

		Page<ObjectField> page1 =
			objectFieldResource.getObjectDefinitionObjectFieldsPage(
				objectDefinitionId, null, null, Pagination.of(1, 2), null);

		List<ObjectField> objectFields1 = (List<ObjectField>)page1.getItems();

		Assert.assertEquals(objectFields1.toString(), 2, objectFields1.size());

		Page<ObjectField> page2 =
			objectFieldResource.getObjectDefinitionObjectFieldsPage(
				objectDefinitionId, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<ObjectField> objectFields2 = (List<ObjectField>)page2.getItems();

		Assert.assertEquals(objectFields2.toString(), 1, objectFields2.size());

		Page<ObjectField> page3 =
			objectFieldResource.getObjectDefinitionObjectFieldsPage(
				objectDefinitionId, null, null, Pagination.of(1, 3), null);

		assertEqualsIgnoringOrder(
			Arrays.asList(objectField1, objectField2, objectField3),
			(List<ObjectField>)page3.getItems());
	}

	@Test
	public void testGetObjectDefinitionObjectFieldsPageWithSortDateTime()
		throws Exception {

		testGetObjectDefinitionObjectFieldsPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, objectField1, objectField2) -> {
				BeanTestUtil.setProperty(
					objectField1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetObjectDefinitionObjectFieldsPageWithSortDouble()
		throws Exception {

		testGetObjectDefinitionObjectFieldsPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, objectField1, objectField2) -> {
				BeanTestUtil.setProperty(
					objectField1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					objectField2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetObjectDefinitionObjectFieldsPageWithSortInteger()
		throws Exception {

		testGetObjectDefinitionObjectFieldsPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, objectField1, objectField2) -> {
				BeanTestUtil.setProperty(
					objectField1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					objectField2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetObjectDefinitionObjectFieldsPageWithSortString()
		throws Exception {

		testGetObjectDefinitionObjectFieldsPageWithSort(
			EntityField.Type.STRING,
			(entityField, objectField1, objectField2) -> {
				Class<?> clazz = objectField1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						objectField1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						objectField2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						objectField1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						objectField2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						objectField1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						objectField2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetObjectDefinitionObjectFieldsPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer<EntityField, ObjectField, ObjectField, Exception>
				unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Long objectDefinitionId =
			testGetObjectDefinitionObjectFieldsPage_getObjectDefinitionId();

		ObjectField objectField1 = randomObjectField();
		ObjectField objectField2 = randomObjectField();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(entityField, objectField1, objectField2);
		}

		objectField1 = testGetObjectDefinitionObjectFieldsPage_addObjectField(
			objectDefinitionId, objectField1);

		objectField2 = testGetObjectDefinitionObjectFieldsPage_addObjectField(
			objectDefinitionId, objectField2);

		for (EntityField entityField : entityFields) {
			Page<ObjectField> ascPage =
				objectFieldResource.getObjectDefinitionObjectFieldsPage(
					objectDefinitionId, null, null, Pagination.of(1, 2),
					entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(objectField1, objectField2),
				(List<ObjectField>)ascPage.getItems());

			Page<ObjectField> descPage =
				objectFieldResource.getObjectDefinitionObjectFieldsPage(
					objectDefinitionId, null, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(objectField2, objectField1),
				(List<ObjectField>)descPage.getItems());
		}
	}

	protected ObjectField
			testGetObjectDefinitionObjectFieldsPage_addObjectField(
				Long objectDefinitionId, ObjectField objectField)
		throws Exception {

		return objectFieldResource.postObjectDefinitionObjectField(
			objectDefinitionId, objectField);
	}

	protected Long
			testGetObjectDefinitionObjectFieldsPage_getObjectDefinitionId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetObjectDefinitionObjectFieldsPage_getIrrelevantObjectDefinitionId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostObjectDefinitionObjectField() throws Exception {
		ObjectField randomObjectField = randomObjectField();

		ObjectField postObjectField =
			testPostObjectDefinitionObjectField_addObjectField(
				randomObjectField);

		assertEquals(randomObjectField, postObjectField);
		assertValid(postObjectField);
	}

	protected ObjectField testPostObjectDefinitionObjectField_addObjectField(
			ObjectField objectField)
		throws Exception {

		return objectFieldResource.postObjectDefinitionObjectField(
			testGetObjectDefinitionObjectFieldsPage_getObjectDefinitionId(),
			objectField);
	}

	@Test
	public void testDeleteObjectField() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		ObjectField objectField = testDeleteObjectField_addObjectField();

		assertHttpResponseStatusCode(
			204,
			objectFieldResource.deleteObjectFieldHttpResponse(
				objectField.getId()));

		assertHttpResponseStatusCode(
			404,
			objectFieldResource.getObjectFieldHttpResponse(
				objectField.getId()));

		assertHttpResponseStatusCode(
			404, objectFieldResource.getObjectFieldHttpResponse(0L));
	}

	protected ObjectField testDeleteObjectField_addObjectField()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteObjectField() throws Exception {
		ObjectField objectField = testGraphQLDeleteObjectField_addObjectField();

		Assert.assertTrue(
			JSONUtil.getValueAsBoolean(
				invokeGraphQLMutation(
					new GraphQLField(
						"deleteObjectField",
						new HashMap<String, Object>() {
							{
								put("objectFieldId", objectField.getId());
							}
						})),
				"JSONObject/data", "Object/deleteObjectField"));
		JSONArray errorsJSONArray = JSONUtil.getValueAsJSONArray(
			invokeGraphQLQuery(
				new GraphQLField(
					"objectField",
					new HashMap<String, Object>() {
						{
							put("objectFieldId", objectField.getId());
						}
					},
					new GraphQLField("id"))),
			"JSONArray/errors");

		Assert.assertTrue(errorsJSONArray.length() > 0);
	}

	protected ObjectField testGraphQLDeleteObjectField_addObjectField()
		throws Exception {

		return testGraphQLObjectField_addObjectField();
	}

	@Test
	public void testGetObjectField() throws Exception {
		ObjectField postObjectField = testGetObjectField_addObjectField();

		ObjectField getObjectField = objectFieldResource.getObjectField(
			postObjectField.getId());

		assertEquals(postObjectField, getObjectField);
		assertValid(getObjectField);
	}

	protected ObjectField testGetObjectField_addObjectField() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetObjectField() throws Exception {
		ObjectField objectField = testGraphQLGetObjectField_addObjectField();

		Assert.assertTrue(
			equals(
				objectField,
				ObjectFieldSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"objectField",
								new HashMap<String, Object>() {
									{
										put(
											"objectFieldId",
											objectField.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/objectField"))));
	}

	@Test
	public void testGraphQLGetObjectFieldNotFound() throws Exception {
		Long irrelevantObjectFieldId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"objectField",
						new HashMap<String, Object>() {
							{
								put("objectFieldId", irrelevantObjectFieldId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected ObjectField testGraphQLGetObjectField_addObjectField()
		throws Exception {

		return testGraphQLObjectField_addObjectField();
	}

	@Test
	public void testPatchObjectField() throws Exception {
		ObjectField postObjectField = testPatchObjectField_addObjectField();

		ObjectField randomPatchObjectField = randomPatchObjectField();

		@SuppressWarnings("PMD.UnusedLocalVariable")
		ObjectField patchObjectField = objectFieldResource.patchObjectField(
			postObjectField.getId(), randomPatchObjectField);

		ObjectField expectedPatchObjectField = postObjectField.clone();

		BeanTestUtil.copyProperties(
			randomPatchObjectField, expectedPatchObjectField);

		ObjectField getObjectField = objectFieldResource.getObjectField(
			patchObjectField.getId());

		assertEquals(expectedPatchObjectField, getObjectField);
		assertValid(getObjectField);
	}

	protected ObjectField testPatchObjectField_addObjectField()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testPutObjectField() throws Exception {
		ObjectField postObjectField = testPutObjectField_addObjectField();

		ObjectField randomObjectField = randomObjectField();

		ObjectField putObjectField = objectFieldResource.putObjectField(
			postObjectField.getId(), randomObjectField);

		assertEquals(randomObjectField, putObjectField);
		assertValid(putObjectField);

		ObjectField getObjectField = objectFieldResource.getObjectField(
			putObjectField.getId());

		assertEquals(randomObjectField, getObjectField);
		assertValid(getObjectField);
	}

	protected ObjectField testPutObjectField_addObjectField() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Rule
	public SearchTestRule searchTestRule = new SearchTestRule();

	protected ObjectField testGraphQLObjectField_addObjectField()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		ObjectField objectField, List<ObjectField> objectFields) {

		boolean contains = false;

		for (ObjectField item : objectFields) {
			if (equals(objectField, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			objectFields + " does not contain " + objectField, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		ObjectField objectField1, ObjectField objectField2) {

		Assert.assertTrue(
			objectField1 + " does not equal " + objectField2,
			equals(objectField1, objectField2));
	}

	protected void assertEquals(
		List<ObjectField> objectFields1, List<ObjectField> objectFields2) {

		Assert.assertEquals(objectFields1.size(), objectFields2.size());

		for (int i = 0; i < objectFields1.size(); i++) {
			ObjectField objectField1 = objectFields1.get(i);
			ObjectField objectField2 = objectFields2.get(i);

			assertEquals(objectField1, objectField2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<ObjectField> objectFields1, List<ObjectField> objectFields2) {

		Assert.assertEquals(objectFields1.size(), objectFields2.size());

		for (ObjectField objectField1 : objectFields1) {
			boolean contains = false;

			for (ObjectField objectField2 : objectFields2) {
				if (equals(objectField1, objectField2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				objectFields2 + " does not contain " + objectField1, contains);
		}
	}

	protected void assertValid(ObjectField objectField) throws Exception {
		boolean valid = true;

		if (objectField.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("DBType", additionalAssertFieldName)) {
				if (objectField.getDBType() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (objectField.getActions() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("businessType", additionalAssertFieldName)) {
				if (objectField.getBusinessType() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("defaultValue", additionalAssertFieldName)) {
				if (objectField.getDefaultValue() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCode", additionalAssertFieldName)) {

				if (objectField.getExternalReferenceCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("indexed", additionalAssertFieldName)) {
				if (objectField.getIndexed() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("indexedAsKeyword", additionalAssertFieldName)) {
				if (objectField.getIndexedAsKeyword() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"indexedLanguageId", additionalAssertFieldName)) {

				if (objectField.getIndexedLanguageId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("label", additionalAssertFieldName)) {
				if (objectField.getLabel() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"listTypeDefinitionExternalReferenceCode",
					additionalAssertFieldName)) {

				if (objectField.getListTypeDefinitionExternalReferenceCode() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"listTypeDefinitionId", additionalAssertFieldName)) {

				if (objectField.getListTypeDefinitionId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("localized", additionalAssertFieldName)) {
				if (objectField.getLocalized() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (objectField.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"objectFieldSettings", additionalAssertFieldName)) {

				if (objectField.getObjectFieldSettings() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("readOnly", additionalAssertFieldName)) {
				if (objectField.getReadOnly() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"readOnlyConditionExpression", additionalAssertFieldName)) {

				if (objectField.getReadOnlyConditionExpression() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("relationshipType", additionalAssertFieldName)) {
				if (objectField.getRelationshipType() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("required", additionalAssertFieldName)) {
				if (objectField.getRequired() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("state", additionalAssertFieldName)) {
				if (objectField.getState() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("system", additionalAssertFieldName)) {
				if (objectField.getSystem() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("type", additionalAssertFieldName)) {
				if (objectField.getType() == null) {
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

	protected void assertValid(Page<ObjectField> page) {
		assertValid(page, Collections.emptyMap());
	}

	protected void assertValid(
		Page<ObjectField> page,
		Map<String, Map<String, String>> expectedActions) {

		boolean valid = false;

		java.util.Collection<ObjectField> objectFields = page.getItems();

		int size = objectFields.size();

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
					com.liferay.object.admin.rest.dto.v1_0.ObjectField.class)) {

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
		ObjectField objectField1, ObjectField objectField2) {

		if (objectField1 == objectField2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("DBType", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectField1.getDBType(), objectField2.getDBType())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (!equals(
						(Map)objectField1.getActions(),
						(Map)objectField2.getActions())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("businessType", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectField1.getBusinessType(),
						objectField2.getBusinessType())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("defaultValue", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectField1.getDefaultValue(),
						objectField2.getDefaultValue())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCode", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						objectField1.getExternalReferenceCode(),
						objectField2.getExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectField1.getId(), objectField2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("indexed", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectField1.getIndexed(), objectField2.getIndexed())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("indexedAsKeyword", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectField1.getIndexedAsKeyword(),
						objectField2.getIndexedAsKeyword())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"indexedLanguageId", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						objectField1.getIndexedLanguageId(),
						objectField2.getIndexedLanguageId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("label", additionalAssertFieldName)) {
				if (!equals(
						(Map)objectField1.getLabel(),
						(Map)objectField2.getLabel())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"listTypeDefinitionExternalReferenceCode",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						objectField1.
							getListTypeDefinitionExternalReferenceCode(),
						objectField2.
							getListTypeDefinitionExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"listTypeDefinitionId", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						objectField1.getListTypeDefinitionId(),
						objectField2.getListTypeDefinitionId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("localized", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectField1.getLocalized(),
						objectField2.getLocalized())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectField1.getName(), objectField2.getName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"objectFieldSettings", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						objectField1.getObjectFieldSettings(),
						objectField2.getObjectFieldSettings())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("readOnly", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectField1.getReadOnly(),
						objectField2.getReadOnly())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"readOnlyConditionExpression", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						objectField1.getReadOnlyConditionExpression(),
						objectField2.getReadOnlyConditionExpression())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("relationshipType", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectField1.getRelationshipType(),
						objectField2.getRelationshipType())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("required", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectField1.getRequired(),
						objectField2.getRequired())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("state", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectField1.getState(), objectField2.getState())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("system", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectField1.getSystem(), objectField2.getSystem())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("type", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						objectField1.getType(), objectField2.getType())) {

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

		if (!(_objectFieldResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_objectFieldResource;

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
		EntityField entityField, String operator, ObjectField objectField) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("DBType")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("actions")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("businessType")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("defaultValue")) {
			sb.append("'");
			sb.append(String.valueOf(objectField.getDefaultValue()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("externalReferenceCode")) {
			sb.append("'");
			sb.append(String.valueOf(objectField.getExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("indexed")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("indexedAsKeyword")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("indexedLanguageId")) {
			sb.append("'");
			sb.append(String.valueOf(objectField.getIndexedLanguageId()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("label")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("listTypeDefinitionExternalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(
					objectField.getListTypeDefinitionExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("listTypeDefinitionId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("localized")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("name")) {
			sb.append("'");
			sb.append(String.valueOf(objectField.getName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("objectFieldSettings")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("readOnly")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("readOnlyConditionExpression")) {
			sb.append("'");
			sb.append(
				String.valueOf(objectField.getReadOnlyConditionExpression()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("relationshipType")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("required")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("state")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("system")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("type")) {
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

	protected ObjectField randomObjectField() throws Exception {
		return new ObjectField() {
			{
				defaultValue = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				externalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				id = RandomTestUtil.randomLong();
				indexed = RandomTestUtil.randomBoolean();
				indexedAsKeyword = RandomTestUtil.randomBoolean();
				indexedLanguageId = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				listTypeDefinitionExternalReferenceCode =
					StringUtil.toLowerCase(RandomTestUtil.randomString());
				listTypeDefinitionId = RandomTestUtil.randomLong();
				localized = RandomTestUtil.randomBoolean();
				name = StringUtil.toLowerCase(RandomTestUtil.randomString());
				readOnlyConditionExpression = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				required = RandomTestUtil.randomBoolean();
				state = RandomTestUtil.randomBoolean();
				system = RandomTestUtil.randomBoolean();
			}
		};
	}

	protected ObjectField randomIrrelevantObjectField() throws Exception {
		ObjectField randomIrrelevantObjectField = randomObjectField();

		return randomIrrelevantObjectField;
	}

	protected ObjectField randomPatchObjectField() throws Exception {
		return randomObjectField();
	}

	protected ObjectFieldResource objectFieldResource;
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
		LogFactoryUtil.getLog(BaseObjectFieldResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.object.admin.rest.resource.v1_0.ObjectFieldResource
		_objectFieldResource;

}