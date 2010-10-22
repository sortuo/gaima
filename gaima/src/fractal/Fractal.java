package fractal;

import java.util.ArrayList;
import java.util.Collection;

public class Fractal {

	public enum Rules {
		ABS, SQRT, TAN, COS, TEST1, TEST2, TEST3, DRIPPINGMATRIX, MANDELBROT
	}

	public static int countStep(int x, int y) {
		return 0xFF000000 + x + y;
	}

	public static int countRandom(int x, int y) {
		return 0x00000FF000000 + (int) (Math.random() * 255);
	}

	public static int countTest(int x, int y) {
		return 0xFF000000 + (int) Math.sqrt(x * y) * (y);
	}

	public static int countDrippingMatrix(int x, int y) {
		return 0xFF000000 + (int) (Math
				.abs((Math.tan(x) * 1.02 * Math.sqrt(x) * 1.02) * Math.sqrt(y)));
	}

	public static int countCheckboards(int x, int y) {
		Collection<Rules> c = new ArrayList<Rules>();
		c.add(Rules.TAN);
		c.add(Rules.SQRT);
		return Fractal.countVariable(x, y, 0xFF000000, c);
	}

	public static int countVariable(int x, int y, int offset,
			Collection<Rules> variables) {
		int result = 0;
		for (Rules rank : variables) {
			result += countWithEnum(x, y, rank);
		}
		return offset + result;
	}

	public static int countWithEnum(int x, int y, Rules r) {
		int result = 0;
		switch (r) {
		case SQRT:
			result = (int) (Math.sqrt(x) + Math.sqrt(y));
			break;
		case ABS:
			result = Math.abs(x) + Math.abs(y);
			break;
		case TAN:
			result = (int) (Math.tan(x) + Math.tan(y));
			break;
		case COS:
			result = (int) (Math.cos(x) + Math.cos(y));
			break;
		case TEST1:
			result = (int) Math.max(x, y) - Math.min(y, x);
			break;
		case DRIPPINGMATRIX:
			result = (int) (Math.abs((Math.tan(x) * 1.02 * Math.sqrt(x) * 1.02)
					* Math.sqrt(y)));
			break;
		case TEST3:
			result = 4 * (x * y / (x + y));
			break;

		case MANDELBROT:
			// double x0 = -2.5 + x*0.005; //3.5
			// double y0 = -1+ y*0.006; //2
			double x0 = -2 + (x / (BuildFractal.width * 0.37)); // 3.5 total range
			double y0 = -1.2 + (y / (BuildFractal.height * 0.4)); // 2 total range 

			double xx = 0;
			double yy = 0;

			int iteration = 0;
			int max_iteration = 1000;

			while (xx * xx + yy * yy <= (2 * 2) && iteration < max_iteration) {
				double xtemp = xx * xx - yy * yy + x0;
				yy = 2 * xx * yy + y0;

				xx = xtemp;

				iteration = iteration + 1;
			}

			if (iteration == max_iteration) {
				result = 0;
			} else {
				result = iteration;
			}

			break;

		default:
			break;
		}
		return result;
	}
	// int red = (y * 255) / (h - 1);
	// int blue = (x * 255) / (w - 1);
	// pix[index++] = (255 << 24) | (red << 16) | blue;
	// pix[index++] = 0x00000FF000000 + (int)(Math.random ()*255);
	// Arabic?
	// pix[index++] = 0xFF000000+(int)Math.tan(x*y)*(y);
	// Smooth fractal
	// pix[index++] = 0xFF000000+(int)Math.sqrt(x*y)*(y);
	// pix[index++] = 0xFF000000+(int)Math.rint(x*y)*(y);
	// Falling stars
	// pix[index++] =
	// 0xFF000000+(int)(Math.abs((Math.tan(x)*1.02*Math.sqrt(x)*1.02)*Math.sqrt(y)));
	// PI triangle
	// pix[index++] =
	// 0xFF000000+(int)(((int)Math.PI*(x*10))&((int)Math.PI*(y*10)));
	// Sharp fractal
	// pix[index++] = 0xFF000000+(int)Math.IEEEremainder(x,y)*(x*y);
	// Boomerang
	// pix[index++] = 0xFF000000+(int)Math.ulp(x*y)*(x*y);
}
