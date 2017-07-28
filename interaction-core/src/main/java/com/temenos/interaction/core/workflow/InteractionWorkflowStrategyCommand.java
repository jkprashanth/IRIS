package com.temenos.interaction.core.workflow;

/*
 * #%L
 * interaction-core
 * %%
 * Copyright (C) 2012 - 2017 Temenos Holdings N.V.
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


import com.temenos.interaction.core.command.InteractionCommand;
import com.temenos.interaction.core.command.TransitionCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of a workflow {@link InteractionCommand} that only uses
 * commands that are not of type {@link TransitionCommand}.
 *
 * @author ikarady
 */
public class InteractionWorkflowStrategyCommand extends AbortOnErrorWorkflowStrategyCommand {


    public InteractionWorkflowStrategyCommand() {}


    /**
     * Construct with a list of commands to execute.
     * @param commands
     * @invariant commands not null
     */
    public InteractionWorkflowStrategyCommand(List<InteractionCommand> commands) {
        super(commands);
    }

    @Override
    public void addCommand(InteractionCommand command) {
        if (command instanceof TransitionCommand) {
            return;
        }
        super.addCommand(command);

    }

}
