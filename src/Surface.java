
/*
 * Class for mesh surfaces of solids. Nov 2015 created by Hengbin Liao
 */

import java.util.ArrayList;
import java.util.Random;

public class Surface {
	public Point center;
	private Material mat;

	private Vector[] perturb;
	private Random rnd;

	public Mesh mesh;
	private int rows, cols;

	// vertices / normals / points
	private Vector v0, v1, v2, n0, n1, n2;
	private Point p0, p1, p2;

	public Surface(Point center, Material mat, int rows, int cols) {
		this.center = center;
		this.mat = mat;
		this.rows = rows;
		this.cols = cols;
		mesh = new Mesh(rows, cols);
		perturb = new Vector[] { new Vector(0.01f, -0.02f, 0.03f), new Vector(0.02f, -0.03f, 0.04f), new Vector(0.03f, 0.04f, -0.05f),
				new Vector(0.04f, -0.05f, 0.06f), new Vector(0.05f, -0.06f, 0.07f), new Vector(0.06f, 0.07f, -0.08f) };
		rnd = new Random();
	}

	/* draw the object with flat shading */
	public void drawFlat(ArrayList<Light> lights, Vector view_vector) {
		Vector triangle_normal = new Vector();

		for (int i = 0; i < rows - 1; i++) {
			for (int j = 0; j < cols - 1; j++) {

				/** draw the first triangle **/
				v0 = mesh.vertices[i][j];
				v1 = mesh.vertices[i][j + 1];
				v2 = mesh.vertices[i + 1][j + 1];

				p0 = new Point((int) v0.x, (int) v0.y, (int) v0.z);
				p1 = new Point((int) v1.x, (int) v1.y, (int) v1.z);
				p2 = new Point((int) v2.x, (int) v2.y, (int) v2.z);

				triangle_normal = computeTriangleNormal(v0, v1, v2);
				Vector view_norm = new Vector(view_vector);
				view_norm.normalize();

				for (int k = 0; k < lights.size(); k++) {
					Light light = lights.get(k);
					light.applyLight(mat, view_norm, triangle_normal, p0);
					light.applyLight(mat, view_norm, triangle_normal, p1);
					light.applyLight(mat, view_norm, triangle_normal, p2);
				}

				Triangle t1 = new Triangle(p0, p1, p2);
				t1.drawFlat();

				/** draw the second triangle **/
				v0 = mesh.vertices[i][j];
				v1 = mesh.vertices[i + 1][j + 1];
				v2 = mesh.vertices[i + 1][j];

				p0 = new Point((int) v0.x, (int) v0.y, (int) v0.z);
				p1 = new Point((int) v1.x, (int) v1.y, (int) v1.z);
				p2 = new Point((int) v2.x, (int) v2.y, (int) v2.z);

				triangle_normal = computeTriangleNormal(v0, v1, v2);

				for (int k = 0; k < lights.size(); k++) {
					Light light = lights.get(k);
					light.applyLight(mat, view_norm, triangle_normal, p0);
					light.applyLight(mat, view_norm, triangle_normal, p1);
					light.applyLight(mat, view_norm, triangle_normal, p2);
				}

				Triangle t2 = new Triangle(p0, p1, p2);
				t2.drawFlat();
			}
		}
	}

	/* draw the object with Gouraud shading */
	public void drawGouraud(ArrayList<Light> lights, Vector view_vector) {

		for (int i = 0; i < rows - 1; i++) {
			for (int j = 0; j < cols - 1; j++) {

				/** draw the first triangle **/
				v0 = mesh.vertices[i][j];
				v1 = mesh.vertices[i][j + 1];
				v2 = mesh.vertices[i + 1][j + 1];

				p0 = new Point((int) v0.x, (int) v0.y, (int) v0.z);
				p1 = new Point((int) v1.x, (int) v1.y, (int) v1.z);
				p2 = new Point((int) v2.x, (int) v2.y, (int) v2.z);

				n0 = mesh.normals[i][j];
				n1 = mesh.normals[i][j + 1];
				n2 = mesh.normals[i + 1][j + 1];

				Vector view_norm = new Vector(view_vector);
				view_norm.normalize();
				for (int k = 0; k < lights.size(); k++) {
					Light light = lights.get(k);
					light.applyLight(mat, view_norm, n0, p0);
					light.applyLight(mat, view_norm, n1, p1);
					light.applyLight(mat, view_norm, n2, p2);
				}

				Triangle t1 = new Triangle(p0, p1, p2);
				t1.drawGouraud();

				/** draw the second triangle **/
				v0 = mesh.vertices[i][j];
				v1 = mesh.vertices[i + 1][j + 1];
				v2 = mesh.vertices[i + 1][j];

				p0 = new Point((int) v0.x, (int) v0.y, (int) v0.z);
				p1 = new Point((int) v1.x, (int) v1.y, (int) v1.z);
				p2 = new Point((int) v2.x, (int) v2.y, (int) v2.z);

				n0 = mesh.normals[i][j];
				n1 = mesh.normals[i + 1][j + 1];
				n2 = mesh.normals[i + 1][j];

				for (int k = 0; k < lights.size(); k++) {
					Light light = lights.get(k);
					light.applyLight(mat, view_norm, n0, p0);
					light.applyLight(mat, view_norm, n1, p1);
					light.applyLight(mat, view_norm, n2, p2);
				}

				Triangle t2 = new Triangle(p0, p1, p2);
				t2.drawGouraud();
			}
		}
	}

