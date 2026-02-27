package models.grid;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import models.interfaces.IBoardLocation;
import models.interfaces.ICell;
import models.interfaces.ICell.CellState;
import org.junit.jupiter.api.Test;

class GridV2Test {

  private GridV2 buildGrid(int rows, int cols, int[]... aliveCells) throws Throwable {
    List<IBoardLocation> alive = new ArrayList<>();
    for (int[] pos : aliveCells) {
      alive.add(new GridLocation(pos[0], pos[1]));
    }
    GridConfig config = GridConfig.builder()
        .rows(rows).columns(cols).aliveCells(alive).build();
    return new GridV2(config);
  }

  // ---- getCellAt ----

  @Test
  void getCellAt_returnsDeadCell_whenNotInitializedAsAlive() throws Throwable {
    GridV2 grid = buildGrid(3, 3);
    assertEquals(CellState.DEAD, grid.getCellAt(new GridLocation(1, 1)).getState());
  }

  @Test
  void getCellAt_returnsAliveCell_whenInitializedAsAlive() throws Throwable {
    GridV2 grid = buildGrid(3, 3, new int[]{1, 1});
    assertEquals(CellState.ALIVE, grid.getCellAt(new GridLocation(1, 1)).getState());
  }

  @Test
  void getCellAt_returnsSameObjectReference_onRepeatedCalls() throws Throwable {
    GridV2 grid = buildGrid(3, 3, new int[]{1, 1});
    ICell first = grid.getCellAt(new GridLocation(1, 1));
    ICell second = grid.getCellAt(new GridLocation(1, 1));
    assertSame(first, second, "should return the same cell object each time");
  }

  // ---- getAdjacentCells ----

  @Test
  void getAdjacentCells_interiorCell_returns8Neighbors() throws Throwable {
    GridV2 grid = buildGrid(5, 5);
    List<ICell> neighbors = grid.getAdjacentCells(grid.getCellAt(new GridLocation(2, 2)));
    assertEquals(8, neighbors.size());
  }

  @Test
  void getAdjacentCells_cornerCell_returns3Neighbors() throws Throwable {
    GridV2 grid = buildGrid(5, 5);
    List<ICell> neighbors = grid.getAdjacentCells(grid.getCellAt(new GridLocation(0, 0)));
    assertEquals(3, neighbors.size());
  }

  @Test
  void getAdjacentCells_edgeCell_returns5Neighbors() throws Throwable {
    GridV2 grid = buildGrid(5, 5);
    // Top edge, not corner
    List<ICell> neighbors = grid.getAdjacentCells(grid.getCellAt(new GridLocation(0, 2)));
    assertEquals(5, neighbors.size());
  }

  @Test
  void getAdjacentCells_doesNotIncludeTheCell_itself() throws Throwable {
    GridV2 grid = buildGrid(5, 5, new int[]{2, 2});
    ICell center = grid.getCellAt(new GridLocation(2, 2));
    List<ICell> neighbors = grid.getAdjacentCells(center);
    assertFalse(neighbors.contains(center), "cell should not list itself as a neighbor");
  }

  // ---- updateCells ----

  @Test
  void updateCells_flipsStateOfPassedCells() throws Throwable {
    GridV2 grid = buildGrid(3, 3, new int[]{1, 1});
    ICell cell = grid.getCellAt(new GridLocation(1, 1));
    assertEquals(CellState.ALIVE, cell.getState());

    List<ICell> toFlip = new ArrayList<>();
    toFlip.add(cell);
    grid.updateCells(toFlip);

    assertEquals(CellState.DEAD, cell.getState(), "cell should have been flipped to dead");
  }

  @Test
  void updateCells_updatesLastUpdatedLocations() throws Throwable {
    GridV2 grid = buildGrid(3, 3, new int[]{1, 1});
    ICell cell = grid.getCellAt(new GridLocation(1, 1));

    List<ICell> toFlip = new ArrayList<>();
    toFlip.add(cell);
    grid.updateCells(toFlip);

    Collection<IBoardLocation> updated = grid.getLastUpdatedLocations();
    assertEquals(1, updated.size());
    assertEquals(new GridLocation(1, 1), updated.iterator().next());
  }

  @Test
  void updateCells_clearsPreviousLastUpdatedLocations() throws Throwable {
    GridV2 grid = buildGrid(3, 3, new int[]{0, 0}, new int[]{1, 1});

    // First update: flip (0,0)
    List<ICell> firstFlip = new ArrayList<>();
    firstFlip.add(grid.getCellAt(new GridLocation(0, 0)));
    grid.updateCells(firstFlip);
    assertEquals(1, grid.getLastUpdatedLocations().size());

    // Second update: flip (1,1) — should replace, not accumulate
    List<ICell> secondFlip = new ArrayList<>();
    secondFlip.add(grid.getCellAt(new GridLocation(1, 1)));
    grid.updateCells(secondFlip);

    Collection<IBoardLocation> updated = grid.getLastUpdatedLocations();
    assertEquals(1, updated.size());
    assertEquals(new GridLocation(1, 1), updated.iterator().next());
  }

  // ---- getAliveCellsLocations ----

  @Test
  void getAliveCellsLocations_returnsOnlyAliveCells() throws Throwable {
    GridV2 grid = buildGrid(3, 3, new int[]{0, 0}, new int[]{2, 2});
    Collection<IBoardLocation> alive = grid.getAliveCellsLocations();
    assertEquals(2, alive.size());
    assertTrue(alive.contains(new GridLocation(0, 0)));
    assertTrue(alive.contains(new GridLocation(2, 2)));
  }

  @Test
  void getAliveCellsLocations_returnsEmpty_whenNoAliveCells() throws Throwable {
    GridV2 grid = buildGrid(3, 3);
    assertTrue(grid.getAliveCellsLocations().isEmpty());
  }

  // ---- initialization ----

  @Test
  void initialization_setsRowsAndColumns() throws Throwable {
    GridV2 grid = buildGrid(7, 9);
    assertEquals(7, grid.getRows());
    assertEquals(9, grid.getColumns());
  }

  @Test
  void initialization_populatesLastUpdatedLocations_withInitialAliveCells() throws Throwable {
    // Critical: ClassicRuleset needs lastUpdatedLocations on first call to getCellsToUpdate
    GridV2 grid = buildGrid(3, 3, new int[]{0, 0}, new int[]{1, 1});
    Collection<IBoardLocation> updated = grid.getLastUpdatedLocations();
    assertEquals(2, updated.size());
    assertTrue(updated.contains(new GridLocation(0, 0)));
    assertTrue(updated.contains(new GridLocation(1, 1)));
  }
}
