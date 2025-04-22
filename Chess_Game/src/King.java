import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    public King(String color) {
        super(color, 1000); // High value for evaluation
    }

    @Override
    public List<Move> getPossibleMoves(ChessBoard board, int row, int col) {
        List<Move> moves = new ArrayList<>();
        int[][] possibleMoves = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},           {0, 1},
                {1, -1}, {1, 0}, {1, 1}
        };

        for (int[] move : possibleMoves) {
            int newRow = row + move[0];
            int newCol = col + move[1];
            if (isValid(newRow, newCol)) {
                Piece target = board.getPiece(newRow, newCol);
                if (target == null || !target.getColor().equals(getColor())) {
                    // TODO: Check if the move puts the king in check
                    moves.add(new Move(row, col, newRow, newCol));
                }
            }
        }

        // TODO: Implement castling

        return moves;
    }

    private boolean isValid(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    @Override
    public Piece copy() {
        return new King(this.getColor());
    }
}