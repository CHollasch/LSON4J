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

import net.hollasch.lson4j.type.*;
import net.hollasch.lson4j.util.LSONTokenUtils;

import java.io.IOException;
import java.util.*;

import static net.hollasch.lson4j.util.LSONTokenUtils.*;

/**
 * @author Connor Hollasch
 * @since Dec 09, 12:49 AM
 */
public class LSONParser
{
    private LSONReader reader;
    private Collection<LSONTypeAdapter<?>> typeAdapters;

    public LSONParser (final LSONReader reader) throws IOException, LSONParseException
    {
        this(reader, Collections.emptyList());
    }

    public LSONParser (
            final LSONReader reader,
            final Collection<LSONTypeAdapter<?>> typeAdapters)
        throws IOException, LSONParseException
    {
        this.reader = reader;
        this.typeAdapters = typeAdapters;

        // Perform initial readNext.
        this.reader.readNext();
    }

    public LSONValue parse () throws IOException, LSONParseException
    {
        // When parsing, remove whitespace before reading initial value as comments and whitespace are allowed previous
        // to the LSON value read in.
        removeWhitespace();

        // Read the value in the reader and return.
        final LSONValue value = readValue();

        // Remove trailing whitespace.
        removeWhitespace(true);

        // Throw an exception when there is still data to be read.
        if (value != null && !this.reader.isFinished()) {
            throw new LSONParseException("Expected end of file", getLocation());
        }

        return value;
    }

    private LSONValue readValue () throws IOException, LSONParseException
    {
        // Capture the initial character to determine what kind of value we are reading in. Either an object, array, or
        // just a regular word.
        final char current = this.reader.getCurrent();

        switch (current) {
            // Read LSON object.
            case LSON_OBJECT_OPENER:
                return readObject();

            // Read LSON Array.
            case LSON_ARRAY_OPENER:
                return readArray();

            // Read LSON Structure.
            case LSON_STRUCTURE_OPENER:
                return readStructure();

            // Handle end of read terminators when reading an empty file.
            case NULL_BYTE:
            case END_OF_FILE:
            case END_OF_STRING:
                return null;

            // Default to reading a word in.
            default:
                return readWord(false, false);
        }
    }

    private LSONObject readObject () throws IOException, LSONParseException
    {
        // Pop object opening character.
        this.reader.readNext();

        final Map<LSONString, LSONValue> lsonObj = new HashMap<>();

        // After the initial object opening character, print a
        removeWhitespace();

        // Handle an empty object.
        if (this.reader.getCurrent() == LSON_OBJECT_CLOSER) {
            // Pop closing tag and return empty object.
            this.reader.readNext();
            return new LSONObject(lsonObj);
        }

        // Keep track of the floating character (character after previous read operation) so we know when to stop
        // reading values into the object.
        char floating;
        do {
            // Create the key word.
            final LSONString key = (LSONString) readWord(true, true);
            removeWhitespace();

            final LSONValue value;

            // If the next token after the key is not a key value separator, it might be a structure opening tag.
            if (this.reader.getCurrent() == LSON_STRUCTURE_OPENER) {
                // Read structure and continue.
                value = readStructure();
                removeWhitespace();

                lsonObj.put(key, value);
            } else {
                // Pop key value separator token.
                expect(KEY_VALUE_SEPARATOR, "Expected a " + (char) KEY_VALUE_SEPARATOR
                        + " to separate key, value pairs");
                this.reader.readNext();

                if (key == null) {
                    throw new LSONParseException("Cannot create LSON object where a key is null", getLocation());
                }

                // Remove all whitespace and parse value.
                removeWhitespace();
                value = readValue();
                removeWhitespace();

                // Store key value pair in lson object.
                lsonObj.put(key, value);
            }

            // Re-determine the floating character and perform end of object checks.
            floating = this.reader.getCurrent();

            if (value == null) {
                throw new LSONParseException("Cannot create LSON object where a value is null", getLocation());
            }
        } while (floating != LSON_OBJECT_CLOSER);

        // Pop object closing character.
        expect(LSON_OBJECT_CLOSER, "Expected " + LSON_OBJECT_CLOSER + " for object terminator");
        this.reader.readNext();

        return new LSONObject(lsonObj);
    }

