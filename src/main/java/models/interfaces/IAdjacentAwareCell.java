package models.interfaces;

import models.interfaces.ICell.CellState;

public interface IAdjacentAwareCell {
  public void addAliveNeighbour();
  public void subAliveNeighbour();
  public int getAdjacentAliveNeighbours();
  public CellState getState();
}
