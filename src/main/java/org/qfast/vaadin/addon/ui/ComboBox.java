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

package org.qfast.vaadin.addon.ui;

@SuppressWarnings("unchecked")
public class ComboBox<T> extends TypedSelect<T> {

    private static final long serialVersionUID = -7353624258904405265L;

    public ComboBox() {
        withSelectType(com.vaadin.ui.ComboBox.class);
    }

    public ComboBox(boolean required) {
        this();
        setRequired(required);
        getSelect().setRequired(required);
        getSelect().setNullSelectionAllowed(false);
    }

    public ComboBox(String caption) {
        this();
        setCaption(caption);
        getSelect().setCaption(caption);
    }

    public void select(T value) {
        setValue(value);
        getSelect().setValue(value);
        getSelect().select(value);
    }
}
