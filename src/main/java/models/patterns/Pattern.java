package models.patterns;

import java.util.List;
import models.grid.GridLocation;

public interface Pattern {
  List<GridLocation> getPattern();
}
