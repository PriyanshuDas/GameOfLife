import java.util.List;
import java.util.stream.Collectors;
import models.configs.GameConfig;
import models.displays.GridDisplay;
import models.grid.GridV1;
import models.grid.GridV2;
import models.grid.GridV3;
import models.interfaces.GeneralException;
import models.interfaces.GridCoordinate;
import models.interfaces.IBoard;
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
    initializeGrid();
    initializeDisplay();
    runGameTimer = new TimeUtil();
    updateStateTimer = new TimeUtil();
    waitTimeBetweenFrames = 1000/gameConfig.getFps();
  }

  private void initializeDisplay() {
    gameDisplay = new GridDisplay();
    gameDisplay.initialize(
        gameConfig.getRows(), gameConfig.getColumns(),
        board.getAliveCellsLocations(),
        gameConfig.getWidth(), gameConfig.getHeight());
  }

  private void initializeGrid() throws GeneralException {
    if (gameConfig.getGridClass().equals(GridV1.class)) {
      board = new GridV1(gameConfig.getBoardConfig());
    }
    else if (gameConfig.getGridClass().equals(GridV2.class)) {
      board = new GridV2(gameConfig.getBoardConfig());
    }
    else if (gameConfig.getGridClass().equals(GridV3.class)) {
      board = new GridV3(gameConfig.getBoardConfig());
    }
    gameConfig.getRuleset().initializeBoardState(board, gameConfig.getBoardConfig());
  }

  public void runGame() {
    long endMs = 0, startMs;
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
      if (shouldLogThisFrame()) {
        runGameTimer.logTime("Time Elapsed to Render frame : ");
      }
      runGameTimer.tick();
      List<GridCoordinate> updatedLocations = gameConfig.getRuleset().updateState(board)
          .stream().map(cell -> (GridCoordinate) cell)
          .collect(Collectors.toList());

      if (shouldLogThisFrame()) {
        runGameTimer.logTime("Time Elapsed to Update Cells : ");
      }
      runGameTimer.tick();
      gameDisplay.updateNextFrame(updatedLocations);

      if (shouldLogThisFrame()) {
      runGameTimer.logTime("Time Elapsed to Update Next Frame : ");
      }
      endMs = System.currentTimeMillis();
      framesRendered++;
    } while (true);
  }

  private boolean shouldLogThisFrame() {
    return DEBUG_ENABLED && framesRendered % (2) == 0;
  }
}
