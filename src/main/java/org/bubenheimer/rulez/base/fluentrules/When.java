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

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("WeakerAccess")
public final class When <F extends Fact, R extends Rule<F>> {
    private final R rule;

    private final Collection<F> facts = new ArrayList<>();

    When(final R rule) {
        this.rule = rule;
    }

    public When<F,R> and(final F fact) {
        facts.add(fact);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public When<F,R> or(final F fact) {
        completeConjunction();
        facts.add(fact);
        return this;
    }

    @SuppressWarnings("unused")
    public WhenNot<F,R> andNot(final F fact) {
        completeConjunction();
        final WhenNot<F,R> whenNot = new WhenNot<>(rule);
        whenNot.or(fact);
        return whenNot;
    }

    @SuppressWarnings("unused")
    public R then(final RuleAction<F> ruleAction) {
        completeConjunction();
        rule.ruleAction = ruleAction;
        return rule;
    }

    public void completeConjunction(
    ) {
        if (!facts.isEmpty()) {
            rule.addCondition(facts);
            facts.clear();
        }
    }
}
