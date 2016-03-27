package org.qfast.vaadin.addon.ui;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TwinColSelect;
import org.qfast.vaadin.addon.data.ValueChangedEvent;
import org.qfast.vaadin.addon.data.ValueChangedEventImpl;
import org.qfast.vaadin.addon.data.ValueChangedListener;
import org.qfast.vaadin.addon.util.CaptionGenerator;
import org.qfast.vaadin.addon.util.ListContainer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A select implementation with better typed API than in core Vaadin.
 *
 * By default the options toString is used to generate the caption for option.
 * To override this behavior, use setCaptionGenerator or override getCaption(T)
 * to provide your own strategy.
 * <p>
 * Behind the scenes uses Vaadin cores NativeSelect (default) or other cores
 * AbstractSelect implementation, type provided in constructor. Tree and Table
 * are not supported, see Table.
 * <p>
 *
 * @author mstahv
 * @param <T> the type of selects value
 */

@SuppressWarnings("unchecked")
public class TypedSelect<T> extends CustomField {

    private static final long serialVersionUID = -7750226747010945712L;
    private CaptionGenerator<T> captionGenerator;

    private AbstractSelect select;

    private ListContainer<T> bic;

    private Class<T> fieldType;

    /**
     * The type of element options in the select
     *
     * @param type the type of options in the list
     */
    public TypedSelect(Class<T> type) {
        this.fieldType = type;
        bic = new ListContainer<T>(type);
    }

    /**
     * Note, that with this constructor, you cannot override the select type.
     *
     * @param options options to select from
     */
    public TypedSelect(T... options) {
        setOptions(options);
    }

    public TypedSelect(String caption) {
        setCaption(caption);
    }

    /**
     * {@inheritDoc}
     *
     * Sets the width of the wrapped select component.
     *
     * @param width the new width for this select
     * @param unit the unit of the new width
     */
    @Override
    public void setWidth(float width, Unit unit) {
        if(select != null) {
            select.setWidth(width,unit);
        }
        super.setWidth(width, unit);
    }

    @Override
    public void addStyleName(String style) {
        getSelect().addStyleName(style);
        super.addStyleName(style);
    }

    @Override
    public void setStyleName(String style) {
        getSelect().setStyleName(style);
        super.setStyleName(style);
    }

    /**
     * Note, that with this constructor, you cannot override the select type.
     *
     * @param caption the caption for the select
     * @param options available options for the select
     */
    public TypedSelect(String caption, Collection<T> options) {
        this(caption);
        setOptions(options);
    }

    public TypedSelect<T> withCaption(String caption) {
        setCaption(caption);
        return this;
    }

    public TypedSelect<T> withSelectType(
            Class<? extends AbstractSelect> selectType) {
        if (selectType == ListSelect.class) {
            setSelectInstance(new ListSelect() {
                private static final long serialVersionUID = 8713366742149866626L;

                @SuppressWarnings("unchecked")
                @Override
                public String getItemCaption(Object itemId) {
                    return TypedSelect.this.getCaption((T) itemId);
                }
            });
        } else if (selectType == OptionGroup.class) {
            setSelectInstance(new OptionGroup() {
                private static final long serialVersionUID = 8806867604617076222L;

                @SuppressWarnings("unchecked")
                @Override
                public String getItemCaption(Object itemId) {
                    return TypedSelect.this.getCaption((T) itemId);
                }
            });
        } else if (selectType == ComboBox.class) {
            setSelectInstance(new ComboBox() {
                private static final long serialVersionUID = 7046818675216333140L;

                @SuppressWarnings("unchecked")
                @Override
                public String getItemCaption(Object itemId) {
                    return TypedSelect.this.getCaption((T) itemId);
                }
            });
        } else if (selectType == TwinColSelect.class) {
            setSelectInstance(new TwinColSelect() {
                private static final long serialVersionUID = 553014999694043514L;

                @SuppressWarnings("unchecked")
                @Override
                public String getItemCaption(Object itemId) {
                    return TypedSelect.this.getCaption((T) itemId);
                }
            });
        } else /*if (selectType == null || selectType == NativeSelect.class)*/ {
            setSelectInstance(new NativeSelect() {
                private static final long serialVersionUID = -3479220640758971716L;

                @SuppressWarnings("unchecked")
                @Override
                public String getItemCaption(Object itemId) {
                    return TypedSelect.this.getCaption((T) itemId);
                }
            });
        }
        return this;
    }

    protected void setSelectInstance(AbstractSelect select) {
        if(this.select != null) {
            piggyBackListener = null;
        }
        this.select = select;
    }

    /**
     *
     * @return the backing select instance, overriding this method may be
     * hazardous
     */
    protected AbstractSelect getSelect() {
        if (select == null) {
            withSelectType(null);
            if (bic != null) {
                select.setContainerDataSource(bic);
            }
        }
        ensurePiggybackListener();
        return select;
    }

    /**
     * Sets the input prompt used by the TypedSelect, in case the backing
     * instance supports inputprompt (read: is ComboBox).
     *
     * @param inputPrompt the input prompt
     * @return this TypedSelect instance
     */
    public TypedSelect<T> setInputPrompt(String inputPrompt) {
        if (getSelect() instanceof ComboBox) {
            ((ComboBox) getSelect()).setInputPrompt(inputPrompt);
        }
        return this;
    }

    protected String getCaption(T option) {
        if (captionGenerator != null) {
            return captionGenerator.getCaption(option);
        }
        return option.toString();
    }

    @Override
    public void focus() {
        getSelect().focus();
    }

