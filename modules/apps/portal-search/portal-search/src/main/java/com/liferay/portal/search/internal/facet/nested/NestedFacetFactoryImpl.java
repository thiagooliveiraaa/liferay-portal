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

package com.liferay.portal.search.internal.facet.nested;

import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.search.facet.Facet;
import com.liferay.portal.search.facet.nested.NestedFacetFactory;
import com.liferay.portal.search.internal.facet.NestedFacetImpl;

import org.osgi.service.component.annotations.Component;

/**
 * @author Jorge Díaz
 */
@Component(service = NestedFacetFactory.class)
public class NestedFacetFactoryImpl implements NestedFacetFactory {

	@Override
	public String getFacetClassName() {
		return NestedFacetFactory.class.getName();
	}

	@Override
	public Facet newInstance(SearchContext searchContext) {
		return new NestedFacetImpl(null, searchContext);
	}

}