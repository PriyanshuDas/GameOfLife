package models.patterns;

import com.google.common.collect.ImmutableList;
import java.util.List;
import models.grid.GridLocation;

public class GospelGun implements Pattern{
  public static int rowSize = 40;
  public static int colSize = 40;

  private static List<String> pattern = ImmutableList.of(
      "00000000000000000000000000000000000000",
      "00000000000000000000000001000000000000",
      "00000000000000000000000101000000000000",
      "00000000000001100000011000000000000110",
      "00000000000010001000011000000000000110",
      "01100000000100000100011000000000000000",
      "01100000000100010110000101000000000000",
      "00000000000100000100000001000000000000",
      "00000000000010001000000000000000000000",
      "00000000000001100000000000000000000000",
      "00000000000000000000000000000000000000");

  @Override
  public List<GridLocation> getPattern() {
//    return PatternUtils.convertStringToPattern(pattern);
    return PatternUtils.convertStringToPattern(
        PatternUtils.getRandomRotation(pattern));
  }
}
