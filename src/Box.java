
/*
 * Class for Box object. Nov 2015 created by Hengbin Liao
 */

import java.util.ArrayList;

public class Box extends Object3D {

	private int rows, cols;
	private float edge_len;

	private Point topCenter;
	private Point bottomCenter;
	private Point leftCenter;
	private Point rightCenter;
	private Point frontCenter;
	private Point backCenter;

	private Surface topSurface;
	private Surface bottomSurface;
	private Surface leftSurface;
	private Surface rightSurface;
	private Surface frontSurface;
	private Surface backSurface;

	private Vector topNormal;
	private Vector bottomNormal;
	private Vector leftNormal;
	private Vector rightNormal;
	private Vector frontNormal;
	private Vector backNormal;

	public Box(Point center, float edge_len, Material mat, int rows, int cols) {
		super(center, mat);
		this.edge_len = edge_len;
		this.rows = rows;
		this.cols = cols;

		topCenter = new Point(center.x, center.y + (int) (edge_len / 2), center.z);
		bottomCenter = new Point(center.x, center.y - (int) (edge_len / 2), center.z);
		leftCenter = new Point(center.x - (int) (edge_len / 2), center.y, center.z);
		rightCenter = new Point(center.x + (int) (edge_len / 2), center.y, center.z);
		frontCenter = new Point(center.x, center.y, center.z + (int) (edge_len / 2));
		backCenter = new Point(center.x, center.y, center.z - (int) (edge_len / 2));

		topSurface = new Surface(topCenter, mat, rows, cols);
		bottomSurface = new Surface(bottomCenter, mat, rows, cols);
		leftSurface = new Surface(leftCenter, mat, rows, cols);
		rightSurface = new Surface(rightCenter, mat, rows, cols);
		frontSurface = new Surface(frontCenter, mat, rows, cols);
		backSurface = new Surface(backCenter, mat, rows, cols);

		topNormal = new Vector(0, 1, 0);
		bottomNormal = new Vector(0, -1, 0);
		leftNormal = new Vector(-1, 0, 0);
		rightNormal = new Vector(1, 0, 0);
		frontNormal = new Vector(0, 0, 1);
		backNormal = new Vector(0, 0, -1);

		fillMesh();
	}

	/* fill the mesh of the object */
	private void fillMesh() {
		fillFrontMesh();
		fillRearMesh();
		fillTopMesh();
		fillBottomMesh();
		fillLeftMesh();
		fillRightMesh();
	}

	// fill the front mesh
	private void fillFrontMesh() {
		int i, j;
		float h, w;
		float d_h = edge_len / (rows - 1);
		float d_w = edge_len / (cols - 1);
		Mesh mesh = frontSurface.mesh;

		for (i = 0, h = (edge_len / 2); i < rows; i++, h -= d_h) {
			for (j = 0, w = -(edge_len / 2); j < cols; j++, w += d_w) {
				mesh.vertices[i][j].x = (float) ((float) (frontCenter.x) + w);
				mesh.vertices[i][j].y = (float) ((float) (frontCenter.y) + h);
				mesh.vertices[i][j].z = (float) frontCenter.z;

				mesh.normals[i][j] = frontNormal;
			}
		}
	}

	// fill the rear mesh
	private void fillRearMesh() {
		int i, j;
		float h, w;
		float d_h = edge_len / (rows - 1);
		float d_w = edge_len / (cols - 1);
		Mesh mesh = backSurface.mesh;

		for (i = 0, h = -(edge_len / 2); i < rows; i++, h += d_h) {
			for (j = 0, w = -(edge_len / 2); j < mesh.cols; j++, w += d_w) {
				mesh.vertices[i][j].x = (float) ((float) (backCenter.x) + w);
				mesh.vertices[i][j].y = (float) ((float) (backCenter.y) + h);
				mesh.vertices[i][j].z = (float) backCenter.z;

				mesh.normals[i][j] = backNormal;
			}
		}
	}

