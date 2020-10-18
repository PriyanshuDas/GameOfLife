package models.displays;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JFrame;
import models.displays.DisplayCell.DisplayCellState;
import utils.ColorUtils;

public class GridRenderer extends JFrame {
  private int width, height;
  private int rows, columns;
  private int cellSize;
  private Collection<DisplayCell> cellsToRender;
  private Color backgroundColor;
  BufferedImage bufferedImage;
  GraphicsEnvironment graphics;
  GraphicsDevice device;


  public GridRenderer(
      String title, int width, int height, int rows, int columns,
      Color backgroundColor) {
    super(title);
    this.width = width;
    this.height = height;
    this.rows = rows;
    this.columns = columns;
    this.cellSize = Math.min(
        Math.max(this.height / this.rows, 2),
        Math.max(this.width / this.columns, 2));

    this.backgroundColor = backgroundColor;
    setUndecorated(true);

    cellsToRender = new ArrayList<>();

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
    this(title, width, height, rows, columns, Color.BLACK);
  }

  public void animation(Graphics g) {
    super.paint(g);
    cellsToRender.forEach(cell -> {
      g.setColor(cell.getColor());
      g.fillRect(
          cell.getPosition().getColumn()*cellSize,
          cell.getPosition().getRow()*cellSize,
          cellSize, cellSize);
//      g.setColor(cell.getBorderColor());
//      g.drawRect(
//          cell.getPosition().getColumn()*cellSize,
//          cell.getPosition().getRow()*cellSize,
//          cellSize, cellSize);
    });
  }

  public void paint(Graphics g) {
    animation(bufferedImage.getGraphics());
    g.drawImage(bufferedImage, 0, 0, null);
  }
  public void update(Graphics g){
    paint(g);
  }
  public void render(Collection<DisplayCell> cellsToRender) {
    this.cellsToRender = cellsToRender;
    repaint();
  }
}
