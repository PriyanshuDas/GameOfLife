package models.rulesets;

import java.util.Collection;
import java.util.List;
import models.interfaces.IBoard;
import models.interfaces.IBoardConfig;
import models.interfaces.ICell;
import models.interfaces.ICell.CellState;

public interface GameOfLifeRuleset {
  Collection<ICell> updateState(IBoard board);
  CellState getNewState(ICell cell, IBoard board);
  Collection<ICell> getCellsToUpdate(IBoard board);

  void initializeBoardState(IBoard board, IBoardConfig boardConfig);
}
