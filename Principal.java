import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.concurrent.*;

class Principal extends JFrame {
  int classe; //1 - Cacador; 2 - Presa;
  int wwidth, wheight;
  int passo;
  Desenho d;
  Cacador c;
  Presa p;
  Guizos gu;
  Color bg, ch, gz;
  Random r;
  Queue<Sons> som = new ConcurrentLinkedQueue<Sons>();

  Principal() {
    super("Paranoia");
    classe = 1;
    wwidth = 800;
    wheight = 500;
    passo = 0;
    r = new Random();
    d = new Desenho();
    c = new Cacador(0, 0);
    p = new Presa(wwidth, wheight);
    gu = new Guizos(r.nextInt(wwidth), r.nextInt(wheight)); //ramdomizar
    System.out.println("x: " + gu.getX() + "\ny: " + gu.getY());
    bg = new Color(0, 0, 0);
    ch = new Color(255, 255, 255);
    gz = new Color(255, 255, 0);
    add(d);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    new ThreadDraw().start();
    addKeyListener(new ListenerMovement());
    pack();
    setVisible(true);
  }

  class Desenho extends JPanel {
    Sons s;
    Desenho() {
      setPreferredSize(new Dimension(wwidth, wheight));
    }

    public void move() {
      repaint();
    }

    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.setColor(bg);
      g.fillRect(0, 0, wwidth, wheight);
      g.setColor(ch);
      if(classe == 1) {
        g.fillOval(c.getX() - 5, c.getY() - 5, 10, 10);
      } else {
        g.fillOval(p.getX() - 5, p.getY() - 5, 10, 10);
      }
      Iterator<Sons> it = som.iterator();
      while(it.hasNext()) {
        s = it.next();
        s.setRaio(s.getRaio() + 1);
        g.setColor(new Color(255 - s.getRaio() * 255 / 100, 255 - s.getRaio() * 255 / 100, 255 - s.getRaio() * 255 / 100));
        g.drawOval(s.getX() - s.getRaio() / 2, s.getY() - s.getRaio() / 2, s.getRaio(), s.getRaio());
        if(s.getRaio() >= 100) it.remove();
      }
      g.setColor(gz);
      g.fillOval(gu.getX() - 5, gu.getY() - 5, 10, 10);
    }
  }

  class ListenerMovement extends KeyAdapter {
    int movx, movy;
    public void keyPressed(KeyEvent e) {
      movx = 0;
      movy = 0;
      switch(e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
            movx = -10;
            passo++;
          break;
        case KeyEvent.VK_RIGHT:
            movx = 10;
            passo++;
          break;
        case KeyEvent.VK_UP:
            movy = -10;
            passo++;
          break;
        case KeyEvent.VK_DOWN:
            movy = 10;
            passo++;
          break;
      }

      if(classe == 1 
        && c.getX() + movx <= wwidth && c.getX() + movx >= 0
        && c.getY() + movy <= wheight && c.getY() + movy >= 0) {
        c.setX(c.getX() + movx);
        c.setY(c.getY() + movy);
      } else if(classe == 2
        && p.getX() + movx <= wwidth && p.getX() + movx >= 0
        && p.getY() + movy <= wheight && p.getY() + movy >= 0) {
        p.setX(p.getX() + movx);
        p.setY(p.getY() + movy);
      }


      if(passo == 10) {
        if(classe == 1) {
          som.add(new Sons(c.getX(), c.getY()));
        } else {
          som.add(new Sons(p.getX(), p.getY()));
        }
        passo = 0;
      }
    }
  }

  class ThreadDraw extends Thread {
    public void run() {
      while(true) {
        try {
          Thread.sleep(1000/60);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        d.move();
      }
    }
  }

  public static void main(String[] args) {
    new Principal();
  }


}
