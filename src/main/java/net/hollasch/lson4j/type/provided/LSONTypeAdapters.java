/*
 * Copyright (C) 2017 LSON4J - All Rights Reserved
 *
 * Unauthorized copying of this file, via any median is strictly prohibited
 * proprietary and confidential. For more information, please contact me at
 * connor@hollasch.net
 *
 * Written by Connor Hollasch <connor@hollasch.net>, December 2017
 */

package net.hollasch.lson4j.type.provided;

import net.hollasch.lson4j.type.LSONTypeAdapter;

/**
 * @author Connor Hollasch
 * @since Dec 11, 9:13 PM
 */
public enum LSONTypeAdapters
{
    BOOLEAN {
        @Override
        public LSONTypeAdapter<?> createAdapter ()
        {
            return new LSONBooleanTypeAdapter();
        }
    },
    NUMBER {
        @Override
        public LSONTypeAdapter<?> createAdapter ()
        {
            return new LSONNumberTypeAdapter();
        }
    };

    public abstract LSONTypeAdapter<?> createAdapter ();
}
