package models.grid;

import models.interfaces.IBoard;

public abstract class Grid implements IBoard {

  public abstract int getRows();

  public abstract int getColumns();
}
