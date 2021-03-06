/*
 * Copyright (c) 2015-2019 Uli Bubenheimer
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

/**
 * Represents a rule in the fluent API.
 */
public final class Proposition {
    private final Rule rule;

    public Proposition(final Rule rule) {
        this.rule = rule;
    }

    @SuppressWarnings("unused")
    public When when(final Fact fact) {
        final When when = new When(rule);
        when.or(fact);
        return when;
    }

    @SuppressWarnings("unused")
    public WhenNot whenNot(final Fact fact) {
        final WhenNot whenNot = new WhenNot(rule);
        whenNot.or(fact);
        return whenNot;
    }

    @SuppressWarnings("unused")
    public Rule then(final RuleAction ruleAction) {
        rule.setRuleAction(ruleAction);
        return rule;
    }
}
