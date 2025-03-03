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

package com.liferay.portal.workflow.kaleo.service.persistence.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.util.IntegerWrapper;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PersistenceTestRule;
import com.liferay.portal.test.rule.TransactionalTestRule;
import com.liferay.portal.workflow.kaleo.exception.NoSuchDefinitionException;
import com.liferay.portal.workflow.kaleo.model.KaleoDefinition;
import com.liferay.portal.workflow.kaleo.service.KaleoDefinitionLocalServiceUtil;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoDefinitionPersistence;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoDefinitionUtil;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @generated
 */
@RunWith(Arquillian.class)
public class KaleoDefinitionPersistenceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(
				Propagation.REQUIRED,
				"com.liferay.portal.workflow.kaleo.service"));

	@Before
	public void setUp() {
		_persistence = KaleoDefinitionUtil.getPersistence();

		Class<?> clazz = _persistence.getClass();

		_dynamicQueryClassLoader = clazz.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
		Iterator<KaleoDefinition> iterator = _kaleoDefinitions.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		KaleoDefinition kaleoDefinition = _persistence.create(pk);

		Assert.assertNotNull(kaleoDefinition);

		Assert.assertEquals(kaleoDefinition.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		KaleoDefinition newKaleoDefinition = addKaleoDefinition();

		_persistence.remove(newKaleoDefinition);

		KaleoDefinition existingKaleoDefinition =
			_persistence.fetchByPrimaryKey(newKaleoDefinition.getPrimaryKey());

		Assert.assertNull(existingKaleoDefinition);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addKaleoDefinition();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		KaleoDefinition newKaleoDefinition = _persistence.create(pk);

		newKaleoDefinition.setMvccVersion(RandomTestUtil.nextLong());

		newKaleoDefinition.setCtCollectionId(RandomTestUtil.nextLong());

		newKaleoDefinition.setGroupId(RandomTestUtil.nextLong());

		newKaleoDefinition.setCompanyId(RandomTestUtil.nextLong());

		newKaleoDefinition.setUserId(RandomTestUtil.nextLong());

		newKaleoDefinition.setUserName(RandomTestUtil.randomString());

		newKaleoDefinition.setCreateDate(RandomTestUtil.nextDate());

		newKaleoDefinition.setModifiedDate(RandomTestUtil.nextDate());

		newKaleoDefinition.setName(RandomTestUtil.randomString());

		newKaleoDefinition.setTitle(RandomTestUtil.randomString());

		newKaleoDefinition.setDescription(RandomTestUtil.randomString());

		newKaleoDefinition.setContent(RandomTestUtil.randomString());

		newKaleoDefinition.setScope(RandomTestUtil.randomString());

		newKaleoDefinition.setVersion(RandomTestUtil.nextInt());

		newKaleoDefinition.setActive(RandomTestUtil.randomBoolean());

		_kaleoDefinitions.add(_persistence.update(newKaleoDefinition));

		KaleoDefinition existingKaleoDefinition = _persistence.findByPrimaryKey(
			newKaleoDefinition.getPrimaryKey());

		Assert.assertEquals(
			existingKaleoDefinition.getMvccVersion(),
			newKaleoDefinition.getMvccVersion());
		Assert.assertEquals(
			existingKaleoDefinition.getCtCollectionId(),
			newKaleoDefinition.getCtCollectionId());
		Assert.assertEquals(
			existingKaleoDefinition.getKaleoDefinitionId(),
			newKaleoDefinition.getKaleoDefinitionId());
		Assert.assertEquals(
			existingKaleoDefinition.getGroupId(),
			newKaleoDefinition.getGroupId());
		Assert.assertEquals(
			existingKaleoDefinition.getCompanyId(),
			newKaleoDefinition.getCompanyId());
		Assert.assertEquals(
			existingKaleoDefinition.getUserId(),
			newKaleoDefinition.getUserId());
		Assert.assertEquals(
			existingKaleoDefinition.getUserName(),
			newKaleoDefinition.getUserName());
		Assert.assertEquals(
			Time.getShortTimestamp(existingKaleoDefinition.getCreateDate()),
			Time.getShortTimestamp(newKaleoDefinition.getCreateDate()));
		Assert.assertEquals(
			Time.getShortTimestamp(existingKaleoDefinition.getModifiedDate()),
			Time.getShortTimestamp(newKaleoDefinition.getModifiedDate()));
		Assert.assertEquals(
			existingKaleoDefinition.getName(), newKaleoDefinition.getName());
		Assert.assertEquals(
			existingKaleoDefinition.getTitle(), newKaleoDefinition.getTitle());
		Assert.assertEquals(
			existingKaleoDefinition.getDescription(),
			newKaleoDefinition.getDescription());
		Assert.assertEquals(
			existingKaleoDefinition.getContent(),
			newKaleoDefinition.getContent());
		Assert.assertEquals(
			existingKaleoDefinition.getScope(), newKaleoDefinition.getScope());
		Assert.assertEquals(
			existingKaleoDefinition.getVersion(),
			newKaleoDefinition.getVersion());
		Assert.assertEquals(
			existingKaleoDefinition.isActive(), newKaleoDefinition.isActive());
	}

	@Test
	public void testCountByCompanyId() throws Exception {
		_persistence.countByCompanyId(RandomTestUtil.nextLong());

		_persistence.countByCompanyId(0L);
	}

	@Test
	public void testCountByC_N() throws Exception {
		_persistence.countByC_N(RandomTestUtil.nextLong(), "");

		_persistence.countByC_N(0L, "null");

		_persistence.countByC_N(0L, (String)null);
	}

	@Test
	public void testCountByC_S() throws Exception {
		_persistence.countByC_S(RandomTestUtil.nextLong(), "");

		_persistence.countByC_S(0L, "null");

		_persistence.countByC_S(0L, (String)null);
	}

	@Test
	public void testCountByC_A() throws Exception {
		_persistence.countByC_A(
			RandomTestUtil.nextLong(), RandomTestUtil.randomBoolean());

		_persistence.countByC_A(0L, RandomTestUtil.randomBoolean());
	}

	@Test
	public void testCountByC_N_V() throws Exception {
		_persistence.countByC_N_V(
			RandomTestUtil.nextLong(), "", RandomTestUtil.nextInt());

		_persistence.countByC_N_V(0L, "null", 0);

		_persistence.countByC_N_V(0L, (String)null, 0);
	}

	@Test
	public void testCountByC_N_A() throws Exception {
		_persistence.countByC_N_A(
			RandomTestUtil.nextLong(), "", RandomTestUtil.randomBoolean());

		_persistence.countByC_N_A(0L, "null", RandomTestUtil.randomBoolean());

		_persistence.countByC_N_A(
			0L, (String)null, RandomTestUtil.randomBoolean());
	}

	@Test
	public void testCountByC_S_A() throws Exception {
		_persistence.countByC_S_A(
			RandomTestUtil.nextLong(), "", RandomTestUtil.randomBoolean());

		_persistence.countByC_S_A(0L, "null", RandomTestUtil.randomBoolean());

		_persistence.countByC_S_A(
			0L, (String)null, RandomTestUtil.randomBoolean());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		KaleoDefinition newKaleoDefinition = addKaleoDefinition();

		KaleoDefinition existingKaleoDefinition = _persistence.findByPrimaryKey(
			newKaleoDefinition.getPrimaryKey());

		Assert.assertEquals(existingKaleoDefinition, newKaleoDefinition);
	}

	@Test(expected = NoSuchDefinitionException.class)
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		_persistence.findByPrimaryKey(pk);
	}

	@Test
	public void testFindAll() throws Exception {
		_persistence.findAll(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, getOrderByComparator());
	}

	protected OrderByComparator<KaleoDefinition> getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create(
			"KaleoDefinition", "mvccVersion", true, "ctCollectionId", true,
			"kaleoDefinitionId", true, "groupId", true, "companyId", true,
			"userId", true, "userName", true, "createDate", true,
			"modifiedDate", true, "name", true, "title", true, "description",
			true, "scope", true, "version", true, "active", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		KaleoDefinition newKaleoDefinition = addKaleoDefinition();

		KaleoDefinition existingKaleoDefinition =
			_persistence.fetchByPrimaryKey(newKaleoDefinition.getPrimaryKey());

		Assert.assertEquals(existingKaleoDefinition, newKaleoDefinition);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		KaleoDefinition missingKaleoDefinition = _persistence.fetchByPrimaryKey(
			pk);

		Assert.assertNull(missingKaleoDefinition);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {

		KaleoDefinition newKaleoDefinition1 = addKaleoDefinition();
		KaleoDefinition newKaleoDefinition2 = addKaleoDefinition();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newKaleoDefinition1.getPrimaryKey());
		primaryKeys.add(newKaleoDefinition2.getPrimaryKey());

		Map<Serializable, KaleoDefinition> kaleoDefinitions =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(2, kaleoDefinitions.size());
		Assert.assertEquals(
			newKaleoDefinition1,
			kaleoDefinitions.get(newKaleoDefinition1.getPrimaryKey()));
		Assert.assertEquals(
			newKaleoDefinition2,
			kaleoDefinitions.get(newKaleoDefinition2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {

		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, KaleoDefinition> kaleoDefinitions =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(kaleoDefinitions.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {

		KaleoDefinition newKaleoDefinition = addKaleoDefinition();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newKaleoDefinition.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, KaleoDefinition> kaleoDefinitions =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, kaleoDefinitions.size());
		Assert.assertEquals(
			newKaleoDefinition,
			kaleoDefinitions.get(newKaleoDefinition.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys() throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, KaleoDefinition> kaleoDefinitions =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(kaleoDefinitions.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey() throws Exception {
		KaleoDefinition newKaleoDefinition = addKaleoDefinition();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newKaleoDefinition.getPrimaryKey());

		Map<Serializable, KaleoDefinition> kaleoDefinitions =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, kaleoDefinitions.size());
		Assert.assertEquals(
			newKaleoDefinition,
			kaleoDefinitions.get(newKaleoDefinition.getPrimaryKey()));
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery =
			KaleoDefinitionLocalServiceUtil.getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod<KaleoDefinition>() {

				@Override
				public void performAction(KaleoDefinition kaleoDefinition) {
					Assert.assertNotNull(kaleoDefinition);

					count.increment();
				}

			});

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting() throws Exception {
		KaleoDefinition newKaleoDefinition = addKaleoDefinition();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			KaleoDefinition.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"kaleoDefinitionId",
				newKaleoDefinition.getKaleoDefinitionId()));

		List<KaleoDefinition> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		Assert.assertEquals(1, result.size());

		KaleoDefinition existingKaleoDefinition = result.get(0);

		Assert.assertEquals(existingKaleoDefinition, newKaleoDefinition);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			KaleoDefinition.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"kaleoDefinitionId", RandomTestUtil.nextLong()));

		List<KaleoDefinition> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting() throws Exception {
		KaleoDefinition newKaleoDefinition = addKaleoDefinition();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			KaleoDefinition.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("kaleoDefinitionId"));

		Object newKaleoDefinitionId = newKaleoDefinition.getKaleoDefinitionId();

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"kaleoDefinitionId", new Object[] {newKaleoDefinitionId}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingKaleoDefinitionId = result.get(0);

		Assert.assertEquals(existingKaleoDefinitionId, newKaleoDefinitionId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			KaleoDefinition.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("kaleoDefinitionId"));

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"kaleoDefinitionId", new Object[] {RandomTestUtil.nextLong()}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		KaleoDefinition newKaleoDefinition = addKaleoDefinition();

		_persistence.clearCache();

		_assertOriginalValues(
			_persistence.findByPrimaryKey(newKaleoDefinition.getPrimaryKey()));
	}

	@Test
	public void testResetOriginalValuesWithDynamicQueryLoadFromDatabase()
		throws Exception {

		_testResetOriginalValuesWithDynamicQuery(true);
	}

	@Test
	public void testResetOriginalValuesWithDynamicQueryLoadFromSession()
		throws Exception {

		_testResetOriginalValuesWithDynamicQuery(false);
	}

	private void _testResetOriginalValuesWithDynamicQuery(boolean clearSession)
		throws Exception {

		KaleoDefinition newKaleoDefinition = addKaleoDefinition();

		if (clearSession) {
			Session session = _persistence.openSession();

			session.flush();

			session.clear();
		}

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			KaleoDefinition.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"kaleoDefinitionId",
				newKaleoDefinition.getKaleoDefinitionId()));

		List<KaleoDefinition> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		_assertOriginalValues(result.get(0));
	}

	private void _assertOriginalValues(KaleoDefinition kaleoDefinition) {
		Assert.assertEquals(
			Long.valueOf(kaleoDefinition.getCompanyId()),
			ReflectionTestUtil.<Long>invoke(
				kaleoDefinition, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "companyId"));
		Assert.assertEquals(
			kaleoDefinition.getName(),
			ReflectionTestUtil.invoke(
				kaleoDefinition, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "name"));

		Assert.assertEquals(
			Long.valueOf(kaleoDefinition.getCompanyId()),
			ReflectionTestUtil.<Long>invoke(
				kaleoDefinition, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "companyId"));
		Assert.assertEquals(
			kaleoDefinition.getName(),
			ReflectionTestUtil.invoke(
				kaleoDefinition, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "name"));
		Assert.assertEquals(
			Integer.valueOf(kaleoDefinition.getVersion()),
			ReflectionTestUtil.<Integer>invoke(
				kaleoDefinition, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "version"));

		Assert.assertEquals(
			Long.valueOf(kaleoDefinition.getCompanyId()),
			ReflectionTestUtil.<Long>invoke(
				kaleoDefinition, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "companyId"));
		Assert.assertEquals(
			kaleoDefinition.getName(),
			ReflectionTestUtil.invoke(
				kaleoDefinition, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "name"));
		Assert.assertEquals(
			Boolean.valueOf(kaleoDefinition.getActive()),
			ReflectionTestUtil.<Boolean>invoke(
				kaleoDefinition, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "active_"));
	}

	protected KaleoDefinition addKaleoDefinition() throws Exception {
		long pk = RandomTestUtil.nextLong();

		KaleoDefinition kaleoDefinition = _persistence.create(pk);

		kaleoDefinition.setMvccVersion(RandomTestUtil.nextLong());

		kaleoDefinition.setCtCollectionId(RandomTestUtil.nextLong());

		kaleoDefinition.setGroupId(RandomTestUtil.nextLong());

		kaleoDefinition.setCompanyId(RandomTestUtil.nextLong());

		kaleoDefinition.setUserId(RandomTestUtil.nextLong());

		kaleoDefinition.setUserName(RandomTestUtil.randomString());

		kaleoDefinition.setCreateDate(RandomTestUtil.nextDate());

		kaleoDefinition.setModifiedDate(RandomTestUtil.nextDate());

		kaleoDefinition.setName(RandomTestUtil.randomString());

		kaleoDefinition.setTitle(RandomTestUtil.randomString());

		kaleoDefinition.setDescription(RandomTestUtil.randomString());

		kaleoDefinition.setContent(RandomTestUtil.randomString());

		kaleoDefinition.setScope(RandomTestUtil.randomString());

		kaleoDefinition.setVersion(RandomTestUtil.nextInt());

		kaleoDefinition.setActive(RandomTestUtil.randomBoolean());

		_kaleoDefinitions.add(_persistence.update(kaleoDefinition));

		return kaleoDefinition;
	}

	private List<KaleoDefinition> _kaleoDefinitions =
		new ArrayList<KaleoDefinition>();
	private KaleoDefinitionPersistence _persistence;
	private ClassLoader _dynamicQueryClassLoader;

}