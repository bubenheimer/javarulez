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

/**
 * API for modifying the fact state
 */
@SuppressWarnings("WeakerAccess")
public interface WritableState <F extends Fact> {
    /**
     * Adds a fact to the state.
     * @param fact the fact to add
     */
    @SuppressWarnings("unused")
    void addFact(F fact);

    /**
     * Adds facts to the state
     * @param facts the facts to add
     */
    @SuppressWarnings({"unused", "unchecked"})
    void addFacts(F... facts);

    /**
     * Removes a fact from the state.
     * @param fact the fact to remove
     */
    @SuppressWarnings("unused")
    void removeFact(F fact);

    /**
     * Removes facts from the state.
     * @param facts the facts to remove
     */
    @SuppressWarnings({"unused", "unchecked"})
    void removeFacts(F... facts);

    /**
     * Adds a fact to the state and removes a fact from the state in a single operation.
     * @param addFact       the fact to add
     * @param removeFact    the fact to remove
     */
    @SuppressWarnings("unused")
    void addRemoveFacts(F addFact, F removeFact);

    /**
     * Adds facts to the state and removes facts from the state in a single operation.
     * @param addFacts      the facts to add
     * @param removeFacts   the facts to remove
     */
    @SuppressWarnings("unused")
    void addRemoveFacts(F[] addFacts, F[] removeFacts);
}
