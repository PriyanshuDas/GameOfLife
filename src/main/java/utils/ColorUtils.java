package utils;

import com.google.common.collect.ImmutableList;
import java.awt.Color;
import java.util.List;
import java.util.Random;
import models.patterns.RandomUtils;

public class ColorUtils {

  private static final int COLORS_COUNT = 3;
  public static Color green = new Color(34,139,34);
  public static Color cyan = Color.CYAN;
  public static Color red = new Color(240,128,128);
  public static Color yellow = new Color(255,255,153);
  private static List<Color> colors = ImmutableList.of(Color.white, yellow, red);

  public static Color randomColor() {
    return colors.get(RandomUtils.randomInt(0, colors.size()));
  }
}