	// fill the top mesh
	private void fillTopMesh() {
		int i, j;
		float h, w;
		float d_h = edge_len / (rows - 1);
		float d_w = edge_len / (cols - 1);
		Mesh mesh = topSurface.mesh;

		for (i = 0, h = (edge_len / 2); i < rows; i++, h -= d_h) {
			for (j = 0, w = -(edge_len / 2); j < cols; j++, w += d_w) {
				mesh.vertices[i][j].x = (float) ((float) (topCenter.x) + h);
				mesh.vertices[i][j].y = (float) (topCenter.y);
				mesh.vertices[i][j].z = (float) ((float) topCenter.z + w);

				mesh.normals[i][j] = topNormal;
			}
		}
	}

	// fill the bottom mesh
	private void fillBottomMesh() {
		int i, j;
		float h, w;
		float d_h = edge_len / (rows - 1);
		float d_w = edge_len / (cols - 1);
		Mesh mesh = bottomSurface.mesh;

		for (i = 0, h = -(edge_len / 2); i < rows; i++, h += d_h) {
			for (j = 0, w = -(edge_len / 2); j < cols; j++, w += d_w) {
				mesh.vertices[i][j].x = (float) ((float) (bottomCenter.x) + h);
				mesh.vertices[i][j].y = (float) (bottomCenter.y);
				mesh.vertices[i][j].z = (float) ((float) bottomCenter.z + w);

				mesh.normals[i][j] = bottomNormal;
			}
		}
	}

	// fill the left mesh
	private void fillLeftMesh() {
		int i, j;
		float h, w;
		float d_h = edge_len / (rows - 1);
		float d_w = edge_len / (cols - 1);
		Mesh mesh = leftSurface.mesh;

		for (i = 0, h = -(edge_len / 2); i < rows; i++, h += d_h) {
			for (j = 0, w = -(edge_len / 2); j < cols; j++, w += d_w) {
				mesh.vertices[i][j].x = (float) (leftCenter.x);
				mesh.vertices[i][j].y = (float) ((float) leftCenter.y + w);
				mesh.vertices[i][j].z = (float) ((float) leftCenter.z + h);

				mesh.normals[i][j] = leftNormal;
			}
		}
	}

	// fill the right mesh
	private void fillRightMesh() {
		int i, j;
		float h, w;
		float d_h = edge_len / (rows - 1);
		float d_w = edge_len / (cols - 1);
		Mesh mesh = rightSurface.mesh;

		for (i = 0, h = (edge_len / 2); i < rows; i++, h -= d_h) {
			for (j = 0, w = -(edge_len / 2); j < cols; j++, w += d_w) {
				mesh.vertices[i][j].x = (float) (rightCenter.x);
				mesh.vertices[i][j].y = (float) ((float) rightCenter.y + w);
				mesh.vertices[i][j].z = (float) ((float) rightCenter.z + h);

				mesh.normals[i][j] = rightNormal;
			}
		}
	}

	/* draw the object with flat shading */
	public void drawFlat(ArrayList<Light> lights, Vector view_vector) {
		topSurface.drawFlat(lights, view_vector);
		bottomSurface.drawFlat(lights, view_vector);
		leftSurface.drawFlat(lights, view_vector);
		rightSurface.drawFlat(lights, view_vector);
		frontSurface.drawFlat(lights, view_vector);
		backSurface.drawFlat(lights, view_vector);
	}

	/* draw the object with Gouraud shading */
	public void drawGouraud(ArrayList<Light> lights, Vector view_vector) {
		topSurface.drawGouraud(lights, view_vector);
		bottomSurface.drawGouraud(lights, view_vector);
		leftSurface.drawGouraud(lights, view_vector);
		rightSurface.drawGouraud(lights, view_vector);
		frontSurface.drawGouraud(lights, view_vector);
		backSurface.drawGouraud(lights, view_vector);
	}

	/* draw the object with Phong shading */
	public void drawPhong(ArrayList<Light> lights, Vector view_vector) {
		topSurface.drawPhong(lights, view_vector);
		bottomSurface.drawPhong(lights, view_vector);
		leftSurface.drawPhong(lights, view_vector);
		rightSurface.drawPhong(lights, view_vector);
		frontSurface.drawPhong(lights, view_vector);
		backSurface.drawPhong(lights, view_vector);
	}

