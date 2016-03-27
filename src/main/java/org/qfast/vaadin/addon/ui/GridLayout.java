/*
 * Copyright 2014 QFast Ahmed El-mawaziny.
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

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;

/**
 * @author Ahmed El-mawaziny
 */
public class GridLayout extends com.vaadin.ui.GridLayout {

    public GridLayout() {
    }

    public GridLayout(int columns) {
        super.setColumns(columns);
    }

    public GridLayout(int columns, int rows) {
        super(columns, rows);
    }

    public GridLayout(int columns, int rows, Component... children) {
        super(columns, rows, children);
    }

    public void addComponent(Component component, Alignment alignment) {
        super.addComponent(component);
        super.setComponentAlignment(component, alignment);
    }
    private static final long serialVersionUID = -2574291188360351099L;
}
