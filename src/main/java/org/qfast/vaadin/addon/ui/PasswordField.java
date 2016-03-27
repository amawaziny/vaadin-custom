/*
 *  Copyright 2016 QFast Ahmed El-mawaziny.
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

/**
 * @author Ahmed El-mawaziny
 */
public class PasswordField extends com.vaadin.ui.PasswordField {

    private static final long serialVersionUID = -263448185185164600L;

    public PasswordField() {
        setup();
    }

    public PasswordField(boolean required) {
        setRequired(required);
        setup();
    }

    public PasswordField(String caption) {
        super(caption);
        setup();
    }

    public PasswordField(String caption, boolean required) {
        super(caption);
        setRequired(required);
        setup();
    }

    private void setup() {
        setNullRepresentation("");
        setNullSettingAllowed(true);
        setValidationVisible(false);
    }
}