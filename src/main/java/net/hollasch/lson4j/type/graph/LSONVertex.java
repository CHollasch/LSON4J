/*
 * Copyright (C) 2018 LSON4J - All Rights Reserved
 *
 * Unauthorized copying of this file, via any median is strictly prohibited
 * proprietary and confidential. For more information, please contact me at
 * connor@hollasch.net
 *
 * Written by Connor Hollasch <connor@hollasch.net>, July 2018
 */

package net.hollasch.lson4j.type.graph;

import net.hollasch.lson4j.type.LSONValue;

/**
 * @author Connor Hollasch
 * @since Jul 28, 11:26 PM
 */
public class LSONVertex
{
    private int index;
    private String name;
    private LSONValue vertexValue;

    public LSONVertex (final int index, final String name, final LSONValue vertexValue)
    {
        this.index = index;
        this.name = name;
        this.vertexValue = vertexValue;
    }

    public int getIndex ()
    {
        return this.index;
    }

    public String getName ()
    {
        return this.name;
    }

    public LSONValue getVertexValue ()
    {
        return this.vertexValue;
    }

    @Override
    public boolean equals (final Object obj)
    {
        if (obj == null || !(obj instanceof LSONVertex)) {
            return false;
        }

        final LSONVertex o = (LSONVertex) obj;

        return o.index == this.index && o.vertexValue.equals(this.vertexValue);
    }

    @Override
    public int hashCode ()
    {
        return (31 * index) + (this.vertexValue != null ? (31 * this.vertexValue.hashCode()) : -1);
    }

    @Override
    public String toString ()
    {
        return "(" + (this.name != null ? this.name : this.index)
                + (this.vertexValue != null ? ". " + this.vertexValue.toString() + ")" : ")");
    }
}
