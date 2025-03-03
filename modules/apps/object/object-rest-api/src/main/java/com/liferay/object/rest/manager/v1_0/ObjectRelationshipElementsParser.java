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

package com.liferay.object.rest.manager.v1_0;

import com.liferay.object.model.ObjectRelationship;

import java.util.List;

/**
 * @author Carlos Correa
 * @author Sergio Jimenez del Coso
 */
public interface ObjectRelationshipElementsParser<T> {

	public String getClassName();

	public long getCompanyId();

	public String getObjectRelationshipType();

	public List<T> parse(ObjectRelationship objectRelationship, Object value)
		throws Exception;

}