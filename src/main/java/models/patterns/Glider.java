package models.patterns;

import com.google.common.collect.ImmutableList;
import java.util.List;
import models.grid.GridLocation;

public class Glider implements Pattern{
  public static List<GridLocation> aliveCells = ImmutableList.of(
      new GridLocation(0, 1),
      new GridLocation(1, 2),
      new GridLocation(2, 0),
      new GridLocation(2, 1),
      new GridLocation(2, 2)
  );
  public List<GridLocation> getPattern() {
    return aliveCells;
  }
}
