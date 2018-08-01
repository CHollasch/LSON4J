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
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION-
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.hollasch.lson4j;

import net.hollasch.lson4j.util.LSONTokenUtils;

import java.io.IOException;
import java.io.Reader;

/**
 * @author Connor Hollasch
 * @since Dec 09, 1:10 AM
 */
public class LSONReader
{
    private static final int DEFAULT_BUFFER_LENGTH = 1024;

    private Reader reader;

    private int bufferLength;
    private int[] buffer;

    private boolean prepared;
    private Character peeked;

    private char current;

    private int bufferOffset;
    private int line;
    private int column;

    private boolean finished;

    LSONReader (final Reader reader)
    {
        this(reader, DEFAULT_BUFFER_LENGTH);
    }

    LSONReader (final Reader reader, final int bufferLength)
    {
        this.reader = reader;
        this.buffer = new int[this.bufferLength = bufferLength];

        this.prepared = false;
        this.peeked = null;

        this.current = (char) 0;

        this.bufferOffset = -1;
        this.line = 1;
        this.column = -1;

        this.finished = false;
    }

    synchronized void prepare () throws IOException
    {
        for (int i = 0; i < this.bufferLength; ++i) {
            this.buffer[i] = this.reader.read();
        }

        this.prepared = true;
    }

    char readNext () throws LSONParseException, IOException
    {
        readNext(false);
        return this.current;
    }

    char peekNext () throws LSONParseException, IOException
    {
        if (this.peeked != null) {
            return this.peeked;
        }

        readNext(true);

        if (this.peeked == null) {
            return (char) 0x0;
        }

        return this.peeked;
    }

    private void readNext (final boolean peek) throws LSONParseException, IOException
    {
        if (!this.prepared) {
            throw new LSONParseException("Unprepared read from LSON reader.", new LSONFileLocation(0, 0));
        }

        if (this.finished) {
            if (peek) {
                return;
            }

            throw new LSONParseException(
                    "Attempted to read through input reader",
                    new LSONFileLocation(this.line, this.column)
            );
        }

        char value;
        if (this.peeked == null) {
            ++this.bufferOffset;

            if (this.bufferOffset >= this.buffer.length) {
                this.bufferOffset = 0;

                for (int i = 0; i < this.bufferLength; ++i) {
                    this.buffer[i] = this.reader.read();
                }
            }

            value = (char) this.buffer[this.bufferOffset];
        } else {
            value = this.peeked;
            this.peeked = null;
        }

        if (!peek) {
            ++this.column;
            if (value == '\n') {
                ++this.line;
                this.column = 0;
            }

            if (this.buffer[this.bufferOffset] == -1) {
                this.finished = true;
            }

            this.current = value;
        } else {
            this.peeked = value;
        }
    }

    boolean isFinished ()
    {
        return this.finished;
    }

    char getCurrent ()
    {
        return this.current;
    }

    int getColumn ()
    {
        return this.column;
    }

    int getLine ()
    {
        return this.line;
    }
}
