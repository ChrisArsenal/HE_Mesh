/*
 * This file is part of HE_Mesh, a library for creating and manipulating meshes.
 * It is dedicated to the public domain. To the extent possible under law,
 * I , Frederik Vanhoutte, have waived all copyright and related or neighboring
 * rights.
 *
 * This work is published from Belgium. (http://creativecommons.org/publicdomain/zero/1.0/)
 *
 */

package wblut.geom;

/**
 * Interface for implementing metric operations on 4D coordinates.
 *
 * None of the operators change the calling object.
 *
 * @author Frederik Vanhoutte
 *
 */
public interface WB_CoordinateMetric4D {

	/**
	 * Length of 4D coordinate.
	 *
	 * @return
	 */
	public double getLength4D();

	/**
	 * Square length of 4D coordinate.
	 *
	 * @return
	 */
	public double getSqLength4D();

	/**
	 * 4D distance to coordinate
	 *
	 * @param p
	 * @return
	 */
	public double getDistance4D(final WB_Coord p);

	/**
	 * Square 4D distance to coordinate.
	 *
	 * @param p
	 * @return
	 */
	public double getSqDistance4D(final WB_Coord p);

	/**
	 * Is this a degenerate vector?
	 *
	 * @return
	 */
	public boolean isZero();

}
