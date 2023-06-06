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

package com.liferay.commerce.product.util;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;

/**
 * @author Igor Beslic
 * @author Brian Wing Shun Chan
 */
public class CPJSONUtil {

	public static JSONArray getJSONArray(JSONObject jsonObject, String key) {
		JSONArray jsonArray = jsonObject.getJSONArray(key);

		if (jsonArray != null) {
			return jsonArray;
		}

		jsonArray = JSONFactoryUtil.createJSONArray();

		String string = jsonObject.getString(key);

		if (string != null) {
			jsonArray.put(string);
		}

		return jsonArray;
	}

	public static JSONArray toJSONArray(Map<String, List<String>> keyValues) {
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		for (Map.Entry<String, List<String>> keyValuesEntry :
				keyValues.entrySet()) {

			JSONObject arrayEntryJSONObject = _jsonFactory.createJSONObject();

			arrayEntryJSONObject.put("key", keyValuesEntry.getKey());

			JSONArray valuesJSONArray = _jsonFactory.createJSONArray();

			List<String> values = keyValuesEntry.getValue();

			for (String value : values) {
				valuesJSONArray.put(value);
			}

			arrayEntryJSONObject.put("value", valuesJSONArray);

			jsonArray.put(arrayEntryJSONObject);
		}

		return jsonArray;
	}

}