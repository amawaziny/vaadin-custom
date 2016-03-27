package org.qfast.vaadin.addon.util;

/**
 * 
 * @param <T>
 */
public interface CaptionGenerator<T> {

    String getCaption(T option);

}
