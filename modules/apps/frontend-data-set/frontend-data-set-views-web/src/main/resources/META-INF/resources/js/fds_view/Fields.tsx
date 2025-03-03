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

import ClayButton, {ClayButtonWithIcon} from '@clayui/button';
import ClayDropDown from '@clayui/drop-down';
import ClayForm, {ClayCheckbox, ClayInput} from '@clayui/form';
import ClayLabel from '@clayui/label';
import ClayLayout from '@clayui/layout';
import ClayLoadingIndicator from '@clayui/loading-indicator';
import ClayModal from '@clayui/modal';
import {
	FDSInternalCellRenderer,
	FDS_INTERNAL_CELL_RENDERERS,
} from '@liferay/frontend-data-set-web';
import {ManagementToolbar} from 'frontend-js-components-web';
import {fetch, navigate, openModal, openToast} from 'frontend-js-web';
import React, {useEffect, useRef, useState} from 'react';

import {API_URL, OBJECT_RELATIONSHIP} from '../Constants';
import {IFDSViewSectionInterface} from '../FDSView';
import {FDSViewType} from '../FDSViews';
import {
	IClientExtensionCellRenderer as FDSClientExtensionCellRenderer,
	getFields,
} from '../api';
import OrderableTable from '../components/OrderableTable';

import '../../css/FDSEntries.scss';

interface IFDSField {
	externalReferenceCode: string;
	id: number;
	label: string;
	name: string;
	renderer: string;
	sortable: boolean;
	type: string;
}

interface IField {
	id: number | null;
	name: string;
	selected: boolean;
	type: string;
	visible: boolean;
}

interface ISaveFDSFieldsModalContentProps {
	closeModal: Function;
	fdsFields: Array<IFDSField>;
	fdsView: FDSViewType;
	namespace: string;
	onSave: Function;
	saveFDSFieldsURL: string;
}

