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

package com.liferay.commerce.initializer.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;

import com.liferay.account.constants.AccountConstants;
import com.liferay.account.model.AccountEntry;
import com.liferay.account.service.AccountEntryLocalService;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.context.CommerceContextFactory;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CProduct;
import com.liferay.commerce.product.service.CPDefinitionLocalService;
import com.liferay.commerce.product.service.CProductLocalService;
import com.liferay.commerce.service.CommerceOrderItemLocalService;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.commerce.util.CommerceAccountHelper;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserIdMapper;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserIdMapperLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.io.File;

import java.util.Date;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Ferrari
 */
@Component(service = CommerceOrderImporter.class)
public class CommerceOrderImporter {

	public void importOrders(
			File commerceOrdersFile, long scopeGroupId, long userId)
		throws Exception {

		MappingJsonFactory mappingJsonFactory = new MappingJsonFactory();

		JsonParser jsonFactoryParser = mappingJsonFactory.createParser(
			commerceOrdersFile);

		JsonToken jsonToken = jsonFactoryParser.nextToken();

		if (jsonToken != JsonToken.START_ARRAY) {
			throw new Exception("JSON Array Expected");
		}

		ServiceContext serviceContext = getServiceContext(scopeGroupId, userId);

		while (jsonFactoryParser.nextToken() != JsonToken.END_ARRAY) {
			TreeNode treeNode = jsonFactoryParser.readValueAsTree();

			JSONObject jsonObject = _jsonFactory.createJSONObject(
				treeNode.toString());

			if (_log.isDebugEnabled()) {
				_log.debug(jsonObject);
			}

			_importCommerceOrder(jsonObject, serviceContext);
		}

		jsonFactoryParser.close();
	}

	protected ServiceContext getServiceContext(long scopeGroupId, long userId)
		throws PortalException {

		User user = _userLocalService.getUser(userId);

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setCompanyId(user.getCompanyId());
		serviceContext.setScopeGroupId(scopeGroupId);
		serviceContext.setUserId(userId);

		return serviceContext;
	}

	private void _importCommerceOrder(
			JSONObject jsonObject, ServiceContext serviceContext)
		throws Exception {

		String externalUserId = jsonObject.getString("externalUserId");

		String externalSystemType = jsonObject.getString("externalSystemType");

		// Retrieve Liferay User ID

		UserIdMapper userIdMapper = null;

		try {
			userIdMapper =
				_userIdMapperLocalService.getUserIdMapperByExternalUserId(
					externalSystemType, externalUserId);
		}
		catch (Exception exception) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Can not find an user Id mapping for: " + externalUserId,
					exception);
			}
		}

		if (userIdMapper == null) {
			return;
		}

		// Retrieve CPDefinition and associated instances

		String externalProductId = jsonObject.getString("externalProductId");

		CProduct cProduct =
			_cProductLocalService.fetchCProductByExternalReferenceCode(
				externalProductId, serviceContext.getCompanyId());

		if (cProduct == null) {
			return;
		}

		CPDefinition cpDefinition = _cpDefinitionLocalService.fetchCPDefinition(
			cProduct.getPublishedCPDefinitionId());

		if (cpDefinition == null) {
			return;
		}

		List<CPInstance> cpInstances = cpDefinition.getCPInstances();

		if (cpInstances.isEmpty()) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"No CPInstances for CPDefinition: " +
						String.valueOf(cpDefinition.getCPDefinitionId()));
			}

			return;
		}

		// Create Order

		long userId = userIdMapper.getUserId();

		AccountEntry accountEntry =
			_accountEntryLocalService.fetchPersonAccountEntry(userId);

		if (accountEntry == null) {
			User user = _userLocalService.getUser(userId);

			accountEntry = _accountEntryLocalService.addAccountEntry(
				userId, AccountConstants.PARENT_ACCOUNT_ENTRY_ID_DEFAULT,
				user.getFullName(), null, null, user.getEmailAddress(), null,
				StringPool.BLANK, AccountConstants.ACCOUNT_ENTRY_TYPE_PERSON,
				WorkflowConstants.STATUS_APPROVED, serviceContext);

			_commerceAccountHelper.addAccountEntryUserRel(
				accountEntry.getAccountEntryId(), user.getUserId(),
				serviceContext);
		}

		CommerceOrder commerceOrder =
			_commerceOrderLocalService.addCommerceOrder(
				userId, serviceContext.getScopeGroupId(),
				accountEntry.getAccountEntryId(), 0, 0);

		// We update the order create date to the one in the dataset

		long timestamp = GetterUtil.getLong(jsonObject.getString("timestamp"));

		Date createDate = new Date(timestamp * 1000);

		commerceOrder.setCreateDate(createDate);

		_commerceOrderLocalService.updateCommerceOrder(commerceOrder);

		// Create CommerceContext

		CommerceContext commerceContext = _commerceContextFactory.create(
			serviceContext.getCompanyId(), commerceOrder.getGroupId(),
			serviceContext.getUserId(), commerceOrder.getCommerceOrderId(),
			accountEntry.getAccountEntryId());

		// Create CommerceOrderItem

		CPInstance cpInstance = cpInstances.get(0);

		_commerceOrderItemLocalService.addCommerceOrderItem(
			userId, commerceOrder.getCommerceOrderId(),
			cpInstance.getCPInstanceId(), StringPool.BLANK, 1, 0, 1,
			commerceContext, serviceContext);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceOrderImporter.class);

	@Reference
	private AccountEntryLocalService _accountEntryLocalService;

	@Reference
	private CommerceAccountHelper _commerceAccountHelper;

	@Reference
	private CommerceContextFactory _commerceContextFactory;

	@Reference
	private CommerceOrderItemLocalService _commerceOrderItemLocalService;

	@Reference
	private CommerceOrderLocalService _commerceOrderLocalService;

	@Reference
	private CPDefinitionLocalService _cpDefinitionLocalService;

	@Reference
	private CProductLocalService _cProductLocalService;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private UserIdMapperLocalService _userIdMapperLocalService;

	@Reference
	private UserLocalService _userLocalService;

}