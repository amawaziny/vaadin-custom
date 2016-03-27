/*
 * Copyright 2014 Ahmed El-mawaziny.
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
package org.qfast.vaadin.addon.ui;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.JavaScript;

/**
 * @author Ahmed El-mawaziny
 */
public class TextField extends com.vaadin.ui.TextField {

    private static final long serialVersionUID = 9175579168781172562L;

    public TextField() {
        super();
        setup();
    }

    public TextField(String caption) {
        super(caption);
        setup();
    }

    public TextField(boolean required) {
        setRequired(required);
        setup();
    }

    public TextField(String caption, boolean required) {
        super(caption);
        setRequired(required);
        setup();
    }

    public TextField(Converter<String, ?> converter) {
        super.setConverter(converter);
        setup();
    }

    private void setup() {
        setNullRepresentation("");
        setNullSettingAllowed(false);
        setValidationVisible(false);
    }

    public TextField withEnabled(boolean enabled) {
        setEnabled(enabled);
        return this;
    }

    public TextField withValidationVisible(boolean validateAutomatically) {
        setValidationVisible(validateAutomatically);
        return this;
    }

    public void setPlaceholder(String placeholder) {
        JavaScript.getCurrent().execute("document.getElementById('"
                + getId() + "').placeholder = '" + placeholder + "';");
    }
}
