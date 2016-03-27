package org.qfast.vaadin.addon.ui.export;

import com.vaadin.data.Property;
import com.vaadin.ui.CustomTable.ColumnGenerator;

public interface CustomTableExportableColumnGenerator extends ColumnGenerator {

    Property getGeneratedProperty(Object itemId, Object columnId);
    // the type of the generated property
    Class<?> getType();
}
