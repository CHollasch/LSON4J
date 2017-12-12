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

import java.io.IOException;
import java.io.Reader;

import static net.hollasch.lson4j.util.LSONTokenUtils.*;

/**
 * @author Connor Hollasch
 * @since Dec 09, 1:10 AM
 */
public class LSONReader
{
    private Reader reader;

    private char current;
    private int offset;
    private int line;
    private int column;

    private boolean finished;

    protected LSONReader (final Reader reader)
    {
        this.reader = reader;

        this.current = 0x0;
        this.offset = 0;
        this.line = 1;
        this.column = -1;

        this.finished = false;
    }

    public char readNext () throws IOException, LSONParseException
    {
        if (this.finished) {
            throw new LSONParseException(
                    "Attempted to read through end of file.",
                    new LSONLocation(this.offset, this.line, this.column));
        }

        if (!this.reader.ready() || (this.current == END_OF_FILE || this.current == END_OF_STRING)) {
            this.finished = true;
        }

        if (isNewline(this.current)) {
            this.line++;
            this.column = 0;
        }

        this.current = (char) this.reader.read();
        this.offset++;
        this.column++;

        return this.current;
    }

    public boolean isFinished ()
    {
        return this.finished;
    }

    public char getCurrent ()
    {
        return this.current;
    }

    public int getOffset ()
    {
        return this.offset;
    }

    public int getColumn ()
    {
        return this.column;
    }

    public int getLine ()
    {
        return this.line;
    }
}
