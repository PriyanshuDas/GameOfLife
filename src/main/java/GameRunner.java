import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import models.configs.GameConfig;
import models.displays.GridDisplay;
import models.grid.Grid;
import models.grid.GridV1;
import models.grid.GridV2;
import models.interfaces.GeneralException;
import models.interfaces.GridCoordinate;
import models.interfaces.IBoard;
import models.interfaces.ICell;
import models.interfaces.ICell.CellState;
import utils.TimeUtil;

public class GameRunner {
  private GameConfig gameConfig;
  private IBoard board;
  private GridDisplay gameDisplay;
  private TimeUtil runGameTimer;
  private TimeUtil updateStateTimer;
  private static boolean DEBUG_ENABLED = false;
  private long framesRendered = 0;
  private long waitTimeBetweenFrames;


  public GameRunner(GameConfig gameConfig) throws GeneralException {
    this.gameConfig = gameConfig;
    board = new GridV2(gameConfig.getBoardConfig());
    gameDisplay = new GridDisplay();
    gameDisplay.initialize(
        gameConfig.getRows(), gameConfig.getColumns(),
        board.getAliveCellsLocations(),
        gameConfig.getWidth(), gameConfig.getHeight());
    runGameTimer = new TimeUtil();
    updateStateTimer = new TimeUtil();
    waitTimeBetweenFrames = 1000/gameConfig.getFps();
  }

  public void runGame() {
    long endMs = 0, startMs = 0;
    do {
      startMs = System.currentTimeMillis();
      long timeToWait = waitTimeBetweenFrames - (startMs - endMs);
      if (timeToWait > 0) {
        try {
          Thread.sleep(timeToWait);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      runGameTimer.tick();
      gameDisplay.render();
      if (framesRendered%(10) == 0) {
        runGameTimer.logTime("Time Elapsed to Render frame : ", true);
      }
      runGameTimer.tick();
      List<GridCoordinate> updatedCells = updateState().stream()
          .map(cell -> (GridCoordinate) cell)
          .collect(Collectors.toList());

      gameDisplay.updateNextFrame(updatedCells);

      if (framesRendered%(10) == 0) {
      runGameTimer.logTime("Time Elapsed to Update state : ", true);
      }
      endMs = System.currentTimeMillis();
      framesRendered++;
    } while (true);
  }

  private List<ICell> updateState() {
    List<ICell> updatedCells = getUpdatedCells();
    board.updateCells(updatedCells);
    return updatedCells;
  }

  private List<ICell> getUpdatedCells() {
    return board.getLastUpdatedLocations().parallelStream()
        .map(location -> board.getCellAt(location))
        .map(cell -> cell.getCellAndNeighbors(board))
        .flatMap(Collection::parallelStream)
        .distinct()
        .filter(cell -> {
          CellState newState = gameConfig.getRuleset().getNewState(cell, board);
          if (!newState.equals(cell.getState())) {
            return true;
          }
          else {
            return false;
          }
        }).collect(Collectors.toList());
  }

}
