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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Connor Hollasch
 * @since Dec 11, 7:28 PM
 */
public class LSONObject<V extends LSONValue> extends LSONValue
{
    private final Map<LSONString, V> map;

    public LSONObject (final Map<LSONString, V> map)
    {
        this.map = map;
    }

    public Map<LSONString, V> toHashMap ()
    {
        return this.map;
    }

    public V get (final String key)
    {
        return get(new LSONString(key));
    }

    public V get (final LSONString key)
    {
        return this.map.get(key);
    }

    public Set<LSONString> keySet ()
    {
        return this.map.keySet();
    }

    public Collection<V> values ()
    {
        return this.map.values();
    }

    public Set<Map.Entry<LSONString, V>> entrySet ()
    {
        return this.map.entrySet();
    }

    public V put (final String key, final V value)
    {
        return this.map.put(new LSONString(key), value);
    }

    public V put (final LSONString key, final V value)
    {
        return this.map.put(key, value);
    }

    public int size ()
    {
        return this.map.size();
    }

    public boolean isEmpty ()
    {
        return this.map.isEmpty();
    }

    @Override
    public boolean isLSONObject ()
    {
        return true;
    }

    @Override
    public boolean equals (final Object obj)
    {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof LSONObject)) {
            if (!(obj instanceof Map)) {
                return false;
            }

            return obj.equals(this.map);
        }

        return ((LSONObject) obj).map.equals(this.map);
    }

    @Override
    public int hashCode ()
    {
        return this.map.hashCode();
    }

    @Override
    public String toString ()
    {
        return toHashMap().toString();
    }
}
