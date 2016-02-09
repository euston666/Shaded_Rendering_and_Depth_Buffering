
/*
 * Class for spot light source. Nov 2015 created by Hengbin Liao
 */

public class SpotLight implements Light {

	public Point light_position;
	public Vector central_direction;
	private ColorType color;
	private double minCos;

	private float a0 = 1;
	private float a1 = 0.00005f;
	private float a2 = 0.0000001f;
	private float a_l = 100; // 300

	public SpotLight(ColorType color, Point light_position, Vector central_direction, double angle) {
		this.light_position = light_position;
		this.color = color;
		this.central_direction = central_direction;
		this.central_direction.normalize();

		minCos = Math.cos(angle);
	}

	/* apply the light on the object */
	public void applyLight(Material mat, Vector view_vector, Vector normal, Point p) {

		Vector light_direction = new Vector(light_position.x - p.x, light_position.y - p.y, light_position.z - p.z);
		float distance = (float) light_direction.magnitude();
		float radialAtten = 1 / (a0 + a1 * distance + a2 * distance * distance);

		light_direction.normalize();
		normal.normalize();

		float angleCos = central_direction.dotProduct(light_direction);
		float angularAtten = (float) Math.pow(angleCos, a_l);

		float r = 0.0f, g = 0.0f, b = 0.0f;
		float nl_dot = light_direction.dotProduct(normal);
		if (nl_dot > 0.0 && angleCos > minCos) {
			if (mat.isDiffuse) {
				r += (float) (nl_dot * mat.kd.r * color.r);
				g += (float) (nl_dot * mat.kd.g * color.g);
				b += (float) (nl_dot * mat.kd.b * color.b);
			}

			if (mat.isSpecular) {
				view_vector.normalize();
				float vr_dot = light_direction.reflect(normal).dotProduct(view_vector);
				if (vr_dot > 0) {
					r += (float) Math.pow(vr_dot * mat.ks.r * color.r, mat.ns);
					g += (float) Math.pow(vr_dot * mat.ks.r * color.g, mat.ns);
					b += (float) Math.pow(vr_dot * mat.ks.b * color.b, mat.ns);
				}
			}
		}

		p.c.r += r * radialAtten * angularAtten;
		p.c.g += g * radialAtten * angularAtten;
		p.c.b += b * radialAtten * angularAtten;

		p.c.r = Math.min(p.c.r, 1);
		p.c.g = Math.min(p.c.g, 1);
		p.c.b = Math.min(p.c.b, 1);

	}

	public void translate(int x, int y, int z) {
		this.light_position.x += x;
		this.light_position.y += y;
		this.light_position.z += z;
	}

	/* rotate the object */
	public void rotate(Quaternion q, Vector pivot) {
		Matrix vMatrix = new Matrix();
		Matrix dMatrix = new Matrix();
		float[] qMatrix = new Matrix(q.toMatrix()).getTranspose();

		float[] transIn = Matrix.translate(-pivot.x, -pivot.y, -pivot.z);
		float[] transOut = Matrix.translate(pivot.x, pivot.y, pivot.z);

		vMatrix.multiplyMatrix(transOut);
		vMatrix.multiplyMatrix(qMatrix);
		vMatrix.multiplyMatrix(transIn);

		dMatrix.multiplyMatrix(qMatrix);

		Vector v = new Vector(this.light_position);
		Vector vt = vMatrix.multiplyPoint(v);
		this.light_position = new Point(vt);

		this.central_direction = dMatrix.multiplyPoint(this.central_direction);

		// System.out.println("spot light pos: " + light_position.toString());
		// System.out.println("spot light dir: " + central_direction.toString());
	}

	public String position() {
		return "(" + this.light_position.x + ", " + this.light_position.y + ", " + this.light_position.z + ")";
	}

	/* get the name of the object */
	public String getName() {
		return "spot light";
	}

}
