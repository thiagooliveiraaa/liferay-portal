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

package com.liferay.commerce.service.impl;

import com.liferay.commerce.exception.CPDefinitionInventoryMaxOrderQuantityException;
import com.liferay.commerce.exception.CPDefinitionInventoryMinOrderQuantityException;
import com.liferay.commerce.exception.CPDefinitionInventoryMultipleOrderQuantityException;
import com.liferay.commerce.model.CPDefinitionInventory;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.service.CPDefinitionLocalService;
import com.liferay.commerce.service.base.CPDefinitionInventoryLocalServiceBaseImpl;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.SystemEventConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.systemevent.SystemEvent;
import com.liferay.portal.kernel.uuid.PortalUUID;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 * @author Alec Sloan
 */
@Component(
	property = "model.class.name=com.liferay.commerce.model.CPDefinitionInventory",
	service = AopService.class
)
public class CPDefinitionInventoryLocalServiceImpl
	extends CPDefinitionInventoryLocalServiceBaseImpl {

	@Override
	public CPDefinitionInventory addCPDefinitionInventory(
			long userId, long cpDefinitionId,
			String cpDefinitionInventoryEngine, String lowStockActivity,
			boolean displayAvailability, boolean displayStockQuantity,
			int minStockQuantity, boolean backOrders, int minOrderQuantity,
			int maxOrderQuantity, String allowedOrderQuantities,
			int multipleOrderQuantity)
		throws PortalException {

		_validateOrderQuantity(
			minOrderQuantity, maxOrderQuantity, multipleOrderQuantity);

		User user = _userLocalService.getUser(userId);

		long cpDefinitionInventoryId = counterLocalService.increment();

		CPDefinitionInventory cpDefinitionInventory =
			cpDefinitionInventoryPersistence.create(cpDefinitionInventoryId);

		CPDefinition cpDefinition = _cpDefinitionLocalService.getCPDefinition(
			cpDefinitionId);

		if (_cpDefinitionLocalService.isVersionable(cpDefinitionId)) {
			cpDefinition = _cpDefinitionLocalService.copyCPDefinition(
				cpDefinitionId);
		}

		cpDefinitionInventory.setGroupId(cpDefinition.getGroupId());
		cpDefinitionInventory.setCompanyId(user.getCompanyId());
		cpDefinitionInventory.setUserId(user.getUserId());
		cpDefinitionInventory.setUserName(user.getFullName());
		cpDefinitionInventory.setCPDefinitionId(
			cpDefinition.getCPDefinitionId());
		cpDefinitionInventory.setCPDefinitionInventoryEngine(
			cpDefinitionInventoryEngine);
		cpDefinitionInventory.setLowStockActivity(lowStockActivity);
		cpDefinitionInventory.setDisplayAvailability(displayAvailability);
		cpDefinitionInventory.setDisplayStockQuantity(displayStockQuantity);
		cpDefinitionInventory.setMinStockQuantity(minStockQuantity);
		cpDefinitionInventory.setBackOrders(backOrders);
		cpDefinitionInventory.setMinOrderQuantity(minOrderQuantity);
		cpDefinitionInventory.setMaxOrderQuantity(maxOrderQuantity);
		cpDefinitionInventory.setAllowedOrderQuantities(allowedOrderQuantities);
		cpDefinitionInventory.setMultipleOrderQuantity(multipleOrderQuantity);

		return cpDefinitionInventoryPersistence.update(cpDefinitionInventory);
	}

	@Override
	public void cloneCPDefinitionInventory(
		long cpDefinitionId, long newCPDefinitionId) {

		CPDefinitionInventory cpDefinitionInventory =
			cpDefinitionInventoryLocalService.
				fetchCPDefinitionInventoryByCPDefinitionId(cpDefinitionId);

		if (cpDefinitionInventory != null) {
			CPDefinitionInventory newCPDefinitionInventory =
				(CPDefinitionInventory)cpDefinitionInventory.clone();

			newCPDefinitionInventory.setUuid(_portalUUID.generate());
			newCPDefinitionInventory.setCPDefinitionInventoryId(
				counterLocalService.increment());
			newCPDefinitionInventory.setCPDefinitionId(newCPDefinitionId);

			cpDefinitionInventoryLocalService.addCPDefinitionInventory(
				newCPDefinitionInventory);
		}
	}

	@Override
	@SystemEvent(type = SystemEventConstants.TYPE_DELETE)
	public CPDefinitionInventory deleteCPDefinitionInventory(
		CPDefinitionInventory cpDefinitionInventory) {

		if (_cpDefinitionLocalService.isVersionable(
				cpDefinitionInventory.getCPDefinitionId())) {

			try {
				CPDefinition newCPDefinition =
					_cpDefinitionLocalService.copyCPDefinition(
						cpDefinitionInventory.getCPDefinitionId());

				cpDefinitionInventory =
					cpDefinitionInventoryPersistence.findByCPDefinitionId(
						newCPDefinition.getCPDefinitionId());
			}
			catch (PortalException portalException) {
				throw new SystemException(portalException);
			}
		}

		return cpDefinitionInventoryPersistence.remove(cpDefinitionInventory);
	}

	@Override
	public CPDefinitionInventory deleteCPDefinitionInventory(
			long cpDefinitionInventoryId)
		throws PortalException {

		CPDefinitionInventory cpDefinitionInventory =
			cpDefinitionInventoryPersistence.findByPrimaryKey(
				cpDefinitionInventoryId);

		return cpDefinitionInventoryLocalService.deleteCPDefinitionInventory(
			cpDefinitionInventory);
	}

	@Override
	public void deleteCPDefinitionInventoryByCPDefinitionId(
		long cpDefinitionId) {

		CPDefinitionInventory cpDefinitionInventory =
			cpDefinitionInventoryLocalService.
				fetchCPDefinitionInventoryByCPDefinitionId(cpDefinitionId);

		if (cpDefinitionInventory != null) {
			cpDefinitionInventoryLocalService.deleteCPDefinitionInventory(
				cpDefinitionInventory);
		}
	}

	@Override
	public CPDefinitionInventory fetchCPDefinitionInventoryByCPDefinitionId(
		long cpDefinitionId) {

		return cpDefinitionInventoryPersistence.fetchByCPDefinitionId(
			cpDefinitionId);
	}

	@Override
	public CPDefinitionInventory updateCPDefinitionInventory(
			long cpDefinitionInventoryId, String cpDefinitionInventoryEngine,
			String lowStockActivity, boolean displayAvailability,
			boolean displayStockQuantity, int minStockQuantity,
			boolean backOrders, int minOrderQuantity, int maxOrderQuantity,
			String allowedOrderQuantities, int multipleOrderQuantity)
		throws PortalException {

		_validateOrderQuantity(
			minOrderQuantity, maxOrderQuantity, multipleOrderQuantity);

		CPDefinitionInventory cpDefinitionInventory =
			cpDefinitionInventoryPersistence.findByPrimaryKey(
				cpDefinitionInventoryId);

		if (_cpDefinitionLocalService.isVersionable(
				cpDefinitionInventory.getCPDefinitionId())) {

			CPDefinition newCPDefinition =
				_cpDefinitionLocalService.copyCPDefinition(
					cpDefinitionInventory.getCPDefinitionId());

			cpDefinitionInventory =
				cpDefinitionInventoryPersistence.findByCPDefinitionId(
					newCPDefinition.getCPDefinitionId());
		}

		cpDefinitionInventory.setCPDefinitionInventoryEngine(
			cpDefinitionInventoryEngine);
		cpDefinitionInventory.setLowStockActivity(lowStockActivity);
		cpDefinitionInventory.setDisplayAvailability(displayAvailability);
		cpDefinitionInventory.setDisplayStockQuantity(displayStockQuantity);
		cpDefinitionInventory.setMinStockQuantity(minStockQuantity);
		cpDefinitionInventory.setBackOrders(backOrders);
		cpDefinitionInventory.setMinOrderQuantity(minOrderQuantity);
		cpDefinitionInventory.setMaxOrderQuantity(maxOrderQuantity);
		cpDefinitionInventory.setAllowedOrderQuantities(allowedOrderQuantities);
		cpDefinitionInventory.setMultipleOrderQuantity(multipleOrderQuantity);

		return cpDefinitionInventoryPersistence.update(cpDefinitionInventory);
	}

	private void _validateOrderQuantity(
			int minOrderQuantity, int maxOrderQuantity,
			int multipleOrderQuantity)
		throws CPDefinitionInventoryMaxOrderQuantityException,
			   CPDefinitionInventoryMinOrderQuantityException,
			   CPDefinitionInventoryMultipleOrderQuantityException {

		if (minOrderQuantity < 1) {
			throw new CPDefinitionInventoryMinOrderQuantityException(
				"Minimum order quantity must be greater than or equal to 1");
		}

		if (maxOrderQuantity < 1) {
			throw new CPDefinitionInventoryMaxOrderQuantityException(
				"Maximum order quantity must be greater than or equal to 1");
		}

		if (multipleOrderQuantity < 1) {
			throw new CPDefinitionInventoryMultipleOrderQuantityException(
				"Multiple order quantity must be greater than or equal to 1");
		}
	}

	@Reference
	private CPDefinitionLocalService _cpDefinitionLocalService;

	@Reference
	private PortalUUID _portalUUID;

	@Reference
	private UserLocalService _userLocalService;

}