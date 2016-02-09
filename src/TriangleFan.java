
/*
 * Class for triangle fan to render cylinder caps. Nov 2015 created by Hengbin Liao
 */

public class TriangleFan {
	public Vector center;
	private Vector normal;
	private int steps;
	private float rx;
	private float ry;
	public Vector[] fanPoints;

	public TriangleFan(Vector center, Vector normal, int steps, float rx, float ry) {
		this.center = center;
		this.normal = normal;
		this.steps = steps;
		this.rx = rx;
		this.ry = ry;
		this.fanPoints = new Vector[steps];
		for (int i = 0; i < steps; i++) {
			fanPoints[i] = new Vector();
		}
		fillFan();
	}

	private void fillFan() {
		int i;
		float theta, cos_theta, sin_theta;
		float d_theta = (float) (2.0 * Math.PI) / (steps - 1);

		for (i = 0, theta = (float) -Math.PI; i < steps; i++, theta += d_theta) {
			cos_theta = (float) Math.cos(theta);
			sin_theta = (float) Math.sin(theta);

			fanPoints[i].x = center.x + rx * cos_theta;
			fanPoints[i].y = center.y + ry * sin_theta;
			fanPoints[i].z = center.z;
		}
	}

	/* rotate the object */
	public void rotate(Quaternion q, Vector pivot) {
		Matrix vMatrix = new Matrix();
		Matrix nMatrix = new Matrix();
		float[] qMatrix = new Matrix(q.toMatrix()).getTranspose();

		float[] transIn = Matrix.translate(-pivot.x, -pivot.y, -pivot.z);
		float[] transOut = Matrix.translate(pivot.x, pivot.y, pivot.z);

		vMatrix.multiplyMatrix(transOut);
		vMatrix.multiplyMatrix(qMatrix);
		vMatrix.multiplyMatrix(transIn);

		nMatrix.multiplyMatrix(qMatrix);

		transformFan(vMatrix, nMatrix);
	}

	public void transformFan(Matrix vMatrix, Matrix nMatrix) {
		for (int i = 0; i < steps; i++) {
			fanPoints[i] = vMatrix.multiplyPoint(fanPoints[i]);
		}
		this.center = vMatrix.multiplyPoint(center);
		normal = nMatrix.multiplyPoint(normal);
	}

	public Vector getNormal() {
		return normal;
	}

}
