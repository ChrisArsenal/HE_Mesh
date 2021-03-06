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

import wblut.math.WB_ScalarParameter;

/**
 * @author FVH
 *
 */
public interface WB_IsoValues2D {
	public double value(int i, int j);

	public class Grid2D implements WB_IsoValues2D {
		private double[][] values;

		public Grid2D(final double[][] values) {
			this.values = new double[values.length][values.length > 0 ? values[0].length : 0];
			for (int i = 0; i < values.length; i++) {
				for (int j = 0; j < values[0].length; j++) {
					this.values[i][j] = values[i][j];
				}
			}
		}

		public Grid2D(final float[][] values) {
			this.values = new double[values.length][values.length > 0 ? values[0].length : 0];
			for (int i = 0; i < values.length; i++) {
				for (int j = 0; j < values[0].length; j++) {
					this.values[i][j] = values[i][j];
				}
			}
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see wblut.geom.WB_IsoValues2D#value(int, int, int)
		 */
		@Override
		public double value(final int i, final int j) {
			// TODO Auto-generated method stub
			return values[i][j];
		}

	}

	public class GridRaw2D implements WB_IsoValues2D {
		private double[][] values;

		public GridRaw2D(final double[][] values) {
			this.values = values;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see wblut.geom.WB_IsoValues2D#value(int, int, int)
		 */
		@Override
		public double value(final int i, final int j) {
			// TODO Auto-generated method stub
			return values[i][j];
		}

	}

	public class Function2D implements WB_IsoValues2D {
		private double fxi, fyi, dfx, dfy;
		private WB_ScalarParameter function;

		public Function2D(final WB_ScalarParameter function, final double xi, final double yi, final double dx,
				final double dy) {
			this.function = function;
			fxi = xi;
			fyi = yi;
			dfx = dx;
			dfy = dy;
			this.function = function;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see wblut.geom.WB_IsoValues2D#value(int, int)
		 */
		@Override
		public double value(final int i, final int j) {
			return function.evaluate(fxi + i * dfx, fyi + j * dfy);
		}
	}

	public class HashGrid2D implements WB_IsoValues2D {
		private WB_HashGridDouble2D values;

		public HashGrid2D(final WB_HashGridDouble2D values) {
			this.values = values;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see wblut.geom.WB_IsoValues2D#value(int, int, int)
		 */
		@Override
		public double value(final int i, final int j) {
			return values.getValue(i, j);
		}

	}

}
