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
        final File big = new File("1mb.json");

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
