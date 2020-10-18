package models.grid;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import models.interfaces.GridCoordinate;
import models.interfaces.IAdjacentAwareCell;


public class CellV2Adjacent extends Cell implements IAdjacentAwareCell {
  private AtomicInteger adjacentAliveNeighbours;
  CellV2Adjacent(GridLocation gridLocation, CellState cellState) {
    super(gridLocation, cellState);
    adjacentAliveNeighbours = new AtomicInteger(0);
  }
  @Override
  public void addAliveNeighbour() {
    adjacentAliveNeighbours.getAndIncrement();
  }
  @Override
  public void subAliveNeighbour() {
    adjacentAliveNeighbours.getAndDecrement();
  }

  @Override
  public int getAdjacentAliveNeighbours() {
    return adjacentAliveNeighbours.get();
  }

  @Override
  public boolean equals(Object o) {
    return super.equals(o);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
