
/*
 * Class for super ellipsoid. Nov 2015 created by Hengbin Liao
 */

import java.util.ArrayList;

public class SuperEllipsoid extends Object3D {

	private float rx;
	private float ry;
	private float rz;
	private int rows, cols;
	private Surface surface;
	private float e1 = 0.6f, e2 = 0.6f;

	public SuperEllipsoid(Point center, float rx, float ry, float rz, Material mat, int rows, int cols) {
		super(center, mat);
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
		this.rows = rows;
		this.cols = cols;
		surface = new Surface(center, mat, rows, cols);

		fillMesh();
	}

	/* fill the mesh of the object */
	public void fillMesh() {

		int i, j;
		float theta, alpha;
		float d_theta = (float) (2.0 * Math.PI) / ((float) (rows - 1));
		float d_alpha = (float) Math.PI / ((float) (cols - 1));
		float cos_theta, sin_theta;
		float cos_alpha, sin_alpha;
		Mesh mesh = surface.mesh;

		for (i = 0, theta = -(float) Math.PI; i < rows; ++i, theta += d_theta) {
			cos_theta = (float) Math.cos(theta);
			sin_theta = (float) Math.sin(theta);

			for (j = 0, alpha = (float) (-0.5 * Math.PI); j < cols; ++j, alpha += d_alpha) {

				cos_alpha = (float) Math.cos(alpha);
				sin_alpha = (float) Math.sin(alpha);

				// vertex location
				mesh.vertices[i][j].x = (float) ((float) center.x
						+ (rx * cos_alpha * Math.pow(Math.abs(cos_alpha), e1 - 1) * cos_theta * Math.pow(Math.abs(cos_theta), e2 - 1)));

				mesh.vertices[i][j].y = (float) ((float) center.y
						+ (ry * cos_alpha * Math.pow(Math.abs(cos_alpha), e1 - 1) * sin_theta * Math.pow(Math.abs(sin_theta), e2 - 1)));

				mesh.vertices[i][j].z = (float) ((float) center.z + (rz * sin_alpha * Math.pow(Math.abs(sin_alpha), e1 - 1)));

				// unit normal to sphere at this vertex
				mesh.normals[i][j].x = (float) (ry * rz * Math.pow(Math.abs(sin_alpha), e1 - 1) * cos_alpha * Math.pow(Math.abs(sin_theta), e2 - 1)
						* cos_theta);

				mesh.normals[i][j].y = (float) (rx * rz * Math.pow(Math.abs(sin_alpha), e1 - 1) * cos_alpha * Math.pow(Math.abs(cos_theta), e2 - 1)
						* sin_theta);

				mesh.normals[i][j].z = (float) (rx * ry * Math.pow(Math.abs(cos_alpha), e1 - 1) * sin_alpha
						* (Math.pow(Math.abs(cos_theta), e2 + 1) * Math.pow(Math.abs(sin_theta), e2 - 1)
								+ Math.pow(Math.abs(sin_theta), e2 + 1) * Math.pow(Math.abs(cos_theta), e2 - 1)));

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
		return "super ellipsoid";
	}

}
