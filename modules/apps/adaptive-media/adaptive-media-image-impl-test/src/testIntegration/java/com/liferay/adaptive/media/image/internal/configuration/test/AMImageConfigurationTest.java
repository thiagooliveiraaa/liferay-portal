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

package com.liferay.adaptive.media.image.internal.configuration.test;

import com.liferay.adaptive.media.image.configuration.AMImageConfigurationEntry;
import com.liferay.adaptive.media.image.configuration.AMImageConfigurationHelper;
import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Sergio González
 */
@RunWith(Arquillian.class)
public class AMImageConfigurationTest extends BaseAMImageConfigurationTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testEmptyConfiguration() throws Exception {
		Iterable<AMImageConfigurationEntry> amImageConfigurationEntries =
			_amImageConfigurationHelper.getAMImageConfigurationEntries(
				TestPropsValues.getCompanyId());

		Iterator<AMImageConfigurationEntry> iterator =
			amImageConfigurationEntries.iterator();

		Assert.assertFalse(iterator.hasNext());
	}

	@Test
	public void testExistantConfigurationEntry() throws Exception {
		_amImageConfigurationHelper.addAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "one", "desc", "1",
			HashMapBuilder.put(
				"max-height", "100"
			).put(
				"max-width", "100"
			).build());

		Assert.assertNotNull(
			_amImageConfigurationHelper.getAMImageConfigurationEntry(
				TestPropsValues.getCompanyId(), "1"));
	}

	@Test
	public void testGetConfigurationEntriesDoesNotReturnDisabledConfigurations()
		throws Exception {

		_amImageConfigurationHelper.addAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "one", "onedesc", "1",
			HashMapBuilder.put(
				"max-height", "100"
			).put(
				"max-width", "100"
			).build());

		_amImageConfigurationHelper.addAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "two", "twodesc", "2",
			HashMapBuilder.put(
				"max-height", "200"
			).put(
				"max-width", "200"
			).build());

		_amImageConfigurationHelper.disableAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "2");

		Collection<AMImageConfigurationEntry> amImageConfigurationEntries =
			_amImageConfigurationHelper.getAMImageConfigurationEntries(
				TestPropsValues.getCompanyId());

		Assert.assertEquals(
			amImageConfigurationEntries.toString(), 1,
			amImageConfigurationEntries.size());

		Iterator<AMImageConfigurationEntry> iterator =
			amImageConfigurationEntries.iterator();

		AMImageConfigurationEntry amImageConfigurationEntry = iterator.next();

		Assert.assertEquals("1", amImageConfigurationEntry.getUUID());
	}

	@Test
	public void testGetConfigurationEntriesWithFilterReturnsDisabledConfigurations()
		throws Exception {

		_amImageConfigurationHelper.addAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "one", "onedesc", "1",
			HashMapBuilder.put(
				"max-height", "100"
			).put(
				"max-width", "100"
			).build());

		_amImageConfigurationHelper.addAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "two", "twodesc", "2",
			HashMapBuilder.put(
				"max-height", "200"
			).put(
				"max-width", "200"
			).build());

		_amImageConfigurationHelper.disableAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "2");

		Collection<AMImageConfigurationEntry> amImageConfigurationEntries =
			_amImageConfigurationHelper.getAMImageConfigurationEntries(
				TestPropsValues.getCompanyId(),
				amImageConfigurationEntry -> true);

		Assert.assertEquals(
			amImageConfigurationEntries.toString(), 2,
			amImageConfigurationEntries.size());

		Iterator<AMImageConfigurationEntry> iterator =
			amImageConfigurationEntries.iterator();

		AMImageConfigurationEntry amImageConfigurationEntry = iterator.next();

		Assert.assertEquals("1", amImageConfigurationEntry.getUUID());

		amImageConfigurationEntry = iterator.next();

		Assert.assertEquals("2", amImageConfigurationEntry.getUUID());
	}

	@Test
	public void testGetConfigurationEntryReturnsDisabledConfiguration()
		throws Exception {

		_amImageConfigurationHelper.addAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "one", "desc", "1",
			HashMapBuilder.put(
				"max-height", "100"
			).put(
				"max-width", "100"
			).build());

		_amImageConfigurationHelper.disableAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "1");

		Assert.assertNotNull(
			_amImageConfigurationHelper.getAMImageConfigurationEntry(
				TestPropsValues.getCompanyId(), "1"));
	}

	@Test
	public void testNonemptyConfiguration() throws Exception {
		_amImageConfigurationHelper.addAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "one", "desc", "1",
			HashMapBuilder.put(
				"max-height", "100"
			).put(
				"max-width", "100"
			).build());

		Collection<AMImageConfigurationEntry> amImageConfigurationEntries =
			_amImageConfigurationHelper.getAMImageConfigurationEntries(
				TestPropsValues.getCompanyId());

		Assert.assertFalse(
			amImageConfigurationEntries.toString(),
			amImageConfigurationEntries.isEmpty());
	}

	@Test
	public void testNonexistentConfigurationEntry() throws Exception {
		_amImageConfigurationHelper.addAMImageConfigurationEntry(
			TestPropsValues.getCompanyId(), "one", "desc", "1",
			HashMapBuilder.put(
				"max-height", "100"
			).put(
				"max-width", "100"
			).build());

		Assert.assertNull(
			_amImageConfigurationHelper.getAMImageConfigurationEntry(
				TestPropsValues.getCompanyId(), "0"));
	}

	@Override
	protected AMImageConfigurationHelper getAMImageConfigurationHelper() {
		return _amImageConfigurationHelper;
	}

	@Inject
	private AMImageConfigurationHelper _amImageConfigurationHelper;

}