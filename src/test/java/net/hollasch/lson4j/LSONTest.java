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

import net.hollasch.lson4j.type.LSONObject;
import net.hollasch.lson4j.type.LSONTable;
import net.hollasch.lson4j.type.LSONValue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;

/**
 * @author Connor Hollasch
 * @since Dec 09, 1:15 AM
 */
public class LSONTest
{
    private static int testIndex = 0;

    public static void main (final String... args) throws IOException, LSONParseException
    {
        /*doParseTest("");
        doParseTest("// Nothing but a comment");
        doParseTest("testString");
        doParseTest("0");
        doParseTest("true");
        doParseTest("null");
        doParseTest("[ ]");
        doParseTest("{ }");
        doParseTest("{Key:Value;Key2:Value2}");
        doParseTest("{Key:\"Value Line 1\"\n + \" \" + \"Value Line 2\"}");
        doParseTest("{\"true\":true}");
        doParseTest("{numberTest:[1,1.0,.5,-12,0xFF]}");
        doParseTest("{key:\"A\" + \"B\"}");
        doParseTest("[# key1 key2 key3: #]");*/

        /*final LSONObject o = (LSONObject) LSON.parse(new StringReader("{\n" +
                "    id: base01\n" +
                "    popup: {\n" +
                "        menus: {\n" +
                "\n" +
                "            // Table (2 columns, 3 rows) with optional brackets and semicolon separators:\n" +
                "            File: [#\n" +
                "                [ Value  ; Action       ]\n" +
                "                :\n" +
                "                [ New    ; [# hi bye : 1 {key:[# a b c : d [1,2,3] f #]} #]]\n" +
                "                [ Open   ; OpenDoc      ]\n" +
                "                [ Close  ; CloseDoc     ]\n" +
                "            #]\n" +
                "\n" +
                "            // Table (2 columns, 3 rows) without optional brackets, with optiona semi-colons:\n" +
                "            Edit: [# value,action: Copy,CopySelection; Cut,CutSelection; Paste,PasteItem #]\n" +
                "        }\n" +
                "    }\n" +
                "}"));

        System.out.println(o.get("popup").toObject().get("menus").toObject().get("File").toTable());*/
        
        final LSONValue o = LSON.parse(new FileInputStream(new File("test.lson")));

        System.out.println(o);
    }

    private static void doParseTest (final String rawData) throws IOException, LSONParseException
    {
        final String header = "============= Test #" + (++LSONTest.testIndex) + " =============";
        System.out.println(header);
        System.out.println("Raw data: " + rawData);

        final LSONValue value = LSON.parse(rawData);

        System.out.println("Parses into: " + value);

        final StringBuilder footer = new StringBuilder();
        for (int i = 0; i < header.length(); ++i) {
            footer.append("=");
        }

        System.out.println(footer.toString());
        System.out.println();
    }
}
