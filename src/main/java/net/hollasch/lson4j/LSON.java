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
