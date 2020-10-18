package models.grid;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import models.interfaces.GridCoordinate;
import models.interfaces.IBoardLocation;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class GridLocation implements IBoardLocation, GridCoordinate {

  int row, column;
  @Getter
  private static Collection<GridLocation> adjacentDelta = ImmutableList.of(
      (new GridLocation(-1, -1)),
      (new GridLocation(-1, 0)),
      (new GridLocation(-1, +1)),
      (new GridLocation(0, -1)),
      (new GridLocation(0, +1)),
      (new GridLocation(+1, -1)),
      (new GridLocation(+1, 0)),
      (new GridLocation(+1, +1))
  );

  public GridLocation(int row, int column) {
    this.row = row;
    this.column = column;

  }

  public List<GridLocation> getAdjacentLocations() {
    List<GridLocation> adjacentLocations = new ArrayList<>();
    adjacentLocations.add(new GridLocation(row - 1, column - 1));
    adjacentLocations.add(new GridLocation(row - 1, column));
    adjacentLocations.add(new GridLocation(row - 1, column + 1));
    adjacentLocations.add(new GridLocation(row, column - 1));
    adjacentLocations.add(new GridLocation(row, column + 1));
    adjacentLocations.add(new GridLocation(row + 1, column - 1));
    adjacentLocations.add(new GridLocation(row + 1, column));
    adjacentLocations.add(new GridLocation(row + 1, column + 1));
    return adjacentLocations;
  }
}
