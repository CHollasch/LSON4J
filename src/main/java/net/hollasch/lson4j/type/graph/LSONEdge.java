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
 * @since Jul 28, 11:29 PM
 */
public class LSONEdge
{
    private LSONVertex v1, v2;
    private boolean directed;

    private LSONValue data;

    public LSONEdge (
            final LSONVertex v1,
            final LSONVertex v2,
            final boolean directed,
            final LSONValue data)
    {
        this.v1 = v1;
        this.v2 = v2;
        this.directed = directed;
        this.data = data;
    }

    public LSONVertex getV1 ()
    {
        return this.v1;
    }

    public LSONVertex getV2 ()
    {
        return this.v2;
    }

    public boolean isDirected ()
    {
        return this.directed;
    }

    @Override
    public boolean equals (final Object obj)
    {
        if (obj == null || !(obj instanceof LSONEdge)) {
            return false;
        }

        final LSONEdge o = (LSONEdge) obj;

        return o.v1.equals(this.v1) && o.v2.equals(this.v2) && o.directed == this.directed;
    }

    @Override
    public int hashCode ()
    {
        return (31 * this.v1.hashCode()) + (31 * this.v2.hashCode())
                + (this.data != null ? (31 * this.data.hashCode()) : -1);
    }

    @Override
    public String toString ()
    {
        return "<" + this.v1.toString()
                + (isDirected() ? "->" : "--")
                + this.v2.toString()
                + (this.data == null ? ">" : " (" + this.data.toString() + ")>");
    }
}
