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
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.Resource;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Component;
import com.vaadin.util.ReflectTools;
import org.apache.commons.lang3.StringUtils;
import org.qfast.vaadin.addon.data.ValueChangedEvent;
import org.qfast.vaadin.addon.data.ValueChangedEventImpl;
import org.qfast.vaadin.addon.data.ValueChangedListener;
import org.qfast.vaadin.addon.util.LazyList;
import org.qfast.vaadin.addon.util.ListContainer;
import org.qfast.vaadin.addon.util.SortableLazyList;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author Ahmed El-mawaziny on 11/15/15.
 */
public class Table<T> extends com.vaadin.ui.Table {
    private static final long serialVersionUID = 4609878210742796583L;

    private ListContainer<T> bic;
    private String[] pendingProperties;
    private String[] pendingHeaders;
    private Map<String, Properties> formattedColumnPatterns;
    private Collection sortableProperties;
    private ItemClickEvent.ItemClickListener itemClickPiggyback;
    private boolean isSorting = false;

    public static final String PROPERTIES_TYPE = "type";
    public static final String PROPERTIES_PATTERN = "pattern";
    private final SimpleDateFormat sdf = new SimpleDateFormat();
    private final DecimalFormat df = new DecimalFormat();

    public Table() {
    }

    /**
     * Constructs a Table with explicit bean type. Handy for example if your
     * beans are JPA proxies or the table is empty when showing it initially.
     *
     * @param type the type of beans that are listed in this table
     */
    public Table(Class<T> type) {
        bic = createContainer(type);
        setContainerDataSource(bic);
    }

    @SafeVarargs
    public Table(T... beans) {
        this(new ArrayList<T>(Arrays.asList(beans)));
    }

    /**
     * A shorthand to create Table using LazyList. By default page size of
     * LazyList.DEFAULT_PAGE_SIZE (30) is used.
     *
     * @param pageProvider  the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     */
    @SuppressWarnings("unchecked")
    public Table(LazyList.PagingProvider pageProvider,
                 LazyList.CountProvider countProvider) {
        this(new LazyList(pageProvider, countProvider, LazyList.DEFAULT_PAGE_SIZE));
    }

    /**
     * A shorthand to create Table using LazyList.
     *
     * @param pageProvider  the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     * @param pageSize      the page size (aka maxResults) that is used in paging.
     */
    @SuppressWarnings("unchecked")
    public Table(LazyList.PagingProvider pageProvider,
                 LazyList.CountProvider countProvider, int pageSize) {
        this(new LazyList(pageProvider, countProvider, pageSize));
    }

    /**
     * A shorthand to create Table using SortableLazyList. By default page size
     * of LazyList.DEFAULT_PAGE_SIZE (30) is used.
     *
     * @param pageProvider  the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     */
    @SuppressWarnings("unchecked")
    public Table(SortableLazyList.SortablePagingProvider pageProvider,
                 LazyList.CountProvider countProvider) {
        this(new SortableLazyList(pageProvider, countProvider, LazyList.DEFAULT_PAGE_SIZE));
    }

    /**
     * A shorthand to create Table using SortableLazyList.
     *
     * @param pageProvider  the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     * @param pageSize      the page size (aka maxResults) that is used in paging.
     */
    @SuppressWarnings("unchecked")
    public Table(SortableLazyList.SortablePagingProvider pageProvider,
                 LazyList.CountProvider countProvider, int pageSize) {
        this(new SortableLazyList(pageProvider, countProvider, pageSize));
    }

    public Table(Collection<T> beans) {
        this();
        if (beans != null) {
            bic = createContainer(beans);
            setContainerDataSource(bic);
        }
    }

    /**
     * Makes the table lazy load its content with given strategy.
     *
     * @param pageProvider  the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     * @return this Table object
     */
    @SuppressWarnings("unchecked")
    public Table<T> lazyLoadFrom(LazyList.PagingProvider pageProvider,
                                 LazyList.CountProvider countProvider) {
        setBeans(new LazyList(pageProvider, countProvider, LazyList.DEFAULT_PAGE_SIZE));
        return this;
    }

    /**
     * Makes the table lazy load its content with given strategy.
     *
     * @param pageProvider  the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     * @param pageSize      the page size (aka maxResults) that is used in paging.
     * @return this Table object
     */
    @SuppressWarnings("unchecked")
    public Table<T> lazyLoadFrom(LazyList.PagingProvider pageProvider,
                                 LazyList.CountProvider countProvider, int pageSize) {
        setBeans(new LazyList(pageProvider, countProvider, pageSize));
        return this;
    }

