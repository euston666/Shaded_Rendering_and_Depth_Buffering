
/*
 * Class for display scenes. Nov 2015 created by Hengbin Liao
 */

import java.util.ArrayList;

public class Scene {

	public static final int INFINITE_LIGHT = 0;
	public static final int SPOT_LIGHT = 1;
	public static final int POINT_LIGHT = 2;

	private ArrayList<Object3D> solids;
	private ArrayList<Light> lights;
	private ArrayList<Material> materials;
	private Light[] lights_array;
	private boolean[] light_toggles;

	public int sceneIndex = 0;
	private int selectedObjIndex = 0;
	public int nStep = 20;
	private int ns = 4;

	// object center positioins
	private Point position0 = new Point(340, 340, -100);
	private Point position1 = new Point(680, 340, 100);
	private Point position2 = new Point(512, 680, 0);
	private Point position3 = new Point(512, 512, 0);

	// object radius
	private float sphere_r = 120.0f;
	private float ellipsoid_r = 80.0f;
	private float torus_r = 40.0f;
	private float torus_r_axial = 90.0f;
	private float cylinder_r = 80.0f;
	private float box_edge = 200.0f;

	private Vector view_vector = new Vector(0.0f, 0.0f, 100.0f);
	private ColorType infi_light_color = new ColorType(1.0f, 1.0f, 1.0f);
	private ColorType spot_light_color = new ColorType(1.0f, 0.0f, 1.0f);
	private ColorType point_light_color = new ColorType(1.0f, 1.0f, 0.0f);
	private ColorType ambient_color = new ColorType(1.0f, 1.0f, 1.0f);
	private Vector infinite_light_direction = new Vector(1.0f, 1.0f, 1.0f);
	private Vector spot_light_direction = new Vector(-1.0f, 0.0f, 1.0f);
	private Point spot_light_position = new Point(100, 500, 500);
	private Point point_light_position = new Point(750, 500, 500);
	private double spot_light_angle = Math.PI / 6;

	// solids
	private Sphere sphere;
	private Torus torus;
	private Ellipsoid ellipsoid;
	private Cylinder cylinder;
	private Box box;
	private SuperEllipsoid super_ellipsoid;
	private SuperToroid super_toroid;
	public Material mat0, mat1, mat2, mat3, mat4;

	// light sources
	private InfiniteLight infinite_light;
	private SpotLight spot_light;
	private PointLight point_light;
	private AmbientLight ambient_light;

	public Scene() {

		ColorType ka0 = new ColorType(0.1f, 0.1f, 0.8f);
		ColorType kd0 = new ColorType(0.0f, 0.1f, 0.0f);
		ColorType ks0 = new ColorType(0.9f, 0.9f, 0.9f);

		ColorType ka1 = new ColorType(0.6f, 0.0f, 0.3f);
		ColorType kd1 = new ColorType(0.0f, 0.1f, 0.0f);
		ColorType ks1 = new ColorType(0.9f, 0.9f, 0.9f);

		ColorType ka2 = new ColorType(0.1f, 0.6f, 0.1f);
		ColorType kd2 = new ColorType(0.0f, 0.1f, 0.0f);
		ColorType ks2 = new ColorType(0.9f, 0.9f, 0.9f);

		ColorType ka3 = new ColorType(0.6f, 0.5f, 0.0f);
		ColorType kd3 = new ColorType(0.1f, 0.1f, 0.1f);
		ColorType ks3 = new ColorType(0.9f, 0.9f, 0.9f);

		ColorType ka4 = new ColorType(0.0f, 0.4f, 0.6f);
		ColorType kd4 = new ColorType(0.1f, 0.0f, 0.0f);
		ColorType ks4 = new ColorType(0.9f, 0.9f, 0.9f);

		mat0 = new Material(ka0, kd0, ks0, ns);
		mat1 = new Material(ka1, kd1, ks1, ns);
		mat2 = new Material(ka2, kd2, ks2, ns);
		mat3 = new Material(ka3, kd3, ks3, ns);
		mat4 = new Material(ka4, kd4, ks4, ns);

		materials = new ArrayList<Material>();
		materials.add(mat0);
		materials.add(mat1);
		materials.add(mat2);
		materials.add(mat3);
		materials.add(mat4);

		infinite_light = new InfiniteLight(infi_light_color, infinite_light_direction);
		spot_light = new SpotLight(spot_light_color, spot_light_position, spot_light_direction, spot_light_angle);
		point_light = new PointLight(point_light_color, point_light_position);
		ambient_light = new AmbientLight(ambient_color);

		light_toggles = new boolean[4];
		for (int i = 0; i < 4; i++) {
			light_toggles[i] = true;
		}

		lights_array = new Light[4];
		lights_array[0] = infinite_light;
		lights_array[1] = point_light;
		lights_array[2] = spot_light;
		lights_array[3] = ambient_light;

		lights = new ArrayList<Light>();
		lights.add(infinite_light);
		lights.add(spot_light);
		lights.add(point_light);
		lights.add(ambient_light);

		solids = new ArrayList<Object3D>();
	}

