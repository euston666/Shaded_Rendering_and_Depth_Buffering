
// ****************************************************************************
// SketchBase.
// ****************************************************************************
// Comments :
// Subroutines to manage and draw points, lines an triangles
//
// History :
// Aug 2014 Created by Jianming Zhang (jimmie33@gmail.com) based on code by
// Stan Sclaroff (from CS480 '06 poly.c)
// Nov 2015 modified by Hengbin Liao to support 3D drawing with depth buffer

import java.awt.Color;
import java.awt.image.BufferedImage;

public class SketchBase {
	public SketchBase() {
		// deliberately left blank
	}

	/**********************************************************************
	 * Draws a point. This is achieved by changing the color of the buffer at the location corresponding to the point.
	 * 
	 * @param buff
	 *            Buffer object.
	 * @param p
	 *            Point to be drawn.
	 */
	public static void drawPoint(BufferedImage buff, Point p) {
		// clipping is implemented
		if (p.x >= 0 && p.x < buff.getWidth() && p.y >= 0 && p.y < buff.getHeight()) {
			if (p.z > PA4.depthBuffer.getDepth(p.x, p.y)) {
				PA4.depthBuffer.setDepth(p.x, p.y, p.z);
				buff.setRGB(p.x, buff.getHeight() - p.y - 1, p.c.getRGB_int());
			}
		}
	}

