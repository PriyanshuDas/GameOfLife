package models.rulesets;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import models.interfaces.IBoard;
import models.interfaces.IBoardConfig;
import models.interfaces.ICell;
import models.interfaces.ICell.CellState;

public class ClassicRuleset implements GameOfLifeRuleset {

  @Override
  public Collection<ICell> updateState(IBoard board) {
    Collection<ICell> cellsToUpdate = getCellsToUpdate(board);
    board.updateCells(cellsToUpdate);
    return cellsToUpdate;
  }

  @Override
  public CellState getNewState(ICell cell, IBoard board) {
    int liveNeighbours = (int) board.getAdjacentCells(cell).stream()
        .filter(neighbourCell -> neighbourCell.getState().equals(CellState.ALIVE)).count();
    return cell.getState() == CellState.ALIVE ?
        getAliveCellNewState(liveNeighbours)
        : getDeadCellNewState(liveNeighbours);
  }

  @Override
  public Collection<ICell> getCellsToUpdate(IBoard board) {
    return board.getLastUpdatedLocations()
        .parallelStream()
        .map(board::getCellAt)
        .map(cell -> cell.getCellAndNeighbors(board))
        .flatMap(Collection::parallelStream)
        .distinct()
        .filter(cell -> !getNewState(cell, board).equals(cell.getState()))
        .collect(Collectors.toList());
  }

  @Override
  public void initializeBoardState(IBoard board, IBoardConfig boardConfig) {

  }

  CellState getDeadCellNewState(int liveNeighbours) {
    return liveNeighbours == 3 ? CellState.ALIVE : CellState.DEAD;
  }

  CellState getAliveCellNewState(int liveNeighbours) {
    return (liveNeighbours == 2 || liveNeighbours == 3) ? CellState.ALIVE : CellState.DEAD;
  }
}
