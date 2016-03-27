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

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;

/**
 * @author Ahmed El-mawaziny
 */
public class FlowLayout extends CssLayout {

    private static final long serialVersionUID = -1897311068657139358L;
    private String css = "float: left;";

    public FlowLayout(Component... children) {
        super();
        this.css += "margin:5px;";
        addComponents(children);
    }

    public FlowLayout(String css) {
        this.css += css;
    }

    @Override
    protected String getCss(Component c) {
        return css;
    }
}