    private LSONStructure readStructure () throws IOException, LSONParseException
    {
        // Pop structure opening character.
        this.reader.readNext();
        final ArrayList<LSONObject<?>> structure = new ArrayList<>();
        removeWhitespace();

        // Load all of the structures keys in.
        final ArrayList<LSONString> structureKeys = new ArrayList<>();
        char floating;
        do {
            // Note that keys can be any value type.
            final LSONWord word = readWord(false, true);

            if (!(word instanceof LSONString)) {
                throw new LSONParseException("Parsed word is not a string", getLocation());
            }

            structureKeys.add((LSONString) word);

            // Read whitespace, re-determine floating character and check to see if the structure header is finished.
            removeWhitespace();
            floating = this.reader.getCurrent();
        } while (floating != LSON_STRUCTURE_CLOSER);

        // Pop structure header tag closing character.
        this.reader.readNext();
        removeWhitespace();

        // Expect a key value separator.
        expect(KEY_VALUE_SEPARATOR, "Expected a " + (char) KEY_VALUE_SEPARATOR
                + " to separate structure header and body");
        this.reader.readNext();
        removeWhitespace();

        // Expect an array opening tag.
        expect(LSON_ARRAY_OPENER, "Expected an " + (char) LSON_ARRAY_OPENER + " for structure opening");
        this.reader.readNext();
        removeWhitespace();

        // Expect either the opening of a structure body fragment or the end of an array (no structure components).
        expectAny("Invalid token, expected either " + (char) LSON_STRUCTURE_OPENER
                        + " or " + (char) LSON_ARRAY_CLOSER + " token",
                LSON_STRUCTURE_OPENER,
                LSON_ARRAY_CLOSER);

        char current = this.reader.getCurrent();
        if (current != LSON_ARRAY_CLOSER) {
            // Load structure up.
            do {
                final ArrayList<LSONValue> lsonStructureComponent = new ArrayList<>();

                // Pop structure component opening tag.
                this.reader.readNext();
                removeWhitespace();

                do {
                    // Read all structure body fragments for the structure body piece.
                    lsonStructureComponent.add(readValue());
                    removeWhitespace();

                    // Stop reading body fragments when the body piece is finished.
                    floating = this.reader.getCurrent();
                } while (floating != LSON_STRUCTURE_CLOSER);

                // Pop structure component closing tag.
                this.reader.readNext();
                removeWhitespace();

                // Create a component map for the key (structure header) and value (structure body piece).
                final Map<LSONString, LSONValue> componentMap = new HashMap<>();

                // Map all key value pairs.
                for (int idx = 0; idx < structureKeys.size(); ++idx) {
                    // If the body piece has less fragments than the header, stop loading more from the body piece.
                    if (lsonStructureComponent.size() <= idx) {
                        break;
                    }

                    final LSONString key = structureKeys.get(idx);
                    final LSONValue value = lsonStructureComponent.get(idx);

                    componentMap.put(key, value);
                }

                // Keep reading body pieces until the array closing tag is present.
                // This marks the end of all body pieces.
                structure.add(new LSONObject(componentMap));
                floating = this.reader.getCurrent();
            } while (floating != LSON_ARRAY_CLOSER);
        }

        // Pop structure closing character.
        this.reader.readNext();
        return new LSONStructure(structure, structureKeys);
    }

