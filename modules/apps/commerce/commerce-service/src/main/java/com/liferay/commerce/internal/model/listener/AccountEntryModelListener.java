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

package com.liferay.commerce.internal.model.listener;

import com.liferay.account.model.AccountEntry;
import com.liferay.commerce.exception.CommerceAccountOrdersException;
import com.liferay.commerce.internal.search.CommerceOrderBatchReindexer;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.commerce.service.CommerceShippingOptionAccountEntryRelLocalService;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.util.StringUtil;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alec Sloan
 * @author Alessio Antonio Rendina
 */
@Component(service = ModelListener.class)
public class AccountEntryModelListener extends BaseModelListener<AccountEntry> {

	@Override
	public void onAfterUpdate(
			AccountEntry originalAccountEntry, AccountEntry accountEntry)
		throws ModelListenerException {

		if (StringUtil.equals(
				originalAccountEntry.getName(), accountEntry.getName())) {

			return;
		}

		commerceOrderBatchReindexer.reindex(
			accountEntry.getAccountEntryId(), accountEntry.getCompanyId());
	}

	@Override
	public void onBeforeRemove(AccountEntry accountEntry) {
		int accountOrders =
			_commerceOrderLocalService.
				getCommerceOrdersCountByCommerceAccountId(
					accountEntry.getAccountEntryId());

		if (accountOrders > 0) {
			throw new CommerceAccountOrdersException();
		}

		_commerceShippingOptionAccountEntryRelLocalService.
			deleteCommerceShippingOptionAccountEntryRelsByAccountEntryId(
				accountEntry.getAccountEntryId());
	}

	@Reference
	protected CommerceOrderBatchReindexer commerceOrderBatchReindexer;

	@Reference
	private CommerceOrderLocalService _commerceOrderLocalService;

	@Reference
	private CommerceShippingOptionAccountEntryRelLocalService
		_commerceShippingOptionAccountEntryRelLocalService;

}