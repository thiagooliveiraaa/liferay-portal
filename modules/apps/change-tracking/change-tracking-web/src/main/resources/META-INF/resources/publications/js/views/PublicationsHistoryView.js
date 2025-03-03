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

import ClayIcon from '@clayui/icon';
import ClayList from '@clayui/list';
import ClaySticker from '@clayui/sticker';
import ClayTable from '@clayui/table';
import React, {useEffect, useState} from 'react';

import PublicationStatus from '../components/PublicationStatus';

const renderUserPortrait = (entry, userInfo) => {
	const user = userInfo[entry.userId];

	return (
		<ClaySticker
			className={`sticker-user-icon ${
				user.portraitURL ? '' : 'user-icon-color-' + (entry.userId % 10)
			}`}
			data-tooltip-align="top"
			title={user.userName}
		>
			{user.portraitURL ? (
				<div className="sticker-overlay">
					<img className="sticker-img" src={user.portraitURL} />
				</div>
			) : (
				<ClayIcon symbol="user" />
			)}
		</ClaySticker>
	);
};

const renderPublicationInfo = (entry, published) => {
	return (
		<a
			className={
				!entry.hasViewPermission || !published
					? 'btn btn-unstyled disabled'
					: ''
			}
			data-tooltip-align="top"
			href={entry.viewURL}
			title={
				!entry.hasViewPermission
					? Liferay.Language.get(
							'you-do-not-have-permission-to-view-this-publication'
					  )
					: ''
			}
		>
			<div className="publication-name">{entry.name}</div>

			{entry.description && (
				<div className="publication-description">
					{entry.description}
				</div>
			)}
		</a>
	);
};

const PublicationsHistoryListItem = ({entry, spritemap, userInfo}) => {
	const [publishedValue, setPublishedValue] = useState(entry.published);

	useEffect(() => {
		if (entry.published !== publishedValue) {
			setPublishedValue(entry.published);
		}
	}, [entry, publishedValue]);

	let title = '';

	if (!entry.hasRevertPermission || !entry.hasViewPermission) {
		title = Liferay.Language.get(
			'you-do-not-have-permission-to-revert-this-publication'
		);
	}
	else if (entry.expired && publishedValue) {
		title = Liferay.Language.get(
			'this-publication-was-created-on-a-previous-liferay-version.-you-cannot-revert-it'
		);
	}

	return (
		<ClayList.Item flex>
			<ClayList.ItemField>
				{renderUserPortrait(entry, userInfo)}
			</ClayList.ItemField>

			<ClayList.ItemField expand>
				<ClayList.ItemText>
					{renderPublicationInfo(entry, publishedValue)}
				</ClayList.ItemText>

				<ClayList.ItemText>
					<PublicationStatus
						dataURL={entry.statusURL}
						displayType={entry.displayType}
						label={entry.label}
						spritemap={spritemap}
						updateStatus={(displayType, label, published) => {
							entry.displayType = displayType;
							entry.published = published;
							entry.label = label;
							setPublishedValue(published);
						}}
					/>
				</ClayList.ItemText>
			</ClayList.ItemField>

			<ClayList.ItemField>
				<div data-tooltip-align="top" title={title}>
					<a
						className={`${
							entry.expired ||
							!entry.hasRevertPermission ||
							!entry.hasViewPermission ||
							!publishedValue
								? 'disabled'
								: ''
						} btn btn-secondary btn-sm`}
						href={entry.revertURL}
					>
						{Liferay.Language.get('revert')}
					</a>
				</div>
			</ClayList.ItemField>
		</ClayList.Item>
	);
};

const PublicationsHistoryTableRow = ({entry, spritemap, userInfo}) => {
	const [publishedValue, setPublishedValue] = useState(entry.published);

	useEffect(() => {
		if (entry.published !== publishedValue) {
			setPublishedValue(entry.published);
		}
	}, [entry, publishedValue]);

	let title = '';

	if (!entry.hasRevertPermission || !entry.hasViewPermission) {
		title = Liferay.Language.get(
			'you-do-not-have-permission-to-revert-this-publication'
		);
	}
	else if (entry.expired && publishedValue) {
		title = Liferay.Language.get(
			'this-publication-was-created-on-a-previous-liferay-version.-you-cannot-revert-it'
		);
	}

	return (
		<ClayTable.Row>
			<ClayTable.Cell className="table-cell-expand">
				{renderPublicationInfo(entry, publishedValue)}
			</ClayTable.Cell>

			<ClayTable.Cell className="table-cell-expand-smaller">
				{entry.timeDescription}
			</ClayTable.Cell>

			<ClayTable.Cell className="table-cell-expand-smallest">
				{renderUserPortrait(entry, userInfo)}
			</ClayTable.Cell>

			<ClayTable.Cell className="table-cell-expand-smaller">
				<PublicationStatus
					dataURL={entry.statusURL}
					displayType={entry.displayType}
					label={entry.label}
					spritemap={spritemap}
					updateStatus={(displayType, label, published) => {
						entry.displayType = displayType;
						entry.published = published;
						entry.label = label;
						setPublishedValue(published);
					}}
				/>
			</ClayTable.Cell>

			<ClayTable.Cell className="table-cell-expand-smallest">
				<div data-tooltip-align="top" title={title}>
					<a
						className={`${
							entry.expired ||
							!entry.hasRevertPermission ||
							!entry.hasViewPermission ||
							!publishedValue
								? 'disabled'
								: ''
						} btn btn-secondary btn-sm`}
						href={entry.revertURL}
					>
						{Liferay.Language.get('revert')}
					</a>
				</div>
			</ClayTable.Cell>
		</ClayTable.Row>
	);
};

export default function PublicationsHistoryView({
	displayStyle,
	entries,
	spritemap,
	userInfo,
}) {
	if (displayStyle === 'list') {
		const rows = [];

		for (let i = 0; i < entries.length; i++) {
			rows.push(
				<PublicationsHistoryTableRow
					entry={entries[i]}
					spritemap={spritemap}
					userInfo={userInfo}
				/>
			);
		}

		return (
			<ClayTable
				className="publications-table"
				headingNoWrap
				hover={false}
			>
				<ClayTable.Head>
					<ClayTable.Row>
						<ClayTable.Cell
							className="table-cell-expand"
							headingCell
						>
							{Liferay.Language.get('publication')}
						</ClayTable.Cell>

						<ClayTable.Cell
							className="table-cell-expand-smaller"
							headingCell
						>
							{Liferay.Language.get('published-date')}
						</ClayTable.Cell>

						<ClayTable.Cell
							className="table-cell-expand-smallest text-center"
							headingCell
						>
							{Liferay.Language.get('published-by')}
						</ClayTable.Cell>

						<ClayTable.Cell
							className="table-cell-expand-smaller"
							headingCell
						>
							{Liferay.Language.get('status')}
						</ClayTable.Cell>

						<ClayTable.Cell
							className="table-cell-expand-smallest"
							headingCell
						/>
					</ClayTable.Row>
				</ClayTable.Head>

				<ClayTable.Body>{rows}</ClayTable.Body>
			</ClayTable>
		);
	}

	const items = [];

	for (let i = 0; i < entries.length; i++) {
		items.push(
			<PublicationsHistoryListItem
				entry={entries[i]}
				spritemap={spritemap}
				userInfo={userInfo}
			/>
		);
	}

	return <ClayList className="publications-table">{items}</ClayList>;
}
