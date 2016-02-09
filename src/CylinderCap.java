
/*
 * Class for cylinder caps. Nov 2015 created by Hengbin Liao
 */

import java.util.ArrayList;

public class CylinderCap {

	public TriangleFan cap;
	private Material mat;
	private int steps;

	public CylinderCap(Vector center, float rx, float ry, Vector normal, Material mat, int steps) {
		this.mat = mat;
		this.steps = steps;
		this.cap = new TriangleFan(center, normal, steps, rx, ry);
	}

	/* draw the object with flat shading */
	public void drawFlat(ArrayList<Light> lights, Vector view_vector) {
		drawGouraud(lights, view_vector);
	}

	/* draw the object with Gouraud shading */
	public void drawGouraud(ArrayList<Light> lights, Vector view_vector) {
		Vector triangle_normal = cap.getNormal();
		Point p0, p1, p2;

		Vector view_norm = new Vector(view_vector);
		view_norm.normalize();

		for (int i = 0; i < this.steps - 1; i++) {
			p0 = new Point((int) cap.center.x, (int) cap.center.y, (int) cap.center.z);
			p1 = new Point((int) cap.fanPoints[i].x, (int) cap.fanPoints[i].y, (int) cap.fanPoints[i].z);
			p2 = new Point((int) cap.fanPoints[i + 1].x, (int) cap.fanPoints[i + 1].y, (int) cap.fanPoints[i + 1].z);

			for (int k = 0; k < lights.size(); k++) {
				Light light = lights.get(k);
				light.applyLight(mat, view_norm, triangle_normal, p0);
				light.applyLight(mat, view_norm, triangle_normal, p1);
				light.applyLight(mat, view_norm, triangle_normal, p2);
			}

			Triangle t = new Triangle(p0, p1, p2);
			t.drawGouraud();
		}
	}

	/* draw the object with Phong shading */
	public void drawPhong(ArrayList<Light> lights, Vector view_vector) {
		Vector triangle_normal = cap.getNormal();
		Point p0, p1, p2;

		for (int i = 0; i < this.steps - 1; i++) {
			p0 = new Point((int) cap.center.x, (int) cap.center.y, (int) cap.center.z);
			p1 = new Point((int) cap.fanPoints[i].x, (int) cap.fanPoints[i].y, (int) cap.fanPoints[i].z);
			p2 = new Point((int) cap.fanPoints[i + 1].x, (int) cap.fanPoints[i + 1].y, (int) cap.fanPoints[i + 1].z);

			p0.normal = triangle_normal;
			p1.normal = triangle_normal;
			p2.normal = triangle_normal;

			Triangle t = new Triangle(p0, p1, p2);
			t.drawPhong(mat, lights, view_vector);
		}
	}

	public Material getMat() {
		return mat;
	}

	public void setMat(Material mat) {
		this.mat = mat;
	}

}
