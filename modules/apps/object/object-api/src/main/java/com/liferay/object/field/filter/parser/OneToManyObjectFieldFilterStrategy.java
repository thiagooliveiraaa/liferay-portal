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

package com.liferay.object.field.filter.parser;

import com.liferay.frontend.data.set.filter.FDSFilter;
import com.liferay.frontend.data.set.filter.SelectionFDSFilterItem;
import com.liferay.object.exception.ObjectViewFilterColumnException;
import com.liferay.object.field.frontend.data.set.filter.OneToManySelectionFDSFilter;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.model.ObjectField;
import com.liferay.object.model.ObjectRelationship;
import com.liferay.object.model.ObjectViewFilterColumn;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.object.service.ObjectFieldLocalService;
import com.liferay.object.service.ObjectRelationshipLocalService;
import com.liferay.object.system.JaxRsApplicationDescriptor;
import com.liferay.object.system.SystemObjectDefinitionManager;
import com.liferay.object.system.SystemObjectDefinitionManagerRegistry;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.NoSuchModelException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.service.PersistedModelLocalServiceRegistry;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Feliphe Marinho
 */
public class OneToManyObjectFieldFilterStrategy
	extends BaseObjectFieldFilterStrategy {

	public OneToManyObjectFieldFilterStrategy(
		Locale locale, ObjectDefinition objectDefinition1,
		ObjectDefinitionLocalService objectDefinitionLocalService,
		ObjectEntryLocalService objectEntryLocalService,
		ObjectField objectField,
		ObjectFieldLocalService objectFieldLocalService,
		ObjectRelationshipLocalService objectRelationshipLocalService,
		ObjectViewFilterColumn objectViewFilterColumn,
		PersistedModelLocalServiceRegistry persistedModelLocalServiceRegistry,
		SystemObjectDefinitionManagerRegistry
			systemObjectDefinitionManagerRegistry) {

		super(locale, objectViewFilterColumn);

		_objectDefinition1 = objectDefinition1;
		_objectDefinitionLocalService = objectDefinitionLocalService;
		_objectEntryLocalService = objectEntryLocalService;
		_objectField = objectField;
		_objectFieldLocalService = objectFieldLocalService;
		_objectRelationshipLocalService = objectRelationshipLocalService;
		_persistedModelLocalServiceRegistry =
			persistedModelLocalServiceRegistry;
		_systemObjectDefinitionManagerRegistry =
			systemObjectDefinitionManagerRegistry;
	}

	@Override
	public FDSFilter getFDSFilter() throws PortalException {
		ObjectRelationship objectRelationship =
			_objectRelationshipLocalService.
				fetchObjectRelationshipByObjectFieldId2(
					_objectField.getObjectFieldId());

		ObjectDefinition objectDefinition1 =
			_objectDefinitionLocalService.getObjectDefinition(
				objectRelationship.getObjectDefinitionId1());

		ObjectField titleObjectField = null;

		if (Validator.isNull(objectDefinition1.getTitleObjectFieldId())) {
			titleObjectField = _objectFieldLocalService.getObjectField(
				objectDefinition1.getObjectDefinitionId(), "id");
		}
		else {
			titleObjectField = _objectFieldLocalService.getObjectField(
				objectDefinition1.getTitleObjectFieldId());
		}

		String restContextPath = null;

		if (objectDefinition1.isUnmodifiableSystemObject()) {
			SystemObjectDefinitionManager systemObjectDefinitionManager =
				_systemObjectDefinitionManagerRegistry.
					getSystemObjectDefinitionManager(
						objectDefinition1.getName());

			JaxRsApplicationDescriptor jaxRsApplicationDescriptor =
				systemObjectDefinitionManager.getJaxRsApplicationDescriptor();

			restContextPath =
				"/o/" + jaxRsApplicationDescriptor.getRESTContextPath();
		}
		else {
			restContextPath = "/o" + objectDefinition1.getRESTContextPath();
		}

		return new OneToManySelectionFDSFilter(
			parse(), restContextPath, titleObjectField.getLabel(locale),
			_objectField.getName(), titleObjectField.getName());
	}

	@Override
	public List<SelectionFDSFilterItem> getSelectionFDSFilterItems()
		throws PortalException {

		List<SelectionFDSFilterItem> selectionFDSFilterItems =
			new ArrayList<>();

		JSONArray jsonArray = getJSONArray();

		if (_objectDefinition1.isUnmodifiableSystemObject()) {
			for (int i = 0; i < jsonArray.length(); i++) {
				selectionFDSFilterItems.add(
					new SelectionFDSFilterItem(
						_objectEntryLocalService.getTitleValue(
							_objectDefinition1.getObjectDefinitionId(),
							GetterUtil.getLong(jsonArray.get(i))),
						jsonArray.getLong(i)));
			}

			return selectionFDSFilterItems;
		}

		for (int i = 0; i < jsonArray.length(); i++) {
			ObjectEntry objectEntry = _objectEntryLocalService.fetchObjectEntry(
				(String)jsonArray.get(i),
				_objectDefinition1.getObjectDefinitionId());

			if (objectEntry == null) {
				continue;
			}

			selectionFDSFilterItems.add(
				new SelectionFDSFilterItem(
					objectEntry.getTitleValue(),
					objectEntry.getObjectEntryId()));
		}

		return selectionFDSFilterItems;
	}

	@Override
	public String toValueSummary() throws PortalException {
		return StringUtil.merge(
			ListUtil.toList(
				getSelectionFDSFilterItems(),
				selectionFDSFilterItem -> selectionFDSFilterItem.getLabel()),
			StringPool.COMMA_AND_SPACE);
	}

	@Override
	public void validate() throws PortalException {
		super.validate();

		JSONArray jsonArray = getJSONArray();

		if (_objectDefinition1.isUnmodifiableSystemObject()) {
			PersistedModelLocalService persistedModelLocalService =
				_persistedModelLocalServiceRegistry.
					getPersistedModelLocalService(
						_objectDefinition1.getClassName());

			for (int i = 0; i < jsonArray.length(); i++) {
				try {
					persistedModelLocalService.getPersistedModel(
						GetterUtil.getLong(jsonArray.get(i)));
				}
				catch (NoSuchModelException noSuchModelException) {
					throw new ObjectViewFilterColumnException(
						noSuchModelException.getMessage());
				}
			}
		}
		else {
			for (int i = 0; i < jsonArray.length(); i++) {
				if (Validator.isNull(
						_objectEntryLocalService.fetchObjectEntry(
							(String)jsonArray.get(i),
							_objectDefinition1.getObjectDefinitionId()))) {

					throw new ObjectViewFilterColumnException(
						StringBundler.concat(
							"No ", _objectDefinition1.getShortName(),
							" exists with the external reference code ",
							(String)jsonArray.get(i)));
				}
			}
		}
	}

	private final ObjectDefinition _objectDefinition1;
	private final ObjectDefinitionLocalService _objectDefinitionLocalService;
	private final ObjectEntryLocalService _objectEntryLocalService;
	private final ObjectField _objectField;
	private final ObjectFieldLocalService _objectFieldLocalService;
	private final ObjectRelationshipLocalService
		_objectRelationshipLocalService;
	private final PersistedModelLocalServiceRegistry
		_persistedModelLocalServiceRegistry;
	private final SystemObjectDefinitionManagerRegistry
		_systemObjectDefinitionManagerRegistry;

}