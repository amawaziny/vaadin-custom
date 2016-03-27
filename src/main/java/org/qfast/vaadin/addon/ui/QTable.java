/*
 * Copyright 2014 mattitahvonenitmill.
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

import com.vaadin.data.Property;
import org.qfast.vaadin.addon.data.ValueChangedEvent;
import org.qfast.vaadin.addon.data.ValueChangedEventImpl;
import org.qfast.vaadin.addon.data.ValueChangedListener;
import org.qfast.vaadin.addon.util.ListContainer;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.qfast.vaadin.addon.data.ValueChangedEventImpl.VALUE_CHANGE_METHOD;

public class QTable<T> extends com.vaadin.ui.Table {

    private ListContainer<T> bic;
    private String[] pendingProperties;
    private String[] pendingHeaders;
    private Map<String, Properties> formattedColumnPatterns;
    private final SimpleDateFormat sdf = new SimpleDateFormat();
    private final DecimalFormat df = new DecimalFormat();
    public static final String PROPERTIES_TYPE = "type";
    public static final String PROPERTIES_PATTERN = "pattern";

    public QTable() {
    }

    /**
     * Constructs a Table with explicit bean type. Handy for example if your
     * beans are JPA proxies or the table in empty when showing it initially.
     *
     * @param type
     */
    public QTable(Class<T> type) {
        bic = new ListContainer<>(type);
        setContainerDataSource(bic);
    }

    @SuppressWarnings("unchecked")
    public QTable(T... beans) {
        this(Arrays.asList(beans));
    }

    public QTable(Collection<T> beans) {
        this();
        if (beans != null) {
            if (beans instanceof List) {
                bic = new ListContainer<>((List<T>) beans);
            } else {
                bic = new ListContainer<>(new ArrayList<>(beans));
            }
            setContainerDataSource(bic);
        }
    }

    public QTable<T> withProperties(String... visibleProperties) {
        if (isContainerInitialized()) {
            setVisibleColumns((Object[]) visibleProperties);
        } else {
            pendingProperties = visibleProperties;
            for (String string : visibleProperties) {
                addContainerProperty(string, String.class, "");
            }
        }
        return this;
    }

    public void setFormattedColumnPatterns(Map<String, Properties> formattedColumnPatterns) {
        this.formattedColumnPatterns = formattedColumnPatterns;
    }

    private boolean isContainerInitialized() {
        return bic != null;
    }

    public QTable<T> withColumnHeaders(String... columnNamesForVisibleProperties) {
        if (isContainerInitialized()) {
            setColumnHeaders(columnNamesForVisibleProperties);
        } else {
            pendingHeaders = columnNamesForVisibleProperties;
            // Add headers to temporary indexed container, in case table is initially
            // empty
            for (String prop : columnNamesForVisibleProperties) {
                addContainerProperty(prop, String.class, "");
            }
        }
        return this;
    }

    @Override
    public void setColumnHeaders(String... columnHeaders) {
        super.setColumnHeaders(columnHeaders);
        if (columnHeaders != null && columnHeaders.length > 10) {
            this.setColumnCollapsingAllowed(true);
        }
    }

    public void addValueChangedListener(ValueChangedListener<T> listener) {
        addListener(ValueChangedEvent.class, listener, VALUE_CHANGE_METHOD);
        setSelectable(true);
        setImmediate(true);
    }

    public void removeValueChangedListener(ValueChangedListener<T> listener) {
        removeListener(ValueChangedEvent.class, listener, VALUE_CHANGE_METHOD);
        setSelectable(hasListeners(ValueChangedEvent.class));
    }

    @Override
    public void fireValueChange(boolean repaintIsNotNeeded) {
        super.fireValueChange(repaintIsNotNeeded);
        fireEvent(new ValueChangedEventImpl(this));
    }

    @SuppressWarnings("unchecked")
    private void ensureBeanItemContainer(Collection<T> beans) {
        if (!isContainerInitialized()) {
            bic = new ListContainer(beans);
            if (pendingProperties != null) {
                setContainerDataSource(bic, Arrays.asList(pendingProperties));
                pendingProperties = null;
            } else {
                setContainerDataSource(bic);
            }
            if (pendingHeaders != null) {
                setColumnHeaders(pendingHeaders);
                pendingHeaders = null;
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getValue() {
        return (T) super.getValue();
    }

    @SuppressWarnings("unchecked")
    public void addBeans(T... beans) {
        addBeans(Arrays.asList(beans));
    }

    public QTable addBeans(Collection<T> beans) {
        if (!beans.isEmpty()) {
            if (isContainerInitialized()) {
                bic.addAll(beans);
            } else {
                ensureBeanItemContainer(beans);
            }
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public QTable setBeans(T... beans) {
        setBeans(Arrays.asList(beans));
        return this;
    }

    public QTable setBeans(Collection<T> beans) {
        if (!isContainerInitialized() && !beans.isEmpty()) {
            ensureBeanItemContainer(beans);
        } else if (isContainerInitialized()) {
            bic.setCollection(beans);
        }
        return this;
    }

    public QTable<T> withFullWidth() {
        setWidth(100, Unit.PERCENTAGE);
        return this;
    }

    public QTable<T> withHeight(String height) {
        setHeight(height);
        return this;
    }

    public QTable<T> withFullHeight() {
        return withHeight("100%");
    }

    public QTable<T> withWidth(String width) {
        setWidth(width);
        return this;
    }

    public QTable<T> withCaption(String caption) {
        setCaption(caption);
        return this;
    }

    @Override
    public void setValue(Object newValue) throws ReadOnlyException {
        super.setValue(newValue);
        fireValueChange(true);
    }

    @Override
    protected String formatPropertyValue(Object rowId, Object colId, Property<?> property) {
        if (formattedColumnPatterns != null && !formattedColumnPatterns.isEmpty()
                && formattedColumnPatterns.containsKey(colId.toString())
                && formattedColumnPatterns.get(colId.toString()) != null
                && property.getValue() != null) {
            Properties pro = formattedColumnPatterns.get(colId.toString());
            if (pro.getProperty(PROPERTIES_TYPE).equalsIgnoreCase("Date")) {
                sdf.applyPattern(pro.getProperty(PROPERTIES_PATTERN));
                return sdf.format((Date) property.getValue());
            } else if (pro.getProperty(PROPERTIES_TYPE).equalsIgnoreCase("Number")) {
                df.applyPattern(pro.getProperty(PROPERTIES_PATTERN));
                return df.format(property.getValue());
            }
        } else if (property.getType() == Boolean.class) {
            if (property.getValue() == null) {
                return "";
            } else if ((Boolean) property.getValue()) {
                return "( ✔ )";
            } else {
                return "(  )";
            }
        }
        return super.formatPropertyValue(rowId, colId, property);
    }

    public void setColumnAlignments(Align columnAlignment) {
        Align[] aligns = new Align[getVisibleColumns().length];
        Arrays.fill(aligns, Align.RIGHT);
        setColumnAlignments(aligns);
    }
    private static final long serialVersionUID = -9210620149843463514L;
}
