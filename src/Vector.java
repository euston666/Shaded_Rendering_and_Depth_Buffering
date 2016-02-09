// ****************************************************************************
// 3D vector class for example program
// ****************************************************************************
// History :
// Nov 6, 2014 Created by Stan Sclaroff
//

public class Vector {
	public float x, y, z;
	public int u, v;
	public static final float ROUNDOFF_THRESHOLD = 0.0001f;

	public Vector(float _x, float _y, float _z) {
		x = _x;
		y = _y;
		z = _z;
	}

	public Vector(Vector _v) {
		x = _v.x;
		y = _v.y;
		z = _v.z;
	}

	public Vector() {
		x = y = z = (float) 0.0;
	}

	public Vector(Point p) {
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
	}

	// compute the cross-product this (x) v and return result in out
	public void crossProduct(Vector v, Vector out) {
		out.x = this.y * v.z - this.z * v.y;
		out.y = this.z * v.x - this.x * v.z;
		out.z = this.x * v.y - this.y * v.x;
	}

	// compute the cross-product this (x) v and return result
	public Vector crossProduct(Vector v) {
		Vector out = new Vector();
		out.x = this.y * v.z - this.z * v.y;
		out.y = this.z * v.x - this.x * v.z;
		out.z = this.x * v.y - this.y * v.x;
		return (out);
	}

	// compute dot product of v and this vector
	public float dotProduct(Vector v) {
		return (v.x * this.x + v.y * this.y + v.z * this.z);
	}

	// subtract vector v from this vector and return result in out
	public void minus(Vector v, Vector out) {
		out.x = this.x - v.x;
		out.y = this.y - v.y;
		out.z = this.z - v.z;
	}

	// subtract vector v from this vector and return result
	public Vector minus(Vector v) {
		Vector out = new Vector();
		out.x = this.x - v.x;
		out.y = this.y - v.y;
		out.z = this.z - v.z;
		return (out);
	}

	// scale this vector by s and return result
	public Vector scale(float s) {
		Vector out = new Vector();
		out.x = this.x * s;
		out.y = this.y * s;
		out.z = this.z * s;
		return (out);
	}

	// add the vector v to this vector and return result
	public Vector plus(Vector v) {
		Vector out = new Vector();
		out.x = this.x + v.x;
		out.y = this.y + v.y;
		out.z = this.z + v.z;
		return (out);
	}

	// compute the length / magnitude
	public double magnitude() {
		double mag = Math.sqrt(dotProduct(this));
		return (mag);
	}

	// produce unit vector
	public void normalize() {
		double mag = this.magnitude();
		if (mag > ROUNDOFF_THRESHOLD) {
			this.x /= mag;
			this.y /= mag;
			this.z /= mag;
		}
		// should probably throw an error exception here for zero magnitude
	}

	// compute the reflection of this vector around vector n
	public Vector reflect(Vector n) {
		float dot = 2 * this.dotProduct(n);
		Vector out = n.scale(dot);
		out = out.minus(this);

		return (out);
	}

	public void set(float _x, float _y, float _z) {
		x = _x;
		y = _y;
		z = _z;
	}

	public String toString() {
		return x + ", " + y + ", " + z;
	}
}