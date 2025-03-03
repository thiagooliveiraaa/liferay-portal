/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.search.experiences.service.persistence.impl;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.SessionFactory;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.sanitizer.Sanitizer;
import com.liferay.portal.kernel.sanitizer.SanitizerException;
import com.liferay.portal.kernel.sanitizer.SanitizerUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.security.permission.InlineSQLHelperUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUID;
import com.liferay.search.experiences.exception.DuplicateSXPBlueprintExternalReferenceCodeException;
import com.liferay.search.experiences.exception.NoSuchSXPBlueprintException;
import com.liferay.search.experiences.model.SXPBlueprint;
import com.liferay.search.experiences.model.SXPBlueprintTable;
import com.liferay.search.experiences.model.impl.SXPBlueprintImpl;
import com.liferay.search.experiences.model.impl.SXPBlueprintModelImpl;
import com.liferay.search.experiences.service.persistence.SXPBlueprintPersistence;
import com.liferay.search.experiences.service.persistence.SXPBlueprintUtil;
import com.liferay.search.experiences.service.persistence.impl.constants.SXPPersistenceConstants;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * The persistence implementation for the sxp blueprint service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(service = SXPBlueprintPersistence.class)
public class SXPBlueprintPersistenceImpl
	extends BasePersistenceImpl<SXPBlueprint>
	implements SXPBlueprintPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>SXPBlueprintUtil</code> to access the sxp blueprint persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		SXPBlueprintImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathWithPaginationFindByUuid;
	private FinderPath _finderPathWithoutPaginationFindByUuid;
	private FinderPath _finderPathCountByUuid;

	/**
	 * Returns all the sxp blueprints where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching sxp blueprints
	 */
	@Override
	public List<SXPBlueprint> findByUuid(String uuid) {
		return findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the sxp blueprints where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SXPBlueprintModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of sxp blueprints
	 * @param end the upper bound of the range of sxp blueprints (not inclusive)
	 * @return the range of matching sxp blueprints
	 */
	@Override
	public List<SXPBlueprint> findByUuid(String uuid, int start, int end) {
		return findByUuid(uuid, start, end, null);
	}

	/**
	 * Returns an ordered range of all the sxp blueprints where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SXPBlueprintModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of sxp blueprints
	 * @param end the upper bound of the range of sxp blueprints (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching sxp blueprints
	 */
	@Override
	public List<SXPBlueprint> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<SXPBlueprint> orderByComparator) {

		return findByUuid(uuid, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the sxp blueprints where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SXPBlueprintModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of sxp blueprints
	 * @param end the upper bound of the range of sxp blueprints (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching sxp blueprints
	 */
	@Override
	public List<SXPBlueprint> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<SXPBlueprint> orderByComparator,
		boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByUuid;
				finderArgs = new Object[] {uuid};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByUuid;
			finderArgs = new Object[] {uuid, start, end, orderByComparator};
		}

		List<SXPBlueprint> list = null;

		if (useFinderCache) {
			list = (List<SXPBlueprint>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (SXPBlueprint sxpBlueprint : list) {
					if (!uuid.equals(sxpBlueprint.getUuid())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_SXPBLUEPRINT_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_UUID_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(SXPBlueprintModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				list = (List<SXPBlueprint>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first sxp blueprint in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching sxp blueprint
	 * @throws NoSuchSXPBlueprintException if a matching sxp blueprint could not be found
	 */
	@Override
	public SXPBlueprint findByUuid_First(
			String uuid, OrderByComparator<SXPBlueprint> orderByComparator)
		throws NoSuchSXPBlueprintException {

		SXPBlueprint sxpBlueprint = fetchByUuid_First(uuid, orderByComparator);

		if (sxpBlueprint != null) {
			return sxpBlueprint;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append("}");

		throw new NoSuchSXPBlueprintException(sb.toString());
	}

	/**
	 * Returns the first sxp blueprint in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching sxp blueprint, or <code>null</code> if a matching sxp blueprint could not be found
	 */
	@Override
	public SXPBlueprint fetchByUuid_First(
		String uuid, OrderByComparator<SXPBlueprint> orderByComparator) {

		List<SXPBlueprint> list = findByUuid(uuid, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last sxp blueprint in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching sxp blueprint
	 * @throws NoSuchSXPBlueprintException if a matching sxp blueprint could not be found
	 */
	@Override
	public SXPBlueprint findByUuid_Last(
			String uuid, OrderByComparator<SXPBlueprint> orderByComparator)
		throws NoSuchSXPBlueprintException {

		SXPBlueprint sxpBlueprint = fetchByUuid_Last(uuid, orderByComparator);

		if (sxpBlueprint != null) {
			return sxpBlueprint;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append("}");

		throw new NoSuchSXPBlueprintException(sb.toString());
	}

	/**
	 * Returns the last sxp blueprint in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching sxp blueprint, or <code>null</code> if a matching sxp blueprint could not be found
	 */
	@Override
	public SXPBlueprint fetchByUuid_Last(
		String uuid, OrderByComparator<SXPBlueprint> orderByComparator) {

		int count = countByUuid(uuid);

		if (count == 0) {
			return null;
		}

		List<SXPBlueprint> list = findByUuid(
			uuid, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the sxp blueprints before and after the current sxp blueprint in the ordered set where uuid = &#63;.
	 *
	 * @param sxpBlueprintId the primary key of the current sxp blueprint
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next sxp blueprint
	 * @throws NoSuchSXPBlueprintException if a sxp blueprint with the primary key could not be found
	 */
	@Override
	public SXPBlueprint[] findByUuid_PrevAndNext(
			long sxpBlueprintId, String uuid,
			OrderByComparator<SXPBlueprint> orderByComparator)
		throws NoSuchSXPBlueprintException {

		uuid = Objects.toString(uuid, "");

		SXPBlueprint sxpBlueprint = findByPrimaryKey(sxpBlueprintId);

		Session session = null;

		try {
			session = openSession();

			SXPBlueprint[] array = new SXPBlueprintImpl[3];

			array[0] = getByUuid_PrevAndNext(
				session, sxpBlueprint, uuid, orderByComparator, true);

			array[1] = sxpBlueprint;

			array[2] = getByUuid_PrevAndNext(
				session, sxpBlueprint, uuid, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected SXPBlueprint getByUuid_PrevAndNext(
		Session session, SXPBlueprint sxpBlueprint, String uuid,
		OrderByComparator<SXPBlueprint> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_SXPBLUEPRINT_WHERE);

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_UUID_3);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_UUID_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(SXPBlueprintModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindUuid) {
			queryPos.add(uuid);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(sxpBlueprint)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<SXPBlueprint> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the sxp blueprints that the user has permission to view where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching sxp blueprints that the user has permission to view
	 */
	@Override
	public List<SXPBlueprint> filterFindByUuid(String uuid) {
		return filterFindByUuid(
			uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the sxp blueprints that the user has permission to view where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SXPBlueprintModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of sxp blueprints
	 * @param end the upper bound of the range of sxp blueprints (not inclusive)
	 * @return the range of matching sxp blueprints that the user has permission to view
	 */
	@Override
	public List<SXPBlueprint> filterFindByUuid(
		String uuid, int start, int end) {

		return filterFindByUuid(uuid, start, end, null);
	}

	/**
	 * Returns an ordered range of all the sxp blueprints that the user has permissions to view where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SXPBlueprintModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of sxp blueprints
	 * @param end the upper bound of the range of sxp blueprints (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching sxp blueprints that the user has permission to view
	 */
	@Override
	public List<SXPBlueprint> filterFindByUuid(
		String uuid, int start, int end,
		OrderByComparator<SXPBlueprint> orderByComparator) {

		if (!InlineSQLHelperUtil.isEnabled()) {
			return findByUuid(uuid, start, end, orderByComparator);
		}

		uuid = Objects.toString(uuid, "");

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				3 + (orderByComparator.getOrderByFields().length * 2));
		}
		else {
			sb = new StringBundler(4);
		}

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_SXPBLUEPRINT_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_SXPBLUEPRINT_NO_INLINE_DISTINCT_WHERE_1);
		}

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_UUID_3_SQL);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_UUID_2_SQL);
		}

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_SXPBLUEPRINT_NO_INLINE_DISTINCT_WHERE_2);
		}

		if (orderByComparator != null) {
			if (getDB().isSupportsInlineDistinct()) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator, true);
			}
			else {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_TABLE, orderByComparator, true);
			}
		}
		else {
			if (getDB().isSupportsInlineDistinct()) {
				sb.append(SXPBlueprintModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(SXPBlueprintModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), SXPBlueprint.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			if (getDB().isSupportsInlineDistinct()) {
				sqlQuery.addEntity(
					_FILTER_ENTITY_ALIAS, SXPBlueprintImpl.class);
			}
			else {
				sqlQuery.addEntity(
					_FILTER_ENTITY_TABLE, SXPBlueprintImpl.class);
			}

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			if (bindUuid) {
				queryPos.add(uuid);
			}

			return (List<SXPBlueprint>)QueryUtil.list(
				sqlQuery, getDialect(), start, end);
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	/**
	 * Returns the sxp blueprints before and after the current sxp blueprint in the ordered set of sxp blueprints that the user has permission to view where uuid = &#63;.
	 *
	 * @param sxpBlueprintId the primary key of the current sxp blueprint
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next sxp blueprint
	 * @throws NoSuchSXPBlueprintException if a sxp blueprint with the primary key could not be found
	 */
	@Override
	public SXPBlueprint[] filterFindByUuid_PrevAndNext(
			long sxpBlueprintId, String uuid,
			OrderByComparator<SXPBlueprint> orderByComparator)
		throws NoSuchSXPBlueprintException {

		if (!InlineSQLHelperUtil.isEnabled()) {
			return findByUuid_PrevAndNext(
				sxpBlueprintId, uuid, orderByComparator);
		}

		uuid = Objects.toString(uuid, "");

		SXPBlueprint sxpBlueprint = findByPrimaryKey(sxpBlueprintId);

		Session session = null;

		try {
			session = openSession();

			SXPBlueprint[] array = new SXPBlueprintImpl[3];

			array[0] = filterGetByUuid_PrevAndNext(
				session, sxpBlueprint, uuid, orderByComparator, true);

			array[1] = sxpBlueprint;

			array[2] = filterGetByUuid_PrevAndNext(
				session, sxpBlueprint, uuid, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected SXPBlueprint filterGetByUuid_PrevAndNext(
		Session session, SXPBlueprint sxpBlueprint, String uuid,
		OrderByComparator<SXPBlueprint> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_SXPBLUEPRINT_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_SXPBLUEPRINT_NO_INLINE_DISTINCT_WHERE_1);
		}

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_UUID_3_SQL);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_UUID_2_SQL);
		}

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_SXPBLUEPRINT_NO_INLINE_DISTINCT_WHERE_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				if (getDB().isSupportsInlineDistinct()) {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_ALIAS, orderByConditionFields[i],
							true));
				}
				else {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_TABLE, orderByConditionFields[i],
							true));
				}

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				if (getDB().isSupportsInlineDistinct()) {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_ALIAS, orderByFields[i], true));
				}
				else {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_TABLE, orderByFields[i], true));
				}

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			if (getDB().isSupportsInlineDistinct()) {
				sb.append(SXPBlueprintModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(SXPBlueprintModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), SXPBlueprint.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN);

		SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

		sqlQuery.setFirstResult(0);
		sqlQuery.setMaxResults(2);

		if (getDB().isSupportsInlineDistinct()) {
			sqlQuery.addEntity(_FILTER_ENTITY_ALIAS, SXPBlueprintImpl.class);
		}
		else {
			sqlQuery.addEntity(_FILTER_ENTITY_TABLE, SXPBlueprintImpl.class);
		}

		QueryPos queryPos = QueryPos.getInstance(sqlQuery);

		if (bindUuid) {
			queryPos.add(uuid);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(sxpBlueprint)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<SXPBlueprint> list = sqlQuery.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the sxp blueprints where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	@Override
	public void removeByUuid(String uuid) {
		for (SXPBlueprint sxpBlueprint :
				findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(sxpBlueprint);
		}
	}

	/**
	 * Returns the number of sxp blueprints where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching sxp blueprints
	 */
	@Override
	public int countByUuid(String uuid) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUuid;

		Object[] finderArgs = new Object[] {uuid};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_SXPBLUEPRINT_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_UUID_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of sxp blueprints that the user has permission to view where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching sxp blueprints that the user has permission to view
	 */
	@Override
	public int filterCountByUuid(String uuid) {
		if (!InlineSQLHelperUtil.isEnabled()) {
			return countByUuid(uuid);
		}

		uuid = Objects.toString(uuid, "");

		StringBundler sb = new StringBundler(2);

		sb.append(_FILTER_SQL_COUNT_SXPBLUEPRINT_WHERE);

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_UUID_3_SQL);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_UUID_2_SQL);
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), SXPBlueprint.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			sqlQuery.addScalar(
				COUNT_COLUMN_NAME, com.liferay.portal.kernel.dao.orm.Type.LONG);

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			if (bindUuid) {
				queryPos.add(uuid);
			}

			Long count = (Long)sqlQuery.uniqueResult();

			return count.intValue();
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	private static final String _FINDER_COLUMN_UUID_UUID_2 =
		"sxpBlueprint.uuid = ?";

	private static final String _FINDER_COLUMN_UUID_UUID_3 =
		"(sxpBlueprint.uuid IS NULL OR sxpBlueprint.uuid = '')";

	private static final String _FINDER_COLUMN_UUID_UUID_2_SQL =
		"sxpBlueprint.uuid_ = ?";

	private static final String _FINDER_COLUMN_UUID_UUID_3_SQL =
		"(sxpBlueprint.uuid_ IS NULL OR sxpBlueprint.uuid_ = '')";

	private FinderPath _finderPathWithPaginationFindByUuid_C;
	private FinderPath _finderPathWithoutPaginationFindByUuid_C;
	private FinderPath _finderPathCountByUuid_C;

	/**
	 * Returns all the sxp blueprints where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching sxp blueprints
	 */
	@Override
	public List<SXPBlueprint> findByUuid_C(String uuid, long companyId) {
		return findByUuid_C(
			uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the sxp blueprints where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SXPBlueprintModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of sxp blueprints
	 * @param end the upper bound of the range of sxp blueprints (not inclusive)
	 * @return the range of matching sxp blueprints
	 */
	@Override
	public List<SXPBlueprint> findByUuid_C(
		String uuid, long companyId, int start, int end) {

		return findByUuid_C(uuid, companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the sxp blueprints where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SXPBlueprintModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of sxp blueprints
	 * @param end the upper bound of the range of sxp blueprints (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching sxp blueprints
	 */
	@Override
	public List<SXPBlueprint> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<SXPBlueprint> orderByComparator) {

		return findByUuid_C(
			uuid, companyId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the sxp blueprints where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SXPBlueprintModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of sxp blueprints
	 * @param end the upper bound of the range of sxp blueprints (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching sxp blueprints
	 */
	@Override
	public List<SXPBlueprint> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<SXPBlueprint> orderByComparator,
		boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByUuid_C;
				finderArgs = new Object[] {uuid, companyId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByUuid_C;
			finderArgs = new Object[] {
				uuid, companyId, start, end, orderByComparator
			};
		}

		List<SXPBlueprint> list = null;

		if (useFinderCache) {
			list = (List<SXPBlueprint>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (SXPBlueprint sxpBlueprint : list) {
					if (!uuid.equals(sxpBlueprint.getUuid()) ||
						(companyId != sxpBlueprint.getCompanyId())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					4 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(4);
			}

			sb.append(_SQL_SELECT_SXPBLUEPRINT_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_C_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_C_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(SXPBlueprintModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(companyId);

				list = (List<SXPBlueprint>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first sxp blueprint in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching sxp blueprint
	 * @throws NoSuchSXPBlueprintException if a matching sxp blueprint could not be found
	 */
	@Override
	public SXPBlueprint findByUuid_C_First(
			String uuid, long companyId,
			OrderByComparator<SXPBlueprint> orderByComparator)
		throws NoSuchSXPBlueprintException {

		SXPBlueprint sxpBlueprint = fetchByUuid_C_First(
			uuid, companyId, orderByComparator);

		if (sxpBlueprint != null) {
			return sxpBlueprint;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchSXPBlueprintException(sb.toString());
	}

	/**
	 * Returns the first sxp blueprint in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching sxp blueprint, or <code>null</code> if a matching sxp blueprint could not be found
	 */
	@Override
	public SXPBlueprint fetchByUuid_C_First(
		String uuid, long companyId,
		OrderByComparator<SXPBlueprint> orderByComparator) {

		List<SXPBlueprint> list = findByUuid_C(
			uuid, companyId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last sxp blueprint in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching sxp blueprint
	 * @throws NoSuchSXPBlueprintException if a matching sxp blueprint could not be found
	 */
	@Override
	public SXPBlueprint findByUuid_C_Last(
			String uuid, long companyId,
			OrderByComparator<SXPBlueprint> orderByComparator)
		throws NoSuchSXPBlueprintException {

		SXPBlueprint sxpBlueprint = fetchByUuid_C_Last(
			uuid, companyId, orderByComparator);

		if (sxpBlueprint != null) {
			return sxpBlueprint;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchSXPBlueprintException(sb.toString());
	}

	/**
	 * Returns the last sxp blueprint in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching sxp blueprint, or <code>null</code> if a matching sxp blueprint could not be found
	 */
	@Override
	public SXPBlueprint fetchByUuid_C_Last(
		String uuid, long companyId,
		OrderByComparator<SXPBlueprint> orderByComparator) {

		int count = countByUuid_C(uuid, companyId);

		if (count == 0) {
			return null;
		}

		List<SXPBlueprint> list = findByUuid_C(
			uuid, companyId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the sxp blueprints before and after the current sxp blueprint in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param sxpBlueprintId the primary key of the current sxp blueprint
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next sxp blueprint
	 * @throws NoSuchSXPBlueprintException if a sxp blueprint with the primary key could not be found
	 */
	@Override
	public SXPBlueprint[] findByUuid_C_PrevAndNext(
			long sxpBlueprintId, String uuid, long companyId,
			OrderByComparator<SXPBlueprint> orderByComparator)
		throws NoSuchSXPBlueprintException {

		uuid = Objects.toString(uuid, "");

		SXPBlueprint sxpBlueprint = findByPrimaryKey(sxpBlueprintId);

		Session session = null;

		try {
			session = openSession();

			SXPBlueprint[] array = new SXPBlueprintImpl[3];

			array[0] = getByUuid_C_PrevAndNext(
				session, sxpBlueprint, uuid, companyId, orderByComparator,
				true);

			array[1] = sxpBlueprint;

			array[2] = getByUuid_C_PrevAndNext(
				session, sxpBlueprint, uuid, companyId, orderByComparator,
				false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected SXPBlueprint getByUuid_C_PrevAndNext(
		Session session, SXPBlueprint sxpBlueprint, String uuid, long companyId,
		OrderByComparator<SXPBlueprint> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_SXPBLUEPRINT_WHERE);

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_C_UUID_3);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_C_UUID_2);
		}

		sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(SXPBlueprintModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindUuid) {
			queryPos.add(uuid);
		}

		queryPos.add(companyId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(sxpBlueprint)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<SXPBlueprint> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the sxp blueprints that the user has permission to view where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching sxp blueprints that the user has permission to view
	 */
	@Override
	public List<SXPBlueprint> filterFindByUuid_C(String uuid, long companyId) {
		return filterFindByUuid_C(
			uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the sxp blueprints that the user has permission to view where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SXPBlueprintModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of sxp blueprints
	 * @param end the upper bound of the range of sxp blueprints (not inclusive)
	 * @return the range of matching sxp blueprints that the user has permission to view
	 */
	@Override
	public List<SXPBlueprint> filterFindByUuid_C(
		String uuid, long companyId, int start, int end) {

		return filterFindByUuid_C(uuid, companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the sxp blueprints that the user has permissions to view where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SXPBlueprintModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of sxp blueprints
	 * @param end the upper bound of the range of sxp blueprints (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching sxp blueprints that the user has permission to view
	 */
	@Override
	public List<SXPBlueprint> filterFindByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<SXPBlueprint> orderByComparator) {

		if (!InlineSQLHelperUtil.isEnabled(companyId, 0)) {
			return findByUuid_C(uuid, companyId, start, end, orderByComparator);
		}

		uuid = Objects.toString(uuid, "");

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByFields().length * 2));
		}
		else {
			sb = new StringBundler(5);
		}

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_SXPBLUEPRINT_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_SXPBLUEPRINT_NO_INLINE_DISTINCT_WHERE_1);
		}

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_C_UUID_3_SQL);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_C_UUID_2_SQL);
		}

		sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_SXPBLUEPRINT_NO_INLINE_DISTINCT_WHERE_2);
		}

		if (orderByComparator != null) {
			if (getDB().isSupportsInlineDistinct()) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator, true);
			}
			else {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_TABLE, orderByComparator, true);
			}
		}
		else {
			if (getDB().isSupportsInlineDistinct()) {
				sb.append(SXPBlueprintModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(SXPBlueprintModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), SXPBlueprint.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			if (getDB().isSupportsInlineDistinct()) {
				sqlQuery.addEntity(
					_FILTER_ENTITY_ALIAS, SXPBlueprintImpl.class);
			}
			else {
				sqlQuery.addEntity(
					_FILTER_ENTITY_TABLE, SXPBlueprintImpl.class);
			}

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			if (bindUuid) {
				queryPos.add(uuid);
			}

			queryPos.add(companyId);

			return (List<SXPBlueprint>)QueryUtil.list(
				sqlQuery, getDialect(), start, end);
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	/**
	 * Returns the sxp blueprints before and after the current sxp blueprint in the ordered set of sxp blueprints that the user has permission to view where uuid = &#63; and companyId = &#63;.
	 *
	 * @param sxpBlueprintId the primary key of the current sxp blueprint
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next sxp blueprint
	 * @throws NoSuchSXPBlueprintException if a sxp blueprint with the primary key could not be found
	 */
	@Override
	public SXPBlueprint[] filterFindByUuid_C_PrevAndNext(
			long sxpBlueprintId, String uuid, long companyId,
			OrderByComparator<SXPBlueprint> orderByComparator)
		throws NoSuchSXPBlueprintException {

		if (!InlineSQLHelperUtil.isEnabled(companyId, 0)) {
			return findByUuid_C_PrevAndNext(
				sxpBlueprintId, uuid, companyId, orderByComparator);
		}

		uuid = Objects.toString(uuid, "");

		SXPBlueprint sxpBlueprint = findByPrimaryKey(sxpBlueprintId);

		Session session = null;

		try {
			session = openSession();

			SXPBlueprint[] array = new SXPBlueprintImpl[3];

			array[0] = filterGetByUuid_C_PrevAndNext(
				session, sxpBlueprint, uuid, companyId, orderByComparator,
				true);

			array[1] = sxpBlueprint;

			array[2] = filterGetByUuid_C_PrevAndNext(
				session, sxpBlueprint, uuid, companyId, orderByComparator,
				false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected SXPBlueprint filterGetByUuid_C_PrevAndNext(
		Session session, SXPBlueprint sxpBlueprint, String uuid, long companyId,
		OrderByComparator<SXPBlueprint> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				6 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(5);
		}

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_SXPBLUEPRINT_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_SXPBLUEPRINT_NO_INLINE_DISTINCT_WHERE_1);
		}

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_C_UUID_3_SQL);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_C_UUID_2_SQL);
		}

		sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_SXPBLUEPRINT_NO_INLINE_DISTINCT_WHERE_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				if (getDB().isSupportsInlineDistinct()) {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_ALIAS, orderByConditionFields[i],
							true));
				}
				else {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_TABLE, orderByConditionFields[i],
							true));
				}

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				if (getDB().isSupportsInlineDistinct()) {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_ALIAS, orderByFields[i], true));
				}
				else {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_TABLE, orderByFields[i], true));
				}

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			if (getDB().isSupportsInlineDistinct()) {
				sb.append(SXPBlueprintModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(SXPBlueprintModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), SXPBlueprint.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN);

		SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

		sqlQuery.setFirstResult(0);
		sqlQuery.setMaxResults(2);

		if (getDB().isSupportsInlineDistinct()) {
			sqlQuery.addEntity(_FILTER_ENTITY_ALIAS, SXPBlueprintImpl.class);
		}
		else {
			sqlQuery.addEntity(_FILTER_ENTITY_TABLE, SXPBlueprintImpl.class);
		}

		QueryPos queryPos = QueryPos.getInstance(sqlQuery);

		if (bindUuid) {
			queryPos.add(uuid);
		}

		queryPos.add(companyId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(sxpBlueprint)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<SXPBlueprint> list = sqlQuery.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the sxp blueprints where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	@Override
	public void removeByUuid_C(String uuid, long companyId) {
		for (SXPBlueprint sxpBlueprint :
				findByUuid_C(
					uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(sxpBlueprint);
		}
	}

	/**
	 * Returns the number of sxp blueprints where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching sxp blueprints
	 */
	@Override
	public int countByUuid_C(String uuid, long companyId) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUuid_C;

		Object[] finderArgs = new Object[] {uuid, companyId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_SXPBLUEPRINT_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_C_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_C_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(companyId);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of sxp blueprints that the user has permission to view where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching sxp blueprints that the user has permission to view
	 */
	@Override
	public int filterCountByUuid_C(String uuid, long companyId) {
		if (!InlineSQLHelperUtil.isEnabled(companyId, 0)) {
			return countByUuid_C(uuid, companyId);
		}

		uuid = Objects.toString(uuid, "");

		StringBundler sb = new StringBundler(3);

		sb.append(_FILTER_SQL_COUNT_SXPBLUEPRINT_WHERE);

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_C_UUID_3_SQL);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_C_UUID_2_SQL);
		}

		sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), SXPBlueprint.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			sqlQuery.addScalar(
				COUNT_COLUMN_NAME, com.liferay.portal.kernel.dao.orm.Type.LONG);

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			if (bindUuid) {
				queryPos.add(uuid);
			}

			queryPos.add(companyId);

			Long count = (Long)sqlQuery.uniqueResult();

			return count.intValue();
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	private static final String _FINDER_COLUMN_UUID_C_UUID_2 =
		"sxpBlueprint.uuid = ? AND ";

	private static final String _FINDER_COLUMN_UUID_C_UUID_3 =
		"(sxpBlueprint.uuid IS NULL OR sxpBlueprint.uuid = '') AND ";

	private static final String _FINDER_COLUMN_UUID_C_UUID_2_SQL =
		"sxpBlueprint.uuid_ = ? AND ";

	private static final String _FINDER_COLUMN_UUID_C_UUID_3_SQL =
		"(sxpBlueprint.uuid_ IS NULL OR sxpBlueprint.uuid_ = '') AND ";

	private static final String _FINDER_COLUMN_UUID_C_COMPANYID_2 =
		"sxpBlueprint.companyId = ?";

	private FinderPath _finderPathWithPaginationFindByCompanyId;
	private FinderPath _finderPathWithoutPaginationFindByCompanyId;
	private FinderPath _finderPathCountByCompanyId;

	/**
	 * Returns all the sxp blueprints where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the matching sxp blueprints
	 */
	@Override
	public List<SXPBlueprint> findByCompanyId(long companyId) {
		return findByCompanyId(
			companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the sxp blueprints where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SXPBlueprintModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of sxp blueprints
	 * @param end the upper bound of the range of sxp blueprints (not inclusive)
	 * @return the range of matching sxp blueprints
	 */
	@Override
	public List<SXPBlueprint> findByCompanyId(
		long companyId, int start, int end) {

		return findByCompanyId(companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the sxp blueprints where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SXPBlueprintModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of sxp blueprints
	 * @param end the upper bound of the range of sxp blueprints (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching sxp blueprints
	 */
	@Override
	public List<SXPBlueprint> findByCompanyId(
		long companyId, int start, int end,
		OrderByComparator<SXPBlueprint> orderByComparator) {

		return findByCompanyId(companyId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the sxp blueprints where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SXPBlueprintModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of sxp blueprints
	 * @param end the upper bound of the range of sxp blueprints (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching sxp blueprints
	 */
	@Override
	public List<SXPBlueprint> findByCompanyId(
		long companyId, int start, int end,
		OrderByComparator<SXPBlueprint> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByCompanyId;
				finderArgs = new Object[] {companyId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByCompanyId;
			finderArgs = new Object[] {
				companyId, start, end, orderByComparator
			};
		}

		List<SXPBlueprint> list = null;

		if (useFinderCache) {
			list = (List<SXPBlueprint>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (SXPBlueprint sxpBlueprint : list) {
					if (companyId != sxpBlueprint.getCompanyId()) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_SXPBLUEPRINT_WHERE);

			sb.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(SXPBlueprintModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				list = (List<SXPBlueprint>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first sxp blueprint in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching sxp blueprint
	 * @throws NoSuchSXPBlueprintException if a matching sxp blueprint could not be found
	 */
	@Override
	public SXPBlueprint findByCompanyId_First(
			long companyId, OrderByComparator<SXPBlueprint> orderByComparator)
		throws NoSuchSXPBlueprintException {

		SXPBlueprint sxpBlueprint = fetchByCompanyId_First(
			companyId, orderByComparator);

		if (sxpBlueprint != null) {
			return sxpBlueprint;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchSXPBlueprintException(sb.toString());
	}

	/**
	 * Returns the first sxp blueprint in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching sxp blueprint, or <code>null</code> if a matching sxp blueprint could not be found
	 */
	@Override
	public SXPBlueprint fetchByCompanyId_First(
		long companyId, OrderByComparator<SXPBlueprint> orderByComparator) {

		List<SXPBlueprint> list = findByCompanyId(
			companyId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last sxp blueprint in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching sxp blueprint
	 * @throws NoSuchSXPBlueprintException if a matching sxp blueprint could not be found
	 */
	@Override
	public SXPBlueprint findByCompanyId_Last(
			long companyId, OrderByComparator<SXPBlueprint> orderByComparator)
		throws NoSuchSXPBlueprintException {

		SXPBlueprint sxpBlueprint = fetchByCompanyId_Last(
			companyId, orderByComparator);

		if (sxpBlueprint != null) {
			return sxpBlueprint;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchSXPBlueprintException(sb.toString());
	}

	/**
	 * Returns the last sxp blueprint in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching sxp blueprint, or <code>null</code> if a matching sxp blueprint could not be found
	 */
	@Override
	public SXPBlueprint fetchByCompanyId_Last(
		long companyId, OrderByComparator<SXPBlueprint> orderByComparator) {

		int count = countByCompanyId(companyId);

		if (count == 0) {
			return null;
		}

		List<SXPBlueprint> list = findByCompanyId(
			companyId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the sxp blueprints before and after the current sxp blueprint in the ordered set where companyId = &#63;.
	 *
	 * @param sxpBlueprintId the primary key of the current sxp blueprint
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next sxp blueprint
	 * @throws NoSuchSXPBlueprintException if a sxp blueprint with the primary key could not be found
	 */
	@Override
	public SXPBlueprint[] findByCompanyId_PrevAndNext(
			long sxpBlueprintId, long companyId,
			OrderByComparator<SXPBlueprint> orderByComparator)
		throws NoSuchSXPBlueprintException {

		SXPBlueprint sxpBlueprint = findByPrimaryKey(sxpBlueprintId);

		Session session = null;

		try {
			session = openSession();

			SXPBlueprint[] array = new SXPBlueprintImpl[3];

			array[0] = getByCompanyId_PrevAndNext(
				session, sxpBlueprint, companyId, orderByComparator, true);

			array[1] = sxpBlueprint;

			array[2] = getByCompanyId_PrevAndNext(
				session, sxpBlueprint, companyId, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected SXPBlueprint getByCompanyId_PrevAndNext(
		Session session, SXPBlueprint sxpBlueprint, long companyId,
		OrderByComparator<SXPBlueprint> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_SXPBLUEPRINT_WHERE);

		sb.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(SXPBlueprintModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(companyId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(sxpBlueprint)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<SXPBlueprint> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the sxp blueprints that the user has permission to view where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the matching sxp blueprints that the user has permission to view
	 */
	@Override
	public List<SXPBlueprint> filterFindByCompanyId(long companyId) {
		return filterFindByCompanyId(
			companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the sxp blueprints that the user has permission to view where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SXPBlueprintModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of sxp blueprints
	 * @param end the upper bound of the range of sxp blueprints (not inclusive)
	 * @return the range of matching sxp blueprints that the user has permission to view
	 */
	@Override
	public List<SXPBlueprint> filterFindByCompanyId(
		long companyId, int start, int end) {

		return filterFindByCompanyId(companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the sxp blueprints that the user has permissions to view where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SXPBlueprintModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of sxp blueprints
	 * @param end the upper bound of the range of sxp blueprints (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching sxp blueprints that the user has permission to view
	 */
	@Override
	public List<SXPBlueprint> filterFindByCompanyId(
		long companyId, int start, int end,
		OrderByComparator<SXPBlueprint> orderByComparator) {

		if (!InlineSQLHelperUtil.isEnabled(companyId, 0)) {
			return findByCompanyId(companyId, start, end, orderByComparator);
		}

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				3 + (orderByComparator.getOrderByFields().length * 2));
		}
		else {
			sb = new StringBundler(4);
		}

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_SXPBLUEPRINT_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_SXPBLUEPRINT_NO_INLINE_DISTINCT_WHERE_1);
		}

		sb.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_SXPBLUEPRINT_NO_INLINE_DISTINCT_WHERE_2);
		}

		if (orderByComparator != null) {
			if (getDB().isSupportsInlineDistinct()) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator, true);
			}
			else {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_TABLE, orderByComparator, true);
			}
		}
		else {
			if (getDB().isSupportsInlineDistinct()) {
				sb.append(SXPBlueprintModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(SXPBlueprintModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), SXPBlueprint.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			if (getDB().isSupportsInlineDistinct()) {
				sqlQuery.addEntity(
					_FILTER_ENTITY_ALIAS, SXPBlueprintImpl.class);
			}
			else {
				sqlQuery.addEntity(
					_FILTER_ENTITY_TABLE, SXPBlueprintImpl.class);
			}

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(companyId);

			return (List<SXPBlueprint>)QueryUtil.list(
				sqlQuery, getDialect(), start, end);
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	/**
	 * Returns the sxp blueprints before and after the current sxp blueprint in the ordered set of sxp blueprints that the user has permission to view where companyId = &#63;.
	 *
	 * @param sxpBlueprintId the primary key of the current sxp blueprint
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next sxp blueprint
	 * @throws NoSuchSXPBlueprintException if a sxp blueprint with the primary key could not be found
	 */
	@Override
	public SXPBlueprint[] filterFindByCompanyId_PrevAndNext(
			long sxpBlueprintId, long companyId,
			OrderByComparator<SXPBlueprint> orderByComparator)
		throws NoSuchSXPBlueprintException {

		if (!InlineSQLHelperUtil.isEnabled(companyId, 0)) {
			return findByCompanyId_PrevAndNext(
				sxpBlueprintId, companyId, orderByComparator);
		}

		SXPBlueprint sxpBlueprint = findByPrimaryKey(sxpBlueprintId);

		Session session = null;

		try {
			session = openSession();

			SXPBlueprint[] array = new SXPBlueprintImpl[3];

			array[0] = filterGetByCompanyId_PrevAndNext(
				session, sxpBlueprint, companyId, orderByComparator, true);

			array[1] = sxpBlueprint;

			array[2] = filterGetByCompanyId_PrevAndNext(
				session, sxpBlueprint, companyId, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected SXPBlueprint filterGetByCompanyId_PrevAndNext(
		Session session, SXPBlueprint sxpBlueprint, long companyId,
		OrderByComparator<SXPBlueprint> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_SXPBLUEPRINT_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_SXPBLUEPRINT_NO_INLINE_DISTINCT_WHERE_1);
		}

		sb.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_SXPBLUEPRINT_NO_INLINE_DISTINCT_WHERE_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				if (getDB().isSupportsInlineDistinct()) {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_ALIAS, orderByConditionFields[i],
							true));
				}
				else {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_TABLE, orderByConditionFields[i],
							true));
				}

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				if (getDB().isSupportsInlineDistinct()) {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_ALIAS, orderByFields[i], true));
				}
				else {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_TABLE, orderByFields[i], true));
				}

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			if (getDB().isSupportsInlineDistinct()) {
				sb.append(SXPBlueprintModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(SXPBlueprintModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), SXPBlueprint.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN);

		SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

		sqlQuery.setFirstResult(0);
		sqlQuery.setMaxResults(2);

		if (getDB().isSupportsInlineDistinct()) {
			sqlQuery.addEntity(_FILTER_ENTITY_ALIAS, SXPBlueprintImpl.class);
		}
		else {
			sqlQuery.addEntity(_FILTER_ENTITY_TABLE, SXPBlueprintImpl.class);
		}

		QueryPos queryPos = QueryPos.getInstance(sqlQuery);

		queryPos.add(companyId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(sxpBlueprint)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<SXPBlueprint> list = sqlQuery.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the sxp blueprints where companyId = &#63; from the database.
	 *
	 * @param companyId the company ID
	 */
	@Override
	public void removeByCompanyId(long companyId) {
		for (SXPBlueprint sxpBlueprint :
				findByCompanyId(
					companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(sxpBlueprint);
		}
	}

	/**
	 * Returns the number of sxp blueprints where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the number of matching sxp blueprints
	 */
	@Override
	public int countByCompanyId(long companyId) {
		FinderPath finderPath = _finderPathCountByCompanyId;

		Object[] finderArgs = new Object[] {companyId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_SXPBLUEPRINT_WHERE);

			sb.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of sxp blueprints that the user has permission to view where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the number of matching sxp blueprints that the user has permission to view
	 */
	@Override
	public int filterCountByCompanyId(long companyId) {
		if (!InlineSQLHelperUtil.isEnabled(companyId, 0)) {
			return countByCompanyId(companyId);
		}

		StringBundler sb = new StringBundler(2);

		sb.append(_FILTER_SQL_COUNT_SXPBLUEPRINT_WHERE);

		sb.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), SXPBlueprint.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			sqlQuery.addScalar(
				COUNT_COLUMN_NAME, com.liferay.portal.kernel.dao.orm.Type.LONG);

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(companyId);

			Long count = (Long)sqlQuery.uniqueResult();

			return count.intValue();
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	private static final String _FINDER_COLUMN_COMPANYID_COMPANYID_2 =
		"sxpBlueprint.companyId = ?";

	private FinderPath _finderPathFetchByERC_C;
	private FinderPath _finderPathCountByERC_C;

	/**
	 * Returns the sxp blueprint where externalReferenceCode = &#63; and companyId = &#63; or throws a <code>NoSuchSXPBlueprintException</code> if it could not be found.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param companyId the company ID
	 * @return the matching sxp blueprint
	 * @throws NoSuchSXPBlueprintException if a matching sxp blueprint could not be found
	 */
	@Override
	public SXPBlueprint findByERC_C(
			String externalReferenceCode, long companyId)
		throws NoSuchSXPBlueprintException {

		SXPBlueprint sxpBlueprint = fetchByERC_C(
			externalReferenceCode, companyId);

		if (sxpBlueprint == null) {
			StringBundler sb = new StringBundler(6);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("externalReferenceCode=");
			sb.append(externalReferenceCode);

			sb.append(", companyId=");
			sb.append(companyId);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchSXPBlueprintException(sb.toString());
		}

		return sxpBlueprint;
	}

	/**
	 * Returns the sxp blueprint where externalReferenceCode = &#63; and companyId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param companyId the company ID
	 * @return the matching sxp blueprint, or <code>null</code> if a matching sxp blueprint could not be found
	 */
	@Override
	public SXPBlueprint fetchByERC_C(
		String externalReferenceCode, long companyId) {

		return fetchByERC_C(externalReferenceCode, companyId, true);
	}

	/**
	 * Returns the sxp blueprint where externalReferenceCode = &#63; and companyId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param companyId the company ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching sxp blueprint, or <code>null</code> if a matching sxp blueprint could not be found
	 */
	@Override
	public SXPBlueprint fetchByERC_C(
		String externalReferenceCode, long companyId, boolean useFinderCache) {

		externalReferenceCode = Objects.toString(externalReferenceCode, "");

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {externalReferenceCode, companyId};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(
				_finderPathFetchByERC_C, finderArgs, this);
		}

		if (result instanceof SXPBlueprint) {
			SXPBlueprint sxpBlueprint = (SXPBlueprint)result;

			if (!Objects.equals(
					externalReferenceCode,
					sxpBlueprint.getExternalReferenceCode()) ||
				(companyId != sxpBlueprint.getCompanyId())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_SXPBLUEPRINT_WHERE);

			boolean bindExternalReferenceCode = false;

			if (externalReferenceCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_ERC_C_EXTERNALREFERENCECODE_3);
			}
			else {
				bindExternalReferenceCode = true;

				sb.append(_FINDER_COLUMN_ERC_C_EXTERNALREFERENCECODE_2);
			}

			sb.append(_FINDER_COLUMN_ERC_C_COMPANYID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindExternalReferenceCode) {
					queryPos.add(externalReferenceCode);
				}

				queryPos.add(companyId);

				List<SXPBlueprint> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByERC_C, finderArgs, list);
					}
				}
				else {
					SXPBlueprint sxpBlueprint = list.get(0);

					result = sxpBlueprint;

					cacheResult(sxpBlueprint);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (SXPBlueprint)result;
		}
	}

	/**
	 * Removes the sxp blueprint where externalReferenceCode = &#63; and companyId = &#63; from the database.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param companyId the company ID
	 * @return the sxp blueprint that was removed
	 */
	@Override
	public SXPBlueprint removeByERC_C(
			String externalReferenceCode, long companyId)
		throws NoSuchSXPBlueprintException {

		SXPBlueprint sxpBlueprint = findByERC_C(
			externalReferenceCode, companyId);

		return remove(sxpBlueprint);
	}

	/**
	 * Returns the number of sxp blueprints where externalReferenceCode = &#63; and companyId = &#63;.
	 *
	 * @param externalReferenceCode the external reference code
	 * @param companyId the company ID
	 * @return the number of matching sxp blueprints
	 */
	@Override
	public int countByERC_C(String externalReferenceCode, long companyId) {
		externalReferenceCode = Objects.toString(externalReferenceCode, "");

		FinderPath finderPath = _finderPathCountByERC_C;

		Object[] finderArgs = new Object[] {externalReferenceCode, companyId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_SXPBLUEPRINT_WHERE);

			boolean bindExternalReferenceCode = false;

			if (externalReferenceCode.isEmpty()) {
				sb.append(_FINDER_COLUMN_ERC_C_EXTERNALREFERENCECODE_3);
			}
			else {
				bindExternalReferenceCode = true;

				sb.append(_FINDER_COLUMN_ERC_C_EXTERNALREFERENCECODE_2);
			}

			sb.append(_FINDER_COLUMN_ERC_C_COMPANYID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindExternalReferenceCode) {
					queryPos.add(externalReferenceCode);
				}

				queryPos.add(companyId);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_ERC_C_EXTERNALREFERENCECODE_2 =
		"sxpBlueprint.externalReferenceCode = ? AND ";

	private static final String _FINDER_COLUMN_ERC_C_EXTERNALREFERENCECODE_3 =
		"(sxpBlueprint.externalReferenceCode IS NULL OR sxpBlueprint.externalReferenceCode = '') AND ";

	private static final String _FINDER_COLUMN_ERC_C_COMPANYID_2 =
		"sxpBlueprint.companyId = ?";

	public SXPBlueprintPersistenceImpl() {
		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("uuid", "uuid_");

		setDBColumnNames(dbColumnNames);

		setModelClass(SXPBlueprint.class);

		setModelImplClass(SXPBlueprintImpl.class);
		setModelPKClass(long.class);

		setTable(SXPBlueprintTable.INSTANCE);
	}

	/**
	 * Caches the sxp blueprint in the entity cache if it is enabled.
	 *
	 * @param sxpBlueprint the sxp blueprint
	 */
	@Override
	public void cacheResult(SXPBlueprint sxpBlueprint) {
		entityCache.putResult(
			SXPBlueprintImpl.class, sxpBlueprint.getPrimaryKey(), sxpBlueprint);

		finderCache.putResult(
			_finderPathFetchByERC_C,
			new Object[] {
				sxpBlueprint.getExternalReferenceCode(),
				sxpBlueprint.getCompanyId()
			},
			sxpBlueprint);
	}

	private int _valueObjectFinderCacheListThreshold;

	/**
	 * Caches the sxp blueprints in the entity cache if it is enabled.
	 *
	 * @param sxpBlueprints the sxp blueprints
	 */
	@Override
	public void cacheResult(List<SXPBlueprint> sxpBlueprints) {
		if ((_valueObjectFinderCacheListThreshold == 0) ||
			((_valueObjectFinderCacheListThreshold > 0) &&
			 (sxpBlueprints.size() > _valueObjectFinderCacheListThreshold))) {

			return;
		}

		for (SXPBlueprint sxpBlueprint : sxpBlueprints) {
			if (entityCache.getResult(
					SXPBlueprintImpl.class, sxpBlueprint.getPrimaryKey()) ==
						null) {

				cacheResult(sxpBlueprint);
			}
		}
	}

	/**
	 * Clears the cache for all sxp blueprints.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(SXPBlueprintImpl.class);

		finderCache.clearCache(SXPBlueprintImpl.class);
	}

	/**
	 * Clears the cache for the sxp blueprint.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(SXPBlueprint sxpBlueprint) {
		entityCache.removeResult(SXPBlueprintImpl.class, sxpBlueprint);
	}

	@Override
	public void clearCache(List<SXPBlueprint> sxpBlueprints) {
		for (SXPBlueprint sxpBlueprint : sxpBlueprints) {
			entityCache.removeResult(SXPBlueprintImpl.class, sxpBlueprint);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(SXPBlueprintImpl.class);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(SXPBlueprintImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(
		SXPBlueprintModelImpl sxpBlueprintModelImpl) {

		Object[] args = new Object[] {
			sxpBlueprintModelImpl.getExternalReferenceCode(),
			sxpBlueprintModelImpl.getCompanyId()
		};

		finderCache.putResult(_finderPathCountByERC_C, args, Long.valueOf(1));
		finderCache.putResult(
			_finderPathFetchByERC_C, args, sxpBlueprintModelImpl);
	}

	/**
	 * Creates a new sxp blueprint with the primary key. Does not add the sxp blueprint to the database.
	 *
	 * @param sxpBlueprintId the primary key for the new sxp blueprint
	 * @return the new sxp blueprint
	 */
	@Override
	public SXPBlueprint create(long sxpBlueprintId) {
		SXPBlueprint sxpBlueprint = new SXPBlueprintImpl();

		sxpBlueprint.setNew(true);
		sxpBlueprint.setPrimaryKey(sxpBlueprintId);

		String uuid = _portalUUID.generate();

		sxpBlueprint.setUuid(uuid);

		sxpBlueprint.setCompanyId(CompanyThreadLocal.getCompanyId());

		return sxpBlueprint;
	}

	/**
	 * Removes the sxp blueprint with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param sxpBlueprintId the primary key of the sxp blueprint
	 * @return the sxp blueprint that was removed
	 * @throws NoSuchSXPBlueprintException if a sxp blueprint with the primary key could not be found
	 */
	@Override
	public SXPBlueprint remove(long sxpBlueprintId)
		throws NoSuchSXPBlueprintException {

		return remove((Serializable)sxpBlueprintId);
	}

	/**
	 * Removes the sxp blueprint with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the sxp blueprint
	 * @return the sxp blueprint that was removed
	 * @throws NoSuchSXPBlueprintException if a sxp blueprint with the primary key could not be found
	 */
	@Override
	public SXPBlueprint remove(Serializable primaryKey)
		throws NoSuchSXPBlueprintException {

		Session session = null;

		try {
			session = openSession();

			SXPBlueprint sxpBlueprint = (SXPBlueprint)session.get(
				SXPBlueprintImpl.class, primaryKey);

			if (sxpBlueprint == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchSXPBlueprintException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(sxpBlueprint);
		}
		catch (NoSuchSXPBlueprintException noSuchEntityException) {
			throw noSuchEntityException;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected SXPBlueprint removeImpl(SXPBlueprint sxpBlueprint) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(sxpBlueprint)) {
				sxpBlueprint = (SXPBlueprint)session.get(
					SXPBlueprintImpl.class, sxpBlueprint.getPrimaryKeyObj());
			}

			if (sxpBlueprint != null) {
				session.delete(sxpBlueprint);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (sxpBlueprint != null) {
			clearCache(sxpBlueprint);
		}

		return sxpBlueprint;
	}

	@Override
	public SXPBlueprint updateImpl(SXPBlueprint sxpBlueprint) {
		boolean isNew = sxpBlueprint.isNew();

		if (!(sxpBlueprint instanceof SXPBlueprintModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(sxpBlueprint.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					sxpBlueprint);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in sxpBlueprint proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom SXPBlueprint implementation " +
					sxpBlueprint.getClass());
		}

		SXPBlueprintModelImpl sxpBlueprintModelImpl =
			(SXPBlueprintModelImpl)sxpBlueprint;

		if (Validator.isNull(sxpBlueprint.getUuid())) {
			String uuid = _portalUUID.generate();

			sxpBlueprint.setUuid(uuid);
		}

		if (Validator.isNull(sxpBlueprint.getExternalReferenceCode())) {
			sxpBlueprint.setExternalReferenceCode(sxpBlueprint.getUuid());
		}
		else {
			SXPBlueprint ercSXPBlueprint = fetchByERC_C(
				sxpBlueprint.getExternalReferenceCode(),
				sxpBlueprint.getCompanyId());

			if (isNew) {
				if (ercSXPBlueprint != null) {
					throw new DuplicateSXPBlueprintExternalReferenceCodeException(
						"Duplicate sxp blueprint with external reference code " +
							sxpBlueprint.getExternalReferenceCode() +
								" and company " + sxpBlueprint.getCompanyId());
				}
			}
			else {
				if ((ercSXPBlueprint != null) &&
					(sxpBlueprint.getSXPBlueprintId() !=
						ercSXPBlueprint.getSXPBlueprintId())) {

					throw new DuplicateSXPBlueprintExternalReferenceCodeException(
						"Duplicate sxp blueprint with external reference code " +
							sxpBlueprint.getExternalReferenceCode() +
								" and company " + sxpBlueprint.getCompanyId());
				}
			}
		}

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date date = new Date();

		if (isNew && (sxpBlueprint.getCreateDate() == null)) {
			if (serviceContext == null) {
				sxpBlueprint.setCreateDate(date);
			}
			else {
				sxpBlueprint.setCreateDate(serviceContext.getCreateDate(date));
			}
		}

		if (!sxpBlueprintModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				sxpBlueprint.setModifiedDate(date);
			}
			else {
				sxpBlueprint.setModifiedDate(
					serviceContext.getModifiedDate(date));
			}
		}

		long userId = GetterUtil.getLong(PrincipalThreadLocal.getName());

		if (userId > 0) {
			long companyId = sxpBlueprint.getCompanyId();

			long groupId = 0;

			long sxpBlueprintId = 0;

			if (!isNew) {
				sxpBlueprintId = sxpBlueprint.getPrimaryKey();
			}

			try {
				sxpBlueprint.setTitle(
					SanitizerUtil.sanitize(
						companyId, groupId, userId,
						SXPBlueprint.class.getName(), sxpBlueprintId,
						ContentTypes.TEXT_PLAIN, Sanitizer.MODE_ALL,
						sxpBlueprint.getTitle(), null));
			}
			catch (SanitizerException sanitizerException) {
				throw new SystemException(sanitizerException);
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(sxpBlueprint);
			}
			else {
				sxpBlueprint = (SXPBlueprint)session.merge(sxpBlueprint);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		entityCache.putResult(
			SXPBlueprintImpl.class, sxpBlueprintModelImpl, false, true);

		cacheUniqueFindersCache(sxpBlueprintModelImpl);

		if (isNew) {
			sxpBlueprint.setNew(false);
		}

		sxpBlueprint.resetOriginalValues();

		return sxpBlueprint;
	}

	/**
	 * Returns the sxp blueprint with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the sxp blueprint
	 * @return the sxp blueprint
	 * @throws NoSuchSXPBlueprintException if a sxp blueprint with the primary key could not be found
	 */
	@Override
	public SXPBlueprint findByPrimaryKey(Serializable primaryKey)
		throws NoSuchSXPBlueprintException {

		SXPBlueprint sxpBlueprint = fetchByPrimaryKey(primaryKey);

		if (sxpBlueprint == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchSXPBlueprintException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return sxpBlueprint;
	}

	/**
	 * Returns the sxp blueprint with the primary key or throws a <code>NoSuchSXPBlueprintException</code> if it could not be found.
	 *
	 * @param sxpBlueprintId the primary key of the sxp blueprint
	 * @return the sxp blueprint
	 * @throws NoSuchSXPBlueprintException if a sxp blueprint with the primary key could not be found
	 */
	@Override
	public SXPBlueprint findByPrimaryKey(long sxpBlueprintId)
		throws NoSuchSXPBlueprintException {

		return findByPrimaryKey((Serializable)sxpBlueprintId);
	}

	/**
	 * Returns the sxp blueprint with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param sxpBlueprintId the primary key of the sxp blueprint
	 * @return the sxp blueprint, or <code>null</code> if a sxp blueprint with the primary key could not be found
	 */
	@Override
	public SXPBlueprint fetchByPrimaryKey(long sxpBlueprintId) {
		return fetchByPrimaryKey((Serializable)sxpBlueprintId);
	}

	/**
	 * Returns all the sxp blueprints.
	 *
	 * @return the sxp blueprints
	 */
	@Override
	public List<SXPBlueprint> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the sxp blueprints.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SXPBlueprintModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of sxp blueprints
	 * @param end the upper bound of the range of sxp blueprints (not inclusive)
	 * @return the range of sxp blueprints
	 */
	@Override
	public List<SXPBlueprint> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the sxp blueprints.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SXPBlueprintModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of sxp blueprints
	 * @param end the upper bound of the range of sxp blueprints (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of sxp blueprints
	 */
	@Override
	public List<SXPBlueprint> findAll(
		int start, int end, OrderByComparator<SXPBlueprint> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the sxp blueprints.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SXPBlueprintModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of sxp blueprints
	 * @param end the upper bound of the range of sxp blueprints (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of sxp blueprints
	 */
	@Override
	public List<SXPBlueprint> findAll(
		int start, int end, OrderByComparator<SXPBlueprint> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindAll;
				finderArgs = FINDER_ARGS_EMPTY;
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindAll;
			finderArgs = new Object[] {start, end, orderByComparator};
		}

		List<SXPBlueprint> list = null;

		if (useFinderCache) {
			list = (List<SXPBlueprint>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_SXPBLUEPRINT);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_SXPBLUEPRINT;

				sql = sql.concat(SXPBlueprintModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<SXPBlueprint>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the sxp blueprints from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (SXPBlueprint sxpBlueprint : findAll()) {
			remove(sxpBlueprint);
		}
	}

	/**
	 * Returns the number of sxp blueprints.
	 *
	 * @return the number of sxp blueprints
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(_SQL_COUNT_SXPBLUEPRINT);

				count = (Long)query.uniqueResult();

				finderCache.putResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	public Set<String> getBadColumnNames() {
		return _badColumnNames;
	}

	@Override
	protected EntityCache getEntityCache() {
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "sxpBlueprintId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_SXPBLUEPRINT;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return SXPBlueprintModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the sxp blueprint persistence.
	 */
	@Activate
	public void activate() {
		_valueObjectFinderCacheListThreshold = GetterUtil.getInteger(
			PropsUtil.get(PropsKeys.VALUE_OBJECT_FINDER_CACHE_LIST_THRESHOLD));

		_finderPathWithPaginationFindAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathWithoutPaginationFindAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathCountAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0], new String[0], false);

		_finderPathWithPaginationFindByUuid = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid",
			new String[] {
				String.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"uuid_"}, true);

		_finderPathWithoutPaginationFindByUuid = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid",
			new String[] {String.class.getName()}, new String[] {"uuid_"},
			true);

		_finderPathCountByUuid = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid",
			new String[] {String.class.getName()}, new String[] {"uuid_"},
			false);

		_finderPathWithPaginationFindByUuid_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid_C",
			new String[] {
				String.class.getName(), Long.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			},
			new String[] {"uuid_", "companyId"}, true);

		_finderPathWithoutPaginationFindByUuid_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid_C",
			new String[] {String.class.getName(), Long.class.getName()},
			new String[] {"uuid_", "companyId"}, true);

		_finderPathCountByUuid_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid_C",
			new String[] {String.class.getName(), Long.class.getName()},
			new String[] {"uuid_", "companyId"}, false);

		_finderPathWithPaginationFindByCompanyId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByCompanyId",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"companyId"}, true);

		_finderPathWithoutPaginationFindByCompanyId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByCompanyId",
			new String[] {Long.class.getName()}, new String[] {"companyId"},
			true);

		_finderPathCountByCompanyId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByCompanyId",
			new String[] {Long.class.getName()}, new String[] {"companyId"},
			false);

		_finderPathFetchByERC_C = new FinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByERC_C",
			new String[] {String.class.getName(), Long.class.getName()},
			new String[] {"externalReferenceCode", "companyId"}, true);

		_finderPathCountByERC_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByERC_C",
			new String[] {String.class.getName(), Long.class.getName()},
			new String[] {"externalReferenceCode", "companyId"}, false);

		SXPBlueprintUtil.setPersistence(this);
	}

	@Deactivate
	public void deactivate() {
		SXPBlueprintUtil.setPersistence(null);

		entityCache.removeCache(SXPBlueprintImpl.class.getName());
	}

	@Override
	@Reference(
		target = SXPPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
	}

	@Override
	@Reference(
		target = SXPPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = SXPPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Reference
	protected EntityCache entityCache;

	@Reference
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_SXPBLUEPRINT =
		"SELECT sxpBlueprint FROM SXPBlueprint sxpBlueprint";

	private static final String _SQL_SELECT_SXPBLUEPRINT_WHERE =
		"SELECT sxpBlueprint FROM SXPBlueprint sxpBlueprint WHERE ";

	private static final String _SQL_COUNT_SXPBLUEPRINT =
		"SELECT COUNT(sxpBlueprint) FROM SXPBlueprint sxpBlueprint";

	private static final String _SQL_COUNT_SXPBLUEPRINT_WHERE =
		"SELECT COUNT(sxpBlueprint) FROM SXPBlueprint sxpBlueprint WHERE ";

	private static final String _FILTER_ENTITY_TABLE_FILTER_PK_COLUMN =
		"sxpBlueprint.sxpBlueprintId";

	private static final String _FILTER_SQL_SELECT_SXPBLUEPRINT_WHERE =
		"SELECT DISTINCT {sxpBlueprint.*} FROM SXPBlueprint sxpBlueprint WHERE ";

	private static final String
		_FILTER_SQL_SELECT_SXPBLUEPRINT_NO_INLINE_DISTINCT_WHERE_1 =
			"SELECT {SXPBlueprint.*} FROM (SELECT DISTINCT sxpBlueprint.sxpBlueprintId FROM SXPBlueprint sxpBlueprint WHERE ";

	private static final String
		_FILTER_SQL_SELECT_SXPBLUEPRINT_NO_INLINE_DISTINCT_WHERE_2 =
			") TEMP_TABLE INNER JOIN SXPBlueprint ON TEMP_TABLE.sxpBlueprintId = SXPBlueprint.sxpBlueprintId";

	private static final String _FILTER_SQL_COUNT_SXPBLUEPRINT_WHERE =
		"SELECT COUNT(DISTINCT sxpBlueprint.sxpBlueprintId) AS COUNT_VALUE FROM SXPBlueprint sxpBlueprint WHERE ";

	private static final String _FILTER_ENTITY_ALIAS = "sxpBlueprint";

	private static final String _FILTER_ENTITY_TABLE = "SXPBlueprint";

	private static final String _ORDER_BY_ENTITY_ALIAS = "sxpBlueprint.";

	private static final String _ORDER_BY_ENTITY_TABLE = "SXPBlueprint.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No SXPBlueprint exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No SXPBlueprint exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		SXPBlueprintPersistenceImpl.class);

	private static final Set<String> _badColumnNames = SetUtil.fromArray(
		new String[] {"uuid"});

	@Override
	protected FinderCache getFinderCache() {
		return finderCache;
	}

	@Reference
	private PortalUUID _portalUUID;

}