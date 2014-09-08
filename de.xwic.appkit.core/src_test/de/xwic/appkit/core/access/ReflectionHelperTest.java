/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.access.AccessHandler
 * Created on 04.01.2008 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.access;

import de.xwic.appkit.core.security.IRight;
import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.core.security.impl.Right;
import de.xwic.appkit.core.security.impl.Role;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test for ReflectionHelper.
 *
 * @author Vitaliy Zhovtyuk
 */
public class ReflectionHelperTest {

    /**
     * logger
     */
    protected final static Log log = LogFactory.getLog(ReflectionHelperTest.class);

    @Test
    public void shouldIntrospectNestedProperty() {
        IRole role = new Role();
        role.setName("roleName");
        IRight right = new Right(role, null, null);
        ReflectionHelper helper = new ReflectionHelper();
        String value = helper.getValue(right, "role.name");
        assertNotNull("Property evaluated is not null", value);
        assertEquals("Property evaluated ", "roleName", value);
    }

    @Test
    public void shouldIntrospectProperty() {
        IRole role = new Role();
        role.setName("roleName");
        IRight right = new Right(role, null, null);
        ReflectionHelper helper = new ReflectionHelper();
        IRole value = helper.getValue(right, "role");
        assertNotNull("Property evaluated is not null", value);
        assertEquals("Property evaluated ", "roleName", value.getName());
    }
}
