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

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.bubenheimer.rulez.base.RuleEngine.formatState;

@SuppressWarnings("WeakerAccess")
public final class FactState <F extends Fact> {
    private static final Logger LOG = Logger.getLogger(FactState.class.getName());

    public interface FactChangeListener <F extends Fact> {
        void onFactChange(F fact, int factMask, boolean isSet, int state);
    }

    private final FactChangeListener<F> factChangeListener;

    /**
     * The state (bit vector).
     */
    private int state;

    public FactState() {
        this(null);
    }

    /**
     * @param factChangeListener listener for fact changes
     */
    public FactState(final FactChangeListener<F> factChangeListener) {
        this(0, factChangeListener);
    }

    /**
     * @param rawState the raw state bit vector (indicating what's true and what's false)
     */
    public FactState(final int rawState) {
        this(rawState, null);
    }

    /**
     * @param rawState the raw state bit vector (indicating what's true and what's false)
     * @param factChangeListener listener for fact changes
     */
    public FactState(final int rawState, final FactChangeListener<F> factChangeListener) {
        this.state = rawState;
        this.factChangeListener = factChangeListener;
    }

    /**
     * @return whether the fact is valid (true)
     */
    final boolean isValid(final F fact) {
        return (state & (1 << fact.id)) != 0;
    }

    /**
     * @return the raw state bit vector (indicating what's true and what's false)
     */
    final public int getState() {
        return state;
    }

    /**
     * Adds a fact to the state.
     * @param fact the fact to add
     * @return {@code true} iff the state changed
     */
    final boolean addFact(final F fact) {
        final int factMask = 1 << fact.id;
        if (factChangeListener != null) {
            factChangeListener.onFactChange(fact, factMask, true, state);
        }
        return addFactsInternal(factMask);
    }

    /**
     * Adds facts to the state
     * @param facts the facts to add
     * @return {@code true} iff the state changed
     */
    @SafeVarargs
    final boolean addFacts(final F... facts) {
        int factVector = 0;
        for (final F fact : facts) {
            final int factMask = 1 << fact.id;
            if (factChangeListener != null) {
                factChangeListener.onFactChange(fact, factMask, true, state);
            }
            factVector |= factMask;
        }
        return addFactsInternal(factVector);
    }

    /**
     * Adds the facts from a fact bit vector.
     * @param factVector the fact bit vector
     */
    private boolean addFactsInternal(final int factVector) {
        final int oldState = state;
        state |= factVector;
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("State change: " + formatState(oldState) + " + " + formatState(factVector)
                    + " = " + formatState(state));
        }
        return oldState != state;
    }

    /**
     * Removes a fact from the state.
     * @param fact the fact to remove
     * @return {@code true} iff the state changed
     */
    final boolean removeFact(final F fact) {
        final int factMask = 1 << fact.id;
        if (factChangeListener != null) {
            factChangeListener.onFactChange(fact, factMask, false, state);
        }
        return removeFactsInternal(factMask);
    }

    /**
     * Removes facts from the state.
     * @param facts the facts to remove
     * @return {@code true} iff the state changed
     */
    @SafeVarargs
    final boolean removeFacts(final F... facts) {
        int factVector = 0;
        for (final F fact : facts) {
            final int factMask = 1 << fact.id;
            if (factChangeListener != null) {
                factChangeListener.onFactChange(fact, factMask, false, state);
            }
            factVector |= factMask;
        }
        return removeFactsInternal(factVector);
    }

    /**
     * Removes the facts from a fact bit vector.
     * @param factVector the fact bit vector
     */
    private boolean removeFactsInternal(final int factVector) {
        final int oldState = state;
        state &= ~factVector;
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("State change: " + formatState(oldState) + " - " + formatState(factVector)
                    + " = " + formatState(state));
        }
        return oldState != state;
    }

    /**
     * Adds a fact to the state and removes a fact from the state in a single operation.
     * @param addFact       the fact to add
     * @param removeFact    the fact to remove
     * @return {@code true} iff the state changed
     */
    boolean addRemoveFacts(final F addFact, final F removeFact) {
        final int addFactMask = 1 << addFact.id;
        if (factChangeListener != null) {
            factChangeListener.onFactChange(addFact, addFactMask, true, state);
        }
        final int removeFactMask = 1 << removeFact.id;
        if (factChangeListener != null) {
            factChangeListener.onFactChange(removeFact, removeFactMask, false, state);
        }
        return addRemoveFactsInternal(addFactMask, removeFactMask);
    }

    /**
     * Adds facts to the state and removes facts from the state in a single operation.
     * @param addFacts      the facts to add
     * @param removeFacts   the facts to remove
     * @return {@code true} iff the state changed
     */
    boolean addRemoveFacts(final F[] addFacts, final F[] removeFacts) {
        int addFactVector = 0;
        for (final F fact : addFacts) {
            final int factMask = 1 << fact.id;
            if (factChangeListener != null) {
                factChangeListener.onFactChange(fact, factMask, true, state);
            }
            addFactVector |= factMask;
        }
        int removeFactVector = 0;
        for (final F fact : removeFacts) {
            final int factMask = 1 << fact.id;
            if (factChangeListener != null) {
                factChangeListener.onFactChange(fact, factMask, false, state);
            }
            removeFactVector |= factMask;
        }
        return addRemoveFactsInternal(addFactVector, removeFactVector);
    }

    /**
     * Adds facts to the state and removes facts from the state via two fact bit vectors.
     * @param addFactVector      the facts to add
     * @param removeFactVector   the facts to remove
     */
    private boolean addRemoveFactsInternal(final int addFactVector, final int removeFactVector) {
        final int oldState = state;
        state = (state | addFactVector) & ~removeFactVector;
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("State change: " + formatState(oldState) + " + " + formatState(addFactVector)
                    + " - " + formatState(removeFactVector) + " = " + formatState(state));
        }
        return oldState != state;
    }
}
