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

package com.liferay.saml.opensaml.integration.internal.util;

import com.liferay.portal.kernel.util.HashMapBuilder;

import java.lang.reflect.Method;

import net.shibboleth.utilities.java.support.xml.BasicParserPool;

import org.apache.xml.security.stax.ext.XMLSecurityConstants;

import org.opensaml.core.config.ConfigurationService;
import org.opensaml.core.config.InitializationException;
import org.opensaml.core.config.InitializationService;
import org.opensaml.core.xml.config.XMLObjectProviderRegistry;
import org.opensaml.xmlsec.signature.support.SignatureValidator;
import org.opensaml.xmlsec.signature.support.Signer;

/**
 * @author Shuyang Zhou
 */
public class ConfigurationServiceBootstrapUtil {

	public static <T> T get(Class<T> configurationClass) {
		return ConfigurationService.get(configurationClass);
	}

	public static <T> void register(
		Class<T> configurationClass, T configuration) {

		ConfigurationService.register(configurationClass, configuration);
	}

	static {
		Thread currentThread = Thread.currentThread();

		ClassLoader classLoader = currentThread.getContextClassLoader();

		try {
			currentThread.setContextClassLoader(
				ConfigurationServiceBootstrapUtil.class.getClassLoader());

			InitializationService.initialize();

			_initializeParserPool();

			Method method = Signer.class.getDeclaredMethod("getSignerProvider");

			method.setAccessible(true);

			method.invoke(null);

			method = SignatureValidator.class.getDeclaredMethod(
				"getSignatureValidationProvider");

			method.setAccessible(true);

			method.invoke(null);

			if (XMLSecurityConstants.xmlOutputFactory == null) {
				throw new IllegalStateException();
			}
		}
		catch (Exception exception) {
			throw new ExceptionInInitializerError(exception);
		}
		finally {
			currentThread.setContextClassLoader(classLoader);
		}
	}

	private static void _initializeParserPool() throws InitializationException {
		BasicParserPool parserPool = new BasicParserPool();

		parserPool.setBuilderFeatures(
			HashMapBuilder.put(
				"http://apache.org/xml/features/disallow-doctype-decl",
				Boolean.TRUE
			).put(
				"http://apache.org/xml/features/dom/defer-node-expansion",
				Boolean.FALSE
			).put(
				"http://javax.xml.XMLConstants/feature/secure-processing",
				Boolean.TRUE
			).put(
				"http://xml.org/sax/features/external-general-entities",
				Boolean.FALSE
			).put(
				"http://xml.org/sax/features/external-parameter-entities",
				Boolean.FALSE
			).build());

		parserPool.setDTDValidating(false);
		parserPool.setExpandEntityReferences(false);
		parserPool.setMaxPoolSize(50);
		parserPool.setNamespaceAware(true);

		try {
			parserPool.initialize();

			parserPool.getBuilder();

			XMLObjectProviderRegistry xmlObjectProviderRegistry =
				ConfigurationService.get(XMLObjectProviderRegistry.class);

			xmlObjectProviderRegistry.setParserPool(parserPool);
		}
		catch (Exception exception) {
			throw new InitializationException(
				"Unable to initialize parser pool: " + exception.getMessage(),
				exception);
		}
	}

}