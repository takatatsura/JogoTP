class Guizos extends Posicao {
	private boolean existe;

	Guizos(int x, int y) {
		existe = true;
		setX(x);
		setY(y);
	}

	public void setExiste(boolean existe) {
		this.existe = existe;
	}

	public boolean isExiste() {
		return existe;
	}

}