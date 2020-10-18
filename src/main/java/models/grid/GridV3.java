package models.grid;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Getter;
import models.interfaces.GeneralException;
import models.interfaces.IBoardConfig;
import models.interfaces.IBoardLocation;
import models.interfaces.ICell;
import models.interfaces.ICell.CellState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// this version keeps track of all items in memory, providing more computational efficiency
public class GridV3 extends Grid{

  Logger logger = LoggerFactory.getLogger(GridV3.class);
  @Getter
  private ConcurrentHashMap<GridLocation, CellV2Adjacent> updateLocations
      = new ConcurrentHashMap<>();


  public GridV3(IBoardConfig boardConfig) throws GeneralException {
        initialize(boardConfig);
    }
  @Override
  public void updateCells(Collection<ICell> cellsToFlip) {
    cellsToFlip.forEach(ICell::flipState);
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
    }

  private void initializeCells() {
    super.cells = IntStream.range(0, rows)
        .mapToObj(row -> IntStream.range(0, columns)
            .mapToObj(column -> new CellV2Adjacent(new GridLocation(row, column), CellState.DEAD))
            .map(cell -> (Cell) cell)
            .collect(Collectors.toList()))
        .collect(Collectors.toList());
  }

  @Override
  public Collection<IBoardLocation> getLastUpdatedLocations() {
    return updateLocations.values().stream()
        .map(Cell::getGridLocation)
        .collect(Collectors.toList());
  }
}
