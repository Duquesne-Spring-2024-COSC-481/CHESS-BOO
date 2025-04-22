import java.util.List;
import java.util.Random;

public class ChessAI {
    private int searchDepth;

    public ChessAI(int searchDepth) {
        this.searchDepth = searchDepth;
    }

    public Move findBestMove(ChessBoard board) {
        if (board.getCurrentPlayer().equals("white")) {
            return maximize(board, searchDepth).move;
        } else {
            return minimize(board, searchDepth).move;
        }
    }

    private ScoreMovePair maximize(ChessBoard board, int depth) {
        if (depth == 0 || isGameOver(board)) {
            return new ScoreMovePair(board.evaluate("white"), null);
        }

        double bestScore = Double.NEGATIVE_INFINITY;
        Move bestMove = null;
        List<Move> possibleMoves = board.getAllPossibleMoves("white");

        if (possibleMoves.isEmpty()) {
            return new ScoreMovePair(board.evaluate("white"), null); // No moves available
        }

        for (Move move : possibleMoves) {
            ChessBoard nextBoard = board.copy();
            nextBoard.movePiece(move);
            nextBoard.switchPlayer();
            double score = minimize(nextBoard, depth - 1).score;

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return new ScoreMovePair(bestScore, bestMove);
    }

    private ScoreMovePair minimize(ChessBoard board, int depth) {
        if (depth == 0 || isGameOver(board)) {
            return new ScoreMovePair(board.evaluate("white"), null); // Evaluation is from white's perspective
        }

        double bestScore = Double.POSITIVE_INFINITY;
        Move bestMove = null;
        List<Move> possibleMoves = board.getAllPossibleMoves("black");

        if (possibleMoves.isEmpty()) {
            return new ScoreMovePair(board.evaluate("white"), null); // No moves available
        }

        for (Move move : possibleMoves) {
            ChessBoard nextBoard = board.copy();
            nextBoard.movePiece(move);
            nextBoard.switchPlayer();
            double score = maximize(nextBoard, depth - 1).score;

            if (score < bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return new ScoreMovePair(bestScore, bestMove);
    }

    private boolean isGameOver(ChessBoard board) {
        // Basic check for no legal moves for the current player
        return board.getAllPossibleMoves(board.getCurrentPlayer()).isEmpty();
        // A more complete implementation would check for checkmate and stalemate
    }

    // Helper class to return both the score and the move
    private static class ScoreMovePair {
        double score;
        Move move;

        ScoreMovePair(double score, Move move) {
            this.score = score;
            this.move = move;
        }
    }
}