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

package org.bubenheimer.rulez.fluent;

import org.bubenheimer.rulez.Fact;
import org.bubenheimer.rulez.Rule;
import org.bubenheimer.rulez.RuleAction;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("WeakerAccess")
public final class When {
    private final Rule rule;

    private final Collection<Fact> facts = new ArrayList<>();

    When(final Rule rule) {
        this.rule = rule;
    }

    public When and(final Fact fact) {
        facts.add(fact);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public When or(final Fact fact) {
        completeConjunction();
        facts.add(fact);
        return this;
    }

    @SuppressWarnings("unused")
    public WhenNot andNot(final Fact fact) {
        completeConjunction();
        final WhenNot whenNot = new WhenNot(rule);
        whenNot.or(fact);
        return whenNot;
    }

    @SuppressWarnings("unused")
    public Rule then(final RuleAction ruleAction) {
        completeConjunction();
        rule.setRuleAction(ruleAction);
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
