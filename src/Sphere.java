
// ****************************************************************************
// Sphere class
// ****************************************************************************
// History :
// Nov 6, 2014 Created by Stan Sclaroff
// Nov 2015 modified by Hengbin Liao

import java.util.ArrayList;

public class Sphere extends Object3D {
	private float r;
	private int rows, cols;
	private Surface surface;
	private Vector[][] pu;
	private Vector[][] pv;

	public Sphere(Point center, float r, Material mat, int rows, int cols) {
		super(center, mat);
		this.r = r;
		this.rows = rows;
		this.cols = cols;
		surface = new Surface(center, mat, rows, cols);

		pu = new Vector[rows][cols];
		pv = new Vector[rows][cols];
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				pu[i][j] = new Vector();
				pv[i][j] = new Vector();
			}
		}

		fillMesh();
	}

	/* fill the mesh of the object */
	private void fillMesh() {
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
				// vertex location
				cos_alpha = (float) Math.cos(alpha);
				sin_alpha = (float) Math.sin(alpha);
				mesh.vertices[i][j].x = center.x + r * cos_alpha * cos_theta;
				mesh.vertices[i][j].y = center.y + r * cos_alpha * sin_theta;
				mesh.vertices[i][j].z = center.z + r * sin_alpha;

				// unit normal to sphere at this vertex
				mesh.normals[i][j].x = cos_alpha * cos_theta;
				mesh.normals[i][j].y = cos_alpha * sin_theta;
				mesh.normals[i][j].z = sin_alpha;

				// partial derivatives
				pu[i][j].x = -r * cos_theta * sin_alpha;
				pu[i][j].y = -r * sin_theta * sin_alpha;
				pu[i][j].z = r * cos_alpha;
				pv[i][j].x = -r * cos_alpha * sin_theta;
				pv[i][j].y = r * cos_alpha * cos_theta;
				pv[i][j].z = 0.0f;

				mesh.vertices[i][j].u = (int) ((alpha + Math.PI / 2) / Math.PI * (PA4.bump_texture.getWidth() - 1));
				mesh.vertices[i][j].v = (int) ((theta + Math.PI) / (2 * Math.PI) * (PA4.bump_texture.getHeight() - 1));

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
		surface.drawEnvMapping();
	}

	/* draw bump mapping */
	public void drawBumpMapping(ArrayList<Light> lights, Vector view_vector) {
		surface.drawBumpMapping(lights, view_vector, pu, pv);
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
		center.x += x;
		center.y += y;
		center.z += z;
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
		Matrix vMatrix = new Matrix();
		Matrix nMatrix = new Matrix();
		float[] qMatrix = new Matrix(q.toMatrix()).getTranspose();

		float[] transIn = Matrix.translate(-pivot.x, -pivot.y, -pivot.z);
		float[] transOut = Matrix.translate(pivot.x, pivot.y, pivot.z);

		vMatrix.multiplyMatrix(transOut);
		vMatrix.multiplyMatrix(qMatrix);
		vMatrix.multiplyMatrix(transIn);

		nMatrix.multiplyMatrix(qMatrix);

		surface.mesh.transformMesh(vMatrix, nMatrix);

		Vector centerVector = new Vector(this.center.x, this.center.y, this.center.z);
		centerVector = vMatrix.multiplyPoint(centerVector);
		this.center.x = Math.round(centerVector.x);
		this.center.y = Math.round(centerVector.y);
		this.center.z = Math.round(centerVector.z);

		surface.center = this.center;
	}

	/* get the name of the object */
	public String getName() {
		return "sphere";
	}
}
