class Sons extends Posicao {
  private int raio;
  Sons(int x, int y) {
    raio = 0;
    setX(x);
    setY(y);
  }

  public void setRaio(int raio) {
    this.raio = raio;
  }

  public int getRaio() {
    return raio;
  }

}
