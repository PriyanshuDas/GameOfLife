package models.grid;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import models.interfaces.GridCoordinate;
import models.interfaces.IBoard;
import models.interfaces.IBoardLocation;
import models.interfaces.ICell;

@Builder
@Getter
@EqualsAndHashCode
public class Cell implements ICell, GridCoordinate {
    private GridLocation gridLocation;
    private CellState cellState;

    @Override
    public void flipState() {
        updateState(this.getState() == CellState.ALIVE ? CellState.DEAD : CellState.ALIVE);
    }

    public CellState getState() {
        return cellState;
    }

    public void updateState(CellState newState) {
        cellState = newState;
    }

    public IBoardLocation getLocation() {
        return gridLocation;
    }

    public List<ICell> getCellAndNeighbors(IBoard board) {
            List<ICell> cellsToUpdate = new ArrayList<>(board.getAdjacentCells(this));
            cellsToUpdate.add(this);
            return cellsToUpdate;
    }

    @Override
    public int getRow() {
        return gridLocation.getRow();
    }

    @Override
    public int getColumn() {
        return gridLocation.getColumn();
    }
}
