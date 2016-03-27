package org.vaadin.kim.filterabletwincolselect;

import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.qfast.vaadin.addon.ui.Button;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class FilterableTwinColSelect extends CustomField<Set> implements
        Container.Viewer, Container.ItemSetChangeListener {

    private static final long serialVersionUID = 1L;

    private Container containerDataSource;

    private TextField filterUnselected = new TextField();

    private TextField filterSelected = new TextField();

    private ListSelect unselected = new ListSelect();

    private ListSelect selected = new ListSelect();

    private ClickListener addSelectedListener;

    private ClickListener addAllListener;

    private ClickListener removeSelectedListener;

    private ClickListener removeAllListener;

    private CssLayout buttonLayout;

    private Object itemCaptionPropertyId;

    private IndexedContainer selectedContainer = new IndexedContainer();

    private IndexedContainer unselectedContainer = new IndexedContainer();

    // Property <-> ItemId map
    private Map<Property.ValueChangeNotifier, Object> properties = new HashMap<>();

    private HorizontalLayout layout;

    public FilterableTwinColSelect() {
        unselectedContainer.addContainerProperty("caption", Object.class, null);
        selectedContainer.addContainerProperty("caption", Object.class, null);
        setValue(new HashSet<>(), false);

        layout = new HorizontalLayout();
        layout.setSizeFull();
        setWidth("300px");
        setHeight("200px");
        unselected.setContainerDataSource(unselectedContainer);
        selected.setContainerDataSource(selectedContainer);
    }

    @Override
    protected Component initContent() {
        Layout unselectedColumn = initializeUnselectedColumn();
        layout.addComponent(unselectedColumn);

        Layout buttonColumn = initializeButtons();
        layout.addComponent(buttonColumn);

        Layout selectedColumn = initializeSelectedColumn();
        layout.addComponent(selectedColumn);

        layout.setExpandRatio(unselectedColumn, 1);
        layout.setExpandRatio(selectedColumn, 1);

        layout.setSpacing(true);
        layout.setComponentAlignment(buttonColumn, Alignment.MIDDLE_CENTER);
        return layout;
    }

    private Layout initializeUnselectedColumn() {
        VerticalLayout unselectedColumn = new VerticalLayout();
        unselectedColumn.setSizeFull();

        filterUnselected.addTextChangeListener(event -> {
            unselectedContainer.removeAllContainerFilters();
            unselectedContainer.addContainerFilter("caption", event.getText(), true, false);
        });
        filterUnselected.setWidth("100%");

//		unselected.setContainerDataSource(unselectedContainer);
        unselected.setMultiSelect(true);
        unselected.setSizeFull();
        unselected.setItemCaptionPropertyId("caption");
        unselectedContainer.sort(new String[]{"caption"}, new boolean[]{true});

        unselectedColumn.addComponent(filterUnselected);
        unselectedColumn.addComponent(unselected);
        unselectedColumn.setExpandRatio(unselected, 1);

        return unselectedColumn;
    }

    private Layout initializeSelectedColumn() {
        VerticalLayout selectedColumn = new VerticalLayout();
        selectedColumn.setSizeFull();

        filterSelected.setWidth("100%");
        filterSelected.addTextChangeListener(event -> {
            selectedContainer.removeAllContainerFilters();
            selectedContainer.addContainerFilter("caption", event.getText(), true, false);
        });

//		selected.setContainerDataSource(selectedContainer);
        selected.setMultiSelect(true);
        selected.setSizeFull();
        selected.setItemCaptionPropertyId("caption");
        selectedContainer.sort(new String[]{"caption"}, new boolean[]{true});

        selectedColumn.addComponent(filterSelected);
        selectedColumn.addComponent(selected);
        selectedColumn.setExpandRatio(selected, 1);

        return selectedColumn;
    }

    @SuppressWarnings("unchecked")
    private void select() {
        if (unselected.getValue() == null) {
            return;
        }

        // Add all selected items to the selected ListSelect
        for (Object itemId : (Collection) unselected.getValue()) {
            copyItem(itemId, unselected.getContainerDataSource(), selected.getContainerDataSource());
            getValue().add(itemId);
        }

        // Remove all selected items from the unselected ListSelect
        new HashSet<>((Collection) unselected.getValue()).forEach(unselected::removeItem);
        fireValueChange(false);

    }

    @SuppressWarnings("unchecked")
    private void copyItem(Object itemId, Container fromContainer, Container toContainer) {
        if (fromContainer.getItem(itemId) == null) {
            return;
        }

        Item item = toContainer.addItem(itemId);
        if (item == null) {
            return;
        }
        Object caption;
        if (fromContainer == selectedContainer || fromContainer == unselectedContainer) {
            caption = fromContainer.getContainerProperty(itemId, "caption").getValue();
        } else {
            caption = itemCaptionPropertyId == null ? itemId :
                    fromContainer.getContainerProperty(itemId, itemCaptionPropertyId).getValue();
        }

        item.getItemProperty("caption").setValue(caption);
    }

    @SuppressWarnings("unchecked")
    private void selectAll() {
        if (unselected.getValue() == null) {
            return;
        }

        // Add all selected items to the selected ListSelect
        for (Object itemId : unselected.getItemIds()) {
            copyItem(itemId, unselected.getContainerDataSource(), selected.getContainerDataSource());
            getValue().add(itemId);
        }

        // Remove all selected items from the unselected ListSelect
        // Don't use removeAllItems(), since there might be filters applied
        new HashSet<>(unselected.getItemIds()).forEach(unselected::removeItem);

        fireValueChange(false);
    }

    @SuppressWarnings("unchecked")
    private void deselect() {
        if (selected.getValue() == null) {
            return;
        }

        // Add all selected items to the selected ListSelect
        for (Object itemId : (Collection) selected.getValue()) {
            copyItem(itemId, selected.getContainerDataSource(),
                    unselected.getContainerDataSource());
            getValue().remove(itemId);
        }

        // Remove all selected items from the unselected ListSelect
        new HashSet<>((Collection) selected.getValue()).forEach(selected::removeItem);
        fireValueChange(false);
    }

    private void deselectAll() {
        if (selected.getValue() == null) {
            return;
        }

        // Add all selected items to the selected ListSelect
        for (Object itemId : selected.getItemIds()) {
            copyItem(itemId, selected.getContainerDataSource(), unselected.getContainerDataSource());
        }

        // Remove all selected items from the unselected ListSelect
        // Don't use removeAllItems(), since there might be filters applied
        for (Object itemId : new HashSet<Object>(selected.getItemIds())) {
            selected.removeItem(itemId);
            getValue().remove(itemId);
        }
        fireValueChange(false);
    }

    private Layout initializeButtons() {
        buttonLayout = new CssLayout();
        buttonLayout.setWidth(40, Unit.PIXELS);

        initializeListeners();

        buttonLayout.addComponent(createButton(FontAwesome.CHEVRON_RIGHT, addSelectedListener));
        buttonLayout.addComponent(createButton(FontAwesome.FORWARD, addAllListener));
        buttonLayout.addComponent(createButton(FontAwesome.BACKWARD, removeAllListener));
        buttonLayout.addComponent(createButton(FontAwesome.CHEVRON_LEFT, removeSelectedListener));

        return buttonLayout;
    }

    private void initializeListeners() {
        addSelectedListener = e -> select();
        addAllListener = e -> selectAll();
        removeSelectedListener = e -> deselect();
        removeAllListener = event -> deselectAll();
    }

    private Button createButton(Resource resource, ClickListener listener) {
        Button button = new Button(resource, listener);
        button.setWidth(100, Unit.PERCENTAGE);
        button.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        return button;
    }

    @Override
    public Class<Set> getType() {
        return Set.class;
    }

    @SuppressWarnings("unchecked")
    private void updateContainers(Container newDataSource, boolean preserveSelections) {
        containerDataSource = newDataSource;
        unselectedContainer.removeAllItems();
        selectedContainer.removeAllItems();
        clearAllValueChangeListeners();

        // In case new datasource is null, then clear all selections
        if (newDataSource == null) {
            getValue().clear();
            fireValueChange(true);
            return;
        }

        // Copy items to the new container
        for (Object itemId : newDataSource.getItemIds()) {
            copyItem(itemId, newDataSource, unselectedContainer);
        }

        if (preserveSelections) {
            // Add all selected items to the selected ListSelect
            for (Object itemId : getValue()) {
                copyItem(itemId, unselectedContainer, selectedContainer);
            }

            // Remove all selected items from the unselected ListSelect
            boolean fireEvent = false;
            Set<Object> itemsToRemove = new HashSet<Object>();
            for (Object itemId : getValue()) {
                if (unselected.getItem(itemId) == null) {
                    itemsToRemove.add(itemId);
                    fireEvent = true;
                } else {
                    unselected.removeItem(itemId);
                }
            }
            getValue().removeAll(itemsToRemove);
            if (fireEvent) {
                fireValueChange(true);
            }
        } else {
            getValue().clear();
            fireValueChange(true);
        }
        registerPropertyListeners();
    }

    @Override
    public Container getContainerDataSource() {
        return containerDataSource;
    }

    @Override
    public void setContainerDataSource(Container newDataSource) {
        // If the same container is already set, then ignore this method call
        if (newDataSource == containerDataSource) {
            return;
        }

        // Check if the currently assigned itemCaptionPropertyId exists in the
        // new container
        if (newDataSource != null
                && !newDataSource.getContainerPropertyIds().contains(
                itemCaptionPropertyId)) {
            itemCaptionPropertyId = null;
        }

        if (containerDataSource != null && containerDataSource instanceof Container.ItemSetChangeNotifier) {
            ((Container.ItemSetChangeNotifier) containerDataSource).removeItemSetChangeListener(this);
        }
        containerDataSource = newDataSource;
        if (containerDataSource instanceof Container.ItemSetChangeNotifier) {
            ((Container.ItemSetChangeNotifier) containerDataSource).addItemSetChangeListener(this);
        }
        updateContainers(newDataSource, false);
    }

    /**
     * Returns the text shown above the right column.
     *
     * @return The text shown or null if not set.
     */
    public String getRightColumnCaption() {
        return filterSelected.getCaption();
    }

    /**
     * Sets the text shown above the right column.
     *
     * @param rightColumnCaption The text to show
     */
    public void setRightColumnCaption(String rightColumnCaption) {
        filterSelected.setCaption(rightColumnCaption);
    }

    /**
     * Returns the text shown above the left column.
     *
     * @return The text shown or null if not set.
     */
    public String getLeftColumnCaption() {
        return filterUnselected.getCaption();
    }

    /**
     * Sets the text shown above the left column.
     *
     * @param leftColumnCaption The text to show
     */
    public void setLeftColumnCaption(String leftColumnCaption) {
        filterUnselected.setCaption(leftColumnCaption);
    }

    @SuppressWarnings("unchecked")
    public void setItemCaption(Object itemId, String caption) {
        Item item = unselectedContainer.getItem(itemId);
        if (item != null) {
            item.getItemProperty("caption").setValue(caption);
        }
        item = selectedContainer.getItem(itemId);
        if (item != null) {
            item.getItemProperty("caption").setValue(caption);
        }
    }

    public String getItemCaption(Object itemId) {
        Object caption = unselectedContainer.getContainerProperty(itemId, "caption").getValue();
        if (caption == null) {
            caption = selected.getContainerProperty(itemId, "caption").getValue();
        }

        return caption == null ? null : caption.toString();
    }

    public Object getItemCaptionPropertyId() {
        return itemCaptionPropertyId;
    }

    public void setItemCaptionPropertyId(Object propertyId) {
        itemCaptionPropertyId = propertyId;
        clearAllValueChangeListeners();
        registerPropertyListeners();
        updateItemCaptions();
    }

    public void setButtonWidth(String width) {
        buttonLayout.setWidth(width);
    }

    @Override
    public void containerItemSetChange(ItemSetChangeEvent event) {
        updateContainers(containerDataSource, true);
    }

    @SuppressWarnings("unchecked")
    private void updateItemCaptions() {
        for (Object itemId : selectedContainer.getItemIds()) {
            Object caption = containerDataSource.getContainerProperty(itemId, itemCaptionPropertyId).getValue();
            selectedContainer.getContainerProperty(itemId, "caption").setValue(caption);
        }
        for (Object itemId : unselectedContainer.getItemIds()) {
            Object caption = containerDataSource.getContainerProperty(itemId, itemCaptionPropertyId).getValue();
            unselectedContainer.getContainerProperty(itemId, "caption").setValue(caption);
        }
    }

    private void clearAllValueChangeListeners() {
        for (Property.ValueChangeNotifier property : properties.keySet()) {
            property.removeValueChangeListener(this);
        }
        properties.clear();
    }

    private void registerPropertyListeners() {
        if (itemCaptionPropertyId != null) {
            for (Object itemId : containerDataSource.getItemIds()) {
                Property property = containerDataSource.getContainerProperty(itemId, itemCaptionPropertyId);
                if (property instanceof Property.ValueChangeNotifier) {
                    ((Property.ValueChangeNotifier) property).addValueChangeListener(this);
                    properties.put((Property.ValueChangeNotifier) property, itemId);
                }

            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
        Object itemId = properties.get(event.getProperty());
        if (itemId != null) {
            Item item = selectedContainer.getItem(itemId);
            if (item != null) {
                item.getItemProperty("caption").setValue(
                        event.getProperty().getValue());
            }
            item = unselectedContainer.getItem(itemId);
            if (item != null) {
                item.getItemProperty("caption").setValue(
                        event.getProperty().getValue());
            }
        }
    }

    public void setFilterInputPrompt(String inputPrompt) {
        filterSelected.setInputPrompt(inputPrompt);
        filterUnselected.setInputPrompt(inputPrompt);
    }

    @Override
    public void setWidth(float width, Unit unit) {
        if (width < 0) {
            width = 300;
        }
        super.setWidth(width, unit);
    }

    @Override
    public void setHeight(float height, Unit unit) {
        if (height < 0) {
            height = 200;
        }
        super.setHeight(height, unit);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setValue(Set newFieldValue)
            throws com.vaadin.data.Property.ReadOnlyException,
            ConversionException {
        super.setValue(newFieldValue);

        if (newFieldValue == null) {
            clearAllSelections();
        }

        for (Object itemId : new HashSet((Collection) selected.getItemIds())) {
            if (!newFieldValue.contains(itemId)) {
                copyItem(itemId, selected.getContainerDataSource(),
                        unselected.getContainerDataSource());
                selected.removeItem(itemId);
            }
        }

        // Add all selected items to the selected ListSelect
        for (Object itemId : newFieldValue) {
            copyItem(itemId, unselected.getContainerDataSource(),
                    selected.getContainerDataSource());
        }

        // Remove all selected items from the unselected ListSelect
        for (Object itemId : newFieldValue) {
            unselected.removeItem(itemId);
        }

    }

    private void clearAllSelections() {
        // TODO Auto-generated method stub

    }

}
