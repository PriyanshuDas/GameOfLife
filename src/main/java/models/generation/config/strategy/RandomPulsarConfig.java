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
import models.patterns.PulsarGenerator;

@AllArgsConstructor
@Builder
public class RandomPulsarConfig implements GridConfigStrategy {
  private double pulsarGenerationProbability;
  private static Random rng = new Random();
  private static PulsarGenerator pulsarGenerator = new PulsarGenerator();

  @Override
  public GridConfig getConfig(int rows, int columns) {
    List<IBoardLocation> aliveCells = new ArrayList<>();
//    addGlider(aliveCells, 20, 20);
    int rowSize =
        PulsarGenerator.patternSpacingRow.get(1) + PulsarGenerator.patternSpacingRow.get(0);
    int columnSize =
        PulsarGenerator.patternSpacingColumn.get(1) + PulsarGenerator.patternSpacingColumn.get(0);
    for (int row = 0; row + rowSize < rows; row+=rowSize) {
      for (int col = 0; col + columnSize < columns; col+=columnSize) {
        double randomValue = rng.nextDouble();
        if (randomValue <= pulsarGenerationProbability) {
          addPulsarGenerator(aliveCells, row + PulsarGenerator.patternSpacingRow.get(0),
              col + PulsarGenerator.patternSpacingColumn.get(0));
        }
      }
    }
    return GridConfig.builder()
        .rows(rows)
        .columns(columns)
        .aliveCells(aliveCells)
        .build();
  }

  private void addPulsarGenerator(List<IBoardLocation> aliveCells, int row, int col) {
    pulsarGenerator.getPattern().forEach(cell -> {
      aliveCells.add(new GridLocation(cell.getRow() + row, cell.getColumn() + col));
    });
  }
}
