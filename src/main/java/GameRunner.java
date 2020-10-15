import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import models.configs.GameConfig;
import models.displays.GridDisplay;
import models.grid.Grid;
import models.interfaces.GeneralException;
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
    board = new Grid(gameConfig.getBoardConfig());
    gameDisplay = new GridDisplay();
    gameDisplay.initialize((Grid) board, gameConfig.getWidth(), gameConfig.getHeight());
    runGameTimer = new TimeUtil();
    updateStateTimer = new TimeUtil();
    waitTimeBetweenFrames = 1000/gameConfig.getFps();
  }

  public void runGame() {
    if (framesRendered%10000 == 0) {
      DEBUG_ENABLED = !DEBUG_ENABLED;
    }
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
      runGameTimer.logTime("Time Elapsed to Render frame : ", DEBUG_ENABLED);
      runGameTimer.tick();
      updateState();
      gameDisplay.updateNextFrame(board);
      runGameTimer.logTime("Time Elapsed to Update state : ", DEBUG_ENABLED);
      endMs = System.currentTimeMillis();
      if (framesRendered%10000 == 0) {
        DEBUG_ENABLED = !DEBUG_ENABLED;
      }
      framesRendered++;
    } while (true);
  }

  private void updateState() {
    updateStateTimer.tick();
    List<ICell> updatedCells = getUpdatedCells();
    updateStateTimer.logTime("Time taken to get updatedCells : ", DEBUG_ENABLED);

    updateStateTimer.tick();
    board.setAliveCells(updatedCells.parallelStream()
        .filter(cell -> cell.getState().equals(CellState.DEAD))
        .map(ICell::getLocation)
        .collect(Collectors.toList()));
    updateStateTimer.logTime("Time taken to set aliveCells : ", DEBUG_ENABLED);

    updateStateTimer.tick();
    board.setDeadCells(updatedCells.parallelStream()
        .filter(cell -> cell.getState().equals(CellState.ALIVE))
        .map(ICell::getLocation)
        .collect(Collectors.toList()));
    updateStateTimer.logTime("Time taken to set deadCells : ", DEBUG_ENABLED);
  }

  private List<ICell> getUpdatedCells() {
    return Stream.concat(
        board.getNewlyAliveLocations().parallelStream(),
        board.getNewlyDeadLocations().parallelStream())
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
