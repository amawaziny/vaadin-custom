package org.qfast.vaadin.addon.ui.export;

import com.vaadin.data.Property;

import java.io.Serializable;

public interface ExportableFormattedProperty extends Serializable {

    public String getFormattedPropertyValue(Object rowId, Object colId, Property property);
}
