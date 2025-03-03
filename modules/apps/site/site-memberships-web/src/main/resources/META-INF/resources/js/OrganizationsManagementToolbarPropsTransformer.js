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

import {openConfirmModal, openSelectionModal, sub} from 'frontend-js-web';

export default function propsTransformer({portletNamespace, ...otherProps}) {
	const deleteSelectedOrganizations = () => {
		openConfirmModal({
			message: Liferay.Language.get(
				'are-you-sure-you-want-to-delete-this'
			),
			onConfirm: (isConfirmed) => {
				if (isConfirmed) {
					const form = document.getElementById(
						`${portletNamespace}fm`
					);

					if (form) {
						submitForm(form);
					}
				}
			},
		});
	};

	const selectOrganizations = (itemData) => {
		openSelectionModal({
			buttonAddLabel: Liferay.Language.get('done'),
			multiple: true,
			onSelect(selectedItems) {
				if (selectedItems.length) {
					const addGroupOrganizationsFm = document.getElementById(
						`${portletNamespace}addGroupOrganizationsFm`
					);

					if (!addGroupOrganizationsFm) {
						return;
					}

					const input = document.createElement('input');

					input.name = `${portletNamespace}rowIds`;
					input.value = selectedItems
						.map((item) => {
							const selectedValue = JSON.parse(item.value);

							return selectedValue.organizationId;
						})
						.join(',');

					addGroupOrganizationsFm.appendChild(input);

					submitForm(addGroupOrganizationsFm);
				}
			},
			title: sub(
				Liferay.Language.get('assign-organizations-to-this-x'),
				itemData?.groupTypeLabel
			),
			url: itemData?.selectOrganizationsURL,
		});
	};

	return {
		...otherProps,
		onActionButtonClick(event, {item}) {
			const action = item?.data?.action;

			if (action === 'deleteSelectedOrganizations') {
				deleteSelectedOrganizations();
			}
		},
		onCreateButtonClick(event, {item}) {
			const data = item?.data;

			const action = data?.action;

			if (action === 'selectOrganizations') {
				selectOrganizations(data);
			}
		},
	};
}
