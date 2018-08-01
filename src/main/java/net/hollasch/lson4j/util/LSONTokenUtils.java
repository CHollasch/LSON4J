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

package net.hollasch.lson4j.util;

/**
 * @author Connor Hollasch
 * @since Dec 09, 2:09 AM
 */
public class LSONTokenUtils
{
    //==================================================================================================================
    // Special characters
    //==================================================================================================================

    public static final int NULL_BYTE = 0x0;

    public static final int END_OF_FILE = 0x4;
    public static final int END_OF_STRING = 0xFFFF;

    //==================================================================================================================
    // Whitespace characters
    //==================================================================================================================

    // Newline characters.
    public static final int NEXT_LINE = 0x85;
    public static final int NEWLINE = '\n';
    public static final int CARRIAGE_RETURN = '\r';
    public static final int FORM_FEED = '\f';

    // Space characters
    public static final int TAB = '\t';
    public static final int VERTICAL_TAB = 0xB;
    public static final int SPACE_CHARACTER = 0x20;
    public static final int COMMA = 0x2C;
    public static final int SEMICOLON = 0x3B;
    public static final int NO_BREAK_SPACE = 0xA0;
    public static final int OGHAM_SPACE_MARK = 0x1680;
    public static final int EN_QUAD = 0x2000;
    public static final int EM_QUAD = 0x2001;
    public static final int EM_SPACE = 0x2003;
    public static final int THREE_PER_EM_SPACE = 0x2004;
    public static final int FOUR_PER_EM_SPACE = 0x2005;
    public static final int SIX_PER_EM_SPACE = 0x2006;
    public static final int FIGURE_SPACE = 0x2007;
    public static final int PUNCTUATION_SPACE = 0x2008;
    public static final int THIN_SPACE = 0x2009;
    public static final int HAIR_SPACE = 0x200A;
    public static final int LINE_SEPARATOR = 0x2028;
    public static final int NARROW_NO_BREAK_SPACE = 0x202f;
    public static final int MEDIUM_MATHEMATICAL_SPACE = 0x205f;
    public static final int IDEOGRAPHIC_SPACE = 0x3000;

    //==================================================================================================================
    // String Delimiters
    //==================================================================================================================

    public static final int QUOTATION_MARK = '"';
    public static final int APOSTROPHE = '\'';

    public static final int POINTED_DOUBLE_ANGLE_QUOTATION_MARK_LEFT = 0xAB;
    public static final int POINTED_DOUBLE_ANGLE_QUOTATION_MARK_RIGHT = 0xBB;

    public static final int SINGLE_QUOTATION_MARK_LEFT = 0x2018;
    public static final int SINGLE_QUOTATION_MARK_RIGHT = 0X2019;

    public static final int DOUBLE_QUOTATION_MARK_LEFT = 0x201C;
    public static final int DOUBLE_QUOTATION_MARK_RIGHT = 0x201D;

    //==================================================================================================================
    // LSON Characters
    //==================================================================================================================

    public static final int COMMENT_START = '/';
    public static final int COMMENT_BLOCK_DETERMINANT = '*';
    public static final int ESCAPE_CHARACTER = '\\';
    public static final int KEY_VALUE_SEPARATOR = ':';
    public static final int STRING_CONCATENATION_OPERATOR = '+';

    public static final int LSON_OBJECT_OPENER = '{';
    public static final int LSON_OBJECT_CLOSER = '}';

    public static final int LSON_ARRAY_OPENER = '[';
    public static final int LSON_ARRAY_CLOSER = ']';

    public static final int LSON_TABLE_STARTER = '#';
    public static final int LSON_TABLE_HEADER_END = ':';

    //==================================================================================================================
    // LSON Graph Characters
    //==================================================================================================================

    public static final int LSON_GRAPH_STARTER = '%';

    public static final int GRAPH_UNDIRECTED_HYPHEN = '-';
    public static final int GRAPH_UNDIRECTED_LR_ARROW = 0x2194;
    public static final int GRAPH_RIGHT_DIRECTED_GREATER_THAN = '>';
    public static final int GRAPH_RIGHT_DIRECTED_ARROW = 0x2192;
    public static final int GRAPH_LEFT_DIRECTED_LESS_THAN = '<';
    public static final int GRAPH_LEFT_DIRECTED_ARROW = 0x2190;

