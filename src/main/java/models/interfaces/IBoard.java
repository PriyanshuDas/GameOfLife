package models.interfaces;

import java.util.List;
import java.util.Set;

public interface IBoard {
    public void initialize(IBoardConfig boardConfig) throws GeneralException;

  List<ICell> getAdjacentCells(ICell cell) throws GeneralException ;
  List<ICell> getAliveCells();
  public void setAliveCells(List<IBoardLocation> positions);
  public void setDeadCells(List<IBoardLocation> positions);
  public Set<IBoardLocation> getNewlyAliveLocations();
  public Set<IBoardLocation> getNewlyDeadLocations();
  public ICell getCellAt(IBoardLocation boardLocation);
}
