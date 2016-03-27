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
package org.qfast.vaadin.addon.util;

/**
 * @author Ahmed El-mawaziny
 */
public class ComparableComparator
        extends org.apache.commons.collections.comparators.ComparableComparator {

    @Override
    @SuppressWarnings("unchecked")
    public int compare(Object obj1, Object obj2) {
        if (obj1 != null && obj2 != null) {
            return ((Comparable) obj1).compareTo(obj2);
        }
        return -1;
    }
    private static final long serialVersionUID = 3699127414114720218L;
}
