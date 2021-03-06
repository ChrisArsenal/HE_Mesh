/*
 * This file is part of HE_Mesh, a library for creating and manipulating meshes.
 * It is dedicated to the public domain. To the extent possible under law,
 * I , Frederik Vanhoutte, have waived all copyright and related or neighboring
 * rights.
 * 
 * This work is published from Belgium. (http://creativecommons.org/publicdomain/zero/1.0/)
 * 
 */
package wblut.math;

public class WB_Horner {
    
    /**
     * 
     *
     * @param a 
     * @param u 
     * @return 
     */
    public static double Horner(final double[] a, final double u) {
	final int n = a.length - 1;
	double result = a[n];
	for (int i = n - 1; i >= 0; i--) {
	    result = result * u + a[i];
	}
	return result;
    }
}
