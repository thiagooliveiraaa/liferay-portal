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

package com.liferay.commerce.price.list.service.base;

import com.liferay.commerce.price.list.model.CommercePriceListDiscountRel;
import com.liferay.commerce.price.list.service.CommercePriceListDiscountRelLocalService;
import com.liferay.commerce.price.list.service.CommercePriceListDiscountRelLocalServiceUtil;
import com.liferay.commerce.price.list.service.persistence.CommercePriceListDiscountRelPersistence;
import com.liferay.exportimport.kernel.lar.ExportImportHelperUtil;
import com.liferay.exportimport.kernel.lar.ManifestSummary;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.petra.sql.dsl.query.DSLQuery;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.BaseLocalServiceImpl;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.service.change.tracking.CTService;
import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.kernel.service.persistence.change.tracking.CTPersistence;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * Provides the base implementation for the commerce price list discount rel local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.commerce.price.list.service.impl.CommercePriceListDiscountRelLocalServiceImpl}.
 * </p>
 *
 * @author Alessio Antonio Rendina
 * @see com.liferay.commerce.price.list.service.impl.CommercePriceListDiscountRelLocalServiceImpl
 * @generated
 */
public abstract class CommercePriceListDiscountRelLocalServiceBaseImpl
	extends BaseLocalServiceImpl
	implements AopService, CommercePriceListDiscountRelLocalService,
			   IdentifiableOSGiService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>CommercePriceListDiscountRelLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>CommercePriceListDiscountRelLocalServiceUtil</code>.
	 */

	/**
	 * Adds the commerce price list discount rel to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommercePriceListDiscountRelLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commercePriceListDiscountRel the commerce price list discount rel
	 * @return the commerce price list discount rel that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CommercePriceListDiscountRel addCommercePriceListDiscountRel(
		CommercePriceListDiscountRel commercePriceListDiscountRel) {

		commercePriceListDiscountRel.setNew(true);

		return commercePriceListDiscountRelPersistence.update(
			commercePriceListDiscountRel);
	}

	/**
	 * Creates a new commerce price list discount rel with the primary key. Does not add the commerce price list discount rel to the database.
	 *
	 * @param commercePriceListDiscountRelId the primary key for the new commerce price list discount rel
	 * @return the new commerce price list discount rel
	 */
	@Override
	@Transactional(enabled = false)
	public CommercePriceListDiscountRel createCommercePriceListDiscountRel(
		long commercePriceListDiscountRelId) {

		return commercePriceListDiscountRelPersistence.create(
			commercePriceListDiscountRelId);
	}

	/**
	 * Deletes the commerce price list discount rel with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommercePriceListDiscountRelLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commercePriceListDiscountRelId the primary key of the commerce price list discount rel
	 * @return the commerce price list discount rel that was removed
	 * @throws PortalException if a commerce price list discount rel with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public CommercePriceListDiscountRel deleteCommercePriceListDiscountRel(
			long commercePriceListDiscountRelId)
		throws PortalException {

		return commercePriceListDiscountRelPersistence.remove(
			commercePriceListDiscountRelId);
	}

	/**
	 * Deletes the commerce price list discount rel from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommercePriceListDiscountRelLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commercePriceListDiscountRel the commerce price list discount rel
	 * @return the commerce price list discount rel that was removed
	 * @throws PortalException
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public CommercePriceListDiscountRel deleteCommercePriceListDiscountRel(
			CommercePriceListDiscountRel commercePriceListDiscountRel)
		throws PortalException {

		return commercePriceListDiscountRelPersistence.remove(
			commercePriceListDiscountRel);
	}

	@Override
	public <T> T dslQuery(DSLQuery dslQuery) {
		return commercePriceListDiscountRelPersistence.dslQuery(dslQuery);
	}

	@Override
	public int dslQueryCount(DSLQuery dslQuery) {
		Long count = dslQuery(dslQuery);

		return count.intValue();
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(
			CommercePriceListDiscountRel.class, clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return commercePriceListDiscountRelPersistence.findWithDynamicQuery(
			dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.price.list.model.impl.CommercePriceListDiscountRelModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return commercePriceListDiscountRelPersistence.findWithDynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.price.list.model.impl.CommercePriceListDiscountRelModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<T> orderByComparator) {

		return commercePriceListDiscountRelPersistence.findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return commercePriceListDiscountRelPersistence.countWithDynamicQuery(
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
		DynamicQuery dynamicQuery, Projection projection) {

		return commercePriceListDiscountRelPersistence.countWithDynamicQuery(
			dynamicQuery, projection);
	}

	@Override
	public CommercePriceListDiscountRel fetchCommercePriceListDiscountRel(
		long commercePriceListDiscountRelId) {

		return commercePriceListDiscountRelPersistence.fetchByPrimaryKey(
			commercePriceListDiscountRelId);
	}

	/**
	 * Returns the commerce price list discount rel with the matching UUID and company.
	 *
	 * @param uuid the commerce price list discount rel's UUID
	 * @param companyId the primary key of the company
	 * @return the matching commerce price list discount rel, or <code>null</code> if a matching commerce price list discount rel could not be found
	 */
	@Override
	public CommercePriceListDiscountRel
		fetchCommercePriceListDiscountRelByUuidAndCompanyId(
			String uuid, long companyId) {

		return commercePriceListDiscountRelPersistence.fetchByUuid_C_First(
			uuid, companyId, null);
	}

	/**
	 * Returns the commerce price list discount rel with the primary key.
	 *
	 * @param commercePriceListDiscountRelId the primary key of the commerce price list discount rel
	 * @return the commerce price list discount rel
	 * @throws PortalException if a commerce price list discount rel with the primary key could not be found
	 */
	@Override
	public CommercePriceListDiscountRel getCommercePriceListDiscountRel(
			long commercePriceListDiscountRelId)
		throws PortalException {

		return commercePriceListDiscountRelPersistence.findByPrimaryKey(
			commercePriceListDiscountRelId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery =
			new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(
			commercePriceListDiscountRelLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(
			CommercePriceListDiscountRel.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName(
			"commercePriceListDiscountRelId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(
			commercePriceListDiscountRelLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(
			CommercePriceListDiscountRel.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName(
			"commercePriceListDiscountRelId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {

		actionableDynamicQuery.setBaseLocalService(
			commercePriceListDiscountRelLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(
			CommercePriceListDiscountRel.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName(
			"commercePriceListDiscountRelId");
	}

	@Override
	public ExportActionableDynamicQuery getExportActionableDynamicQuery(
		final PortletDataContext portletDataContext) {

		final ExportActionableDynamicQuery exportActionableDynamicQuery =
			new ExportActionableDynamicQuery() {

				@Override
				public long performCount() throws PortalException {
					ManifestSummary manifestSummary =
						portletDataContext.getManifestSummary();

					StagedModelType stagedModelType = getStagedModelType();

					long modelAdditionCount = super.performCount();

					manifestSummary.addModelAdditionCount(
						stagedModelType, modelAdditionCount);

					long modelDeletionCount =
						ExportImportHelperUtil.getModelDeletionCount(
							portletDataContext, stagedModelType);

					manifestSummary.addModelDeletionCount(
						stagedModelType, modelDeletionCount);

					return modelAdditionCount;
				}

			};

		initActionableDynamicQuery(exportActionableDynamicQuery);

		exportActionableDynamicQuery.setAddCriteriaMethod(
			new ActionableDynamicQuery.AddCriteriaMethod() {

				@Override
				public void addCriteria(DynamicQuery dynamicQuery) {
					portletDataContext.addDateRangeCriteria(
						dynamicQuery, "modifiedDate");
				}

			});

		exportActionableDynamicQuery.setCompanyId(
			portletDataContext.getCompanyId());

		exportActionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod
				<CommercePriceListDiscountRel>() {

				@Override
				public void performAction(
						CommercePriceListDiscountRel
							commercePriceListDiscountRel)
					throws PortalException {

					StagedModelDataHandlerUtil.exportStagedModel(
						portletDataContext, commercePriceListDiscountRel);
				}

			});
		exportActionableDynamicQuery.setStagedModelType(
			new StagedModelType(
				PortalUtil.getClassNameId(
					CommercePriceListDiscountRel.class.getName())));

		return exportActionableDynamicQuery;
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel createPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return commercePriceListDiscountRelPersistence.create(
			((Long)primaryKeyObj).longValue());
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {

		if (_log.isWarnEnabled()) {
			_log.warn(
				"Implement CommercePriceListDiscountRelLocalServiceImpl#deleteCommercePriceListDiscountRel(CommercePriceListDiscountRel) to avoid orphaned data");
		}

		return commercePriceListDiscountRelLocalService.
			deleteCommercePriceListDiscountRel(
				(CommercePriceListDiscountRel)persistedModel);
	}

	@Override
	public BasePersistence<CommercePriceListDiscountRel> getBasePersistence() {
		return commercePriceListDiscountRelPersistence;
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return commercePriceListDiscountRelPersistence.findByPrimaryKey(
			primaryKeyObj);
	}

	/**
	 * Returns the commerce price list discount rel with the matching UUID and company.
	 *
	 * @param uuid the commerce price list discount rel's UUID
	 * @param companyId the primary key of the company
	 * @return the matching commerce price list discount rel
	 * @throws PortalException if a matching commerce price list discount rel could not be found
	 */
	@Override
	public CommercePriceListDiscountRel
			getCommercePriceListDiscountRelByUuidAndCompanyId(
				String uuid, long companyId)
		throws PortalException {

		return commercePriceListDiscountRelPersistence.findByUuid_C_First(
			uuid, companyId, null);
	}

	/**
	 * Returns a range of all the commerce price list discount rels.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.price.list.model.impl.CommercePriceListDiscountRelModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce price list discount rels
	 * @param end the upper bound of the range of commerce price list discount rels (not inclusive)
	 * @return the range of commerce price list discount rels
	 */
	@Override
	public List<CommercePriceListDiscountRel> getCommercePriceListDiscountRels(
		int start, int end) {

		return commercePriceListDiscountRelPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of commerce price list discount rels.
	 *
	 * @return the number of commerce price list discount rels
	 */
	@Override
	public int getCommercePriceListDiscountRelsCount() {
		return commercePriceListDiscountRelPersistence.countAll();
	}

	/**
	 * Updates the commerce price list discount rel in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommercePriceListDiscountRelLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commercePriceListDiscountRel the commerce price list discount rel
	 * @return the commerce price list discount rel that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CommercePriceListDiscountRel updateCommercePriceListDiscountRel(
		CommercePriceListDiscountRel commercePriceListDiscountRel) {

		return commercePriceListDiscountRelPersistence.update(
			commercePriceListDiscountRel);
	}

	@Deactivate
	protected void deactivate() {
		CommercePriceListDiscountRelLocalServiceUtil.setService(null);
	}

	@Override
	public Class<?>[] getAopInterfaces() {
		return new Class<?>[] {
			CommercePriceListDiscountRelLocalService.class,
			IdentifiableOSGiService.class, CTService.class,
			PersistedModelLocalService.class
		};
	}

	@Override
	public void setAopProxy(Object aopProxy) {
		commercePriceListDiscountRelLocalService =
			(CommercePriceListDiscountRelLocalService)aopProxy;

		CommercePriceListDiscountRelLocalServiceUtil.setService(
			commercePriceListDiscountRelLocalService);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return CommercePriceListDiscountRelLocalService.class.getName();
	}

	@Override
	public CTPersistence<CommercePriceListDiscountRel> getCTPersistence() {
		return commercePriceListDiscountRelPersistence;
	}

	@Override
	public Class<CommercePriceListDiscountRel> getModelClass() {
		return CommercePriceListDiscountRel.class;
	}

	@Override
	public <R, E extends Throwable> R updateWithUnsafeFunction(
			UnsafeFunction<CTPersistence<CommercePriceListDiscountRel>, R, E>
				updateUnsafeFunction)
		throws E {

		return updateUnsafeFunction.apply(
			commercePriceListDiscountRelPersistence);
	}

	protected String getModelClassName() {
		return CommercePriceListDiscountRel.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource =
				commercePriceListDiscountRelPersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(
				dataSource, sql);

			sqlUpdate.update();
		}
		catch (Exception exception) {
			throw new SystemException(exception);
		}
	}

	protected CommercePriceListDiscountRelLocalService
		commercePriceListDiscountRelLocalService;

	@Reference
	protected CommercePriceListDiscountRelPersistence
		commercePriceListDiscountRelPersistence;

	@Reference
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

	private static final Log _log = LogFactoryUtil.getLog(
		CommercePriceListDiscountRelLocalServiceBaseImpl.class);

}