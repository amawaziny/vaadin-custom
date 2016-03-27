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

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;

/**
 * @author Ahmed El-mawaziny
 */
public class VerticalLayout extends com.vaadin.ui.VerticalLayout {
    private static final long serialVersionUID = -7919090034659433565L;
    private boolean marginTop;
    private boolean marginBottom;
    private boolean marginRight;
    private boolean marginLeft;

    public VerticalLayout() {
    }

    public VerticalLayout(Component... children) {
        super(children);
    }

    public void addComponent(Component c, Alignment alignment) {
        super.addComponent(c);
        super.setComponentAlignment(c, alignment);
    }

    public VerticalLayout withMargin(boolean margin) {
        setMargin(margin);
        return this;
    }

    public VerticalLayout alignAll(Alignment alignment) {
        for (Component component : this) {
            setComponentAlignment(component, alignment);
        }
        return this;
    }

    public VerticalLayout withMarginTop(boolean top) {
        marginTop = top;
        setMargin(new MarginInfo(marginTop, marginRight, marginBottom, marginLeft));
        return this;
    }

    public VerticalLayout withMarginBottom(boolean bottom) {
        marginBottom = bottom;
        setMargin(new MarginInfo(marginTop, marginRight, marginBottom, marginLeft));
        return this;
    }

    public VerticalLayout withMarginRight(boolean right) {
        marginRight = right;
        setMargin(new MarginInfo(marginTop, marginRight, marginBottom, marginLeft));
        return this;
    }

    public VerticalLayout withMarginLeft(boolean left) {
        marginLeft = left;
        setMargin(new MarginInfo(marginTop, marginRight, marginBottom, marginLeft));
        return this;
    }

    public VerticalLayout withFullWidth() {
        setWidth("100%");
        return this;
    }

    public VerticalLayout withSpacing(boolean spacing) {
        this.setSpacing(true);
        return this;
    }

    public VerticalLayout expand(Component... componentsToExpand) {
        if (getWidth() < 0) {
            withFullWidth();
        }

        for (Component component : componentsToExpand) {
            if (component.getParent() != this) {
                addComponent(component);
            }
            setExpandRatio(component, 1);
            component.setWidth(100, Unit.PERCENTAGE);
        }
        return this;
    }
}
