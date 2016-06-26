/*******************************************************************************
 * Portions created by Sebastian Thomschke are copyright (c) 2005-2016 Sebastian
 * Thomschke.
 *
 * All Rights Reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Sebastian Thomschke - initial implementation.
 *******************************************************************************/
package net.sf.oval.guard;

import net.sf.oval.AbstractCheck;
import net.sf.oval.Validator;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.OValException;

/**
 * @author Sebastian Thomschke
 */
public class PostCheck extends AbstractCheck {
    private static final long serialVersionUID = 1L;

    private String expression;
    private String language;
    private String old;

    public void configure(final Post constraintAnnotation) {
        setMessage(constraintAnnotation.message());
        setErrorCode(constraintAnnotation.errorCode());
        setSeverity(constraintAnnotation.severity());
        setExpression(constraintAnnotation.expr());
        setLanguage(constraintAnnotation.lang());
        setOld(constraintAnnotation.old());
        setProfiles(constraintAnnotation.profiles());
    }

    public String getExpression() {
        return expression;
    }

    public String getLanguage() {
        return language;
    }

    public String getOld() {
        return old;
    }

    public boolean isSatisfied(final Object validatedObject, final Object valueToValidate, final OValContext context, final Validator validator)
            throws OValException {
        throw new UnsupportedOperationException();
    }

    public void setExpression(final String condition) {
        expression = condition;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    public void setOld(final String old) {
        this.old = old;
    }
}
