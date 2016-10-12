package melnorme.lang.tooling;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
/** 
 * This is marker for LANG code whose contents is language specific, yet it it refered to by other LANG code.
 * Such code must not be place in the src-lang source folder
 */
public @interface LANG_SPECIFIC {
}