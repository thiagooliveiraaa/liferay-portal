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

package com.liferay.headless.commerce.delivery.catalog.dto.v1_0;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;
import com.liferay.portal.vulcan.util.ObjectMapperUtil;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Generated;

import javax.validation.constraints.DecimalMin;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Andrea Sbarra
 * @generated
 */
@Generated("")
@GraphQLName("SkuOption")
@JsonFilter("Liferay.Vulcan")
@XmlRootElement(name = "SkuOption")
public class SkuOption implements Serializable {

	public static SkuOption toDTO(String json) {
		return ObjectMapperUtil.readValue(SkuOption.class, json);
	}

	public static SkuOption unsafeToDTO(String json) {
		return ObjectMapperUtil.unsafeReadValue(SkuOption.class, json);
	}

	@DecimalMin("0")
	@Schema(example = "31130")
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	@JsonIgnore
	public void setKey(UnsafeSupplier<Long, Exception> keyUnsafeSupplier) {
		try {
			key = keyUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	protected Long key;

	@Schema(example = "30130")
	public Long getSkuOptionId() {
		return skuOptionId;
	}

	public void setSkuOptionId(Long skuOptionId) {
		this.skuOptionId = skuOptionId;
	}

	@JsonIgnore
	public void setSkuOptionId(
		UnsafeSupplier<Long, Exception> skuOptionIdUnsafeSupplier) {

		try {
			skuOptionId = skuOptionIdUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Long skuOptionId;

	@Schema(example = "Sku Option Key")
	public String getSkuOptionKey() {
		return skuOptionKey;
	}

	public void setSkuOptionKey(String skuOptionKey) {
		this.skuOptionKey = skuOptionKey;
	}

	@JsonIgnore
	public void setSkuOptionKey(
		UnsafeSupplier<String, Exception> skuOptionKeyUnsafeSupplier) {

		try {
			skuOptionKey = skuOptionKeyUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String skuOptionKey;

	@Schema(example = "30130")
	public Long getSkuOptionValueId() {
		return skuOptionValueId;
	}

	public void setSkuOptionValueId(Long skuOptionValueId) {
		this.skuOptionValueId = skuOptionValueId;
	}

	@JsonIgnore
	public void setSkuOptionValueId(
		UnsafeSupplier<Long, Exception> skuOptionValueIdUnsafeSupplier) {

		try {
			skuOptionValueId = skuOptionValueIdUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Long skuOptionValueId;

	@Schema(example = "Sku Option Value Key")
	public String getSkuOptionValueKey() {
		return skuOptionValueKey;
	}

	public void setSkuOptionValueKey(String skuOptionValueKey) {
		this.skuOptionValueKey = skuOptionValueKey;
	}

	@JsonIgnore
	public void setSkuOptionValueKey(
		UnsafeSupplier<String, Exception> skuOptionValueKeyUnsafeSupplier) {

		try {
			skuOptionValueKey = skuOptionValueKeyUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String skuOptionValueKey;

	@DecimalMin("0")
	@Schema(example = "31130")
	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	@JsonIgnore
	public void setValue(UnsafeSupplier<Long, Exception> valueUnsafeSupplier) {
		try {
			value = valueUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	protected Long value;

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof SkuOption)) {
			return false;
		}

		SkuOption skuOption = (SkuOption)object;

		return Objects.equals(toString(), skuOption.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		StringBundler sb = new StringBundler();

		sb.append("{");

		if (key != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"key\": ");

			sb.append(key);
		}

		if (skuOptionId != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"skuOptionId\": ");

			sb.append(skuOptionId);
		}

		if (skuOptionKey != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"skuOptionKey\": ");

			sb.append("\"");

			sb.append(_escape(skuOptionKey));

			sb.append("\"");
		}

		if (skuOptionValueId != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"skuOptionValueId\": ");

			sb.append(skuOptionValueId);
		}

		if (skuOptionValueKey != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"skuOptionValueKey\": ");

			sb.append("\"");

			sb.append(_escape(skuOptionValueKey));

			sb.append("\"");
		}

		if (value != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"value\": ");

			sb.append(value);
		}

		sb.append("}");

		return sb.toString();
	}

	@Schema(
		accessMode = Schema.AccessMode.READ_ONLY,
		defaultValue = "com.liferay.headless.commerce.delivery.catalog.dto.v1_0.SkuOption",
		name = "x-class-name"
	)
	public String xClassName;

	private static String _escape(Object object) {
		return StringUtil.replace(
			String.valueOf(object), _JSON_ESCAPE_STRINGS[0],
			_JSON_ESCAPE_STRINGS[1]);
	}

	private static boolean _isArray(Object value) {
		if (value == null) {
			return false;
		}

		Class<?> clazz = value.getClass();

		return clazz.isArray();
	}

	private static String _toJSON(Map<String, ?> map) {
		StringBuilder sb = new StringBuilder("{");

		@SuppressWarnings("unchecked")
		Set set = map.entrySet();

		@SuppressWarnings("unchecked")
		Iterator<Map.Entry<String, ?>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, ?> entry = iterator.next();

			sb.append("\"");
			sb.append(_escape(entry.getKey()));
			sb.append("\": ");

			Object value = entry.getValue();

			if (_isArray(value)) {
				sb.append("[");

				Object[] valueArray = (Object[])value;

				for (int i = 0; i < valueArray.length; i++) {
					if (valueArray[i] instanceof String) {
						sb.append("\"");
						sb.append(valueArray[i]);
						sb.append("\"");
					}
					else {
						sb.append(valueArray[i]);
					}

					if ((i + 1) < valueArray.length) {
						sb.append(", ");
					}
				}

				sb.append("]");
			}
			else if (value instanceof Map) {
				sb.append(_toJSON((Map<String, ?>)value));
			}
			else if (value instanceof String) {
				sb.append("\"");
				sb.append(_escape(value));
				sb.append("\"");
			}
			else {
				sb.append(value);
			}

			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}

		sb.append("}");

		return sb.toString();
	}

	private static final String[][] _JSON_ESCAPE_STRINGS = {
		{"\\", "\"", "\b", "\f", "\n", "\r", "\t"},
		{"\\\\", "\\\"", "\\b", "\\f", "\\n", "\\r", "\\t"}
	};

}