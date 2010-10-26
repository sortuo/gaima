package fractal;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Fractals are created with this Fractal class and different rules are stated
 * with Enum Rules or by calling methods. You can combine different rules with
 * the proram parameters. 0xFF000000 is the base blue color for images.
 * 
 * Mandelbrot code is based on pseudocode located here:
 * http://en.wikipedia.org/wiki/Mandelbrot_set
 * 
 * @author sortuo
 * 
 */
public class Fractal {

	/**
	 * Rules for fractal generation.
	 */
	public enum Rules {
		ABS, SQRT, TAN, COS, TEST1, TEST2, TEST3, TEST4, DRIPPINGMATRIX, MANDELBROT, MANDELBROT_MOD1, MANDELBROT_BLACKLAGOON
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

	/**
	 * Calculates the results for a collection of different rules.
	 * 
	 * @param x
	 *            image x pixel
	 * @param y
	 *            image y pixel
	 * @param offset
	 *            the base offset (base color) of the calculation
	 * @param variables
	 *            collection of different Rules
	 * @return returns the calculated int value (color) calculated by the rules
	 */
	public static int countVariable(int x, int y, int offset,
			Collection<Rules> variables) {
		int result = 0;
		for (Rules rule : variables) {
			result += countWithEnum(x, y, rule, offset);
		}
		return offset + result;
	}

	/**
	 * Calculates specified Rule for pixel and return the color value.
	 * 
	 * @param x
	 *            image x pixel
	 * @param y
	 *            image y pixel
	 * @param r
	 *            enumeration of specifien Rule
	 * @param offset
	 *            the base offset (base color) of the calculation
	 * @return returns the calculated int value (color) of calculation
	 */
	public static int countWithEnum(int x, int y, Rules r, int offset) {
		int result = offset;
		switch (r) {
		case SQRT:
			result += (int) (Math.sqrt(x) + Math.sqrt(y));
			break;
		case ABS:
			result += Math.abs(x) + Math.abs(y);
			break;
		case TAN:
			result += (int) (Math.tan(x) + Math.tan(y));
			break;
		case COS:
			result += (int) (Math.cos(x) + Math.cos(y));
			break;
		case TEST1:
			result += (int) Math.max(x, y) - Math.min(y, x);
			break;
		case DRIPPINGMATRIX:
			result += (int) (Math
					.abs((Math.tan(x) * 1.02 * Math.sqrt(x) * 1.02)
							* Math.sqrt(y)));
			break;
		case TEST3:
			result += 4 * (x * y / (x + y));
			break;

		case MANDELBROT:
			// double x0 = -2.5 + x*0.005; //3.5
			// double y0 = -1+ y*0.006; //2
			double x0 = -2 + (x / (BuildFractal.width * 0.37)); // 3.5 total
			// range
			double y0 = -1.2 + (y / (BuildFractal.height * 0.4)); // 2 total
			// range

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
				result += iteration;
			}

			break;

		case MANDELBROT_MOD1:
			int blue = Color.blue.getRGB();
			int green = Color.green.getRGB();

			result = green / 2;
			x0 = -2 + (x / (BuildFractal.width * 0.37)); // 3.5 total
			// range
			y0 = -1.2 + (y / (BuildFractal.height * 0.4)); // 2 total
			// range

			xx = 0;
			yy = 0;

			iteration = 0;
			max_iteration = 1000;

			while (xx * xx + yy * yy <= (2 * 2) && iteration < max_iteration) {
				double xtemp = Math.sqrt(xx * yy) - Math.sqrt(yy * xx) + x0;
				yy = 2 * xx / 2 * yy + y0;

				xx = xtemp;

				iteration = iteration + 1;
			}

			if (iteration == max_iteration) {
				result = 0;
			} else {
				result += iteration;
			}

			break;

		case MANDELBROT_BLACKLAGOON:
			result = 0xFF000000;
			x0 = -2 + (x / (BuildFractal.width * 0.37)); // 3.5 total
			// range
			y0 = -1.2 + (y / (BuildFractal.height * 0.4)); // 2 total
			// range

			xx = 0;
			yy = 0;

			iteration = 0;
			max_iteration = 1000;

			while (xx * xx + yy * yy <= (2 * 2) && iteration < max_iteration) {
				double xtemp = xx * xx - yy * yy + x0;
				yy = 2 * xx / 2 * yy + y0;

				xx = xtemp;

				iteration = iteration + 1;
			}

			if (iteration == max_iteration) {
				result = 0;
			} else {
				result += iteration;
			}

			break;

		case TEST4:
			blue = Color.blue.getRGB();
			green = Color.green.getRGB();
			red = Color.red.getRGB();

			result = blue;
			x0 = -2 + (x / (BuildFractal.width * 0.37)); // 3.5 total
			// range
			y0 = -1.2 + (y / (BuildFractal.height * 0.4)); // 2 total
			// range

			xx = 0;
			yy = 0;

			iteration = 0;
			max_iteration = 1000;

			result += (int) Math.sqrt(x * y) * (y);

			break;

		default:
			break;
		}
		return result;
	}

	// OLD TESTS before refactoring.
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