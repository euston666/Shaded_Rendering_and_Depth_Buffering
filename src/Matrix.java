
/*
 * Class for Matrix calculation. Nov 2015 created by Hengbin Liao
 */

public class Matrix {
	public float[] m;

	public Matrix() {
		m = new float[] { 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f };
	}

	public Matrix(float[] _m) {
		this.m = _m;
	}

	public static float[] scale(float sx, float sy, float sz) {
		float[] sMatrix = new float[] { sx, 0.0f, 0.0f, 0.0f, 0.0f, sy, 0.0f, 0.0f, 0.0f, 0.0f, sz, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f };
		return sMatrix;
	}

	public static float[] translate(float x, float y, float z) {
		float[] tMatrix = new float[] { 1.0f, 0.0f, 0.0f, x, 0.0f, 1.0f, 0.0f, y, 0.0f, 0.0f, 1.0f, z, 0.0f, 0.0f, 0.0f, 1.0f };
		return tMatrix;
	}

	public float[] multiplyMatrix(float[] _m) {
		float[] result = new float[16];
		for (int i = 0; i < 16; i++) {
			int a = i / 4 * 4;
			int b = i % 4;
			result[i] = m[a] * _m[b] + m[a + 1] * _m[b + 4] + m[a + 2] * _m[b + 8] + m[a + 3] * _m[b + 12];
		}
		m = result;

		return result;
	}

	public Vector multiplyPoint(Vector p) {
		Vector res = new Vector(0, 0, 0);

		float[] v = new float[4];

		v[0] = p.x;
		v[1] = p.y;
		v[2] = p.z;
		v[3] = 1.0f;

		for (int i = 0; i < 4; i++) {
			v[i] = v[0] * m[i * 4] + v[1] * m[i * 4 + 1] + v[2] * m[i * 4 + 2] + v[3] * m[i * 4 + 3];
		}

		// pResult.x = v[0] / v[3];
		// pResult.y = v[1] / v[3];
		// pResult.z = v[2] / v[3];
		res.x = v[0];
		res.y = v[1];
		res.z = v[2];

		return res;
	}

	public float[] getTranspose() {
		float[] transpose = new float[16];

		for (int i = 0; i < 16; i++) {
			int j = i % 4 * 4 + i / 4;
			transpose[i] = m[j];
		}

		return transpose;
	}

	public float[] getMatrix() {
		return this.m;
	}

	public void setMatrix(float[] matrix) {
		if (matrix.length == 16) {
			this.m = matrix;
		} else {
			System.out.println("wrong matrix");
		}
	}

	public void printMatrix() {
		for (int i = 0; i < 16; i++) {
			System.out.printf("%.2f ,", m[i]);
			if (i % 4 == 3) {
				System.out.println();
			}
		}
	}

}
