import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class ChessGUI extends JFrame implements ActionListener {

    private ChessBoard board;
    private JButton[][] squares;
    private GameController controller;
    private JPanel boardPanel;
    private JButton sourceSquare = null; // Keep track of the first clicked square
    private Map<Piece, Image> pieceImages;

    public ChessGUI(GameController controller) {
        this.controller = controller;
        this.board = controller.getBoard();
        this.squares = new JButton[8][8];
        this.pieceImages = loadPieceImages();

        setTitle("Java Chess");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        boardPanel = new JPanel(new GridLayout(8, 8));
        add(boardPanel, BorderLayout.CENTER);

        initializeBoard();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private Map<Piece, Image> loadPieceImages() {
        Map<Piece, Image> images = new HashMap<>();
        try {
            images.put(new Pawn("white"), new ImageIcon(getClass().getResource("/images/wpawn.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            images.put(new Rook("white"), new ImageIcon(getClass().getResource("/images/wrook.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            images.put(new Knight("white"), new ImageIcon(getClass().getResource("/images/wknight.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            images.put(new Bishop("white"), new ImageIcon(getClass().getResource("/images/wbishop.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            images.put(new Queen("white"), new ImageIcon(getClass().getResource("/images/wqueen.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            images.put(new King("white"), new ImageIcon(getClass().getResource("/images/wking.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));

            images.put(new Pawn("black"), new ImageIcon(getClass().getResource("/images/bpawn.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            images.put(new Rook("black"), new ImageIcon(getClass().getResource("/images/brook.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            images.put(new Knight("black"), new ImageIcon(getClass().getResource("/images/bknight.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            images.put(new Bishop("black"), new ImageIcon(getClass().getResource("/images/bbishop.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            images.put(new Queen("black"), new ImageIcon(getClass().getResource("/images/bqueen.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            images.put(new King("black"), new ImageIcon(getClass().getResource("/images/bking.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading piece images: " + e.getMessage());
            e.printStackTrace();
        }
        return images;
    }

    private void initializeBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton square = new JButton();
                square.putClientProperty("row", row);
                square.putClientProperty("col", col);
                square.addActionListener(this);
                squares[row][col] = square;
                boardPanel.add(square);

                // Set background color
                if ((row + col) % 2 == 0) {
                    square.setBackground(new Color(240, 217, 181)); // Light squares
                } else {
                    square.setBackground(new Color(181, 136, 99));   // Dark squares
                }

                updateSquare(row, col);
            }
        }
    }

    private void updateSquare(int row, int col) {
        JButton square = squares[row][col];
        Piece piece = board.getPiece(row, col);
        square.setIcon(null); // Clear any previous icon
        if (piece != null) {
            for (Map.Entry<Piece, Image> entry : pieceImages.entrySet()) {
                Piece keyPiece = entry.getKey();
                if (keyPiece.getClass().equals(piece.getClass()) && keyPiece.getColor().equals(piece.getColor())) {
                    square.setIcon(new ImageIcon(entry.getValue()));
                    break;
                }
            }
        }
    }

    public void updateBoardUI() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                updateSquare(row, col);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedSquare = (JButton) e.getSource();
        int row = (int) clickedSquare.getClientProperty("row");
        int col = (int) clickedSquare.getClientProperty("col");

        if (sourceSquare == null) {
            // First click: select a piece to move
            Piece piece = board.getPiece(row, col);
            if (piece != null && piece.getColor().equals(board.getCurrentPlayer())) {
                sourceSquare = clickedSquare;
                sourceSquare.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3)); // Highlight selected piece
            }
        } else {
            // Second click: select the destination square
            int startRow = (int) sourceSquare.getClientProperty("row");
            int startCol = (int) sourceSquare.getClientProperty("col");

            Move move = new Move(startRow, startCol, row, col);

            if (controller.isValidMove(move)) {
                controller.makeMove(move);
                updateBoardUI();
                board.switchPlayer(); // Simple turn switching for now

                // AI's turn (basic implementation - needs integration with ChessAI)
                if (board.getCurrentPlayer().equals("black")) {
                    Move aiMove = controller.getBestAIMove(); // Assuming this method exists in GameController
                    if (aiMove != null) {
                        controller.makeMove(aiMove);
                        updateBoardUI();
                        board.switchPlayer();
                    } else {
                        JOptionPane.showMessageDialog(this, "Checkmate or Stalemate!");
                    }
                }

            } else {
                JOptionPane.showMessageDialog(this, "Invalid move!");
            }

            // Reset selection
            sourceSquare.setBorder(BorderFactory.createEmptyBorder());
            sourceSquare = null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameController controller = new GameController(); // Initialize your GameController
            new ChessGUI(controller);
        });
    }
}