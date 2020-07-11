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

package org.bubenheimer.rulez.base.fluentrules;

import org.bubenheimer.rulez.base.Fact;
import org.bubenheimer.rulez.base.Rule;
import org.bubenheimer.rulez.base.RuleAction;

/**
 * Represents a rule in the fluent API.
 */
public final class Proposition <F extends Fact, R extends Rule<F>> {
    private final R rule;

    public Proposition(final R rule) {
        this.rule = rule;
    }

    @SuppressWarnings("unused")
    public When<F,R> when(final F fact) {
        final When<F,R> when = new When<>(rule);
        when.or(fact);
        return when;
    }

    @SuppressWarnings("unused")
    public WhenNot<F,R> whenNot(final F fact) {
        final WhenNot<F,R> whenNot = new WhenNot<>(rule);
        whenNot.or(fact);
        return whenNot;
    }

    @SuppressWarnings("unused")
    public R then(final RuleAction<F> ruleAction) {
        rule.ruleAction = ruleAction;
        return rule;
    }
}
