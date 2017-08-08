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

import wblut.geom.WB_Plane;

/**
 * Planar cut of a mesh. Both parts are returned as separate meshes.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEMC_SplitMesh extends HEMC_MultiCreator {
	/** Cutting plane. */
	private WB_Plane P;
	/** Source mesh. */
	private HE_Mesh mesh;
	/** Cap holes?. */
	private boolean cap = true;;
	/** The offset. */
	private double offset;
	private boolean simpleCap = true;

	/**
	 * Set offset.
	 *
	 * @param d
	 *            offset
	 * @return self
	 */
	public HEMC_SplitMesh setOffset(final double d) {
		offset = d;
		return this;
	}

	/**
	 * Instantiates a new HEMC_SplitMesh.
	 *
	 */
	public HEMC_SplitMesh() {
		super();
	}

	/**
	 * Set split plane.
	 *
	 * @param P
	 *            plane
	 * @return self
	 */
	public HEMC_SplitMesh setPlane(final WB_Plane P) {
		this.P = P;
		return this;
	}

	/**
	 * Set source mesh.
	 *
	 * @param mesh
	 *            mesh to split
	 * @return self
	 */
	public HEMC_SplitMesh setMesh(final HE_Mesh mesh) {
		this.mesh = mesh;
		return this;
	}

	/**
	 * Set option to cap holes.
	 *
	 * @param b
	 *            true, false;
	 * @return self
	 */
	public HEMC_SplitMesh setCap(final Boolean b) {
		cap = b;
		return this;
	}

	public HEMC_SplitMesh setSimpleCap(final Boolean b) {
		simpleCap = b;
		return this;
	}

	@Override
	void create(final HE_MeshCollection result) {

		if (mesh == null) {
			_numberOfMeshes = 0;
			return;
		}
		if (P == null) {
			result.add(mesh.copy());
			_numberOfMeshes = 1;
			return;
		}
		final HEM_Slice sm = new HEM_Slice();
		HE_Mesh tmp = mesh.copy();
		sm.setPlane(P).setReverse(false).setCap(cap).setOffset(offset).setSimpleCap(simpleCap);
		sm.applyInt(tmp);
		tmp.resetFaceInternalLabels();
		HE_FaceIterator fItr = sm.capFaces.fItr();
		while (fItr.hasNext()) {
			fItr.next().setInternalLabel(1);
		}
		result.add(tmp);
		P.flipNormal();
		sm.setPlane(P).setReverse(false).setCap(cap).setOffset(offset).setSimpleCap(simpleCap);
		tmp = mesh.copy();
		sm.applyInt(tmp);
		tmp.resetFaceInternalLabels();
		fItr = sm.capFaces.fItr();
		while (fItr.hasNext()) {
			fItr.next().setInternalLabel(1);
		}
		result.add(tmp);
		_numberOfMeshes = 2;

	}
}
