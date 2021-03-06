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

/**
 * Persistence store to support {@link Fact#PERSISTENCE_DISK}.
 */
public interface PersistenceStore {
    /**
     * Retrieve fact value from persistence store
     * @param id    unique fact ID
     * @param name  unique fact name
     * @return fact value
     */
    boolean get(int id, String name);

    /**
     * Set fact value in persistence store
     * @param id    unique fact ID
     * @param name  unique fact name
     * @param value fact value
     */
    void set(int id, String name, boolean value);
}
