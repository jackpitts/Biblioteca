package biblioteca;

import java.util.*;

class Biblioteca {

    private List<Libro> libri;
    private Map<Persona, List<Libro>> prestiti;
    XMLArchive archive = new XMLArchive();

    public Biblioteca() {
        this.libri = new ArrayList<>();
        this.prestiti = new HashMap<>();
    }

    public void acquisisce(Libro libro) {
        libri.add(libro);
    }

    public void prestito(Libro libro, Persona persona) {
        if (!libro.isInPrestito()) {
            libro.prendiInPrestito();
            prestiti.computeIfAbsent(persona, k -> new ArrayList<>()).add(libro);
        } else {
            System.out.println("Il libro è già in prestito.");
        }
    }

    public void restituzione(Libro libro) {
        if (libro.isInPrestito()) {
            libro.restituisci();
            prestiti.forEach((persona, libri) -> libri.remove(libro));
        } else {
            System.out.println("Il libro non è in prestito.");
        }
    }

    public List<String> libriInPrestitoPerPersona(Persona persona) {
        List<String> titoliLibri = new ArrayList<>();
        List<Libro> libriInPrestito = prestiti.getOrDefault(persona, Collections.emptyList());
        for (Libro libro : libriInPrestito) {
            titoliLibri.add(libro.getTitolo());
        }
        return titoliLibri;
    }

    public void eseguiBiblioteca(String utenteCorrente, XMLArchive archive) {
        ArrayList<String> storicoAzioniUtente = archive.getStoricoAzioniUtente(utenteCorrente);
        if (storicoAzioniUtente.isEmpty()) {
            System.out.println("Nessuno storico disponibile.");
        } else {
            System.out.println("\nStorico delle azioni:");
            for (String azione : storicoAzioniUtente) {
                System.out.println(azione);
            }
        }
    }
}