	/**********************************************************************
	 * Draws a line segment using Bresenham's algorithm, linearly interpolating RGB color along line segment. This
	 * method only uses integer arithmetic.
	 * 
	 * @param buff
	 *            Buffer object.
	 * @param p1
	 *            First given endpoint of the line.
	 * @param p2
	 *            Second given endpoint of the line.
	 */
	public static void drawLine(BufferedImage buff, Point p1, Point p2) {
		int xStart = p1.x, yStart = p1.y, zStart = p1.z;
		int xEnd = p2.x, yEnd = p2.y, zEnd = p2.z;
		int dx = Math.abs(xEnd - xStart), dy = Math.abs(yEnd - yStart), dz = Math.abs(zEnd - zStart);

		if (dx == 0 && dy == 0) {
			drawPoint(buff, p1);
			return;
		}

		// if slope is greater than 1, then swap the role of x and y
		boolean x_y_role_swapped = (dy > dx);
		if (x_y_role_swapped) {
			xStart = p1.y;
			yStart = p1.x;
			xEnd = p2.y;
			yEnd = p2.x;
			dx = Math.abs(xEnd - xStart);
			dy = Math.abs(yEnd - yStart);
		}

		// initialize the decision parameter and increments
		int p = 2 * dy - dx;
		int twoDy = 2 * dy, twoDyMinusDx = 2 * (dy - dx);
		int x = xStart, y = yStart, z = zStart;

		// set step increment to be positive or negative
		int step_x = xStart < xEnd ? 1 : -1;
		int step_y = yStart < yEnd ? 1 : -1;

		// deal with setup for color interpolation
		// first get r,g,b integer values at the end points
		int rStart = p1.c.getR_int(), rEnd = p2.c.getR_int();
		int gStart = p1.c.getG_int(), gEnd = p2.c.getG_int();
		int bStart = p1.c.getB_int(), bEnd = p2.c.getB_int();

		// compute the change in r,g,b
		int dr = Math.abs(rEnd - rStart), dg = Math.abs(gEnd - gStart), db = Math.abs(bEnd - bStart);

		// set step increment to be positive or negative
		int step_r = rStart < rEnd ? 1 : -1;
		int step_g = gStart < gEnd ? 1 : -1;
		int step_b = bStart < bEnd ? 1 : -1;

		// compute whole step in each color that is taken each time through loop
		int whole_step_r = step_r * (dr / dx);
		int whole_step_g = step_g * (dg / dx);
		int whole_step_b = step_b * (db / dx);

		// compute remainder, which will be corrected depending on decision parameter
		dr = dr % dx;
		dg = dg % dx;
		db = db % dx;

		// initialize decision parameters for red, green, and blue
		int p_r = 2 * dr - dx;
		int twoDr = 2 * dr, twoDrMinusDx = 2 * (dr - dx);
		int r = rStart;

		int p_g = 2 * dg - dx;
		int twoDg = 2 * dg, twoDgMinusDx = 2 * (dg - dx);
		int g = gStart;

		int p_b = 2 * db - dx;
		int twoDb = 2 * db, twoDbMinusDx = 2 * (db - dx);
		int b = bStart;

		// draw start pixel
		if (x_y_role_swapped) {
			if (x >= 0 && x < buff.getHeight() && y >= 0 && y < buff.getWidth()) {
				if (z > PA4.depthBuffer.getDepth(y, x)) {
					PA4.depthBuffer.setDepth(y, x, z);
					buff.setRGB(y, buff.getHeight() - x - 1, (r << 16) | (g << 8) | b);
				}
			}
		} else {
			if (y >= 0 && y < buff.getHeight() && x >= 0 && x < buff.getWidth()) {
				if (z > PA4.depthBuffer.getDepth(x, y)) {
					PA4.depthBuffer.setDepth(x, y, z);
					buff.setRGB(x, buff.getHeight() - y - 1, (r << 16) | (g << 8) | b);
				}
			}
		}

		while (x != xEnd) {
			// increment x and y
			x += step_x;
			if (p < 0)
				p += twoDy;
			else {
				y += step_y;
				p += twoDyMinusDx;
			}

			z = (int) (zStart + 1.0f * (zEnd - zStart) * (x - xStart) / (xEnd - xStart));

			// increment r by whole amount slope_r, and correct for accumulated error if needed
			r += whole_step_r;
			if (p_r < 0)
				p_r += twoDr;
			else {
				r += step_r;
				p_r += twoDrMinusDx;
			}

			// increment g by whole amount slope_b, and correct for accumulated error if needed
			g += whole_step_g;
			if (p_g < 0)
				p_g += twoDg;
			else {
				g += step_g;
				p_g += twoDgMinusDx;
			}

			// increment b by whole amount slope_b, and correct for accumulated error if needed
			b += whole_step_b;
			if (p_b < 0)
				p_b += twoDb;
			else {
				b += step_b;
				p_b += twoDbMinusDx;
			}

			if (x_y_role_swapped) {
				if (x >= 0 && x < buff.getHeight() && y >= 0 && y < buff.getWidth()) {
					if (z > PA4.depthBuffer.getDepth(y, x)) {
						PA4.depthBuffer.setDepth(y, x, z);
						buff.setRGB(y, buff.getHeight() - x - 1, (r << 16) | (g << 8) | b);
					}
				}

			} else {
				if (y >= 0 && y < buff.getHeight() && x >= 0 && x < buff.getWidth()) {
					if (z > PA4.depthBuffer.getDepth(x, y)) {
						PA4.depthBuffer.setDepth(x, y, z);
						buff.setRGB(x, buff.getHeight() - y - 1, (r << 16) | (g << 8) | b);
					}
				}
			}
		}
	}

