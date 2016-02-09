// ****************************************************************************
// material class
// ****************************************************************************
// History :
// Nov 6, 2014po Created by Stan Sclaroff
//

public class Material {
	public ColorType ka, kd, ks;
	public int ns;
	public boolean isSpecular, isDiffuse, isAmbient;

	public Material(ColorType ka, ColorType kd, ColorType ks, int ns) {
		this.ks = new ColorType(ks);  // specular coefficient for r,g,b
		this.ka = new ColorType(ka);  // ambient coefficient for r,g,b
		this.kd = new ColorType(kd);  // diffuse coefficient for r,g,b
		this.ns = ns;  // specular exponent

		// set boolean variables
		isSpecular = (ns > 0 && (ks.r > 0.0 || ks.g > 0.0 || ks.b > 0.0));
		isDiffuse = (kd.r > 0.0 || kd.g > 0.0 || kd.b > 0.0);
		isAmbient = (ka.r > 0.0 || ka.g > 0.0 || ka.b > 0.0);
	}
}
