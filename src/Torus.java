
// ****************************************************************************
// Torus class
// ****************************************************************************
// History :
// Nov 9, 2014 Created by Stan Sclaroff
// Nov 2015 modified by Hengbin Liao

import java.util.ArrayList;

public class Torus extends Object3D {

	private float r, r_axial;
	private int rows, cols;
	private Surface surface;

	public Torus(Point center, float r, float r_axial, Material mat, int rows, int cols) {
		super(center, mat);
		this.r = r;
		this.r_axial = r_axial;
		this.rows = rows;
		this.cols = cols;
		surface = new Surface(center, mat, rows, cols);
		fillMesh();
	}

	/* fill the mesh of the object */
	private void fillMesh() {
		int i, j;
		float theta, alpha;
		float d_theta = (float) (2.0 * Math.PI) / ((float) rows - 1);
		float d_alpha = (float) (2.0 * Math.PI) / ((float) cols - 1);
		float cos_theta, sin_theta;
		float cos_alpha, sin_alpha;
		Vector du = new Vector();
		Vector dv = new Vector();

		Mesh mesh = surface.mesh;
		for (i = 0, theta = (float) -Math.PI; i < rows; ++i, theta += d_theta) {
			cos_theta = (float) Math.cos(theta);
			sin_theta = (float) Math.sin(theta);

			for (j = 0, alpha = (float) -Math.PI; j < cols; ++j, alpha += d_alpha) {
				// follow the formulation for torus given in textbook
				cos_alpha = (float) Math.cos(alpha);
				sin_alpha = (float) Math.sin(alpha);
				mesh.vertices[i][j].x = center.x + (r_axial + r * cos_alpha) * cos_theta;
				mesh.vertices[i][j].y = center.y + (r_axial + r * cos_alpha) * sin_theta;
				mesh.vertices[i][j].z = center.z + r * sin_alpha;

				// compute partial derivatives
				// then use cross-product to get the normal
				// and normalize to produce a unit vector for the normal
				du.x = -(r_axial + r * cos_alpha) * sin_theta;
				du.y = (r_axial + r * cos_alpha) * cos_theta;
				du.z = 0;

				dv.x = -r * sin_alpha * cos_theta;
				dv.y = -r * sin_alpha * sin_theta;
				dv.z = r * cos_alpha;

				du.crossProduct(dv, surface.mesh.normals[i][j]);
				mesh.normals[i][j].normalize();
			}
		}
	}

	public void drawFlat(ArrayList<Light> lightSources, Vector viewVec) {
		surface.drawFlat(lightSources, viewVec);
	}

	public void drawGouraud(ArrayList<Light> lightSources, Vector viewVec) {
		surface.drawGouraud(lightSources, viewVec);
	}

	public void drawPhong(ArrayList<Light> lightSources, Vector viewVec) {
		surface.drawPhong(lightSources, viewVec);
	}

	/* draw environment mapping */
	public void drawEnvMapping() {
		// surface.drawEnvMapping();
	}

	/* draw bump mapping */
	public void drawBumpMapping(ArrayList<Light> lights, Vector view_vector) {
	}

	/* toggle the diffuse effect */
	public void toggleDiffuse(boolean isDiffuse) {
		surface.getMat().isDiffuse = isDiffuse;
	}

	/* toggle the specular effect */
	public void toggleSpecular(boolean isSpecular) {
		surface.getMat().isSpecular = isSpecular;
	}

	/* toggle the ambient effect */
	public void toggleAmbient(boolean isAmbient) {
		surface.getMat().isAmbient = isAmbient;
	}

	/* translate the object */
	public void translate(float x, float y, float z) {

		Matrix vMatrix = new Matrix();
		vMatrix.setMatrix(Matrix.translate(x, y, z));
		Matrix nMatrix = new Matrix();

		surface.mesh.transformMesh(vMatrix, nMatrix);
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
	}

	/* rotate the object */
	public void rotate(Quaternion q, Vector pivot) {
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

	/* get the name of the object */
	public String getName() {
		return "torus";
	}
}
