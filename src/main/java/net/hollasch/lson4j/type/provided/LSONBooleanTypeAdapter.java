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
 * @since Dec 09, 1:11 AM
 */
public class LSONBooleanTypeAdapter implements LSONTypeAdapter<Boolean>
{
    public Boolean buildFromString (final String value)
    {
        if (value.length() == 0) {
            return null;
        }

        return value.charAt(0) == 't' ? Boolean.TRUE : Boolean.FALSE;
    }
}
