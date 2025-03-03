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

package com.liferay.headless.site.resource.v1_0.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.headless.site.client.dto.v1_0.Site;
import com.liferay.headless.site.client.problem.Problem;
import com.liferay.headless.site.client.resource.v1_0.SiteResource;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.model.LayoutSetPrototype;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutSetPrototypeLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.log.LogCapture;
import com.liferay.portal.test.log.LoggerTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.site.initializer.SiteInitializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Rubén Pulido
 */
@RunWith(Arquillian.class)
public class SiteResourceTest extends BaseSiteResourceTestCase {

	@Before
	@Override
	public void setUp() throws Exception {
		SiteResource.Builder builder = SiteResource.builder();

		siteResource = builder.authentication(
			"test@liferay.com", "test"
		).locale(
			LocaleUtil.getDefault()
		).build();
	}

	@After
	@Override
	public void tearDown() throws Exception {
		Collections.reverse(_sites);

		for (Site site : _sites) {
			_groupLocalService.deleteGroup(site.getId());
		}
	}

	@Override
	@Test
	public void testPostSite() throws Exception {
		super.testPostSite();

		_testPostSiteFailureDuplicateName();
		_testPostSiteFailureInvalidKey();
		_testPostSiteFailureNoName();
		_testPostSiteFailureParentSiteNotFound();
		_testPostSiteFailureSiteInitializerInactive();
		_testPostSiteFailureSiteInitializerNotFound();
		_testPostSiteFailureSiteTemplateInactive();
		_testPostSiteFailureSiteTemplateNotFound();
		_testPostSiteFailureTemplateKeyNoTemplateType();
		_testPostSiteFailureTemplateTypeNoTemplateKey();
		_testPostSiteSuccessChild();
		_testPostSiteSuccessMembershipTypePrivate();
		_testPostSiteSuccessSiteInitializer();
		_testPostSiteSuccessSiteTemplate();
	}

	@Override
	protected Site randomSite() throws Exception {
		return new Site() {
			{
				name = StringUtil.toLowerCase(RandomTestUtil.randomString());
			}
		};
	}

	@Override
	protected Site testPostSite_addSite(Site site) throws Exception {
		Site postSite = siteResource.postSite(site);

		_sites.add(postSite);

		return postSite;
	}

	private void _testPostSiteFailureDuplicateName() throws Exception {
		Site randomSite = randomSite();

		testPostSite_addSite(randomSite);

		try {
			try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
					_CLASS_NAME_EXCEPTION_MAPPER, LoggerTestUtil.ERROR)) {

				testPostSite_addSite(randomSite);
			}

