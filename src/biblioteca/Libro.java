package biblioteca;

class Libro {
    private String titolo;
    private boolean inPrestito;

    public Libro(String titolo) {
        this.titolo = titolo;
        this.inPrestito = false;
    }

    public String getTitolo() {
        return titolo;
    }

    public boolean isInPrestito() {
        return inPrestito;
    }

    public void prendiInPrestito() {
        inPrestito = true;
    }

    public void restituisci() {
        inPrestito = false;
    }

}
