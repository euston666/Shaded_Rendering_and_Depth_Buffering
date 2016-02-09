
/*
 * Parent class of all the solids. Nov 2015 created by Hengbin Liao
 */

import java.util.ArrayList;

public abstract class Object3D {
	protected Material material;
	protected Point center;

	public Object3D(Point center, Material mat) {
		this.center = center;
		this.material = mat;
	}

	public void draw(ArrayList<Light> lights, Vector view_vector, String drawType) {
		if (drawType.equals("Flat")) {
			drawFlat(lights, view_vector);
			return;
		}

		if (drawType.equals("Gouraud")) {
			drawGouraud(lights, view_vector);
			return;
		}

		if (drawType.equals("Phong")) {
			drawPhong(lights, view_vector);
			return;
		}

		if (drawType.equals("Environment Mapping")) {
			drawEnvMapping();
			return;
		}

		if (drawType.equals("Bump Mapping")) {
			drawBumpMapping(lights, view_vector);
			return;
		}
	}

	public abstract void drawFlat(ArrayList<Light> lights, Vector view_vector);

	public abstract void drawGouraud(ArrayList<Light> lights, Vector view_vector);

	public abstract void drawPhong(ArrayList<Light> lights, Vector view_vector);

	public abstract void drawEnvMapping();

	public abstract void drawBumpMapping(ArrayList<Light> lights, Vector view_vector);

	public abstract void toggleDiffuse(boolean isDiffuse);

	public abstract void toggleSpecular(boolean isSpecular);

	public abstract void toggleAmbient(boolean isAmbient);

	public abstract void rotate(Quaternion q, Vector rotate_center);

	public abstract void translate(float x, float y, float z);

	public abstract void scale(float factor);

	public abstract String getName();

	public Vector getCenter() {
		return new Vector(center.x, center.y, center.z);
	}
}
