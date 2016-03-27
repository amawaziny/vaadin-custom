/*
 * Copyright 2015 QFast Ahmed El-mawaziny.
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

import static com.vaadin.event.ShortcutAction.KeyCode.W;
import static com.vaadin.event.ShortcutAction.ModifierKey.CTRL;
import com.vaadin.ui.Component;

/**
 * @author Ahmed El-mawaziny
 */
public class Window extends com.vaadin.ui.Window {

    public Window() {
        setup();
    }

    public Window(String caption) {
        super(caption);
        setup();
    }

    public Window(String caption, Component content) {
        super(caption, content);
        setup();
    }

    private void setup() {
        removeCloseShortcut();
        setCloseShortcut(W, CTRL);
    }
    private static final long serialVersionUID = -2288597052601714188L;
}
