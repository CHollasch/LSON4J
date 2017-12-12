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

import net.hollasch.lson4j.type.LSONValue;
import net.hollasch.lson4j.type.provided.LSONBooleanTypeAdapter;
import net.hollasch.lson4j.type.provided.LSONTypeAdapters;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @author Connor Hollasch
 * @since Dec 09, 1:15 AM
 */
public class LSONTest
{
    private static int testIndex = 0;

    public static void main (final String... args) throws IOException, LSONParseException
    {
        doParseTest("");
        doParseTest("// Nothing but a comment");
        doParseTest("testString");
        doParseTest("0");
        doParseTest("true");
        doParseTest("null");
        doParseTest("[ ]");
        doParseTest("{ }");
        doParseTest("<a b c>:[<A B C>]");
        doParseTest("{Key:Value;Key2:Value2}");
        doParseTest("{Key:\"Value Line 1\"\n + \" \" + \"Value Line 2\"}");
        doParseTest("{\"true\":true}");
        doParseTest("{numberTest:[1,1.0,.5,-12,0xFF]}");
    }

    private static void doParseTest (final String rawData) throws IOException, LSONParseException
    {
        final String header = "============= Test #" + (++LSONTest.testIndex) + " =============";
        System.out.println(header);
        System.out.println("Raw data: " + rawData);

        final LSONValue value = LSON.parseWithAdapters(rawData, LSONTypeAdapters.BOOLEAN, LSONTypeAdapters.NUMBER);

        System.out.println("Parses into: " + value);

        final StringBuilder footer = new StringBuilder();
        for (int i = 0; i < header.length(); ++i) {
            footer.append("=");
        }

        System.out.println(footer.toString());
        System.out.println();
    }
}
