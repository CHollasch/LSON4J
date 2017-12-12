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

import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Connor Hollasch
 * @since Dec 11, 4:18 PM
 */
public class LSONBenchmark
{
    public static void main (final String... args) throws IOException, LSONParseException
    {
        final File big = new File("warframe.json");

        System.out.println("Parsing a 1mb test file...");

        timeParser("LSON4J", () -> new LSONParser(new LSONReader(new FileReader(big))).parse());
        timeParser("JSON-Simple", () -> new JSONParser().parse(new FileReader(big)));
    }

    private static void timeParser (final String name, final RunnableThrowall runnable)
    {
        final long start = System.currentTimeMillis();

        try {
            runnable.run();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final long end = System.currentTimeMillis() - start;
        System.out.println(name + " took " + end + " milliseconds...");
    }

    private interface RunnableThrowall
    {
        void run () throws Exception;
    }
}
