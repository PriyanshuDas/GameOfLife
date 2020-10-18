package models.grid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import models.interfaces.IBoard;
import models.interfaces.IBoardLocation;
import models.interfaces.ICell;
import models.interfaces.ICell.CellState;

public abstract class Grid implements IBoard {
  List<List<Cell>> cells;
  @Getter
  int rows, columns;

  @Override
  public List<ICell> getAdjacentCells(ICell cell) {
    if (!(cell instanceof Cell)) {
      return new ArrayList<>();
    }
    return GridLocation.getAdjacentDelta().stream()
        .filter(delta -> deltaInRange((Cell) cell, delta))
        .map(delta -> this.getCellAt(cell, delta))
        .collect(Collectors.toList());
  }

  private boolean deltaInRange(Cell cell, GridLocation delta) {
    return cell.getRow() + delta.getRow() >= 0 &&
        cell.getRow() + delta.getRow() < getRows() &&
        cell.getColumn() + delta.getColumn() >= 0 &&
        cell.getColumn() + delta.getColumn() < getColumns();
  }

  private ICell getCellAt(ICell cell, GridLocation delta) {
    int row = ((Cell)cell).getRow() + delta.getRow();
    int column = ((Cell)cell).getColumn() + delta.getColumn();
    return cells.get(row).get(column);
  }

  public Collection<IBoardLocation> getAliveCellsLocations() {
    return cells.parallelStream()
        .flatMap(Collection::parallelStream)
        .filter(cell -> cell.getCellState().equals(CellState.ALIVE))
        .map(Cell::getGridLocation)
        .collect(Collectors.toList());
  }

  public Cell getCellAt(IBoardLocation gridLocation) {
    return cells.get(((GridLocation) gridLocation).getRow())
        .get(((GridLocation) gridLocation).getColumn());
  }

}
