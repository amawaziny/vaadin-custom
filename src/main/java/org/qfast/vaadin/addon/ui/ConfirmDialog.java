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

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.io.Serializable;

/**
 * @author Ahmed El-mawaziny
 */
public class ConfirmDialog extends Window {

    private final Button confirm = new PrimaryButton("Confirmed");
    private final Button cancel = new Button("Cancel");
    private boolean confirmed;

    public ConfirmDialog() {
        this(null, null);
    }

    public ConfirmDialog(String title, String message, final Listener listener) {
        this(title, message);
        addListener(listener);
    }

    private ConfirmDialog getDialog() {
        return this;
    }

    private void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmCaption(String caption) {
        confirm.setCaption(caption);
    }

    public void setCancelCaption(String caption) {
        cancel.setCaption(caption);
    }

    public ConfirmDialog(String title, String messageText) {
        setClosable(false);
        setDraggable(false);
        setResizable(false);
        addStyleName("dialog");
        setModal(true);
        setCaption(title);
        setWidth(300, Unit.PIXELS);
        setHeight(225, Unit.PIXELS);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setMargin(true);
        content.setSpacing(true);

        Label message;
        if (messageText != null && !messageText.isEmpty()) {
            messageText = messageText.replaceAll("\\r?\\n", "<br />");
            message = new Label(messageText, ContentMode.HTML);
        } else {
            message = new Label("Are You Sure ?");
        }
        content.addComponent(message);
        content.setComponentAlignment(message, Alignment.MIDDLE_CENTER);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.setMargin(true);

        buttons.addComponent(confirm);
        buttons.setComponentAlignment(confirm, Alignment.MIDDLE_CENTER);

        buttons.addComponent(cancel);
        buttons.setComponentAlignment(cancel, Alignment.MIDDLE_CENTER);

        content.addComponent(buttons);
        content.setComponentAlignment(buttons, Alignment.BOTTOM_CENTER);

        setContent(content);
    }

    public void addConfirmListener(ClickListener clickListener) {
        confirm.addClickListener(clickListener);
    }

    public void addCancelListener(ClickListener clickListener) {
        cancel.addClickListener(clickListener);
    }

    public interface Listener extends Serializable {

        void onClose(ConfirmDialog dialog);
    }

    public final void addListener(final Listener listener) {
        ClickListener clickListener = new ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (getDialog().isEnabled()) {
                    getDialog().setEnabled(false);
                    getDialog().setConfirmed(event.getButton() == confirm);
                    if (listener != null) {
                        listener.onClose(getDialog());
                    }
                }
            }
        };
        confirm.addClickListener(clickListener);
        cancel.addClickListener(clickListener);
    }

    public static void show(final UI ui, final String message,
            final Listener listener) {
        ConfirmDialog d = new ConfirmDialog("Please confirm...", message, listener);
        ui.addWindow(d);
    }

    public static void show(final UI ui, String title, final String message,
            final Listener listener) {
        ConfirmDialog d = new ConfirmDialog(title, message, listener);
        ui.addWindow(d);
    }

    public static void show(String title, String message, Listener listener) {
        ConfirmDialog d = new ConfirmDialog(title, message, listener);
        UI.getCurrent().addWindow(d);
    }

    public static void show(String message, Listener listener) {
        ConfirmDialog d = new ConfirmDialog("Please confirm...", message, listener);
        UI.getCurrent().addWindow(d);
    }

    public static void show(Listener listener) {
        ConfirmDialog d = new ConfirmDialog("Please confirm...", "Are you sure ?",
                listener);
        UI.getCurrent().addWindow(d);
    }
    private static final long serialVersionUID = -8834282428786048757L;
}
