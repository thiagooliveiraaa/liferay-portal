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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/clay" prefix="clay" %><%@
taglib uri="http://liferay.com/tld/ddm" prefix="liferay-ddm" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ page import="com.liferay.petra.string.StringPool" %><%@
page import="com.liferay.portal.kernel.util.HashMapBuilder" %><%@
page import="com.liferay.portal.kernel.util.HtmlUtil" %><%@
page import="com.liferay.portal.kernel.util.WebKeys" %><%@
page import="com.liferay.portal.search.web.internal.facet.display.context.BucketDisplayContext" %><%@
page import="com.liferay.portal.search.web.internal.facet.display.context.UserSearchFacetDisplayContext" %><%@
page import="com.liferay.portal.search.web.internal.user.facet.configuration.UserFacetPortletInstanceConfiguration" %><%@
page import="com.liferay.portal.search.web.internal.user.facet.portlet.UserFacetPortlet" %>

<portlet:defineObjects />

<%
UserSearchFacetDisplayContext userSearchFacetDisplayContext = (UserSearchFacetDisplayContext)java.util.Objects.requireNonNull(request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT));

if (userSearchFacetDisplayContext.isRenderNothing()) {
	return;
}

UserFacetPortletInstanceConfiguration userFacetPortletInstanceConfiguration = userSearchFacetDisplayContext.getUserFacetPortletInstanceConfiguration();
%>

<c:choose>
	<c:when test="<%= userSearchFacetDisplayContext.isRenderNothing() %>">
		<aui:input autocomplete="off" name="<%= HtmlUtil.escapeAttribute(userSearchFacetDisplayContext.getParameterName()) %>" type="hidden" value="<%= userSearchFacetDisplayContext.getParameterValue() %>" />
	</c:when>
	<c:otherwise>
		<aui:form action="#" method="post" name="fm">
			<aui:input autocomplete="off" name="<%= HtmlUtil.escapeAttribute(userSearchFacetDisplayContext.getParameterName()) %>" type="hidden" value="<%= userSearchFacetDisplayContext.getParameterValue() %>" />
			<aui:input cssClass="facet-parameter-name" name="facet-parameter-name" type="hidden" value="<%= userSearchFacetDisplayContext.getParameterName() %>" />
			<aui:input cssClass="start-parameter-name" name="start-parameter-name" type="hidden" value="<%= userSearchFacetDisplayContext.getPaginationStartParameterName() %>" />

			<liferay-ddm:template-renderer
				className="<%= UserFacetPortlet.class.getName() %>"
				contextObjects='<%=
					HashMapBuilder.<String, Object>put(
						"namespace", liferayPortletResponse.getNamespace()
					).put(
						"userSearchFacetDisplayContext", userSearchFacetDisplayContext
					).build()
				%>'
				displayStyle="<%= userFacetPortletInstanceConfiguration.displayStyle() %>"
				displayStyleGroupId="<%= userSearchFacetDisplayContext.getDisplayStyleGroupId() %>"
				entries="<%= userSearchFacetDisplayContext.getBucketDisplayContexts() %>"
			>
				<liferay-ui:panel-container
					extended="<%= true %>"
					id='<%= liferayPortletResponse.getNamespace() + "facetUserPanelContainer" %>'
					markupView="lexicon"
					persistState="<%= true %>"
				>
					<liferay-ui:panel
						collapsible="<%= true %>"
						cssClass="search-facet"
						id='<%= liferayPortletResponse.getNamespace() + "facetUserPanel" %>'
						markupView="lexicon"
						persistState="<%= true %>"
						title="user"
					>
						<c:if test="<%= !userSearchFacetDisplayContext.isNothingSelected() %>">
							<clay:button
								cssClass="btn-unstyled c-mb-4 facet-clear-btn"
								displayType="link"
								id='<%= liferayPortletResponse.getNamespace() + "facetUserClear" %>'
								onClick="Liferay.Search.FacetUtil.clearSelections(event);"
							>
								<strong><liferay-ui:message key="clear" /></strong>
							</clay:button>
						</c:if>

						<ul class="list-unstyled">

							<%
							int i = 0;

							for (BucketDisplayContext bucketDisplayContext : userSearchFacetDisplayContext.getBucketDisplayContexts()) {
								i++;
							%>

								<li class="facet-value">
									<div class="custom-checkbox custom-control">
										<label class="facet-checkbox-label" for="<portlet:namespace />term_<%= i %>">
											<input
												autocomplete="off"
												<%= bucketDisplayContext.isSelected() ? "checked" : StringPool.BLANK %>
												class="custom-control-input facet-term"
												data-term-id="<%= HtmlUtil.escapeAttribute(bucketDisplayContext.getBucketText()) %>"
												disabled
												id="<portlet:namespace />term_<%= i %>"name="<portlet:namespace />term_<%= i %>"
												onChange="Liferay.Search.FacetUtil.changeSelection(event);"
												type="checkbox"
											/>

											<span class="custom-control-label term-name <%= bucketDisplayContext.isSelected() ? "facet-term-selected" : "facet-term-unselected" %>">
												<span class="custom-control-label-text">
													<c:choose>
														<c:when test="<%= bucketDisplayContext.isSelected() %>">
															<strong><%= HtmlUtil.escape(bucketDisplayContext.getBucketText()) %></strong>
														</c:when>
														<c:otherwise>
															<%= HtmlUtil.escape(bucketDisplayContext.getBucketText()) %>
														</c:otherwise>
													</c:choose>
												</span>
											</span>

											<c:if test="<%= bucketDisplayContext.isFrequencyVisible() %>">
												<small class="term-count">
													(<%= bucketDisplayContext.getFrequency() %>)
												</small>
											</c:if>
										</label>
									</div>
								</li>

							<%
							}
							%>

						</ul>
					</liferay-ui:panel>
				</liferay-ui:panel-container>
			</liferay-ddm:template-renderer>
		</aui:form>
	</c:otherwise>
</c:choose>

<aui:script use="liferay-search-facet-util">
	Liferay.Search.FacetUtil.enableInputs(
		document.querySelectorAll('#<portlet:namespace />fm .facet-term')
	);
</aui:script>