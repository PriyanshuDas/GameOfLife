package utils;

import com.google.common.collect.ImmutableList;
import java.awt.Color;
import java.util.List;
import models.patterns.RandomUtils;

public class ColorUtils {

  private static final List<Color> CLASSIC = ImmutableList.of(
      Color.white,
      new Color(255, 255, 153),  // light yellow
      new Color(240, 128, 128)   // light red
  );

  private static final List<Color> MATRIX = ImmutableList.of(
      new Color(0, 255, 0),      // bright green
      new Color(0, 180, 0),      // medium green
      new Color(0, 100, 0)       // dark green
  );

  private static final List<Color> FIRE = ImmutableList.of(
      new Color(220, 20, 20),    // red
      new Color(255, 140, 0),    // orange
      new Color(255, 255, 0)     // yellow
  );

  private static final List<Color> ICE = ImmutableList.of(
      Color.CYAN,
      Color.white,
      new Color(173, 216, 230)   // light blue
  );

  private static List<Color> activeTheme = CLASSIC;

  public static void setTheme(String theme) {
    switch (theme) {
      case "matrix":  activeTheme = MATRIX;  break;
      case "fire":    activeTheme = FIRE;    break;
      case "ice":     activeTheme = ICE;     break;
      default:        activeTheme = CLASSIC; break;
    }
  }

  public static Color randomColor() {
    return activeTheme.get(RandomUtils.randomInt(0, activeTheme.size()));
  }
}