    public final TypedSelect<T> setOptions(T... values) {
        return setOptions(Arrays.asList(values));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class getType() {

        if (fieldType == null) {
            try {
                fieldType = (Class<T>) ((Container.Sortable) select
                        .getContainerDataSource()).firstItemId().getClass();
            } catch (Exception e) {
                // If field type isn't set or can't be detected just report
                // Object, should be fine in most cases (vaadin will just
                // assign value without conversion.
                return Object.class;
            }
        }
        return fieldType;
    }

    /**
     * Explicitly sets the element type of the select.
     *
     * @param type the type of options in the select
     * @return this typed select instance
     */
    public TypedSelect setType(Class<T> type) {
        this.fieldType = type;
        return this;
    }

    /**
     * Explicitly sets the element type of the select.
     *
     * @param type the type of options in the select
     * @return this typed select instance
     */
    public TypedSelect setFieldType(Class<T> type) {
        this.fieldType = type;
        return this;
    }

    @Override
    public void setInvalidCommitted(boolean isCommitted) {
        super.setInvalidCommitted(isCommitted);
        getSelect().setInvalidCommitted(isCommitted);
    }

    @Override
    public void commit() throws SourceException, InvalidValueException {
        getSelect().commit();
        super.commit();
    }

    @Override
    public void discard() throws SourceException {
        getSelect().discard();
        super.discard();
    }

    /**
     * Buffering probably doesn't work correctly for this field, but in general
     * you should never use buffering either.
     * https://vaadin.com/web/matti/blog/-/blogs/3-pro-tips-for-vaadin-developers
     *
     * @param buffered
     */
    @Override
    public void setBuffered(boolean buffered) {
        getSelect().setBuffered(buffered);
        super.setBuffered(buffered);
    }

    @Override
    public boolean isBuffered() {
        return getSelect().isBuffered();
    }

    @Override
    protected void setInternalValue(Object newValue) {
        super.setInternalValue(newValue);
        getSelect().setValue(newValue);
    }

    public TypedSelect<T> addValueChangedListener(
            ValueChangedListener<T> listener) {
        addListener(ValueChangedEvent.class, listener,
                ValueChangedEventImpl.VALUE_CHANGE_METHOD);
        return this;
    }

    public TypedSelect<T> removeValueChangedListener(ValueChangedListener<T> listener) {
        removeListener(ValueChangedEvent.class, listener,
                ValueChangedEventImpl.VALUE_CHANGE_METHOD);
        return this;
    }

    @Override
    public int getTabIndex() {
        return getSelect().getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        getSelect().setTabIndex(tabIndex);
    }

    public CaptionGenerator<T> getCaptionGenerator() {
        return captionGenerator;
    }

    public TypedSelect<T> setCaptionGenerator(
            CaptionGenerator<T> captionGenerator) {
        this.captionGenerator = captionGenerator;
        return this;
    }

    public final TypedSelect<T> setOptions(Collection<T> options) {
        if (bic != null) {
            bic.setCollection(options);
        } else {
            bic = new ListContainer<T>(options);
        }
        getSelect().setContainerDataSource(bic);
        return this;
    }

    public final List<T> getOptions() {
        if (bic == null) {
            return Collections.EMPTY_LIST;
        } else {
            return (List<T>) bic.getItemIds();
        }
    }

    public TypedSelect<T> setNullSelectionAllowed(boolean nullAllowed) {
        getSelect().setNullSelectionAllowed(nullAllowed);
        return this;
    }

    public TypedSelect<T> setBeans(Collection<T> options) {
        return setOptions(options);
    }

    @Override
    public void attach() {
        if (bic != null && getSelect().getContainerDataSource() != bic) {
            getSelect().setContainerDataSource(bic);
        }
        super.attach();
    }

    private ValueChangeListener piggyBackListener;

    private void ensurePiggybackListener() {
        if (piggyBackListener == null) {
            piggyBackListener = new ValueChangeListener() {

                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    setValue(event.getProperty().getValue());
                    fireEvent(new ValueChangedEventImpl<T>(TypedSelect.this));
                }
            };
            getSelect().addValueChangeListener(piggyBackListener);
        }
    }

    @Override
    public T getValue() {
        return (T) super.getValue();
    }

    public TypedSelect<T> withFullWidth() {
        setWidth("100%");
        return this;
    }

    public TypedSelect<T> withReadOnly(boolean readOnly) {
        setReadOnly(readOnly);
        return this;
    }

    public TypedSelect<T> withValidator(Validator validator) {
        setImmediate(true);
        addValidator(validator);
        return this;
    }

    public TypedSelect<T> withWidth(float width, Unit unit) {
        setWidth(width, unit);
        return this;
    }

    public TypedSelect<T> withWidth(String width) {
        setWidth(width);
        return this;
    }

    public void selectFirst() {
        if (bic != null && bic.size() > 0) {
            getSelect().setValue(bic.getIdByIndex(0));
        }
    }

    @Override
    public void setValidationVisible(boolean validateAutomatically) {
        super.setValidationVisible(validateAutomatically);
        getSelect().setValidationVisible(validateAutomatically);
    }

    @Override
    public void removeStyleName(String style) {
        super.removeStyleName(style);
        getSelect().removeStyleName(style);
    }

    /**
     *
     * @return gets the ListContainer used by this component
     */
    protected ListContainer<T> getBic() {
        return bic;
    }

    /**
     *
     * @param listContainer sets the ListContainer used by this select. For
     * extensions only, should be set early or will fail.
     */
    protected void setBic(ListContainer<T> listContainer) {
        bic = listContainer;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        getSelect().setReadOnly(readOnly);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        getSelect().setEnabled(enabled);
    }

    @Override
    protected Component initContent() {
        return getSelect();
    }

}
