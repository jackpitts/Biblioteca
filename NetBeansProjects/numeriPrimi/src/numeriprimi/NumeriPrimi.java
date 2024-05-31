package numeriprimi;

import java.util.Scanner;

public class NumeriPrimi {

        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                try {
                    System.out.print("Inserisci un numero intero (per uscire digita 'E'): ");
                    String userInput = scanner.nextLine().trim();

                    if (userInput.equalsIgnoreCase("E")) {
                        System.out.println("Uscita dal programma.");
                        break;
                    }

                    int num = Integer.parseInt(userInput);

                    if (isPrime(num)) {
                        System.out.println(num + " e' un numero primo.");
                    } else {
                        System.out.println(num + " non e' un numero primo.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Errore: Inserisci un numero intero valido.");
                }
            }
        }

        private static boolean isPrime(int num) {
            if (num < 2) {
                return false;
            }
            for (int i = 2; i <= Math.sqrt(num); i++) {
                if (num % i == 0) {
                    return false;
                }
            }
            return true;
        }
    }
