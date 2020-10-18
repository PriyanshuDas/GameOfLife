package models.interfaces;

import java.util.Collection;
import java.util.List;

public interface IBoard {
    void initialize(IBoardConfig boardConfig) throws GeneralException;

  List<ICell> getAdjacentCells(ICell cell);
  Collection<IBoardLocation> getLastUpdatedLocations();
  ICell getCellAt(IBoardLocation boardLocation);

  void updateCells(Collection<ICell> cellsToFlip);

  Collection<IBoardLocation> getAliveCellsLocations();
}
