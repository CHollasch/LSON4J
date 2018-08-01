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

/**
 * @author Connor Hollasch
 * @since Dec 11, 7:28 PM
 */
public class LSONWord<T> extends LSONValue
{
    private String word;
    private T object;

    public LSONWord (final String word, final T object)
    {
        this.word = word;
        this.object = object;
    }

    public String getWord ()
    {
        return this.word;
    }

    public T getObject ()
    {
        return this.object;
    }

    @Override
    public boolean isLSONWord ()
    {
        return true;
    }

    @Override
    public String toString ()
    {
        return getObject().toString();
    }

    @Override
    public boolean equals (final Object obj)
    {
        if (obj == null) {
            return false;
        }

        return obj.equals(this.object);
    }

    @Override
    public int hashCode ()
    {
        return (31 * getWord().hashCode()) + (31 * getObject().hashCode());
    }
}
