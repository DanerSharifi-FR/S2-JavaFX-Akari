package iut.prj2024;

import java.util.Scanner;

import iut.prj2024.jeu.ErreurPlacement;
import iut.prj2024.jeu.Grille;

public class JeuCLI {
    private Grille grille;
    private boolean jeuTermine = false;
    private Scanner scanner;

    public JeuCLI(int largeur, int hauteur) {
        grille = new Grille(largeur, hauteur);
        scanner = new Scanner(System.in);
    }

    public void demarrer() {
        while (!jeuTermine) {
            afficherGrille();
            System.out.println("Entrez les coordonnées 'x y' pour placer une ampoule (ou 'q' pour quitter) :");
            String input = scanner.nextLine();
            if ("q".equalsIgnoreCase(input)) {
                jeuTermine = true;
            } else {
                traiterEntree(input);
            }
        }
    }

    private void traiterEntree(String input) {
        String[] parts = input.split(" ");
        if (parts.length != 2) {
            System.out.println("Entrée invalide. Assurez-vous de formater l'entrée comme 'x y'.");
            return;
        }
        try {
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            ErreurPlacement erreur = grille.placerAmpoule(x, y);
            if (erreur != ErreurPlacement.AUCUNE_ERREUR) {
                System.out.println("Erreur lors du placement : " + erreur);
            } else if (grille.verifierVictoire()) {
                System.out.println("Félicitations ! Vous avez gagné !");
                jeuTermine = true;
            }
        } catch (NumberFormatException e) {
            System.out.println("Veuillez entrer des coordonnées valides.");
        }
    }

    private void afficherGrille() {
        for (int y = 0; y < grille.getHauteur(); y++) {
            for (int x = 0; x < grille.getLargeur(); x++) {
                if (grille.getCellule(x, y).getNombreAmpoulesNecessaires()>0) {
                    System.out.print(grille.getCellule(x, y).getNombreAmpoulesNecessaires() + " ");
                } else {
                    System.out.print(grille.getCellule(x, y).getType().getSymbol() + " ");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        //JeuCLI jeu = new Jeu(5, 5);  // Taille de la grille à 10x10 pour l'exemple
        //jeu.grille.chargerGrille("dataset/5x5/easy.txt");
        //jeu.grille.chargerGrille("dataset/5x5/hard.txt");

        JeuCLI jeu = new JeuCLI(3, 3);  // Taille de la grille à 10x10 pour l'exemple
        jeu.grille.chargerGrille("dataset/3x3/easy.txt");
        jeu.demarrer();
    }
}