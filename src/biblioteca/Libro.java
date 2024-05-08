package biblioteca;

class Libro {
    private String titolo, autore, casaEditrice;
    private int anno;
    private boolean inPrestito;


    public Libro(String titolo, String autore, String casaEditrice, int anno) {
        this.titolo = titolo;
        this.autore = autore;
        this.casaEditrice = casaEditrice;
        this.anno = anno;
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

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getCasaEditrice() {
        return casaEditrice;
    }

    public void setCasaEditrice(String casaEditrice) {
        this.casaEditrice = casaEditrice;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

}