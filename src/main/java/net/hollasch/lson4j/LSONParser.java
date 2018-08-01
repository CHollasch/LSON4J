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
import net.hollasch.lson4j.type.graph.LSONEdge;
import net.hollasch.lson4j.type.graph.LSONGraph;
import net.hollasch.lson4j.type.graph.LSONVertex;
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
    // Similar to java reader, optimized to buffer reads. Reading from input reader.
    private LSONReader reader;

    // Collection of scalar type adapters being used to parse words.
    private Collection<LSONTypeAdapter<?>> typeAdapters;

    /**
     * Creates an LSON parser given the following parameters.
     *
     * @param reader       {@link LSONReader} that will be used to read the input LSON string.
     * @param typeAdapters {@link Collection} of scalar type adapters being used to parse words.
     * @throws IOException        if the reader has a problem with IO.
     * @throws LSONParseException if there is a syntax error while parsing the input LSON string.
     */
    public LSONParser (
            final LSONReader reader,
            final Collection<LSONTypeAdapter<?>> typeAdapters)
            throws IOException, LSONParseException
    {
        this.reader = reader;
        this.typeAdapters = typeAdapters;

        // Begin buffering.
        this.reader.prepare();

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
        return readValue(false);
    }

    private LSONValue readValue (final boolean flattenArrays) throws IOException, LSONParseException
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
                final char determinant = this.reader.readNext();

                // Parse table vs array.
                if (determinant == LSON_TABLE_STARTER) {
                    return readTable();
                } else if (determinant == LSON_GRAPH_STARTER) {
                    return readGraph();
                } else {
                    return readArray(flattenArrays);
                }

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

    private LSONObject<LSONValue> readObject () throws IOException, LSONParseException
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
            return new LSONObject<>(lsonObj);
        }

        // Keep track of the floating character (character after previous read operation) so we know when to stop
        // reading values into the object.
        char floating;
        do {
            Collection<LSONString> keys;
            if (this.reader.getCurrent() == LSON_ARRAY_OPENER) {
                this.reader.readNext();
                keys = new ArrayList<>();

                for (final LSONValue key : readArray(true)) {
                    if (key.isLSONString()) {
                        keys.add(key.toLsonString());
                    }
                }
            } else {
                // Create the key word.
                final LSONString key = (LSONString) readWord(true, true);
                keys = Collections.singleton(key);
            }
            removeWhitespace();

            final LSONValue value;

            // Pop key value separator token.
            expect(KEY_VALUE_SEPARATOR, "Expected a " + (char) KEY_VALUE_SEPARATOR
                    + " to separate key, value pairs, got " + this.reader.getCurrent());
            this.reader.readNext();

            if (keys.isEmpty()) {
                throw new LSONParseException("Cannot create LSON object where a key is null", getLocation());
            }

            // Remove all whitespace and parse value.
            removeWhitespace();
            value = readValue();
            removeWhitespace();

            // Store key(s) value pair in lson object.
            for (final LSONString key : keys) {
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

        return new LSONObject<>(lsonObj);
    }

    private LSONArray<LSONValue> readArray (final boolean flatten) throws IOException, LSONParseException
    {
        final ArrayList<LSONValue> array = new ArrayList<>();
        removeWhitespace();

        if (this.reader.getCurrent() == LSON_ARRAY_CLOSER) {
            // Pop closing tag and return empty object.
            this.reader.readNext();
            return new LSONArray<>(array);
        }

        char floating;
        do {
            // Keep reading values into the array.
            final LSONValue value = readValue(true);

            if (flatten && value != null && value.isLSONArray()) {
                for (final LSONValue v : ((LSONArray<LSONValue>) value)) {
                    array.add(v);
                }
            } else {
                array.add(value);
            }

            removeWhitespace();

            // Read values until there is an array closing tag.
            floating = this.reader.getCurrent();
        } while (floating != LSON_ARRAY_CLOSER);

        // Expect an array closing tag for termination. This line is just a syntax reminder as the loop will never
        // terminate if an array closing tag is not present either way.
        expect(LSON_ARRAY_CLOSER, "Expected " + (char) LSON_ARRAY_CLOSER + " for array terminator.");
        this.reader.readNext();

        return new LSONArray<>(array);
    }

    private LSONTable readTable () throws IOException, LSONParseException
    {
        // Remove the hash tag table opener.
        this.reader.readNext();
        removeWhitespace();

        final ArrayList<LSONWord> header = new ArrayList<>();
        boolean headerHasArrayOpener = false;

        if (this.reader.getCurrent() == LSON_ARRAY_OPENER) {
            this.reader.readNext();
            headerHasArrayOpener = true;
            removeWhitespace();
        }

        char floating;
        do {
            header.add(readWord(true, true));
            removeWhitespace();

            // Read values until there is an array closing tag if array opened header, or colon.
            floating = this.reader.getCurrent();
        } while (headerHasArrayOpener ? floating != LSON_ARRAY_CLOSER : floating != LSON_TABLE_HEADER_END);

        if (headerHasArrayOpener) {
            this.reader.readNext();
            removeWhitespace();
        }

        expect(LSON_TABLE_HEADER_END, "Expected a table header row separator, got " + this.reader.getCurrent());
        this.reader.readNext();
        removeWhitespace();

        final ArrayList<ArrayList<LSONValue>> rowData = new ArrayList<>();

        do {
            if (this.reader.getCurrent() == LSON_TABLE_STARTER && this.reader.peekNext() == LSON_ARRAY_CLOSER) {
                this.reader.readNext();
                this.reader.readNext();
                return new LSONTable(header, new ArrayList[0]);
            }

            if (headerHasArrayOpener) {
                expect(LSON_ARRAY_OPENER, "Expected " + LSON_ARRAY_OPENER + ", got " + this.reader.getCurrent());
                this.reader.readNext();
                removeWhitespace();
            }

            final ArrayList<LSONValue> row = new ArrayList<>();
            for (int i = 0; i < header.size(); ++i) {
                row.add(readValue());
                removeWhitespace();
            }
            rowData.add(row);

            if (headerHasArrayOpener) {
                expect(LSON_ARRAY_CLOSER, "Expected " + LSON_ARRAY_CLOSER + ", got " + this.reader.getCurrent());
                this.reader.readNext();
                removeWhitespace();
            }

            floating = this.reader.getCurrent();
        } while (floating != LSON_TABLE_STARTER && this.reader.peekNext() != LSON_ARRAY_CLOSER);

        this.reader.readNext();
        expect(LSON_ARRAY_CLOSER, "Expected table to end with " + LSON_TABLE_STARTER + LSON_ARRAY_CLOSER
                + ", got " + this.reader.getCurrent());
        this.reader.readNext();

        final LSONTable table = new LSONTable(header, rowData.toArray(new ArrayList[rowData.size()]));
        return table;
    }

    private LSONGraph readGraph () throws IOException, LSONParseException
    {
        // Remove percentage from graph opening statement
        this.reader.readNext();
        removeWhitespace();

        LSONValue[] nodesByIndex = null;
        Map<String, LSONValue> nodesByName = null;

        if (this.reader.getCurrent() == LSON_ARRAY_OPENER) {
            // Arrays are special since they have a determinant for tables and graphs, so we have to skip the opener
            // for the read array function to read properly. If not, it would assume there is an array embedded in the
            // array we're trying to read.
            this.reader.readNext();

            final LSONArray<LSONValue> array = readArray(false);
            nodesByIndex = new LSONValue[array.size()];

            for (int i = 0; i < nodesByIndex.length; ++i) {
                nodesByIndex[i] = array.get(i);
            }
        } else if (this.reader.getCurrent() == LSON_OBJECT_OPENER) {
            final LSONObject<LSONValue> map = readObject();
            nodesByName = new LinkedHashMap<>();

            for (final LSONString key : map.keySet()) {
                nodesByName.put(key.getWord(), map.get(key));
            }
        } else {
            final int nodes = readInteger();
            nodesByIndex = new LSONValue[nodes];
        }

        final ArrayList<LSONVertex> vertices = new ArrayList<>();
        final Map<String, Integer> nameToVertexIdMap = new HashMap<>();

        if (nodesByIndex != null) {
            for (int i = 0; i < nodesByIndex.length; ++i) {
                vertices.add(new LSONVertex(i, null, nodesByIndex[i]));
            }
        } else {
            int index = 0;
            for (final Map.Entry<String, LSONValue> entry : nodesByName.entrySet()) {
                vertices.add(new LSONVertex(index, entry.getKey(), entry.getValue()));
                nameToVertexIdMap.put(entry.getKey(), index);
                ++index;
            }
        }

        removeWhitespace();
        final ArrayList<LSONEdge> edges = new ArrayList<>();

        expectAny("Expected either { or [ to open edge data, got " + this.reader.getCurrent(),
                LSON_ARRAY_OPENER, LSON_OBJECT_OPENER);

        boolean nodesWithData = this.reader.getCurrent() == LSON_OBJECT_OPENER;
        this.reader.readNext();
        removeWhitespace();

        do {
            final LSONVertex v1, v2;
            final int direction;

            if (nodesByIndex != null) {
                v1 = vertices.get(readInteger());
                removeWhitespace();

                direction = LSONTokenUtils.getEdgeDirectionality(this.reader.getCurrent());
                this.reader.readNext();
                removeWhitespace();

                v2 = vertices.get(readInteger());
                removeWhitespace();
            } else {
                v1 = vertices.get(nameToVertexIdMap.get(readWord(true, true).getWord()));
                removeWhitespace();

                direction = LSONTokenUtils.getEdgeDirectionality(this.reader.getCurrent());
                this.reader.readNext();
                removeWhitespace();

                v2 = vertices.get(nameToVertexIdMap.get(readWord(true, true).getWord()));
                removeWhitespace();
            }

            final LSONValue data;
            if (nodesWithData) {
                expect(KEY_VALUE_SEPARATOR, "Expected a key value separator :, got " + this.reader.getCurrent());

                this.reader.readNext();
                removeWhitespace();

                data = readValue();
                removeWhitespace();
            } else {
                data = null;
            }

            final LSONEdge edge;
            switch (direction) {
                case GRAPH_UNDIRECTED:
                    edge = new LSONEdge(v1, v2, false, data);
                    break;
                case GRAPH_RIGHT_DIRECTED:
                    edge = new LSONEdge(v1, v2, true, data);
                    break;
                default:
                    edge = new LSONEdge(v2, v1, true, data);
                    break;
            }

            edges.add(edge);
            removeWhitespace();
        } while (nodesWithData ?
                this.reader.getCurrent() != LSON_OBJECT_CLOSER
                : this.reader.getCurrent() != LSON_ARRAY_CLOSER);
        this.reader.readNext();

        removeWhitespace();
        expect(LSON_GRAPH_STARTER, "Expected a " + (char) LSON_GRAPH_STARTER + " to close a graph, got "
                + this.reader.getCurrent());

        this.reader.readNext();
        this.reader.readNext();

        return new LSONGraph(vertices, edges);
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
                    return buildWord(capture.toString(), promotedToString
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

                    // If this is a key and forced string and we catch graph reserved tokens, break out.
                    if ((isObjectKey && forceString) && LSONTokenUtils.getEdgeDirectionality(current) != -1) {
                        break;
                    }

                    // If we are closing something, break out of parsing this word.
                    else if (LSONTokenUtils.isLSONClosingReservedToken(current)) {
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

    private int readInteger () throws IOException, LSONParseException
    {
        int number = 0;

        do {
            expectAny("Expected a digit but got " + this.reader.getCurrent(), NUMBERS);
            number = (10 * number) + (this.reader.getCurrent() - '0');
            this.reader.readNext();
        } while (isNumber(this.reader.getCurrent()));

        return number;
    }

    @SuppressWarnings("unchecked")
    private LSONWord buildWord (final String string, final boolean promotedToString)
    {
        // Can't build a word from a forced string.
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
        return new LSONFileLocation(this.reader.getLine(), this.reader.getColumn());
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
