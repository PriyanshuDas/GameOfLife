package models.displays;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import models.displays.DisplayCell.DisplayCellState;
import models.grid.Grid;
import models.grid.GridLocation;
import models.interfaces.GridCoordinate;
import models.interfaces.IBoardLocation;
import utils.ColorUtils;

public class GridDisplay implements GameDisplay {

  private static final String TITLE = "Game Of Life";
  int rows, columns;
  List<List<DisplayCell>> cells;
  ConcurrentHashMap<DisplayPosition, DisplayCell> cellsAlivePositions;
  GridRenderer gridRenderer;
  private Color borderColor;
  private Color backgroundColor;
  private Color cellColor;

  public void initialize(int rows, int columns, Collection<IBoardLocation> alivePositions, int width, int height) {
    this.rows = rows;
    this.columns = columns;
    borderColor = Color.black;
    backgroundColor = Color.black;
    gridRenderer = new GridRenderer(TITLE, width, height, this.rows, this.columns, backgroundColor);
    initializeCells();
    updateNextFrame(alivePositions.stream()
        .map(location -> (GridLocation)location).collect(Collectors.toList()));
  }

  private void initializeCells() {
    cells = IntStream.range(0, rows)
        .mapToObj(row -> IntStream.range(0, columns)
            .mapToObj(column -> new DisplayCell(
                ColorUtils.randomColor(), borderColor,
                new DisplayPosition(row, column), DisplayCellState.DEAD))
            .collect(Collectors.toList()))
        .collect(Collectors.toList());
    cellsAlivePositions = new ConcurrentHashMap<>();
  }

  @Override
  public void updateNextFrame(List<GridCoordinate> flipPositions) {
    flipPositions.parallelStream().forEach(position -> {
      DisplayCell displayCell = cells.get(position.getRow()).get(position.getColumn());
      displayCell.flipState();
      if (displayCell.getState().equals(DisplayCellState.ALIVE))
        cellsAlivePositions.put(displayCell.getPosition(), displayCell);
      else
        cellsAlivePositions.remove(displayCell.getPosition());
    });
  }

  private DisplayCell getDisplayCellAt(DisplayPosition location) {
    return cells.get(location.getRow()).get(location.getColumn());
  }

  public void render() {
    gridRenderer.render(cellsAlivePositions.values());
  }
}