    private LSONArray readArray () throws IOException, LSONParseException
    {
        // Pop object opening character.
        this.reader.readNext();
        final ArrayList<LSONValue> array = new ArrayList<>();
        removeWhitespace();

        if (this.reader.getCurrent() == LSON_ARRAY_CLOSER) {
            // Pop closing tag and return empty object.
            this.reader.readNext();
            return new LSONArray(array);
        }

        char floating;
        do {
            // Keep reading values into the array.
            array.add(readValue());
            removeWhitespace();

            // Read values until there is an array closing tag.
            floating = this.reader.getCurrent();
        } while (floating != LSON_ARRAY_CLOSER);

        // Expect an array closing tag for termination. This line is just a syntax reminder as the loop will never
        // terminate if an array closing tag is not present either way.
        expect(LSON_ARRAY_CLOSER, "Expected " + (char) LSON_ARRAY_CLOSER + " for array terminator.");
        this.reader.readNext();

        return new LSONArray(array);
    }

    private LSONWord readWord (final boolean isObjectKey, final boolean forceString)
            throws IOException, LSONParseException
    {
        // Load current character and create word capture.
        char current = this.reader.getCurrent();
        final StringBuilder capture = new StringBuilder();

        boolean promotedToString = false;

        // If a reserved closing token is present, there is no word to parse.
        if (LSONTokenUtils.isLSONClosingReservedToken(current)) {
            return null;
        }

        // Create a non-terminating loop to allow for string concatenation later on.
        while (true) {
            // Check to see if the word is delimited and save opening delimiter.
            char stringOpener = NULL_BYTE;
            if (LSONTokenUtils.isOpeningString(current)) {
                promotedToString = true;
                stringOpener = current;
                current = this.reader.readNext();
            }

            // Read through string until we reach an end
            do {
                // Handle parsing a single word.
                if (this.reader.isFinished()) {
                    // Substring, as isFinished returns true after end of file has been read in.
                    // We capture the entire string minus the end of file character at the end.
                    return buildWord(capture.substring(0, capture.length() -1), promotedToString
                            || forceString || isObjectKey);
                }

                // Capture the escaped character.
                if (current == ESCAPE_CHARACTER) {
                    current = captureEscapeCharacter();
                }

                if (stringOpener == NULL_BYTE) {
                    // If this word is a key and the current token is a key value separator, break out.
                    if (isObjectKey && current == KEY_VALUE_SEPARATOR) {
                        break;
                    }

                    // If we are closing something, break out of parsing this word.
                    else if (LSONTokenUtils.isLSONClosingReservedToken(current)) {
                        break;
                    }

                    // If we open a structure, our word is done parsing so we can continue to build the structure.
                    else if (current == LSON_STRUCTURE_OPENER) {
                        break;
                    }
                }

                // Append current to buffer and continue.
                capture.append(current);
                current = this.reader.readNext();
            }

            // Keep looping until either...
            //  A. We reached the end of the file.
            //  B. There was a character opening an encapsulated string and the closing character is present.
            //  C. This is a non-delimited word and we have whitespace.
            while (stringOpener == NULL_BYTE ?
                    !LSONTokenUtils.isWhitespace(current)
                    : !LSONTokenUtils.isClosingString(stringOpener, current));

            // Pop string closer off reader.
            if (stringOpener != NULL_BYTE) {
                this.reader.readNext();
            }

            // Remove any whitespace and get character after whitespace.
            removeWhitespace();
            current = this.reader.getCurrent();

            // If we have a string concatenation operator, loop again and keep appending strings.
            if (current == STRING_CONCATENATION_OPERATOR) {
                // Read the concat operator, remove whitespace, and set current character.
                this.reader.readNext();
                removeWhitespace();
                current = this.reader.getCurrent();

                promotedToString = true;
            } else {
                // Break when all loading is done.
                break;
            }
        }

        // Build the word and return.
        return buildWord(capture.toString(), promotedToString || forceString || isObjectKey);
    }

    private LSONWord buildWord (final String string, final boolean promotedToString)
    {
        if (promotedToString) {
            return new LSONString(string);
        }

        for (final LSONTypeAdapter<?> typeAdapter : this.typeAdapters) {
            if (typeAdapter.willAdaptFor(string)) {
                return new LSONWord(string, typeAdapter.buildFromWord(string));
            }
        }

        return new LSONString(string);
    }

