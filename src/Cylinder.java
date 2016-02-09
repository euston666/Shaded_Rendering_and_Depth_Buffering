
/*
 * Class for Cylinder object. Nov 2015 created by Hengbin Liao
 */

import java.util.ArrayList;

public class Cylinder extends Object3D {

	private float rx, ry, umax;
	private int rows, cols;
	private Surface surface;
	private CylinderCap topCap;
	private CylinderCap bottomCap;

	public Cylinder(Point center, float rx, float ry, float umax, Material mat, int rows, int cols) {
		super(center, mat);
		this.rx = rx;
		this.ry = ry;
		this.umax = umax;
		this.rows = rows;
		this.cols = cols;

		surface = new Surface(center, mat, rows, cols);

		Vector topCenter = new Vector(center.x, center.y, (int) (center.z + umax));
		Vector topNormal = new Vector(0, 0, 1);
		topCap = new CylinderCap(topCenter, rx, ry, topNormal, mat, cols);

		Vector bottomCenter = new Vector(center.x, center.y, (int) (center.z - umax));
		Vector bottomNormal = new Vector(0, 0, -1);
		bottomCap = new CylinderCap(bottomCenter, rx, ry, bottomNormal, mat, cols);

		fillMesh();
	}

	/* fill the mesh of the object */
	private void fillMesh() {
		int i, j;
		float u, v;
		float d_u = (float) (2.0 * umax) / ((float) cols - 1);
		float d_v = (float) (2.0 * Math.PI) / ((float) (rows - 1));
		float cosv, sinv;

		Mesh mesh = surface.mesh;
		for (i = 0, v = -(float) Math.PI; i < rows; ++i, v += d_v) {
			cosv = (float) Math.cos(v);
			sinv = (float) Math.sin(v);

			for (j = 0, u = -umax; j < cols; ++j, u += d_u) {
				mesh.vertices[i][j].x = center.x + rx * cosv;
				mesh.vertices[i][j].y = center.y + ry * sinv;
				mesh.vertices[i][j].z = center.z + u;

				mesh.normals[i][j].x = ry * cosv;
				mesh.normals[i][j].y = rx * sinv;
				mesh.normals[i][j].z = 0;
			}
		}
	}

	/* draw the object with flat shading */
	public void drawFlat(ArrayList<Light> lights, Vector view_vector) {
		surface.drawFlat(lights, view_vector);
		topCap.drawFlat(lights, view_vector);
		bottomCap.drawFlat(lights, view_vector);
	}

	/* draw the object with Gouraud shading */
	public void drawGouraud(ArrayList<Light> lights, Vector view_vector) {
		surface.drawGouraud(lights, view_vector);
		topCap.drawGouraud(lights, view_vector);
		bottomCap.drawGouraud(lights, view_vector);
	}

	/* draw the object with Phong shading */
	public void drawPhong(ArrayList<Light> lights, Vector view_vector) {
		surface.drawPhong(lights, view_vector);
		topCap.drawPhong(lights, view_vector);
		bottomCap.drawPhong(lights, view_vector);
	}

	/* draw environment mapping */
	public void drawEnvMapping() {
	}

	/* draw bump mapping */
	public void drawBumpMapping(ArrayList<Light> lights, Vector view_vector) {
	}

	/* toggle the diffuse effect */
	public void toggleDiffuse(boolean isDiffuse) {
		surface.getMat().isDiffuse = isDiffuse;
		topCap.getMat().isDiffuse = isDiffuse;
		bottomCap.getMat().isDiffuse = isDiffuse;
	}

	/* toggle the specular effect */
	public void toggleSpecular(boolean isSpecular) {
		surface.getMat().isSpecular = isSpecular;
		topCap.getMat().isSpecular = isSpecular;
		bottomCap.getMat().isSpecular = isSpecular;
	}

	/* toggle the ambient effect */
	public void toggleAmbient(boolean isAmbient) {
		surface.getMat().isAmbient = isAmbient;
		topCap.getMat().isAmbient = isAmbient;
		bottomCap.getMat().isAmbient = isAmbient;
	}

	/* translate the object */
	public void translate(float x, float y, float z) {

		Matrix vMatrix = new Matrix();
		vMatrix.setMatrix(Matrix.translate(x, y, z));
		Matrix nMatrix = new Matrix();

		surface.mesh.transformMesh(vMatrix, nMatrix);
		topCap.cap.transformFan(vMatrix, nMatrix);
		bottomCap.cap.transformFan(vMatrix, nMatrix);
	}

	/* rotate the object */
	public void rotate(Quaternion q, Vector pivot) {
		topCap.cap.rotate(q, pivot);
		bottomCap.cap.rotate(q, pivot);

		Matrix vertexMatrix = new Matrix();
		Matrix normalMatrix = new Matrix();
		float[] qMatrix = new Matrix(q.toMatrix()).getTranspose();

		float[] transIn = Matrix.translate(-pivot.x, -pivot.y, -pivot.z);
		float[] transOut = Matrix.translate(pivot.x, pivot.y, pivot.z);

		vertexMatrix.multiplyMatrix(transOut);
		vertexMatrix.multiplyMatrix(qMatrix);
		vertexMatrix.multiplyMatrix(transIn);

		normalMatrix.multiplyMatrix(qMatrix);

		Vector centerVector = new Vector(this.center.x, this.center.y, this.center.z);
		centerVector = vertexMatrix.multiplyPoint(centerVector);
		surface.mesh.transformMesh(vertexMatrix, normalMatrix);

		this.center.x = Math.round(centerVector.x);
		this.center.y = Math.round(centerVector.y);
		this.center.z = Math.round(centerVector.z);

		surface.center = center;
	}

	/* scale the object */
	public void scale(float factor) {
		float[] transIn = Matrix.translate(-this.center.x, -this.center.y, -this.center.z);
		float[] transOut = Matrix.translate(this.center.x, this.center.y, this.center.z);
		float[] sMatrix = new Matrix().multiplyMatrix(Matrix.scale(factor, factor, factor));

		Matrix vMatrix = new Matrix();
		vMatrix.multiplyMatrix(transOut);
		vMatrix.multiplyMatrix(sMatrix);
		vMatrix.multiplyMatrix(transIn);

		Matrix nMatrix = new Matrix();
		surface.mesh.transformMesh(vMatrix, nMatrix);
		topCap.cap.transformFan(vMatrix, nMatrix);
		bottomCap.cap.transformFan(vMatrix, nMatrix);
	}

	/* get the name of the object */
	public String getName() {
		return "cylinder";
	}

}
