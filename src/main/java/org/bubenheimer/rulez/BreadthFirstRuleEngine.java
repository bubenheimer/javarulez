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

package org.bubenheimer.rulez;

import org.bubenheimer.rulez.base.Fact;
import org.bubenheimer.rulez.base.FactState;
import org.bubenheimer.rulez.base.ReadableState;
import org.bubenheimer.rulez.base.Rule;
import org.bubenheimer.rulez.base.RuleBase;
import org.bubenheimer.rulez.base.RuleEngine;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>A rule engine where conceptually the evaluation strategy checks for each rule whether its
 * left-hand side matches the current state, and saves the rule away for later execution.
 * Once all rules have been evaluated, the actions of all matched rules are executed sequentially.
 * Then the engine checks if the state has changed and starts over.
 * This results in forward-chaining breadth-first rule evaluation.</p>
 *
 * <p>The actual implementation may use optimizations such that the conceptual strategy is
 * implemented in a non-literal, yet equivalent manner.</p>
 *
 * <p>Not thread-safe.</p>
 *
 */
@SuppressWarnings("WeakerAccess")
public class BreadthFirstRuleEngine <F extends Fact, R extends Rule<F>> extends RuleEngine<F,R> {
    private static final Logger LOG = Logger.getLogger(BreadthFirstRuleEngine.class.getName());

//    /**
//     * A bit mask for the match state of all rules. Indicates whether a rule has already fired.
//     */
//    private int ruleMatchState;

    /**
     * Indicates whether an evaluation of the rule base has been scheduled due to changed state.
     */
    @SuppressWarnings("FieldCanBeLocal")
    private boolean evaluationScheduled = false;

    /**
     * Indicates whether we are currently evaluating the rule base.
     */
    private boolean isEvaluating = false;

    /**
     * A {@link ReadableState} representing the current rule base state to pass to rule bodies.
     * There is just a single one to avoid garbage collection issues.
     */
    private final BaseState<F> baseState = new BaseState<>();

    /**
     * Represents the current rule base state to pass to rule bodies.
     */
    private static final class BaseState <F extends Fact> implements ReadableState<F> {
        /**
         * the current state
         */
        int state;

        @Override
        public boolean isValid(final F fact) {
            return false;
        }
    }

    public BreadthFirstRuleEngine(final FactState<F> factState, final RuleBase<F, R> ruleBase) {
        super(factState, ruleBase);
//        this(ruleBase, factState, 0);
    }

//    public BreadthFirstRuleEngine(
//            final RuleBase<F,R> ruleBase,
//            final FactState<F> factState,
//            final int ruleMatchState
//    ) {
//        super(ruleBase, factState);
//        this.ruleMatchState = ruleMatchState;
//    }

//    /**
//     * @return a bit mask for the match state of all rules. Indicates whether a rule has
//     * already fired.
//     */
//    @SuppressWarnings("WeakerAccess")
//    protected final int getRuleMatchState() {
//        return ruleMatchState;
//    }

    @Override
    protected void scheduleEvaluation() {
        if (isEvaluating) {
            evaluationScheduled = true;
            return;
        }

        isEvaluating = true;
        do {
            evaluationScheduled = false;
            evaluate();
        } while (evaluationScheduled);

        isEvaluating = false;

        handleEvaluationEnd();
    }

    /**
     * Evaluates the rule base.
     */
    @SuppressWarnings("WeakerAccess")
    protected final void evaluate() {
        final RuleBase<F,R> ruleBase = getRuleBase();
        if (ruleBase == null) {
            return;
        }
        baseState.state = getFactState().getState();
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("Evaluating: " + formatState(baseState.state));
        }
//        int evaluatedMask = 1;
        final int ruleCount = ruleBase.rules.size();
        for (int i = 0; i < ruleCount; ++i) {
            final R rule = ruleBase.rules.get(i);
            matchRule(rule);
            //TODO
//            if (rule.matchType != Rule.MATCH_ONCE
//                    || (ruleMatchState & evaluatedMask) == 0) {
//                if (rule.eval(baseState.state)) {
//                    if (rule.matchType == Rule.MATCH_ALWAYS
//                            || (ruleMatchState & evaluatedMask) == 0) {
//                        if (LOG.isLoggable(Level.FINE)) {
//                            LOG.fine("Rule fired: " + rule);
//                        }
//                        ruleMatchState |= evaluatedMask;
//                        rule.ruleAction.fire(baseState, engineFactState);
//                    }
//                } else if (rule.matchType == Rule.MATCH_RESET
//                        && (ruleMatchState & evaluatedMask) != 0) {
//                    if (LOG.isLoggable(Level.FINE)) {
//                        LOG.fine("Rule reset: " + rule);
//                    }
//                    ruleMatchState ^= evaluatedMask;
//                }
//            }
//            evaluatedMask <<= 1;
        }
    }

    protected void matchRule(final R rule) {
        if (rule.eval(baseState.state)) {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("Rule fired: " + rule);
            }
            rule.ruleAction.fire(baseState, getWritableState());
        }
    }
}