    private char captureEscapeCharacter () throws IOException, LSONParseException
    {
        final char escaping = this.reader.readNext();

        // Switch potential escape types.
        switch (escaping) {
            case '0':
                return NULL_BYTE;
            case 'n':
                return NEWLINE;
            case 'r':
                return CARRIAGE_RETURN;
            case 't':
                return TAB;
            case 'u':
            case 'U':
                // Uppercase U indicates six long hex Unicode value.
                // Lowercase u indicates four long hex Unicode value.
                final int hexReadBounds = (escaping == 'u' ? 4 : 6);
                final StringBuilder sb = new StringBuilder();

                for (int i = 0; i < hexReadBounds; ++i) {
                    sb.append(this.reader.readNext());
                }

                // Parse unicode character from base 16 integer.
                return (char) Integer.parseInt(sb.toString(), 16);
            default:
                return escaping;
        }
    }

    private LSONFileLocation getLocation ()
    {
        return new LSONFileLocation(this.reader.getOffset(), this.reader.getLine(), this.reader.getColumn());
    }

    private void expect (final int character, final String onError) throws LSONParseException
    {
        expect((char) character, onError);
    }

    private void expect (final char character, final String onError) throws LSONParseException
    {
        if (this.reader.getCurrent() == character) {
            return;
        }

        throw new LSONParseException(onError, getLocation());
    }

    private void expectAny (final String onError, final int... characters) throws LSONParseException
    {
        final int current = this.reader.getCurrent();

        for (final int character : characters) {
            if (current == character) {
                return;
            }
        }

        throw new LSONParseException(onError, getLocation());
    }

    private void removeWhitespace () throws IOException, LSONParseException
    {
        removeWhitespace(false);
    }

    private void removeWhitespace (final boolean removeEndOfFile) throws IOException, LSONParseException
    {
        char current = this.reader.getCurrent();

        // Keep stripping characters while whitespace or a comment is present.
        // If this whitespace is trimmed off the end of the file, remove end of file character too.
        while ((removeEndOfFile && LSONTokenUtils.isEndOfRead(current))
                || LSONTokenUtils.isWhitespace(current)
                || current == COMMENT_START) {

            // Remove single line or block comment.
            if (current == COMMENT_START) {
                stripComment();
            }

            // If the reader is finished, the file was just a comment. Stop removing whitespace and return.
            if (this.reader.isFinished()) {
                return;
            }

            // Strip whitespace character.
            current = this.reader.readNext();
        }
    }

    private void stripComment () throws IOException, LSONParseException
    {
        // Determines if the comment is a block or single line comment.
        final char commentDeterminant = this.reader.readNext();
        char current = this.reader.getCurrent();

        // Single line comment (keep trimming until end of line).
        if (commentDeterminant == COMMENT_START) {
            while (!LSONTokenUtils.isNewline(current) && !this.reader.isFinished()) {
                current = this.reader.readNext();
            }
        } else if (commentDeterminant == COMMENT_BLOCK_DETERMINANT) {
            // Block comment (trim until block is closed).
            current = this.reader.readNext();

            while (true) {
                boolean peeked = false;

                // Check to see if the comment is being closed.
                // If peeked is set to true, it means we already checked to see if the block was ending, so the post
                // check trim will not need to take place.
                if (current == COMMENT_BLOCK_DETERMINANT) {
                    current = this.reader.readNext();
                    peeked = true;

                    // If the peeked character is the comment start token, the block is closed, so break out.
                    if (current == COMMENT_START) {
                        break;
                    }
                }

                // If the reader was not peeked for block ending, keep trimming.
                if (!peeked) {
                    current = this.reader.readNext();
                }
            }
        } else {
            // Cannot have dangling comment start token.
            throw new LSONParseException("Expected either " + (char) COMMENT_START + " or "
                    + (char) COMMENT_BLOCK_DETERMINANT + " after comment start", getLocation());
        }
    }

    public Collection<LSONTypeAdapter<?>> getTypeAdapters ()
    {
        return this.typeAdapters;
    }

    public LSONReader getReader ()
    {
        return this.reader;
    }
}
