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
 * @since Dec 10, 11:15 PM
 */
public class LSONParseException extends Exception
{
    private final String reason;
    private final LSONLocation location;

    public LSONParseException (final String reason, final LSONLocation location)
    {
        this.reason = reason;
        this.location = location;
    }

    public String getReason ()
    {
        return this.reason;
    }

    public LSONLocation getLocation ()
    {
        return this.location;
    }

    @Override
    public String getMessage ()
    {
        return this.reason + " at line: " + this.location.getLine() + ", column: " + this.location.getColumn();
    }
}
