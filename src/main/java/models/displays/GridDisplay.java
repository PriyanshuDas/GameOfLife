package models.displays;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import models.grid.Grid;
import models.grid.GridLocation;
import models.interfaces.IBoard;

public class GridDisplay implements GameDisplay {

  private static final String TITLE = "Game Of Life";
  List<DisplayPosition> nextFrameAliveCells;
  GridRenderer gridRenderer;

  public void initialize(Grid grid, int width, int height) {
    nextFrameAliveCells = new ArrayList<>();
    gridRenderer = new GridRenderer(TITLE, width, height, grid.getRows(), grid.getColumns());
    populateNextFrameAliveCells(grid);
  }
  @Override
  public void updateNextFrame(IBoard board) {
    populateNextFrameAliveCells((Grid) board);
  }

  private void populateNextFrameAliveCells(Grid board) {
    Set<GridLocation> newlyDeadlocations = board.getNewlyDeadLocations()
        .stream().map(item -> (GridLocation) item).collect(Collectors.toSet());

    nextFrameAliveCells = nextFrameAliveCells.stream()
        .filter(cell ->
            !newlyDeadlocations.contains(new GridLocation(cell.getRow(), cell.getColumn())))
        .collect(Collectors.toList());

    nextFrameAliveCells.addAll(board.getNewlyAliveLocations().stream()
        .map(item -> new DisplayPosition(
            ((GridLocation) item).getRow(),
            ((GridLocation) item).getColumn()))
        .collect(Collectors.toList()));
  }

  public void render() {
    gridRenderer.render(nextFrameAliveCells);
  }
}
