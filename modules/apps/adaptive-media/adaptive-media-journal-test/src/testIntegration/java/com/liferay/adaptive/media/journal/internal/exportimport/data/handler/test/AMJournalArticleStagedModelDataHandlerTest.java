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

package com.liferay.adaptive.media.journal.internal.exportimport.data.handler.test;

import com.liferay.adaptive.media.image.configuration.AMImageConfigurationHelper;
import com.liferay.adaptive.media.image.html.AMImageHTMLTagFactory;
import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.dynamic.data.mapping.form.field.type.constants.DDMFormFieldTypeConstants;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.test.util.DDMStructureTestUtil;
import com.liferay.dynamic.data.mapping.test.util.DDMTemplateTestUtil;
import com.liferay.exportimport.kernel.lar.ExportImportThreadLocal;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.exportimport.test.util.lar.BaseWorkflowedStagedModelDataHandlerTestCase;
import com.liferay.journal.constants.JournalArticleConstants;
import com.liferay.journal.constants.JournalFolderConstants;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalFolder;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.journal.service.JournalFolderLocalService;
import com.liferay.journal.util.JournalContent;
import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.StagedModel;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.AssertUtils;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.DateTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Alejandro Tardín
 */
@RunWith(Arquillian.class)
public class AMJournalArticleStagedModelDataHandlerTest
	extends BaseWorkflowedStagedModelDataHandlerTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		_amImageConfigurationHelper.addAMImageConfigurationEntry(
			stagingGroup.getCompanyId(), StringUtil.randomString(),
			StringUtil.randomString(), _AM_JOURNAL_CONFIG_UUID,
			HashMapBuilder.put(
				"max-height", "600"
			).put(
				"max-width", "800"
			).build());
	}

	@After
	@Override
	public void tearDown() throws Exception {
		_amImageConfigurationHelper.forceDeleteAMImageConfigurationEntry(
			stagingGroup.getCompanyId(), _AM_JOURNAL_CONFIG_UUID);

		super.tearDown();
	}

	@Test
	public void testExportImportContentWithMultipleDynamicReferences()
		throws Exception {

		ServiceContext serviceContext = _getServiceContext();

		FileEntry fileEntry1 = _addImageFileEntry(serviceContext);
		FileEntry fileEntry2 = _addImageFileEntry(serviceContext);

		String content = _getDynamicContent(fileEntry1, fileEntry2);

		JournalArticle journalArticle = _addJournalArticle(
			content, _getServiceContext());

		ExportImportThreadLocal.setPortletImportInProcess(true);

		try {
			exportImportStagedModel(journalArticle);
		}
		finally {
			ExportImportThreadLocal.setPortletImportInProcess(false);
		}

		JournalArticle importedJournalArticle = (JournalArticle)getStagedModel(
			journalArticle.getUuid(), liveGroup);

		_assertXMLEquals(
			_getExpectedDynamicContent(fileEntry1, fileEntry2),
			importedJournalArticle.getContent());
	}

	@Test
	public void testExportImportContentWithMultipleStaticReferences()
		throws Exception {

		ServiceContext serviceContext = _getServiceContext();

		FileEntry fileEntry1 = _addImageFileEntry(serviceContext);
		FileEntry fileEntry2 = _addImageFileEntry(serviceContext);

		String content = _getStaticContent(fileEntry1, fileEntry2);

		JournalArticle journalArticle = _addJournalArticle(
			content, serviceContext);

		ExportImportThreadLocal.setPortletImportInProcess(true);

		try {
			exportImportStagedModel(journalArticle);
		}
		finally {
			ExportImportThreadLocal.setPortletImportInProcess(false);
		}

		JournalArticle importedJournalArticle = (JournalArticle)getStagedModel(
			journalArticle.getUuid(), liveGroup);

		_assertXMLEquals(
			_getExpectedStaticContent(fileEntry1, fileEntry2),
			importedJournalArticle.getContent());
	}

	@Test
	public void testExportImportContentWithNoReferences() throws Exception {
		JournalArticle journalArticle = _addJournalArticle(
			_getContent(StringPool.BLANK), _getServiceContext());

		ExportImportThreadLocal.setPortletImportInProcess(true);

		try {
			exportImportStagedModel(journalArticle);
		}
		finally {
			ExportImportThreadLocal.setPortletImportInProcess(false);
		}

		JournalArticle importedJournalArticle = (JournalArticle)getStagedModel(
			journalArticle.getUuid(), liveGroup);

		_assertContentEquals(journalArticle, importedJournalArticle);
	}

	@Test
	public void testExportSucceedsWithInvalidReferences() throws Exception {
		int invalidFileEntryId = 9999999;

		JournalArticle journalArticle = _withPortletImportEnabled(
			() -> _addJournalArticle(
				_getContent(_getImgTag(invalidFileEntryId)),
				_getServiceContext()));

		initExport();

		StagedModelDataHandlerUtil.exportStagedModel(
			portletDataContext, journalArticle);
	}

	@Override
	protected StagedModel addStagedModel(
			Group group,
			Map<String, List<StagedModel>> dependentStagedModelsMap)
		throws Exception {

		ServiceContext serviceContext = _getServiceContext();

		FileEntry fileEntry = _addImageFileEntry(serviceContext);

		return _addJournalArticle(
			_getContent(_getImgTag(fileEntry)), serviceContext);
	}

	@Override
	protected List<StagedModel> addWorkflowedStagedModels(Group group)
		throws Exception {

		ServiceContext serviceContext = _getServiceContext();

		FileEntry fileEntry = _addImageFileEntry(serviceContext);

		return Collections.singletonList(
			_addJournalArticle(
				_getContent(_getImgTag(fileEntry)), serviceContext));
	}

	@Override
	protected StagedModel getStagedModel(String uuid, Group group)
		throws PortalException {

		return _journalArticleLocalService.getJournalArticleByUuidAndGroupId(
			uuid, group.getGroupId());
	}

	@Override
	protected Class<? extends StagedModel> getStagedModelClass() {
		return JournalArticle.class;
	}

	@Override
	protected void validateImportedStagedModel(
			StagedModel stagedModel, StagedModel importedStagedModel)
		throws Exception {

		DateTestUtil.assertEquals(
			stagedModel.getCreateDate(), importedStagedModel.getCreateDate());

		Assert.assertEquals(
			stagedModel.getUuid(), importedStagedModel.getUuid());
	}

	private FileEntry _addImageFileEntry(ServiceContext serviceContext)
		throws Exception {

		return _dlAppLocalService.addFileEntry(
			null, TestPropsValues.getUserId(), stagingGroup.getGroupId(),
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			StringUtil.randomString(), ContentTypes.IMAGE_JPEG,
			FileUtil.getBytes(getClass(), "dependencies/image.jpg"), null, null,
			serviceContext);
	}

	private JournalArticle _addJournalArticle(
			String content, ServiceContext serviceContext)
		throws Exception {

		JournalFolder journalFolder = _journalFolderLocalService.addFolder(
			null, serviceContext.getUserId(), serviceContext.getScopeGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			RandomTestUtil.randomString(), "This is a test folder.",
			serviceContext);

		Map<Locale, String> titleMap = HashMapBuilder.put(
			LocaleUtil.getSiteDefault(), "Test Article"
		).build();

		DDMForm ddmForm = DDMStructureTestUtil.getSampleDDMForm(
			"content", "string", "text", true,
			DDMFormFieldTypeConstants.RICH_TEXT,
			new Locale[] {LocaleUtil.getSiteDefault()},
			LocaleUtil.getSiteDefault());

		DDMStructure ddmStructure = DDMStructureTestUtil.addStructure(
			stagingGroup.getGroupId(), JournalArticle.class.getName(), 0,
			ddmForm, LocaleUtil.getSiteDefault(),
			ServiceContextTestUtil.getServiceContext());

		DDMTemplate ddmTemplate = DDMTemplateTestUtil.addTemplate(
			stagingGroup.getGroupId(),
			PortalUtil.getClassNameId(DDMStructure.class),
			ddmStructure.getStructureId(),
			PortalUtil.getClassNameId(JournalArticle.class));

		return _journalArticleLocalService.addArticle(
			null, serviceContext.getUserId(), serviceContext.getScopeGroupId(),
			journalFolder.getFolderId(),
			JournalArticleConstants.CLASS_NAME_ID_DEFAULT, 0, StringPool.BLANK,
			true, 0, titleMap, null, titleMap, content,
			ddmStructure.getStructureId(), ddmTemplate.getTemplateKey(), null,
			1, 1, 1965, 0, 0, 0, 0, 0, 0, 0, true, 0, 0, 0, 0, 0, true, true,
			false, null, null, null, null, serviceContext);
	}

	private void _assertContentEquals(
		JournalArticle expectedJournalArticle,
		JournalArticle actualJournalArticle) {

		String expectedContent = _journalContent.getContent(
			expectedJournalArticle.getGroupId(),
			expectedJournalArticle.getArticleId(), Constants.VIEW,
			expectedJournalArticle.getDefaultLanguageId());
		String actualContent = _journalContent.getContent(
			actualJournalArticle.getGroupId(),
			actualJournalArticle.getArticleId(), Constants.VIEW,
			actualJournalArticle.getDefaultLanguageId());

		AssertUtils.assertEqualsIgnoreCase(expectedContent, actualContent);
	}

	private void _assertXMLEquals(String expectedXML, String actualXML)
		throws Exception {

		Document actualDocument = SAXReaderUtil.read(actualXML);
		Document expectedDocument = SAXReaderUtil.read(expectedXML);

		AssertUtils.assertEqualsIgnoreCase(
			expectedDocument.formattedString(),
			actualDocument.formattedString());
	}

	private String _getContent(String html) throws Exception {
		return StringUtil.replace(
			new String(
				FileUtil.getBytes(
					getClass(), "dependencies/dynamic_content.xml")),
			"[$CONTENT$]", html);
	}

	private String _getDynamicContent(FileEntry... fileEntries)
		throws Exception {

		StringBundler sb = new StringBundler(fileEntries.length);

		for (FileEntry fileEntry : fileEntries) {
			sb.append(_getImgTag(fileEntry));
			sb.append(StringPool.NEW_LINE);
		}

		sb.setIndex(sb.index() - 1);

		return _getContent(sb.toString());
	}

	private String _getExpectedDynamicContent(FileEntry... fileEntries)
		throws Exception {

		List<FileEntry> importedFileEntries = new ArrayList<>();

		for (FileEntry fileEntry : fileEntries) {
			importedFileEntries.add(
				_dlAppLocalService.getFileEntryByUuidAndGroupId(
					fileEntry.getUuid(), liveGroup.getGroupId()));
		}

		return _getDynamicContent(
			importedFileEntries.toArray(new FileEntry[0]));
	}

	private String _getExpectedStaticContent(FileEntry... fileEntries)
		throws Exception {

		StringBundler sb = new StringBundler(fileEntries.length * 2);

		for (FileEntry fileEntry : fileEntries) {
			FileEntry importedFileEntry =
				_dlAppLocalService.getFileEntryByUuidAndGroupId(
					fileEntry.getUuid(), liveGroup.getGroupId());

			sb.append(
				_amImageHTMLTagFactory.create(
					_getImgTag(importedFileEntry), importedFileEntry));

			sb.append(StringPool.NEW_LINE);
		}

		sb.setIndex(sb.index() - 1);

		return _getContent(sb.toString());
	}

	private String _getImgTag(FileEntry fileEntry) throws Exception {
		return _getImgTag(fileEntry.getFileEntryId());
	}

	private String _getImgTag(long fileEntryId) throws Exception {
		return String.format(
			"<img alt=\"alt\" class=\"a class\" src=\"theURL\" " +
				"data-fileentryid=\"%s\" />",
			fileEntryId);
	}

	private String _getPictureTag(FileEntry fileEntry) throws Exception {
		StringBundler sb = new StringBundler(6);

		sb.append("<picture data-fileentryid=\"");
		sb.append(fileEntry.getFileEntryId());
		sb.append("\">");
		sb.append("<source></source>");
		sb.append(_getImgTag(fileEntry));
		sb.append("</picture>");

		return sb.toString();
	}

	private ServiceContext _getServiceContext() throws Exception {
		return ServiceContextTestUtil.getServiceContext(
			stagingGroup.getGroupId(), TestPropsValues.getUserId());
	}

	private String _getStaticContent(FileEntry... fileEntries)
		throws Exception {

		StringBundler sb = new StringBundler(fileEntries.length);

		for (FileEntry fileEntry : fileEntries) {
			sb.append(_getPictureTag(fileEntry));
			sb.append(StringPool.NEW_LINE);
		}

		sb.setIndex(sb.index() - 1);

		return _getContent(sb.toString());
	}

	private JournalArticle _withPortletImportEnabled(
			UnsafeSupplier<JournalArticle, Exception> unsafeSupplier)
		throws Exception {

		boolean portletImportInProcess =
			ExportImportThreadLocal.isPortletImportInProcess();

		try {
			ExportImportThreadLocal.setPortletImportInProcess(true);

			return unsafeSupplier.get();
		}
		finally {
			ExportImportThreadLocal.setPortletImportInProcess(
				portletImportInProcess);
		}
	}

	private static final String _AM_JOURNAL_CONFIG_UUID = "journal-config";

	@Inject
	private AMImageConfigurationHelper _amImageConfigurationHelper;

	@Inject
	private AMImageHTMLTagFactory _amImageHTMLTagFactory;

	@Inject
	private DLAppLocalService _dlAppLocalService;

	@Inject
	private JournalArticleLocalService _journalArticleLocalService;

	@Inject
	private JournalContent _journalContent;

	@Inject
	private JournalFolderLocalService _journalFolderLocalService;

}