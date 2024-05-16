package iut.prj2024.jeu;

public enum ReponsePlacement {
    AJOUTE_AMPOULE,     // l'ampoule a bien été placée
    SUPPRIME_AMPOULE,   // l'ampoule a été supprimée
    SUR_MUR,            // ERREUR : cet emplacement est un mur
    DEJA_ECLAIREE,      // ERREUR : cet emplacement est déjà éclairé
    NON_CONFORME_NOMBRE // ERREUR : les murs adjacents ont trop de lumières
}
