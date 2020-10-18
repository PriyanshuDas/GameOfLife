package models.grid;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import models.interfaces.GeneralException;
import models.interfaces.IBoardConfig;
import models.interfaces.IBoardLocation;
import models.interfaces.ICell;
import models.interfaces.ICell.CellState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// this version keeps track of all items in memory, providing more computational efficiency
public class GridV2 extends Grid{

  Logger logger = LoggerFactory.getLogger(GridV2.class);
  private ConcurrentHashMap<IBoardLocation, Cell> lastUpdatedLocations = new ConcurrentHashMap<>();


  public GridV2(IBoardConfig boardConfig) throws GeneralException {
        initialize(boardConfig);
    }
  @Override
  public void updateCells(Collection<ICell> cellsToFlip) {
    lastUpdatedLocations.clear();
    cellsToFlip.parallelStream().forEach(cell -> {
      cell.flipState();
      lastUpdatedLocations.put(cell.getLocation(), (Cell) cell);
    });
  }

  @Override
    public void initialize(IBoardConfig boardConfig) throws GeneralException {
        if (!boardConfig.getClass().equals(GridConfig.class)) {
            throw new GridException("Config not supported");
        }
        GridConfig gridConfig = (GridConfig) boardConfig;
        super.columns = gridConfig.getColumns();
        super.rows = gridConfig.getRows();
        initializeCells();
        setAliveCells(gridConfig.getAliveCells());
    }

  private void setAliveCells(List<IBoardLocation> aliveCells) {
    aliveCells.parallelStream().map(this::getCellAt)
        .forEach(cell -> {
          cell.updateState(CellState.ALIVE);
          lastUpdatedLocations.put(cell.getGridLocation(), cell);
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
  public Collection<IBoardLocation> getLastUpdatedLocations() {
    return lastUpdatedLocations.keySet();
  }
}
