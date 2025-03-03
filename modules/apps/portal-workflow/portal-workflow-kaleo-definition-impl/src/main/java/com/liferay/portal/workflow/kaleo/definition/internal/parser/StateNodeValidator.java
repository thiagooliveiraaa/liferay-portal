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

package com.liferay.portal.workflow.kaleo.definition.internal.parser;

import com.liferay.portal.workflow.kaleo.definition.Definition;
import com.liferay.portal.workflow.kaleo.definition.NodeType;
import com.liferay.portal.workflow.kaleo.definition.State;
import com.liferay.portal.workflow.kaleo.definition.exception.KaleoDefinitionValidationException;
import com.liferay.portal.workflow.kaleo.definition.parser.NodeValidator;

import java.util.Objects;

import org.osgi.service.component.annotations.Component;

/**
 * @author Michael C. Han
 * @author Marcellus Tavares
 */
@Component(service = NodeValidator.class)
public class StateNodeValidator extends BaseNodeValidator<State> {

	@Override
	public NodeType getNodeType() {
		return NodeType.STATE;
	}

	@Override
	protected void doValidate(Definition definition, State state)
		throws KaleoDefinitionValidationException {

		if (state.isInitial()) {
			_validateInitialState(definition, state);
		}
		else if (state.getIncomingTransitionsCount() == 0) {
			throw new KaleoDefinitionValidationException.
				MustSetIncomingTransition(state.getDefaultLabel());
		}
	}

	private void _validateInitialState(Definition definition, State state)
		throws KaleoDefinitionValidationException {

		State initialState = definition.getInitialState();

		if (!Objects.equals(initialState, state)) {
			throw new KaleoDefinitionValidationException.
				MultipleInitialStateNodes(
					state.getDefaultLabel(), initialState.getDefaultLabel());
		}

		if (state.getIncomingTransitionsCount() > 0) {
			throw new KaleoDefinitionValidationException.
				MustNotSetIncomingTransition(state.getDefaultLabel());
		}

		if (state.getOutgoingTransitionsCount() == 0) {
			throw new KaleoDefinitionValidationException.
				MustSetOutgoingTransition(state.getDefaultLabel());
		}
	}

}