    public static final int GRAPH_UNDIRECTED = 0;
    public static final int GRAPH_RIGHT_DIRECTED = 1;
    public static final int GRAPH_LEFT_DIRECTED = 2;

    //==================================================================================================================
    // LSON Etc
    //==================================================================================================================

    public static final int[] NUMBERS = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};

    //==================================================================================================================

    public static boolean isOpeningString (final int character)
    {
        switch (character) {
            case QUOTATION_MARK:
            case APOSTROPHE:
            case POINTED_DOUBLE_ANGLE_QUOTATION_MARK_LEFT:
            case SINGLE_QUOTATION_MARK_LEFT:
            case DOUBLE_QUOTATION_MARK_LEFT:
                return true;
            default:
                return false;
        }
    }

    public static boolean isEndOfRead (final int character)
    {
        switch (character) {
            case NULL_BYTE:
            case END_OF_FILE:
            case END_OF_STRING:
                return true;
            default:
                return false;
        }
    }

    public static boolean isClosingString (final int openingCharacter, final int closingCharacter)
    {
        switch (openingCharacter) {
            case QUOTATION_MARK:
                return closingCharacter == QUOTATION_MARK;
            case APOSTROPHE:
                return closingCharacter == APOSTROPHE;
            case POINTED_DOUBLE_ANGLE_QUOTATION_MARK_LEFT:
                return closingCharacter == POINTED_DOUBLE_ANGLE_QUOTATION_MARK_RIGHT;
            case SINGLE_QUOTATION_MARK_LEFT:
                return closingCharacter == SINGLE_QUOTATION_MARK_RIGHT;
            case DOUBLE_QUOTATION_MARK_LEFT:
                return closingCharacter == DOUBLE_QUOTATION_MARK_RIGHT;
            default:
                return false;
        }
    }

    public static boolean isLSONClosingReservedToken (final int token)
    {
        switch (token) {
            case LSON_OBJECT_CLOSER:
            case LSON_ARRAY_CLOSER:
                return true;
            default:
                return false;
        }
    }

    public static boolean isNewline (final int character)
    {
        switch (character) {
            case NEXT_LINE:
            case NEWLINE:
            case CARRIAGE_RETURN:
            case FORM_FEED:
                return true;
            default:
                return false;
        }
    }

    public static int getEdgeDirectionality (final int character)
    {
        switch (character) {
            case GRAPH_UNDIRECTED_HYPHEN:
            case GRAPH_UNDIRECTED_LR_ARROW:
                return GRAPH_UNDIRECTED;
            case GRAPH_LEFT_DIRECTED_ARROW:
            case GRAPH_LEFT_DIRECTED_LESS_THAN:
                return GRAPH_LEFT_DIRECTED;
            case GRAPH_RIGHT_DIRECTED_ARROW:
            case GRAPH_RIGHT_DIRECTED_GREATER_THAN:
                return GRAPH_RIGHT_DIRECTED;
            default:
                return -1;
        }
    }

    public static boolean isWhitespace (final int character)
    {
        switch (character) {
            case TAB:
            case VERTICAL_TAB:
            case SPACE_CHARACTER:
            case COMMA:
            case SEMICOLON:
            case NO_BREAK_SPACE:
            case OGHAM_SPACE_MARK:
            case EN_QUAD:
            case EM_QUAD:
            case EM_SPACE:
            case THREE_PER_EM_SPACE:
            case FOUR_PER_EM_SPACE:
            case SIX_PER_EM_SPACE:
            case FIGURE_SPACE:
            case PUNCTUATION_SPACE:
            case THIN_SPACE:
            case HAIR_SPACE:
            case LINE_SEPARATOR:
            case NARROW_NO_BREAK_SPACE:
            case MEDIUM_MATHEMATICAL_SPACE:
            case IDEOGRAPHIC_SPACE:
                return true;
            default:
                return isNewline(character);
        }
    }

    public static boolean isNumber (final int character)
    {
        switch (character) {
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
