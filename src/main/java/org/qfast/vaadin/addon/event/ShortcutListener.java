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

package org.qfast.vaadin.addon.event;

import static com.vaadin.event.ShortcutAction.KeyCode.ENTER;

/**
 * @author Ahmed El-mawaziny on 6/11/15.
 */
public abstract class ShortcutListener extends com.vaadin.event.ShortcutListener {

    private static final long serialVersionUID = 6581229317483192742L;

    public ShortcutListener() {
        super("Enter", ENTER, null);
    }
}
