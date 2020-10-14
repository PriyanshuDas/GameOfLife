package models.displays;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

public class GridRendererV2 extends JFrame {
  private int width, height;
  private int rows, columns;
  private int cellSize;
  private List<DisplayPosition> cellsToDelete;
  private List<DisplayPosition> cellsToCreate;
  private List<DisplayCell> aliveCells;
  private Color aliveColor = new Color(34,139,34);
  private Color borderColor = Color.BLACK;
  BufferedImage bufferedImage;


  public GridRendererV2(String title, int width, int height, int rows, int columns) {
    super(title);
    this.width = width;
    this.height = height;
    this.rows = rows;
    this.columns = columns;
    this.cellSize = Math.min(
        Math.max(this.height / this.rows, 4),
        Math.max(this.width / this.columns, 4));
    cellsToCreate = new ArrayList<>();
    cellsToDelete = new ArrayList<>();

    setSize(width, height);
    setResizable(false);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setVisible(true);
    setBackground(Color.BLACK);
    bufferedImage =
        new BufferedImage( this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
  }

  public void animation(Graphics g) {
    super.paint(g);
    cellsToDelete.forEach(cell -> {
      GridCell gridCell = new GridCell(cell.getRow(), cell.getColumn(),
          cellSize);
      g.clearRect(gridCell.getX(), gridCell.getY(), gridCell.getWidth(), gridCell.getHeight());
    });
    cellsToCreate.forEach(cell -> {
      GridCell gridCell = new GridCell(cell.getRow(), cell.getColumn(),
          cellSize);
      g.setColor(borderColor);
      g.drawRect(gridCell.getX(), gridCell.getY(), gridCell.getWidth(), gridCell.getHeight());
      g.setColor(aliveColor);
      g.fillRect(gridCell.getX()+1, gridCell.getY()+1, gridCell.getWidth()-1, gridCell.getHeight()-1);
    });
  }

  public void paint(Graphics g) {
    animation(bufferedImage.getGraphics());
    g.drawImage(bufferedImage, 0, 0, null);
  }
  public void update(Graphics g){
    paint(g);
  }
//    g.drawRect(20, 20, 400, 400);
//    g.setColor(Color.BLUE);
//    g.fillRect(20, 20, 400, 400);
//    g.setColor(Color.GREEN);
//    g.fillRect(400, 400, 400, 400);

  public void render(List<DisplayPosition> cellsToDelete, List<DisplayPosition> cellsToCreate) {
    this.cellsToCreate = cellsToCreate;
//    this.cellsToDelete = cellsToDelete;
//    validate();
    repaint();
  }
}
