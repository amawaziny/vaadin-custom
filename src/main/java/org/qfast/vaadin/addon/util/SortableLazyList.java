/*
 * Copyright 2015 QFast Ahmed El-mawaziny.
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

import java.io.Serializable;
import java.util.List;

/**
 * @param <T>
 * @author Ahmed El-mawaziny
 */
public class SortableLazyList<T> extends LazyList<T> implements Serializable {

    private static final long serialVersionUID = 7731703656109956142L;
    private final SortablePagingProvider sortablePageProvider;
    private boolean sortAscending = true;
    private String sortProperty;
    // Split into subinterfaces for better Java 8 lambda support

    /**
     * Constructs a new LazyList with given provider and default page size of
     * DEFAULT_PAGE_SIZE (30).
     *
     * @param dataProvider the data provider that is used to fetch pages of
     *                     entities and to detect the total count of entities
     */
    public SortableLazyList(SortableEntityProvider dataProvider) {
        this(dataProvider, DEFAULT_PAGE_SIZE);
    }

    /**
     * Constructs a new LazyList with given provider and default page size of
     * DEFAULT_PAGE_SIZE (30).
     *
     * @param dataProvider the data provider that is used to fetch pages of
     *                     entities and to detect the total count of entities
     * @param pageSize     the page size to be used
     */
    public SortableLazyList(SortableEntityProvider dataProvider, int pageSize) {
        super(dataProvider, pageSize);
        this.sortablePageProvider = dataProvider;
    }

    /**
     * Constructs a new LazyList with given providers and default page size of
     * DEFAULT_PAGE_SIZE (30).
     *
     * @param pageProvider  the interface via "pages" of entities are requested
     * @param countProvider the interface via the total count of entities is
     *                      detected.
     */
    public SortableLazyList(SortablePagingProvider pageProvider, CountProvider countProvider) {
        this(pageProvider, countProvider, DEFAULT_PAGE_SIZE);
    }

    /**
     * Constructs a new LazyList with given providers and page size.
     *
     * @param pageProvider  the interface via "pages" of entities are requested
     * @param countProvider the interface via the total count of entities is
     *                      detected.
     * @param pageSize      the page size that should be used
     */
    public SortableLazyList(SortablePagingProvider pageProvider, CountProvider countProvider, int pageSize) {
        super(countProvider, pageSize);
        this.sortablePageProvider = pageProvider;
    }

    public void sort(boolean ascending, String property) {
        sortAscending = ascending;
        sortProperty = property;
        // TODO resetting size at this point is actually obsolete?
        reset();
    }

    @Override
    protected List findEntities(int i) {
        return sortablePageProvider.findEntities(i, isSortAscending(), getSortProperty());
    }

    public boolean isSortAscending() {
        return sortAscending;
    }

    public void setSortAscending(boolean sortAscending) {
        this.sortAscending = sortAscending;
    }

    public String getSortProperty() {
        return sortProperty;
    }

    public void setSortProperty(String sortProperty) {
        this.sortProperty = sortProperty;
    }

    /**
     * Interface via the LazyList communicates with the "backend"
     *
     * @param <T> The type of the objects in the list
     */
    public interface SortablePagingProvider<T> extends Serializable {

        /**
         * Fetches one "page" of entities form the backend. The amount
         * "maxResults" should match with the value configured for the LazyList
         *
         * @param firstRow      the index of first row that should be fetched
         * @param sortAscending the direction to be used for sorting, true if
         *                      ascending
         * @param property      the property based on the sorting should be done,
         *                      null for natural order
         * @return a sub list from given first index
         */
        public List<T> findEntities(int firstRow, boolean sortAscending, String property);
    }

    /**
     * Interface via the LazyList communicates with the "backend"
     *
     * @param <T> The type of the objects in the list
     */
    public interface SortableEntityProvider<T> extends SortablePagingProvider, CountProvider {
    }
}
