/*
 * This file is part of HE_Mesh, a library for creating and manipulating meshes.
 * It is dedicated to the public domain. To the extent possible under law,
 * I , Frederik Vanhoutte, have waived all copyright and related or neighboring
 * rights.
 *
 * This work is published from Belgium. (http://creativecommons.org/publicdomain/zero/1.0/)
 *
 */
package wblut.hemesh;

import java.util.Iterator;
import java.util.List;

import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import wblut.geom.WB_AABB;
import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryOp3D;
import wblut.geom.WB_Point;
import wblut.math.WB_Epsilon;

/**
 *
 */
public class HEM_Soapfilm extends HEM_Modifier {

	/**
	 *
	 */
	private boolean autoRescale;

	/**
	 *
	 */
	private int iter;

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.modifiers.HEB_Modifier#modify(wblut.hemesh.HE_Mesh)
	 */
	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEM_Soapfilm setAutoRescale(final boolean b) {
		autoRescale = b;
		return this;
	}

	/**
	 *
	 *
	 * @param r
	 * @return
	 */
	public HEM_Soapfilm setIterations(final int r) {
		iter = r;
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HEM_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		mesh.triangulate();
		WB_AABB box = new WB_AABB();
		if (autoRescale) {
			box = mesh.getAABB();
		}
		final TLongObjectMap<WB_Coord> newPositions = new TLongObjectHashMap<WB_Coord>(10, 0.5f, 0L);
		if (iter < 1) {
			iter = 1;
		}
		for (int r = 0; r < iter; r++) {
			Iterator<HE_Vertex> vItr = mesh.vItr();
			HE_Vertex v;
			final HE_Selection sel = HE_Selection.selectAllFaces(mesh);
			final List<HE_Vertex> outer = sel.getOuterVertices();
			while (vItr.hasNext()) {
				v = vItr.next();
				if (outer.contains(v)) {
					newPositions.put(v.getKey(), v);
				} else {
					newPositions.put(v.getKey(), minDirichletEnergy(v));
				}
			}
			vItr = mesh.vItr();
			while (vItr.hasNext()) {
				v = vItr.next();
				v.set(newPositions.get(v.getKey()));
			}
		}
		if (autoRescale) {
			mesh.fitInAABB(box);
		}
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * wblut.hemesh.modifiers.HEB_Modifier#modifySelected(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		selection.collectVertices();
		selection.parent.triangulate();
		WB_AABB box = new WB_AABB();
		if (autoRescale) {
			box = selection.parent.getAABB();
		}
		final TLongObjectMap<WB_Coord> newPositions = new TLongObjectHashMap<WB_Coord>(10, 0.5f, 0L);
		if (iter < 1) {
			iter = 1;
		}
		for (int r = 0; r < iter; r++) {
			Iterator<HE_Vertex> vItr = selection.vItr();
			HE_Vertex v;
			final HE_Selection sel = HE_Selection.selectAllFaces(selection.parent);
			final List<HE_Vertex> outer = sel.getOuterVertices();
			while (vItr.hasNext()) {
				v = vItr.next();
				if (outer.contains(v)) {
					newPositions.put(v.getKey(), v);
				} else {
					newPositions.put(v.getKey(), minDirichletEnergy(v));
				}
			}
			vItr = selection.vItr();
			while (vItr.hasNext()) {
				v = vItr.next();
				v.set(newPositions.get(v.getKey()));
			}
		}
		if (autoRescale) {
			selection.parent.fitInAABB(box);
		}
		return selection.parent;
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	private WB_Point minDirichletEnergy(final HE_Vertex v) {
		final WB_Point result = new WB_Point();
		final List<HE_Halfedge> hes = v.getHalfedgeStar();
		HE_Vertex neighbor;
		HE_Vertex corner;
		HE_Halfedge he;
		double cota;
		double cotb;
		double cotsum;
		double weight = 0;
		for (int i = 0; i < hes.size(); i++) {
			cotsum = 0;
			he = hes.get(i);
			neighbor = he.getEndVertex();
			{
				corner = he.getPrevInFace().getVertex();
				cota = WB_GeometryOp3D.getCosAngleBetween(corner.xd(), corner.yd(), corner.zd(), neighbor.xd(),
						neighbor.yd(), neighbor.zd(), v.xd(), v.yd(), v.zd());
				cotsum += cota / Math.sqrt(1 - cota * cota);
				corner = he.getPair().getPrevInFace().getVertex();
				cotb = WB_GeometryOp3D.getCosAngleBetween(corner.xd(), corner.yd(), corner.zd(), neighbor.xd(),
						neighbor.yd(), neighbor.zd(), v.xd(), v.yd(), v.zd());
				cotsum += cotb / Math.sqrt(1 - cotb * cotb);
			}
			result.addMulSelf(cotsum, neighbor);
			weight += cotsum;
		}
		if (!WB_Epsilon.isZero(weight)) {
			result.divSelf(weight);
		}
		return result;
	}
}
