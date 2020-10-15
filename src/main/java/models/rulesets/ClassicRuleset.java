package models.rulesets;

import java.util.List;
import models.interfaces.GeneralException;
import models.interfaces.IBoard;
import models.interfaces.ICell;
import models.interfaces.ICell.CellState;

public class ClassicRuleset implements GameOfLifeRuleset {

  @Override
  public CellState getNewState(ICell cell, IBoard board) {
    try {
      List<ICell> adjacentCells = board.getAdjacentCells(cell);
      int liveNeighbours = (int) adjacentCells.stream()
          .filter(neighbourCell -> neighbourCell.getState().equals(CellState.ALIVE)).count();
      return cell.getState() == CellState.ALIVE ?
          getAliveCellNewState(liveNeighbours)
          : getDeadCellNewState(liveNeighbours);
    } catch (GeneralException e) {
      e.printStackTrace();
      return null;
    }
  }

  private CellState getDeadCellNewState(int liveNeighbours) {
    if (liveNeighbours == 3) {
      return CellState.ALIVE;
    }
    else {
      return CellState.DEAD;
    }
  }

  private CellState getAliveCellNewState(int liveNeighbours) {

    if (liveNeighbours == 2 || liveNeighbours == 3) {
      return CellState.ALIVE;
    }
    else {
      return CellState.DEAD;
    }
  }
}