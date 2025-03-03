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

package com.liferay.organizations.item.selector;

import com.liferay.item.selector.BaseItemSelectorCriterion;

/**
 * @author Alessio Antonio Rendina
 */
public class OrganizationItemSelectorCriterion
	extends BaseItemSelectorCriterion {

	public long[] getSelectedOrganizationIds() {
		return _selectedOrganizationIds;
	}

	public boolean isMultiSelection() {
		return _multiSelection;
	}

	public void setMultiSelection(boolean multiSelection) {
		_multiSelection = multiSelection;
	}

	public void setSelectedOrganizationIds(long[] selectedOrganizationIds) {
		_selectedOrganizationIds = selectedOrganizationIds;
	}

	private boolean _multiSelection;
	private long[] _selectedOrganizationIds;

}