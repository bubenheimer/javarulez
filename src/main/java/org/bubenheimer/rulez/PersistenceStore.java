/*
 * Copyright (c) 2015-2017 Uli Bubenheimer. All rights reserved.
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
