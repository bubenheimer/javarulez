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

/**
 * A fact
 */
public class Fact {
    static public class FactCreator implements FactBase.FactCreator<Fact> {
        @Override
        public @NotNull Fact create(int id) {
            return new Fact(id);
        }
    }

    /**
     * The fact's ID within a given rule base
     */
    public final int id;

    /**
     * Creates a new fact
     * @param id the internal fact id. All facts in a rule base must have distinct IDs.
     */
    public Fact(final int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Fact " + id;
    }

    public final boolean getFactValue(final int factState) {
        return ((factState >>> id) & 1) == 1;
    }

    public final int applyToFactState(final boolean value, final int factState) {
        if (value) {
            return factState | getMask();
        } else {
            return factState & ~getMask();
        }
    }

    public final int getMask() {
        return 1 << id;
    }
}
