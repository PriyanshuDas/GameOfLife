package models.interfaces;

import java.util.List;

public interface ICell {

  void flipState();

  enum CellState {DEAD, ALIVE}

  CellState getState();
    void updateState(CellState newState);
    IBoardLocation getLocation();
    List<ICell> getCellAndNeighbors(IBoard board);
}
