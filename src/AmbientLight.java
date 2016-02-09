/*
 * Class representing the ambient light. Nov 2015 created by Hengbin Liao
 */

public class AmbientLight implements Light {

	// ambient color
	private ColorType color;

	public AmbientLight(ColorType color) {
		this.color = color;
	}

	// apply with the ambient term
	/* apply the light on the object */
	public void applyLight(Material mat, Vector view_vector, Vector normal, Point p) {

		if (mat.isAmbient) {
			p.c.r += (mat.ka.r * color.r);
			p.c.g += (mat.ka.g * color.g);
			p.c.b += (mat.ka.b * color.b);
		}

		p.c.r = Math.min(p.c.r, 1);
		p.c.g = Math.min(p.c.g, 1);
		p.c.b = Math.min(p.c.b, 1);
	}

	// deliberately left blank
	public void translate(int x, int y, int z) {
		;
	}

	// deliberately left blank
	/* rotate the object */
	public void rotate(Quaternion q, Vector pivot) {
		;
	}

	// deliberately left blank
	public String position() {
		return "";
	}

	/* get the name of the object */
	public String getName() {
		return "ambient light";
	}

}
