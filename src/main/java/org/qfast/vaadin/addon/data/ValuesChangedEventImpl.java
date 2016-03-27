/*
 *  Copyright 2015 QFast Ahmed El-mawaziny.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.qfast.vaadin.addon.data;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Field;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @author Ahmed El-mawaziny
 * @param <T>
 */
public class ValuesChangedEventImpl<T> extends AbstractField.ValueChangeEvent implements ValuesChangedEvent<T> {

    public static final Method VALUE_CHANGE_METHOD;
    private static final long serialVersionUID = -6914651403381368190L;

    static {
        try {
            VALUE_CHANGE_METHOD = ValuesChangedListener.class.getDeclaredMethod("valueChange",
                    new Class[]{ValuesChangedEvent.class});
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException("Internal error finding methods in ValueChangedEventImpl");
        }
    }

    public ValuesChangedEventImpl(Field source) {
        super(source);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<T> getValue() {
        return (Collection<T>) getProperty().getValue();
    }

}
