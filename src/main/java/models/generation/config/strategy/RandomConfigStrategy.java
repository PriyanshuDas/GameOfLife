package models.generation.config.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.Builder;
import models.grid.GridConfig;
import models.grid.GridLocation;
import models.interfaces.IBoardLocation;

@Builder
public class RandomConfigStrategy implements GridConfigStrategy {
  private double aliveProbability;
  private static Random rng = new Random();

  @Override
  public GridConfig getConfig(int rows, int columns) {
    List<IBoardLocation> aliveCells = new ArrayList<>();
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < columns; col++) {
        double randomValue = rng.nextDouble();
        if (randomValue <= aliveProbability) {
          aliveCells.add(new GridLocation(row, col));
        }
      }
    }
    return GridConfig.builder()
        .rows(rows)
        .columns(columns)
        .aliveCells(aliveCells)
        .build();
  }
}
