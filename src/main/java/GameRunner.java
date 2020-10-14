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
  GameConfig gameConfig;
  IBoard board;
  GridDisplay gameDisplay;
  TimeUtil timeUtil;
  private static boolean DEBUG_ENABLED = false;


  public GameRunner(GameConfig gameConfig) throws GeneralException {
    this.gameConfig = gameConfig;
    board = new Grid(gameConfig.getBoardConfig());
    gameDisplay = new GridDisplay();
    gameDisplay.initialize((Grid) board, gameConfig.getWidth(), gameConfig.getHeight());
    timeUtil = new TimeUtil();
  }

  public void runGame() {
    long wait = 1000/gameConfig.getFps();
    long endMs = 0, startMs = 0;
    do {
      startMs = System.currentTimeMillis();
      long timeToWait = wait - (startMs - endMs);
      if (timeToWait > 0) {
        try {
          Thread.sleep(timeToWait);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      timeUtil.tick();
      gameDisplay.render();
      logTime("Time Elapsed to Render frame : ");
      timeUtil.tick();
      updateState();
      gameDisplay.updateNextFrame(board);
      logTime("Time Elapsed to Update state : ");
      endMs = System.currentTimeMillis();
    } while (true);
  }

  private void logTime(String s) {
    if (DEBUG_ENABLED) {
      System.out.println(s + timeUtil.getElapsedTime() + " ms");
    }
  }

  //todo: debug
  private void updateState() {
    List<ICell> updatedCells = getUpdatedCells();

    List<IBoardLocation> positionsToDeleteCells = updatedCells.stream()
        .filter(cell -> cell.getState().equals(CellState.ALIVE))
        .map(ICell::getLocation)
        .collect(Collectors.toList());

    List<IBoardLocation> positionsToCreateCells = updatedCells.stream()
        .filter(cell -> cell.getState().equals(CellState.DEAD))
        .map(ICell::getLocation)
        .collect(Collectors.toList());

    board.setAliveCells(positionsToCreateCells);
    board.setDeadCells(positionsToDeleteCells);
  }

  private List<ICell> getUpdatedCells() {
    List<ICell> locationsToUpdate = new ArrayList<>();
    locationsToUpdate.addAll(board.getNewlyAliveLocations().stream()
        .map(location -> board.getCellAt(location))
        .collect(Collectors.toList()));

    locationsToUpdate.addAll(board.getNewlyDeadLocations().stream()
        .map(location -> board.getCellAt(location))
        .collect(Collectors.toList()));

    List<ICell> adjacentCells = locationsToUpdate.stream()
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

    locationsToUpdate = locationsToUpdate.stream()
        .map(item -> (Cell) item).distinct()
        .collect(Collectors.toList());

    return locationsToUpdate.stream()
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
