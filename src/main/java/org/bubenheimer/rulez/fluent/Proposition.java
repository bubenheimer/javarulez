/*
 * Copyright (c) 2015-2017 Uli Bubenheimer. All rights reserved.
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
