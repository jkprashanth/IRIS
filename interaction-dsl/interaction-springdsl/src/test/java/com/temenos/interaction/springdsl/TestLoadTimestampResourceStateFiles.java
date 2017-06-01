package com.temenos.interaction.springdsl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.BeforeClass;
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

    private static SpringDSLResourceStateProvider dsl;
    private static ConfigLoader configLoader = new ConfigLoader();
    private static ResourceState resource;

    @BeforeClass
    public static void setUpClass() {
        dsl = new SpringDSLResourceStateProvider();
        String location = ClassLoader.getSystemClassLoader().getResource("PRDFiles").getPath().replaceFirst("/", "");
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
