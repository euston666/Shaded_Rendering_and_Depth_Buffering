
/*
 * Class representing triangles of the mesh. Nov 2015 created by Hengbin Liao
 */

import java.util.ArrayList;

public class Triangle {
	private Point p0, p1, p2;
	private TriangleEdge[] edges;
	private int tallestIndex;

	public Triangle(Point p0, Point p1, Point p2) {
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;

		edges = new TriangleEdge[3];

		edges[0] = new TriangleEdge(p0, p1);
		edges[1] = new TriangleEdge(p1, p2);
		edges[2] = new TriangleEdge(p2, p0);

		tallestIndex = tallestEdge();
	}

	// draw the triangle with flat shading
	public void drawFlat() {
		drawGouraud();
	}

	// draw the triangle with Gouraud shading
	public void drawGouraud() {
		SketchBase.drawPoint(PA4.buff, p0);
		SketchBase.drawPoint(PA4.buff, p1);
		SketchBase.drawPoint(PA4.buff, p2);

		for (int i = 1; i < edges.length; i++) {
			TriangleEdge e1 = edges[tallestIndex];
			TriangleEdge e2 = edges[(tallestIndex + i) % 3];

			if (e2.p0.y == e2.p1.y) {
				continue;
			}

			int startY = e2.p0.y < e2.p1.y ? e2.p0.y : e2.p1.y;
			int endY = e2.p0.y < e2.p1.y ? e2.p1.y : e2.p0.y;

			for (int j = startY; j <= endY; j++) {
				int x1 = e1.getXfromY(j);
				int x2 = e2.getXfromY(j);

				int z1 = e1.getZfromY(j);
				int z2 = e2.getZfromY(j);

				Point endPoint1 = new Point(x1, j, z1);
				Point endPoint2 = new Point(x2, j, z2);

				endPoint1.c = e1.getColorFromY(j);
				endPoint2.c = e2.getColorFromY(j);

				SketchBase.drawLine(PA4.buff, endPoint1, endPoint2);
			}
		}
	}

	// draw the triangle with Phong shading
	public void drawPhong(Material mat, ArrayList<Light> lights, Vector view_vector) {

		Vector view_norm = new Vector(view_vector);
		view_norm.normalize();

		for (int i = 0; i < lights.size(); i++) {
			Light light = lights.get(i);

			light.applyLight(mat, view_norm, p0.normal, p0);
			light.applyLight(mat, view_norm, p1.normal, p1);
			light.applyLight(mat, view_norm, p2.normal, p2);
		}

		SketchBase.drawPoint(PA4.buff, p0);
		SketchBase.drawPoint(PA4.buff, p1);
		SketchBase.drawPoint(PA4.buff, p2);

		for (int i = 1; i < 3; i++) {
			TriangleEdge e1 = edges[tallestIndex];
			TriangleEdge e2 = edges[(tallestIndex + i) % 3];

			if (e2.p0.y == e2.p1.y) {
				continue;
			}

			int startY = e2.p0.y < e2.p1.y ? e2.p0.y : e2.p1.y;
			int endY = e2.p0.y < e2.p1.y ? e2.p1.y : e2.p0.y;

			for (int j = startY; j <= endY; j++) {
				int x1 = (int) e1.getXfromY(j);
				int x2 = (int) e2.getXfromY(j);

				int z1 = e1.getZfromY(j);
				int z2 = e2.getZfromY(j);

				Point endPoint1 = new Point(x1, j, z1);
				Point endPoint2 = new Point(x2, j, z2);

				endPoint1.normal = e1.getNormalFromY(j);
				endPoint2.normal = e2.getNormalFromY(j);

				drawPhongLine(endPoint1, endPoint2, mat, lights, view_vector);
			}
		}
	}

	// draw the scanline with Phong shading
	private void drawPhongLine(Point p0, Point p1, Material mat, ArrayList<Light> lights, Vector view_vector) {
		TriangleEdge edge = new TriangleEdge(p0, p1);

		int xStart = p0.x < p1.x ? p0.x : p1.x;
		int xEnd = p0.x < p1.x ? p1.x : p0.x;

		Vector view_norm = new Vector(view_vector);
		view_norm.normalize();

		if (xStart == xEnd) {
			for (int i = 0; i < lights.size(); i++) {
				Light light = lights.get(i);
				light.applyLight(mat, view_norm, edge.p0.normal, edge.p0);
				light.applyLight(mat, view_norm, edge.p1.normal, edge.p1);
			}

			SketchBase.drawPoint(PA4.buff, edge.p0);
			SketchBase.drawPoint(PA4.buff, edge.p1);
			return;
		}

		for (int i = xStart; i <= xEnd; i++) {
			int z = edge.getZfromX(i);
			Vector n = edge.getNormalFromX(i);
			Point p = new Point(i, p0.y, z, n);

			for (int j = 0; j < lights.size(); j++) {
				lights.get(j).applyLight(mat, view_norm, p.normal, p);
			}

			SketchBase.drawPoint(PA4.buff, p);
		}
	}

	// get the edge with max delta Y
	private int tallestEdge() {
		float maxY = 0;
		int edgeIndex = 0;
		for (int i = 0; i < 3; i++) {
			if (maxY < edges[i].getDy()) {
				edgeIndex = i;
				maxY = edges[i].getDy();
			}
		}
		return edgeIndex;
	}
}
