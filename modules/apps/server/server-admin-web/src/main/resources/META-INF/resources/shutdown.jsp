<%--
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
--%>

<%@ include file="/init.jsp" %>

<liferay-ui:error key="shutdownMinutes" message="please-enter-the-number-of-minutes" />

<c:choose>
	<c:when test="<%= ShutdownUtil.isInProcess() %>">
		<aui:button cssClass="save-server-button" data-cmd="shutdown" value="cancel-shutdown" />
	</c:when>
	<c:otherwise>
		<div class="sheet">
			<div class="panel-group panel-group-flush">
				<aui:input label="number-of-minutes" name="minutes" required="<%= true %>" size="3" type="text">
					<aui:validator name="digits" />
					<aui:validator name="min">1</aui:validator>
				</aui:input>

				<aui:input cssClass="lfr-textarea-container" label="custom-message" name="message" type="textarea" />

				<aui:button cssClass="save-server-button" data-cmd="shutdown" primary="<%= true %>" value="shutdown" />
			</div>
		</div>
	</c:otherwise>
</c:choose>