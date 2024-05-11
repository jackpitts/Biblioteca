package biblioteca;

import java.util.Date;

class Libro {

    private String titolo, autore, casaEditrice, genere;
    private int anno, quantitaDisponibile;
    private boolean inPrestito;
    private Date data;

    public Libro(String titolo, String autore, String casaEditrice, String genere, int anno, int quantitaDisponibile, boolean inPrestito, Date data) {
        this.titolo = titolo;
        this.autore = autore;
        this.casaEditrice = casaEditrice;
        this.genere = genere;
        this.anno = anno;
        this.quantitaDisponibile = quantitaDisponibile;
        this.inPrestito = inPrestito;
        this.data = data;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
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

    public boolean isInPrestito() {
        return inPrestito;
    }

    public void setInPrestito(boolean inPrestito) {
        this.inPrestito = inPrestito;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void prendiInPrestito() {
        inPrestito = true;
    }

    public void restituisci() {
        inPrestito = false;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public int getQuantitaDisponibile() {
        return quantitaDisponibile;
    }

    public void setQuantitaDisponibile(int quantitaDisponibile) {
        this.quantitaDisponibile = quantitaDisponibile;
    }
}
