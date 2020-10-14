package models.displays;

import lombok.Getter;

@Getter
public class GridCell {
  int x, y;
  int height, width;

  public GridCell(int row, int column, int cellSize) {
    y = row*cellSize;
    x = column*cellSize;
    height = cellSize;
    width = cellSize;
  }
}
