/*
 * Copyright (C) 2017 LSON4J - All Rights Reserved
 *
 * Unauthorized copying of this file, via any median is strictly prohibited
 * proprietary and confidential. For more information, please contact me at
 * connor@hollasch.net
 *
 * Written by Connor Hollasch <connor@hollasch.net>, December 2017
 */

package net.hollasch.lson4j.type;

/**
 * @author Connor Hollasch
 * @since Dec 09, 1:08 AM
 */
public interface LSONTypeAdapter<T>
{
    T buildFromString(final String value);
}
