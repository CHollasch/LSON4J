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

import java.io.IOException;
import java.io.StringReader;

/**
 * @author Connor Hollasch
 * @since Dec 09, 1:15 AM
 */
public class LSONTest
{
    private static final String test2 = "{collection<value,isPositive>:[<-1.5;false><0><2.4;true,,,,red,,,,,,,,null>]}";

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
    }

    private static void doParseTest (final String rawData) throws IOException, LSONParseException
    {
        final String header = "============= Test #" + (++LSONTest.testIndex) + " =============";
        System.out.println(header);
        System.out.println("Raw data: " + rawData);

        LSONReader read = new LSONReader(new StringReader(rawData));
        LSONParser parser = new LSONParser(read);

        System.out.println("Parses into: " + parser.parse());

        final StringBuilder footer = new StringBuilder();
        for (int i = 0; i < header.length(); ++i) {
            footer.append("=");
        }

        System.out.println(footer.toString());
        System.out.println();
    }
}
