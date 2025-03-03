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

<%
Layout selLayout = layoutsAdminDisplayContext.getSelLayout();

UnicodeProperties layoutTypeSettingsUnicodeProperties = selLayout.getTypeSettingsProperties();
%>

<liferay-ui:error-marker
	key="<%= WebKeys.ERROR_SECTION %>"
	value="advanced"
/>

<aui:model-context bean="<%= selLayout %>" model="<%= Layout.class %>" />

<liferay-ui:error exception="<%= ImageTypeException.class %>" message="please-enter-a-file-with-a-valid-file-type" />

<%
Group group = layoutsAdminDisplayContext.getGroup();
%>

<c:if test="<%= !group.isLayoutPrototype() %>">
	<div class="alert alert-warning layout-prototype-info-message <%= selLayout.isLayoutPrototypeLinkActive() ? StringPool.BLANK : "hide" %>">
		<liferay-ui:message arguments='<%= new String[] {LanguageUtil.get(request, "inherit-changes"), "General"} %>' key="some-page-settings-are-unavailable-because-x-is-enabled" translateArguments="<%= false %>" />
	</div>

	<aui:input cssClass="propagatable-field" disabled="<%= selLayout.isLayoutPrototypeLinkActive() %>" helpMessage="query-string-help" label="query-string" name="TypeSettingsProperties--query-string--" size="30" type="text" value='<%= GetterUtil.getString(layoutTypeSettingsUnicodeProperties.getProperty("query-string")) %>' />
</c:if>

<%
String targetType = GetterUtil.getString(layoutTypeSettingsUnicodeProperties.getProperty("targetType"));
%>

<div class="d-flex">
	<aui:select cssClass="propagatable-field" id="targetType" label="target-type" name="TypeSettingsProperties--targetType--" wrapperCssClass="c-mr-3 w-50">
		<aui:option label="specific-frame" selected='<%= !Objects.equals(targetType, "useNewTab") %>' value="" />
		<aui:option label="new-tab" selected='<%= Objects.equals(targetType, "useNewTab") %>' value="useNewTab" />
	</aui:select>

	<aui:input cssClass="propagatable-field" disabled="<%= selLayout.isLayoutPrototypeLinkActive() %>" id="target" label="target" name="TypeSettingsProperties--target--" size="15" type="text" value='<%= GetterUtil.getString(layoutTypeSettingsUnicodeProperties.getProperty("target")) %>' wrapperCssClass='<%= Objects.equals(targetType, "useNewTab") ? "hide" : "w-50" %>' />
</div>

<liferay-frontend:logo-selector
	currentLogoURL='<%= (selLayout.getIconImageId() == 0) ? themeDisplay.getPathThemeImages() + "/spacer.png" : themeDisplay.getPathImage() + "/logo?img_id=" + selLayout.getIconImageId() + "&t=" + WebServerServletTokenUtil.getToken(selLayout.getIconImageId()) %>'
	defaultLogoURL='<%= themeDisplay.getPathThemeImages() + "/spacer.png" %>'
	description='<%= LanguageUtil.get(request, "this-icon-will-be-shown-in-the-navigation-menu") %>'
	label='<%= LanguageUtil.get(request, "icon") %>'
/>

<liferay-frontend:component
	componentId='<%= liferayPortletResponse.getNamespace() + "addLayout" %>'
	context='<%=
		HashMapBuilder.<String, Object>put(
			"defaultTarget", Objects.equals(targetType, "useNewTab") ? StringPool.BLANK : GetterUtil.getString(layoutTypeSettingsUnicodeProperties.getProperty("target"))
		).put(
			"namespace", liferayPortletResponse.getNamespace()
		).build()
	%>'
	module="js/Advanced"
/>