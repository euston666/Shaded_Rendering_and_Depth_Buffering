
/*
 * Class for depth buffer. Nov 2015 created by Hengbin Liao
 */

public class DepthBuffer {
	private int width, height;
	private int[][] depthBuff;

	public DepthBuffer(int width, int height) {
		this.width = width;
		this.height = height;
		this.depthBuff = new int[width][height];
		clearDepthBuffer();
	}

	// clear the depth buffer
	public void clearDepthBuffer() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				depthBuff[i][j] = -999;
			}
		}
	}

	public int getDepth(int x, int y) {
		return depthBuff[x][y];
	}

	public void setDepth(int x, int y, int depth) {
		depthBuff[x][y] = depth;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public int[][] getBuffer() {
		return this.depthBuff;
	}
}
