package models.displays;

import java.awt.Color;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import models.interfaces.GridCoordinate;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class DisplayCell implements GridCoordinate {

  @Override
  public int getRow() {
    return position.getRow();
  }

  @Override
  public int getColumn() {
    return position.getColumn();
  }

  public enum  DisplayCellState {ALIVE, DEAD}

  private Color color;
  private Color borderColor;
  private DisplayPosition position;
  private DisplayCellState state;
  public void flipState() {
    state = (state == DisplayCellState.ALIVE)? DisplayCellState.DEAD : DisplayCellState.ALIVE;
  }
}
