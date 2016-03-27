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

import com.vaadin.data.Property;
import com.vaadin.shared.ui.label.ContentMode;

/**
 * @author Ahmed El-mawaziny
 */
public class Label extends com.vaadin.ui.Label {
    private static final long serialVersionUID = 8213337003583574373L;

    public Label() {
    }

    public Label(String content) {
        super(content);
    }

    public Label(Property contentSource) {
        super(contentSource);
    }

    public Label(String content, ContentMode contentMode) {
        super(content, contentMode);
    }

    public Label(Property contentSource, ContentMode contentMode) {
        super(contentSource, contentMode);
    }

    public Label withBold(boolean bold) {
        if (bold) {
            setValue("<b>" + getValue() + "</b>");
            setContentMode(ContentMode.HTML);
        } else {
            setValue(getValue().replace("<b>", "").replace("</b>", ""));
        }
        return this;
    }

    public Label withStyle(String styleName) {
        addStyleName(styleName);
        return this;
    }

    public Label withSizeUndefined() {
        setSizeUndefined();
        return this;
    }
}
