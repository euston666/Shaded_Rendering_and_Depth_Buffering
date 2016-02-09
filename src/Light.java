
/*
 * Interface for different kinds of lights. Nov 2015 created by Hengbin Liao
 */

public interface Light {
	/* apply the light on the object */
	public void applyLight(Material mat, Vector view_vector, Vector normal, Point p);

	public void translate(int x, int y, int z);

	public void rotate(Quaternion q, Vector pivot);

	public String position();

	public String getName();
}
