import java.util.ArrayList;
import java.util.List;

public class ChessBoard {
    private Piece[][] board;
    private String currentPlayer;

    public ChessBoard() {
        board = new Piece[8][8];
        currentPlayer = "white";
        initializeBoard();
    }

    public ChessBoard(Piece[][] board, String currentPlayer) {
        this.board = copyBoard(board);
        this.currentPlayer = currentPlayer;
    }

    public Piece[][] getBoard() {
        return board;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer.equals("white")) ? "black" : "white";
    }

    private void initializeBoard() {
        // Initialize white pieces
        board[0][0] = new Rook("white");
        board[0][1] = new Knight("white");
        board[0][2] = new Bishop("white");
        board[0][3] = new Queen("white");
        board[0][4] = new King("white");
        board[0][5] = new Bishop("white");
        board[0][6] = new Knight("white");
        board[0][7] = new Rook("white");
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn("white");
        }

        // Initialize black pieces
        board[7][0] = new Rook("black");
        board[7][1] = new Knight("black");
        board[7][2] = new Bishop("black");
        board[7][3] = new Queen("black");
        board[7][4] = new King("black");
        board[7][5] = new Bishop("black");
        board[7][6] = new Knight("black");
        board[7][7] = new Rook("black");
        for (int i = 0; i < 8; i++) {
            board[6][i] = new Pawn("black");
        }

        // Initialize empty squares
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }
    }

    public Piece getPiece(int row, int col) {
        if (row >= 0 && row < 8 && col >= 0 && col < 8) {
            return board[row][col];
        }
        return null;
    }

    public void movePiece(Move move) {
        Piece piece = board[move.getStartRow()][move.getStartCol()];
        board[move.getStartRow()][move.getStartCol()] = null;
        board[move.getEndRow()][move.getEndCol()] = piece;
    }

    public List<Move> getAllPossibleMoves(String player) {
        List<Move> moves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board[i][j];
                if (piece != null && piece.getColor().equals(player)) {
                    moves.addAll(piece.getPossibleMoves(this, i, j));
                }
            }
        }
        return moves;
    }

    public ChessBoard copy() {
        return new ChessBoard(this.board, this.currentPlayer);
    }

    private Piece[][] copyBoard(Piece[][] original) {
        Piece[][] copy = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (original[i][j] != null) {
                    copy[i][j] = original[i][j].copy();
                }
            }
        }
        return copy;
    }

    // Basic evaluation function (can be improved significantly)
    public int evaluate(String maximizingPlayer) {
        int score = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board[i][j];
                if (piece != null) {
                    int pieceValue = piece.getValue();
                    if (piece.getColor().equals("white")) {
                        score += pieceValue;
                    } else {
                        score -= pieceValue;
                    }
                }
            }
        }
        return (maximizingPlayer.equals("white")) ? score : -score;
    }
}