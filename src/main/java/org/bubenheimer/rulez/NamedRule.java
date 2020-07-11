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
import org.bubenheimer.rulez.base.Rule;
import org.bubenheimer.rulez.base.RuleBase;
import org.jetbrains.annotations.NotNull;

public class NamedRule <F extends Fact> extends Rule<F> {
    public static class RuleCreator<F extends Fact>
            implements RuleBase.RuleCreator<F, NamedRule<F>> {
        private final String name;

        public RuleCreator(final String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public NamedRule<F> create() {
            return new NamedRule<>(name);
        }
    }

    /**
     * Rule name
     */
    public final String name;

    /**
     * Create a rule.
     * @param name the rule name for debugging
     */
    public NamedRule(final String name) {
        this.name = name;
    }
}
