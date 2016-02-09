/*
 * Class for point light source. Nov 2015 created by Hengbin Liao
 */

public class PointLight implements Light {

	private ColorType color;
	public Point light_position;

	private float a0 = 1;
	private float a1 = 0.0001f; // 0.0005f
	private float a2 = 0.000005f; // 0.00001f

	public PointLight(ColorType color, Point light_position) {
		this.color = color;
		this.light_position = light_position;
	}

	/* apply the light on the object */
	public void applyLight(Material mat, Vector view_vector, Vector normal, Point p) {

		Vector light_direction = new Vector(light_position.x - p.x, light_position.y - p.y, light_position.z - p.z);
		float distance = (float) light_direction.magnitude();
		float radialAtten = 1 / (a0 + a1 * distance + a2 * distance * distance);

		light_direction.normalize();
		normal.normalize();
		view_vector.normalize();

		float r = 0.0f, g = 0.0f, b = 0.0f;
		float nl_dot = light_direction.dotProduct(normal);
		if (nl_dot > 0) {
			if (mat.isDiffuse) {
				r += (float) (nl_dot * mat.kd.r * color.r);
				g += (float) (nl_dot * mat.kd.g * color.g);
				b += (float) (nl_dot * mat.kd.b * color.b);
			}

			if (mat.isSpecular) {
				float vr_dot = light_direction.reflect(normal).dotProduct(view_vector);
				if (vr_dot > 0) {
					r += (float) Math.pow(vr_dot * mat.ks.r * color.r, mat.ns);
					g += (float) Math.pow(vr_dot * mat.ks.r * color.g, mat.ns);
					b += (float) Math.pow(vr_dot * mat.ks.b * color.b, mat.ns);
				}
			}
		}

		p.c.r += r * radialAtten;
		p.c.g += g * radialAtten;
		p.c.b += b * radialAtten;

		p.c.r = Math.min(p.c.r, 1);
		p.c.g = Math.min(p.c.g, 1);
		p.c.b = Math.min(p.c.b, 1);
	}

	public void translate(int x, int y, int z) {
		this.light_position.x += x;
		this.light_position.y += y;
		this.light_position.z += z;
	}

	/* rotate the light position and light direction */
	public void rotate(Quaternion q, Vector pivot) {
		Matrix vMatrix = new Matrix();
		float[] qMatrix = new Matrix(q.toMatrix()).getTranspose();

		float[] transIn = Matrix.translate(-pivot.x, -pivot.y, -pivot.z);
		float[] transOut = Matrix.translate(pivot.x, pivot.y, pivot.z);

		vMatrix.multiplyMatrix(transOut);
		vMatrix.multiplyMatrix(qMatrix);
		vMatrix.multiplyMatrix(transIn);

		Vector centerVector = new Vector(this.light_position);
		centerVector = vMatrix.multiplyPoint(centerVector);

		this.light_position.x = Math.round(centerVector.x);
		this.light_position.y = Math.round(centerVector.y);
		this.light_position.z = Math.round(centerVector.z);
	}

	public String position() {
		return "(" + this.light_position.x + ", " + this.light_position.y + ", " + this.light_position.z + ")";
	}

	/* get the name of the object */
	public String getName() {
		return "point light";
	}

}
