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

package com.liferay.sample;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONObject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Feliphe Marinho
 * @author Brian Wing Shun Chan
 */
@RequestMapping("/object/entry/manager/1")
@RestController
public class ObjectEntryManager1RestController extends BaseRestController {

	@DeleteMapping
	public ResponseEntity<String> delete(
		@AuthenticationPrincipal Jwt jwt,
		@PathVariable String externalReferenceCode) {

		log(jwt, _log);

		JSONObject objectEntryJSONObject = _objectEntryJSONObjects.remove(
			externalReferenceCode);

		return new ResponseEntity<>(
			String.valueOf(objectEntryJSONObject), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<String> get(@AuthenticationPrincipal Jwt jwt) {
		log(jwt, _log);

		return new ResponseEntity<>(
			new JSONObject(
			).put(
				"items", _objectEntryJSONObjects.values()
			).put(
				"totalCount", _objectEntryJSONObjects.size()
			).toString(),
			HttpStatus.OK);
	}

	@GetMapping("/${externalReferenceCode}")
	public ResponseEntity<String> get(
		@AuthenticationPrincipal Jwt jwt,
		@PathVariable String externalReferenceCode) {

		log(jwt, _log);

		JSONObject objectEntryJSONObject = _objectEntryJSONObjects.get(
			externalReferenceCode);

		return new ResponseEntity<>(
			String.valueOf(objectEntryJSONObject), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<String> post(
		@AuthenticationPrincipal Jwt jwt, @RequestBody String json) {

		log(jwt, _log, json);

		JSONObject jsonObject = new JSONObject(json);

		JSONObject objectEntryJSONObject = new JSONObject(
			jsonObject.get(
				"objectEntry"
			).toString());

		_objectEntryJSONObjects.put(
			String.valueOf(jsonObject.get("externalReferenceCode")),
			objectEntryJSONObject);

		return new ResponseEntity<>(
			objectEntryJSONObject.toString(), HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<String> put(
		@AuthenticationPrincipal Jwt jwt, @RequestBody String json) {

		log(jwt, _log, json);

		JSONObject jsonObject = new JSONObject(json);

		JSONObject objectEntryJSONObject = new JSONObject(
			jsonObject.get(
				"objectEntry"
			).toString());

		objectEntryJSONObject.put(
			"creator", Collections.singletonMap("name", "Creator Name"));

		String externalReferenceCode =
			"objectEntry" + (_objectEntryJSONObjects.size() + 1);

		objectEntryJSONObject.put(
			"externalReferenceCode", externalReferenceCode);

		_objectEntryJSONObjects.put(
			externalReferenceCode, objectEntryJSONObject);

		return new ResponseEntity<>(
			objectEntryJSONObject.toString(), HttpStatus.OK);
	}

	private static final Log _log = LogFactory.getLog(
		ObjectEntryManager1RestController.class);

	private static final Map<String, JSONObject> _objectEntryJSONObjects =
		new HashMap<>();

}