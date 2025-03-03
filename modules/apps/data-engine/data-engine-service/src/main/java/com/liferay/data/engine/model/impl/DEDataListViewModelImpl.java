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

package com.liferay.data.engine.model.impl;

import com.liferay.data.engine.model.DEDataListView;
import com.liferay.data.engine.model.DEDataListViewModel;
import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.LocaleException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.sql.Blob;
import java.sql.Types;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The base model implementation for the DEDataListView service. Represents a row in the &quot;DEDataListView&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface <code>DEDataListViewModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link DEDataListViewImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DEDataListViewImpl
 * @generated
 */
public class DEDataListViewModelImpl
	extends BaseModelImpl<DEDataListView> implements DEDataListViewModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a de data list view model instance should use the <code>DEDataListView</code> interface instead.
	 */
	public static final String TABLE_NAME = "DEDataListView";

	public static final Object[][] TABLE_COLUMNS = {
		{"mvccVersion", Types.BIGINT}, {"ctCollectionId", Types.BIGINT},
		{"uuid_", Types.VARCHAR}, {"deDataListViewId", Types.BIGINT},
		{"groupId", Types.BIGINT}, {"companyId", Types.BIGINT},
		{"userId", Types.BIGINT}, {"userName", Types.VARCHAR},
		{"createDate", Types.TIMESTAMP}, {"modifiedDate", Types.TIMESTAMP},
		{"appliedFilters", Types.CLOB}, {"ddmStructureId", Types.BIGINT},
		{"fieldNames", Types.CLOB}, {"name", Types.VARCHAR},
		{"sortField", Types.VARCHAR}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("mvccVersion", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("ctCollectionId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("uuid_", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("deDataListViewId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("groupId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("appliedFilters", Types.CLOB);
		TABLE_COLUMNS_MAP.put("ddmStructureId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("fieldNames", Types.CLOB);
		TABLE_COLUMNS_MAP.put("name", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("sortField", Types.VARCHAR);
	}

	public static final String TABLE_SQL_CREATE =
		"create table DEDataListView (mvccVersion LONG default 0 not null,ctCollectionId LONG default 0 not null,uuid_ VARCHAR(75) null,deDataListViewId LONG not null,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,appliedFilters TEXT null,ddmStructureId LONG,fieldNames TEXT null,name STRING null,sortField VARCHAR(75) null,primary key (deDataListViewId, ctCollectionId))";

	public static final String TABLE_SQL_DROP = "drop table DEDataListView";

	public static final String ORDER_BY_JPQL =
		" ORDER BY deDataListView.deDataListViewId ASC";

	public static final String ORDER_BY_SQL =
		" ORDER BY DEDataListView.deDataListViewId ASC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long COMPANYID_COLUMN_BITMASK = 1L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long DDMSTRUCTUREID_COLUMN_BITMASK = 2L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long GROUPID_COLUMN_BITMASK = 4L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long UUID_COLUMN_BITMASK = 8L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *		#getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long DEDATALISTVIEWID_COLUMN_BITMASK = 16L;

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static void setEntityCacheEnabled(boolean entityCacheEnabled) {
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static void setFinderCacheEnabled(boolean finderCacheEnabled) {
	}

	public DEDataListViewModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _deDataListViewId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setDeDataListViewId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _deDataListViewId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return DEDataListView.class;
	}

	@Override
	public String getModelClassName() {
		return DEDataListView.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<DEDataListView, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		for (Map.Entry<String, Function<DEDataListView, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<DEDataListView, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName,
				attributeGetterFunction.apply((DEDataListView)this));
		}

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<DEDataListView, Object>>
			attributeSetterBiConsumers = getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<DEDataListView, Object> attributeSetterBiConsumer =
				attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(DEDataListView)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<DEDataListView, Object>>
		getAttributeGetterFunctions() {

		return AttributeGetterFunctionsHolder._attributeGetterFunctions;
	}

	public Map<String, BiConsumer<DEDataListView, Object>>
		getAttributeSetterBiConsumers() {

		return AttributeSetterBiConsumersHolder._attributeSetterBiConsumers;
	}

	private static class AttributeGetterFunctionsHolder {

		private static final Map<String, Function<DEDataListView, Object>>
			_attributeGetterFunctions;

		static {
			Map<String, Function<DEDataListView, Object>>
				attributeGetterFunctions =
					new LinkedHashMap
						<String, Function<DEDataListView, Object>>();

			attributeGetterFunctions.put(
				"mvccVersion", DEDataListView::getMvccVersion);
			attributeGetterFunctions.put(
				"ctCollectionId", DEDataListView::getCtCollectionId);
			attributeGetterFunctions.put("uuid", DEDataListView::getUuid);
			attributeGetterFunctions.put(
				"deDataListViewId", DEDataListView::getDeDataListViewId);
			attributeGetterFunctions.put("groupId", DEDataListView::getGroupId);
			attributeGetterFunctions.put(
				"companyId", DEDataListView::getCompanyId);
			attributeGetterFunctions.put("userId", DEDataListView::getUserId);
			attributeGetterFunctions.put(
				"userName", DEDataListView::getUserName);
			attributeGetterFunctions.put(
				"createDate", DEDataListView::getCreateDate);
			attributeGetterFunctions.put(
				"modifiedDate", DEDataListView::getModifiedDate);
			attributeGetterFunctions.put(
				"appliedFilters", DEDataListView::getAppliedFilters);
			attributeGetterFunctions.put(
				"ddmStructureId", DEDataListView::getDdmStructureId);
			attributeGetterFunctions.put(
				"fieldNames", DEDataListView::getFieldNames);
			attributeGetterFunctions.put("name", DEDataListView::getName);
			attributeGetterFunctions.put(
				"sortField", DEDataListView::getSortField);

			_attributeGetterFunctions = Collections.unmodifiableMap(
				attributeGetterFunctions);
		}

	}

	private static class AttributeSetterBiConsumersHolder {

		private static final Map<String, BiConsumer<DEDataListView, Object>>
			_attributeSetterBiConsumers;

		static {
			Map<String, BiConsumer<DEDataListView, ?>>
				attributeSetterBiConsumers =
					new LinkedHashMap<String, BiConsumer<DEDataListView, ?>>();

			attributeSetterBiConsumers.put(
				"mvccVersion",
				(BiConsumer<DEDataListView, Long>)
					DEDataListView::setMvccVersion);
			attributeSetterBiConsumers.put(
				"ctCollectionId",
				(BiConsumer<DEDataListView, Long>)
					DEDataListView::setCtCollectionId);
			attributeSetterBiConsumers.put(
				"uuid",
				(BiConsumer<DEDataListView, String>)DEDataListView::setUuid);
			attributeSetterBiConsumers.put(
				"deDataListViewId",
				(BiConsumer<DEDataListView, Long>)
					DEDataListView::setDeDataListViewId);
			attributeSetterBiConsumers.put(
				"groupId",
				(BiConsumer<DEDataListView, Long>)DEDataListView::setGroupId);
			attributeSetterBiConsumers.put(
				"companyId",
				(BiConsumer<DEDataListView, Long>)DEDataListView::setCompanyId);
			attributeSetterBiConsumers.put(
				"userId",
				(BiConsumer<DEDataListView, Long>)DEDataListView::setUserId);
			attributeSetterBiConsumers.put(
				"userName",
				(BiConsumer<DEDataListView, String>)
					DEDataListView::setUserName);
			attributeSetterBiConsumers.put(
				"createDate",
				(BiConsumer<DEDataListView, Date>)
					DEDataListView::setCreateDate);
			attributeSetterBiConsumers.put(
				"modifiedDate",
				(BiConsumer<DEDataListView, Date>)
					DEDataListView::setModifiedDate);
			attributeSetterBiConsumers.put(
				"appliedFilters",
				(BiConsumer<DEDataListView, String>)
					DEDataListView::setAppliedFilters);
			attributeSetterBiConsumers.put(
				"ddmStructureId",
				(BiConsumer<DEDataListView, Long>)
					DEDataListView::setDdmStructureId);
			attributeSetterBiConsumers.put(
				"fieldNames",
				(BiConsumer<DEDataListView, String>)
					DEDataListView::setFieldNames);
			attributeSetterBiConsumers.put(
				"name",
				(BiConsumer<DEDataListView, String>)DEDataListView::setName);
			attributeSetterBiConsumers.put(
				"sortField",
				(BiConsumer<DEDataListView, String>)
					DEDataListView::setSortField);

			_attributeSetterBiConsumers = Collections.unmodifiableMap(
				(Map)attributeSetterBiConsumers);
		}

	}

	@Override
	public long getMvccVersion() {
		return _mvccVersion;
	}

	@Override
	public void setMvccVersion(long mvccVersion) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_mvccVersion = mvccVersion;
	}

	@Override
	public long getCtCollectionId() {
		return _ctCollectionId;
	}

	@Override
	public void setCtCollectionId(long ctCollectionId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_ctCollectionId = ctCollectionId;
	}

	@Override
	public String getUuid() {
		if (_uuid == null) {
			return "";
		}
		else {
			return _uuid;
		}
	}

	@Override
	public void setUuid(String uuid) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_uuid = uuid;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public String getOriginalUuid() {
		return getColumnOriginalValue("uuid_");
	}

	@Override
	public long getDeDataListViewId() {
		return _deDataListViewId;
	}

	@Override
	public void setDeDataListViewId(long deDataListViewId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_deDataListViewId = deDataListViewId;
	}

	@Override
	public long getGroupId() {
		return _groupId;
	}

	@Override
	public void setGroupId(long groupId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_groupId = groupId;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalGroupId() {
		return GetterUtil.getLong(this.<Long>getColumnOriginalValue("groupId"));
	}

	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public void setCompanyId(long companyId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_companyId = companyId;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalCompanyId() {
		return GetterUtil.getLong(
			this.<Long>getColumnOriginalValue("companyId"));
	}

	@Override
	public long getUserId() {
		return _userId;
	}

	@Override
	public void setUserId(long userId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_userId = userId;
	}

	@Override
	public String getUserUuid() {
		try {
			User user = UserLocalServiceUtil.getUserById(getUserId());

			return user.getUuid();
		}
		catch (PortalException portalException) {
			return "";
		}
	}

	@Override
	public void setUserUuid(String userUuid) {
	}

	@Override
	public String getUserName() {
		if (_userName == null) {
			return "";
		}
		else {
			return _userName;
		}
	}

	@Override
	public void setUserName(String userName) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_userName = userName;
	}

	@Override
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_createDate = createDate;
	}

	@Override
	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public boolean hasSetModifiedDate() {
		return _setModifiedDate;
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		_setModifiedDate = true;

		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_modifiedDate = modifiedDate;
	}

	@Override
	public String getAppliedFilters() {
		if (_appliedFilters == null) {
			return "";
		}
		else {
			return _appliedFilters;
		}
	}

	@Override
	public void setAppliedFilters(String appliedFilters) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_appliedFilters = appliedFilters;
	}

	@Override
	public long getDdmStructureId() {
		return _ddmStructureId;
	}

	@Override
	public void setDdmStructureId(long ddmStructureId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_ddmStructureId = ddmStructureId;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalDdmStructureId() {
		return GetterUtil.getLong(
			this.<Long>getColumnOriginalValue("ddmStructureId"));
	}

	@Override
	public String getFieldNames() {
		if (_fieldNames == null) {
			return "";
		}
		else {
			return _fieldNames;
		}
	}

	@Override
	public void setFieldNames(String fieldNames) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_fieldNames = fieldNames;
	}

	@Override
	public String getName() {
		if (_name == null) {
			return "";
		}
		else {
			return _name;
		}
	}

	@Override
	public String getName(Locale locale) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getName(languageId);
	}

	@Override
	public String getName(Locale locale, boolean useDefault) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getName(languageId, useDefault);
	}

	@Override
	public String getName(String languageId) {
		return LocalizationUtil.getLocalization(getName(), languageId);
	}

	@Override
	public String getName(String languageId, boolean useDefault) {
		return LocalizationUtil.getLocalization(
			getName(), languageId, useDefault);
	}

	@Override
	public String getNameCurrentLanguageId() {
		return _nameCurrentLanguageId;
	}

	@JSON
	@Override
	public String getNameCurrentValue() {
		Locale locale = getLocale(_nameCurrentLanguageId);

		return getName(locale);
	}

	@Override
	public Map<Locale, String> getNameMap() {
		return LocalizationUtil.getLocalizationMap(getName());
	}

	@Override
	public void setName(String name) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_name = name;
	}

	@Override
	public void setName(String name, Locale locale) {
		setName(name, locale, LocaleUtil.getSiteDefault());
	}

	@Override
	public void setName(String name, Locale locale, Locale defaultLocale) {
		String languageId = LocaleUtil.toLanguageId(locale);
		String defaultLanguageId = LocaleUtil.toLanguageId(defaultLocale);

		if (Validator.isNotNull(name)) {
			setName(
				LocalizationUtil.updateLocalization(
					getName(), "Name", name, languageId, defaultLanguageId));
		}
		else {
			setName(
				LocalizationUtil.removeLocalization(
					getName(), "Name", languageId));
		}
	}

	@Override
	public void setNameCurrentLanguageId(String languageId) {
		_nameCurrentLanguageId = languageId;
	}

	@Override
	public void setNameMap(Map<Locale, String> nameMap) {
		setNameMap(nameMap, LocaleUtil.getSiteDefault());
	}

	@Override
	public void setNameMap(Map<Locale, String> nameMap, Locale defaultLocale) {
		if (nameMap == null) {
			return;
		}

		setName(
			LocalizationUtil.updateLocalization(
				nameMap, getName(), "Name",
				LocaleUtil.toLanguageId(defaultLocale)));
	}

	@Override
	public String getSortField() {
		if (_sortField == null) {
			return "";
		}
		else {
			return _sortField;
		}
	}

	@Override
	public void setSortField(String sortField) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_sortField = sortField;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return new StagedModelType(
			PortalUtil.getClassNameId(DEDataListView.class.getName()));
	}

	public long getColumnBitmask() {
		if (_columnBitmask > 0) {
			return _columnBitmask;
		}

		if ((_columnOriginalValues == null) ||
			(_columnOriginalValues == Collections.EMPTY_MAP)) {

			return 0;
		}

		for (Map.Entry<String, Object> entry :
				_columnOriginalValues.entrySet()) {

			if (!Objects.equals(
					entry.getValue(), getColumnValue(entry.getKey()))) {

				_columnBitmask |= _columnBitmasks.get(entry.getKey());
			}
		}

		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(
			getCompanyId(), DEDataListView.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public String[] getAvailableLanguageIds() {
		Set<String> availableLanguageIds = new TreeSet<String>();

		Map<Locale, String> nameMap = getNameMap();

		for (Map.Entry<Locale, String> entry : nameMap.entrySet()) {
			Locale locale = entry.getKey();
			String value = entry.getValue();

			if (Validator.isNotNull(value)) {
				availableLanguageIds.add(LocaleUtil.toLanguageId(locale));
			}
		}

		return availableLanguageIds.toArray(
			new String[availableLanguageIds.size()]);
	}

	@Override
	public String getDefaultLanguageId() {
		String xml = getName();

		if (xml == null) {
			return "";
		}

		Locale defaultLocale = LocaleUtil.getSiteDefault();

		return LocalizationUtil.getDefaultLanguageId(xml, defaultLocale);
	}

	@Override
	public void prepareLocalizedFieldsForImport() throws LocaleException {
		Locale defaultLocale = LocaleUtil.fromLanguageId(
			getDefaultLanguageId());

		Locale[] availableLocales = LocaleUtil.fromLanguageIds(
			getAvailableLanguageIds());

		Locale defaultImportLocale = LocalizationUtil.getDefaultImportLocale(
			DEDataListView.class.getName(), getPrimaryKey(), defaultLocale,
			availableLocales);

		prepareLocalizedFieldsForImport(defaultImportLocale);
	}

	@Override
	@SuppressWarnings("unused")
	public void prepareLocalizedFieldsForImport(Locale defaultImportLocale)
		throws LocaleException {

		Locale defaultLocale = LocaleUtil.getSiteDefault();

		String modelDefaultLanguageId = getDefaultLanguageId();

		String name = getName(defaultLocale);

		if (Validator.isNull(name)) {
			setName(getName(modelDefaultLanguageId), defaultLocale);
		}
		else {
			setName(getName(defaultLocale), defaultLocale, defaultLocale);
		}
	}

	@Override
	public DEDataListView toEscapedModel() {
		if (_escapedModel == null) {
			Function<InvocationHandler, DEDataListView>
				escapedModelProxyProviderFunction =
					EscapedModelProxyProviderFunctionHolder.
						_escapedModelProxyProviderFunction;

			_escapedModel = escapedModelProxyProviderFunction.apply(
				new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		DEDataListViewImpl deDataListViewImpl = new DEDataListViewImpl();

		deDataListViewImpl.setMvccVersion(getMvccVersion());
		deDataListViewImpl.setCtCollectionId(getCtCollectionId());
		deDataListViewImpl.setUuid(getUuid());
		deDataListViewImpl.setDeDataListViewId(getDeDataListViewId());
		deDataListViewImpl.setGroupId(getGroupId());
		deDataListViewImpl.setCompanyId(getCompanyId());
		deDataListViewImpl.setUserId(getUserId());
		deDataListViewImpl.setUserName(getUserName());
		deDataListViewImpl.setCreateDate(getCreateDate());
		deDataListViewImpl.setModifiedDate(getModifiedDate());
		deDataListViewImpl.setAppliedFilters(getAppliedFilters());
		deDataListViewImpl.setDdmStructureId(getDdmStructureId());
		deDataListViewImpl.setFieldNames(getFieldNames());
		deDataListViewImpl.setName(getName());
		deDataListViewImpl.setSortField(getSortField());

		deDataListViewImpl.resetOriginalValues();

		return deDataListViewImpl;
	}

	@Override
	public DEDataListView cloneWithOriginalValues() {
		DEDataListViewImpl deDataListViewImpl = new DEDataListViewImpl();

		deDataListViewImpl.setMvccVersion(
			this.<Long>getColumnOriginalValue("mvccVersion"));
		deDataListViewImpl.setCtCollectionId(
			this.<Long>getColumnOriginalValue("ctCollectionId"));
		deDataListViewImpl.setUuid(
			this.<String>getColumnOriginalValue("uuid_"));
		deDataListViewImpl.setDeDataListViewId(
			this.<Long>getColumnOriginalValue("deDataListViewId"));
		deDataListViewImpl.setGroupId(
			this.<Long>getColumnOriginalValue("groupId"));
		deDataListViewImpl.setCompanyId(
			this.<Long>getColumnOriginalValue("companyId"));
		deDataListViewImpl.setUserId(
			this.<Long>getColumnOriginalValue("userId"));
		deDataListViewImpl.setUserName(
			this.<String>getColumnOriginalValue("userName"));
		deDataListViewImpl.setCreateDate(
			this.<Date>getColumnOriginalValue("createDate"));
		deDataListViewImpl.setModifiedDate(
			this.<Date>getColumnOriginalValue("modifiedDate"));
		deDataListViewImpl.setAppliedFilters(
			this.<String>getColumnOriginalValue("appliedFilters"));
		deDataListViewImpl.setDdmStructureId(
			this.<Long>getColumnOriginalValue("ddmStructureId"));
		deDataListViewImpl.setFieldNames(
			this.<String>getColumnOriginalValue("fieldNames"));
		deDataListViewImpl.setName(this.<String>getColumnOriginalValue("name"));
		deDataListViewImpl.setSortField(
			this.<String>getColumnOriginalValue("sortField"));

		return deDataListViewImpl;
	}

	@Override
	public int compareTo(DEDataListView deDataListView) {
		long primaryKey = deDataListView.getPrimaryKey();

		if (getPrimaryKey() < primaryKey) {
			return -1;
		}
		else if (getPrimaryKey() > primaryKey) {
			return 1;
		}
		else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof DEDataListView)) {
			return false;
		}

		DEDataListView deDataListView = (DEDataListView)object;

		long primaryKey = deDataListView.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	@Override
	public boolean isEntityCacheEnabled() {
		return true;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	@Override
	public boolean isFinderCacheEnabled() {
		return true;
	}

	@Override
	public void resetOriginalValues() {
		_columnOriginalValues = Collections.emptyMap();

		_setModifiedDate = false;

		_columnBitmask = 0;
	}

	@Override
	public CacheModel<DEDataListView> toCacheModel() {
		DEDataListViewCacheModel deDataListViewCacheModel =
			new DEDataListViewCacheModel();

		deDataListViewCacheModel.mvccVersion = getMvccVersion();

		deDataListViewCacheModel.ctCollectionId = getCtCollectionId();

		deDataListViewCacheModel.uuid = getUuid();

		String uuid = deDataListViewCacheModel.uuid;

		if ((uuid != null) && (uuid.length() == 0)) {
			deDataListViewCacheModel.uuid = null;
		}

		deDataListViewCacheModel.deDataListViewId = getDeDataListViewId();

		deDataListViewCacheModel.groupId = getGroupId();

		deDataListViewCacheModel.companyId = getCompanyId();

		deDataListViewCacheModel.userId = getUserId();

		deDataListViewCacheModel.userName = getUserName();

		String userName = deDataListViewCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			deDataListViewCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			deDataListViewCacheModel.createDate = createDate.getTime();
		}
		else {
			deDataListViewCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			deDataListViewCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			deDataListViewCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		deDataListViewCacheModel.appliedFilters = getAppliedFilters();

		String appliedFilters = deDataListViewCacheModel.appliedFilters;

		if ((appliedFilters != null) && (appliedFilters.length() == 0)) {
			deDataListViewCacheModel.appliedFilters = null;
		}

		deDataListViewCacheModel.ddmStructureId = getDdmStructureId();

		deDataListViewCacheModel.fieldNames = getFieldNames();

		String fieldNames = deDataListViewCacheModel.fieldNames;

		if ((fieldNames != null) && (fieldNames.length() == 0)) {
			deDataListViewCacheModel.fieldNames = null;
		}

		deDataListViewCacheModel.name = getName();

		String name = deDataListViewCacheModel.name;

		if ((name != null) && (name.length() == 0)) {
			deDataListViewCacheModel.name = null;
		}

		deDataListViewCacheModel.sortField = getSortField();

		String sortField = deDataListViewCacheModel.sortField;

		if ((sortField != null) && (sortField.length() == 0)) {
			deDataListViewCacheModel.sortField = null;
		}

		return deDataListViewCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<DEDataListView, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			(5 * attributeGetterFunctions.size()) + 2);

		sb.append("{");

		for (Map.Entry<String, Function<DEDataListView, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<DEDataListView, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("\"");
			sb.append(attributeName);
			sb.append("\": ");

			Object value = attributeGetterFunction.apply((DEDataListView)this);

			if (value == null) {
				sb.append("null");
			}
			else if (value instanceof Blob || value instanceof Date ||
					 value instanceof Map || value instanceof String) {

				sb.append(
					"\"" + StringUtil.replace(value.toString(), "\"", "'") +
						"\"");
			}
			else {
				sb.append(value);
			}

			sb.append(", ");
		}

		if (sb.index() > 1) {
			sb.setIndex(sb.index() - 1);
		}

		sb.append("}");

		return sb.toString();
	}

	private static class EscapedModelProxyProviderFunctionHolder {

		private static final Function<InvocationHandler, DEDataListView>
			_escapedModelProxyProviderFunction =
				ProxyUtil.getProxyProviderFunction(
					DEDataListView.class, ModelWrapper.class);

	}

	private long _mvccVersion;
	private long _ctCollectionId;
	private String _uuid;
	private long _deDataListViewId;
	private long _groupId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _setModifiedDate;
	private String _appliedFilters;
	private long _ddmStructureId;
	private String _fieldNames;
	private String _name;
	private String _nameCurrentLanguageId;
	private String _sortField;

	public <T> T getColumnValue(String columnName) {
		columnName = _attributeNames.getOrDefault(columnName, columnName);

		Function<DEDataListView, Object> function =
			AttributeGetterFunctionsHolder._attributeGetterFunctions.get(
				columnName);

		if (function == null) {
			throw new IllegalArgumentException(
				"No attribute getter function found for " + columnName);
		}

		return (T)function.apply((DEDataListView)this);
	}

	public <T> T getColumnOriginalValue(String columnName) {
		if (_columnOriginalValues == null) {
			return null;
		}

		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		return (T)_columnOriginalValues.get(columnName);
	}

	private void _setColumnOriginalValues() {
		_columnOriginalValues = new HashMap<String, Object>();

		_columnOriginalValues.put("mvccVersion", _mvccVersion);
		_columnOriginalValues.put("ctCollectionId", _ctCollectionId);
		_columnOriginalValues.put("uuid_", _uuid);
		_columnOriginalValues.put("deDataListViewId", _deDataListViewId);
		_columnOriginalValues.put("groupId", _groupId);
		_columnOriginalValues.put("companyId", _companyId);
		_columnOriginalValues.put("userId", _userId);
		_columnOriginalValues.put("userName", _userName);
		_columnOriginalValues.put("createDate", _createDate);
		_columnOriginalValues.put("modifiedDate", _modifiedDate);
		_columnOriginalValues.put("appliedFilters", _appliedFilters);
		_columnOriginalValues.put("ddmStructureId", _ddmStructureId);
		_columnOriginalValues.put("fieldNames", _fieldNames);
		_columnOriginalValues.put("name", _name);
		_columnOriginalValues.put("sortField", _sortField);
	}

	private static final Map<String, String> _attributeNames;

	static {
		Map<String, String> attributeNames = new HashMap<>();

		attributeNames.put("uuid_", "uuid");

		_attributeNames = Collections.unmodifiableMap(attributeNames);
	}

	private transient Map<String, Object> _columnOriginalValues;

	public static long getColumnBitmask(String columnName) {
		return _columnBitmasks.get(columnName);
	}

	private static final Map<String, Long> _columnBitmasks;

	static {
		Map<String, Long> columnBitmasks = new HashMap<>();

		columnBitmasks.put("mvccVersion", 1L);

		columnBitmasks.put("ctCollectionId", 2L);

		columnBitmasks.put("uuid_", 4L);

		columnBitmasks.put("deDataListViewId", 8L);

		columnBitmasks.put("groupId", 16L);

		columnBitmasks.put("companyId", 32L);

		columnBitmasks.put("userId", 64L);

		columnBitmasks.put("userName", 128L);

		columnBitmasks.put("createDate", 256L);

		columnBitmasks.put("modifiedDate", 512L);

		columnBitmasks.put("appliedFilters", 1024L);

		columnBitmasks.put("ddmStructureId", 2048L);

		columnBitmasks.put("fieldNames", 4096L);

		columnBitmasks.put("name", 8192L);

		columnBitmasks.put("sortField", 16384L);

		_columnBitmasks = Collections.unmodifiableMap(columnBitmasks);
	}

	private long _columnBitmask;
	private DEDataListView _escapedModel;

}