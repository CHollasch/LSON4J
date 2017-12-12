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

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Connor Hollasch
 * @since Dec 11, 7:28 PM
 */
public class LSONArray extends LSONValue implements Iterable<LSONValue>
{
    private final ArrayList<LSONValue> array;

    public LSONArray (final ArrayList<LSONValue> array)
    {
        this.array = array;
    }

    public LSONValue get (final int index)
    {
        return this.array.get(index);
    }

    public LSONValue set (final int index, final LSONValue value)
    {
        return this.array.set(index, value);
    }

    public int size ()
    {
        return this.array.size();
    }

    public boolean isEmpty ()
    {
        return this.array.isEmpty();
    }

    public ArrayList<LSONValue> toArrayList ()
    {
        return this.array;
    }

    @Override
    public Iterator<LSONValue> iterator ()
    {
        return this.array.iterator();
    }

    @Override
    public String toString ()
    {
        return toArrayList().toString();
    }
}
