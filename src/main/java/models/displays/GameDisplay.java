package models.displays;

import models.interfaces.GeneralException;
import models.interfaces.IBoard;

public interface GameDisplay {
  public void updateNextFrame(IBoard board) throws GeneralException;
}
