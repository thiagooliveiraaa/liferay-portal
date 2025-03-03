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

package com.liferay.document.library.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.document.library.kernel.model.DLFileEntryMetadata;
import com.liferay.document.library.kernel.model.DLFileEntryType;
import com.liferay.document.library.kernel.service.DLFileEntryTypeLocalServiceUtil;
import com.liferay.document.library.kernel.service.persistence.DLFileEntryTypeFinderUtil;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.test.util.DDMStructureTestUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;
import com.liferay.portal.test.rule.TransactionalTestRule;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Roberto Díaz
 */
@RunWith(Arquillian.class)
public class DLFileEntryTypeFinderTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE,
			TransactionalTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_user = UserTestUtil.addUser();
	}

	@Test
	public void testFilterCountByKeywords() throws Exception {
		int initialFileEntryTypesCount =
			DLFileEntryTypeFinderUtil.filterCountByKeywords(
				_group.getCompanyId(), new long[] {_group.getGroupId()},
				_DL_FILE_ENTRY_TYPE_NAME, true);

		addFileEntryType(
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _user.getUserId()));

		Assert.assertEquals(
			initialFileEntryTypesCount + 1,
			DLFileEntryTypeFinderUtil.filterCountByKeywords(
				_group.getCompanyId(), new long[] {_group.getGroupId()},
				_DL_FILE_ENTRY_TYPE_NAME, true));
	}

	@Test
	public void testFilterCountByKeywordsAndBlankKeywordsBasicType()
		throws Exception {

		Assert.assertEquals(
			1,
			DLFileEntryTypeFinderUtil.filterCountByKeywords(
				_group.getCompanyId(), new long[] {_group.getGroupId()},
				StringPool.BLANK, true));
	}

	@Test
	public void testFilterCountByKeywordsAsPowerUser() throws Exception {
		User user = UserTestUtil.addGroupUser(_group, RoleConstants.POWER_USER);

		PermissionChecker permissionChecker =
			PermissionCheckerFactoryUtil.create(user);

		PermissionChecker originalPermissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		PermissionThreadLocal.setPermissionChecker(permissionChecker);

		try {
			int initialFileEntryTypesCount =
				DLFileEntryTypeFinderUtil.filterCountByKeywords(
					_group.getCompanyId(), new long[] {_group.getGroupId()},
					_DL_FILE_ENTRY_TYPE_NAME, true);

			addFileEntryType(
				ServiceContextTestUtil.getServiceContext(
					_group.getGroupId(), _user.getUserId()));

			Assert.assertEquals(
				initialFileEntryTypesCount + 1,
				DLFileEntryTypeFinderUtil.filterCountByKeywords(
					_group.getCompanyId(), new long[] {_group.getGroupId()},
					_DL_FILE_ENTRY_TYPE_NAME, true));
		}
		finally {
			PermissionThreadLocal.setPermissionChecker(
				originalPermissionChecker);
		}
	}

	@Test
	public void testFilterCountByKeywordsWithBlankKeywords() throws Exception {
		int initialFileEntryTypesCount =
			DLFileEntryTypeFinderUtil.filterCountByKeywords(
				_group.getCompanyId(), new long[] {_group.getGroupId()},
				StringPool.BLANK, true);

		addFileEntryType(
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _user.getUserId()));

		Assert.assertEquals(
			initialFileEntryTypesCount + 1,
			DLFileEntryTypeFinderUtil.filterCountByKeywords(
				_group.getCompanyId(), new long[] {_group.getGroupId()},
				StringPool.BLANK, true));
	}

	@Test
	public void testFilterCountByKeywordsWithBlankKeywordsExcludingBasicType()
		throws Exception {

		int initialFileEntryTypesCount =
			DLFileEntryTypeFinderUtil.filterCountByKeywords(
				_group.getCompanyId(), new long[] {_group.getGroupId()},
				StringPool.BLANK, false);

		addFileEntryType(
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _user.getUserId()));

		Assert.assertEquals(
			initialFileEntryTypesCount + 1,
			DLFileEntryTypeFinderUtil.filterCountByKeywords(
				_group.getCompanyId(), new long[] {_group.getGroupId()},
				StringPool.BLANK, false));
	}

	@Test
	public void testFilterFindByKeywords() throws Exception {
		DLFileEntryType fileEntryType = addFileEntryType(
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _user.getUserId()));

		List<DLFileEntryType> fileEntryTypes =
			DLFileEntryTypeFinderUtil.filterFindByKeywords(
				_group.getCompanyId(), new long[] {_group.getGroupId()},
				_DL_FILE_ENTRY_TYPE_NAME, true, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, null);

		Assert.assertEquals(
			fileEntryTypes.toString(), 1, fileEntryTypes.size());
		Assert.assertTrue(
			fileEntryTypes.toString(), fileEntryTypes.contains(fileEntryType));
	}

	@Test
	public void testFilterFindByKeywordsAsPowerUser() throws Exception {
		User user = UserTestUtil.addGroupUser(_group, RoleConstants.POWER_USER);

		PermissionChecker permissionChecker =
			PermissionCheckerFactoryUtil.create(user);

		PermissionChecker originalPermissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		PermissionThreadLocal.setPermissionChecker(permissionChecker);

		try {
			DLFileEntryType fileEntryType = addFileEntryType(
				ServiceContextTestUtil.getServiceContext(
					_group.getGroupId(), _user.getUserId()));

			List<DLFileEntryType> fileEntryTypes =
				DLFileEntryTypeFinderUtil.filterFindByKeywords(
					_group.getCompanyId(), new long[] {_group.getGroupId()},
					_DL_FILE_ENTRY_TYPE_NAME, true, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null);

			Assert.assertEquals(
				fileEntryTypes.toString(), 1, fileEntryTypes.size());
			Assert.assertTrue(
				fileEntryTypes.toString(),
				fileEntryTypes.contains(fileEntryType));
		}
		finally {
			PermissionThreadLocal.setPermissionChecker(
				originalPermissionChecker);
		}
	}

	@Test
	public void testFilterFindByKeywordsAsSiteMember() throws Exception {
		User user = UserTestUtil.addGroupUser(
			_group, RoleConstants.SITE_MEMBER);

		PermissionChecker permissionChecker =
			PermissionCheckerFactoryUtil.create(user);

		PermissionChecker originalPermissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		PermissionThreadLocal.setPermissionChecker(permissionChecker);

		try {
			DLFileEntryType fileEntryType = addFileEntryType(
				ServiceContextTestUtil.getServiceContext(
					_group.getGroupId(), _user.getUserId()));

			List<DLFileEntryType> fileEntryTypes =
				DLFileEntryTypeFinderUtil.filterFindByKeywords(
					_group.getCompanyId(), new long[] {_group.getGroupId()},
					_DL_FILE_ENTRY_TYPE_NAME, true, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null);

			Assert.assertEquals(
				fileEntryTypes.toString(), 0, fileEntryTypes.size());
			Assert.assertFalse(
				fileEntryTypes.toString(),
				fileEntryTypes.contains(fileEntryType));
		}
		finally {
			PermissionThreadLocal.setPermissionChecker(
				originalPermissionChecker);
		}
	}

	@Test
	public void testFilterFindByKeywordsWithBlankKeywords() throws Exception {
		DLFileEntryType fileEntryType = addFileEntryType(
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _user.getUserId()));

		List<DLFileEntryType> fileEntryTypes =
			DLFileEntryTypeFinderUtil.filterFindByKeywords(
				_group.getCompanyId(), new long[] {_group.getGroupId()},
				StringPool.BLANK, true, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
				null);

		Assert.assertEquals(
			fileEntryTypes.toString(), 2, fileEntryTypes.size());
		Assert.assertTrue(
			fileEntryTypes.toString(), fileEntryTypes.contains(fileEntryType));

		DLFileEntryType basicFileEntryType =
			DLFileEntryTypeLocalServiceUtil.getFileEntryType(
				0, "BASIC-DOCUMENT");

		Assert.assertTrue(
			fileEntryTypes.toString(),
			fileEntryTypes.contains(basicFileEntryType));
	}

	@Test
	public void testFilterFindByKeywordsWithBlankKeywordsBasicType()
		throws Exception {

		List<DLFileEntryType> fileEntryTypes =
			DLFileEntryTypeFinderUtil.filterFindByKeywords(
				_group.getCompanyId(), new long[] {_group.getGroupId()},
				StringPool.BLANK, true, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
				null);

		Assert.assertEquals(
			fileEntryTypes.toString(), 1, fileEntryTypes.size());

		DLFileEntryType basicFileEntryType =
			DLFileEntryTypeLocalServiceUtil.getFileEntryType(
				0, "BASIC-DOCUMENT");

		Assert.assertTrue(
			fileEntryTypes.toString(),
			fileEntryTypes.contains(basicFileEntryType));
	}

	@Test
	public void testFilterFindByKeywordsWithBlankKeywordsExcludingBasicType()
		throws Exception {

		DLFileEntryType fileEntryType = addFileEntryType(
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _user.getUserId()));

		List<DLFileEntryType> fileEntryTypes =
			DLFileEntryTypeFinderUtil.filterFindByKeywords(
				_group.getCompanyId(), new long[] {_group.getGroupId()},
				StringPool.BLANK, false, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
				null);

		Assert.assertEquals(
			fileEntryTypes.toString(), 1, fileEntryTypes.size());
		Assert.assertTrue(
			fileEntryTypes.toString(), fileEntryTypes.contains(fileEntryType));
	}

	protected DLFileEntryType addFileEntryType(ServiceContext serviceContext)
		throws Exception {

		DDMStructure ddmStructure = DDMStructureTestUtil.addStructure(
			_group.getGroupId(), DLFileEntryMetadata.class.getName());

		return DLFileEntryTypeLocalServiceUtil.addFileEntryType(
			_user.getUserId(), _group.getGroupId(), _DL_FILE_ENTRY_TYPE_NAME,
			RandomTestUtil.randomString(),
			new long[] {ddmStructure.getStructureId()}, serviceContext);
	}

	private static final String _DL_FILE_ENTRY_TYPE_NAME =
		"DLFileEntryTypeFinderTest";

	@DeleteAfterTestRun
	private Group _group;

	@DeleteAfterTestRun
	private User _user;

}