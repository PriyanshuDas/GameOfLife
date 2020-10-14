package models.grid;

import models.interfaces.GeneralException;

public class GridException extends GeneralException {
    public GridException(String message) {
        throw new RuntimeException(message);
    }
}
