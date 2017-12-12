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
 * @since Dec 11, 9:14 PM
 */
public class LSONNumberTypeAdapter implements LSONTypeAdapter<Number>
{
    @Override
    public Number buildFromWord (String value)
    {
        if (value.contains("x")
                && value.length() > 2
                && (value.charAt(1) == 'x' || value.charAt(1) == 'X')) {
            return Long.parseLong(value.substring(2), 16);
        } else {
            return Double.parseDouble(value);
        }
    }

    @Override
    public boolean willAdaptFor (final String word)
    {
        if (word.length() == 0) {
            return false;
        }

        final char first = word.charAt(0);
        switch (first) {
            case '.':
            case '-':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return true;
            default:
                return false;
        }
    }
}
