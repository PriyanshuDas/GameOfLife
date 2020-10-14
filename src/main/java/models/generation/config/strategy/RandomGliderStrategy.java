package models.generation.config.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.AllArgsConstructor;
import lombok.Builder;
import models.grid.GridConfig;
import models.grid.GridLocation;
import models.interfaces.IBoardLocation;
import models.patterns.Glider;

@AllArgsConstructor
@Builder
public class RandomGliderStrategy implements GridConfigStrategy {
  private double gliderGenerationProbability;
  private static Random rng = new Random();

  @Override
  public GridConfig getConfig(int rows, int columns) {
    List<IBoardLocation> aliveCells = new ArrayList<>();
//    addGlider(aliveCells, 20, 20);
    for (int row = 0; row + 2 < rows; row+=3) {
      for (int col = 0; col + 2 < columns; col+=3) {
        double randomValue = rng.nextDouble();
        if (randomValue <= gliderGenerationProbability) {
          addGlider(aliveCells, row, col);
        }
      }
    }
    return GridConfig.builder()
        .rows(rows)
        .columns(columns)
        .aliveCells(aliveCells)
        .build();
  }

  private void addGlider(List<IBoardLocation> aliveCells, int row, int col) {
    Glider.aliveCells.forEach(cell -> {
      aliveCells.add(new GridLocation(cell.getRow() + row, cell.getColumn() + col));
    });
  }
}
