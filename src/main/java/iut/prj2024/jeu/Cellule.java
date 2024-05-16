package iut.prj2024.jeu;

public class Cellule {
    private TypeCellule type;
    private int nombreAmpoulesNecessaires; // -1 pou les murs sans chiffre

    public Cellule(TypeCellule type, int nombreAmpoulesNecessaires) {
        this.type = type;
        this.nombreAmpoulesNecessaires = nombreAmpoulesNecessaires;
    }

    public TypeCellule getType() {
        return type;
    }

    public void setType(TypeCellule type) {
        this.type = type;
    }

    public int getNombreAmpoulesNecessaires() {
        return nombreAmpoulesNecessaires;
    }

    public void setNombreAmpoulesNecessaires(int nombre) {
        this.nombreAmpoulesNecessaires = nombre;
    }
}