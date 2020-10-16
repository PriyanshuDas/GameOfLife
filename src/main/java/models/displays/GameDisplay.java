package models.displays;

import java.util.Collection;
import java.util.List;
import models.interfaces.GeneralException;
import models.interfaces.GridCoordinate;
import models.interfaces.IBoard;

public interface GameDisplay {
  public void updateNextFrame(List<GridCoordinate> flippedPositions) throws GeneralException;
}
