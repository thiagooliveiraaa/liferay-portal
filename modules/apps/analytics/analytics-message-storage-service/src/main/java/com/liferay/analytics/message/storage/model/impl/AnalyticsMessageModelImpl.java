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

package com.liferay.analytics.message.storage.model.impl;

import com.liferay.analytics.message.storage.model.AnalyticsMessage;
import com.liferay.analytics.message.storage.model.AnalyticsMessageBodyBlobModel;
import com.liferay.analytics.message.storage.model.AnalyticsMessageModel;
import com.liferay.analytics.message.storage.service.AnalyticsMessageLocalServiceUtil;
import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.sql.Blob;
import java.sql.Types;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The base model implementation for the AnalyticsMessage service. Represents a row in the &quot;AnalyticsMessage&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface <code>AnalyticsMessageModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link AnalyticsMessageImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AnalyticsMessageImpl
 * @generated
 */
public class AnalyticsMessageModelImpl
	extends BaseModelImpl<AnalyticsMessage> implements AnalyticsMessageModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a analytics message model instance should use the <code>AnalyticsMessage</code> interface instead.
	 */
	public static final String TABLE_NAME = "AnalyticsMessage";

	public static final Object[][] TABLE_COLUMNS = {
		{"mvccVersion", Types.BIGINT}, {"ctCollectionId", Types.BIGINT},
		{"analyticsMessageId", Types.BIGINT}, {"companyId", Types.BIGINT},
		{"userId", Types.BIGINT}, {"userName", Types.VARCHAR},
		{"createDate", Types.TIMESTAMP}, {"body", Types.BLOB}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("mvccVersion", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("ctCollectionId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("analyticsMessageId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("body", Types.BLOB);
	}

	public static final String TABLE_SQL_CREATE =
		"create table AnalyticsMessage (mvccVersion LONG default 0 not null,ctCollectionId LONG default 0 not null,analyticsMessageId LONG not null,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,body BLOB,primary key (analyticsMessageId, ctCollectionId))";

	public static final String TABLE_SQL_DROP = "drop table AnalyticsMessage";

	public static final String ORDER_BY_JPQL =
		" ORDER BY analyticsMessage.analyticsMessageId ASC";

	public static final String ORDER_BY_SQL =
		" ORDER BY AnalyticsMessage.analyticsMessageId ASC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long COMPANYID_COLUMN_BITMASK = 1L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *		#getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long ANALYTICSMESSAGEID_COLUMN_BITMASK = 2L;

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

	public AnalyticsMessageModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _analyticsMessageId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setAnalyticsMessageId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _analyticsMessageId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return AnalyticsMessage.class;
	}

	@Override
	public String getModelClassName() {
		return AnalyticsMessage.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<AnalyticsMessage, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		for (Map.Entry<String, Function<AnalyticsMessage, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<AnalyticsMessage, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName,
				attributeGetterFunction.apply((AnalyticsMessage)this));
		}

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<AnalyticsMessage, Object>>
			attributeSetterBiConsumers = getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<AnalyticsMessage, Object> attributeSetterBiConsumer =
				attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(AnalyticsMessage)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<AnalyticsMessage, Object>>
		getAttributeGetterFunctions() {

		return AttributeGetterFunctionsHolder._attributeGetterFunctions;
	}

	public Map<String, BiConsumer<AnalyticsMessage, Object>>
		getAttributeSetterBiConsumers() {

		return AttributeSetterBiConsumersHolder._attributeSetterBiConsumers;
	}

	private static class AttributeGetterFunctionsHolder {

		private static final Map<String, Function<AnalyticsMessage, Object>>
			_attributeGetterFunctions;

		static {
			Map<String, Function<AnalyticsMessage, Object>>
				attributeGetterFunctions =
					new LinkedHashMap
						<String, Function<AnalyticsMessage, Object>>();

			attributeGetterFunctions.put(
				"mvccVersion", AnalyticsMessage::getMvccVersion);
			attributeGetterFunctions.put(
				"ctCollectionId", AnalyticsMessage::getCtCollectionId);
			attributeGetterFunctions.put(
				"analyticsMessageId", AnalyticsMessage::getAnalyticsMessageId);
			attributeGetterFunctions.put(
				"companyId", AnalyticsMessage::getCompanyId);
			attributeGetterFunctions.put("userId", AnalyticsMessage::getUserId);
			attributeGetterFunctions.put(
				"userName", AnalyticsMessage::getUserName);
			attributeGetterFunctions.put(
				"createDate", AnalyticsMessage::getCreateDate);
			attributeGetterFunctions.put("body", AnalyticsMessage::getBody);

			_attributeGetterFunctions = Collections.unmodifiableMap(
				attributeGetterFunctions);
		}

	}

	private static class AttributeSetterBiConsumersHolder {

		private static final Map<String, BiConsumer<AnalyticsMessage, Object>>
			_attributeSetterBiConsumers;

		static {
			Map<String, BiConsumer<AnalyticsMessage, ?>>
				attributeSetterBiConsumers =
					new LinkedHashMap
						<String, BiConsumer<AnalyticsMessage, ?>>();

			attributeSetterBiConsumers.put(
				"mvccVersion",
				(BiConsumer<AnalyticsMessage, Long>)
					AnalyticsMessage::setMvccVersion);
			attributeSetterBiConsumers.put(
				"ctCollectionId",
				(BiConsumer<AnalyticsMessage, Long>)
					AnalyticsMessage::setCtCollectionId);
			attributeSetterBiConsumers.put(
				"analyticsMessageId",
				(BiConsumer<AnalyticsMessage, Long>)
					AnalyticsMessage::setAnalyticsMessageId);
			attributeSetterBiConsumers.put(
				"companyId",
				(BiConsumer<AnalyticsMessage, Long>)
					AnalyticsMessage::setCompanyId);
			attributeSetterBiConsumers.put(
				"userId",
				(BiConsumer<AnalyticsMessage, Long>)
					AnalyticsMessage::setUserId);
			attributeSetterBiConsumers.put(
				"userName",
				(BiConsumer<AnalyticsMessage, String>)
					AnalyticsMessage::setUserName);
			attributeSetterBiConsumers.put(
				"createDate",
				(BiConsumer<AnalyticsMessage, Date>)
					AnalyticsMessage::setCreateDate);
			attributeSetterBiConsumers.put(
				"body",
				(BiConsumer<AnalyticsMessage, Blob>)AnalyticsMessage::setBody);

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
	public long getAnalyticsMessageId() {
		return _analyticsMessageId;
	}

	@Override
	public void setAnalyticsMessageId(long analyticsMessageId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_analyticsMessageId = analyticsMessageId;
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
	public Blob getBody() {
		if (_bodyBlobModel == null) {
			try {
				_bodyBlobModel =
					AnalyticsMessageLocalServiceUtil.getBodyBlobModel(
						getPrimaryKey());
			}
			catch (Exception exception) {
			}
		}

		Blob blob = null;

		if (_bodyBlobModel != null) {
			blob = _bodyBlobModel.getBodyBlob();
		}

		return blob;
	}

	@Override
	public void setBody(Blob body) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		if (_bodyBlobModel == null) {
			_bodyBlobModel = new AnalyticsMessageBodyBlobModel(
				getPrimaryKey(), body);
		}
		else {
			_bodyBlobModel.setBodyBlob(body);
		}
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
			getCompanyId(), AnalyticsMessage.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public AnalyticsMessage toEscapedModel() {
		if (_escapedModel == null) {
			Function<InvocationHandler, AnalyticsMessage>
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
		AnalyticsMessageImpl analyticsMessageImpl = new AnalyticsMessageImpl();

		analyticsMessageImpl.setMvccVersion(getMvccVersion());
		analyticsMessageImpl.setCtCollectionId(getCtCollectionId());
		analyticsMessageImpl.setAnalyticsMessageId(getAnalyticsMessageId());
		analyticsMessageImpl.setCompanyId(getCompanyId());
		analyticsMessageImpl.setUserId(getUserId());
		analyticsMessageImpl.setUserName(getUserName());
		analyticsMessageImpl.setCreateDate(getCreateDate());

		analyticsMessageImpl.resetOriginalValues();

		return analyticsMessageImpl;
	}

	@Override
	public AnalyticsMessage cloneWithOriginalValues() {
		AnalyticsMessageImpl analyticsMessageImpl = new AnalyticsMessageImpl();

		analyticsMessageImpl.setMvccVersion(
			this.<Long>getColumnOriginalValue("mvccVersion"));
		analyticsMessageImpl.setCtCollectionId(
			this.<Long>getColumnOriginalValue("ctCollectionId"));
		analyticsMessageImpl.setAnalyticsMessageId(
			this.<Long>getColumnOriginalValue("analyticsMessageId"));
		analyticsMessageImpl.setCompanyId(
			this.<Long>getColumnOriginalValue("companyId"));
		analyticsMessageImpl.setUserId(
			this.<Long>getColumnOriginalValue("userId"));
		analyticsMessageImpl.setUserName(
			this.<String>getColumnOriginalValue("userName"));
		analyticsMessageImpl.setCreateDate(
			this.<Date>getColumnOriginalValue("createDate"));

		return analyticsMessageImpl;
	}

	@Override
	public int compareTo(AnalyticsMessage analyticsMessage) {
		int value = 0;

		if (getAnalyticsMessageId() <
				analyticsMessage.getAnalyticsMessageId()) {

			value = -1;
		}
		else if (getAnalyticsMessageId() >
					analyticsMessage.getAnalyticsMessageId()) {

			value = 1;
		}
		else {
			value = 0;
		}

		if (value != 0) {
			return value;
		}

		return 0;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof AnalyticsMessage)) {
			return false;
		}

		AnalyticsMessage analyticsMessage = (AnalyticsMessage)object;

		long primaryKey = analyticsMessage.getPrimaryKey();

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

		_bodyBlobModel = null;

		_columnBitmask = 0;
	}

	@Override
	public CacheModel<AnalyticsMessage> toCacheModel() {
		AnalyticsMessageCacheModel analyticsMessageCacheModel =
			new AnalyticsMessageCacheModel();

		analyticsMessageCacheModel.mvccVersion = getMvccVersion();

		analyticsMessageCacheModel.ctCollectionId = getCtCollectionId();

		analyticsMessageCacheModel.analyticsMessageId = getAnalyticsMessageId();

		analyticsMessageCacheModel.companyId = getCompanyId();

		analyticsMessageCacheModel.userId = getUserId();

		analyticsMessageCacheModel.userName = getUserName();

		String userName = analyticsMessageCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			analyticsMessageCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			analyticsMessageCacheModel.createDate = createDate.getTime();
		}
		else {
			analyticsMessageCacheModel.createDate = Long.MIN_VALUE;
		}

		return analyticsMessageCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(17);

		sb.append("{\"mvccVersion\": ");

		sb.append(getMvccVersion());

		sb.append(", \"ctCollectionId\": ");

		sb.append(getCtCollectionId());

		sb.append(", \"analyticsMessageId\": ");

		sb.append(getAnalyticsMessageId());

		sb.append(", \"companyId\": ");

		sb.append(getCompanyId());

		sb.append(", \"userId\": ");

		sb.append(getUserId());

		sb.append(", \"userName\": ");

		sb.append("\"" + getUserName() + "\"");

		sb.append(", \"createDate\": ");

		sb.append("\"" + getCreateDate() + "\"");

		return sb.toString();
	}

	private static class EscapedModelProxyProviderFunctionHolder {

		private static final Function<InvocationHandler, AnalyticsMessage>
			_escapedModelProxyProviderFunction =
				ProxyUtil.getProxyProviderFunction(
					AnalyticsMessage.class, ModelWrapper.class);

	}

	private long _mvccVersion;
	private long _ctCollectionId;
	private long _analyticsMessageId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private AnalyticsMessageBodyBlobModel _bodyBlobModel;

	public <T> T getColumnValue(String columnName) {
		Function<AnalyticsMessage, Object> function =
			AttributeGetterFunctionsHolder._attributeGetterFunctions.get(
				columnName);

		if (function == null) {
			throw new IllegalArgumentException(
				"No attribute getter function found for " + columnName);
		}

		return (T)function.apply((AnalyticsMessage)this);
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
		_columnOriginalValues.put("analyticsMessageId", _analyticsMessageId);
		_columnOriginalValues.put("companyId", _companyId);
		_columnOriginalValues.put("userId", _userId);
		_columnOriginalValues.put("userName", _userName);
		_columnOriginalValues.put("createDate", _createDate);
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

		columnBitmasks.put("analyticsMessageId", 4L);

		columnBitmasks.put("companyId", 8L);

		columnBitmasks.put("userId", 16L);

		columnBitmasks.put("userName", 32L);

		columnBitmasks.put("createDate", 64L);

		columnBitmasks.put("body", 128L);

		_columnBitmasks = Collections.unmodifiableMap(columnBitmasks);
	}

	private long _columnBitmask;
	private AnalyticsMessage _escapedModel;

}