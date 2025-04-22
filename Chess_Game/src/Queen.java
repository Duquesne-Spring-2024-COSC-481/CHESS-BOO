import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    public Queen(String color) {
        super(color, 9);
    }

    @Override
    public List<Move> getPossibleMoves(ChessBoard board, int row, int col) {
        List<Move> moves = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}}; // All directions

        for (int[] dir : directions) {
            int currentRow = row + dir[0];
            int currentCol = col + dir[1];
            while (isValid(currentRow, currentCol)) {
                Piece target = board.getPiece(currentRow, currentCol);
                moves.add(new Move(row, col, currentRow, currentCol));
                if (target != null) {
                    if (!target.getColor().equals(getColor())) {
                        // Capture
                    }
                    break; // Stop in this direction after encountering a piece
                }
                currentRow += dir[0];
                currentCol += dir[1];
            }
        }
        return moves;
    }

    private boolean isValid(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    @Override
    public Piece copy() {
        return new Queen(this.getColor());
    }
}
