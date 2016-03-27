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

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

/**
 * A primary button, commonly used for e.g. saving an entity. Automatically sets 
 * "primary" class name and hooks click shortcut for ENTER.
 */
public class DeleteButton extends Button {

    public DeleteButton() {
        setupPrimaryButton();
    }

    public DeleteButton(String caption) {
        super(caption);
        setupPrimaryButton();
    }

    public DeleteButton(String caption, ClickListener listener) {
        super(caption, listener);
        setupPrimaryButton();
    }

    public DeleteButton(String caption, Resource icon) {
        super(caption, icon);
        setupPrimaryButton();
    }

    public DeleteButton(Resource icon) {
        super(icon);
        setupPrimaryButton();
    }

    public DeleteButton(Resource icon, ClickListener listener) {
        super(icon, listener);
        setupPrimaryButton();
    }

    private void setupPrimaryButton() {
        setStyleName(ValoTheme.BUTTON_DANGER);
        setClickShortcut(ShortcutAction.KeyCode.DELETE, null);
    }
    private static final long serialVersionUID = -2021035436509034505L;
    
}
