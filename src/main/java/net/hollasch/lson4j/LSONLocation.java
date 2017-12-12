/*
 * Copyright (C) 2017 LSON4J - All Rights Reserved
 *
 * Unauthorized copying of this file, via any median is strictly prohibited
 * proprietary and confidential. For more information, please contact me at
 * connor@hollasch.net
 *
 * Written by Connor Hollasch <connor@hollasch.net>, December 2017
 */

package net.hollasch.lson4j;

/**
 * @author Connor Hollasch
 * @since Dec 09, 1:42 AM
 */
public class LSONLocation
{
    private final int offset;
    private final int line;
    private final int column;

    public LSONLocation (final int offset, final int line, final int column)
    {
        this.offset = offset;
        this.line = line;
        this.column = column;
    }

    public int getOffset ()
    {
        return this.offset;
    }

    public int getLine ()
    {
        return this.line;
    }

    public int getColumn ()
    {
        return this.column;
    }

    @Override
    public String toString ()
    {
        return this.line + ":" + this.column;
    }
}