const SaveFDSFieldsModalContent = ({
	closeModal,
	fdsFields,
	fdsView,
	namespace,
	onSave,
	saveFDSFieldsURL,
}: ISaveFDSFieldsModalContentProps) => {
	const [fields, setFields] = useState<Array<IField> | null>(null);
	const [query, setQuery] = useState('');

	const onSearch = (query: string) => {
		setQuery(query);

		if (!fields) {
			return;
		}

		const regexp = new RegExp(query, 'i');

		setFields(
			fields.map((field) => ({
				...field,
				visible: Boolean(field.name.match(regexp)),
			}))
		);
	};

	const saveFDSFields = async () => {
		const creationData: Array<{name: string; type: string}> = [];
		const deletionIds: Array<number> = [];

		fields?.forEach((field) => {
			if (field.selected && !field.id) {
				creationData.push({name: field.name, type: field.type});
			}

			if (!field.selected && field.id) {
				deletionIds.push(field.id);
			}
		});

		const formData = new FormData();

		formData.append(
			`${namespace}creationData`,
			JSON.stringify(creationData)
		);

		deletionIds.forEach((id) => {
			formData.append(`${namespace}deletionIds`, String(id));
		});

		formData.append(`${namespace}fdsViewId`, fdsView.id);

		const response = await fetch(saveFDSFieldsURL, {
			body: formData,
			method: 'POST',
		});

		if (!response.ok) {
			openToast({
				message: Liferay.Language.get(
					'your-request-failed-to-complete'
				),
				type: 'danger',
			});

			return;
		}

		const createdFDSFields: Array<IFDSField> = await response.json();

		closeModal();

		openToast({
			message: Liferay.Language.get(
				'your-request-completed-successfully'
			),
			type: 'success',
		});

		onSave({
			createdFDSFields: createdFDSFields.map((fdsField) => ({
				...fdsField,
				id: Number(fdsField.id),
			})),
			deletedFDSFieldsIds: deletionIds,
		});
	};

	useEffect(() => {
		getFields(fdsView).then((newFields) => {
			if (!newFields) {
				return;
			}

			setFields(
				newFields.map((field) => {
					const fdsField = fdsFields.find(
						(fdsField) => fdsField.name === field.name
					);

					return {
						id: fdsField?.id || null,
						name: field.name,
						selected: Boolean(fdsField),
						type: field.type,
						visible: true,
					};
				})
			);
		});
	}, [fdsFields, fdsView]);

	const isSelectAllChecked = () => {
		if (!fields) {
			return false;
		}

		const selectedFields = fields.filter((field) => field.selected);

		const selectedFieldsCount = selectedFields?.length || 0;

		return selectedFieldsCount === fields.length;
	};

	const isSelectAllIndeterminate = () => {
		if (!fields) {
			return false;
		}

		const selectedFieldsCount =
			fields.filter((field) => field.selected)?.length || 0;

		return selectedFieldsCount > 0 && selectedFieldsCount < fields.length;
	};

	const visibleFields = fields?.filter((field) => field.visible) ?? [];

	return (
		<div className="fds-view-fields-modal">
			<ClayModal.Header>
				{Liferay.Language.get('add-fields')}
			</ClayModal.Header>

			<ClayModal.Body>
				{fields === null ? (
					<ClayLoadingIndicator />
				) : (
					<>
						<ManagementToolbar.Container>
							<ManagementToolbar.ItemList expand>
								<ManagementToolbar.Item className="pr-2">
									<ClayCheckbox
										checked={isSelectAllChecked()}
										indeterminate={isSelectAllIndeterminate()}
										onChange={({target: {checked}}) =>
											setFields(
												fields.map((field) => ({
													...field,
													selected: checked,
												}))
											)
										}
									/>
								</ManagementToolbar.Item>

								<ManagementToolbar.Item className="nav-item-expand">
									<ClayInput.Group>
										<ClayInput.GroupItem>
											<ClayInput
												insetAfter
												onChange={(event) =>
													onSearch(event.target.value)
												}
												placeholder={Liferay.Language.get(
													'search'
												)}
												type="text"
												value={query}
											/>

											<ClayInput.GroupInsetItem
												after
												tag="span"
											>
												<ClayButtonWithIcon
													aria-label={Liferay.Language.get(
														'search'
													)}
													displayType="unstyled"
													symbol="search"
												/>
											</ClayInput.GroupInsetItem>
										</ClayInput.GroupItem>
									</ClayInput.Group>
								</ManagementToolbar.Item>
							</ManagementToolbar.ItemList>
						</ManagementToolbar.Container>

						<div className="fields pb-2 pt-2">
							{visibleFields.length ? (
								visibleFields.map(({name, selected}) => (
									<div
										className="pb-2 pl-3 pr-3 pt-2"
										key={name}
									>
										<ClayCheckbox
											checked={selected}
											label={name}
											onChange={({target: {checked}}) => {
												setFields(
													fields.map((field) =>
														field.name === name
															? {
																	...field,
																	selected: checked,
															  }
															: field
													)
												);
											}}
										/>
									</div>
								))
							) : (
								<div className="pb-2 pl-3 pr-3 pt-2 text-3">
									{Liferay.Language.get('no-results-found')}
								</div>
							)}
						</div>
					</>
				)}
			</ClayModal.Body>

			<ClayModal.Footer
				last={
					<ClayButton.Group spaced>
						<ClayButton onClick={() => saveFDSFields()}>
							{Liferay.Language.get('save')}
						</ClayButton>

						<ClayButton
							displayType="secondary"
							onClick={() => closeModal()}
						>
							{Liferay.Language.get('cancel')}
						</ClayButton>
					</ClayButton.Group>
				}
			/>
		</div>
	);
};

interface IEditFDSFieldModalContentProps {
	closeModal: Function;
	fdsClientExtensionCellRenderers: FDSClientExtensionCellRenderer[];
	fdsField: IFDSField;
	namespace: string;
	onSave: Function;
}

