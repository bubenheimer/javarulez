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

package org.bubenheimer.rulez;

import org.bubenheimer.rulez.base.Fact;
import org.bubenheimer.rulez.base.FactBase;
import org.jetbrains.annotations.NotNull;

public class NamedFact extends Fact {
    static public class FactCreator implements FactBase.FactCreator<NamedFact> {
        private final String name;

        public FactCreator(final String name) {
            this.name = name;
        }

        @Override
        public @NotNull NamedFact create(int id) {
            return new NamedFact(id, name);
        }
    }

    /**
     * Fact name
     */
    public final String name;

    /**
     * Creates a new fact
     *
     * @param id the internal fact id. All facts in a rule base must have distinct IDs.
     * @param name a unique fact name
     */
    public NamedFact(final int id, final String name) {
        super(id);

        this.name = name;
    }

    @Override
    public String toString() {
        return "Fact " + id + ": " + name;
    }
}