    /**
     * Makes the table lazy load its content with given strategy.
     *
     * @param pageProvider  the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     * @return this Table object
     */
    @SuppressWarnings("unchecked")
    public Table<T> lazyLoadFrom(SortableLazyList.SortablePagingProvider pageProvider,
                                 LazyList.CountProvider countProvider) {
        setBeans(new SortableLazyList(pageProvider, countProvider, LazyList.DEFAULT_PAGE_SIZE));
        return this;
    }

    /**
     * Makes the table lazy load its content with given strategy.
     *
     * @param pageProvider  the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     * @param pageSize      the page size (aka maxResults) that is used in paging.
     * @return this Table object
     */
    @SuppressWarnings("unchecked")
    public Table<T> lazyLoadFrom(SortableLazyList.SortablePagingProvider pageProvider,
                                 LazyList.CountProvider countProvider, int pageSize) {
        setBeans(new SortableLazyList(pageProvider, countProvider, pageSize));
        return this;
    }

    protected ListContainer<T> createContainer(Class<T> type) {
        return new ListContainer<T>(type);
    }

    protected ListContainer<T> createContainer(Collection<T> beans) {
        return new ListContainer<T>(beans);
    }

    protected ListContainer<T> getContainer() {
        return bic;
    }

    public Table<T> withProperties(String... visibleProperties) {
        if (isContainerInitialized()) {
            bic.setContainerPropertyIds(visibleProperties);
            setVisibleColumns((Object[]) visibleProperties);
        } else {
            pendingProperties = visibleProperties;
            for (String string : visibleProperties) {
                addContainerProperty(string, String.class, "");
            }
        }
        for (String visibleProperty : visibleProperties) {
            String[] parts = StringUtils.splitByCharacterTypeCamelCase(
                    visibleProperty);
            parts[0] = StringUtils.capitalize(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                parts[i] = parts[i].toLowerCase();
            }
            String saneCaption = StringUtils.join(parts, " ");
            setColumnHeader(visibleProperty, saneCaption);
        }
        return this;
    }

    protected boolean isContainerInitialized() {
        return bic != null;
    }

