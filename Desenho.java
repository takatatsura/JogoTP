import java.awt.*;
import javax.swing.*;

class Desenho extends JPanel{
  int n, x, y;
  Desenho() {
    setPreferredSize(new Dimension(500, 500));

  }

  public void move(int x, int y, int n) {
    this.n = n;
    this.x = x;
    this.y = y;
    repaint();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(new Color(0, 0, 0));
    g.fillRect(0, 0, 500, 500);
    g.setColor(new Color(200 - n * 200 / 100, 0, 255 - n * 255 / 100));
    g.drawOval(x - n / 2, y - n / 2, n, n);
  }



}
