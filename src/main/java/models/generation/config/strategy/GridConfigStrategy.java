package models.generation.config.strategy;

import models.grid.GridConfig;

public interface GridConfigStrategy {
  public GridConfig getConfig(int rows, int columns);
}
