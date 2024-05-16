package iut.prj2024.jeu;

public enum TypeCellule {
    VIDE('.'),
    MUR('#'),
    AMPOULE('*'),
    ILLUMINEE('-');

    private final char symbol;

    TypeCellule(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }
}