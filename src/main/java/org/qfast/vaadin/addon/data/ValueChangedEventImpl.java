/*
 * Copyright 2014 mattitahvonenitmill.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.qfast.vaadin.addon.data;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Field;

import java.lang.reflect.Method;

/**
 * @author Ahmed El-mawaziny
 * @param <T>
 */
public class ValueChangedEventImpl<T> extends AbstractField.ValueChangeEvent implements ValueChangedEvent<T> {

    public static final Method VALUE_CHANGE_METHOD;
    private static final long serialVersionUID = 4714852733440010434L;

    static {
        try {
            VALUE_CHANGE_METHOD = ValueChangedListener.class.getDeclaredMethod("valueChange", new Class[]{ValueChangedEvent.class});
        } catch (final java.lang.NoSuchMethodException e) {
            throw new java.lang.RuntimeException("Internal error finding methods in ValueChangedEventImpl");
        }
    }

    public ValueChangedEventImpl(Field source) {
        super(source);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getValue() {
        return (T) getProperty().getValue();
    }

}
