
// ****************************************************************************
// Example Main Program for CS480 PA1
// ****************************************************************************
// Description:
//
// This is a template program for the sketching tool.
//
// LEFTMOUSE: draw line segments
// RIGHTMOUSE: draw triangles
//
// The following keys control the program:
//
// Q,q: quit
// C,c: clear polygon (set vertex count=0)
// R,r: randomly change the color
// S,s: toggle the smooth shading for triangle
// (no smooth shading by default)
// T,t: show testing examples
// >: increase the step number for examples
// <: decrease the step number for examples
//
// ****************************************************************************
// History :
// Aug 2004 Created by Jianming Zhang based on the C
// code by Stan Sclaroff
// Nov 2014 modified to include test cases for shading example for PA4
// Nov 2015 modified by Hengbin Liao: Shaded Rendering and Depth-Buffering implemented
//

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;

public class PA4 extends JFrame implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_WINDOW_WIDTH = 1024;
	public static final int DEFAULT_WINDOW_HEIGHT = 1024;
	public static final float DEFAULT_LINE_WIDTH = 1.0f;

	private GLCapabilities capabilities;
	private GLCanvas canvas;
	private FPSAnimator animator;

	private int testCase;
	public static BufferedImage buff;
	public static BufferedImage env_texture;
	public static BufferedImage bv_texture;
	public static BufferedImage bu_texture;
	// public static BufferedImage bu_texture;
	// public static BufferedImage bv_texture;
	public static BufferedImage bump_texture;
	@SuppressWarnings("unused")
	private ColorType color;
	private Random rng;

	// specular exponent for materials
	private int ns = 5;

	public static DepthBuffer depthBuffer;
	private ArrayList<Point> lineSegs;
	private ArrayList<Point> triangles;
	private boolean doSmoothShading;
	private int Nsteps;

	/** The quaternion which controls the rotation of the world. */
	private Quaternion viewing_quaternion = new Quaternion();
	private Vector viewing_center = new Vector((float) (DEFAULT_WINDOW_WIDTH / 2), (float) (DEFAULT_WINDOW_HEIGHT / 2), (float) 0.0);
	/** The last x and y coordinates of the mouse press. */
	private int last_x = 0, last_y = 0;
	/** Whether the world is being rotated. */
	private boolean rotate_world = false;

	private boolean isSpecular = true;
	private boolean isDiffuse = true;
	private boolean isAmbient = true;

	private String drawType = "Flat";
	private boolean isCamRotate = false;
	// private boolean toggleCameraOrOb = true;
	private boolean isObjectTransform = false;
	private int sceneNum = 0;
	private Scene scene;

	public PA4() {
		capabilities = new GLCapabilities(null);
		capabilities.setDoubleBuffered(true);  // Enable Double buffering

		canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addKeyListener(this);
		canvas.setAutoSwapBufferMode(true); // true by default. Just to be explicit
		canvas.setFocusable(true);
		getContentPane().add(canvas);

		animator = new FPSAnimator(canvas, 60);

		setTitle("CS480/680 Lab 11");
		setSize(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
		// display the window in the center of the screen
		setLocationRelativeTo(null);
		// canvas gets focus whenever frame is activated
		addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent e) {
				canvas.requestFocusInWindow();
			}
		});

		rng = new Random();
		color = new ColorType(1.0f, 0.0f, 0.0f);
		lineSegs = new ArrayList<Point>();
		triangles = new ArrayList<Point>();
		depthBuffer = new DepthBuffer(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
		doSmoothShading = false;
		scene = new Scene();
	}

	public void run() {
		animator.start();
	}

	public static void main(String[] args) {
		PA4 pa4 = new PA4();
		pa4.run();
	}

	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glLineWidth(DEFAULT_LINE_WIDTH);
		Dimension dimension = this.getContentPane().getSize();
		buff = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_3BYTE_BGR);
		clearPixelBuffer();

		try {
			env_texture = ImageIO.read(new File("env.jpg"));
		} catch (IOException e) {
			System.out.println("Error: reading texture image.");
			e.printStackTrace();
		}

		try {
			bump_texture = ImageIO.read(new File("bump.jpg"));
		} catch (IOException e) {
			System.out.println("Error: reading texture image.");
			e.printStackTrace();
		}

		try {
			bu_texture = ImageIO.read(new File("bu.jpg"));
		} catch (IOException e) {
			System.out.println("Error: reading texture image.");
			e.printStackTrace();
		}

		try {
			bv_texture = ImageIO.read(new File("bv.jpg"));
		} catch (IOException e) {
			System.out.println("Error: reading texture image.");
			e.printStackTrace();
		}

		scene = new Scene();
		scene.setScene0();
	}

	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		WritableRaster wr = buff.getRaster();
		DataBufferByte dbb = (DataBufferByte) wr.getDataBuffer();
		byte[] data = dbb.getData();

		gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);
		gl.glDrawPixels(buff.getWidth(), buff.getHeight(), GL2.GL_BGR, GL2.GL_UNSIGNED_BYTE, ByteBuffer.wrap(data));
		scene.drawScene(drawType);
	}

	// Window size change
	public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
		// deliberately left blank
	}

	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
		// deliberately left blank
	}

	public static void clearPixelBuffer() {
		Graphics2D g = buff.createGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, buff.getWidth(), buff.getHeight());
		g.dispose();
		depthBuffer.clearDepthBuffer();
	}

	public void mouseDragged(final MouseEvent mouse) {
		if (this.rotate_world) {
			// get the current position of the mouse
			final int x = mouse.getX();
			final int y = mouse.getY();

			// get the change in position from the previous one
			final int dx = x - this.last_x;
			final int dy = y - this.last_y;

			// create a unit vector in the direction of the vector (dy, dx, 0)
			final float magnitude = (float) Math.sqrt(dx * dx + dy * dy);
			if (magnitude > 0.0001) {
				// define axis perpendicular to (dx,-dy,0)
				// use -y because origin is in upper lefthand corner of the
				// window
				final float[] axis = new float[] { -(float) (dy / magnitude), (float) (dx / magnitude), 0 };

				// calculate appropriate quaternion
				final float viewing_delta = 3.1415927f / 180.0f * 2.0f;
				final float s = (float) Math.sin(0.5f * viewing_delta);
				final float c = (float) Math.cos(0.5f * viewing_delta);
				final Quaternion Q = new Quaternion(c, s * axis[0], s * axis[1], s * axis[2]);
				this.viewing_quaternion = Q.multiply(this.viewing_quaternion);

				// normalize to counteract accumulating round-off error
				this.viewing_quaternion.normalize();

				// save x, y as last x, y
				this.last_x = x;
				this.last_y = y;

				Q.normalize();
				if (isObjectTransform) {
					scene.rotateObject(Q);
				} else {
					if (isCamRotate) {
						scene.rotateCam(Q);
					} else {
						scene.rotateAllSolids(Q, viewing_center);
					}
				}
			}
		}
	}

	public void mouseMoved(MouseEvent e) {
		// UNIMPLEMENT ON PURPOSE
	}

	public void mouseClicked(MouseEvent e) {
		// UNIMPLEMENT ON PURPOSE
	}

	public void mousePressed(MouseEvent mouse) {
		int button = mouse.getButton();
		if (button == MouseEvent.BUTTON1) {
			last_x = mouse.getX();
			last_y = mouse.getY();
			rotate_world = true;
		}
	}

	public void mouseReleased(MouseEvent mouse) {
		int button = mouse.getButton();
		if (button == MouseEvent.BUTTON1) {
			rotate_world = false;
		}
	}

	public void mouseEntered(MouseEvent e) {
		// UNIMPLEMENT ON PURPOSE
	}

	public void mouseExited(MouseEvent e) {
		// UNIMPLEMENT ON PURPOSE
	}

	public void keyTyped(KeyEvent key) {
		switch (key.getKeyChar()) {

		// change the 5 scenes which to be drawn
		case 'T':
		case 't':
			scene.switchScene((++sceneNum) % 4);
			if (sceneNum % 4 == 3) {
				drawType = "Gouraud";
				scene.drawScene(drawType);
			}
			break;
		// quit
		case 'Q':
		case 'q':
			new Thread() {
				public void run() {
					animator.stop();
				}
			}.start();
			System.exit(0);
			break;
		// reset
		case 'R':
		case 'r':
			scene.nStep = 30;
			viewing_quaternion.reset();
			drawType = "Flat";
			scene.switchScene(sceneNum % 4);
			scene.drawScene(drawType);
			break;
		// Toggle Specular/Diffuse/Ambient
		case 'S':
		case 's':
			isSpecular = !isSpecular;
			scene.toggleSpecular(isSpecular);
			System.out.println("Specular = " + isSpecular);
			break;
		// toggle the diffuse term
		case 'D':
		case 'd':
			isDiffuse = !isDiffuse;
			scene.toggleDiffuse(isDiffuse);
			System.out.println("Diffuse = " + isDiffuse);
			break;
		// toggle the ambient term
		case 'A':
		case 'a':
			isAmbient = !isAmbient;
			scene.toggleAmbient(isAmbient);
			System.out.println("Ambient = " + isAmbient);
			break;
		// Change the render method to Flat/Gouraud/Phong
		case 'F':
		case 'f':
			if (drawType.equals("Environment Mapping") || drawType.equals("Bump Mapping")) {
				scene.nStep = 30;
			}
			drawType = "Flat";
			// scene.switchScene(sceneNum % 4);
			scene.drawScene(drawType);
			System.out.println("Draw Type = Flat");
			break;
		// toggle Gouraud shading
		case 'G':
		case 'g':
			if (drawType.equals("Environment Mapping") || drawType.equals("Bump Mapping")) {
				scene.nStep = 30;
			}
			drawType = "Gouraud";
			// scene.switchScene(sceneNum % 4);
			scene.drawScene(drawType);
			System.out.println("Draw Type = Gouraud");
			break;
		// toggle Phong shading
		case 'P':
		case 'p':
			if (drawType.equals("Environment Mapping") || drawType.equals("Bump Mapping")) {
				scene.nStep = 30;
			}
			drawType = "Phong";
			// scene.switchScene(sceneNum % 4);
			scene.drawScene(drawType);
			System.out.println("Draw Type = Phong");
			break;
		// toggle environment mapping
		case 'E':
		case 'e':
			drawType = "Environment Mapping";
			scene.nStep = 1500;
			scene.switchScene(sceneNum % 4);
			scene.drawScene(drawType);
			System.out.println("Draw Tpye = Env Mapping");
			break;
		// toggle bump mapping
		case 'B':
		case 'b':
			drawType = "Bump Mapping";
			scene.nStep = 20;
			scene.toggleLight(Scene.POINT_LIGHT);
			scene.toggleLight(Scene.SPOT_LIGHT);
			scene.switchScene(sceneNum % 4);
			scene.drawScene(drawType);
			System.out.println("Draw Tpye = Bump Mapping");
			break;
		// toggle infinite light
		case '1':
			scene.toggleLight(Scene.INFINITE_LIGHT);
			break;
		// toggle point light
		case '2':
			scene.toggleLight(Scene.POINT_LIGHT);
			break;
		// toggle spot light
		case '3':
			scene.toggleLight(Scene.SPOT_LIGHT);
			break;
		case '+':
		case '=':
			ns++;
			System.out.println("expNs = " + ns);
			break;
		case '-':
		case '_':
			if (ns > 0) {
				ns--;
			}
			System.out.println("expNs = " + ns);
			break;
		// control on single object
		case 'O':
		case 'o':
			isObjectTransform = !isObjectTransform;
			break;
		// toggle camera rotation
		case 'M':
		case 'm':
			isCamRotate = !isCamRotate;
			break;
		// change selected objects
		case '/':
			scene.changeSelectedObj();
			break;
		// decrease the mesh dimension
		case '[':
			scene.nStep = scene.nStep <= 20 ? scene.nStep : scene.nStep - 10;
			scene.switchScene(sceneNum % 4);
			scene.drawScene(drawType);
			System.out.println("nStep = " + scene.nStep);
			break;
		// incease the mesh dimension
		case ']':
			scene.nStep = scene.nStep >= 1280 ? scene.nStep : scene.nStep + 10;
			scene.switchScene(sceneNum % 4);
			scene.drawScene(drawType);
			System.out.println("nStep = " + scene.nStep);
			break;
		// decrease material
		case ';':
			scene.materialDown();
			scene.switchScene(sceneNum % 4);
			scene.drawScene(drawType);
			break;
		// add material
		case '\'':
			scene.materialUp();
			scene.switchScene(sceneNum % 4);
			scene.drawScene(drawType);
			break;
		// make the object smaller
		case ',':
			scene.scaleObject(0.9f);
			break;
		// make the object bigger
		case '.':
			scene.scaleObject(1.1f);
			break;
		default:
			break;
		}
	}

	public void keyPressed(KeyEvent key) {
		switch (key.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			new Thread() {
				public void run() {
					animator.stop();
				}
			}.start();
			System.exit(0);
			break;
		case KeyEvent.VK_UP:
			if (isObjectTransform) {
				scene.translateObject(0, -5, 0);
			} else {
				scene.translateCamera(0, -5, 0);
			}
			break;
		case KeyEvent.VK_DOWN:
			if (isObjectTransform) {
				scene.translateObject(0, 5, 0);
			} else {
				scene.translateCamera(0, 5, 0);
			}
			break;
		case KeyEvent.VK_LEFT:
			if (isObjectTransform) {
				scene.translateObject(-5, 0, 0);
			} else {
				scene.translateCamera(5, 0, 0);
			}
			break;
		case KeyEvent.VK_RIGHT:
			if (isObjectTransform) {
				scene.translateObject(5, 0, 0);
			} else {
				scene.translateCamera(-5, 0, 0);
			}
			break;
		default:
			break;
		}
	}

	public void keyReleased(KeyEvent e) {
		// UNIMPLEMENT ON PURPOSE
	}

	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
	}

}
