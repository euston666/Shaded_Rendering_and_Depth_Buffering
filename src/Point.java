
/*
 * Class for 3D vertices of solids. Nov 2015 modified by Hengbin Liao
 */

public class Point {
	public int x;
	public int y;
	public int z;
	public ColorType c;
	public Vector normal;
	public float u, v; // uv coordinates for texture mapping

	public Point(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.c = new ColorType();
		this.normal = new Vector();
	}

	public Point(int x, int y, int z, Vector normal) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.c = new ColorType();
		this.normal = new Vector(normal);
	}

	public Point(Point p) {
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
		this.c = new ColorType(p.c.r, p.c.g, p.c.b);
		this.normal = new Vector(p.normal);
	}

	public Point(Vector v) {
		this.x = (int) v.x;
		this.y = (int) v.y;
		this.z = (int) v.z;
	}

	public String toString() {
		return x + ", " + y + ", " + z;
	}
}
