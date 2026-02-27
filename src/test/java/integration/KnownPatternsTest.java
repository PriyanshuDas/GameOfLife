package integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import models.grid.GridConfig;
import models.grid.GridLocation;
import models.grid.GridV2;
import models.grid.GridV3;
import models.interfaces.IBoardLocation;
import models.interfaces.ICell.CellState;
import models.rulesets.ClassicRuleset;
import models.rulesets.ClassicRulesetAdjacentAwareCells;
import org.junit.jupiter.api.Test;

/**
 * Integration tests using well-known Conway's Game of Life patterns.
 *
 * Pattern coordinates use (row, col) with 0-indexed top-left origin.
 */
class KnownPatternsTest {

  // ---- helpers ----

  private GridConfig makeConfig(int rows, int cols, int[]... aliveCells) {
    List<IBoardLocation> alive = new ArrayList<>();
    for (int[] pos : aliveCells) {
      alive.add(new GridLocation(pos[0], pos[1]));
    }
    return GridConfig.builder().rows(rows).columns(cols).aliveCells(alive).build();
  }

  private GridV2 gridV2(int rows, int cols, int[]... aliveCells) throws Throwable {
    return new GridV2(makeConfig(rows, cols, aliveCells));
  }

  private GridV3 gridV3WithInit(int rows, int cols, int[]... aliveCells) throws Throwable {
    GridConfig config = makeConfig(rows, cols, aliveCells);
    GridV3 grid = new GridV3(config);
    new ClassicRulesetAdjacentAwareCells().initializeBoardState(grid, config);
    return grid;
  }

  private CellState v2StateOf(GridV2 grid, int row, int col) {
    return grid.getCellAt(new GridLocation(row, col)).getState();
  }

  private CellState v3StateOf(GridV3 grid, int row, int col) {
    return grid.getCellAt(new GridLocation(row, col)).getState();
  }

  // ============================================================
  // BLOCK — 2x2 still life. Should not change across any tick.
  //
  //   . . . . .
  //   . X X . .
  //   . X X . .
  //   . . . . .
  // ============================================================

  @Test
  void block_isStillLife_withClassicRuleset() throws Throwable {
    ClassicRuleset ruleset = new ClassicRuleset();
    GridV2 grid = gridV2(5, 5,
        new int[]{1, 1}, new int[]{1, 2},
        new int[]{2, 1}, new int[]{2, 2});

    for (int tick = 0; tick < 5; tick++) {
      assertTrue(ruleset.updateState(grid).isEmpty(),
          "block should produce no changes on tick " + (tick + 1));
    }

    assertEquals(CellState.ALIVE, v2StateOf(grid, 1, 1));
    assertEquals(CellState.ALIVE, v2StateOf(grid, 1, 2));
    assertEquals(CellState.ALIVE, v2StateOf(grid, 2, 1));
    assertEquals(CellState.ALIVE, v2StateOf(grid, 2, 2));
  }

  @Test
  void block_isStillLife_withAdjacentAwareRuleset() throws Throwable {
    ClassicRulesetAdjacentAwareCells ruleset = new ClassicRulesetAdjacentAwareCells();
    GridV3 grid = gridV3WithInit(5, 5,
        new int[]{1, 1}, new int[]{1, 2},
        new int[]{2, 1}, new int[]{2, 2});

    for (int tick = 0; tick < 5; tick++) {
      ruleset.updateState(grid);
    }

    assertEquals(CellState.ALIVE, v3StateOf(grid, 1, 1));
    assertEquals(CellState.ALIVE, v3StateOf(grid, 1, 2));
    assertEquals(CellState.ALIVE, v3StateOf(grid, 2, 1));
    assertEquals(CellState.ALIVE, v3StateOf(grid, 2, 2));
    // No cells outside the block should be alive
    assertEquals(CellState.DEAD, v3StateOf(grid, 0, 0));
    assertEquals(CellState.DEAD, v3StateOf(grid, 3, 3));
  }

  // ============================================================
  // BLINKER — period-2 oscillator.
  //
  // Gen 1 (horizontal):    Gen 2 (vertical):
  //   . . . . .              . . . . .
  //   . . . . .              . . X . .
  //   . X X X .    <-->      . . X . .
  //   . . . . .              . . X . .
  //   . . . . .              . . . . .
  // ============================================================

