/*
 * Copyright (c) 2015-2020 Uli Bubenheimer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.bubenheimer.rulez.base;


import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The rule engine's collection of facts
 */
@SuppressWarnings("WeakerAccess")
public class FactBase <F extends Fact> {
    private static final Logger LOG = Logger.getLogger(FactBase.class.getName());

    public interface FactCreator <F extends Fact> {
        @NotNull F create(int id);
    }

    /**
     * Maximum number of facts. Change this to {@link Long#SIZE} if using long instead of int to
     * represent the fact state.
     */
    public static final int MAX_FACTS = Integer.SIZE;

    @SuppressWarnings("unchecked")
    final F[] facts = (F[]) new Fact[MAX_FACTS];

    private int factIdCounter = 0;

    /**
     * Create a new fact
     * @return the new fact
     */
    public final @NotNull F newFact(
            final FactCreator<F> factCreator
    ) {
        if (MAX_FACTS <= factIdCounter) {
            throw new AssertionError("Too many facts");
        } else {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine(String.format(Locale.US, "ID %2d for new fact", factIdCounter/*, name*/));
            }
            final F fact = factCreator.create(factIdCounter/*, name, persistence*/);
            facts[factIdCounter++] = fact;
            return fact;
        }
    }

    public final int getFactCount() {
        return factIdCounter;
    }

    public final List<F> getFactList() {
        return Arrays.asList(facts);
    }
}
