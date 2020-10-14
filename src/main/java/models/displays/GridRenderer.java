package models.displays;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import utils.ColorUtils;

public class GridRenderer extends JFrame {
  private int width, height;
  private int rows, columns;
  private int cellSize;
  private List<DisplayPosition> cellsToDelete;
  private List<DisplayPosition> cellsToCreate;
  private Color borderColor;
  private Color backgroundColor;
  private Color cellColor;
  BufferedImage bufferedImage;


  public GridRenderer(
      String title, int width, int height, int rows, int columns,
      Color backgroundColor, Color cellColor, Color borderColor) {
    super(title);
    this.width = width;
    this.height = height;
    this.rows = rows;
    this.columns = columns;
    this.cellSize = Math.min(
        Math.max(this.height / this.rows, 4),
        Math.max(this.width / this.columns, 4));

    this.backgroundColor = backgroundColor;
    this.cellColor = cellColor;
    this.borderColor = borderColor;

    cellsToCreate = new ArrayList<>();
    cellsToDelete = new ArrayList<>();

    setSize(width, height);
    setResizable(false);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setVisible(true);
    getContentPane().setBackground(this.backgroundColor);
    bufferedImage =
        new BufferedImage( this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
  }
  public GridRenderer(
      String title, int width, int height, int rows, int columns) {
    this(title, width, height, rows, columns,
        Color.BLACK, ColorUtils.randomColor(), Color.BLACK);
  }

  public void animation(Graphics g) {
    super.paint(g);
    cellsToCreate.forEach(cell -> {
      GridCell gridCell = new GridCell(cell.getRow(), cell.getColumn(),
          cellSize);
      g.setColor(borderColor);
      g.drawRect(gridCell.getX(), gridCell.getY(), gridCell.getWidth(), gridCell.getHeight());
      g.setColor(cellColor);
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
  public void render(List<DisplayPosition> cellsToCreate) {
    this.cellsToCreate = cellsToCreate;
//    this.cellsToDelete = cellsToDelete;
    repaint();
  }
}
