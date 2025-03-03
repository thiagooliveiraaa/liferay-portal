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

package com.liferay.headless.admin.user.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.admin.user.client.dto.v1_0.EmailAddress;
import com.liferay.headless.admin.user.client.http.HttpInvoker;
import com.liferay.headless.admin.user.client.pagination.Page;
import com.liferay.headless.admin.user.client.resource.v1_0.EmailAddressResource;
import com.liferay.headless.admin.user.client.serdes.v1_0.EmailAddressSerDes;
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
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public abstract class BaseEmailAddressResourceTestCase {

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

		_emailAddressResource.setContextCompany(testCompany);

		EmailAddressResource.Builder builder = EmailAddressResource.builder();

		emailAddressResource = builder.authentication(
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

		EmailAddress emailAddress1 = randomEmailAddress();

		String json = objectMapper.writeValueAsString(emailAddress1);

		EmailAddress emailAddress2 = EmailAddressSerDes.toDTO(json);

		Assert.assertTrue(equals(emailAddress1, emailAddress2));
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

		EmailAddress emailAddress = randomEmailAddress();

		String json1 = objectMapper.writeValueAsString(emailAddress);
		String json2 = EmailAddressSerDes.toJSON(emailAddress);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		EmailAddress emailAddress = randomEmailAddress();

		emailAddress.setEmailAddress(regex);
		emailAddress.setType(regex);

		String json = EmailAddressSerDes.toJSON(emailAddress);

		Assert.assertFalse(json.contains(regex));

		emailAddress = EmailAddressSerDes.toDTO(json);

		Assert.assertEquals(regex, emailAddress.getEmailAddress());
		Assert.assertEquals(regex, emailAddress.getType());
	}

	@Test
	public void testGetEmailAddress() throws Exception {
		EmailAddress postEmailAddress = testGetEmailAddress_addEmailAddress();

		EmailAddress getEmailAddress = emailAddressResource.getEmailAddress(
			postEmailAddress.getId());

		assertEquals(postEmailAddress, getEmailAddress);
		assertValid(getEmailAddress);
	}

	protected EmailAddress testGetEmailAddress_addEmailAddress()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetEmailAddress() throws Exception {
		EmailAddress emailAddress =
			testGraphQLGetEmailAddress_addEmailAddress();

		Assert.assertTrue(
			equals(
				emailAddress,
				EmailAddressSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"emailAddress",
								new HashMap<String, Object>() {
									{
										put(
											"emailAddressId",
											emailAddress.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/emailAddress"))));
	}

	@Test
	public void testGraphQLGetEmailAddressNotFound() throws Exception {
		Long irrelevantEmailAddressId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"emailAddress",
						new HashMap<String, Object>() {
							{
								put("emailAddressId", irrelevantEmailAddressId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected EmailAddress testGraphQLGetEmailAddress_addEmailAddress()
		throws Exception {

		return testGraphQLEmailAddress_addEmailAddress();
	}

	@Test
	public void testGetOrganizationEmailAddressesPage() throws Exception {
		String organizationId =
			testGetOrganizationEmailAddressesPage_getOrganizationId();
		String irrelevantOrganizationId =
			testGetOrganizationEmailAddressesPage_getIrrelevantOrganizationId();

		Page<EmailAddress> page =
			emailAddressResource.getOrganizationEmailAddressesPage(
				organizationId);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantOrganizationId != null) {
			EmailAddress irrelevantEmailAddress =
				testGetOrganizationEmailAddressesPage_addEmailAddress(
					irrelevantOrganizationId, randomIrrelevantEmailAddress());

			page = emailAddressResource.getOrganizationEmailAddressesPage(
				irrelevantOrganizationId);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantEmailAddress),
				(List<EmailAddress>)page.getItems());
			assertValid(
				page,
				testGetOrganizationEmailAddressesPage_getExpectedActions(
					irrelevantOrganizationId));
		}

		EmailAddress emailAddress1 =
			testGetOrganizationEmailAddressesPage_addEmailAddress(
				organizationId, randomEmailAddress());

		EmailAddress emailAddress2 =
			testGetOrganizationEmailAddressesPage_addEmailAddress(
				organizationId, randomEmailAddress());

		page = emailAddressResource.getOrganizationEmailAddressesPage(
			organizationId);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(emailAddress1, emailAddress2),
			(List<EmailAddress>)page.getItems());
		assertValid(
			page,
			testGetOrganizationEmailAddressesPage_getExpectedActions(
				organizationId));
	}

	protected Map<String, Map<String, String>>
			testGetOrganizationEmailAddressesPage_getExpectedActions(
				String organizationId)
		throws Exception {

		Map<String, Map<String, String>> expectedActions = new HashMap<>();

		return expectedActions;
	}

	protected EmailAddress
			testGetOrganizationEmailAddressesPage_addEmailAddress(
				String organizationId, EmailAddress emailAddress)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String testGetOrganizationEmailAddressesPage_getOrganizationId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetOrganizationEmailAddressesPage_getIrrelevantOrganizationId()
		throws Exception {

		return null;
	}

	@Test
	public void testGetUserAccountEmailAddressesPage() throws Exception {
		Long userAccountId =
			testGetUserAccountEmailAddressesPage_getUserAccountId();
		Long irrelevantUserAccountId =
			testGetUserAccountEmailAddressesPage_getIrrelevantUserAccountId();

		Page<EmailAddress> page =
			emailAddressResource.getUserAccountEmailAddressesPage(
				userAccountId);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantUserAccountId != null) {
			EmailAddress irrelevantEmailAddress =
				testGetUserAccountEmailAddressesPage_addEmailAddress(
					irrelevantUserAccountId, randomIrrelevantEmailAddress());

			page = emailAddressResource.getUserAccountEmailAddressesPage(
				irrelevantUserAccountId);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantEmailAddress),
				(List<EmailAddress>)page.getItems());
			assertValid(
				page,
				testGetUserAccountEmailAddressesPage_getExpectedActions(
					irrelevantUserAccountId));
		}

		EmailAddress emailAddress1 =
			testGetUserAccountEmailAddressesPage_addEmailAddress(
				userAccountId, randomEmailAddress());

		EmailAddress emailAddress2 =
			testGetUserAccountEmailAddressesPage_addEmailAddress(
				userAccountId, randomEmailAddress());

		page = emailAddressResource.getUserAccountEmailAddressesPage(
			userAccountId);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(emailAddress1, emailAddress2),
			(List<EmailAddress>)page.getItems());
		assertValid(
			page,
			testGetUserAccountEmailAddressesPage_getExpectedActions(
				userAccountId));
	}

	protected Map<String, Map<String, String>>
			testGetUserAccountEmailAddressesPage_getExpectedActions(
				Long userAccountId)
		throws Exception {

		Map<String, Map<String, String>> expectedActions = new HashMap<>();

		return expectedActions;
	}

	protected EmailAddress testGetUserAccountEmailAddressesPage_addEmailAddress(
			Long userAccountId, EmailAddress emailAddress)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetUserAccountEmailAddressesPage_getUserAccountId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetUserAccountEmailAddressesPage_getIrrelevantUserAccountId()
		throws Exception {

		return null;
	}

	protected EmailAddress testGraphQLEmailAddress_addEmailAddress()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		EmailAddress emailAddress, List<EmailAddress> emailAddresses) {

		boolean contains = false;

		for (EmailAddress item : emailAddresses) {
			if (equals(emailAddress, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			emailAddresses + " does not contain " + emailAddress, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		EmailAddress emailAddress1, EmailAddress emailAddress2) {

		Assert.assertTrue(
			emailAddress1 + " does not equal " + emailAddress2,
			equals(emailAddress1, emailAddress2));
	}

	protected void assertEquals(
		List<EmailAddress> emailAddresses1,
		List<EmailAddress> emailAddresses2) {

		Assert.assertEquals(emailAddresses1.size(), emailAddresses2.size());

		for (int i = 0; i < emailAddresses1.size(); i++) {
			EmailAddress emailAddress1 = emailAddresses1.get(i);
			EmailAddress emailAddress2 = emailAddresses2.get(i);

			assertEquals(emailAddress1, emailAddress2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<EmailAddress> emailAddresses1,
		List<EmailAddress> emailAddresses2) {

		Assert.assertEquals(emailAddresses1.size(), emailAddresses2.size());

		for (EmailAddress emailAddress1 : emailAddresses1) {
			boolean contains = false;

			for (EmailAddress emailAddress2 : emailAddresses2) {
				if (equals(emailAddress1, emailAddress2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				emailAddresses2 + " does not contain " + emailAddress1,
				contains);
		}
	}

	protected void assertValid(EmailAddress emailAddress) throws Exception {
		boolean valid = true;

		if (emailAddress.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("emailAddress", additionalAssertFieldName)) {
				if (emailAddress.getEmailAddress() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("primary", additionalAssertFieldName)) {
				if (emailAddress.getPrimary() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("type", additionalAssertFieldName)) {
				if (emailAddress.getType() == null) {
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

	protected void assertValid(Page<EmailAddress> page) {
		assertValid(page, Collections.emptyMap());
	}

	protected void assertValid(
		Page<EmailAddress> page,
		Map<String, Map<String, String>> expectedActions) {

		boolean valid = false;

		java.util.Collection<EmailAddress> emailAddresses = page.getItems();

		int size = emailAddresses.size();

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
					com.liferay.headless.admin.user.dto.v1_0.EmailAddress.
						class)) {

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
		EmailAddress emailAddress1, EmailAddress emailAddress2) {

		if (emailAddress1 == emailAddress2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("emailAddress", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						emailAddress1.getEmailAddress(),
						emailAddress2.getEmailAddress())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						emailAddress1.getId(), emailAddress2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("primary", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						emailAddress1.getPrimary(),
						emailAddress2.getPrimary())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("type", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						emailAddress1.getType(), emailAddress2.getType())) {

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

		if (!(_emailAddressResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_emailAddressResource;

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
		EntityField entityField, String operator, EmailAddress emailAddress) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("emailAddress")) {
			sb.append("'");
			sb.append(String.valueOf(emailAddress.getEmailAddress()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("primary")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("type")) {
			sb.append("'");
			sb.append(String.valueOf(emailAddress.getType()));
			sb.append("'");

			return sb.toString();
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

	protected EmailAddress randomEmailAddress() throws Exception {
		return new EmailAddress() {
			{
				emailAddress =
					StringUtil.toLowerCase(RandomTestUtil.randomString()) +
						"@liferay.com";
				id = RandomTestUtil.randomLong();
				primary = RandomTestUtil.randomBoolean();
				type = StringUtil.toLowerCase(RandomTestUtil.randomString());
			}
		};
	}

	protected EmailAddress randomIrrelevantEmailAddress() throws Exception {
		EmailAddress randomIrrelevantEmailAddress = randomEmailAddress();

		return randomIrrelevantEmailAddress;
	}

	protected EmailAddress randomPatchEmailAddress() throws Exception {
		return randomEmailAddress();
	}

	protected EmailAddressResource emailAddressResource;
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
		LogFactoryUtil.getLog(BaseEmailAddressResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.admin.user.resource.v1_0.EmailAddressResource
		_emailAddressResource;

}