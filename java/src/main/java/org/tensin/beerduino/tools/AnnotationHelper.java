package org.tensin.beerduino.tools;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class AnnotationHelper.
 */
public class AnnotationHelper {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationHelper.class);
	
	/**
	 * Détermine si une annotation implémente ou non un type.
	 * Ne PAS utiliser instanceof car dans certains contextes la classe de l'annotatino retournée par introspection est (par ex.) $Proxy1 au lieu de Root
	 *
	 * @param annotation the annotation
	 * @param clazz the clazz
	 * @return true, if is instance
	 */
	public static boolean isInstance(Annotation annotation, Class<?> clazz) {
		return annotation.annotationType().getSimpleName().equalsIgnoreCase(clazz.getSimpleName());
	}

	/**
	 * Gets the field value.
	 *
	 * @param annotation the annotation
	 * @param fieldName the field name
	 * @return the field value
	 */
	public static Object getFieldValue(Annotation annotation, String fieldName) {
        try {
			return annotation.annotationType().getDeclaredMethod(fieldName, new Class[0]).invoke(annotation);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Error while retrieving annotation information", e);
		} catch (SecurityException e) {
			LOGGER.error("Error while retrieving annotation information", e);
		} catch (IllegalAccessException e) {
			LOGGER.error("Error while retrieving annotation information", e);
		} catch (InvocationTargetException e) {
			LOGGER.error("Error while retrieving annotation information", e);
		} catch (NoSuchMethodException e) {
			LOGGER.error("Error while retrieving annotation information", e);
		}
        return null;
	}
}