	/* draw environment mapping */
	public void drawEnvMapping() {
	}

	/* draw bump mapping */
	public void drawBumpMapping(ArrayList<Light> lights, Vector view_vector) {
	}

	/* toggle the diffuse effect */
	public void toggleDiffuse(boolean isDiffuse) {
		topSurface.getMat().isDiffuse = isDiffuse;
		bottomSurface.getMat().isDiffuse = isDiffuse;
		leftSurface.getMat().isDiffuse = isDiffuse;
		rightSurface.getMat().isDiffuse = isDiffuse;
		frontSurface.getMat().isDiffuse = isDiffuse;
		backSurface.getMat().isDiffuse = isDiffuse;
	}

	/* toggle the specular effect */
	public void toggleSpecular(boolean isSpecular) {
		topSurface.getMat().isSpecular = isSpecular;
		bottomSurface.getMat().isSpecular = isSpecular;
		leftSurface.getMat().isSpecular = isSpecular;
		rightSurface.getMat().isSpecular = isSpecular;
		frontSurface.getMat().isSpecular = isSpecular;
		backSurface.getMat().isSpecular = isSpecular;
	}

	/* toggle the ambient effect */
	public void toggleAmbient(boolean isAmbient) {
		topSurface.getMat().isAmbient = isAmbient;
		bottomSurface.getMat().isAmbient = isAmbient;
		leftSurface.getMat().isAmbient = isAmbient;
		rightSurface.getMat().isAmbient = isAmbient;
		frontSurface.getMat().isAmbient = isAmbient;
		backSurface.getMat().isAmbient = isAmbient;
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

		topSurface.mesh.transformMesh(vMatrix, nMatrix);
		bottomSurface.mesh.transformMesh(vMatrix, nMatrix);
		leftSurface.mesh.transformMesh(vMatrix, nMatrix);
		rightSurface.mesh.transformMesh(vMatrix, nMatrix);
		frontSurface.mesh.transformMesh(vMatrix, nMatrix);
		backSurface.mesh.transformMesh(vMatrix, nMatrix);

		Vector centerVector = new Vector(this.center.x, this.center.y, this.center.z);
		centerVector = vMatrix.multiplyPoint(centerVector);

		center.x = Math.round(centerVector.x);
		center.y = Math.round(centerVector.y);
		center.z = Math.round(centerVector.z);
	}

	/* translate the object */
	public void translate(float x, float y, float z) {

		Matrix vMatrix = new Matrix();
		vMatrix.setMatrix(Matrix.translate(x, y, z));
		Matrix nMatrix = new Matrix();

		topSurface.mesh.transformMesh(vMatrix, nMatrix);
		bottomSurface.mesh.transformMesh(vMatrix, nMatrix);
		leftSurface.mesh.transformMesh(vMatrix, nMatrix);
		rightSurface.mesh.transformMesh(vMatrix, nMatrix);
		frontSurface.mesh.transformMesh(vMatrix, nMatrix);
		backSurface.mesh.transformMesh(vMatrix, nMatrix);
	}

	/* scale the object */
	public void scale(float factor) {

		float[] transIn = Matrix.translate(-this.center.x, -this.center.y, -this.center.z);
		float[] transOut = Matrix.translate(this.center.x, this.center.y, this.center.z);
		float[] sMatrix = new Matrix().multiplyMatrix(Matrix.scale(factor, factor, factor));

		Matrix vMatrix = new Matrix();
		vMatrix.setMatrix(transOut);
		vMatrix.multiplyMatrix(sMatrix);
		vMatrix.multiplyMatrix(transIn);

		Matrix nMatrix = new Matrix();
		topSurface.mesh.transformMesh(vMatrix, nMatrix);
		bottomSurface.mesh.transformMesh(vMatrix, nMatrix);
		leftSurface.mesh.transformMesh(vMatrix, nMatrix);
		rightSurface.mesh.transformMesh(vMatrix, nMatrix);
		frontSurface.mesh.transformMesh(vMatrix, nMatrix);
		backSurface.mesh.transformMesh(vMatrix, nMatrix);
	}

	/* get the name of the object */
	public String getName() {
		return "box";
	}

}