const EditFDSFieldModalContent = ({
	closeModal,
	fdsClientExtensionCellRenderers,
	fdsField,
	namespace,
	onSave,
}: IEditFDSFieldModalContentProps) => {
	const [selectedFDSFieldRenderer, setSelectedFDSFieldRenderer] = useState(
		fdsField.renderer ?? 'default'
	);
	const [fdsFieldSortable, setFSDFieldSortable] = useState<boolean>(
		fdsField.sortable ?? true
	);

	const fdsFieldLabelRef = useRef<HTMLInputElement>(null);

	const fdsInternalCellRendererLabels = FDS_INTERNAL_CELL_RENDERERS.map(
		(cellRenderer) => cellRenderer.label
	);

	const editFDSField = async () => {
		const body = {
			label: fdsFieldLabelRef.current?.value,
			renderer: selectedFDSFieldRenderer,
			rendererType: !fdsInternalCellRendererLabels.includes(
				selectedFDSFieldRenderer
			)
				? 'clientExtension'
				: 'internal',
			sortable: fdsFieldSortable,
		};

		const response = await fetch(
			`${API_URL.FDS_FIELDS}/by-external-reference-code/${fdsField.externalReferenceCode}`,
			{
				body: JSON.stringify(body),
				headers: {
					'Accept': 'application/json',
					'Content-Type': 'application/json',
				},
				method: 'PATCH',
			}
		);

		if (!response.ok) {
			openToast({
				message: Liferay.Language.get(
					'your-request-failed-to-complete'
				),
				type: 'danger',
			});
		}

		const editedFDSField = await response.json();

		closeModal();

		openToast({
			message: Liferay.Language.get(
				'your-request-completed-successfully'
			),
			type: 'success',
		});

		onSave({editedFDSField});
	};

	const fdsFieldNameInputId = `${namespace}fdsFieldNameInput`;
	const fdsFieldLabelInputId = `${namespace}fdsFieldLabelInput`;
	const fdsFieldRendererSelectId = `${namespace}fdsFieldRendererSelectId`;

	const options = FDS_INTERNAL_CELL_RENDERERS.map(
		(renderer: FDSInternalCellRenderer) => ({
			label: renderer.label!,
			value: renderer.name!,
		})
	);

	options.push(
		...fdsClientExtensionCellRenderers.map((item) => ({
			label: item.name!,
			value: item.erc!,
		}))
	);

	const CellRendererDropdown = ({
		cellRenderers,
		namespace,
		onItemClick,
	}: {
		cellRenderers: {
			label: string;
			value: string;
		}[];
		namespace: string;
		onItemClick: Function;
	}) => {
		const fdsClientExtensionCellRenderersERCs = fdsClientExtensionCellRenderers.map(
			(cellRendererCET) => cellRendererCET.erc
		);

		return (
			<ClayDropDown
				menuElementAttrs={{
					className: 'fds-cell-renderers-dropdown-menu',
				}}
				trigger={
					<ClayButton
						aria-labelledby={`${namespace}cellRenderersLabel`}
						className="form-control form-control-select form-control-select-secondary"
						displayType="secondary"
						id={fdsFieldRendererSelectId}
					>
						{selectedFDSFieldRenderer
							? selectedFDSFieldRenderer
							: Liferay.Language.get('choose-an-option')}
					</ClayButton>
				}
			>
				<ClayDropDown.ItemList items={cellRenderers} role="listbox">
					{cellRenderers.map((cellRenderer) => (
						<ClayDropDown.Item
							className="align-items-center d-flex justify-content-between"
							key={cellRenderer.value}
							onClick={() => onItemClick(cellRenderer.label)}
							roleItem="option"
						>
							{cellRenderer.label}

							{fdsClientExtensionCellRenderersERCs.includes(
								cellRenderer.value
							) && (
								<ClayLabel displayType="info">
									{Liferay.Language.get('cell-renderer')}
								</ClayLabel>
							)}
						</ClayDropDown.Item>
					))}
				</ClayDropDown.ItemList>
			</ClayDropDown>
		);
	};

	return (
		<>
			<ClayModal.Header>
				{Liferay.Util.sub(
					Liferay.Language.get('edit-x'),
					fdsField.label
				)}
			</ClayModal.Header>

			<ClayModal.Body>
				<ClayForm.Group>
					<label htmlFor={fdsFieldNameInputId}>
						{Liferay.Language.get('name')}
					</label>

					<ClayInput
						disabled
						id={fdsFieldNameInputId}
						type="text"
						value={fdsField.name}
					/>
				</ClayForm.Group>

				<ClayForm.Group>
					<label htmlFor={fdsFieldLabelInputId}>
						{Liferay.Language.get('label')}
					</label>

					<ClayInput
						defaultValue={fdsField.label}
						id={fdsFieldLabelInputId}
						ref={fdsFieldLabelRef}
						type="text"
					/>
				</ClayForm.Group>

				<ClayForm.Group>
					<label htmlFor={fdsFieldRendererSelectId}>
						{Liferay.Language.get('cell-renderer')}
					</label>

					<CellRendererDropdown
						cellRenderers={options}
						namespace={namespace}
						onItemClick={(item: string) =>
							setSelectedFDSFieldRenderer(item)
						}
					/>
				</ClayForm.Group>

				<ClayForm.Group>
					<ClayCheckbox
						checked={fdsFieldSortable}
						label={Liferay.Language.get('sortable')}
						onChange={({target: {checked}}) =>
							setFSDFieldSortable(checked)
						}
					/>
				</ClayForm.Group>
			</ClayModal.Body>

			<ClayModal.Footer
				last={
					<ClayButton.Group spaced>
						<ClayButton onClick={() => editFDSField()}>
							{Liferay.Language.get('save')}
						</ClayButton>

						<ClayButton
							displayType="secondary"
							onClick={() => closeModal()}
						>
							{Liferay.Language.get('cancel')}
						</ClayButton>
					</ClayButton.Group>
				}
			/>
		</>
	);
};

