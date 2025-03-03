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

package com.liferay.commerce.inventory.service;

import com.liferay.portal.kernel.service.ServiceWrapper;
import com.liferay.portal.kernel.service.persistence.BasePersistence;

/**
 * Provides a wrapper for {@link CommerceInventoryWarehouseItemLocalService}.
 *
 * @author Luca Pellizzon
 * @see CommerceInventoryWarehouseItemLocalService
 * @generated
 */
public class CommerceInventoryWarehouseItemLocalServiceWrapper
	implements CommerceInventoryWarehouseItemLocalService,
			   ServiceWrapper<CommerceInventoryWarehouseItemLocalService> {

	public CommerceInventoryWarehouseItemLocalServiceWrapper() {
		this(null);
	}

	public CommerceInventoryWarehouseItemLocalServiceWrapper(
		CommerceInventoryWarehouseItemLocalService
			commerceInventoryWarehouseItemLocalService) {

		_commerceInventoryWarehouseItemLocalService =
			commerceInventoryWarehouseItemLocalService;
	}

	/**
	 * Adds the commerce inventory warehouse item to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceInventoryWarehouseItemLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceInventoryWarehouseItem the commerce inventory warehouse item
	 * @return the commerce inventory warehouse item that was added
	 */
	@Override
	public com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem
		addCommerceInventoryWarehouseItem(
			com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem
				commerceInventoryWarehouseItem) {

		return _commerceInventoryWarehouseItemLocalService.
			addCommerceInventoryWarehouseItem(commerceInventoryWarehouseItem);
	}

	@Override
	public com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem
			addCommerceInventoryWarehouseItem(
				long userId, long commerceInventoryWarehouseId, String sku,
				int quantity)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceInventoryWarehouseItemLocalService.
			addCommerceInventoryWarehouseItem(
				userId, commerceInventoryWarehouseId, sku, quantity);
	}

	@Override
	public com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem
			addCommerceInventoryWarehouseItem(
				String externalReferenceCode, long userId,
				long commerceInventoryWarehouseId, String sku, int quantity)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceInventoryWarehouseItemLocalService.
			addCommerceInventoryWarehouseItem(
				externalReferenceCode, userId, commerceInventoryWarehouseId,
				sku, quantity);
	}

	@Override
	public com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem
			addOrUpdateCommerceInventoryWarehouseItem(
				long userId, long commerceInventoryWarehouseId, String sku,
				int quantity)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceInventoryWarehouseItemLocalService.
			addOrUpdateCommerceInventoryWarehouseItem(
				userId, commerceInventoryWarehouseId, sku, quantity);
	}

	@Override
	public com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem
			addOrUpdateCommerceInventoryWarehouseItem(
				String externalReferenceCode, long companyId, long userId,
				long commerceInventoryWarehouseId, String sku, int quantity)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceInventoryWarehouseItemLocalService.
			addOrUpdateCommerceInventoryWarehouseItem(
				externalReferenceCode, companyId, userId,
				commerceInventoryWarehouseId, sku, quantity);
	}

	@Override
	public int countItemsByCompanyId(long companyId, String sku) {
		return _commerceInventoryWarehouseItemLocalService.
			countItemsByCompanyId(companyId, sku);
	}

	/**
	 * Creates a new commerce inventory warehouse item with the primary key. Does not add the commerce inventory warehouse item to the database.
	 *
	 * @param commerceInventoryWarehouseItemId the primary key for the new commerce inventory warehouse item
	 * @return the new commerce inventory warehouse item
	 */
	@Override
	public com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem
		createCommerceInventoryWarehouseItem(
			long commerceInventoryWarehouseItemId) {

		return _commerceInventoryWarehouseItemLocalService.
			createCommerceInventoryWarehouseItem(
				commerceInventoryWarehouseItemId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel createPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceInventoryWarehouseItemLocalService.createPersistedModel(
			primaryKeyObj);
	}

	/**
	 * Deletes the commerce inventory warehouse item from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceInventoryWarehouseItemLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceInventoryWarehouseItem the commerce inventory warehouse item
	 * @return the commerce inventory warehouse item that was removed
	 */
	@Override
	public com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem
		deleteCommerceInventoryWarehouseItem(
			com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem
				commerceInventoryWarehouseItem) {

		return _commerceInventoryWarehouseItemLocalService.
			deleteCommerceInventoryWarehouseItem(
				commerceInventoryWarehouseItem);
	}

	/**
	 * Deletes the commerce inventory warehouse item with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceInventoryWarehouseItemLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceInventoryWarehouseItemId the primary key of the commerce inventory warehouse item
	 * @return the commerce inventory warehouse item that was removed
	 * @throws PortalException if a commerce inventory warehouse item with the primary key could not be found
	 */
	@Override
	public com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem
			deleteCommerceInventoryWarehouseItem(
				long commerceInventoryWarehouseItemId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceInventoryWarehouseItemLocalService.
			deleteCommerceInventoryWarehouseItem(
				commerceInventoryWarehouseItemId);
	}

	@Override
	public void deleteCommerceInventoryWarehouseItems(
		long commerceInventoryWarehouseId) {

		_commerceInventoryWarehouseItemLocalService.
			deleteCommerceInventoryWarehouseItems(commerceInventoryWarehouseId);
	}

	@Override
	public void deleteCommerceInventoryWarehouseItems(
		long companyId, String sku) {

		_commerceInventoryWarehouseItemLocalService.
			deleteCommerceInventoryWarehouseItems(companyId, sku);
	}

	@Override
	public void deleteCommerceInventoryWarehouseItemsByCompanyId(
		long companyId) {

		_commerceInventoryWarehouseItemLocalService.
			deleteCommerceInventoryWarehouseItemsByCompanyId(companyId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceInventoryWarehouseItemLocalService.deletePersistedModel(
			persistedModel);
	}

	@Override
	public <T> T dslQuery(com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {
		return _commerceInventoryWarehouseItemLocalService.dslQuery(dslQuery);
	}

	@Override
	public int dslQueryCount(
		com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {

		return _commerceInventoryWarehouseItemLocalService.dslQueryCount(
			dslQuery);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _commerceInventoryWarehouseItemLocalService.dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _commerceInventoryWarehouseItemLocalService.dynamicQuery(
			dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.inventory.model.impl.CommerceInventoryWarehouseItemModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return _commerceInventoryWarehouseItemLocalService.dynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.inventory.model.impl.CommerceInventoryWarehouseItemModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return _commerceInventoryWarehouseItemLocalService.dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _commerceInventoryWarehouseItemLocalService.dynamicQueryCount(
			dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return _commerceInventoryWarehouseItemLocalService.dynamicQueryCount(
			dynamicQuery, projection);
	}

	@Override
	public com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem
		fetchCommerceInventoryWarehouseItem(
			long commerceInventoryWarehouseItemId) {

		return _commerceInventoryWarehouseItemLocalService.
			fetchCommerceInventoryWarehouseItem(
				commerceInventoryWarehouseItemId);
	}

	@Override
	public com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem
		fetchCommerceInventoryWarehouseItem(
			long commerceInventoryWarehouseId, String sku) {

		return _commerceInventoryWarehouseItemLocalService.
			fetchCommerceInventoryWarehouseItem(
				commerceInventoryWarehouseId, sku);
	}

	@Override
	public com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem
		fetchCommerceInventoryWarehouseItemByExternalReferenceCode(
			String externalReferenceCode, long companyId) {

		return _commerceInventoryWarehouseItemLocalService.
			fetchCommerceInventoryWarehouseItemByExternalReferenceCode(
				externalReferenceCode, companyId);
	}

	/**
	 * Returns the commerce inventory warehouse item with the matching UUID and company.
	 *
	 * @param uuid the commerce inventory warehouse item's UUID
	 * @param companyId the primary key of the company
	 * @return the matching commerce inventory warehouse item, or <code>null</code> if a matching commerce inventory warehouse item could not be found
	 */
	@Override
	public com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem
		fetchCommerceInventoryWarehouseItemByUuidAndCompanyId(
			String uuid, long companyId) {

		return _commerceInventoryWarehouseItemLocalService.
			fetchCommerceInventoryWarehouseItemByUuidAndCompanyId(
				uuid, companyId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _commerceInventoryWarehouseItemLocalService.
			getActionableDynamicQuery();
	}

	/**
	 * Returns the commerce inventory warehouse item with the primary key.
	 *
	 * @param commerceInventoryWarehouseItemId the primary key of the commerce inventory warehouse item
	 * @return the commerce inventory warehouse item
	 * @throws PortalException if a commerce inventory warehouse item with the primary key could not be found
	 */
	@Override
	public com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem
			getCommerceInventoryWarehouseItem(
				long commerceInventoryWarehouseItemId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceInventoryWarehouseItemLocalService.
			getCommerceInventoryWarehouseItem(commerceInventoryWarehouseItemId);
	}

	@Override
	public com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem
			getCommerceInventoryWarehouseItem(
				long commerceInventoryWarehouseId, String sku)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceInventoryWarehouseItemLocalService.
			getCommerceInventoryWarehouseItem(
				commerceInventoryWarehouseId, sku);
	}

	@Override
	public com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem
			getCommerceInventoryWarehouseItemByExternalReferenceCode(
				String externalReferenceCode, long companyId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceInventoryWarehouseItemLocalService.
			getCommerceInventoryWarehouseItemByExternalReferenceCode(
				externalReferenceCode, companyId);
	}

	@Override
	public com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem
			getCommerceInventoryWarehouseItemByReferenceCode(
				String externalReferenceCode, long companyId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceInventoryWarehouseItemLocalService.
			getCommerceInventoryWarehouseItemByReferenceCode(
				externalReferenceCode, companyId);
	}

	/**
	 * Returns the commerce inventory warehouse item with the matching UUID and company.
	 *
	 * @param uuid the commerce inventory warehouse item's UUID
	 * @param companyId the primary key of the company
	 * @return the matching commerce inventory warehouse item
	 * @throws PortalException if a matching commerce inventory warehouse item could not be found
	 */
	@Override
	public com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem
			getCommerceInventoryWarehouseItemByUuidAndCompanyId(
				String uuid, long companyId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceInventoryWarehouseItemLocalService.
			getCommerceInventoryWarehouseItemByUuidAndCompanyId(
				uuid, companyId);
	}

	/**
	 * Returns a range of all the commerce inventory warehouse items.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.inventory.model.impl.CommerceInventoryWarehouseItemModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce inventory warehouse items
	 * @param end the upper bound of the range of commerce inventory warehouse items (not inclusive)
	 * @return the range of commerce inventory warehouse items
	 */
	@Override
	public java.util.List
		<com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem>
			getCommerceInventoryWarehouseItems(int start, int end) {

		return _commerceInventoryWarehouseItemLocalService.
			getCommerceInventoryWarehouseItems(start, end);
	}

	@Override
	public java.util.List
		<com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem>
			getCommerceInventoryWarehouseItems(
				long commerceInventoryWarehouseId, int start, int end) {

		return _commerceInventoryWarehouseItemLocalService.
			getCommerceInventoryWarehouseItems(
				commerceInventoryWarehouseId, start, end);
	}

	@Override
	public java.util.List
		<com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem>
			getCommerceInventoryWarehouseItems(
				long companyId, String sku, int start, int end) {

		return _commerceInventoryWarehouseItemLocalService.
			getCommerceInventoryWarehouseItems(companyId, sku, start, end);
	}

	@Override
	public java.util.List
		<com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem>
			getCommerceInventoryWarehouseItemsByCompanyId(
				long companyId, int start, int end) {

		return _commerceInventoryWarehouseItemLocalService.
			getCommerceInventoryWarehouseItemsByCompanyId(
				companyId, start, end);
	}

	@Override
	public java.util.List
		<com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem>
			getCommerceInventoryWarehouseItemsByCompanyIdAndSku(
				long companyId, String sku, int start, int end) {

		return _commerceInventoryWarehouseItemLocalService.
			getCommerceInventoryWarehouseItemsByCompanyIdAndSku(
				companyId, sku, start, end);
	}

	@Override
	public java.util.List
		<com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem>
			getCommerceInventoryWarehouseItemsByModifiedDate(
				long companyId, java.util.Date startDate,
				java.util.Date endDate, int start, int end) {

		return _commerceInventoryWarehouseItemLocalService.
			getCommerceInventoryWarehouseItemsByModifiedDate(
				companyId, startDate, endDate, start, end);
	}

	/**
	 * Returns the number of commerce inventory warehouse items.
	 *
	 * @return the number of commerce inventory warehouse items
	 */
	@Override
	public int getCommerceInventoryWarehouseItemsCount() {
		return _commerceInventoryWarehouseItemLocalService.
			getCommerceInventoryWarehouseItemsCount();
	}

	@Override
	public int getCommerceInventoryWarehouseItemsCount(
		long commerceInventoryWarehouseId) {

		return _commerceInventoryWarehouseItemLocalService.
			getCommerceInventoryWarehouseItemsCount(
				commerceInventoryWarehouseId);
	}

	@Override
	public int getCommerceInventoryWarehouseItemsCount(
		long companyId, String sku) {

		return _commerceInventoryWarehouseItemLocalService.
			getCommerceInventoryWarehouseItemsCount(companyId, sku);
	}

	@Override
	public int getCommerceInventoryWarehouseItemsCountByCompanyId(
		long companyId) {

		return _commerceInventoryWarehouseItemLocalService.
			getCommerceInventoryWarehouseItemsCountByCompanyId(companyId);
	}

	@Override
	public int getCommerceInventoryWarehouseItemsCountByModifiedDate(
		long companyId, java.util.Date startDate, java.util.Date endDate) {

		return _commerceInventoryWarehouseItemLocalService.
			getCommerceInventoryWarehouseItemsCountByModifiedDate(
				companyId, startDate, endDate);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery
		getExportActionableDynamicQuery(
			com.liferay.exportimport.kernel.lar.PortletDataContext
				portletDataContext) {

		return _commerceInventoryWarehouseItemLocalService.
			getExportActionableDynamicQuery(portletDataContext);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _commerceInventoryWarehouseItemLocalService.
			getIndexableActionableDynamicQuery();
	}

	@Override
	public java.util.List<com.liferay.commerce.inventory.model.CIWarehouseItem>
		getItemsByCompanyId(long companyId, String sku, int start, int end) {

		return _commerceInventoryWarehouseItemLocalService.getItemsByCompanyId(
			companyId, sku, start, end);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _commerceInventoryWarehouseItemLocalService.
			getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceInventoryWarehouseItemLocalService.getPersistedModel(
			primaryKeyObj);
	}

	@Override
	public int getStockQuantity(long companyId, long groupId, String sku) {
		return _commerceInventoryWarehouseItemLocalService.getStockQuantity(
			companyId, groupId, sku);
	}

	@Override
	public int getStockQuantity(long companyId, String sku) {
		return _commerceInventoryWarehouseItemLocalService.getStockQuantity(
			companyId, sku);
	}

	@Override
	public com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem
			increaseCommerceInventoryWarehouseItemQuantity(
				long userId, long commerceInventoryWarehouseItemId,
				int quantity)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceInventoryWarehouseItemLocalService.
			increaseCommerceInventoryWarehouseItemQuantity(
				userId, commerceInventoryWarehouseItemId, quantity);
	}

	@Override
	public void moveQuantitiesBetweenWarehouses(
			long userId, long fromCommerceInventoryWarehouseId,
			long toCommerceInventoryWarehouseId, String sku, int quantity)
		throws com.liferay.portal.kernel.exception.PortalException {

		_commerceInventoryWarehouseItemLocalService.
			moveQuantitiesBetweenWarehouses(
				userId, fromCommerceInventoryWarehouseId,
				toCommerceInventoryWarehouseId, sku, quantity);
	}

	/**
	 * Updates the commerce inventory warehouse item in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceInventoryWarehouseItemLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceInventoryWarehouseItem the commerce inventory warehouse item
	 * @return the commerce inventory warehouse item that was updated
	 */
	@Override
	public com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem
		updateCommerceInventoryWarehouseItem(
			com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem
				commerceInventoryWarehouseItem) {

		return _commerceInventoryWarehouseItemLocalService.
			updateCommerceInventoryWarehouseItem(
				commerceInventoryWarehouseItem);
	}

	@Override
	public com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem
			updateCommerceInventoryWarehouseItem(
				long userId, long commerceInventoryWarehouseItemId,
				int quantity, int reservedQuantity, long mvccVersion)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceInventoryWarehouseItemLocalService.
			updateCommerceInventoryWarehouseItem(
				userId, commerceInventoryWarehouseItemId, quantity,
				reservedQuantity, mvccVersion);
	}

	@Override
	public com.liferay.commerce.inventory.model.CommerceInventoryWarehouseItem
			updateCommerceInventoryWarehouseItem(
				long userId, long commerceInventoryWarehouseItemId,
				int quantity, long mvccVersion)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceInventoryWarehouseItemLocalService.
			updateCommerceInventoryWarehouseItem(
				userId, commerceInventoryWarehouseItemId, quantity,
				mvccVersion);
	}

	@Override
	public BasePersistence<?> getBasePersistence() {
		return _commerceInventoryWarehouseItemLocalService.getBasePersistence();
	}

	@Override
	public CommerceInventoryWarehouseItemLocalService getWrappedService() {
		return _commerceInventoryWarehouseItemLocalService;
	}

	@Override
	public void setWrappedService(
		CommerceInventoryWarehouseItemLocalService
			commerceInventoryWarehouseItemLocalService) {

		_commerceInventoryWarehouseItemLocalService =
			commerceInventoryWarehouseItemLocalService;
	}

	private CommerceInventoryWarehouseItemLocalService
		_commerceInventoryWarehouseItemLocalService;

}