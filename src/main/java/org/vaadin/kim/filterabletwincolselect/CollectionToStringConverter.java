package org.vaadin.kim.filterabletwincolselect;

import com.vaadin.data.util.converter.Converter;

import java.util.Locale;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class CollectionToStringConverter implements Converter<String, Set> {

	private static final long serialVersionUID = 1206097427974288932L;

	@Override
	public Set convertToModel(String s, Class<? extends Set> aClass, Locale locale) throws ConversionException {
		return null;
	}

	@Override
	public String convertToPresentation(Set value, Class<? extends String> aClass, Locale locale) throws ConversionException {
		String presentation = "";
		if (value != null) {
			for (Object o : value) {
				if (presentation.length() > 0) {
					presentation += ", ";
				}

				presentation += o.toString();
			}
		}
		return presentation;
	}

	@Override
	public Class<Set> getModelType() {
		return Set.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
