
/*
 * Class for triangle edges of the mesh. Nov 2015 created by Hengbin Liao
 */

public class TriangleEdge {
	public Point p0, p1;
	private int dx, dy;

	public TriangleEdge(Point _p0, Point _p1) {
		this.p0 = _p0;
		this.p1 = _p1;

		dx = Math.abs(p1.x - p0.x);
		dy = Math.abs(p1.y - p0.y);
	}

	public int getXfromY(float y) {
		return Math.round(p0.x + 1.0f * (p1.x - p0.x) * (y - p0.y) / (p1.y - p0.y));
	}

	public int getZfromX(float x) {
		return Math.round(p0.z + 1.0f * (p1.z - p0.z) * (x - p0.x) / (p1.x - p0.x));
	}

	public int getZfromY(float y) {
		return Math.round(p0.z + 1.0f * (p1.z - p0.z) * (y - p0.y) / (p1.y - p0.y));
	}

	public Vector getNormalFromX(float x) {
		Vector normal = new Vector(0, 0, 0);

		normal.x = p0.normal.x + 1.0f * (p1.normal.x - p0.normal.x) * (x - p0.x) / (p1.x - p0.x);
		normal.y = p0.normal.y + 1.0f * (p1.normal.y - p0.normal.y) * (x - p0.x) / (p1.x - p0.x);
		normal.z = p0.normal.z + 1.0f * (p1.normal.z - p0.normal.z) * (x - p0.x) / (p1.x - p0.x);

		return normal;
	}

	public Vector getNormalFromY(float y) {
		Vector normal = new Vector(0, 0, 0);

		normal.x = p0.normal.x + 1.0f * (p1.normal.x - p0.normal.x) * (y - p0.y) / (p1.y - p0.y);
		normal.y = p0.normal.y + 1.0f * (p1.normal.y - p0.normal.y) * (y - p0.y) / (p1.y - p0.y);
		normal.z = p0.normal.z + 1.0f * (p1.normal.z - p0.normal.z) * (y - p0.y) / (p1.y - p0.y);

		return normal;
	}

	public ColorType getColorFromY(float y) {
		ColorType color = new ColorType(0.0f, 0.0f, 0.0f);

		color.r = p0.c.r + 1.0f * (p1.c.r - p0.c.r) * (y - p0.y) / (p1.y - p0.y);
		color.g = p0.c.g + 1.0f * (p1.c.g - p0.c.g) * (y - p0.y) / (p1.y - p0.y);
		color.b = p0.c.b + 1.0f * (p1.c.b - p0.c.b) * (y - p0.y) / (p1.y - p0.y);

		return color;
	}

	public ColorType getColorFromX(float x) {
		ColorType color = new ColorType(0.0f, 0.0f, 0.0f);

		color.r = p0.c.r + 1.0f * (p1.c.r - p0.c.r) * (x - p0.x) / (p1.x - p0.x);
		color.g = p0.c.g + 1.0f * (p1.c.g - p0.c.g) * (x - p0.x) / (p1.x - p0.x);
		color.b = p0.c.b + 1.0f * (p1.c.b - p0.c.b) * (x - p0.x) / (p1.x - p0.x);

		return color;
	}

	public int getDx() {
		return dx;
	}

	public int getDy() {
		return dy;
	}

}
