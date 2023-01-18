package hr.fer.oprpp2.hw04.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Class used to help working with colors.
 * 
 * @author Ana Bagić
 *
 */
public class ColorsUtil {

	/** Mapping of color names to their hex values. */
	private static final Map<String, String> colors = new HashMap<>();
	
	static {
		colors.put("gray", 	"444444");
		colors.put("purple", "4d1b7b");
		colors.put("pink", "ff0065");
		colors.put("red", "bf0000");
		colors.put("yellow", "ecb939");
		colors.put("green", "277c42");
		colors.put("blue", "005073");
	}
	
	/**
	 * @param color to get
	 * @return color hex value for the requested color
	 */
	public static String getColor(String color) {
		return colors.get(color);
	}
	
	/**
	 * @return random colour of the ones stored in this class
	 */
	public static String getRandColor() {
		Random random = new Random();
		int rand = random.nextInt(colors.size());
		return switch (rand) {
		case 0 -> colors.get("gray");
		case 1 -> colors.get("purple");
		case 2 -> colors.get("pink");
		case 3 -> colors.get("red");
		case 4 -> colors.get("yellow");
		case 5 -> colors.get("green");
		case 6 -> colors.get("blue");
		default -> null;
		};
	}

}
