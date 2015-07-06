/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
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
