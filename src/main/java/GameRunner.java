import models.configs.GameConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import models.displays.GridDisplay;
import models.grid.Cell;
import models.grid.Grid;
import models.interfaces.GeneralException;
import models.interfaces.IBoard;
import models.interfaces.IBoardLocation;
import models.interfaces.ICell;
import models.interfaces.ICell.CellState;
import utils.TimeUtil;

public class GameRunner {
  private GameConfig gameConfig;
  private IBoard board;
  private GridDisplay gameDisplay;
  private TimeUtil runGameTimer;
  private TimeUtil updateStateTimer;
  private static boolean DEBUG_ENABLED = true;
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
    if (framesRendered%500 == 0) {
      DEBUG_ENABLED = true;
    }
    else {
      DEBUG_ENABLED = false;
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
      framesRendered++;
    } while (true);
  }


  //todo: debug
  private void updateState() {
    updateStateTimer.tick();
    List<ICell> updatedCells = getUpdatedCells();
    updateStateTimer.logTime("Time taken to get updatedCells : ", DEBUG_ENABLED);

    List<IBoardLocation> positionsToDeleteCells = updatedCells.parallelStream()
        .filter(cell -> cell.getState().equals(CellState.ALIVE))
        .map(ICell::getLocation)
        .collect(Collectors.toList());

    List<IBoardLocation> positionsToCreateCells = updatedCells.parallelStream()
        .filter(cell -> cell.getState().equals(CellState.DEAD))
        .map(ICell::getLocation)
        .collect(Collectors.toList());

    updateStateTimer.tick();
    board.setAliveCells(positionsToCreateCells);
    updateStateTimer.logTime("Time taken to set aliveCells : ", DEBUG_ENABLED);

    updateStateTimer.tick();
    board.setDeadCells(positionsToDeleteCells);
    updateStateTimer.logTime("Time taken to set deadCells : ", DEBUG_ENABLED);
  }

  private List<ICell> getUpdatedCells() {
    List<ICell> locationsToUpdate = new ArrayList<>();
    locationsToUpdate.addAll(board.getNewlyAliveLocations().parallelStream()
        .map(location -> board.getCellAt(location))
        .collect(Collectors.toList()));

    locationsToUpdate.addAll(board.getNewlyDeadLocations().parallelStream()
        .map(location -> board.getCellAt(location))
        .collect(Collectors.toList()));

    List<ICell> adjacentCells = locationsToUpdate.parallelStream()
        .map(cell -> {
          try {
            return board.getAdjacentCells(cell);
          } catch (GeneralException e) {
            e.printStackTrace();
            return new ArrayList<ICell>();
          }
        })
        .flatMap(List::stream)
        .distinct()
        .collect(Collectors.toList());
    locationsToUpdate.addAll(adjacentCells);

    locationsToUpdate = locationsToUpdate.parallelStream()
        .map(item -> (Cell) item).distinct()
        .collect(Collectors.toList());

    return locationsToUpdate.parallelStream()
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
