import java.util.List;

public abstract class Piece {
    private String color;
    private int value;

    public Piece(String color, int value) {
        this.color = color;
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public int getValue() {
        return value;
    }

    public abstract List<Move> getPossibleMoves(ChessBoard board, int row, int col);

    public abstract Piece copy();
}
