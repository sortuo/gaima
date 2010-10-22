package fractal;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import fractal.Fractal.Rules;

public class BuildFractal {

	public static Image i;
	public static BufferedImage bi;
	public static int width = 10000;
	public static int height = 10000;
	public static int wOffset = 1;
	public static int hOffset = 1;
	private int pix[];
	private static Collection<Rules> variablesCollection = new ArrayList<Rules>();
	private static String imgName = "default_fractal";
	private File file;
	private static Logger log = Logger.getLogger("fractal.buildfractal");
	private static FileHandler fh;

	public static void main(String[] args) throws Exception {
		// Append to logger.
		fh = new FileHandler("fractal.log", true);
		log.addHandler(fh);
		// Run arguments setup
		setup(args);
		new BuildFractal();
	}

	public static void setup(String[] args) {
		long now = System.currentTimeMillis();
		String allRules = "";
		for (Rules r : Rules.values()) {
			allRules += " " + r.toString();
		}
		try {
			if (args.length > 0) {
				width = Integer.parseInt((args[0]));
				height = Integer.parseInt((args[1]));
				imgName = args[2] + "_" + now;
				// Iterate rule-arguments and add them to list
				for (int i = 3; i < args.length; i++) {
					variablesCollection.add(Rules.valueOf(args[i]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("Usage: BuildFractal [width] [height] [image name] [List of rules:"
							+ allRules + " ...]");
			System.out
					.println("Using defaults: BuildFractal [10000] [10000] [default_fractal]");
		}
		String runmessage = "";
		for (String s : args) {
			runmessage += " [" + s + "]";
		}

		log.log(Level.INFO, "Run parameters" + runmessage + " [" + now + "]");

	}

	public BuildFractal() {

		pix = new int[width * height];
		int index = 0;
		for (int y = hOffset; y < height + hOffset; y++) {
			for (int x = wOffset; x < width + wOffset; x++) {
				pix[index] = Fractal.countVariable(x, y, 0xFF000000,
						variablesCollection);
				// pix[index] = Fractal.countDrippingMatrix(x, y);
				index++;
			}
			if (y % 1000 == 0)
				System.out.println("Lines " + y);
		}

		System.gc();

		i = Toolkit.getDefaultToolkit().createImage(
				new MemoryImageSource(width, height, pix, 0, width));
		bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		bi.getGraphics().drawImage(i, 0, 0, null);

		Frame frame = new Frame("Fractal");
		frame.add(new DrawFractal(this));
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setSize(Toolkit.getDefaultToolkit().getScreenSize().width / 2,
				Toolkit.getDefaultToolkit().getScreenSize().height / 2);
		frame.setVisible(true);

		file = new File(imgName + ".jpg");
		try {
			ImageIO.write(bi, "jpg", file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (file.canRead() == false) {

		}
		//System.exit(0);

	}

	public void draw(Graphics g) {
		g.drawImage(i, 1, 1, null);
	}

}

class DrawFractal extends Canvas {

	private static final long serialVersionUID = 1L;
	BuildFractal fractal;

	public DrawFractal(BuildFractal t) {
		this.fractal = t;
		// Add a listener for resize events
		addComponentListener(new ComponentAdapter() {
			// This method is called when the component's size changes
			public void componentResized(ComponentEvent evt) {
				Component c = (Component) evt.getSource();
				c.repaint();
			}
		});

	}

	public void paint(Graphics g) {
		fractal.draw(g);
	}

}
