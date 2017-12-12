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

import net.hollasch.lson4j.type.LSONTypeAdapter;
import net.hollasch.lson4j.type.LSONValue;
import net.hollasch.lson4j.type.provided.LSONTypeAdapters;

import java.io.*;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author Connor Hollasch
 * @since Dec 11, 8:58 PM
 */
public class LSON
{
    private LSON ()
    {
        // Cannot create an LSON object.
    }

    public static LSONValue parse (final String string)
            throws IOException, LSONParseException
    {
        return parseWithAdapters(new StringReader(string));
    }

    public static LSONValue parse (final InputStream inputStream)
            throws IOException, LSONParseException
    {
        return parseWithAdapters(new InputStreamReader(inputStream));
    }

    public static LSONValue parse (final Reader reader)
            throws IOException, LSONParseException
    {
        return parseWithAdapters(reader);
    }

    public static LSONValue parseWithAdapters (final String string, final LSONTypeAdapters... typeAdapters)
            throws IOException, LSONParseException
    {
        return parseWithAdapters(new StringReader(string), typeAdapters);
    }

    public static LSONValue parseWithAdapters (final InputStream stream, final LSONTypeAdapters... typeAdapters)
            throws IOException, LSONParseException
    {
        return parseWithAdapters(new InputStreamReader(stream), typeAdapters);
    }

    public static LSONValue parseWithAdapters (final Reader reader, final LSONTypeAdapters... typeAdapters)
            throws IOException, LSONParseException
    {
        final Collection<LSONTypeAdapter<?>> adapterList = new HashSet<>();

        for (final LSONTypeAdapters adapter : typeAdapters) {
            adapterList.add(adapter.createAdapter());
        }

        return parseWithAdapters(reader, adapterList);
    }

    public static LSONValue parseWithAdapters (final String string, final Collection<LSONTypeAdapter<?>> adapters)
            throws IOException, LSONParseException
    {
        return parseWithAdapters(new StringReader(string), adapters);
    }

    public static LSONValue parseWithAdapters (final InputStream stream, final Collection<LSONTypeAdapter<?>> adapters)
            throws IOException, LSONParseException
    {
        return parseWithAdapters(new InputStreamReader(stream), adapters);
    }

    public static LSONValue parseWithAdapters (final Reader reader, final Collection<LSONTypeAdapter<?>> adapters)
            throws IOException, LSONParseException
    {
        final LSONReader lsonReader = new LSONReader(reader);
        final LSONParser lsonParser = new LSONParser(lsonReader, adapters);

        return lsonParser.parse();
    }
}
