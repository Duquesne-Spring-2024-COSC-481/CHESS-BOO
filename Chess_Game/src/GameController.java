import java.util.List;

public class GameController {
    private ChessBoard board;
    private ChessAI ai; // Assuming you have a ChessAI instance

    public GameController() {
        this.board = new ChessBoard();
        this.ai = new ChessAI(3); // Initialize AI with a search depth
    }

    public ChessBoard getBoard() {
        return board;
    }

    public boolean isValidMove(Move move) {
        Piece piece = board.getPiece(move.getStartRow(), move.getStartCol());
        if (piece != null && piece.getColor().equals(board.getCurrentPlayer())) {
            List<Move> possibleMoves = piece.getPossibleMoves(board, move.getStartRow(), move.getStartCol());
            return possibleMoves.contains(move);
        }
        return false;
    }

    public void makeMove(Move move) {
        board.movePiece(move);
    }

    public Move getBestAIMove() {
        board.switchPlayer(); // Switch to the AI's turn temporarily for evaluation
        Move bestMove = ai.findBestMove(board.copy()); // Pass a copy to avoid modifying the actual board
        board.switchPlayer(); // Switch back to the current player
        return bestMove;
    }

    // You might need methods to check for game over conditions (checkmate, stalemate)
    
}