			Assert.fail();
		}
		catch (Problem.ProblemException problemException) {
			Problem problem = problemException.getProblem();

			Assert.assertEquals("CONFLICT", problem.getStatus());
			Assert.assertEquals(
				"A site with the same key already exists", problem.getTitle());
		}
	}

	private void _testPostSiteFailureInvalidKey() throws Exception {
		Site randomSite = randomSite();

		randomSite.setName("*");

		try {
			testPostSite_addSite(randomSite);

			Assert.fail();
		}
		catch (Problem.ProblemException problemException) {
			Problem problem = problemException.getProblem();

			Assert.assertEquals("BAD_REQUEST", problem.getStatus());
			Assert.assertEquals("Site key is invalid", problem.getTitle());
		}
	}

	private void _testPostSiteFailureNoName() throws Exception {
		Site randomSite = randomSite();

		randomSite.setName((String)null);

		try {
			testPostSite_addSite(randomSite);

			Assert.fail();
		}
		catch (Problem.ProblemException problemException) {
			Problem problem = problemException.getProblem();

			Assert.assertEquals("BAD_REQUEST", problem.getStatus());

			String title = problem.getTitle();

			Assert.assertTrue(title.contains("name must not be empty"));
		}
	}

	private void _testPostSiteFailureParentSiteNotFound() throws Exception {
		Site randomSite = randomSite();

		randomSite.setParentSiteKey(
			StringUtil.toLowerCase(RandomTestUtil.randomString()));

		try {
			testPostSite_addSite(randomSite);

			Assert.fail();
		}
		catch (Problem.ProblemException problemException) {
			Problem problem = problemException.getProblem();

			Assert.assertEquals("NOT_FOUND", problem.getStatus());
			Assert.assertEquals(
				"No site exists for site key " + randomSite.getParentSiteKey(),
				problem.getTitle());
		}
	}

	private void _testPostSiteFailureSiteInitializerInactive()
		throws Exception {

		Bundle bundle = FrameworkUtil.getBundle(SiteResourceTest.class);

		BundleContext bundleContext = bundle.getBundleContext();

		String siteInitializerKey = RandomTestUtil.randomString();

		ServiceRegistration<SiteInitializer> serviceRegistration =
			bundleContext.registerService(
				SiteInitializer.class,
				new TestSiteInitializer(siteInitializerKey),
				HashMapDictionaryBuilder.put(
					"site.initializer.key", siteInitializerKey
				).build());

		Site randomSite = randomSite();

		randomSite.setTemplateKey(siteInitializerKey);
		randomSite.setTemplateType(Site.TemplateType.SITE_INITIALIZER);

		try {
			testPostSite_addSite(randomSite);

			Assert.fail();
		}
		catch (Problem.ProblemException problemException) {
			Problem problem = problemException.getProblem();

			Assert.assertEquals("BAD_REQUEST", problem.getStatus());
			Assert.assertEquals(
				"Site initializer with site template key " +
					randomSite.getTemplateKey() + " is inactive",
				problem.getTitle());
		}
		finally {
			serviceRegistration.unregister();
		}
	}

	private void _testPostSiteFailureSiteInitializerNotFound()
		throws Exception {

		Site randomSite = randomSite();

		randomSite.setTemplateKey(
			StringUtil.toLowerCase(RandomTestUtil.randomString()));
		randomSite.setTemplateType(Site.TemplateType.SITE_INITIALIZER);

		try {
			testPostSite_addSite(randomSite);

			Assert.fail();
		}
		catch (Problem.ProblemException problemException) {
			Problem problem = problemException.getProblem();

			Assert.assertEquals("BAD_REQUEST", problem.getStatus());
			Assert.assertEquals(
				"No site initializer was found for site template key " +
					randomSite.getTemplateKey(),
				problem.getTitle());
		}
	}

	private void _testPostSiteFailureSiteTemplateInactive() throws Exception {
		Site randomSite = randomSite();

		LayoutSetPrototype layoutSetPrototype =
			_layoutSetPrototypeLocalService.addLayoutSetPrototype(
				TestPropsValues.getUserId(), TestPropsValues.getCompanyId(),
				HashMapBuilder.put(
					LocaleUtil.getDefault(),
					StringUtil.toLowerCase(RandomTestUtil.randomString())
				).build(),
				null, false, true, new ServiceContext());

		randomSite.setTemplateKey(
			String.valueOf(layoutSetPrototype.getLayoutSetPrototypeId()));

		randomSite.setTemplateType(Site.TemplateType.SITE_TEMPLATE);

		try {
			try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
					_CLASS_NAME_EXCEPTION_MAPPER, LoggerTestUtil.ERROR)) {

				testPostSite_addSite(randomSite);
			}

			Assert.fail();
		}
		catch (Problem.ProblemException problemException) {
			Problem problem = problemException.getProblem();

			Assert.assertEquals("BAD_REQUEST", problem.getStatus());
			Assert.assertEquals(
				"Site template with site template key " +
					randomSite.getTemplateKey() + " is inactive",
				problem.getTitle());
		}
	}

	private void _testPostSiteFailureSiteTemplateNotFound() throws Exception {
		Site randomSite = randomSite();

		randomSite.setTemplateKey(String.valueOf(RandomTestUtil.randomLong()));
		randomSite.setTemplateType(Site.TemplateType.SITE_TEMPLATE);

		try {
			testPostSite_addSite(randomSite);

			Assert.fail();
		}
		catch (Problem.ProblemException problemException) {
			Problem problem = problemException.getProblem();

			Assert.assertEquals("BAD_REQUEST", problem.getStatus());
			Assert.assertEquals(
				"No site template was found for site template key " +
					randomSite.getTemplateKey(),
				problem.getTitle());
		}
	}

	private void _testPostSiteFailureTemplateKeyNoTemplateType()
		throws Exception {

		Site randomSite = randomSite();

		randomSite.setTemplateKey(
			StringUtil.toLowerCase(RandomTestUtil.randomString()));

		try {
			testPostSite_addSite(randomSite);

			Assert.fail();
		}
		catch (Problem.ProblemException problemException) {
			Problem problem = problemException.getProblem();

			Assert.assertEquals("BAD_REQUEST", problem.getStatus());
			Assert.assertEquals(
				"Template type cannot be empty if template key is specified",
				problem.getTitle());
		}
	}

	private void _testPostSiteFailureTemplateTypeNoTemplateKey()
		throws Exception {

		Site randomSite = randomSite();

		randomSite.setTemplateType(Site.TemplateType.SITE_INITIALIZER);

		try {
			testPostSite_addSite(randomSite);

			Assert.fail();
		}
		catch (Problem.ProblemException problemException) {
			Problem problem = problemException.getProblem();

			Assert.assertEquals("BAD_REQUEST", problem.getStatus());
			Assert.assertEquals(
				"Template key cannot be empty if template type is specified",
				problem.getTitle());
		}
	}

	private Site _testPostSiteSuccess(Site site) throws Exception {
		Site postSite = testPostSite_addSite(site);

		assertEquals(site, postSite);
		assertValid(postSite);

		return postSite;
	}

	private void _testPostSiteSuccessChild() throws Exception {
		Site parentSite = testPostSite_addSite(randomSite());

		Site randomSite = randomSite();

		randomSite.setParentSiteKey(parentSite.getKey());

		Site postSite = _testPostSiteSuccess(randomSite);

		Group group = _groupLocalService.fetchGroup(postSite.getId());

		Group parentGroup = group.getParentGroup();

		Assert.assertEquals(parentSite.getKey(), parentGroup.getGroupKey());
	}

	private void _testPostSiteSuccessMembershipTypePrivate() throws Exception {
		Site randomSite = randomSite();

		randomSite.setMembershipType(Site.MembershipType.PRIVATE);

		Site postSite = _testPostSiteSuccess(randomSite);

		Group group = _groupLocalService.fetchGroup(postSite.getId());

		Assert.assertEquals(GroupConstants.TYPE_SITE_PRIVATE, group.getType());
	}

	private void _testPostSiteSuccessSiteInitializer() throws Exception {
		Site randomSite = randomSite();

		randomSite.setTemplateKey("blank-site-initializer");
		randomSite.setTemplateType(Site.TemplateType.SITE_INITIALIZER);

		_testPostSiteSuccess(randomSite);
	}

	private void _testPostSiteSuccessSiteTemplate() throws Exception {
		Site randomSite = randomSite();

		LayoutSetPrototype layoutSetPrototype =
			_layoutSetPrototypeLocalService.addLayoutSetPrototype(
				TestPropsValues.getUserId(), TestPropsValues.getCompanyId(),
				HashMapBuilder.put(
					LocaleUtil.getDefault(),
					StringUtil.toLowerCase(RandomTestUtil.randomString())
				).build(),
				null, true, true, new ServiceContext());

		randomSite.setTemplateKey(
			String.valueOf(layoutSetPrototype.getLayoutSetPrototypeId()));

		randomSite.setTemplateType(Site.TemplateType.SITE_TEMPLATE);

		Site postSite = _testPostSiteSuccess(randomSite);

		Group group = _groupLocalService.fetchGroup(postSite.getId());

		LayoutSet publicLayoutSet = group.getPublicLayoutSet();

		Assert.assertEquals(
			layoutSetPrototype.getLayoutSetPrototypeId(),
			publicLayoutSet.getLayoutSetPrototypeId());
	}

	private static final String _CLASS_NAME_EXCEPTION_MAPPER =
		"com.liferay.portal.vulcan.internal.jaxrs.exception.mapper." +
			"ExceptionMapper";

	@Inject
	private GroupLocalService _groupLocalService;

	@Inject
	private LayoutSetPrototypeLocalService _layoutSetPrototypeLocalService;

	private final List<Site> _sites = new ArrayList<>();

	private class TestSiteInitializer implements SiteInitializer {

		public TestSiteInitializer(String key) {
			_key = key;
		}

		@Override
		public String getDescription(Locale locale) {
			return RandomTestUtil.randomString();
		}

		@Override
		public String getKey() {
			return _key;
		}

		@Override
		public String getName(Locale locale) {
			return RandomTestUtil.randomString();
		}

		@Override
		public String getThumbnailSrc() {
			return RandomTestUtil.randomString();
		}

		@Override
		public void initialize(long groupId) {
		}

		@Override
		public boolean isActive(long companyId) {
			return false;
		}

		private final String _key;

	}

}