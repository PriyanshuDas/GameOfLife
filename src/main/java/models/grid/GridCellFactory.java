package models.grid;

import models.interfaces.IBoardLocation;
import models.interfaces.ICell;
import models.interfaces.ICell.CellState;

public class GridCellFactory implements ICellFactory {
  public static Cell buildCell(GridLocation gridLocation, CellState cellState) {
    return Cell.builder()
        .gridLocation(gridLocation)
        .cellState(cellState)
        .build();
  }

  @Override
  public ICell buildCell(IBoardLocation location, CellState cellState) {
    if (location == null || !location.getClass().equals(GridLocation.class)) {
      throw new RuntimeException("location not per specification when building cell!");
    }
    return buildCell((GridLocation) location, cellState);
  }
}
