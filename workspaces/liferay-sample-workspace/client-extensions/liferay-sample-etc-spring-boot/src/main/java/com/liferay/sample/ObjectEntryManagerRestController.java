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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Feliphe Marinho
 * @author Brian Wing Shun Chan
 */
@RequestMapping("/object/entry/manager/1")
@RestController
public class ObjectEntryManagerRestController extends BaseRestController {

	@PostMapping("/delete/object/entry")
	public ResponseEntity<String> deleteObjectEntry(
		@AuthenticationPrincipal Jwt jwt, @RequestBody String json) {

		JSONObject jsonObject = new JSONObject(json);

		if (_log.isInfoEnabled()) {
			_log.info("JWT Claims: " + jwt.getClaims());
			_log.info("JWT ID: " + jwt.getId());
			_log.info("JWT Subject: " + jwt.getSubject());
			_log.info("\n\n" + jsonObject.toString(4) + "\n");
		}

		_objectEntryJSONObjects.remove(
			String.valueOf(jsonObject.get("externalReferenceCode")));

		return new ResponseEntity<>(json, HttpStatus.CREATED);
	}

	@PostMapping("/get/object/entries")
	public ResponseEntity<String> postSampleObjectEntryManager1GetObjectEntries(
		@AuthenticationPrincipal Jwt jwt, @RequestBody String json) {

		if (_log.isInfoEnabled()) {
			_log.info("JWT Claims: " + jwt.getClaims());
			_log.info("JWT ID: " + jwt.getId());
			_log.info("JWT Subject: " + jwt.getSubject());

			try {
				JSONObject jsonObject = new JSONObject(json);

				_log.info("\n\n" + jsonObject.toString(4) + "\n");
			}
			catch (Exception exception) {
				_log.error("JSON: " + json, exception);
			}
		}

		return new ResponseEntity<>(
			new JSONObject(
			).put(
				"items", _objectEntryJSONObjects.values()
			).put(
				"totalCount", _objectEntryJSONObjects.size()
			).toString(),
			HttpStatus.CREATED);
	}

	@PostMapping("/get/object/entry")
	public ResponseEntity<String> postSampleObjectEntryManager1GetObjectEntry(
		@AuthenticationPrincipal Jwt jwt, @RequestBody String json) {

		JSONObject jsonObject = new JSONObject(json);

		if (_log.isInfoEnabled()) {
			_log.info("JWT Claims: " + jwt.getClaims());
			_log.info("JWT ID: " + jwt.getId());
			_log.info("JWT Subject: " + jwt.getSubject());
			_log.info("\n\n" + jsonObject.toString(4) + "\n");
		}

		JSONObject objectEntryJSONObject = _objectEntryJSONObjects.get(
			String.valueOf(jsonObject.get("externalReferenceCode")));

		return new ResponseEntity<>(
			objectEntryJSONObject.toString(), HttpStatus.CREATED);
	}

	@PostMapping("/post/object/entry")
	public ResponseEntity<String> postSampleObjectEntryManager1PostObjectEntry(
		@AuthenticationPrincipal Jwt jwt, @RequestBody String json) {

		JSONObject jsonObject = new JSONObject(json);

		if (_log.isInfoEnabled()) {
			_log.info("JWT Claims: " + jwt.getClaims());
			_log.info("JWT ID: " + jwt.getId());
			_log.info("JWT Subject: " + jwt.getSubject());
			_log.info("\n\n" + jsonObject.toString(4) + "\n");
		}

		JSONObject objectEntryJSONObject = new JSONObject(
			jsonObject.get(
				"objectEntry"
			).toString());

		_objectEntryJSONObjects.put(
			String.valueOf(jsonObject.get("externalReferenceCode")),
			objectEntryJSONObject);

		return new ResponseEntity<>(
			objectEntryJSONObject.toString(), HttpStatus.CREATED);
	}

	@PostMapping("/put/object/entry")
	public ResponseEntity<String> postSampleObjectEntryManager1PuttObjectEntry(
		@AuthenticationPrincipal Jwt jwt, @RequestBody String json) {

		JSONObject jsonObject = new JSONObject(json);

		if (_log.isInfoEnabled()) {
			_log.info("JWT Claims: " + jwt.getClaims());
			_log.info("JWT ID: " + jwt.getId());
			_log.info("JWT Subject: " + jwt.getSubject());
			_log.info("\n\n" + jsonObject.toString(4) + "\n");
		}

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
			objectEntryJSONObject.toString(), HttpStatus.CREATED);
	}

	private static final Log _log = LogFactory.getLog(
		ObjectEntryManagerRestController.class);

	private static final Map<String, JSONObject> _objectEntryJSONObjects =
		new HashMap<>();

}