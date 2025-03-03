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

package com.liferay.exportimport.kernel.xstream;

import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerList;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerListFactory;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;
import com.liferay.portal.kernel.util.SetUtil;

import java.util.Set;

/**
 * @author Daniel Kocsis
 */
public class XStreamConverterRegistryUtil {

	public static Set<XStreamConverter> getXStreamConverters() {
		return SetUtil.fromList(_xStreamConverters.toList());
	}

	private XStreamConverterRegistryUtil() {
	}

	private static final ServiceTrackerList<XStreamConverter>
		_xStreamConverters = ServiceTrackerListFactory.open(
			SystemBundleUtil.getBundleContext(), XStreamConverter.class);

}