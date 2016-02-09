// ****************************************************************************
// Infinite light source class
// ****************************************************************************
// History :
// Nov 6, 2014 Created by Stan Sclaroff
// Nov 20, 2015 modified by Hengbin Liao

public class InfiniteLight implements Light {

	public Vector direction;
	private ColorType color;

	public InfiniteLight(ColorType c, Vector direction) {
		this.color = new ColorType(c);
		this.direction = new Vector(direction);
	}

	/* apply the light on the object */
	public void applyLight(Material mat, Vector view_vector, Vector normal, Point p) {

		direction.normalize();
		view_vector.normalize();
		normal.normalize();

		float nl_dot = direction.dotProduct(normal);
		if (nl_dot > 0) {
			if (mat.isDiffuse) {
				p.c.r += mat.kd.r * color.r * nl_dot;
				p.c.g += mat.kd.g * color.g * nl_dot;
				p.c.b += mat.kd.b * color.b * nl_dot;
			}
			if (mat.isSpecular) {
				float vr_dot = direction.reflect(normal).dotProduct(view_vector);
				if (vr_dot > 0) {
					p.c.r += (float) Math.pow(mat.ks.r * color.r * vr_dot, mat.ns);
					p.c.g += (float) Math.pow(mat.ks.g * color.g * vr_dot, mat.ns);
					p.c.b += (float) Math.pow(mat.ks.b * color.b * vr_dot, mat.ns);
				}
			}
		}

		p.c.r = Math.min(p.c.r, 1);
		p.c.g = Math.min(p.c.g, 1);
		p.c.b = Math.min(p.c.b, 1);
	}

	public void translate(int x, int y, int z) {
		;
	}

	/* rotate the light direction */
	public void rotate(Quaternion q, Vector pivot) {
		Matrix m = new Matrix();
		float[] qMatrix = new Matrix(q.toMatrix()).getTranspose();
		m.multiplyMatrix(qMatrix);
		this.direction = m.multiplyPoint(this.direction);
	}

	public String position() {
		return "";
	}

	/* get the name of the object */
	public String getName() {
		return "infinite light";
	}

}
