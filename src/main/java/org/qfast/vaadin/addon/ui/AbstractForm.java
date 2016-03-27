package org.qfast.vaadin.addon.ui;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import static com.vaadin.ui.Notification.Type.ERROR_MESSAGE;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Collection;
import java.util.Iterator;
import org.qfast.vaadin.addon.fieldgroup.BeanBinder;

/**
 * Abstract super class for simple editor forms.
 *
 * @param <T> the type of the bean edited
 */
public abstract class AbstractForm<T> extends CustomComponent {

    public AbstractForm() {
    }

    public interface SavedHandler<T> {

        void onSave(T entity);
    }

    public interface SubmitedHandler<T> {

        void onSubmit(T entity);
    }

    public interface ResetHandler<T> {

        void onReset(T entity);
    }

    protected T entity;
    private SavedHandler<T> savedHandler;
    private SubmitedHandler<T> submitedHandler;
    private ResetHandler<T> resetHandler;
    protected BeanFieldGroup<T> fieldGroup;

    public void setEntity(T entity) {
        this.entity = entity;
        if (entity != null) {
            fieldGroup = BeanBinder.bind(entity, this);
            setValidationVisible(false);
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        fieldGroup.setReadOnly(readOnly);
    }

    @Override
    public void focus() {
        super.focus();
        Iterator<Field<?>> iterator = fieldGroup.getFields().iterator();
        if (iterator.hasNext()) {
            iterator.next().focus();
        }
    }

    public BeanFieldGroup<T> getFieldGroup() {
        return fieldGroup;
    }

    public T getEntity() throws FieldGroup.CommitException {
        setValidationVisible(true);
        fieldGroup.commit();
        return entity;
    }

    public void setSavedHandler(SavedHandler<T> savedHandler) {
        this.savedHandler = savedHandler;
    }

    public void setSubmitedHandler(SubmitedHandler<T> submitedHandler) {
        this.submitedHandler = submitedHandler;
    }

    public void setResetHandler(ResetHandler<T> resetHandler) {
        this.resetHandler = resetHandler;
    }

    /**
     * @return A default toolbar containing save & cancel buttons
     */
    public HorizontalLayout getToolbar() {
        HorizontalLayout toolbarLayout
                = new HorizontalLayout(createSaveButton(), createCancelButton());
        toolbarLayout.setSpacing(true);
        toolbarLayout.setMargin(true);
        return toolbarLayout;
    }

    protected Component createCancelButton() {
        Button delBtn = new Button(FontAwesome.TRASH_O, new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                reset(event);
            }
            private static final long serialVersionUID = 5019806363620874205L;
        });
        delBtn.addStyleName(ValoTheme.BUTTON_DANGER);
        return delBtn;
    }

    protected Component createSaveButton() {
        return new PrimaryButton(FontAwesome.SAVE, new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                save(event);
            }
            private static final long serialVersionUID = 5019806363620874206L;
        });
    }

    protected Component createSubmitButton() {
        return new PrimaryButton(FontAwesome.CHECK, new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                submit(event);
            }
            private static final long serialVersionUID = 5019806363620874207L;
        });
    }

    protected void save(Button.ClickEvent e) {
        try {
            setValidationVisible(true);
            fieldGroup.commit();
            savedHandler.onSave(entity);
        } catch (FieldGroup.CommitException ex) {
            Notification.show(ex.getLocalizedMessage(), ERROR_MESSAGE);
        }
    }

    protected void submit(Button.ClickEvent e) {
        try {
            setValidationVisible(true);
            fieldGroup.commit();
            submitedHandler.onSubmit(entity);
        } catch (FieldGroup.CommitException ex) {
            Notification.show(ex.getLocalizedMessage(), ERROR_MESSAGE);
        }
    }

    protected void reset(Button.ClickEvent e) {
        resetHandler.onReset(entity);
    }

    public void setValidationVisible(boolean visible) {
        Collection<Field<?>> fields = fieldGroup.getFields();
        for (Field<?> f : fields) {
            if (!f.isValid() && visible) {
                f.addStyleName("error");
            } else {
                ((AbstractComponent) f).setComponentError(null);
                f.removeStyleName("error");
            }
            if ((f instanceof AbstractField || f instanceof ComboBox)) {
                if (f instanceof ComboBox) {
                    ((ComboBox) f).setValidationVisible(visible);
                } else {
                    ((AbstractField) f).setValidationVisible(visible);
                }
            }
        }
    }

    @Override
    public void attach() {
        super.attach();
        setCompositionRoot(createContent());
    }

    /**
     * This method should return the actual content of the form, including
     * possible toolbar.
     *
     * Example implementation could look like this: {@code
     *  public class PersonForm extends AbstractForm&lt;Person&gt; {
     *
     *      private TextField firstName = new MTextField(&quot;First Name&quot;);
     *      private TextField lastName = new MTextField(&quot;Last Name&quot;);
     *
     *      @Override
     *      protected Component createContent() {
     *          return new MVerticalLayout(
     *                  new FormLayout(
     *                          firstName,
     *                             lastName
     *                     ),
     *                     getToolbar()
     *          );
     *      }
     * }
     * }
     *
     * @return the content of the form
     *
     */
    protected abstract Component createContent();
    private static final long serialVersionUID = -9127352114052742724L;
}
