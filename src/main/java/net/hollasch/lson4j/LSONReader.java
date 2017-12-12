/*
 * Copyright (c) 2017 Connor Hollasch
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
                    new LSONFileLocation(this.offset, this.line, this.column));
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
