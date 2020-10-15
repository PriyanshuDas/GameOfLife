package utils;

import com.google.common.collect.ImmutableList;
import java.awt.Color;
import java.util.List;
import java.util.Random;

public class ColorUtils {

  private static final int COLORS_COUNT = 3;
  public static Color green = new Color(34,139,34);
  public static Color cyan = Color.CYAN;
  public static Color yellow = new Color(255,255,153);
  private static List<Color> colors = ImmutableList.of(green, cyan, yellow);
  private static Random rng = new Random();

  public static Color randomColor() {
    return cyan;
  }
}
