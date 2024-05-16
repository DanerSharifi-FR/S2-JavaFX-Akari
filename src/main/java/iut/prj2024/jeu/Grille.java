package iut.prj2024.jeu;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Grille {
    private Cellule[][] cellules;
    private int         largeur;
    private int         hauteur;



    public Grille(int largeur, int hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.cellules = new Cellule[largeur][hauteur];
        // Initialiser avec des cellules vides
        for (int i = 0; i < largeur; i++) {
            for (int j = 0; j < hauteur; j++) {
                cellules[i][j] = new Cellule(TypeCellule.VIDE, 0);
            }
        }
    }
    public int getLargeur() {
        return largeur;
    }
    public int getHauteur() {
        return hauteur;
    }
    public Cellule getCellule(int x, int y) {
        return this.cellules[x][y];
    }

    public boolean chargerGrille(String cheminFichier) throws IllegalArgumentException {
        AtomicInteger y = new AtomicInteger(0);

        try (Stream<String> lines = Files.lines(Paths.get(cheminFichier))) {
            lines.forEach(line -> {
                if (line.length() != largeur) {
                    throw new IllegalArgumentException("FORMAT DE FICHIER NON VALIDE : La ligne n'a pas la largeur attendue.");
                }
                int currentY = y.getAndIncrement();
                for (int x = 0; x < line.length(); x++) {
                    char c = line.charAt(x);
                    switch (c) {
                        case ' ':
                            cellules[x][currentY] = new Cellule(TypeCellule.VIDE, 0);
                            break;
                        case 'X':
                            cellules[x][currentY] = new Cellule(TypeCellule.MUR, 0);
                            break;
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                            int nombreAmpoulesNecessaires = Character.getNumericValue(c);
                            cellules[x][currentY] = new Cellule(TypeCellule.MUR, nombreAmpoulesNecessaires);
                            break;
                        default:
                            throw new IllegalArgumentException("FORMAT DE FICHIER NON VALIDE : Caractère non reconnu dans le fichier: " + c);
                    }
                }
            });

            if (y.get() != hauteur) {
                throw new IllegalArgumentException("Le nombre de lignes ne correspond pas à la hauteur attendue.");
            }

            return true;
        } catch (Exception e) {
            System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
            return false;
        }
    }

    /**
     * Cette méthode va placer une ampoule et mettre à jour les cellules qu'elle éclaire.
     * @param x
     * @param y
     * @return Le code de validation du placement, si il est différent de ErreurPlacement.AUCUNE_ERREUR, l'ampoule n'a pas été placée
     */
    public ErreurPlacement placerAmpoule(int x, int y) {
        ErreurPlacement erreur = peutPlacerAmpoule(x, y);
        if (erreur == ErreurPlacement.AUCUNE_ERREUR) {
            cellules[x][y].setType(TypeCellule.AMPLOULE);
            mettreAJourEclairage(x, y);
        }
        return erreur;
    }

    /**
     * Cette méthode vérifiera si placer une ampoule à une position donnée respecte les règles.
     * @param x
     * @param y
     * @return le code "ErreurPlacement" correspondant au coup (AUCUNE_ERREUR ssi  le coup est valide)
     */
    private ErreurPlacement peutPlacerAmpoule(int x, int y) {
        if (cellules[x][y].getType() == TypeCellule.MUR) {
            return ErreurPlacement.SUR_MUR;
        }
        if (cellules[x][y].getType() == TypeCellule.ILLUMINEE) {
            return ErreurPlacement.DEJA_ECLAIREE;
        }
        if (cellules[x][y].getType() == TypeCellule.AMPLOULE) {
            return ErreurPlacement.DEJA_AMPLOULE;
        }
        // Vérifier la conformité autour des murs numérotés
        if (!verifieConformiteNombre(x, y)) {
            return ErreurPlacement.NON_CONFORME_NOMBRE;
        }

        return ErreurPlacement.AUCUNE_ERREUR;
    }
    private boolean verifieConformiteNombre(int x, int y) {
        // Exemple simplifié pour vérifier autour d'une cellule avec un mur numéroté
        // Ici, on doit ajouter la logique pour compter les ampoules placées autour
        return true; // Simplifié pour l'exemple
    }

    /**
     * Quand une ampoule est placée, cette méthode éclaire les cellules jusqu'à rencontrer un mur ou le bord de la grille
     * @param x
     * @param y
     */
    private void mettreAJourEclairage(int x, int y) {
        updateDirection(x, y, 1, 0);  // Droite
        updateDirection(x, y, -1, 0); // Gauche
        updateDirection(x, y, 0, 1);  // Bas
        updateDirection(x, y, 0, -1); // Haut
    }

    private void updateDirection(int x, int y, int dx, int dy) {
        int nx = x + dx;
        int ny = y + dy;
        while (nx >= 0 && nx < cellules.length && ny >= 0 && ny < cellules[0].length && cellules[nx][ny].getType() != TypeCellule.MUR) {
            if (cellules[nx][ny].getType() == TypeCellule.VIDE) {
                cellules[nx][ny].setType(TypeCellule.ILLUMINEE);
            }
            nx += dx;
            ny += dy;
        }
    }


    /**
     * Vérifier si toutes les conditions pour gagner la partie sont remplies
     * @return vrai si la partie est gagnée
     */
    public boolean verifierVictoire() {
        for (Cellule[] ligne : cellules) {
            for (Cellule cell : ligne) {
                if (cell.getType() == TypeCellule.VIDE) {
                    return false;  // Il reste des cellules non éclairées
                }
            }
        }
        return true;
    }
}