/*
 * Copyright (C) 2018 LSON4J - All Rights Reserved
 *
 * Unauthorized copying of this file, via any median is strictly prohibited
 * proprietary and confidential. For more information, please contact me at
 * connor@hollasch.net
 *
 * Written by Connor Hollasch <connor@hollasch.net>, July 2018
 */

package net.hollasch.lson4j.type;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Connor Hollasch
 * @since Jul 25, 11:08 PM
 */
public class LSONTable extends LSONValue
{
    private ArrayList<LSONWord> header;
    private ArrayList<LSONValue>[] rowData;

    public LSONTable (final ArrayList<LSONWord> header, final ArrayList<LSONValue>[] rowData)
    {
        this.header = header;
        this.rowData = rowData;
    }

    public ArrayList<LSONWord> getHeader ()
    {
         return this.header;
    }

    public ArrayList<LSONValue>[] getRowData ()
    {
         return this.rowData;
    }

    public ArrayList<LSONValue> getRow (final int rowNumber)
    {
        return this.rowData[rowNumber];
    }

    public ArrayList<LSONValue> getColumn (final String headerKey)
    {
        for (int i = 0; i < this.header.size(); ++i) {
            if (this.header.get(i).equals(headerKey)) {
                return getColumn(i);
            }
        }

        return null;
    }

    public ArrayList<LSONValue> getColumn (final int colNumber)
    {
        if (colNumber < 0 || colNumber > this.rowData.length) {
            throw new IndexOutOfBoundsException();
        }

        final ArrayList<LSONValue> col = new ArrayList<>();

        for (int i = 0; i < this.rowData.length; ++i) {
            col.add(this.rowData[i].get(colNumber));
        }

        return col;
    }

    @Override
    public boolean isTable ()
    {
        return true;
    }

    @Override
    public String toString ()
    {
        return "[#" + this.header.toString() + ": " + Arrays.toString(rowData) + "#]";
    }
}
