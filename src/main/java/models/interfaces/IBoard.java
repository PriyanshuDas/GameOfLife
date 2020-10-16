package models.interfaces;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IBoard {
    public void initialize(IBoardConfig boardConfig) throws GeneralException;

  List<ICell> getAdjacentCells(ICell cell);
  public Collection<IBoardLocation> getLastUpdatedLocations();
  public ICell getCellAt(IBoardLocation boardLocation);

  void updateCells(List<ICell> cellsToFlip);

  Collection<IBoardLocation> getAliveCellsLocations();
}
