package models.interfaces;

public interface ICell {
    public enum CellState {DEAD, ALIVE};
    CellState getState();
    public void updateState(CellState newState);
    public IBoardLocation getLocation();
}
