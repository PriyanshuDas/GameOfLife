package models.displays;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import models.interfaces.GridCoordinate;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class DisplayPosition implements GridCoordinate {
  private int row, column;
}