  @Test
  void blinker_oscillates_withClassicRuleset() throws Throwable {
    ClassicRuleset ruleset = new ClassicRuleset();
    GridV2 grid = gridV2(5, 5,
        new int[]{2, 1}, new int[]{2, 2}, new int[]{2, 3});

    // Tick 1: horizontal → vertical
    ruleset.updateState(grid);
    assertEquals(CellState.DEAD,  v2StateOf(grid, 2, 1), "left cell should die");
    assertEquals(CellState.ALIVE, v2StateOf(grid, 2, 2), "center stays alive");
    assertEquals(CellState.DEAD,  v2StateOf(grid, 2, 3), "right cell should die");
    assertEquals(CellState.ALIVE, v2StateOf(grid, 1, 2), "top cell should birth");
    assertEquals(CellState.ALIVE, v2StateOf(grid, 3, 2), "bottom cell should birth");

    // Tick 2: vertical → horizontal (back to start)
    ruleset.updateState(grid);
    assertEquals(CellState.ALIVE, v2StateOf(grid, 2, 1));
    assertEquals(CellState.ALIVE, v2StateOf(grid, 2, 2));
    assertEquals(CellState.ALIVE, v2StateOf(grid, 2, 3));
    assertEquals(CellState.DEAD,  v2StateOf(grid, 1, 2));
    assertEquals(CellState.DEAD,  v2StateOf(grid, 3, 2));
  }

  @Test
  void blinker_oscillates_withAdjacentAwareRuleset() throws Throwable {
    ClassicRulesetAdjacentAwareCells ruleset = new ClassicRulesetAdjacentAwareCells();
    GridV3 grid = gridV3WithInit(5, 5,
        new int[]{2, 1}, new int[]{2, 2}, new int[]{2, 3});

    // Tick 1: horizontal → vertical
    ruleset.updateState(grid);
    assertEquals(CellState.DEAD,  v3StateOf(grid, 2, 1), "left cell should die");
    assertEquals(CellState.ALIVE, v3StateOf(grid, 2, 2), "center stays alive");
    assertEquals(CellState.DEAD,  v3StateOf(grid, 2, 3), "right cell should die");
    assertEquals(CellState.ALIVE, v3StateOf(grid, 1, 2), "top cell should birth");
    assertEquals(CellState.ALIVE, v3StateOf(grid, 3, 2), "bottom cell should birth");

    // Tick 2: vertical → horizontal
    ruleset.updateState(grid);
    assertEquals(CellState.ALIVE, v3StateOf(grid, 2, 1));
    assertEquals(CellState.ALIVE, v3StateOf(grid, 2, 2));
    assertEquals(CellState.ALIVE, v3StateOf(grid, 2, 3));
    assertEquals(CellState.DEAD,  v3StateOf(grid, 1, 2));
    assertEquals(CellState.DEAD,  v3StateOf(grid, 3, 2));
  }

  @Test
  void blinker_bothRulesets_produceIdenticalResults() throws Throwable {
    ClassicRuleset classicRuleset = new ClassicRuleset();
    ClassicRulesetAdjacentAwareCells adjacentRuleset = new ClassicRulesetAdjacentAwareCells();

    GridV2 v2Grid = gridV2(7, 7,
        new int[]{3, 2}, new int[]{3, 3}, new int[]{3, 4});
    GridV3 v3Grid = gridV3WithInit(7, 7,
        new int[]{3, 2}, new int[]{3, 3}, new int[]{3, 4});

    // Run both through 6 ticks and compare cell states at each step
    for (int tick = 0; tick < 6; tick++) {
      classicRuleset.updateState(v2Grid);
      adjacentRuleset.updateState(v3Grid);

      for (int r = 0; r < 7; r++) {
        for (int c = 0; c < 7; c++) {
          assertEquals(v2StateOf(v2Grid, r, c), v3StateOf(v3Grid, r, c),
              "Mismatch at tick=" + (tick + 1) + " row=" + r + " col=" + c);
        }
      }
    }
  }

  // ============================================================
  // FACTORY CONFIG BUG: ClassicRuleset + GridV3 is broken.
  //
  // This test documents the known incompatibility described in CODE_REVIEW.md.
  // GridV3.initialize() does not set alive cells; ClassicRuleset.initializeBoardState()
  // is a no-op. Result: game starts frozen with a blank grid.
  // ============================================================

  @Test
  void brokenConfig_classicRuleset_with_gridV3_producesBlankFrozenGame() throws Throwable {
    ClassicRuleset ruleset = new ClassicRuleset();
    // Intentionally using ClassicRuleset (wrong pairing) with GridV3
    GridConfig config = makeConfig(5, 5,
        new int[]{2, 1}, new int[]{2, 2}, new int[]{2, 3});
    GridV3 grid = new GridV3(config);
    ruleset.initializeBoardState(grid, config); // no-op

    // All cells should be dead because GridV3 doesn't self-initialize from config
    assertEquals(CellState.DEAD, grid.getCellAt(new GridLocation(2, 1)).getState(),
        "GridV3 + ClassicRuleset: cells are never initialized — game is blank from the start");

    // And the game is frozen: updateState finds nothing to evaluate
    assertTrue(ruleset.updateState(grid).isEmpty(),
        "GridV3 + ClassicRuleset: updateLocations is empty, so nothing ever evaluates");
  }
}