	/**********************************************************************
	 * Draws a filled triangle. The triangle may be filled using flat fill or smooth fill. This routine fills columns of
	 * pixels within the left-hand part, and then the right-hand part of the triangle.
	 * 
	 * * /|\ / | \ / | \ *---|---* left-hand right-hand part part
	 *
	 * @param buff
	 *            Buffer object.
	 * @param p1
	 *            First given vertex of the triangle.
	 * @param p2
	 *            Second given vertex of the triangle.
	 * @param p3
	 *            Third given vertex of the triangle.
	 * @param do_smooth
	 *            Flag indicating whether flat fill or smooth fill should be used.
	 */
	public static void drawTriangle(BufferedImage buff, Point p1, Point p2, Point p3, boolean do_smooth) {
		// sort the triangle vertices by ascending x value
		Point p[] = sortTriangleVerts(p1, p2, p3);

		int x;
		float y_a, y_b, z_a, z_b;
		// y increment
		float inc_y_a, inc_z_a, inc_y_b, inc_z_b;
		// r,g,b increment
		float inc_r_a = 0, inc_g_a = 0, inc_b_a = 0, inc_r_b = 0, inc_g_b = 0, inc_b_b = 0;

		Point side_a = new Point(p[0]), side_b = new Point(p[0]);

		if (!do_smooth) {
			side_a.c = new ColorType(p1.c);
			side_b.c = new ColorType(p1.c);
		}

		y_b = p[0].y;
		z_b = p[0].z;
		inc_y_b = ((float) (p[2].y - p[0].y)) / (p[2].x - p[0].x);
		inc_z_b = ((float) (p[2].z - p[0].z)) / (p[2].x - p[0].x);

		if (do_smooth) {
			// calculate slopes in r, g, b for segment b
			inc_r_b = ((float) (p[2].c.r - p[0].c.r)) / (p[2].x - p[0].x);
			inc_g_b = ((float) (p[2].c.g - p[0].c.g)) / (p[2].x - p[0].x);
			inc_b_b = ((float) (p[2].c.b - p[0].c.b)) / (p[2].x - p[0].x);
		}

		// if there is a left-hand part to the triangle then fill it
		if (p[0].x != p[1].x) {
			y_a = p[0].y;
			z_a = p[0].z;
			inc_y_a = ((float) (p[1].y - p[0].y)) / (p[1].x - p[0].x);
			inc_z_a = ((float) (p[1].z - p[0].z)) / (p[1].x - p[0].x);

			if (do_smooth) {
				// calculate slopes in r, g, b for segment a
				inc_r_a = ((float) (p[1].c.r - p[0].c.r)) / (p[1].x - p[0].x);
				inc_g_a = ((float) (p[1].c.g - p[0].c.g)) / (p[1].x - p[0].x);
				inc_b_a = ((float) (p[1].c.b - p[0].c.b)) / (p[1].x - p[0].x);
			}

			// loop over the columns (vertical lines) for left-hand part of triangle
			// filling from side a to side b of the span
			for (x = p[0].x; x < p[1].x; ++x) {
				drawLine(buff, side_a, side_b);

				++side_a.x;
				++side_b.x;
				y_a += inc_y_a;
				y_b += inc_y_b;
				z_a += inc_z_a;
				z_b += inc_z_b;
				side_a.y = (int) y_a;
				side_b.y = (int) y_b;
				side_a.z = (int) z_a;
				side_b.z = (int) z_b;

				if (do_smooth) {
					side_a.c.r += inc_r_a;
					side_a.c.g += inc_g_a;
					side_a.c.b += inc_b_a;

					side_b.c.r += inc_r_b;
					side_b.c.g += inc_g_b;
					side_b.c.b += inc_b_b;
				}
			}
		}

		// there is no right-hand part of triangle
		if (p[1].x == p[2].x)
			return;

		// set up to fill the right-hand part of triangle
		// replace segment a
		side_a = new Point(p[1]);
		if (!do_smooth)
			side_a.c = new ColorType(p1.c);

		y_a = p[1].y;
		z_a = p[1].z;
		inc_y_a = ((float) (p[2].y - p[1].y)) / (p[2].x - p[1].x);
		inc_z_a = ((float) (p[2].z - p[1].z)) / (p[2].x - p[1].x);

		if (do_smooth) {
			// calculate slopes in r, g, b for replacement for segment a
			inc_r_a = ((float) (p[2].c.r - p[1].c.r)) / (p[2].x - p[1].x);
			inc_g_a = ((float) (p[2].c.g - p[1].c.g)) / (p[2].x - p[1].x);
			inc_b_a = ((float) (p[2].c.b - p[1].c.b)) / (p[2].x - p[1].x);
		}

		// loop over the columns for right-hand part of triangle
		// filling from side a to side b of the span
		for (x = p[1].x; x <= p[2].x; ++x) {
			drawLine(buff, side_a, side_b);

			++side_a.x;
			++side_b.x;
			y_a += inc_y_a;
			y_b += inc_y_b;
			z_a += inc_z_a;
			z_b += inc_z_b;
			side_a.y = (int) y_a;
			side_b.y = (int) y_b;
			side_a.z = (int) z_a;
			side_b.z = (int) z_b;

			if (do_smooth) {
				side_a.c.r += inc_r_a;
				side_a.c.g += inc_g_a;
				side_a.c.b += inc_b_a;

				side_b.c.r += inc_r_b;
				side_b.c.g += inc_g_b;
				side_b.c.b += inc_b_b;
			}
		}
	}

