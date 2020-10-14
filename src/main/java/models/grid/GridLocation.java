package models.grid;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import models.interfaces.IBoardLocation;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class GridLocation implements IBoardLocation {
    int row, column;

  public List<GridLocation> getAdjacentLocations() {
    List<GridLocation> adjacentLocations = new ArrayList<>();
    adjacentLocations.add(new GridLocation(row-1, column-1));
    adjacentLocations.add(new GridLocation(row-1, column));
    adjacentLocations.add(new GridLocation(row-1, column+1));
    adjacentLocations.add(new GridLocation(row, column-1));
    adjacentLocations.add(new GridLocation(row, column+1));
    adjacentLocations.add(new GridLocation(row+1, column-1));
    adjacentLocations.add(new GridLocation(row+1, column));
    adjacentLocations.add(new GridLocation(row+1, column+1));
    return adjacentLocations;
  }
}
