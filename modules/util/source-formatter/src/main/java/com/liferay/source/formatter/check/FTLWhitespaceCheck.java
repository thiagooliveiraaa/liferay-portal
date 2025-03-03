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

package com.liferay.source.formatter.check;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.io.unsync.UnsyncBufferedReader;
import com.liferay.portal.kernel.io.unsync.UnsyncStringReader;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.IOException;

/**
 * @author Hugo Huijser
 */
public class FTLWhitespaceCheck extends WhitespaceCheck {

	@Override
	protected String doProcess(
			String fileName, String absolutePath, String content)
		throws IOException {

		content = StringUtil.replace(content, " >\n", ">\n");

		content = _formatWhitespace(fileName, absolutePath, content);

		content = StringUtil.replace(content, "\n\n\n", "\n\n");

		return content;
	}

	@Override
	protected String formatDoubleSpace(String line) {
		String trimmedLine = StringUtil.trim(line);

		if (trimmedLine.startsWith("*")) {
			return line;
		}

		return super.formatDoubleSpace(line);
	}

	private String _formatWhitespace(
			String fileName, String absolutePath, String content)
		throws IOException {

		StringBundler sb = new StringBundler();

		try (UnsyncBufferedReader unsyncBufferedReader =
				new UnsyncBufferedReader(new UnsyncStringReader(content))) {

			String line = null;

			while ((line = unsyncBufferedReader.readLine()) != null) {
				line = trimLine(fileName, absolutePath, line);

				String trimmedLine = StringUtil.trimLeading(line);

				if (trimmedLine.startsWith("<#assign ")) {
					line = formatWhitespace(line, trimmedLine, true);

					line = formatIncorrectSyntax(line, "=[", "= [", false);
					line = formatIncorrectSyntax(line, "+[", "+ [", false);
				}

				if (line.endsWith(">")) {
					if (line.endsWith("/>")) {
						if (!trimmedLine.equals("/>") &&
							!line.endsWith(" />")) {

							line = StringUtil.replaceLast(line, "/>", " />");
						}
					}
					else if (line.endsWith(" >")) {
						line = StringUtil.replaceLast(line, " >", ">");
					}
				}

				sb.append(line);
				sb.append("\n");
			}
		}

		content = sb.toString();

		if (content.endsWith("\n")) {
			content = content.substring(0, content.length() - 1);
		}

		return content;
	}

}