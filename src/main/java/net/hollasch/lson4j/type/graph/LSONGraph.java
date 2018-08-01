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

import java.util.ArrayList;

/**
 * @author Connor Hollasch
 * @since Jul 28, 12:26 AM
 */
public class LSONGraph extends LSONValue
{
    private ArrayList<LSONVertex> vertices;
    private ArrayList<LSONEdge> edges;

    public LSONGraph (final ArrayList<LSONVertex> vertices, final ArrayList<LSONEdge> edges)
    {
        this.vertices = vertices;
        this.edges = edges;
    }

    public ArrayList<LSONVertex> getVertices ()
    {
        return this.vertices;
    }

    public ArrayList<LSONEdge> getEdges ()
    {
        return this.edges;
    }

    @Override
    public boolean isGraph ()
    {
        return true;
    }

    @Override
    public boolean equals (final Object obj)
    {
        if (obj == null || !(obj instanceof LSONGraph)) {
            return false;
        }

        final LSONGraph o = (LSONGraph) obj;

        return o.vertices.equals(this.vertices) && o.edges.equals(this.edges);
    }

    @Override
    public int hashCode ()
    {
        return (31 * this.vertices.hashCode()) + (31 * this.edges.hashCode());
    }

    @Override
    public String toString ()
    {
        return "[%" + this.vertices.toString() + ", " + this.edges.toString() + "%]";
    }
}
