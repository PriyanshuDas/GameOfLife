package models.patterns;

import com.google.common.collect.ImmutableList;
import java.util.List;
import models.grid.GridLocation;

public class PulsarGenerator implements Pattern{
  public static List<Integer> patternSpacingColumn = ImmutableList.of(7, 10);
  public static List<Integer> patternSpacingRow = ImmutableList.of(7, 11);
  List<String> pattern = ImmutableList.of(
      "111",
      "101",
      "101",
      "101",
      "111");

  @Override
  public List<GridLocation> getPattern() {
    return PatternUtils.convertStringToPattern(pattern);
  }
}
