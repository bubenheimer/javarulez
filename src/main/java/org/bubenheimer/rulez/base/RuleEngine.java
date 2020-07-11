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

import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract rule engine missing an evaluation strategy.
 */
@SuppressWarnings("WeakerAccess")
public abstract class RuleEngine <F extends Fact, R extends Rule<F>> {
    private static final Logger LOG = Logger.getLogger(RuleEngine.class.getName());

    /**
     * The fact state with engine reference.
     */
    private final EngineFactState<F> engineFactState;

    /**
     * Listener to be invoked when rule evaluation ends.
     */
    private EvalEndListener evalEndListener;

    /**
     * The rule base.
     */
    private final @NotNull RuleBase<F,R> ruleBase;

    /**
     * @param factState the fact state (bit vector)
     * @param ruleBase the rule base
     */
    public RuleEngine(final FactState<F> factState, final @NotNull RuleBase<F, R> ruleBase) {
        this.engineFactState = new EngineFactState<>(this, factState);
        this.ruleBase = ruleBase;
    }

    /**
     * @return the fact state (what's true and what's false)
     */
    @SuppressWarnings("WeakerAccess")
    public final FactState<F> getFactState() {
        return engineFactState.childFactState;
    }

    /**
     * @return readable fact state
     */
    public final ReadableState<F> getReadableState() {
        return engineFactState;
    }

    /**
     * @return writable fact state
     */
    public final WritableState<F> getWritableState() {
        return engineFactState;
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
     * @return the rule base.
     */
    @SuppressWarnings("WeakerAccess")
    public final @NotNull RuleBase<F,R> getRuleBase() {
        return ruleBase;
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
            LOG.fine("Evaluation ended: " + formatState(engineFactState.childFactState.getState()));
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

final class EngineFactState <F extends Fact> implements ReadableState<F>, WritableState<F> {
    private final RuleEngine<F, ?> ruleEngine;
    final FactState<F> childFactState;

    EngineFactState(final RuleEngine<F, ?> ruleEngine, final FactState<F> childFactState) {
        this.ruleEngine = ruleEngine;
        this.childFactState = childFactState;
    }

    @Override
    final public boolean isValid(F fact) {
        return childFactState.isValid(fact);
    }

    @Override
    final public void addFact(F fact) {
        if (childFactState.addFact(fact)) {
            ruleEngine.scheduleEvaluation();
        }
    }

    @SafeVarargs
    @Override
    final public void addFacts(F... facts) {
        if (childFactState.addFacts(facts)) {
            ruleEngine.scheduleEvaluation();
        }
    }

    @Override
    final public void removeFact(F fact) {
        if (childFactState.removeFact(fact)) {
            ruleEngine.scheduleEvaluation();
        }
    }

    @SafeVarargs
    @Override
    final public void removeFacts(F... facts) {
        if (childFactState.removeFacts(facts)) {
            ruleEngine.scheduleEvaluation();
        }
    }

    @Override
    final public void addRemoveFacts(F addFact, F removeFact) {
        if (childFactState.addRemoveFacts(addFact, removeFact)) {
            ruleEngine.scheduleEvaluation();
        }
    }

    @Override
    final public void addRemoveFacts(F[] addFacts, F[] removeFacts) {
        if (childFactState.addRemoveFacts(addFacts, removeFacts)) {
            ruleEngine.scheduleEvaluation();
        }
    }
}
