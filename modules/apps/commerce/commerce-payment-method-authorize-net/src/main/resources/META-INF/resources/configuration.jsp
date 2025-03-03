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
AuthorizeNetGroupServiceConfiguration authorizeNetGroupServiceConfiguration = (AuthorizeNetGroupServiceConfiguration)request.getAttribute(AuthorizeNetGroupServiceConfiguration.class.getName());
%>

<portlet:actionURL name="/commerce_payment_methods/edit_authorize_net_commerce_payment_method_configuration" var="editCommercePaymentMethodActionURL" />

<aui:form action="<%= editCommercePaymentMethodActionURL %>" cssClass="container-fluid container-fluid-max-xl" method="post" name="fm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
	<aui:input name="commerceChannelId" type="hidden" value='<%= ParamUtil.getLong(request, "commerceChannelId") %>' />
	<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />

	<commerce-ui:panel>
		<commerce-ui:info-box
			title='<%= LanguageUtil.get(request, "authentication") %>'
		>
			<aui:input label="api-login-id" name="settings--apiLoginId--" value="<%= authorizeNetGroupServiceConfiguration.apiLoginId() %>" />

			<aui:input label="transaction-key" name="settings--transactionKey--" value="<%= authorizeNetGroupServiceConfiguration.transactionKey() %>" />

			<aui:select name="settings--environment--">

				<%
				for (String environment : AuthorizeNetCommercePaymentMethodConstants.ENVIRONMENTS) {
				%>

					<aui:option label="<%= environment %>" selected="<%= environment.equals(authorizeNetGroupServiceConfiguration.environment()) %>" value="<%= environment %>" />

				<%
				}
				%>

			</aui:select>
		</commerce-ui:info-box>

		<commerce-ui:info-box
			title='<%= LanguageUtil.get(request, "display") %>'
		>
			<aui:input checked="<%= authorizeNetGroupServiceConfiguration.showBankAccount() %>" label="show-bank-account" name="settings--showBankAccount--" type="checkbox" />

			<aui:input checked="<%= authorizeNetGroupServiceConfiguration.showCreditCard() %>" label="show-credit-card" name="settings--showCreditCard--" type="checkbox" />

			<aui:input checked="<%= authorizeNetGroupServiceConfiguration.showStoreName() %>" label="show-store-name" name="settings--showStoreName--" type="checkbox" />
		</commerce-ui:info-box>

		<commerce-ui:info-box
			title='<%= LanguageUtil.get(request, "security") %>'
		>
			<aui:input checked="<%= authorizeNetGroupServiceConfiguration.requireCaptcha() %>" label="require-captcha" name="settings--requireCaptcha--" type="checkbox" />

			<aui:input checked="<%= authorizeNetGroupServiceConfiguration.requireCardCodeVerification() %>" label="require-card-code-verification" name="settings--requireCardCodeVerification--" type="checkbox" />
		</commerce-ui:info-box>
	</commerce-ui:panel>

	<aui:button-row>
		<aui:button cssClass="btn-lg" type="submit" />

		<aui:button cssClass="btn-lg" href="<%= redirect %>" type="cancel" />
	</aui:button-row>
</aui:form>