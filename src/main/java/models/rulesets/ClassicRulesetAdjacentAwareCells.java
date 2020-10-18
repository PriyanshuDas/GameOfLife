package models.rulesets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import models.grid.CellV2Adjacent;
import models.grid.GridConfig;
import models.grid.GridV3;
import models.interfaces.IAdjacentAwareCell;
import models.interfaces.IBoard;
import models.interfaces.IBoardConfig;
import models.interfaces.ICell;
import models.interfaces.ICell.CellState;

public class ClassicRulesetAdjacentAwareCells extends ClassicRuleset {

  @Override
  public Collection<ICell> updateState(IBoard board) {
    GridV3 grid = (GridV3) board;
    Collection<ICell> cellsToUpdate = getCellsToUpdate(grid);
    grid.getUpdateLocations().clear();
    updateCells(grid, cellsToUpdate);
    return cellsToUpdate;
  }

  private void updateCells(GridV3 grid, Collection<ICell> cellsToUpdate) {
    cellsToUpdate.parallelStream().forEach(iCell -> {
      iCell.flipState();
      updateAdjacentCellsCounter(iCell, grid);
    });
    List<CellV2Adjacent> nextCellsToUpdate = cellsToUpdate.parallelStream()
        .map(cell -> cell.getCellAndNeighbors(grid))
        .flatMap(Collection::stream)
        .map(cell -> (CellV2Adjacent) cell)
        .distinct()
        .collect(Collectors.toList());

    nextCellsToUpdate.stream()
        .filter(cell -> getNewState(cell) != cell.getState())
        .forEach(cell -> grid.getUpdateLocations().put(cell.getGridLocation(), cell));
  }

  @Override
  public CellState getNewState(ICell cell, IBoard board) {
    long aliveNeighbours = board.getAdjacentCells(cell).stream()
        .map(ICell::getState)
        .filter(state -> state.equals(CellState.ALIVE))
        .count();

    return cell.getState() == CellState.ALIVE ?
        getAliveCellNewState((int)aliveNeighbours)
        : getDeadCellNewState((int)aliveNeighbours);
  }

  private CellState getNewState(IAdjacentAwareCell cell) {
    int liveNeighbours = cell.getAdjacentAliveNeighbours();
    return cell.getState() == CellState.ALIVE ?
        getAliveCellNewState(liveNeighbours)
        : getDeadCellNewState(liveNeighbours);
  }

  @Override
  public Collection<ICell> getCellsToUpdate(IBoard board) {
    return new ArrayList<>(((GridV3) board).getUpdateLocations().values());
  }

  @Override
  public void initializeBoardState(IBoard board, IBoardConfig boardConfig) {
    GridV3 grid = (GridV3) board;
    GridConfig gridConfig = (GridConfig) boardConfig;
    List<ICell> cellsToFlip = gridConfig.getAliveCells().stream()
        .map(board::getCellAt)
        .collect(Collectors.toList());
    updateCells(grid, cellsToFlip);
  }

  private Collection<CellV2Adjacent> updateAdjacentCellsCounter(
      ICell cell, GridV3 grid) {
    if (cell.getState().equals(CellState.ALIVE)) {
      return grid.getAdjacentCells(cell).parallelStream()
          .map(adjCell -> (CellV2Adjacent) adjCell)
          .peek(CellV2Adjacent::addAliveNeighbour)
          .collect(Collectors.toList());
    } else {
      return grid.getAdjacentCells(cell).parallelStream()
          .map(adjCell -> (CellV2Adjacent) adjCell)
          .peek(CellV2Adjacent::subAliveNeighbour)
          .collect(Collectors.toList());
    }
  }

}