const Fields = ({
	fdsClientExtensionCellRenderers,
	fdsView,
	fdsViewsURL,
	namespace,
	saveFDSFieldsURL,
}: IFDSViewSectionInterface) => {
	const [fdsFields, setFDSFields] = useState<Array<IFDSField> | null>(null);

	const fdsFieldsOrderRef = useRef('');

	const getFDSFields = async () => {
		const response = await fetch(
			`${API_URL.FDS_FIELDS}?filter=(${OBJECT_RELATIONSHIP.FDS_VIEW_FDS_FIELD_ID} eq '${fdsView.id}')&nestedFields=${OBJECT_RELATIONSHIP.FDS_VIEW_FDS_FIELD}`
		);

		if (!response.ok) {
			openToast({
				message: Liferay.Language.get(
					'your-request-failed-to-complete'
				),
				type: 'danger',
			});

			return null;
		}

		const responseJSON = await response.json();

		const storedFDSFields = responseJSON?.items;

		if (!storedFDSFields) {
			openToast({
				message: Liferay.Language.get(
					'your-request-failed-to-complete'
				),
				type: 'danger',
			});

			return null;
		}

		const fdsFieldsOrder =
			storedFDSFields?.[0]?.[OBJECT_RELATIONSHIP.FDS_VIEW_FDS_FIELD]
				?.fdsFieldsOrder;

		if (fdsFieldsOrder) {
			fdsFieldsOrderRef.current = fdsFieldsOrder;

			const storedOrderedFDSFieldIds = fdsFieldsOrder.split(',');

			const orderedFDSFields: Array<IFDSField> = [];

			const orderedFDSFieldIds: Array<number> = [];

			storedOrderedFDSFieldIds.forEach((fdsFieldId: string) => {
				storedFDSFields.forEach((storedFDSField: IFDSField) => {
					if (fdsFieldId === String(storedFDSField.id)) {
						orderedFDSFields.push(storedFDSField);

						orderedFDSFieldIds.push(storedFDSField.id);
					}
				});
			});

			storedFDSFields.forEach((storedFDSField: IFDSField) => {
				if (!orderedFDSFieldIds.includes(storedFDSField.id)) {
					orderedFDSFields.push(storedFDSField);
				}
			});

			fdsFieldsOrderRef.current = orderedFDSFieldIds.join(',');

			setFDSFields(orderedFDSFields);
		}
		else {
			fdsFieldsOrderRef.current = storedFDSFields
				.map((storedFDSField: IFDSField) => storedFDSField.id)
				.join(',');

			setFDSFields(storedFDSFields);
		}
	};

	const handleDelete = ({item}: {item: IFDSField}) => {
		openModal({
			bodyHTML: Liferay.Language.get(
				'are-you-sure-you-want-to-delete-this-field?-fragments-using-it-will-be-affected'
			),
			buttons: [
				{
					autoFocus: true,
					displayType: 'secondary',
					label: Liferay.Language.get('cancel'),
					type: 'cancel',
				},
				{
					displayType: 'warning',
					label: Liferay.Language.get('delete'),
					onClick: async ({
						processClose,
					}: {
						processClose: Function;
					}) => {
						processClose();

						const url = `${API_URL.FDS_FIELDS}/${item.id}`;

						const response = await fetch(url, {method: 'DELETE'});

						if (!response.ok) {
							openToast({
								message: Liferay.Language.get(
									'your-request-failed-to-complete'
								),
								type: 'danger',
							});

							return;
						}

						openToast({
							message: Liferay.Language.get(
								'your-request-completed-successfully'
							),
							type: 'success',
						});

						setFDSFields(
							fdsFields?.filter(
								(fdsField: IFDSField) => fdsField.id !== item.id
							) || []
						);
					},
				},
			],
			status: 'warning',
			title: Liferay.Language.get('delete-filter'),
		});
	};

	const updateFDSFieldsOrder = async () => {
		const body = {
			fdsFieldsOrder: fdsFieldsOrderRef.current,
		};

		const response = await fetch(
			`${API_URL.FDS_VIEWS}/by-external-reference-code/${fdsView.externalReferenceCode}`,
			{
				body: JSON.stringify(body),
				headers: {
					'Accept': 'application/json',
					'Content-Type': 'application/json',
				},
				method: 'PATCH',
			}
		);

		if (!response.ok) {
			openToast({
				message: Liferay.Language.get(
					'your-request-failed-to-complete'
				),
				type: 'danger',
			});

			return null;
		}

		const responseJSON = await response.json();

		const fdsFieldsOrder = responseJSON?.fdsFieldsOrder;

		if (fdsFieldsOrder && fdsFieldsOrder === fdsFieldsOrderRef.current) {
			openToast({
				message: Liferay.Language.get(
					'your-request-completed-successfully'
				),
				type: 'success',
			});
		}
		else {
			openToast({
				message: Liferay.Language.get(
					'your-request-failed-to-complete'
				),
				type: 'danger',
			});
		}
	};

	useEffect(() => {
		getFDSFields();

		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, []);

	const onCreationButtonClick = () =>
		openModal({
			className: 'overflow-auto',
			contentComponent: ({closeModal}: {closeModal: Function}) => (
				<SaveFDSFieldsModalContent
					closeModal={closeModal}
					fdsFields={fdsFields || []}
					fdsView={fdsView}
					namespace={namespace}
					onSave={({
						createdFDSFields,
						deletedFDSFieldsIds,
					}: {
						createdFDSFields: Array<IFDSField>;
						deletedFDSFieldsIds: Array<number>;
					}) => {
						const newFDSFields: Array<IFDSField> = [];

						fdsFields?.forEach((fdsField) => {
							if (!deletedFDSFieldsIds.includes(fdsField.id)) {
								newFDSFields.push(fdsField);
							}
						});

						createdFDSFields.forEach((fdsField) => {
							newFDSFields.push(fdsField);
						});

						setFDSFields(newFDSFields);
					}}
					saveFDSFieldsURL={saveFDSFieldsURL}
				/>
			),
		});

	const onEditFDSField = ({editedFDSField}: {editedFDSField: IFDSField}) => {
		setFDSFields(
			fdsFields?.map((fdsField) => {
				if (fdsField.id === editedFDSField.id) {
					return editedFDSField;
				}

				return fdsField;
			}) || null
		);
	};

	return (
		<ClayLayout.ContainerFluid>
			{fdsFields ? (
				<OrderableTable
					actions={[
						{
							icon: 'pencil',
							label: Liferay.Language.get('edit'),
							onClick: ({item}: {item: IFDSField}) => {
								openModal({
									className: 'overflow-auto',
									contentComponent: ({
										closeModal,
									}: {
										closeModal: Function;
									}) => (
										<EditFDSFieldModalContent
											closeModal={closeModal}
											fdsClientExtensionCellRenderers={
												fdsClientExtensionCellRenderers
											}
											fdsField={item}
											namespace={namespace}
											onSave={onEditFDSField}
										/>
									),
								});
							},
						},
						{
							icon: 'trash',
							label: Liferay.Language.get('delete'),
							onClick: handleDelete,
						},
					]}
					fields={[
						{
							label: Liferay.Language.get('name'),
							name: 'name',
						},
						{
							label: Liferay.Language.get('label'),
							name: 'label',
						},
						{
							label: Liferay.Language.get('type'),
							name: 'type',
						},
						{
							label: Liferay.Language.get('cell-renderer'),
							name: 'renderer',
						},
						{
							label: Liferay.Language.get('sortable'),
							name: 'sortable',
						},
					]}
					items={fdsFields}
					noItemsButtonLabel={Liferay.Language.get('add-fields')}
					noItemsDescription={Liferay.Language.get(
						'add-fields-to-show-in-your-view'
					)}
					noItemsTitle={Liferay.Language.get('no-fields-added-yet')}
					onCancelButtonClick={() => navigate(fdsViewsURL)}
					onCreationButtonClick={onCreationButtonClick}
					onOrderChange={({
						orderedItems,
					}: {
						orderedItems: Array<IFDSField>;
					}) => {
						fdsFieldsOrderRef.current = orderedItems
							.map((item) => item.id)
							.join(',');
					}}
					onSaveButtonClick={() => updateFDSFieldsOrder()}
					title={Liferay.Language.get('fields')}
				/>
			) : (
				<ClayLoadingIndicator />
			)}
		</ClayLayout.ContainerFluid>
	);
};

export default Fields;
