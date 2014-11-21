/**
 *
 */
package de.xwic.appkit.core.util;

/**
 * @deprecated use {@link ExceptionalFunction} instead
 * @author Alexandru Bledea
 * @since Aug 27, 2014
 */
@Deprecated
public interface ILazyEvalWithException<O, R, X extends Exception> extends ExceptionalFunction<O, R, X> {

}
