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

package com.liferay.exportimport.kernel.staging;

import com.liferay.petra.lang.CentralizedThreadLocal;
import com.liferay.petra.lang.HashUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Raymond Augé
 * @author Shuyang Zhou
 */
public class MergeLayoutPrototypesThreadLocal {

	public static void clearMergeComplete() {
		_mergeComplete.remove();
	}

	public static boolean isInProgress() {
		return _inProgress.get();
	}

	public static boolean isMergeComplete(
		String methodName, Object... arguments) {

		Set<MethodKey> methodKeys = _mergeComplete.get();

		return methodKeys.contains(new MethodKey(methodName, arguments));
	}

	public static boolean isSkipMerge() {
		return _skipMerge.get();
	}

	public static void setInProgress(boolean inProgress) {
		_inProgress.set(inProgress);
	}

	public static void setMergeComplete(
		String methodName, Object... arguments) {

		Set<MethodKey> methodKeys = _mergeComplete.get();

		methodKeys.add(new MethodKey(methodName, arguments));

		setInProgress(false);
	}

	public static void setSkipMerge(boolean skipMerge) {
		_skipMerge.set(skipMerge);
	}

	private static final ThreadLocal<Boolean> _inProgress =
		new CentralizedThreadLocal<>(
			MergeLayoutPrototypesThreadLocal.class + "._inProgress",
			() -> Boolean.FALSE);
	private static final ThreadLocal<Set<MethodKey>> _mergeComplete =
		new CentralizedThreadLocal<>(
			MergeLayoutPrototypesThreadLocal.class + "._mergeComplete",
			HashSet::new);
	private static final ThreadLocal<Boolean> _skipMerge =
		new CentralizedThreadLocal<>(
			MergeLayoutPrototypesThreadLocal.class + "._skipMerge",
			() -> Boolean.FALSE);

	private static class MethodKey {

		public MethodKey(String methodName, Object[] arguments) {
			_methodName = methodName;
			_arguments = arguments;
		}

		@Override
		public boolean equals(Object object) {
			MethodKey methodKey = (MethodKey)object;

			if (Objects.equals(_methodName, methodKey._methodName) &&
				Arrays.equals(_arguments, methodKey._arguments)) {

				return true;
			}

			return false;
		}

		@Override
		public int hashCode() {
			int hashCode = _methodName.hashCode();

			if (_arguments != null) {
				for (Object object : _arguments) {
					if (object == null) {
						hashCode = HashUtil.hash(hashCode, 0);
					}
					else {
						hashCode = HashUtil.hash(hashCode, object.hashCode());
					}
				}
			}

			return hashCode;
		}

		private final Object[] _arguments;
		private final String _methodName;

	}

}