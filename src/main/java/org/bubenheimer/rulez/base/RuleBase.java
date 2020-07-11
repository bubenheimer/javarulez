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


import org.bubenheimer.rulez.base.fluentrules.Proposition;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * The rule engine's collection of rules
 */
@SuppressWarnings("WeakerAccess")
public class RuleBase <F extends Fact, R extends Rule<F>> {
    public interface RuleCreator <F extends Fact, R extends Rule<F>> {
        @NotNull R create();
    }

    /**
     * Maximum number of rules. Change this to Long#SIZE if using long instead of int to
     * represent the rule base state.
     */
    public static final int MAX_RULES = Integer.SIZE;

    /**
     * The rules
     */
    public final ArrayList<R> rules = new ArrayList<>(MAX_RULES);

//    /**
//     * Create a rule via a fluent builder pattern with a default match type of
//     * {@link Rule#MATCH_ALWAYS}.
//     *
//     * @param name rule name
//     * @return a builder instance
//     */
//    public @NotNull Proposition rule(final String name) {
//        return rule(name, Rule.MATCH_ALWAYS);
//    }

    /**
     * Create a rule via a fluent builder pattern.
//     * @param name         rule name
//     * @param matchType    rule match type. Specifies if a rule should run no more than once,
//     *                     or under what conditions it becomes eligible to re-run.
     * @return a builder instance
     */
    public final @NotNull Proposition<F,R> rule(
            final RuleCreator<F,R> ruleCreator
//            final @NotNull String name
//            @SuppressWarnings("SameParameterValue") @Rule.MatchType final int matchType
    ) {
        if (MAX_RULES <= rules.size()) {
            throw new AssertionError("Too many rules");
        }
        final R rule = ruleCreator.create();
        rules.add(rule);
        return new Proposition<F,R>(rule);
    }

//    new Rule(name/*, matchType*/);
}
