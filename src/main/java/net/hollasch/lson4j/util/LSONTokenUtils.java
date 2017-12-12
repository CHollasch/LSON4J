/*
 * Copyright (C) 2017 LSON4J - All Rights Reserved
 *
 * Unauthorized copying of this file, via any median is strictly prohibited
 * proprietary and confidential. For more information, please contact me at
 * connor@hollasch.net
 *
 * Written by Connor Hollasch <connor@hollasch.net>, December 2017
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

    public static final int LSON_STRUCTURE_OPENER = '<';
    public static final int LSON_STRUCTURE_CLOSER = '>';

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
            case LSON_STRUCTURE_CLOSER:
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
}
