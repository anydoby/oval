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
package net.sf.oval.test.validator;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import net.sf.oval.ConstraintTarget;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import net.sf.oval.constraint.AssertTrue;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.MinLength;
import net.sf.oval.constraint.MinSize;
import net.sf.oval.constraint.NotNull;
import net.sf.oval.context.FieldContext;
import net.sf.oval.exception.InvalidConfigurationException;
import net.sf.oval.exception.ValidationFailedException;

/**
 * @author Sebastian Thomschke
 */
public class TargetJXPathTest extends TestCase {
    public static class Level1 {
        @AssertTrue(target = "jxpath:.[@visible=0]/visible" /* find an item where visible='false' and select the 'visible' property for testing */)
        protected List<Thing> things = new ArrayList<Thing>();

        @MinSize(target = "jxpath:level3/array", value = 4, message = "LEVEL3_ARRAY_TOO_SMALL")
        @MinLength(target = "jxpath:level3/array", value = 4, appliesTo = ConstraintTarget.VALUES, message = "LEVEL3_ARRAY_ITEM_TOO_SMALL")
        @MaxLength(target = "jxpath:level3/array[1]", value = 5, message = "LEVEL3_ARRAY_FIRST_ITEM_TOO_LONG")
        @NotNull(target = "jxpath:level3/name", message = "LEVEL3_NAME_IS_NULL")
        protected Level2 level2a;

        // illegal path, results in an InvalidConfigurationException
        @NotNull(target = "jxpath:level3/foobar")
        protected Level2 level2b;

        public Level2 getLevel2a() {
            return level2a;
        }

        public Level2 getLevel2b() {
            return level2b;
        }
    }

    public static class Level2 {
        protected Level3 level3;

        public Level3 getLevel3() {
            return level3;
        }
    }

    public static class Level3 {
        protected String name;
        protected String[] array;

        public String[] getArray() {
            return array;
        }

        public String getName() {
            return name;
        }
    }

    public static class Thing {
        private final boolean visible;

        public Thing(final boolean visible) {
            super();
            this.visible = visible;
        }

        public boolean isVisible() {
            return visible;
        }
    }

    public void testTarget() {
        final Validator v = new Validator();
        List<ConstraintViolation> violations;

        final Level1 lv1 = new Level1();
        assertEquals(0, v.validate(lv1).size());

        lv1.level2a = new Level2();
        lv1.level2b = new Level2();
        assertEquals(0, v.validate(lv1).size());

        lv1.things.add(new Thing(true));
        assertEquals(0, v.validate(lv1).size());
        lv1.things.add(new Thing(false));
        assertEquals(1, v.validate(lv1).size());
        lv1.things.clear();

        lv1.level2a.level3 = new Level3();
        violations = v.validate(lv1);
        assertEquals(1, violations.size());
        lv1.level2a.level3.name = "foo";

        lv1.level2a.level3.array = new String[] {};
        violations = v.validate(lv1);
        assertEquals(1, violations.size());
        ConstraintViolation violation = violations.get(0);
        assertTrue(violation.getContext() instanceof FieldContext);
        assertEquals("LEVEL3_ARRAY_TOO_SMALL", violation.getMessage());

        lv1.level2a.level3.array = new String[] { "123", "1234", "1234", "1234" };
        violations = v.validate(lv1);
        assertEquals(1, violations.size());
        violation = violations.get(0);
        assertTrue(violation.getContext() instanceof FieldContext);
        assertEquals("LEVEL3_ARRAY_ITEM_TOO_SMALL", violation.getMessage());

        lv1.level2a.level3.array = new String[] { "123456789", "1234", "1234", "1234" };
        violations = v.validate(lv1);
        assertEquals(1, violations.size());
        violation = violations.get(0);
        assertTrue(violation.getContext() instanceof FieldContext);
        assertEquals("LEVEL3_ARRAY_FIRST_ITEM_TOO_LONG", violation.getMessage());

        lv1.level2a.level3.array = new String[] { "123456789", "123456789", "123456789", "123456789" };
        violations = v.validate(lv1);
        assertEquals(1, violations.size());
        violation = violations.get(0);
        assertTrue(violation.getContext() instanceof FieldContext);
        assertEquals("LEVEL3_ARRAY_FIRST_ITEM_TOO_LONG", violation.getMessage());

        try {
            lv1.level2b.level3 = new Level3();
            v.validate(lv1);
            fail();
        } catch (final ValidationFailedException ex) {
            assertTrue(ex.getCause() instanceof InvalidConfigurationException);
        }
    }
}