	/* draw the object with Phong shading */
	public void drawPhong(ArrayList<Light> lights, Vector view_vector) {
		for (int i = 0; i < rows - 1; i++) {
			for (int j = 0; j < cols - 1; j++) {

				/** draw the first triangle **/
				v0 = mesh.vertices[i][j];
				v1 = mesh.vertices[i][j + 1];
				v2 = mesh.vertices[i + 1][j + 1];

				p0 = new Point((int) v0.x, (int) v0.y, (int) v0.z);
				p1 = new Point((int) v1.x, (int) v1.y, (int) v1.z);
				p2 = new Point((int) v2.x, (int) v2.y, (int) v2.z);

				p0.normal = mesh.normals[i][j];
				p1.normal = mesh.normals[i][j + 1];
				p2.normal = mesh.normals[i + 1][j + 1];

				Triangle t1 = new Triangle(p0, p1, p2);
				t1.drawPhong(mat, lights, view_vector);

				/** draw the second triangle **/
				v0 = mesh.vertices[i][j];
				v1 = mesh.vertices[i + 1][j + 1];
				v2 = mesh.vertices[i + 1][j];

				p0 = new Point((int) v0.x, (int) v0.y, (int) v0.z);
				p1 = new Point((int) v1.x, (int) v1.y, (int) v1.z);
				p2 = new Point((int) v2.x, (int) v2.y, (int) v2.z);

				p0.normal = mesh.normals[i][j];
				p1.normal = mesh.normals[i + 1][j + 1];
				p2.normal = mesh.normals[i + 1][j];

				Triangle t2 = new Triangle(p0, p1, p2);
				t2.drawPhong(mat, lights, view_vector);
			}
		}
	}

	/* draw environment mapping */
	public void drawEnvMapping() {
		Vector[][] vertices = mesh.vertices;
		Vector[][] normals = mesh.normals;
		Vector view = new Vector(0.0f, 0.0f, 1.0f);
		for (int i = 0; i < vertices.length; i++) {
			for (int j = 0; j < vertices[0].length; j++) {
				Vector vertex = vertices[i][j];
				Vector normal = normals[i][j];
				normal.normalize();

				Vector r = view.reflect(normal);
				double m = 2.0 * Math.sqrt(r.x * r.x + r.y * r.y + (r.z + 1.0) * (r.z + 1.0));
				float u = (float) (r.x / m + 0.5);
				float v = (float) (r.y / m + 0.5);

				float[] rgb = SketchBase.getTextureRGB(u, v, PA4.env_texture);
				Point p = new Point((int) vertex.x, (int) vertex.y, (int) vertex.z);
				p.c = new ColorType(rgb[0], rgb[1], rgb[2]);
				SketchBase.drawPoint(PA4.buff, p);
			}
		}
	}

	/* draw bump mapping */
	public void drawBumpMapping(ArrayList<Light> lights, Vector view_vector, Vector[][] Pu, Vector[][] Pv) {
		for (int i = 0; i < rows - 1; i++) {
			for (int j = 0; j < cols - 1; j++) {

				/** draw the first triangle **/
				v0 = mesh.vertices[i][j];
				v1 = mesh.vertices[i][j + 1];
				v2 = mesh.vertices[i + 1][j + 1];

				p0 = new Point((int) v0.x, (int) v0.y, (int) v0.z);
				p1 = new Point((int) v1.x, (int) v1.y, (int) v1.z);
				p2 = new Point((int) v2.x, (int) v2.y, (int) v2.z);

				p0.normal = mesh.normals[i][j];
				p1.normal = mesh.normals[i][j + 1];
				p2.normal = mesh.normals[i + 1][j + 1];

				/* bump map calculation */
				p0.normal.normalize();
				p1.normal.normalize();
				p2.normal.normalize();

				p0.normal = p0.normal.plus(perturb[0]);
				p0.normal.normalize();
				p1.normal = p1.normal.plus(perturb[2]);
				p1.normal.normalize();
				p2.normal = p2.normal.plus(perturb[4]);
				p2.normal.normalize();

				Triangle t1 = new Triangle(p0, p1, p2);
				t1.drawPhong(mat, lights, view_vector);

				/** draw the second triangle **/
				v0 = mesh.vertices[i][j];
				v1 = mesh.vertices[i + 1][j + 1];
				v2 = mesh.vertices[i + 1][j];

				p0 = new Point((int) v0.x, (int) v0.y, (int) v0.z);
				p1 = new Point((int) v1.x, (int) v1.y, (int) v1.z);
				p2 = new Point((int) v2.x, (int) v2.y, (int) v2.z);

				p0.normal = mesh.normals[i][j];
				p1.normal = mesh.normals[i + 1][j + 1];
				p2.normal = mesh.normals[i + 1][j];

				/* bump map calculation */
				p0.normal.normalize();
				p1.normal.normalize();
				p2.normal.normalize();

				p0.normal = p0.normal.plus(perturb[1]);
				p0.normal.normalize();
				p1.normal = p1.normal.plus(perturb[3]);
				p1.normal.normalize();
				p2.normal = p2.normal.plus(perturb[5]);
				p2.normal.normalize();

				Triangle t2 = new Triangle(p0, p1, p2);
				t2.drawPhong(mat, lights, view_vector);
			}
		}
	}

	// helper method that computes the unit normal to the plane of the triangle
	// degenerate triangles yield normal that is numerically zero
	private Vector computeTriangleNormal(Vector v0, Vector v1, Vector v2) {
		Vector e0 = v1.minus(v2);
		Vector e1 = v0.minus(v2);
		Vector norm = e0.crossProduct(e1);

		if (norm.magnitude() > 0.000001)
			norm.normalize();
		else 	// detect degenerate triangle and set its normal to zero
			norm.set((float) 0.0, (float) 0.0, (float) 0.0);

		return norm;
	}

	public Material getMat() {
		return mat;
	}
}
