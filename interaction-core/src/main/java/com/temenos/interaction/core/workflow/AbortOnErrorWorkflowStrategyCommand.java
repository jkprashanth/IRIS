package com.temenos.interaction.core.workflow;

/*
 * #%L
 * interaction-core
 * %%
 * Copyright (C) 2012 - 2013 Temenos Holdings N.V.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


import java.util.ArrayList;
import java.util.List;

import com.temenos.interaction.core.command.InteractionCommand;
import com.temenos.interaction.core.command.InteractionContext;
import com.temenos.interaction.core.command.InteractionException;


/**
 * <p>This command implements a workflow that will abort if there is an error.</p>
 * Commands are added to this workflow and then executed in the same order.  If a 
 * command returns an error, the workflow is aborted.
 * @author aphethean
 */
public class AbortOnErrorWorkflowStrategyCommand implements WorkflowCommand {

	protected List<InteractionCommand> commands = new ArrayList<InteractionCommand>();
	
	public AbortOnErrorWorkflowStrategyCommand() {}

	/**
	 * Construct with a list of commands to execute.
	 * @param commands
	 * @invariant commands not null
	 */
	public AbortOnErrorWorkflowStrategyCommand(List<InteractionCommand> commands) {
		this.commands = commands;
		if (commands == null)
			throw new IllegalArgumentException("No commands supplied");		
	}

	@Override
	public boolean isEmpty() {
		return commands.isEmpty();
	}

	public void addCommand(InteractionCommand command) {
		if (command == null)
			throw new IllegalArgumentException("No command supplied");
		commands.add(command);
	}
	
	/**
	 * @throws InteractionException 
	 * @precondition at least one command has been added {@link addCommand}
	 * @postcondition returned {@link Result) will be the result of a logical 
	 * short-circuit evaluation of the supplied commands.  Short-circut will 
	 * occur when the {@link Command} result is not {@link InteractionCommand.Result.SUCCESS}
	 */
	@Override
	public Result execute(InteractionContext ctx) throws InteractionException {
		assert(commands != null);
		assert(commands.size() > 0) : "There must be at least one command in the workflow";
		if (ctx == null)
			throw new IllegalArgumentException("InteractionContext must be supplied");

		Result result = null;
		for (InteractionCommand command : commands) {
			result = command.execute(ctx);
			if (result != Result.SUCCESS)
				break;
		}
		return result;
	}

}
