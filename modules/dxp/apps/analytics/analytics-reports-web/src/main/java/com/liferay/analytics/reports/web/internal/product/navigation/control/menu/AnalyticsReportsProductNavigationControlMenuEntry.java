/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.analytics.reports.web.internal.product.navigation.control.menu;

import com.liferay.analytics.reports.constants.AnalyticsReportsWebKeys;
import com.liferay.analytics.reports.info.item.AnalyticsReportsInfoItem;
import com.liferay.analytics.reports.info.item.AnalyticsReportsInfoItemRegistry;
import com.liferay.analytics.reports.info.item.ClassNameClassPKInfoItemIdentifier;
import com.liferay.analytics.reports.info.item.provider.AnalyticsReportsInfoItemObjectProvider;
import com.liferay.analytics.reports.web.internal.constants.AnalyticsReportsPortletKeys;
import com.liferay.analytics.reports.web.internal.constants.ProductNavigationControlMenuEntryConstants;
import com.liferay.analytics.reports.web.internal.info.item.provider.AnalyticsReportsInfoItemObjectProviderRegistry;
import com.liferay.analytics.reports.web.internal.util.AnalyticsReportsUtil;
import com.liferay.analytics.settings.rest.manager.AnalyticsSettingsManager;
import com.liferay.frontend.js.loader.modules.extender.npm.NPMResolver;
import com.liferay.frontend.taglib.clay.servlet.taglib.ButtonTag;
import com.liferay.frontend.taglib.clay.servlet.taglib.IconTag;
import com.liferay.info.item.ClassPKInfoItemIdentifier;
import com.liferay.info.item.InfoItemReference;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.PortletURLFactory;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Html;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.template.react.renderer.ComponentDescriptor;
import com.liferay.portal.template.react.renderer.ReactRenderer;
import com.liferay.product.navigation.control.menu.BaseProductNavigationControlMenuEntry;
import com.liferay.product.navigation.control.menu.ProductNavigationControlMenuEntry;
import com.liferay.product.navigation.control.menu.constants.ProductNavigationControlMenuCategoryKeys;
import com.liferay.taglib.util.BodyBottomTag;

import java.io.IOException;
import java.io.Writer;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.portlet.PortletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Sarai Díaz
 */
