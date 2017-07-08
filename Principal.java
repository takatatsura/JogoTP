import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.concurrent.*;

class Principal extends JFrame {
  int classe; //1 - Cacador; 2 - Presa;
  // acho que tem que ajustar o tamanho do passo e o da tela
  int wwidth, wheight;
  int passo;
  int tparado;
  Desenho d;
  //to pensando em ao invés de fazer uma classe Cacador e outra classe Presa, faz apenas uma classe Posicao
  //nao vai precisar verificar a classe toda vez que mexer com a posição do jogador atual
  //já que o usuário só vai ver a ele mesmo
  Cacador c;
  Presa p;
  Guizos gu;
  Color bg, ch, gz;
  Random r;
  //talvez agora dê pra usar a queue normal
  //antes tava dando ruim pq executando direto de dentro da thread
  Queue<Sons> som = new ConcurrentLinkedQueue<Sons>();

  Principal() {
    super("Paranoia");
    classe = 2;
    wwidth = 800;
    wheight = 500;
    passo = 0;
    tparado = 180;
    r = new Random();
    d = new Desenho();
    c = new Cacador(0, 0);
    p = new Presa(wwidth, wheight);
    if(classe == 1) {
      addMouseListener(new ListenerShot());
    } else {
      gu = new Guizos(r.nextInt(wwidth / 10) * 10, r.nextInt(wheight / 10) * 10);
      gz = new Color(255, 255, 0);
    }
    bg = new Color(0, 0, 0);
    ch = new Color(255, 255, 255);
    new ThreadDraw().start();
    addKeyListener(new ListenerMovement());
    add(d);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

  class Desenho extends JPanel {
    Sons s;
    Desenho() {
      setPreferredSize(new Dimension(wwidth, wheight));
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

      //Tentar navegar elemento a elemento ao inves de usar iterator.
      Iterator<Sons> it = som.iterator();
      while(it.hasNext()) {
        s = it.next();
        s.setRaio(s.getRaio() + 2);
        g.setColor(s.getColor());
        g.drawOval(s.getX() - s.getRaio() / 2, s.getY() - s.getRaio() / 2, s.getRaio(), s.getRaio());
        if(s.getRaio() >= 150) it.remove();
      }
      if(classe == 2) {
        g.setColor(gz);
        g.drawOval(gu.getX() - 5, gu.getY() - 5, 10, 10);
      }
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
          break;
        case KeyEvent.VK_RIGHT:
          movx = 10;
          break;
        case KeyEvent.VK_UP:
          movy = -10;
          break;
        case KeyEvent.VK_DOWN:
          movy = 10;
          break;
      }

      if(classe == 1
        && c.getX() + movx <= wwidth && c.getX() + movx >= 0
        && c.getY() + movy <= wheight && c.getY() + movy >= 0) {
        passo++;
        tparado = 180;
        c.setX(c.getX() + movx);
        c.setY(c.getY() + movy);
      } else if(classe == 2
        && p.getX() + movx <= wwidth && p.getX() + movx >= 0
        && p.getY() + movy <= wheight && p.getY() + movy >= 0) {
        passo++;
        tparado = 180;
        p.setX(p.getX() + movx);
        p.setY(p.getY() + movy);
        if(p.getX() == gu.getX() && p.getY() == gu.getY()) {
          //ponto++
          som.add(new Sons(gu.getX(), gu.getY(), 255, 255, 0));
          gu = new Guizos(r.nextInt(wwidth / 10) * 10, r.nextInt(wheight / 10) * 10);
        }
      }

      if(passo == 10) {
        if(classe == 1) {
          som.add(new Sons(c.getX(), c.getY(), 255, 255, 255));
        } else {
          som.add(new Sons(p.getX(), p.getY(), 255, 255, 255));
        }
        passo = 0;
      }
    }
  }

  class ListenerShot extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
      Point tiro = MouseInfo.getPointerInfo().getLocation();
      System.out.println("\nMouse:\nx = " + tiro.getX() + "\ny = " + tiro.getY());
    }
    //   d.paintComponent(g);
    //   g.setColor(new Color(255, 0, 0));
    //   g.drawLine(c.getX(), c.getY(), (int)ponto.getX(), (int)ponto.getY());
  }

  class ThreadDraw extends Thread {
    public void run() {
      while(true) {
        try {
          Thread.sleep(1000/60);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        tparado--;
        if(tparado <= 0 && tparado * (-1) % 20 == 0) {
          if(classe == 1) {
            som.add(new Sons(c.getX(), c.getY(), 255, 0, 0));
          } else {
            som.add(new Sons(p.getX(), p.getY(), 255, 0, 0));
          }
        }
        repaint();
      }
    }
  }

  public static void main(String[] args) {
    new Principal();
  }
}
