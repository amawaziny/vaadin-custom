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
import com.vaadin.event.ShortcutAction.KeyCode;
import static com.vaadin.event.ShortcutAction.KeyCode.S;
import com.vaadin.event.ShortcutAction.ModifierKey;
import static com.vaadin.event.ShortcutAction.ModifierKey.CTRL;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

/**
 * A primary button, commonly used for e.g. saving an entity. Automatically sets
 * "primary" class name and hooks click shortcut for KeyCode.S,
 * ModifierKey.CTRL.
 */
public class PrimaryButton extends Button {

    private ClickShortcut shortcut;

    public PrimaryButton() {
        setupPrimaryButton();
    }

    public PrimaryButton(String caption) {
        super(caption);
        setupPrimaryButton();
    }

    public PrimaryButton(String caption, ClickListener listener) {
        super(caption, listener);
        setupPrimaryButton();
    }

    public PrimaryButton(String caption, Resource icon) {
        super(caption, icon);
        setupPrimaryButton();
    }

    public PrimaryButton(Resource icon) {
        super(icon);
        setupPrimaryButton();
    }

    public PrimaryButton(Resource icon, ClickListener listener) {
        super(icon, listener);
        setupPrimaryButton();
    }

    private void setupPrimaryButton() {
        setStyleName(ValoTheme.BUTTON_PRIMARY);
        shortcut = new ClickShortcut(this, S, CTRL);
        addShortcutListener(shortcut);
    }

    @Override
    public void removeClickShortcut() {
        super.removeClickShortcut();
        if (shortcut != null) {
            removeShortcutListener(shortcut);
            shortcut = null;
            getState().clickShortcutKeyCode = 0;
        }
    }

    public void addClickShortcut() {
        shortcut = new ClickShortcut(this, S, CTRL);
        addShortcutListener(shortcut);
    }

    public static class ClickShortcut extends ShortcutListener {

        protected com.vaadin.ui.Button button;

        /**
         * Creates a keyboard shortcut for clicking the given button using the
         * shorthand notation defined in {@link ShortcutAction}.
         *
         * @param button to be clicked when the shortcut is invoked
         * @param shorthandCaption the caption with shortcut keycode and
         * modifiers indicated
         */
        public ClickShortcut(com.vaadin.ui.Button button, String shorthandCaption) {
            super(shorthandCaption);
            this.button = button;
        }

        /**
         * Creates a keyboard shortcut for clicking the given button using the
         * given {@link KeyCode} and {@link ModifierKey}s.
         *
         * @param button to be clicked when the shortcut is invoked
         * @param keyCode KeyCode to react to
         * @param modifiers optional modifiers for shortcut
         */
        public ClickShortcut(com.vaadin.ui.Button button, int keyCode, int... modifiers) {
            super(null, keyCode, modifiers);
            this.button = button;
        }

        /**
         * Creates a keyboard shortcut for clicking the given button using the
         * given {@link KeyCode}.
         *
         * @param button to be clicked when the shortcut is invoked
         * @param keyCode KeyCode to react to
         */
        public ClickShortcut(com.vaadin.ui.Button button, int keyCode) {
            this(button, keyCode, null);
        }

        @Override
        public void handleAction(Object sender, Object target) {
            button.click();
            if (button.isDisableOnClick()) {
                button.setEnabled(false);
            }
        }
    }
    private static final long serialVersionUID = -2021035436509034505L;

}