@Component(
	property = {
		"product.navigation.control.menu.category.key=" + ProductNavigationControlMenuCategoryKeys.USER,
		"product.navigation.control.menu.entry.order:Integer=400"
	},
	service = ProductNavigationControlMenuEntry.class
)
public class AnalyticsReportsProductNavigationControlMenuEntry
	extends BaseProductNavigationControlMenuEntry {

	@Override
	public String getLabel(Locale locale) {
		return null;
	}

	@Override
	public String getURL(HttpServletRequest httpServletRequest) {
		return null;
	}

	@Override
	public boolean includeBody(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException {

		BodyBottomTag bodyBottomTag = new BodyBottomTag();

		bodyBottomTag.setOutputKey("analyticsReportsPanel");

		try {
			bodyBottomTag.doBodyTag(
				httpServletRequest, httpServletResponse,
				this::_processBodyBottomTagBody);
		}
		catch (JspException jspException) {
			throw new IOException(jspException);
		}

		return true;
	}

	@Override
	public boolean includeIcon(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException {

		Map<String, String> values = new HashMap<>();

		if (isPanelStateOpen(
				httpServletRequest,
				ProductNavigationControlMenuEntryConstants.
					SESSION_CLICKS_KEY)) {

			values.put("cssClass", "active");
		}
		else {
			values.put("cssClass", StringPool.BLANK);
		}

		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			_portal.getLocale(httpServletRequest), getClass());

		values.put(
			"title",
			_html.escape(_language.get(resourceBundle, "content-performance")));

		IconTag iconTag = new IconTag();

		iconTag.setCssClass("icon-monospaced");
		iconTag.setSymbol("analytics");

		try {
			values.put(
				"iconTag",
				iconTag.doTagAsString(httpServletRequest, httpServletResponse));
		}
		catch (JspException jspException) {
			throw new IOException(jspException);
		}

		values.put("portletNamespace", _portletNamespace);

		Writer writer = httpServletResponse.getWriter();

		writer.write(StringUtil.replace(_ICON_TMPL_CONTENT, "${", "}", values));

		return true;
	}

	@Override
	public boolean isShow(HttpServletRequest httpServletRequest)
		throws PortalException {

		InfoItemReference infoItemReference = _getInfoItemReference(
			httpServletRequest);

		AnalyticsReportsInfoItemObjectProvider<Object>
			analyticsReportsInfoItemObjectProvider =
				(AnalyticsReportsInfoItemObjectProvider<Object>)
					_analyticsReportsInfoItemObjectProviderRegistry.
						getAnalyticsReportsInfoItemObjectProvider(
							infoItemReference.getClassName());

		if (analyticsReportsInfoItemObjectProvider == null) {
			return false;
		}

		Object analyticsReportsInfoItemObject =
			analyticsReportsInfoItemObjectProvider.
				getAnalyticsReportsInfoItemObject(infoItemReference);

		if (analyticsReportsInfoItemObject == null) {
			return false;
		}

		AnalyticsReportsInfoItem<Object> analyticsReportsInfoItem =
			(AnalyticsReportsInfoItem<Object>)
				_analyticsReportsInfoItemRegistry.getAnalyticsReportsInfoItem(
					infoItemReference.getClassName());

		if ((analyticsReportsInfoItem == null) ||
			!analyticsReportsInfoItem.isShow(analyticsReportsInfoItemObject)) {

			return false;
		}

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		try {
			if (!AnalyticsReportsUtil.isShowAnalyticsReportsPanel(
					_analyticsSettingsManager, themeDisplay.getCompanyId(),
					httpServletRequest)) {

				return false;
			}
		}
		catch (PortalException portalException) {
			throw portalException;
		}
		catch (Exception exception) {
			_log.error(exception);

			return false;
		}

		return super.isShow(httpServletRequest);
	}

	@Activate
	protected void activate() {
		_portletNamespace = _portal.getPortletNamespace(
			AnalyticsReportsPortletKeys.ANALYTICS_REPORTS);
	}

	private String _getAnalyticsReportsURL(
		HttpServletRequest httpServletRequest) {

		InfoItemReference infoItemReference = _getInfoItemReference(
			httpServletRequest);

		if (infoItemReference.getInfoItemIdentifier() instanceof
				ClassNameClassPKInfoItemIdentifier) {

			ClassNameClassPKInfoItemIdentifier
				classNameClassPKInfoItemIdentifier =
					(ClassNameClassPKInfoItemIdentifier)
						infoItemReference.getInfoItemIdentifier();

			return PortletURLBuilder.create(
				_portletURLFactory.create(
					httpServletRequest,
					AnalyticsReportsPortletKeys.ANALYTICS_REPORTS,
					PortletRequest.RESOURCE_PHASE)
			).setParameter(
				"className", infoItemReference.getClassName()
			).setParameter(
				"classPK", classNameClassPKInfoItemIdentifier.getClassPK()
			).setParameter(
				"classTypeName",
				classNameClassPKInfoItemIdentifier.getClassName()
			).setParameter(
				"p_p_resource_id", "/analytics_reports/get_data"
			).buildString();
		}
		else if (infoItemReference.getInfoItemIdentifier() instanceof
					ClassPKInfoItemIdentifier) {

			return PortletURLBuilder.create(
				_portletURLFactory.create(
					httpServletRequest,
					AnalyticsReportsPortletKeys.ANALYTICS_REPORTS,
					PortletRequest.RESOURCE_PHASE)
			).setParameter(
				"className", infoItemReference.getClassName()
			).setParameter(
				"classPK", infoItemReference.getClassPK()
			).setParameter(
				"p_p_resource_id", "/analytics_reports/get_data"
			).buildString();
		}

		return StringPool.BLANK;
	}

	private InfoItemReference _getInfoItemReference(
		HttpServletRequest httpServletRequest) {

		InfoItemReference infoItemReference =
			(InfoItemReference)httpServletRequest.getAttribute(
				AnalyticsReportsWebKeys.ANALYTICS_INFO_ITEM_REFERENCE);

		if (infoItemReference == null) {
			ThemeDisplay themeDisplay =
				(ThemeDisplay)httpServletRequest.getAttribute(
					WebKeys.THEME_DISPLAY);

			return new InfoItemReference(
				Layout.class.getName(), themeDisplay.getPlid());
		}

		return infoItemReference;
	}

	private void _processBodyBottomTagBody(PageContext pageContext) {
		try {
			HttpServletRequest httpServletRequest =
				(HttpServletRequest)pageContext.getRequest();

			ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
				_portal.getLocale(httpServletRequest), getClass());

			pageContext.setAttribute("resourceBundle", resourceBundle);

			JspWriter jspWriter = pageContext.getOut();

			StringBundler sb = new StringBundler(24);

			sb.append("<div class=\"");

			if (isPanelStateOpen(
					httpServletRequest,
					ProductNavigationControlMenuEntryConstants.
						SESSION_CLICKS_KEY)) {

				sb.append("lfr-has-analytics-reports-panel open-admin-panel ");
			}

			sb.append(
				StringBundler.concat(
					"cadmin d-print-none lfr-admin-panel ",
					"lfr-product-menu-panel lfr-analytics-reports-panel ",
					"sidenav-fixed sidenav-menu-slider sidenav-right\" id=\""));
			sb.append(_portletNamespace);
			sb.append("analyticsReportsPanelId\" ");
			sb.append("tabindex=\"-1\">");
			sb.append("<div class=\"sidebar sidebar-light sidenav-menu ");
			sb.append("sidebar-sm\">");
			sb.append("<div class=\"lfr-analytics-reports-sidebar\" ");
			sb.append("id=\"analyticsReportsSidebar\">");
			sb.append("<div class=\"d-flex justify-content-between p-3 ");
			sb.append("sidebar-header\">");
			sb.append("<h1 class=\"sr-only\">");
			sb.append(
				_language.get(httpServletRequest, "content-performance-panel"));
			sb.append("</h1>");
			sb.append("<span class=\"font-weight-bold\">");
			sb.append(_language.get(httpServletRequest, "content-performance"));
			sb.append("</span>");

			ButtonTag buttonTag = new ButtonTag();

			buttonTag.setCssClass("close sidenav-close");
			buttonTag.setDisplayType("unstyled");
			buttonTag.setDynamicAttribute(
				StringPool.BLANK, "aria-label",
				_language.get(
					(HttpServletRequest)pageContext.getRequest(), "close"));
			buttonTag.setIcon("times");

			sb.append(buttonTag.doTagAsString(pageContext));

			sb.append("</div>");
			sb.append("<div class=\"sidebar-body\">");
			sb.append("<span aria-hidden=\"true\" ");
			sb.append("className=\"loading-animation ");
			sb.append("loading-animation-sm\" />");

			jspWriter.write(sb.toString());

			_reactRenderer.renderReact(
				new ComponentDescriptor(
					_npmResolver.resolveModuleName("analytics-reports-web") +
						"/js/AnalyticsReportsApp"),
				HashMapBuilder.<String, Object>put(
					"context",
					HashMapBuilder.<String, Object>put(
						"analyticsReportsDataURL",
						_getAnalyticsReportsURL(httpServletRequest)
					).put(
						"isPanelStateOpen",
						isPanelStateOpen(
							httpServletRequest,
							ProductNavigationControlMenuEntryConstants.
								SESSION_CLICKS_KEY)
					).build()
				).put(
					"portletNamespace", _portletNamespace
				).build(),
				httpServletRequest, jspWriter);

			jspWriter.write("</div></div></div></div>");
		}
		catch (Exception exception) {
			ReflectionUtil.throwException(exception);
		}
	}

	private static final String _ICON_TMPL_CONTENT = StringUtil.read(
		AnalyticsReportsProductNavigationControlMenuEntry.class, "icon.tmpl");

	private static final Log _log = LogFactoryUtil.getLog(
		AnalyticsReportsProductNavigationControlMenuEntry.class);

	@Reference
	private AnalyticsReportsInfoItemObjectProviderRegistry
		_analyticsReportsInfoItemObjectProviderRegistry;

	@Reference
	private AnalyticsReportsInfoItemRegistry _analyticsReportsInfoItemRegistry;

	@Reference
	private AnalyticsSettingsManager _analyticsSettingsManager;

	@Reference
	private Html _html;

	@Reference
	private Language _language;

	@Reference
	private NPMResolver _npmResolver;

	@Reference
	private Portal _portal;

	private String _portletNamespace;

	@Reference
	private PortletURLFactory _portletURLFactory;

	@Reference
	private ReactRenderer _reactRenderer;

}