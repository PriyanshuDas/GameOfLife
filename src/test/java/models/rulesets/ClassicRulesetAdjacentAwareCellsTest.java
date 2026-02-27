package models.rulesets;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import models.grid.CellV2Adjacent;
import models.grid.GridConfig;
import models.grid.GridLocation;
import models.grid.GridV3;
import models.interfaces.IBoardLocation;
import models.interfaces.ICell.CellState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClassicRulesetAdjacentAwareCellsTest {

  private ClassicRulesetAdjacentAwareCells ruleset;

  @BeforeEach
  void setUp() {
    ruleset = new ClassicRulesetAdjacentAwareCells();
  }

  // ---- helpers ----

  private GridV3 buildAndInitGrid(int rows, int cols, int[]... aliveCells) throws Throwable {
    List<IBoardLocation> alive = new ArrayList<>();
    for (int[] pos : aliveCells) {
      alive.add(new GridLocation(pos[0], pos[1]));
    }
    GridConfig config = GridConfig.builder()
        .rows(rows).columns(cols).aliveCells(alive).build();
    GridV3 grid = new GridV3(config);
    ruleset.initializeBoardState(grid, config);
    return grid;
  }

  private CellState stateOf(GridV3 grid, int row, int col) {
    return grid.getCellAt(new GridLocation(row, col)).getState();
  }

  private int neighborCount(GridV3 grid, int row, int col) {
    return ((CellV2Adjacent) grid.getCellAt(new GridLocation(row, col))).getAdjacentAliveNeighbours();
  }

  // ---- initializeBoardState ----

  @Test
  void initializeBoardState_setsAliveCellsCorrectly() throws Throwable {
    GridV3 grid = buildAndInitGrid(3, 3, new int[]{1, 1});
    assertEquals(CellState.ALIVE, stateOf(grid, 1, 1));
    assertEquals(CellState.DEAD, stateOf(grid, 0, 0));
  }

  @Test
  void initializeBoardState_populatesNeighborCounters() throws Throwable {
    // Single alive cell at center of 3x3: all 8 neighbors get counter=1
    GridV3 grid = buildAndInitGrid(3, 3, new int[]{1, 1});
    assertEquals(1, neighborCount(grid, 0, 0));
    assertEquals(1, neighborCount(grid, 0, 1));
    assertEquals(1, neighborCount(grid, 0, 2));
    assertEquals(1, neighborCount(grid, 1, 0));
    assertEquals(1, neighborCount(grid, 1, 2));
    assertEquals(1, neighborCount(grid, 2, 0));
    assertEquals(1, neighborCount(grid, 2, 1));
    assertEquals(1, neighborCount(grid, 2, 2));
  }

  @Test
  void initializeBoardState_centerCellCounter_reflectsAliveNeighbors() throws Throwable {
    // Block pattern: 4 cells alive in 2x2, center of a 4x4 grid
    // (1,1),(1,2),(2,1),(2,2) — each has 3 alive neighbors from the block
    GridV3 grid = buildAndInitGrid(4, 4,
        new int[]{1, 1}, new int[]{1, 2}, new int[]{2, 1}, new int[]{2, 2});
    // Each block cell has exactly 3 alive neighbors (the other 3 in the block)
    assertEquals(3, neighborCount(grid, 1, 1));
    assertEquals(3, neighborCount(grid, 1, 2));
    assertEquals(3, neighborCount(grid, 2, 1));
    assertEquals(3, neighborCount(grid, 2, 2));
  }

  @Test
  void initializeBoardState_populatesUpdateLocations() throws Throwable {
    // After init, updateLocations should contain cells that will change on first tick
    // Single alive cell will die (0 neighbors... wait, it has itself flip but no neighbors alive)
    // Let's use a pattern where we know something will change: horizontal blinker
    // The 3 cells will produce cells that change on next tick
    GridV3 grid = buildAndInitGrid(5, 5,
        new int[]{2, 1}, new int[]{2, 2}, new int[]{2, 3});
    assertFalse(grid.getUpdateLocations().isEmpty(),
        "updateLocations must be non-empty after initializeBoardState or the game is frozen");
  }

  // ---- updateState ----

  @Test
  void updateState_isolatedCell_dies() throws Throwable {
    GridV3 grid = buildAndInitGrid(3, 3, new int[]{1, 1});
    ruleset.updateState(grid);
    assertEquals(CellState.DEAD, stateOf(grid, 1, 1));
  }

  @Test
  void updateState_deadCellWith3Neighbors_becomesAlive() throws Throwable {
    // Row of 3: (1,0),(1,1),(1,2) — center cell (0,1) and (2,1) should birth
    GridV3 grid = buildAndInitGrid(3, 3,
        new int[]{1, 0}, new int[]{1, 1}, new int[]{1, 2});
    ruleset.updateState(grid);
    assertEquals(CellState.ALIVE, stateOf(grid, 0, 1));
    assertEquals(CellState.ALIVE, stateOf(grid, 2, 1));
  }

  @Test
  void updateState_maintainsNeighborCounterAfterCellDies() throws Throwable {
    // Single alive cell at (1,1) in 3x3: after one tick, cell dies
    // All neighbors' counters should be decremented back to 0
    GridV3 grid = buildAndInitGrid(3, 3, new int[]{1, 1});
    ruleset.updateState(grid);

    assertEquals(0, neighborCount(grid, 0, 0));
    assertEquals(0, neighborCount(grid, 0, 1));
    assertEquals(0, neighborCount(grid, 0, 2));
    assertEquals(0, neighborCount(grid, 1, 0));
    assertEquals(0, neighborCount(grid, 1, 2));
    assertEquals(0, neighborCount(grid, 2, 0));
    assertEquals(0, neighborCount(grid, 2, 1));
    assertEquals(0, neighborCount(grid, 2, 2));
  }

  @Test
  void updateState_repopulatesUpdateLocationsForNextGen() throws Throwable {
    // After one tick on a blinker, updateLocations should have cells for the next tick
    GridV3 grid = buildAndInitGrid(5, 5,
        new int[]{2, 1}, new int[]{2, 2}, new int[]{2, 3});
    ruleset.updateState(grid);
    assertFalse(grid.getUpdateLocations().isEmpty(),
        "updateLocations must be repopulated after each tick or the game stops evolving");
  }

  // ---- getCellsToUpdate reads from updateLocations, not getLastUpdatedLocations ----

  @Test
  void getCellsToUpdate_readsFromUpdateLocations_notBaseClass() throws Throwable {
    GridV3 grid = buildAndInitGrid(5, 5,
        new int[]{2, 1}, new int[]{2, 2}, new int[]{2, 3});
    // updateLocations is populated by initializeBoardState; getCellsToUpdate should use it
    Collection<?> toUpdate = ruleset.getCellsToUpdate(grid);
    assertFalse(toUpdate.isEmpty(),
        "getCellsToUpdate must read from updateLocations (populated by AdjacentAware system)");
  }
}
