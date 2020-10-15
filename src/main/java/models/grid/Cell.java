package models.grid;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import models.interfaces.IBoard;
import models.interfaces.IBoardLocation;
import models.interfaces.ICell;

@Builder
@Getter
@EqualsAndHashCode
public class Cell implements ICell {
    private GridLocation gridLocation;
    private CellState cellState;
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
}