    public Table<T> withColumnHeaders(String... columnNamesForVisibleProperties) {
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

    /**
     * Explicitly sets which properties are sortable in the UI.
     *
     * @param sortableProperties the collection of property identifiers/names
     *                           that should be sortable
     * @return the Table instance
     */
    public Table<T> setSortableProperties(Collection sortableProperties) {
        this.sortableProperties = sortableProperties;
        return this;
    }

    public Collection getSortableProperties() {
        return sortableProperties;
    }

    /**
     * Explicitly sets which properties are sortable in the UI.
     *
     * @param sortableProperties the collection of property identifiers/names
     *                           that should be sortable
     * @return the Table instance
     */
    public Table<T> setSortableProperties(String... sortableProperties) {
        this.sortableProperties = Arrays.asList(sortableProperties);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<?> getSortableContainerPropertyIds() {
        if (getSortableProperties() != null) {
            return Collections.unmodifiableCollection(sortableProperties);
        }
        return super.getSortableContainerPropertyIds();
    }

    public void addValueChangedListener(ValueChangedListener<T> listener) {
        addListener(ValueChangedEvent.class, listener,
                ValueChangedEventImpl.VALUE_CHANGE_METHOD);
        // implicitly consider the table should be selectable
        setSelectable(true);
        // Needed as client side checks only for "real value change listener"
        setImmediate(true);
    }

    public void removeValueChangedListener(ValueChangedListener<T> listener) {
        removeListener(ValueChangedEvent.class, listener,
                ValueChangedEventImpl.VALUE_CHANGE_METHOD);
        setSelectable(hasListeners(ValueChangedEvent.class));
    }

    @Override
    public void fireValueChange(boolean repaintIsNotNeeded) {
        super.fireValueChange(repaintIsNotNeeded);
        fireEvent(new ValueChangedEventImpl(this));
    }

    protected void ensureBeanItemContainer(Collection<T> beans) {
        if (!isContainerInitialized()) {
            bic = createContainer(beans);
            if (pendingProperties != null) {
                bic.setContainerPropertyIds(pendingProperties);
                pendingProperties = null;
            }
            setContainerDataSource(bic);
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

    @Override
    @Deprecated
    public void setMultiSelect(boolean multiSelect) {
        super.setMultiSelect(multiSelect);
    }

    @SafeVarargs
    public final Table<T> addBeans(T... beans) {
        addBeans(Arrays.asList(beans));
        return this;
    }

    public Table<T> addBeans(Collection<T> beans) {
        if (!beans.isEmpty()) {
            if (isContainerInitialized()) {
                bic.addAll(beans);
            } else {
                ensureBeanItemContainer(beans);
            }
        }
        return this;
    }

    @SafeVarargs
    public final Table<T> setBeans(T... beans) {
        setBeans(new ArrayList<T>(Arrays.asList(beans)));
        return this;
    }

    public Table<T> setBeans(Collection<T> beans) {
        if (!isContainerInitialized() && !beans.isEmpty()) {
            ensureBeanItemContainer(beans);
        } else if (isContainerInitialized()) {
            bic.setCollection(beans);
        }
        return this;
    }

    /**
     * Makes the first column of the table a primary column, for which all
     * space left out from other columns is given. The method also makes sure
     * the Table has a width defined (otherwise the setting makes no sense).
     * *
     *
     * @return {@link Table}
     */
    public Table<T> expandFirstColumn() {
        expand(getContainerPropertyIds().iterator().next().toString());
        if (getWidth() == -1) {
            return withFullWidth();
        }
        return this;
    }

    public Table<T> withFullWidth() {
        setWidth(100, Unit.PERCENTAGE);
        return this;
    }

    public Table<T> withHeight(String height) {
        setHeight(height);
        return this;
    }

    public Table<T> withFullHeight() {
        return withHeight("100%");
    }

    public Table<T> withWidth(String width) {
        setWidth(width);
        return this;
    }

    public Table<T> withCaption(String caption) {
        setCaption(caption);
        return this;
    }

    public Table<T> withStyleName(String styleName) {
        setStyleName(styleName);
        return this;
    }

    public Table<T> withIcon(Resource icon) {
        setIcon(icon);
        return this;
    }

    public Table<T> expand(String... propertiesToExpand) {
        for (String property : propertiesToExpand) {
            setColumnExpandRatio(property, 1);
        }
        return this;
    }

    private void ensureTypedItemClickPiggybackListener() {
        if (itemClickPiggyback == null) {
            itemClickPiggyback = (ItemClickEvent.ItemClickListener) event -> fireEvent(new RowClickEvent<T>(event));
            addItemClickListener(itemClickPiggyback);
        }
    }

    @SuppressWarnings("unchecked")
    public Table<T> withGeneratedColumn(String columnId, final SimpleColumnGenerator<T> columnGenerator) {
        addGeneratedColumn(columnId, (ColumnGenerator)
                (source, itemId, columnId1) -> columnGenerator.generate((T) itemId));
        return this;
    }

    public Table addSortListener(SortListener listener) {
        addListener(SortEvent.class, listener, SortEvent.method);
        return this;
    }

    public Table removeSortListener(SortListener listener) {
        removeListener(SortEvent.class, listener, SortEvent.method);
        return this;
    }

    @Override
    public void sort(Object[] propertyId, boolean[] ascending) throws UnsupportedOperationException {
        if (isSorting) {
            // hack to avoid recursion
            return;
        }

        boolean refreshingPreviouslyEnabled = disableContentRefreshing();
        boolean defaultTableSortingMethod = false;
        try {
            isSorting = true;

            // create sort event and fire it, allow user to prevent default
            // operation
            final boolean sortAscending = ascending != null && ascending.length > 0 ? ascending[0] : true;
            final String sortProperty = propertyId != null && propertyId.length > 0 ? propertyId[0].
                    toString() : null;

            final SortEvent sortEvent = new SortEvent(this, sortAscending, sortProperty);
            fireEvent(sortEvent);

            if (!sortEvent.isPreventContainerSort()) {
                // if not prevented, do sorting
                if (bic != null && bic.getItemIds() instanceof SortableLazyList) {
                    // Explicit support for SortableLazyList, set sort parameters
                    // it uses to backend services and clear internal buffers
                    @SuppressWarnings("unchecked")
                    SortableLazyList<T> sll = (SortableLazyList) bic.getItemIds();
                    if (ascending == null || ascending.length == 0) {
                        sll.sort(true, null);
                    } else {
                        sll.sort(ascending[0], propertyId[0].toString());
                    }
                    resetPageBuffer();
                } else {
                    super.sort(propertyId, ascending);
                    defaultTableSortingMethod = true;
                }
            }
            if (!defaultTableSortingMethod) {
                // Ensure the values used in UI are set as this method is public
                // and can be called by both UI event and app logic
                setSortAscending(sortAscending);
                setSortContainerPropertyId(sortProperty);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            isSorting = false;
            if (refreshingPreviouslyEnabled) {
                enableContentRefreshing(true);
            }
        }
    }

    public void addRowClickListener(RowClickListener<T> listener) {
        ensureTypedItemClickPiggybackListener();
        addListener(RowClickEvent.class, listener, RowClickEvent.TYPED_ITEM_CLICK_METHOD);
    }

    public void removeRowClickListener(RowClickListener<T> listener) {
        removeListener(RowClickEvent.class, listener, RowClickEvent.TYPED_ITEM_CLICK_METHOD);
    }

    /**
     * Clears caches in case the Table is backed by a LazyList implementation.
     * Also resets "pageBuffer" used by table. If you know you have changes in
     * the listing, you can call this method to ensure the UI gets updated.
     */
    public void resetLazyList() {
        if (bic != null && bic.getItemIds() instanceof LazyList) {
            ((LazyList) bic.getItemIds()).reset();
        }
        resetPageBuffer();
    }

    /**
     * Sets the row of given entity as selected. This is practically a better
     * typed version for select(Object) and setValue(Object) methods.
     *
     * @param entity the entity whose row should be selected
     * @return the Table instance
     */
    public Table<T> setSelected(T entity) {
        setValue(entity);
        return this;
    }

    public void setVisibleColumns(LinkedHashMap<String, String> visibleColumns) {
        Set<String> columns = visibleColumns.keySet();
        setVisibleColumns(columns.toArray(new Object[columns.size()]));
        Collection<String> headers = visibleColumns.values();
        setColumnHeaders(headers.toArray(new String[headers.size()]));
    }

    public void setFormattedColumnPatterns(Map<String, Properties> formattedColumnPatterns) {
        this.formattedColumnPatterns = formattedColumnPatterns;
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

    public static interface SimpleColumnGenerator<T> {

        public Object generate(T entity);
    }

    /**
     * A listener that can be used to track when user sorts table on a column.
     * <p>
     * Via the event user can also prevent the "container sort" done by the
     * Table and implement own sorting logic instead (e.g. get a sorted list of
     * entities from the backend).
     */
    public interface SortListener {

        public void onSort(SortEvent event);

    }

    /**
     * A better typed version of ItemClickEvent.
     *
     * @param <T> the type of entities listed in the table
     */
    public interface RowClickListener<T> extends Serializable {
        public void rowClick(RowClickEvent<T> event);
    }

    public static class SortEvent extends Component.Event {

        private final static Method method = ReflectTools.findMethod(
                SortListener.class, "onSort",
                SortEvent.class);
        private final boolean sortAscending;
        private final String sortProperty;
        private boolean preventContainerSort = false;

        public SortEvent(Component source, boolean sortAscending,
                         String property) {
            super(source);
            this.sortAscending = sortAscending;
            this.sortProperty = property;
        }

        public String getSortProperty() {
            return sortProperty;
        }

        public boolean isSortAscending() {
            return sortAscending;
        }

        /**
         * By calling this method you can prevent the sort call to the container
         * used by Table. In this case you most most probably you want to
         * manually sort the container instead.
         */
        public void preventContainerSort() {
            preventContainerSort = true;
        }

        public boolean isPreventContainerSort() {
            return preventContainerSort;
        }

    }

    /**
     * A version of ItemClickEvent that is properly typed and named.
     *
     * @param <T>
     */
    public static class RowClickEvent<T> extends MouseEvents.ClickEvent {

        public static final Method TYPED_ITEM_CLICK_METHOD;

        static {
            try {
                TYPED_ITEM_CLICK_METHOD = RowClickListener.class.getDeclaredMethod("rowClick", new Class[]{RowClickEvent.class});
            } catch (final java.lang.NoSuchMethodException e) {
                // This should never happen
                throw new java.lang.RuntimeException();
            }
        }

        private final ItemClickEvent orig;

        public RowClickEvent(ItemClickEvent orig) {
            super(orig.getComponent(), null);
            this.orig = orig;
        }

        /**
         * @return the entity(~row) that was clicked.
         */
        @SuppressWarnings("unchecked")
        public T getEntity() {
            return (T) orig.getItemId();
        }

        /**
         * @return the entity(~row) that was clicked.
         */
        public T getRow() {
            return getEntity();
        }

        /**
         * @return the identifier of the column on which the row click happened.
         */
        public String getColumnId() {
            return orig.getPropertyId().toString();
        }

        @Override
        public MouseEventDetails.MouseButton getButton() {
            return orig.getButton();
        }

        @Override
        public int getClientX() {
            return orig.getClientX();
        }

        @Override
        public int getClientY() {
            return orig.getClientY();
        }

        @Override
        public int getRelativeX() {
            return orig.getRelativeX();
        }

        @Override
        public int getRelativeY() {
            return orig.getRelativeY();
        }

        @Override
        public boolean isAltKey() {
            return orig.isAltKey();
        }

        @Override
        public boolean isCtrlKey() {
            return orig.isCtrlKey();
        }

        @Override
        public boolean isDoubleClick() {
            return orig.isDoubleClick();
        }

        @Override
        public boolean isMetaKey() {
            return orig.isMetaKey();
        }

        @Override
        public boolean isShiftKey() {
            return orig.isShiftKey();
        }

    }
}
