package models.patterns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import models.grid.GridLocation;

public class PatternUtils {

  public static List<GridLocation> convertStringToPattern(List<String> pattern) {
    List<GridLocation> aliveCells = new ArrayList<GridLocation>();
    for (int i = 0; i < pattern.size(); i++) {
      for (int j = 0; j < pattern.get(i).length(); j++) {
        if (pattern.get(i).charAt(j) == '1') {
          aliveCells.add(new GridLocation(i, j));
        }
      }
    }
    return aliveCells;
  }
  public static List<String> flipPattern(List<String> pattern, FlipType flipType) {
    switch (flipType) {
      case VERTICAL:
        return pattern.stream()
            .map(row -> new StringBuilder(row).reverse().toString())
            .collect(Collectors.toList());
      case HORIZONTAL:
        List<String> copiedPattern = pattern.stream().map(String::new).collect(Collectors.toList());
        Collections.reverse(copiedPattern);
        return copiedPattern;
      default:
        return pattern;
    }
  }
  public static List<String> rotatePattern(List<String> pattern) {
    List<String> newPattern = new ArrayList<>();
    for (int i = 0; i < pattern.get(0).length(); i++) {
      StringBuilder newString = new StringBuilder();
      for (int j = 0; j < pattern.size(); j++) {
        newString.append(pattern.get(j).charAt(i));
      }
      newPattern.add(newString.toString());
    }
    return newPattern;
  }

  public static List<String> getRotation(List<String> pattern, int rotationType) {
    switch (rotationType) {
      case 1:
        return rotatePattern(pattern);
      case 2:
        return flipPattern(pattern, FlipType.HORIZONTAL);
      case 3:
        return rotatePattern(flipPattern(pattern, FlipType.HORIZONTAL));
      case 4:
        return flipPattern(pattern, FlipType.VERTICAL);
      case 5:
        return rotatePattern(flipPattern(pattern, FlipType.VERTICAL));
      case 6:
        return flipPattern(flipPattern(pattern, FlipType.HORIZONTAL), FlipType.VERTICAL);
      case 7:
        return rotatePattern(
            flipPattern(flipPattern(pattern, FlipType.HORIZONTAL), FlipType.VERTICAL));
      default:
        return pattern.stream().map(String::new).collect(Collectors.toList());
    }
  }
  public static List<String> getRandomRotation(List<String> pattern) {
    return getRotation(pattern, RandomUtils.randomInt(0, 8));
  }
}