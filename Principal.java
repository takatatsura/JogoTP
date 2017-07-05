import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Principal extends JFrame {
  int classe; //1 - Cacador; 2 - Presa;
  Desenho d = new Desenho();
  Cacador c = new Cacador();
  Presa p = new Presa();

  Principal() {
    super("Paranoia");
    classe = 2;
    add(d);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    new ThreadDraw().start();
    addKeyListener(new ListenerMovement());
    pack();
    setVisible(true);
  }

  class ListenerMovement extends KeyAdapter {
    int movx, movy;
    public void keyPressed(KeyEvent e) {
      movx = 0;
      movy = 0;
      switch(e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
            movx = -20;
          break;
        case KeyEvent.VK_RIGHT:
            movx = 20;
          break;
        case KeyEvent.VK_UP:
            movy = -20;
          break;
        case KeyEvent.VK_DOWN:
            movy = 20;
          break;
      }
      if(classe == 1) {
        c.setX(c.getX() + movx);
        c.setY(c.getY() + movy);
      } else {
        p.setX(p.getX() + movx);
        p.setY(p.getY() + movy);
      }
    }
  }

  class ThreadDraw extends Thread{
    public void run() {
      for(int i = 0; i < 100; i += 2) {
        System.out.println(i + "");
        if(classe == 1) {
          d.move(c.getX(), c.getY(), i);
        } else {
          d.move(p.getX(), p.getY(), i);
        }
        try {
          Thread.sleep(200);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static void main(String[] args) {
    new Principal();
  }


}
