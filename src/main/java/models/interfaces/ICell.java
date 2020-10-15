package models.interfaces;

import java.util.List;

public interface ICell {
    public enum CellState {DEAD, ALIVE};
    CellState getState();
    public void updateState(CellState newState);
    public IBoardLocation getLocation();
    public List<ICell> getCellAndNeighbors(IBoard board);
}
