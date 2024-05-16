package iut.prj2024.jeu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Jeu Akari
 * @author Fabrice PELLEAU
 * @since  Semaine IHM 2024
 */
public class JeuAraki {
    private Cellule[][] cellules;
    private int         largeur;
    private int         hauteur;

    //==============================================================
    // Méthodes publiques
    //==============================================================

    public JeuAraki(int largeur, int hauteur) {
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

    /**
     * Placer une ampoule et mettre à jour les cellules qu'elle éclaire.
     * @return Le code de validation du placement, si il est différent de ReponsePlacement.AUCUNE_ERREUR, l'ampoule n'a pas été placée
     */
    public ReponsePlacement placerAmpoule(int x, int y) {
        ReponsePlacement erreur = peutPlacerAmpoule(x, y);
        if (erreur == ReponsePlacement.AJOUTE_AMPOULE) {
            cellules[x][y].setType(TypeCellule.AMPOULE);
            allumerAmpoule(x, y);
        } else if (erreur == ReponsePlacement.SUPPRIME_AMPOULE) {
            cellules[x][y].setType(TypeCellule.VIDE);
            allumerAmpoules();
        }
        return erreur;
    }
    /**
     * Charger une grille au format ASCII depuis un Stream
     * => à utiliser si le fichier est dans 'resources/'
     * @param cheminFichier
     * @return true si chargement ok
     * @throws IllegalArgumentException
     */
    public boolean chargerGrilleFromStream(InputStream in) {
        return chargerGrilleFromInputStream(in);
    }
    /**
     * Charger une grille au format ASCII depuis un Fichier externe
     * @param cheminFichier
     * @return true si chargement ok
     * @throws IllegalArgumentException, FileNotFoundException
     */
    public boolean chargerGrilleFromFileName(String cheminFichier) throws FileNotFoundException {
        return chargerGrilleFromInputStream(new FileInputStream(cheminFichier));
    }
    /**
     * Charger une grille au format ASCII depuis un Fichier externe (au format File)
     * @param cheminFichier
     * @return true si chargement ok
     * @throws IllegalArgumentException, FileNotFoundException
     */
    public boolean chargerGrilleFromFile(File fichierExterne) throws FileNotFoundException {
        return chargerGrilleFromInputStream(new FileInputStream(fichierExterne));
    }
    //==============================================================
    // Méthodes privées
    //==============================================================
    private boolean chargerGrilleFromInputStream(InputStream in) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            int y = 0;

            while ((line = reader.readLine()) != null && y < hauteur) {
                if (line.length() != largeur) {
                    throw new IllegalArgumentException("La ligne n'a pas la largeur attendue.");
                }

                for (int x = 0; x < line.length(); x++) {
                    char c = line.charAt(x);
                    switch (c) {
                        case '.':
                            cellules[x][y] = new Cellule(TypeCellule.VIDE, 0);
                            break;
                        case '?':
                            cellules[x][y] = new Cellule(TypeCellule.MUR, -1);
                            break;
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                            int nombreAmpoulesNecessaires = Character.getNumericValue(c);
                            cellules[x][y] = new Cellule(TypeCellule.MUR, nombreAmpoulesNecessaires);
                            break;
                        default:
                            throw new IllegalArgumentException("Caractère non reconnu dans le fichier: " + c);
                    }
                }

                y++;
            }

            if (y != hauteur) {
                throw new IllegalArgumentException("Le nombre de lignes ne correspond pas à la hauteur attendue.");
            }

            return true;
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur de format de fichier : " + e.getMessage());
            return false;
        }
    }
    private ReponsePlacement peutPlacerAmpoule(int x, int y) {
        if (cellules[x][y].getType() == TypeCellule.MUR) {
            return ReponsePlacement.SUR_MUR;
        }
        if (cellules[x][y].getType() == TypeCellule.ILLUMINEE) {
            return ReponsePlacement.DEJA_ECLAIREE;
        }
        if (cellules[x][y].getType() == TypeCellule.AMPOULE) {
            return ReponsePlacement.SUPPRIME_AMPOULE;
        }
        // Vérifier la conformité autour des murs numérotés
        if (!verifieConformiteNombre(x, y)) {
            return ReponsePlacement.NON_CONFORME_NOMBRE;
        }

        return ReponsePlacement.AJOUTE_AMPOULE;
    }
    private boolean verifieConformiteNombre(int x, int y) {
        // On peut ajouter une ampoule SSI aucune des 4 cases adjacente n'a un MUR "plein"
        return murNonPlein(x+1,y)&&murNonPlein(x-1,y)&&murNonPlein(x,y+1)&&murNonPlein(x,y-1);
    }
    private boolean murNonPlein(int x, int y) {
        if ( x<0 || y<0 || x>largeur-1 || y>hauteur-1 
            || cellules[x][y].getType() != TypeCellule.MUR
            || cellules[x][y].getNombreAmpoulesNecessaires()==-1) {
            return true;
        }
        int nbAmpoule = compteAmpoule(x+1, y)+compteAmpoule(x-1, y)+compteAmpoule(x, y+1)+compteAmpoule(x, y-1);
        if ( nbAmpoule<cellules[x][y].getNombreAmpoulesNecessaires() ) {
            return true;
        } else {
            return false;
        }
    }
    private int compteAmpoule(int x, int y) {
        if (x>=0 && x<largeur && y>=0 && y<hauteur && cellules[x][y].getType() == TypeCellule.AMPOULE) {
            return 1;
        } else {
            return 0;
        }
    }
    private void allumerAmpoule(int x, int y) {
        updateDirection(x, y, 1, 0);  // Droite
        updateDirection(x, y, -1, 0); // Gauche
        updateDirection(x, y, 0, 1);  // Bas
        updateDirection(x, y, 0, -1); // Haut
    }
    private void allumerAmpoules() {
        // eteindre toutes les cellules vides
        for (int i = 0; i < largeur; i++) {
            for (int j = 0; j < hauteur; j++) {
                if (cellules[i][j].getType()==TypeCellule.ILLUMINEE) {
                    cellules[i][j].setType(TypeCellule.VIDE);
                }
            }
        }
        // allumer toutes les Ampoules
        for (int i = 0; i < largeur; i++) {
            for (int j = 0; j < hauteur; j++) {
                if (cellules[i][j].getType()==TypeCellule.AMPOULE) {
                    allumerAmpoule(i, j);
                }
            }
        }
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
    public boolean verifierVictoire() {
        // Phase 1 : il ne doit rester aucune case vide
        for (Cellule[] ligne : cellules) {
            for (Cellule cell : ligne) {
                if (cell.getType() == TypeCellule.VIDE) {
                    return false;  // Il reste des cellules non éclairées
                }
            }
        }
        // Phase 2 : tous les murs avec un numéro doivent être pleins
        for (int i = 0; i < largeur; i++) {
            for (int j = 0; j < hauteur; j++) {
                if (cellules[i][j].getType()==TypeCellule.MUR && cellules[i][j].getNombreAmpoulesNecessaires()>=0 && murNonPlein(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }
}