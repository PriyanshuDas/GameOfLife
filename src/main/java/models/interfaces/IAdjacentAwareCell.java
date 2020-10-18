package models.interfaces;

import models.interfaces.ICell.CellState;

public interface IAdjacentAwareCell {
  void addAliveNeighbour();
  void subAliveNeighbour();
  int getAdjacentAliveNeighbours();
  CellState getState();
}
