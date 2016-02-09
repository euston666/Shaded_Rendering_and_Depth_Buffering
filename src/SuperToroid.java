
/*
 * Class for super toroid. Nov 2015 created by Hengbin Liao
 */

import java.util.ArrayList;

public class SuperToroid extends Object3D {

	private Surface surface;
	private float r;
	private float r_axial;
	private int rows, cols;
	private float scale1 = 4.0f, scale2 = 4.0f;

	public SuperToroid(Point center, float r, float r_axial, Material mat, int rows, int cols) {
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
		float d_theta = (float) (2.0 * Math.PI) / ((float) (rows - 1));
		float d_alpha = (float) (2.0 * Math.PI) / ((float) (cols - 1));
		float cos_theta, sin_theta;
		float cos_alpha, sin_alpha;
		Mesh mesh = surface.mesh;

		Vector du = new Vector();
		Vector dv = new Vector();

		for (i = 0, theta = (float) -Math.PI; i < rows; ++i, theta += d_theta) {
			cos_theta = (float) Math.cos(theta);
			sin_theta = (float) Math.sin(theta);

			for (j = 0, alpha = (float) -(Math.PI); j < cols; ++j, alpha += d_alpha) {

				cos_alpha = (float) Math.cos(alpha);
				sin_alpha = (float) Math.sin(alpha);

				// vertex location
				mesh.vertices[i][j].x = (float) ((float) center.x + (r_axial + r * cos_alpha * Math.pow(Math.abs(cos_alpha), scale1 - 1)) * cos_theta
						* Math.pow(Math.abs(cos_theta), scale2 - 1));

				mesh.vertices[i][j].y = (float) ((float) center.y + sin_theta * Math.pow(Math.abs(sin_theta), scale2 - 1)
						* (r_axial + r * cos_alpha * Math.pow(Math.abs(cos_alpha), scale1 - 1)));

				mesh.vertices[i][j].z = (float) ((float) center.z + (r_axial * sin_alpha * Math.pow(Math.abs(sin_alpha), scale1 - 1)));

				// compute partial derivatives
				// then use cross-product to get the normal
				// and normalize to produce a unit vector for the normal
				du.x = (float) (-(float) scale1 * r * Math.pow(Math.abs(cos_alpha), scale1 - 1) * sin_alpha
						* Math.pow(Math.abs(cos_theta), scale2 - 1) * cos_theta);
				du.y = (float) (-(float) scale1 * r * Math.pow(Math.abs(cos_alpha), scale1 - 1) * sin_alpha
						* Math.pow(Math.abs(sin_theta), scale2 - 1) * sin_theta);
				du.z = (float) ((float) scale1 * r_axial * Math.pow(Math.abs(sin_alpha), scale1 - 1) * cos_alpha);

				dv.x = -(float) (scale2 * r * cos_alpha * Math.pow(Math.abs(cos_alpha), scale1 - 1) * Math.pow(Math.abs(cos_theta), scale2 - 1)
						* sin_theta + scale2 * r_axial * Math.pow(Math.abs(cos_theta), scale2 - 1) * sin_theta);
				dv.y = (float) (scale2 * r * cos_alpha * Math.pow(Math.abs(cos_alpha), scale1 - 1) * Math.pow(Math.abs(sin_theta), scale2 - 1)
						* cos_theta + scale2 * r_axial * Math.pow(Math.abs(sin_theta), scale2 - 1) * cos_theta);
				dv.z = 0;

				dv.crossProduct(du, mesh.normals[i][j]);
				mesh.normals[i][j].normalize();
			}
		}

	}

	/* draw the object with flat shading */
	public void drawFlat(ArrayList<Light> lights, Vector view_vector) {
		surface.drawFlat(lights, view_vector);
	}

	/* draw the object with Gouraud shading */
	public void drawGouraud(ArrayList<Light> lights, Vector view_vector) {
		surface.drawGouraud(lights, view_vector);
	}

	/* draw the object with Phong shading */
	public void drawPhong(ArrayList<Light> lights, Vector view_vector) {
		surface.drawPhong(lights, view_vector);
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
	}

	/* toggle the specular effect */
	public void toggleSpecular(boolean isSpecular) {
		surface.getMat().isSpecular = isSpecular;
	}

	/* toggle the ambient effect */
	public void toggleAmbient(boolean isAmbient) {
		surface.getMat().isAmbient = isAmbient;
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

	/* get the name of the object */
	public String getName() {
		return "super toroid";
	}

}
