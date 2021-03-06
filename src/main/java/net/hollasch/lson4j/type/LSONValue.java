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


package net.hollasch.lson4j.type;

import net.hollasch.lson4j.type.graph.LSONGraph;

import java.io.Serializable;

/**
 * @author Connor Hollasch
 * @since Dec 11, 7:27 PM
 */
public abstract class LSONValue implements Serializable
{
    public boolean isLSONObject ()
    {
        return false;
    }

    public boolean isLSONArray ()
    {
        return false;
    }

    public boolean isTable ()
    {
        return false;
    }

    public boolean isGraph ()
    {
        return false;
    }

    public boolean isLSONWord ()
    {
        return false;
    }

    public boolean isLSONString ()
    {
        return false;
    }

    public LSONObject toObject ()
    {
        return (LSONObject) this;
    }

    public <T extends LSONValue> LSONArray<T> toArray ()
    {
        return (LSONArray<T>) this;
    }

    public LSONTable toTable ()
    {
        return (LSONTable) this;
    }

    public LSONGraph toGraph ()
    {
        return (LSONGraph) this;
    }

    public LSONWord toWord ()
    {
        return (LSONWord) this;
    }

    public LSONString toLsonString ()
    {
        return (LSONString) this;
    }

    public boolean literalEquals (final Object other)
    {
        return super.equals(other);
    }
}