	/**********************************************************************
	 * Helper function to bubble sort triangle vertices by ascending x value.
	 * 
	 * @param p1
	 *            First given vertex of the triangle.
	 * @param p2
	 *            Second given vertex of the triangle.
	 * @param p3
	 *            Third given vertex of the triangle.
	 * @return Array of 3 points, sorted by ascending x value.
	 */
	private static Point[] sortTriangleVerts(Point p1, Point p2, Point p3) {
		Point pts[] = { p1, p2, p3 };
		Point tmp;
		int j = 0;
		boolean swapped = true;

		while (swapped) {
			swapped = false;
			j++;
			for (int i = 0; i < 3 - j; i++) {
				if (pts[i].x > pts[i + 1].x) {
					tmp = pts[i];
					pts[i] = pts[i + 1];
					pts[i + 1] = tmp;
					swapped = true;
				}
			}
		}
		return (pts);
	}

	// get RGB from the texture
	public static float[] getTextureRGB(float u, float v, BufferedImage texture) {
		float[] rgb = new float[3];
		float i, j, tu, tv;
		// RGB values of surrounding texels, 0~255
		int blR, blG, blB, brR, brG, brB, tlR, tlG, tlB, trR, trG, trB;

		int width = texture.getWidth();
		int height = texture.getHeight();

		i = (width - 1) * u;
		j = (height - 1) * v;

		// remainder of linear interpolation
		tu = i - (int) i;
		tv = j - (int) j;

		// edge case, i-1 to get surrounding texels
		if (i == (width - 1))
			i -= 1;
		if (j == (height - 1))
			j -= 1;

		// colors of four surrounding texels
		Color tlC = new Color(texture.getRGB((int) i, (int) j + 1));
		Color trC = new Color(texture.getRGB((int) i + 1, (int) j + 1));
		Color blC = new Color(texture.getRGB((int) i, (int) j));
		Color brC = new Color(texture.getRGB((int) i + 1, (int) j));

		blR = blC.getRed();
		blG = blC.getGreen();
		blB = blC.getBlue();
		brR = brC.getRed();
		brG = brC.getGreen();
		brB = brC.getBlue();
		tlR = tlC.getRed();
		tlG = tlC.getGreen();
		tlB = tlC.getBlue();
		trR = trC.getRed();
		trG = trC.getGreen();
		trB = trC.getBlue();

		rgb[0] = ((1 - tu) * (1 - tv) * blR + tu * (1 - tv) * brR + (1 - tu) * tv * tlR + tu * tv * trR) / 255;
		rgb[1] = ((1 - tu) * (1 - tv) * blG + tu * (1 - tv) * brG + (1 - tu) * tv * tlG + tu * tv * trG) / 255;
		rgb[2] = ((1 - tu) * (1 - tv) * blB + tu * (1 - tv) * brB + (1 - tu) * tv * tlB + tu * tv * trB) / 255;

		return rgb;
	}

}