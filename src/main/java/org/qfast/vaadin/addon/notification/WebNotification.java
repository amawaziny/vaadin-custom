package org.qfast.vaadin.addon.notification;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractJavaScriptExtension;
import com.vaadin.ui.UI;

@JavaScript({"notify.min.js", "webnotifications-connector.min.js"})
public class WebNotification extends AbstractJavaScriptExtension {

    public WebNotification(UI ui) {
        extend(ui);
    }

    public void requestPermission() {
        callFunction("requestPermission");
    }

    public void show(String title, String body) {
        callFunction("show", title, body);
    }
    private static final long serialVersionUID = -1078061694872573406L;
}