	// initialize the solids of the first scene
	public void setScene0() {
		solids.clear();

		sphere = new Sphere(position0, sphere_r, mat0, nStep, nStep);
		torus = new Torus(position1, torus_r, torus_r_axial, mat1, nStep, nStep);
		ellipsoid = new Ellipsoid(position2, ellipsoid_r * 2, ellipsoid_r, ellipsoid_r, mat2, nStep, nStep);

		solids.add(sphere);
		solids.add(torus);
		solids.add(ellipsoid);
	}

	// initialize the solids of the second scene
	public void setScene1() {
		solids.clear();

		cylinder = new Cylinder(position0, cylinder_r * 1.5f, cylinder_r, cylinder_r, mat1, nStep, nStep);
		box = new Box(position1, box_edge, mat2, nStep, nStep);
		sphere = new Sphere(position2, sphere_r, mat3, nStep, nStep);

		solids.add(cylinder);
		solids.add(box);
		solids.add(sphere);
	}

	// initialize the solids of the third scene
	public void setScene2() {
		solids.clear();

		super_ellipsoid = new SuperEllipsoid(position0, ellipsoid_r * 1.5f, ellipsoid_r, ellipsoid_r, mat2, nStep, nStep);
		super_toroid = new SuperToroid(position1, torus_r, torus_r_axial, mat3, nStep, nStep);
		torus = new Torus(position2, torus_r, torus_r_axial, mat4, nStep, nStep);

		solids.add(super_ellipsoid);
		solids.add(super_toroid);
		solids.add(torus);
	}

	// initialize the solids of the forth scene
	public void setScene3() {
		solids.clear();
		sphere = new Sphere(position3, sphere_r * 2, mat3, nStep, nStep);
		solids.add(sphere);
	}

	// draw solids of a scene
	public void drawScene(String drawType) {
		PA4.clearPixelBuffer();
		for (int i = 0; i < solids.size(); i++) {
			Object3D obj = solids.get(i);
			obj.draw(lights, view_vector, drawType);
		}
	}

	// switch between different scenes
	public void switchScene(int num) {

		switch (num) {
		case 0:
			setScene0();
			break;
		case 1:
			setScene1();
			break;
		case 2:
			setScene2();
			break;
		case 3:
			setScene3();
			break;
		default:
			setScene0();
			break;
		}
	}

	// toggle diffuse effect
	/* toggle the diffuse effect */
	public void toggleDiffuse(boolean isDiffuse) {
		for (int i = 0; i < solids.size(); i++) {
			solids.get(i).toggleDiffuse(isDiffuse);
		}
	}

	// toggle specular effect
	/* toggle the specular effect */
	public void toggleSpecular(boolean isSpecular) {
		for (int i = 0; i < solids.size(); i++) {
			solids.get(i).toggleSpecular(isSpecular);
		}
	}

