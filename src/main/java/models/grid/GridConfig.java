package models.grid;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import models.interfaces.IBoardConfig;

import java.util.List;
import models.interfaces.IBoardLocation;

@Builder
@Getter
@Setter
public class GridConfig implements IBoardConfig {
    int rows, columns;
    List<IBoardLocation> aliveCells;
}
