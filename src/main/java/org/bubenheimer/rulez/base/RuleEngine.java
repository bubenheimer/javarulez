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

package org.bubenheimer.rulez;

import java.lang.ref.WeakReference;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract rule engine missing an evaluation strategy.
 */
@SuppressWarnings("WeakerAccess")
public abstract class RuleEngine {
    private static final Logger LOG = Logger.getLogger(RuleEngine.class.getName());

    /**
     * The fact state (bit vector).
     */
    private final FactState factState = new FactState(this);

    /**
     * Listener to be invoked when rule evaluation ends.
     */
    private EvalEndListener evalEndListener;

    /**
     * A weak reference to the rule base.
     */
    private WeakReference<RuleBase> ruleBaseRef = new WeakReference<>(null);

    /**
     * @return the fact state (what's true and what's false)
     */
    @SuppressWarnings("WeakerAccess")
    public final FactState getFactState() {
        return factState;
    }

    /**
     * Clears the rule base and clears the rule engine state.
     */
    @SuppressWarnings("unused")
    public void clear() {
        ruleBaseRef.clear();
        clearState();
    }

    /**
     * Clears the rule engine state
     */
    public void clearState() {
        factState.clear();
    }

    /**
     * @return the listener to be invoked when rule evaluation ends. May be null.
     */
    @SuppressWarnings("unused")
    public final EvalEndListener getEvalEndListener() {
        return evalEndListener;
    }

    /**
     * @param listener the listener to be invoked when rule evaluation ends. May be null.
     */
    @SuppressWarnings("WeakerAccess")
    public final void setEvalEndListener(final EvalEndListener listener) {
        evalEndListener = listener;
    }

    /**
     * @return the rule base. May be null.
     */
    @SuppressWarnings("WeakerAccess")
    public final RuleBase getRuleBase() {
        return ruleBaseRef.get();
    }

    /**
     * Sets the rule base. If the rule base is not {@code null},
     * it needs to be completely initialized.
     *
     * @param ruleBase the rule base. May be {@code null}.
     */
    public void setRuleBase(final RuleBase ruleBase) {
        ruleBaseRef = new WeakReference<>(ruleBase);
        if (ruleBase != null) {
            final PersistenceStore persistenceStore = ruleBase.persistenceStore;
            if (persistenceStore != null) {
                final int factCount = ruleBase.getFactCount();
                int initState = factState.getState();
                for (int i = 0; i < factCount; ++i) {
                    final Fact fact = ruleBase.facts[i];
                    if (fact.persistence == Fact.PERSISTENCE_DISK) {
                        if (persistenceStore.get(fact.id, fact.name)) {
                            initState |= 1 << fact.id;
                        }
                    }
                }
                factState.setState(initState);
            }
        }
    }

    /**
     * Schedules a rule evaluation step.
     */
    protected abstract void scheduleEvaluation();

    /**
     * To be called by subclasses at the end of a rule evaluation step to notify the rule engine
     * when evaluation has concluded.
     */
    @SuppressWarnings("WeakerAccess")
    protected final void handleEvaluationEnd() {
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("Evaluation ended: " + formatState(factState.getState()));
        }

        if (evalEndListener != null) {
            evalEndListener.onEvalEnd(this);
        }
    }

    /**
     * Convenience method to format the fact state of the rule engine or the rule base evaluation
     * state as a string in a standard manner (as a bit vector).
     * @param value the fact state or rule base
     * @return the standardized string-formatted state
     */
    @SuppressWarnings("WeakerAccess")
    protected static String formatState(final int value) {
        return Integer.toBinaryString(value);
    }
}
