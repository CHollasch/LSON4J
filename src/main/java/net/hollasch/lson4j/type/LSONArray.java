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
import java.util.List;

/**
 * @author Connor Hollasch
 * @since Dec 11, 7:28 PM
 */
public class LSONArray<T extends LSONValue> extends LSONValue implements Iterable<T>
{
    private final ArrayList<T> array;

    public LSONArray (final ArrayList<T> array)
    {
        this.array = array;
    }

    public T get (final int index)
    {
        return this.array.get(index);
    }

    public T set (final int index, final T value)
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

    @Override
    public boolean isLSONArray ()
    {
        return true;
    }

    public ArrayList<T> toArrayList ()
    {
        return this.array;
    }

    @Override
    public Iterator<T> iterator ()
    {
        return this.array.iterator();
    }

    @Override
    public boolean equals (final Object obj)
    {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof LSONArray)) {
            if (!(obj instanceof List)) {
                return false;
            }

            return obj.equals(this.array);
        }

        return ((LSONArray) obj).array.equals(this.array);
    }

    @Override
    public int hashCode ()
    {
        return this.array.hashCode();
    }

    @Override
    public String toString ()
    {
        return toArrayList().toString();
    }
}
