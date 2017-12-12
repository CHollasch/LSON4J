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

package net.hollasch.lson4j.type.provided;

import net.hollasch.lson4j.type.LSONTypeAdapter;

/**
 * @author Connor Hollasch
 * @since Dec 11, 9:14 PM
 */
public class LSONNumberTypeAdapter implements LSONTypeAdapter<Number>
{
    @Override
    public Number buildFromWord (String value)
    {
        if (value.contains("x")
                && value.length() > 2
                && (value.charAt(1) == 'x' || value.charAt(1) == 'X')) {
            return Long.parseLong(value.substring(2), 16);
        } else {
            return Double.parseDouble(value);
        }
    }

    @Override
    public boolean willAdaptFor (final String word)
    {
        if (word.length() == 0) {
            return false;
        }

        final char first = word.charAt(0);
        switch (first) {
            case '.':
            case '-':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return true;
            default:
                return false;
        }
    }
}
