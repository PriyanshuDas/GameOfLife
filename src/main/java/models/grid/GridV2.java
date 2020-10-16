package models.grid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Getter;
import models.interfaces.GeneralException;
import models.interfaces.GridCoordinate;
import models.interfaces.IBoard;
import models.interfaces.IBoardConfig;
import models.interfaces.IBoardLocation;
import models.interfaces.ICell;
import models.interfaces.ICell.CellState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// this version keeps track of all items in memory, providing more computational efficiency
public class GridV2 extends Grid{

  Logger logger = LoggerFactory.getLogger(GridV2.class);
  @Getter
  private int rows, columns;
  private List<List<Cell>> cells;
  @Getter
  private Set<IBoardLocation> lastUpdatedLocations = new HashSet<>();


  public GridV2(IBoardConfig boardConfig) throws GeneralException {
        initialize(boardConfig);
    }

  public Cell getCellAt(IBoardLocation gridLocation) {
    return cells.get(((GridLocation) gridLocation).getRow())
        .get(((GridLocation) gridLocation).getColumn());
  }

  @Override
  public void updateCells(List<ICell> cellsToFlip) {
    lastUpdatedLocations.clear();
    cellsToFlip.parallelStream().forEach(cell -> {
      cell.flipState();
      lastUpdatedLocations.add(cell.getLocation());
    });
  }

  @Override
  public Collection<IBoardLocation> getAliveCellsLocations() {
    return cells.parallelStream()
        .flatMap(Collection::parallelStream)
        .filter(cell -> cell.getCellState().equals(CellState.ALIVE))
        .map(Cell::getGridLocation)
        .collect(Collectors.toList());
  }

  @Override
    public void initialize(IBoardConfig boardConfig) throws GeneralException {
        if (!boardConfig.getClass().equals(GridConfig.class)) {
            throw new GridException("Config not supported");
        }
        GridConfig gridConfig = (GridConfig) boardConfig;
        columns = gridConfig.getColumns();
        rows = gridConfig.getRows();
        initializeCells();
        setAliveCells(gridConfig.getAliveCells());
    }

  private void setAliveCells(List<IBoardLocation> aliveCells) {
    aliveCells.parallelStream().map(this::getCellAt)
        .forEach(cell -> {
          cell.updateState(CellState.ALIVE);
          lastUpdatedLocations.add(cell.getGridLocation());
        });
  }

  private void initializeCells() {
    cells = IntStream.range(0, rows)
        .mapToObj(row -> IntStream.range(0, columns)
            .mapToObj(column -> new Cell(new GridLocation(row, column), CellState.DEAD))
            .collect(Collectors.toList()))
        .collect(Collectors.toList());
  }

  @Override
  public List<ICell> getAdjacentCells(ICell cell) {
    if (!cell.getClass().equals(Cell.class)) {
      return new ArrayList<>();
    }
    return ((Cell) cell).getGridLocation().getAdjacentLocations().stream()
        .filter(location -> location.getRow() > 0 && location.getColumn() > 0
            && location.getRow() < rows && location.getColumn() < columns)
        .map(this::getCellAt)
        .collect(Collectors.toList());
  }
}
