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

package com.liferay.portal.tools.service.builder.test.service.persistence.impl;

import com.liferay.portal.kernel.test.ReflectionTestUtil;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Tina Tian
 */
public class FinderWhereClauseEntryPersistenceImplTest {

	@Test
	public void testFinderWhereClause() {
		Assert.assertEquals(
			"finderWhereClauseEntry.name = ? AND finderWhereClauseEntry." +
				"nickname IS NOT NULL",
			ReflectionTestUtil.getFieldValue(
				FinderWhereClauseEntryPersistenceImpl.class,
				"_FINDER_COLUMN_NAME_NICKNAME_NAME_2"));
	}

}