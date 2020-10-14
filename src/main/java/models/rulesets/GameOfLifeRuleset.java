package models.rulesets;

import models.interfaces.IBoard;
import models.interfaces.ICell;
import models.interfaces.ICell.CellState;

public interface GameOfLifeRuleset {
  public CellState getNewState(ICell cell, IBoard board);
}
