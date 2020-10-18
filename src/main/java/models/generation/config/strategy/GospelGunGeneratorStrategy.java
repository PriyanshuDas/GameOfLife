package models.generation.config.strategy;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import models.grid.GridConfig;
import models.grid.GridLocation;
import models.interfaces.IBoardLocation;
import models.patterns.GospelGun;
import models.patterns.RandomUtils;

@AllArgsConstructor
@Builder
public class GospelGunGeneratorStrategy implements GridConfigStrategy{
  private double gunGenerationProbability;
  private static GospelGun gospelGun = new GospelGun();

  @Override
  public GridConfig getConfig(int rows, int columns) {

    List<IBoardLocation> aliveCells = new ArrayList<>();
//    addGlider(aliveCells, 20, 20);
    for (int row = 0; row + GospelGun.rowSize < rows; row+=GospelGun.rowSize) {
      for (int col = 0; col + GospelGun.colSize < columns; col+=GospelGun.colSize) {
        double randomValue = RandomUtils.randomDouble();
        if (randomValue <= gunGenerationProbability) {
          addGospelGun(aliveCells, row, col);
        }
      }
    }
    return GridConfig.builder()
        .rows(rows)
        .columns(columns)
        .aliveCells(aliveCells)
        .build();
  }

  private void addGospelGun(List<IBoardLocation> aliveCells, int row, int col) {
    List<GridLocation> pattern = gospelGun.getPattern();
    pattern.forEach(location ->
        aliveCells.add(new GridLocation(
        row + location.getRow(), col + location.getColumn())));
  }
}
