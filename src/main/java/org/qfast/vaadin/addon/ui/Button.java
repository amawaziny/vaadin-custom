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

import com.vaadin.server.Resource;

public class Button extends com.vaadin.ui.Button {

    private static final long serialVersionUID = -8595460792454282869L;

    public Button() {
        setupButton();
    }

    public Button(String caption) {
        super(caption);
        setupButton();
    }

    public Button(String caption, ClickListener listener) {
        super(caption, listener);
        setupButton();
    }

    public Button(String caption, Resource icon) {
        super(caption, icon);
        setupButton();
    }

    public Button(Resource icon) {
        super(icon);
        setupButton();
    }

    public Button(Resource icon, ClickListener listener) {
        super(icon);
        addClickListener(listener);
        setupButton();
    }

    private void setupButton() {
    }
}
