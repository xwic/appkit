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

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

/**
 * Helper to get bean properties (including nested) and values.
 *
 * @author Vitaliy Zhovtyuk
 */
public class ReflectionHelper {
    /**
     * Iterate over passed property names and get bean value using getter method.
     *
     * @param bean      bean to get it properties
     * @param fieldName string property name
     * @param <K>       value type
     * @return property value
     */
    public <K> K getValue(Object bean, String fieldName) {
        String[] fieldsHierarchy = fieldName.split("\\.");
        Object value = bean;
        for (String fieldInHierarchy : fieldsHierarchy) {
            value = getPropertyValue(value, fieldInHierarchy);
        }
        return (K) value;
    }

    /**
     * Get bean property value.
     *
     * @param bean      bean to get it properties
     * @param fieldName string property name
     * @param <K>       value type
     * @return property value
     */
    private <K> K getPropertyValue(Object bean, String fieldName) {
        try {
            PropertyDescriptor descr = new PropertyDescriptor(fieldName, bean.getClass());
            K value = (K) descr.getReadMethod().invoke(bean, (Object[]) null);
            return value;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
