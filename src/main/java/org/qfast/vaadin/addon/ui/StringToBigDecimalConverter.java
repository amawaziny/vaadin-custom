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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author Ahmed El-mawaziny
 */
public class StringToBigDecimalConverter extends com.vaadin.data.util.converter.StringToBigDecimalConverter {

    private static final long serialVersionUID = 2134393505674406580L;
    private String pattern;

    public StringToBigDecimalConverter() {

    }

    public StringToBigDecimalConverter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    protected NumberFormat getFormat(Locale locale) {
        DecimalFormat format = (DecimalFormat) super.getFormat(locale);
        format.applyPattern(pattern);
        return format;
    }
}
