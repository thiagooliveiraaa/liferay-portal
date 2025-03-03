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

package com.liferay.push.notifications.model.impl;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.push.notifications.model.PushNotificationsDevice;
import com.liferay.push.notifications.model.PushNotificationsDeviceModel;

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
 * The base model implementation for the PushNotificationsDevice service. Represents a row in the &quot;PushNotificationsDevice&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface <code>PushNotificationsDeviceModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link PushNotificationsDeviceImpl}.
 * </p>
 *
 * @author Bruno Farache
 * @see PushNotificationsDeviceImpl
 * @generated
 */
@JSON(strict = true)
public class PushNotificationsDeviceModelImpl
	extends BaseModelImpl<PushNotificationsDevice>
	implements PushNotificationsDeviceModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a push notifications device model instance should use the <code>PushNotificationsDevice</code> interface instead.
	 */
	public static final String TABLE_NAME = "PushNotificationsDevice";

	public static final Object[][] TABLE_COLUMNS = {
		{"pushNotificationsDeviceId", Types.BIGINT},
		{"companyId", Types.BIGINT}, {"userId", Types.BIGINT},
		{"createDate", Types.TIMESTAMP}, {"platform", Types.VARCHAR},
		{"token", Types.VARCHAR}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("pushNotificationsDeviceId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("platform", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("token", Types.VARCHAR);
	}

	public static final String TABLE_SQL_CREATE =
		"create table PushNotificationsDevice (pushNotificationsDeviceId LONG not null primary key,companyId LONG,userId LONG,createDate DATE null,platform VARCHAR(75) null,token STRING null)";

	public static final String TABLE_SQL_DROP =
		"drop table PushNotificationsDevice";

	public static final String ORDER_BY_JPQL =
		" ORDER BY pushNotificationsDevice.pushNotificationsDeviceId ASC";

	public static final String ORDER_BY_SQL =
		" ORDER BY PushNotificationsDevice.pushNotificationsDeviceId ASC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long PLATFORM_COLUMN_BITMASK = 1L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long TOKEN_COLUMN_BITMASK = 2L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long USERID_COLUMN_BITMASK = 4L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *		#getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long PUSHNOTIFICATIONSDEVICEID_COLUMN_BITMASK = 8L;

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

	public PushNotificationsDeviceModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _pushNotificationsDeviceId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setPushNotificationsDeviceId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _pushNotificationsDeviceId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return PushNotificationsDevice.class;
	}

	@Override
	public String getModelClassName() {
		return PushNotificationsDevice.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<PushNotificationsDevice, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		for (Map.Entry<String, Function<PushNotificationsDevice, Object>>
				entry : attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<PushNotificationsDevice, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName,
				attributeGetterFunction.apply((PushNotificationsDevice)this));
		}

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<PushNotificationsDevice, Object>>
			attributeSetterBiConsumers = getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<PushNotificationsDevice, Object>
				attributeSetterBiConsumer = attributeSetterBiConsumers.get(
					attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(PushNotificationsDevice)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<PushNotificationsDevice, Object>>
		getAttributeGetterFunctions() {

		return AttributeGetterFunctionsHolder._attributeGetterFunctions;
	}

	public Map<String, BiConsumer<PushNotificationsDevice, Object>>
		getAttributeSetterBiConsumers() {

		return AttributeSetterBiConsumersHolder._attributeSetterBiConsumers;
	}

	private static class AttributeGetterFunctionsHolder {

		private static final Map
			<String, Function<PushNotificationsDevice, Object>>
				_attributeGetterFunctions;

		static {
			Map<String, Function<PushNotificationsDevice, Object>>
				attributeGetterFunctions =
					new LinkedHashMap
						<String, Function<PushNotificationsDevice, Object>>();

			attributeGetterFunctions.put(
				"pushNotificationsDeviceId",
				PushNotificationsDevice::getPushNotificationsDeviceId);
			attributeGetterFunctions.put(
				"companyId", PushNotificationsDevice::getCompanyId);
			attributeGetterFunctions.put(
				"userId", PushNotificationsDevice::getUserId);
			attributeGetterFunctions.put(
				"createDate", PushNotificationsDevice::getCreateDate);
			attributeGetterFunctions.put(
				"platform", PushNotificationsDevice::getPlatform);
			attributeGetterFunctions.put(
				"token", PushNotificationsDevice::getToken);

			_attributeGetterFunctions = Collections.unmodifiableMap(
				attributeGetterFunctions);
		}

	}

	private static class AttributeSetterBiConsumersHolder {

		private static final Map
			<String, BiConsumer<PushNotificationsDevice, Object>>
				_attributeSetterBiConsumers;

		static {
			Map<String, BiConsumer<PushNotificationsDevice, ?>>
				attributeSetterBiConsumers =
					new LinkedHashMap
						<String, BiConsumer<PushNotificationsDevice, ?>>();

			attributeSetterBiConsumers.put(
				"pushNotificationsDeviceId",
				(BiConsumer<PushNotificationsDevice, Long>)
					PushNotificationsDevice::setPushNotificationsDeviceId);
			attributeSetterBiConsumers.put(
				"companyId",
				(BiConsumer<PushNotificationsDevice, Long>)
					PushNotificationsDevice::setCompanyId);
			attributeSetterBiConsumers.put(
				"userId",
				(BiConsumer<PushNotificationsDevice, Long>)
					PushNotificationsDevice::setUserId);
			attributeSetterBiConsumers.put(
				"createDate",
				(BiConsumer<PushNotificationsDevice, Date>)
					PushNotificationsDevice::setCreateDate);
			attributeSetterBiConsumers.put(
				"platform",
				(BiConsumer<PushNotificationsDevice, String>)
					PushNotificationsDevice::setPlatform);
			attributeSetterBiConsumers.put(
				"token",
				(BiConsumer<PushNotificationsDevice, String>)
					PushNotificationsDevice::setToken);

			_attributeSetterBiConsumers = Collections.unmodifiableMap(
				(Map)attributeSetterBiConsumers);
		}

	}

	@JSON
	@Override
	public long getPushNotificationsDeviceId() {
		return _pushNotificationsDeviceId;
	}

	@Override
	public void setPushNotificationsDeviceId(long pushNotificationsDeviceId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_pushNotificationsDeviceId = pushNotificationsDeviceId;
	}

	@JSON
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

	@JSON
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

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalUserId() {
		return GetterUtil.getLong(this.<Long>getColumnOriginalValue("userId"));
	}

	@JSON
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

	@JSON
	@Override
	public String getPlatform() {
		if (_platform == null) {
			return "";
		}
		else {
			return _platform;
		}
	}

	@Override
	public void setPlatform(String platform) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_platform = platform;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public String getOriginalPlatform() {
		return getColumnOriginalValue("platform");
	}

	@JSON
	@Override
	public String getToken() {
		if (_token == null) {
			return "";
		}
		else {
			return _token;
		}
	}

	@Override
	public void setToken(String token) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_token = token;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public String getOriginalToken() {
		return getColumnOriginalValue("token");
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
			getCompanyId(), PushNotificationsDevice.class.getName(),
			getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public PushNotificationsDevice toEscapedModel() {
		if (_escapedModel == null) {
			Function<InvocationHandler, PushNotificationsDevice>
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
		PushNotificationsDeviceImpl pushNotificationsDeviceImpl =
			new PushNotificationsDeviceImpl();

		pushNotificationsDeviceImpl.setPushNotificationsDeviceId(
			getPushNotificationsDeviceId());
		pushNotificationsDeviceImpl.setCompanyId(getCompanyId());
		pushNotificationsDeviceImpl.setUserId(getUserId());
		pushNotificationsDeviceImpl.setCreateDate(getCreateDate());
		pushNotificationsDeviceImpl.setPlatform(getPlatform());
		pushNotificationsDeviceImpl.setToken(getToken());

		pushNotificationsDeviceImpl.resetOriginalValues();

		return pushNotificationsDeviceImpl;
	}

	@Override
	public PushNotificationsDevice cloneWithOriginalValues() {
		PushNotificationsDeviceImpl pushNotificationsDeviceImpl =
			new PushNotificationsDeviceImpl();

		pushNotificationsDeviceImpl.setPushNotificationsDeviceId(
			this.<Long>getColumnOriginalValue("pushNotificationsDeviceId"));
		pushNotificationsDeviceImpl.setCompanyId(
			this.<Long>getColumnOriginalValue("companyId"));
		pushNotificationsDeviceImpl.setUserId(
			this.<Long>getColumnOriginalValue("userId"));
		pushNotificationsDeviceImpl.setCreateDate(
			this.<Date>getColumnOriginalValue("createDate"));
		pushNotificationsDeviceImpl.setPlatform(
			this.<String>getColumnOriginalValue("platform"));
		pushNotificationsDeviceImpl.setToken(
			this.<String>getColumnOriginalValue("token"));

		return pushNotificationsDeviceImpl;
	}

	@Override
	public int compareTo(PushNotificationsDevice pushNotificationsDevice) {
		long primaryKey = pushNotificationsDevice.getPrimaryKey();

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

		if (!(object instanceof PushNotificationsDevice)) {
			return false;
		}

		PushNotificationsDevice pushNotificationsDevice =
			(PushNotificationsDevice)object;

		long primaryKey = pushNotificationsDevice.getPrimaryKey();

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

		_columnBitmask = 0;
	}

	@Override
	public CacheModel<PushNotificationsDevice> toCacheModel() {
		PushNotificationsDeviceCacheModel pushNotificationsDeviceCacheModel =
			new PushNotificationsDeviceCacheModel();

		pushNotificationsDeviceCacheModel.pushNotificationsDeviceId =
			getPushNotificationsDeviceId();

		pushNotificationsDeviceCacheModel.companyId = getCompanyId();

		pushNotificationsDeviceCacheModel.userId = getUserId();

		Date createDate = getCreateDate();

		if (createDate != null) {
			pushNotificationsDeviceCacheModel.createDate = createDate.getTime();
		}
		else {
			pushNotificationsDeviceCacheModel.createDate = Long.MIN_VALUE;
		}

		pushNotificationsDeviceCacheModel.platform = getPlatform();

		String platform = pushNotificationsDeviceCacheModel.platform;

		if ((platform != null) && (platform.length() == 0)) {
			pushNotificationsDeviceCacheModel.platform = null;
		}

		pushNotificationsDeviceCacheModel.token = getToken();

		String token = pushNotificationsDeviceCacheModel.token;

		if ((token != null) && (token.length() == 0)) {
			pushNotificationsDeviceCacheModel.token = null;
		}

		return pushNotificationsDeviceCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<PushNotificationsDevice, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			(5 * attributeGetterFunctions.size()) + 2);

		sb.append("{");

		for (Map.Entry<String, Function<PushNotificationsDevice, Object>>
				entry : attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<PushNotificationsDevice, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("\"");
			sb.append(attributeName);
			sb.append("\": ");

			Object value = attributeGetterFunction.apply(
				(PushNotificationsDevice)this);

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

		private static final Function
			<InvocationHandler, PushNotificationsDevice>
				_escapedModelProxyProviderFunction =
					ProxyUtil.getProxyProviderFunction(
						PushNotificationsDevice.class, ModelWrapper.class);

	}

	private long _pushNotificationsDeviceId;
	private long _companyId;
	private long _userId;
	private Date _createDate;
	private String _platform;
	private String _token;

	public <T> T getColumnValue(String columnName) {
		Function<PushNotificationsDevice, Object> function =
			AttributeGetterFunctionsHolder._attributeGetterFunctions.get(
				columnName);

		if (function == null) {
			throw new IllegalArgumentException(
				"No attribute getter function found for " + columnName);
		}

		return (T)function.apply((PushNotificationsDevice)this);
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

		_columnOriginalValues.put(
			"pushNotificationsDeviceId", _pushNotificationsDeviceId);
		_columnOriginalValues.put("companyId", _companyId);
		_columnOriginalValues.put("userId", _userId);
		_columnOriginalValues.put("createDate", _createDate);
		_columnOriginalValues.put("platform", _platform);
		_columnOriginalValues.put("token", _token);
	}

	private transient Map<String, Object> _columnOriginalValues;

	public static long getColumnBitmask(String columnName) {
		return _columnBitmasks.get(columnName);
	}

	private static final Map<String, Long> _columnBitmasks;

	static {
		Map<String, Long> columnBitmasks = new HashMap<>();

		columnBitmasks.put("pushNotificationsDeviceId", 1L);

		columnBitmasks.put("companyId", 2L);

		columnBitmasks.put("userId", 4L);

		columnBitmasks.put("createDate", 8L);

		columnBitmasks.put("platform", 16L);

		columnBitmasks.put("token", 32L);

		_columnBitmasks = Collections.unmodifiableMap(columnBitmasks);
	}

	private long _columnBitmask;
	private PushNotificationsDevice _escapedModel;

}