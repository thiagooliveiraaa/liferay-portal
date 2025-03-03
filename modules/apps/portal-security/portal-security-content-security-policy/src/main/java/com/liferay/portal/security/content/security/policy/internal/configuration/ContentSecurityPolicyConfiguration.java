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

package com.liferay.portal.security.content.security.policy.internal.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedAttributeDefinition;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Olivér Kecskeméty
 */
@ExtendedObjectClassDefinition(
	category = "content-security-policy",
	scope = ExtendedObjectClassDefinition.Scope.GROUP
)
@Meta.OCD(
	id = "com.liferay.portal.security.content.security.policy.internal.configuration.ContentSecurityPolicyConfiguration",
	localization = "content/Language",
	name = "content-security-policy-configuration-name"
)
public interface ContentSecurityPolicyConfiguration {

	@Meta.AD(deflt = "false", name = "enabled", required = false)
	public boolean enabled();

	@ExtendedAttributeDefinition(descriptionArguments = "[$NONCE$]")
	@Meta.AD(
		description = "content-security-policy-help",
		name = "content-security-policy", required = false
	)
	public String policy();

	@Meta.AD(
		deflt = "/api/,/combo,/documents/,/image/,/layouttpl/,/o/,/webdav/",
		description = "content-security-policy-excluded-paths-help",
		name = "excluded-paths", required = false
	)
	public String[] excludedPaths();

}