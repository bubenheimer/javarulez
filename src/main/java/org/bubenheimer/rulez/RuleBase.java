/*
 * Copyright (c) 2015-2017 Uli Bubenheimer. All rights reserved.
 */

package org.bubenheimer.rulez;


import org.bubenheimer.rulez.fluent.Proposition;

import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The rule engine's collection of rules
 */
@SuppressWarnings("WeakerAccess")
public class RuleBase {
    private static final Logger LOG = Logger.getLogger(RuleBase.class.getName());

    /** Maximum number of facts. Change this to {@code 64} if long is used instead of int to
     * represent the fact state. */
    public static final int MAX_FACTS = 32;

    /** Maximum number of rules. Change this to {@code 64} if long is used instead of int to
     * represent the rule base state. */
    public static final int MAX_RULES = 32;

    final Fact[] facts = new Fact[MAX_FACTS];

    private int factIdCounter = 0;

    /**
     * The rules
     */
    final ArrayList<Rule> rules = new ArrayList<>(MAX_RULES);

    /**
     * @param persistenceStore a persistence store for saving and restoring persistent fact state.
     *                         May be null to not use persistent state.
     */
    @SuppressWarnings("unused")
    public void setPersistenceStore(final PersistenceStore persistenceStore) {
        this.persistenceStore = persistenceStore;
    }

    /**
     * Create a new fact with no fact state persistence.
     * @param name fact name for debugging
     * @return the new fact
     */
    @SuppressWarnings("unused")
    public Fact newFact(final String name) {
        return newFact(name, Fact.PERSISTENCE_NONE);
    }

    /**
     * Create a new fact
     * @param name          fact name for debugging
     * @param persistence   fact state persistence
     * @return the new fact
     */
    public Fact newFact(
            final String name,
            @SuppressWarnings("SameParameterValue") @Fact.Persistence final int persistence) {
        if (factIdCounter >= MAX_FACTS) {
            throw new AssertionError("Too many facts");
        } else {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine(String.format(Locale.US, "ID %2d for new fact %s", factIdCounter, name));
            }
            final Fact fact = new Fact(factIdCounter, name, persistence);
            facts[factIdCounter++] = fact;
            return fact;
        }
    }

    PersistenceStore persistenceStore;

    public int getFactCount() {
        return factIdCounter;
    }

    /**
     * Create a rule via a fluent builder pattern with a default match type of
     * {@link Rule#MATCH_ALWAYS}.
     *
     * @param name rule name
     * @return a builder instance
     */
    public Proposition rule(final String name) {
        return rule(name, Rule.MATCH_ALWAYS);
    }

    /**
     * Create a rule via a fluent builder pattern.
     * @param name         rule name
     * @param matchType    rule match type. Specifies if a rule should run no more than once,
     *                     or under what conditions it becomes eligible to re-run.
     * @return a builder instance
     */
    public Proposition rule(
            final String name,
            @SuppressWarnings("SameParameterValue") @Rule.MatchType final int matchType) {
        if (rules.size() >= MAX_RULES) {
            throw new AssertionError("Too many rules");
        }
        final Rule rule = new Rule(name, matchType);
        rules.add(rule);
        return new Proposition(rule);
    }
}
