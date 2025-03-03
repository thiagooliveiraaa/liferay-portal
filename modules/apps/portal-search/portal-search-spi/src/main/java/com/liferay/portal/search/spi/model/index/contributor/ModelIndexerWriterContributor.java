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

package com.liferay.portal.search.spi.model.index.contributor;

import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.search.batch.BatchIndexingActionable;
import com.liferay.portal.search.spi.model.index.contributor.helper.IndexerWriterMode;
import com.liferay.portal.search.spi.model.index.contributor.helper.ModelIndexerWriterDocumentHelper;

/**
 * @author Michael C. Han
 */
public interface ModelIndexerWriterContributor<T extends BaseModel<?>> {

	public void customize(
		BatchIndexingActionable batchIndexingActionable,
		ModelIndexerWriterDocumentHelper modelIndexerWriterDocumentHelper);

	public BatchIndexingActionable getBatchIndexingActionable();

	public long getCompanyId(T baseModel);

	public default IndexerWriterMode getIndexerWriterMode(T baseModel) {
		return null;
	}

	public default void modelDeleted(T baseModel) {
	}

	public default void modelIndexed(T baseModel) {
	}

}