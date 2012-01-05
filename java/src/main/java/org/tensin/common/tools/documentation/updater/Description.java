package org.tensin.common.tools.documentation.updater;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation permettant de donner une description.
 *
 * @author j385649
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {

	   /**
   	 * Value.
   	 *
   	 * @return the string
   	 */
   	public String value();

}
