package models.rulesets;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import models.grid.GridConfig;
import models.grid.GridLocation;
import models.grid.GridV2;
import models.interfaces.IBoardLocation;
import models.interfaces.ICell;
import models.interfaces.ICell.CellState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClassicRulesetTest {

  private ClassicRuleset ruleset;

  @BeforeEach
  void setUp() {
    ruleset = new ClassicRuleset();
  }

  // ---- helpers ----

  private GridV2 buildGrid(int rows, int cols, int[]... aliveCells) throws Throwable {
    List<IBoardLocation> alive = new ArrayList<>();
    for (int[] pos : aliveCells) {
      alive.add(new GridLocation(pos[0], pos[1]));
    }
    GridConfig config = GridConfig.builder()
        .rows(rows).columns(cols).aliveCells(alive).build();
    return new GridV2(config);
  }

  private CellState stateOf(GridV2 grid, int row, int col) {
    return grid.getCellAt(new GridLocation(row, col)).getState();
  }

  // ---- alive cell rules ----

  @Test
  void aliveCell_with0Neighbors_dies() throws Throwable {
    GridV2 grid = buildGrid(3, 3, new int[]{1, 1});
    assertEquals(CellState.DEAD, ruleset.getNewState(grid.getCellAt(new GridLocation(1, 1)), grid));
  }

  @Test
  void aliveCell_with1Neighbor_dies() throws Throwable {
    GridV2 grid = buildGrid(3, 3, new int[]{1, 1}, new int[]{0, 0});
    assertEquals(CellState.DEAD, ruleset.getNewState(grid.getCellAt(new GridLocation(1, 1)), grid));
  }

  @Test
  void aliveCell_with2Neighbors_survives() throws Throwable {
    GridV2 grid = buildGrid(3, 3, new int[]{1, 1}, new int[]{0, 0}, new int[]{0, 1});
    assertEquals(CellState.ALIVE, ruleset.getNewState(grid.getCellAt(new GridLocation(1, 1)), grid));
  }

  @Test
  void aliveCell_with3Neighbors_survives() throws Throwable {
    GridV2 grid = buildGrid(3, 3, new int[]{1, 1}, new int[]{0, 0}, new int[]{0, 1}, new int[]{0, 2});
    assertEquals(CellState.ALIVE, ruleset.getNewState(grid.getCellAt(new GridLocation(1, 1)), grid));
  }

  @Test
  void aliveCell_with4Neighbors_dies() throws Throwable {
    GridV2 grid = buildGrid(3, 3,
        new int[]{1, 1}, new int[]{0, 0}, new int[]{0, 1}, new int[]{0, 2}, new int[]{1, 0});
    assertEquals(CellState.DEAD, ruleset.getNewState(grid.getCellAt(new GridLocation(1, 1)), grid));
  }

  @Test
  void aliveCell_with8Neighbors_dies() throws Throwable {
    // All cells alive — center has 8 neighbors
    GridV2 grid = buildGrid(3, 3,
        new int[]{0,0}, new int[]{0,1}, new int[]{0,2},
        new int[]{1,0}, new int[]{1,1}, new int[]{1,2},
        new int[]{2,0}, new int[]{2,1}, new int[]{2,2});
    assertEquals(CellState.DEAD, ruleset.getNewState(grid.getCellAt(new GridLocation(1, 1)), grid));
  }

  // ---- dead cell rules ----

  @Test
  void deadCell_with0Neighbors_staysDead() throws Throwable {
    GridV2 grid = buildGrid(3, 3);
    assertEquals(CellState.DEAD, ruleset.getNewState(grid.getCellAt(new GridLocation(1, 1)), grid));
  }

  @Test
  void deadCell_with2Neighbors_staysDead() throws Throwable {
    GridV2 grid = buildGrid(3, 3, new int[]{0, 0}, new int[]{0, 1});
    assertEquals(CellState.DEAD, ruleset.getNewState(grid.getCellAt(new GridLocation(1, 1)), grid));
  }

  @Test
  void deadCell_with3Neighbors_becomesAlive() throws Throwable {
    GridV2 grid = buildGrid(3, 3, new int[]{0, 0}, new int[]{0, 1}, new int[]{0, 2});
    ICell center = grid.getCellAt(new GridLocation(1, 1));
    assertEquals(CellState.DEAD, center.getState(), "precondition: center must be dead");
    assertEquals(CellState.ALIVE, ruleset.getNewState(center, grid));
  }

  @Test
  void deadCell_with4Neighbors_staysDead() throws Throwable {
    GridV2 grid = buildGrid(3, 3,
        new int[]{0, 0}, new int[]{0, 1}, new int[]{0, 2}, new int[]{1, 0});
    assertEquals(CellState.DEAD, ruleset.getNewState(grid.getCellAt(new GridLocation(1, 1)), grid));
  }

  // ---- updateState mutates the board ----

  @Test
  void updateState_mutatesBoard_returnsChangedCells() throws Throwable {
    // Single alive cell: should die (0 neighbors after looking at itself)
    GridV2 grid = buildGrid(3, 3, new int[]{1, 1});
    Collection<ICell> changed = ruleset.updateState(grid);

    assertFalse(changed.isEmpty(), "should report at least the dying cell");
    assertEquals(CellState.DEAD, stateOf(grid, 1, 1), "isolated cell should die");
  }

  @Test
  void updateState_onlyEvaluatesCellsNearLastUpdate() throws Throwable {
    // Alive cell far from initial updates should NOT be re-evaluated if no neighbor changed
    // Block pattern: 2x2 all alive — stable, so updateState should return no changes
    GridV2 grid = buildGrid(4, 4,
        new int[]{1, 1}, new int[]{1, 2}, new int[]{2, 1}, new int[]{2, 2});
    Collection<ICell> changed = ruleset.updateState(grid);

    assertTrue(changed.isEmpty(), "block pattern is a still life — nothing should change");
    assertEquals(CellState.ALIVE, stateOf(grid, 1, 1));
    assertEquals(CellState.ALIVE, stateOf(grid, 1, 2));
    assertEquals(CellState.ALIVE, stateOf(grid, 2, 1));
    assertEquals(CellState.ALIVE, stateOf(grid, 2, 2));
  }
}
