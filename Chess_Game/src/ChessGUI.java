import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class ChessGUI extends JFrame implements ActionListener {

    private ChessBoard board; // Represents the chessboard model
    private JButton[][] squares; // 2D array of buttons representing the board squares
    private GameController controller; // Handles game logic and rules
    private JPanel boardPanel; // Panel to hold the chessboard grid
    private JButton sourceSquare = null; // Tracks the first clicked square for a move
    private Map<Piece, Image> pieceImages; // Maps chess pieces to their corresponding images

    // Constructor to initialize the GUI
    public ChessGUI(GameController controller) {
        this.controller = controller;
        this.board = controller.getBoard(); // Get the chessboard from the controller
        this.squares = new JButton[8][8]; // Initialize the 8x8 grid of buttons
        this.pieceImages = loadPieceImages(); // Load images for chess pieces

        setTitle("Java Chess"); // Set the window title
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the application on exit
        setLayout(new BorderLayout()); // Use BorderLayout for the main layout

        boardPanel = new JPanel(new GridLayout(8, 8)); // Create an 8x8 grid for the board
        add(boardPanel, BorderLayout.CENTER); // Add the board to the center of the window

        initializeBoard(); // Set up the board with buttons and initial pieces

        pack(); // Adjust the window size to fit its components
        setLocationRelativeTo(null); // Center the window on the screen
        setVisible(true); // Make the window visible
    }

    // Method to load chess piece images
    private Map<Piece, Image> loadPieceImages() {
        Map<Piece, Image> images = new HashMap<>();
        try {
            // Load white piece images
            images.put(new Pawn("white"), new ImageIcon(getClass().getResource("/images/wpawn.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            images.put(new Rook("white"), new ImageIcon(getClass().getResource("/images/wrook.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            images.put(new Knight("white"), new ImageIcon(getClass().getResource("/images/wknight.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            images.put(new Bishop("white"), new ImageIcon(getClass().getResource("/images/wbishop.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            images.put(new Queen("white"), new ImageIcon(getClass().getResource("/images/wqueen.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            images.put(new King("white"), new ImageIcon(getClass().getResource("/images/wking.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));

            // Load black piece images
            images.put(new Pawn("black"), new ImageIcon(getClass().getResource("/images/bpawn.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            images.put(new Rook("black"), new ImageIcon(getClass().getResource("/images/brook.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            images.put(new Knight("black"), new ImageIcon(getClass().getResource("/images/bknight.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            images.put(new Bishop("black"), new ImageIcon(getClass().getResource("/images/bbishop.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            images.put(new Queen("black"), new ImageIcon(getClass().getResource("/images/bqueen.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            images.put(new King("black"), new ImageIcon(getClass().getResource("/images/bking.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));

        } catch (Exception e) {
            // Show an error message if images fail to load
            JOptionPane.showMessageDialog(this, "Error loading piece images: " + e.getMessage());
            e.printStackTrace();
        }
        return images;
    }

    // Method to initialize the chessboard UI
    private void initializeBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton square = new JButton(); // Create a button for each square
                square.putClientProperty("row", row); // Store the row index
                square.putClientProperty("col", col); // Store the column index
                square.addActionListener(this); // Add an action listener for clicks
                squares[row][col] = square; // Store the button in the grid
                boardPanel.add(square); // Add the button to the board panel

                // Set the background color of the square
                if ((row + col) % 2 == 0) {
                    square.setBackground(new Color(240, 217, 181)); // Light squares
                } else {
                    square.setBackground(new Color(181, 136, 99)); // Dark squares
                }

                updateSquare(row, col); // Update the square with the initial piece (if any)
            }
        }
    }

    // Method to update a specific square with the correct piece image
    private void updateSquare(int row, int col) {
        JButton square = squares[row][col];
        Piece piece = board.getPiece(row, col); // Get the piece at the given position
        square.setIcon(null); // Clear any previous icon
        if (piece != null) {
            // Find the matching image for the piece and set it as the icon
            for (Map.Entry<Piece, Image> entry : pieceImages.entrySet()) {
                Piece keyPiece = entry.getKey();
                if (keyPiece.getClass().equals(piece.getClass()) && keyPiece.getColor().equals(piece.getColor())) {
                    square.setIcon(new ImageIcon(entry.getValue()));
                    break;
                }
            }
        }
    }

    // Method to refresh the entire board UI
    public void updateBoardUI() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                updateSquare(row, col); // Update each square
            }
        }
    }

    // Handle user interactions with the board
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedSquare = (JButton) e.getSource(); // Get the clicked square
        int row = (int) clickedSquare.getClientProperty("row"); // Get the row index
        int col = (int) clickedSquare.getClientProperty("col"); // Get the column index

        if (sourceSquare == null) {
            // First click: select a piece to move
            Piece piece = board.getPiece(row, col);
            if (piece != null && piece.getColor().equals(board.getCurrentPlayer())) {
                sourceSquare = clickedSquare; // Store the selected square
                sourceSquare.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3)); // Highlight the selected piece
            }
        } else {
            // Second click: select the destination square
            int startRow = (int) sourceSquare.getClientProperty("row");
            int startCol = (int) sourceSquare.getClientProperty("col");

            Move move = new Move(startRow, startCol, row, col); // Create a move object

            if (controller.isValidMove(move)) {
                // If the move is valid, execute it
                controller.makeMove(move);
                updateBoardUI(); // Refresh the board
                board.switchPlayer(); // Switch the turn to the other player

                // AI's turn (basic implementation)
                if (board.getCurrentPlayer().equals("black")) {
                    Move aiMove = controller.getBestAIMove(); // Get the AI's move
                    if (aiMove != null) {
                        controller.makeMove(aiMove); // Execute the AI's move
                        updateBoardUI(); // Refresh the board
                        board.switchPlayer(); // Switch back to the human player
                    } else {
                        JOptionPane.showMessageDialog(this, "Checkmate or Stalemate!"); // End of game
                    }
                }

            } else {
                // Show an error message for invalid moves
                JOptionPane.showMessageDialog(this, "Invalid move!");
            }

            // Reset the selection
            sourceSquare.setBorder(BorderFactory.createEmptyBorder());
            sourceSquare = null;
        }
    }

    // Main method to launch the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameController controller = new GameController(); // Initialize the game controller
            new ChessGUI(controller); // Create and display the GUI
        });
    }
}