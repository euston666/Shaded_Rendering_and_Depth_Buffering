// ****************************************************************************
// 3D triangle mesh with normals
// ****************************************************************************
// History :
// Nov 6, 2014 Created by Stan Sclaroff
// Nov 20, 2015 modified by Hengbin Liao

public class Mesh {
	public int rows, cols;
	public Vector[][] vertices;
	public Vector[][] normals;

	public Mesh(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;

		vertices = new Vector[rows][cols];
		normals = new Vector[rows][cols];

		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				vertices[i][j] = new Vector();
				normals[i][j] = new Vector();
			}
		}
	}

	// transform the mesh with vertex Matrix and normal matrix
	public void transformMesh(Matrix vMatrix, Matrix nMatrix) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				vertices[i][j] = vMatrix.multiplyPoint(vertices[i][j]);
				normals[i][j] = nMatrix.multiplyPoint(normals[i][j]);
				normals[i][j].normalize();
			}
		}
	}
}
