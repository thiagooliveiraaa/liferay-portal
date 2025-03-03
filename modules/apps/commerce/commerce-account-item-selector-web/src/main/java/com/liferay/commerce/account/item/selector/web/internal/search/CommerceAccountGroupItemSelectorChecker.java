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

package com.liferay.commerce.account.item.selector.web.internal.search;

import com.liferay.account.model.AccountGroup;
import com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker;
import com.liferay.portal.kernel.util.SetUtil;

import java.util.Set;

import javax.portlet.RenderResponse;

/**
 * @author Alessio Antonio Rendina
 */
public class CommerceAccountGroupItemSelectorChecker
	extends EmptyOnClickRowChecker {

	public CommerceAccountGroupItemSelectorChecker(
		RenderResponse renderResponse, long[] checkedCommerceAccountGroupIds) {

		super(renderResponse);

		_checkedCommerceAccountGroupIds = SetUtil.fromArray(
			checkedCommerceAccountGroupIds);
	}

	@Override
	public boolean isChecked(Object object) {
		AccountGroup accountGroup = (AccountGroup)object;

		return _checkedCommerceAccountGroupIds.contains(
			accountGroup.getAccountGroupId());
	}

	@Override
	public boolean isDisabled(Object object) {
		return isChecked(object);
	}

	private final Set<Long> _checkedCommerceAccountGroupIds;

}