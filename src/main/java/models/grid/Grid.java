package models.grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.Getter;
import models.interfaces.GeneralException;
import models.interfaces.IBoard;
import models.interfaces.IBoardConfig;
import models.interfaces.IBoardLocation;
import models.interfaces.ICell;
import models.interfaces.ICell.CellState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Grid implements IBoard {

  Logger logger = LoggerFactory.getLogger(Grid.class);
  @Getter
  private int rows, columns;
  private Map<IBoardLocation, Cell> aliveCells = new ConcurrentHashMap<>();
  private GridCellFactory gridCellFactory = new GridCellFactory();
  @Getter
  private Set<IBoardLocation> newlyAliveLocations = new HashSet<>();
  @Getter
  private Set<IBoardLocation> newlyDeadLocations = new HashSet<>();


  public Grid(IBoardConfig boardConfig) throws GeneralException {
        initialize(boardConfig);
    }

  public void setAliveCells(List<IBoardLocation> aliveCells) {
    this.newlyAliveLocations.clear();
    this.newlyAliveLocations.addAll(aliveCells);
    aliveCells.parallelStream().forEach(gridLocation -> this.aliveCells.put(gridLocation,
        (Cell) gridCellFactory.buildCell(gridLocation, CellState.ALIVE)));
  }

  public void setDeadCells(List<IBoardLocation> deadCells) {
    this.newlyDeadLocations.clear();
    this.newlyDeadLocations.addAll(deadCells);
    deadCells.parallelStream().forEach(gridLocation -> this.aliveCells.remove(gridLocation));
  }

  public Cell getCellAt(IBoardLocation gridLocation) {
    Cell cell = aliveCells.get(gridLocation);
    if (cell == null) {
        return GridCellFactory.buildCell((GridLocation) gridLocation, CellState.DEAD);
    }
    return cell;
  }

    public List<IBoardLocation> getAliveCellLocations() {
      return new ArrayList<>(aliveCells.keySet());
    }

    @Override
    public void initialize(IBoardConfig boardConfig) throws GeneralException {
        if (!boardConfig.getClass().equals(GridConfig.class)) {
            throw new GridException("Config not supported");
        }
        GridConfig gridConfig = (GridConfig) boardConfig;
        columns = gridConfig.getColumns();
        rows = gridConfig.getRows();
        setAliveCells(gridConfig.getAliveCells());
    }

  @Override
  public List<ICell> getAdjacentCells(ICell cell) throws GeneralException  {
    if (!cell.getClass().equals(Cell.class)) {
      throw new GridException("cell type not supported");
    }
    GridLocation cellLocation = ((Cell) cell).getGridLocation();
    return cellLocation.getAdjacentLocations().stream()
        .filter(location -> location.getRow() > 0 && location.getColumn() > 0
            && location.getRow() < rows && location.getColumn() < columns)
        .map(this::getCellAt)
        .collect(Collectors.toList());
  }

  @Override
  public List<ICell> getAliveCells() {
    return new ArrayList<>(aliveCells.values());
  }
}
