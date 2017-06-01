package com.temenos.interaction.springdsl;

/*
 * #%L
 * interaction-springdsl
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.temenos.interaction.core.hypermedia.ResourceState;
import com.temenos.interaction.core.hypermedia.Transition;
import com.temenos.interaction.core.resource.ConfigLoader;


/**
 * TODO: Document me!
 *
 * @author kprasanth
 *
 */
public class TestLoadTimestampResourceStateFiles {

    private  SpringDSLResourceStateProvider dsl;
    private  ConfigLoader configLoader = new ConfigLoader();
    private  ResourceState resource;
    private  ClassLoader classloader = this.getClass().getClassLoader();
    @Before
    public void setUpClass() {
        dsl = new SpringDSLResourceStateProvider();
        
        String location = classloader.getResource("PRDFiles").getPath().replaceFirst("/", "");
        System.out.println(location+"/..");
        System.out.println(Arrays.deepToString(new File(location+"/..").list()));
        System.out.println(location);
        System.out.println(Arrays.deepToString(new File(location).list()));
        configLoader.setIrisConfigDirPath(location);
        dsl.setConfigLoader(configLoader);
        resource = dsl.getResourceState("Tst_Twins-notes");
    }

    @Test
    public void testSameResourceNameTransitionList() {

        assertNotNull(resource);
        List<Transition> transistionList = resource.getTransitions();
        assertEquals(2, transistionList.size());
        assertEquals(transistionList.get(0).getSource().toString(), "Note.notes");
        assertEquals(transistionList.get(1).getTarget().toString(), ".Tst_Twins-Page2");
    }

    @Test
    public void testInvalidResourceState() {
        ResourceState resource = dsl.getResourceState("Tst_Invalid-notes");
        assertNull(resource);
    }

    @Test
    public void testGetResourceFromClassPath() {
        dsl.getResourceState("Tst_Twins-notes");
        assertNotNull(resource);
        List<Transition> transistionList = resource.getTransitions();
        assertEquals(2, transistionList.size());
        assertEquals(transistionList.get(0).getSource().toString(), "Note.notes");
        assertEquals(transistionList.get(0).getTarget().toString(), ".Tst_Twins-Page1");
    }
}