	// toggle ambient light
	/* toggle the ambient effect */
	public void toggleAmbient(boolean isAmbient) {
		for (int i = 0; i < solids.size(); i++) {
			solids.get(i).toggleAmbient(isAmbient);
		}
	}

	// toggle infinite light, point light or spot light
	public void toggleLight(int index) {
		lights = new ArrayList<Light>();
		light_toggles[index] = !light_toggles[index];
		System.out.println(lights_array[index].getName() + " = " + (light_toggles[index] ? "on" : "off"));
		for (int i = 0; i < light_toggles.length; i++) {
			if (light_toggles[i]) {
				lights.add(lights_array[i]);
			}
		}
	}

	// translate the camera
	public void translateCamera(int x, int y, int z) {
		translateLights(-x, -y, -z);
		translateAllSolids(-x, -y, -z);
		translateView(-x, -y, -z);
	}

	// translate all the solids
	public void translateAllSolids(int x, int y, int z) {
		for (int i = 0; i < solids.size(); i++) {
			solids.get(i).translate(x, y, z);
		}
	}

	// translate the select objects
	public void translateObject(int x, int y, int z) {
		Object3D obj = solids.get(selectedObjIndex);
		obj.translate(x, y, z);
		translateView(-x, -y, -z);
	}

	// translate the positions of the lights
	public void translateLights(int x, int y, int z) {
		for (int i = 0; i < lights.size(); i++) {
			Light light = lights.get(i);
			light.translate(x, y, z);
		}
	}

	// translate the view_vector
	public void translateView(int x, int y, int z) {
		this.view_vector.x += x;
		this.view_vector.y += y;
		this.view_vector.z += z;
	}

	// rotate the camera
	public void rotateCam(Quaternion q) {
		for (int i = 0; i < solids.size(); i++) {
			Object3D obj = solids.get(i);
			obj.rotate(q, new Vector(100.0f, 0.0f, 1500.0f));
		}
		rotateLights(q);
	}

	// rotate the camera
	public void rotateAllSolids(Quaternion q, Vector view_center) {
		for (int i = 0; i < solids.size(); i++) {
			solids.get(i).rotate(q, view_center);
		}
	}

	// rotate the selected objects
	public void rotateObject(Quaternion q) {
		Object3D obj = solids.get(selectedObjIndex);
		obj.rotate(q, obj.getCenter());
	}

	// rotate the position and direction of the lights
	public void rotateLights(Quaternion q) {
		for (int i = 0; i < lights.size(); i++) {
			Light light = lights.get(i);
			light.rotate(q, new Vector(100.0f, 0.0f, 1500.0f));
		}
	}

	// change the selected objects
	public void changeSelectedObj() {
		selectedObjIndex = (selectedObjIndex + 1) % solids.size();
	}

	// scale the selected objects
	public void scaleObject(float factor) {
		Object3D obj = solids.get(selectedObjIndex);
		obj.scale(factor);
	}

	// increase the material values
	public void materialUp() {
		for (int i = 0; i < materials.size(); i++) {
			Material mat = materials.get(i);
			mat.ka.r = mat.ka.r >= 1.0f ? 1.0f : mat.ka.r + 0.1f;
			mat.kd.r = mat.kd.r >= 1.0f ? 1.0f : mat.kd.r + 0.1f;
			mat.ks.r = mat.ks.r >= 1.0f ? 1.0f : mat.ks.r + 0.1f;
		}
	}

	// decrease the material values
	public void materialDown() {
		for (int i = 0; i < materials.size(); i++) {
			Material mat = materials.get(i);
			mat.ka.r = mat.ka.r <= 0.0f ? 0.0f : mat.ka.r - 0.1f;
			mat.kd.r = mat.kd.r <= 0.0f ? 0.0f : mat.kd.r - 0.1f;
			mat.ks.r = mat.ks.r <= 0.0f ? 0.0f : mat.ks.r - 0.1f;
		}
	}

}
