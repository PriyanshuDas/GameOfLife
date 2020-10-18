package models.grid;

import models.interfaces.IBoardLocation;
import models.interfaces.ICell;
import models.interfaces.ICell.CellState;

public interface ICellFactory {
  ICell buildCell(IBoardLocation location, CellState cellState);
}
