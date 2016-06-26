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

import static net.sf.oval.Validator.*;

import java.util.List;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.exception.ConstraintsViolatedException;
import net.sf.oval.internal.util.ArrayUtils;

/**
 * @author Sebastian Thomschke
 */
public class ConstraintsViolatedAdapter implements ConstraintsViolatedListener {
    private final List<ConstraintsViolatedException> violationExceptions = getCollectionFactory().createList(8);
    private final List<ConstraintViolation> violations = getCollectionFactory().createList(8);

    public void clear() {
        violationExceptions.clear();
        violations.clear();
    }

    public List<ConstraintsViolatedException> getConstraintsViolatedExceptions() {
        return violationExceptions;
    }

    public List<ConstraintViolation> getConstraintViolations() {
        return violations;
    }

    public void onConstraintsViolatedException(final ConstraintsViolatedException exception) {
        violationExceptions.add(exception);
        violations.addAll(ArrayUtils.asList(exception.getConstraintViolations()));
    }
}
