package org.qfast.vaadin.addon.ui.export;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.ui.UI;

import java.io.Serializable;
import java.util.List;

/**
 * @author thomas
 */
public interface TableHolder extends Serializable {

    List<Object> getPropIds();
    boolean isHierarchical();
    void setHierarchical(final boolean hierarchical);

    Short getCellAlignment(Object propId);
    boolean isGeneratedColumn(final Object propId) throws IllegalArgumentException;
    Class<?> getPropertyTypeForGeneratedColumn(final Object propId) throws IllegalArgumentException;
    Property getPropertyForGeneratedColumn(final Object propId, final Object rootItemId) throws
            IllegalArgumentException;

    // table delegated methods
    boolean isColumnCollapsed(Object propertyId);
    UI getUI();
    String getColumnHeader(Object propertyId);
    Container getContainerDataSource();
    boolean isExportableFormattedProperty();
    String getFormattedPropertyValue(Object rowId, Object colId, Property property);
}
