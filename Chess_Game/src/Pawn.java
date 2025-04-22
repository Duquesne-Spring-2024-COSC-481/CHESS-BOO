import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(String color) {
        super(color, 1);
    }

    @Override
    public List<Move> getPossibleMoves(ChessBoard board, int row, int col) {
        List<Move> moves = new ArrayList<>();
        int direction = (getColor().equals("white")) ? 1 : -1;
        int startRow = (getColor().equals("white")) ? 1 : 6;

        // Move forward one square
        int nextRow = row + direction;
        if (isValid(nextRow, col) && board.getPiece(nextRow, col) == null) {
            moves.add(new Move(row, col, nextRow, col));
            // Move forward two squares from starting position
            if (row == startRow) {
                int nextNextRow = row + 2 * direction;
                if (isValid(nextNextRow, col) && board.getPiece(nextNextRow, col) == null) {
                    moves.add(new Move(row, col, nextNextRow, col));
                }
            }
        }

        // Capture diagonally
        int[] cols = {col - 1, col + 1};
        for (int nextCol : cols) {
            if (isValid(nextRow, nextCol)) {
                Piece target = board.getPiece(nextRow, nextCol);
                if (target != null && !target.getColor().equals(getColor())) {
                    moves.add(new Move(row, col, nextRow, nextCol));
                }
            }
        }

        // TODO: Implement en passant and promotion

        return moves;
    }

    private boolean isValid(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    @Override
    public Piece copy() {
        return new Pawn(this.getColor());
    }
}
