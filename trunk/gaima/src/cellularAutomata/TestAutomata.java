package cellularAutomata;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.imageio.ImageIO;


public class TestAutomata {

	public static Image i;
	
	public static BufferedImage bi;

	public static void main(String[] args) {
		new TestAutomata();
	}
	
	public TestAutomata(){
		GenerateBasicRules rule = new GenerateBasicRules();
		Set<int[]> calculateRules = rule.calculateRules(8, 256);
		for (Iterator<int[]> iterator = calculateRules.iterator(); iterator
				.hasNext();) {
			int[] bs = (int[]) iterator.next();
			for (int i = 0; i < bs.length; i++) {
				System.out.print(bs[i] + "=" + Long.toString(bs[i], 2) + ",");
			}
			System.out.println();
		}
		i = Toolkit.getDefaultToolkit().createImage(
				new MemoryImageSource(10, 10, new int[] { 
						0
						}, 0, 1));

		int w = 5000;
		int h = 5000;
		int pix[] = new int[w * h];
		int index = 0;
		for (int y = 0; y < h; y++) {
		    //int red = (y * 255) / (h - 1);
		    for (int x = 0; x < w; x++) {
			//int blue = (x * 255) / (w - 1);
			//pix[index++] = (255 << 24) | (red << 16) | blue;
			//pix[index++] = 0x00000FF000000 + (int)(Math.random ()*255);
			pix[index++] = 0x00000FF000000+x*y;
		    }
		}

		Image i2 = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(w, h, pix, 0, w));
		 i= i2;
			bi = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB);
			System.out.println("IMG save"+bi.getGraphics().drawImage(i, 0 , 0, null));

		Frame frame = new Frame("Cellular Automata Set");
		frame.add(new MyCanvas(this));
		frame.setSize(300, 200);
		frame.setVisible(true);
		
//		java.io.File f = new java.io.File("test.jpg");
        File file = new File("newimage.jpg");
//        BufferedImage bufferedImage = (BufferedImage) i;
        //bufferedImage.setData(i.);
        try {
			ImageIO.write(bi, "jpg", file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void draw(Graphics g) {
		g.drawImage(i, 1, 1, null);
		System.out.println("Draw "+i.toString());
	}

}

class MyCanvas extends Canvas {
	
	TestAutomata t;

	MyCanvas(TestAutomata t) {
		this.t=t;
		// Add a listener for resize events
		addComponentListener(new ComponentAdapter() {
			// This method is called when the component's size changes
			public void componentResized(ComponentEvent evt) {
				Component c = (Component) evt.getSource();

				// Get new size
				Dimension newSize = c.getSize();

				c.repaint();
			}
		});
	}

	public void paint(Graphics g) {
		t.draw(g);
	}

}
