/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */
import {Liferay} from '../services/liferay';

export default function getDateCustomFormat(rawDate, format) {
	if (rawDate) {
		const date =
			typeof rawDate === 'string'
				? new Date(rawDate.substring(0, rawDate.length - 1))
				: rawDate;

		return date.toLocaleDateString(
			Liferay.ThemeDisplay.getBCP47LanguageId(),
			format
		);
	}
}
