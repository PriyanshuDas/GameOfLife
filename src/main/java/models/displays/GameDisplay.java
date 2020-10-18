package models.displays;

import java.util.List;
import models.interfaces.GeneralException;
import models.interfaces.GridCoordinate;

public interface GameDisplay {
  void updateNextFrame(List<GridCoordinate> flippedPositions) throws GeneralException;
}
