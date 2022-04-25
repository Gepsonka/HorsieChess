package horsie.chess.model;

import horsie.chess.model.SquareState;
import horsie.chess.model.PlayerTurn;
import horsie.chess.model.Position;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.tinylog.Logger;


public class HorsieGameModel {
    private SquareState[][] board = new SquareState[8][8];

    private PlayerTurn currentPlayer = PlayerTurn.NONE_OF_THEM;
    private Position whitePosition = new Position();
    private Position blackPosition = new Position();


    /**
     * Initialize the starting position of the board.
     * Black and white horses are in the opposite corners of the board.
     * starting position
     * black: (0, 7)
     * white: (7, 0)
     */
    public HorsieGameModel(){
        currentPlayer=PlayerTurn.WHITE;

        whitePosition.setX(7);
        whitePosition.setY(0);

        blackPosition.setX(0);
        blackPosition.setY(7);

        for (int i=0; i<=7; i++) {
            for (int j=0; j<8; j++) {
                if (i==0 && j==7) {
                    board[i][j] = SquareState.WHITE_HORSE;
                }
                else if (i==7 && j==0){
                    board[i][j] = SquareState.BLACK_HORSE;
                }
                else {
                    board[i][j] = SquareState.EMPTY;
                }
            }
        }

        Logger.debug("Horsie chess backend initialized");
        Logger.debug("White position: x: {}, y: {}", whitePosition.getX(), whitePosition.getY());
        Logger.debug("White position: x: {}, y: {}", blackPosition.getX(), blackPosition.getY());
    }

    /**
     * Initialize the starting position of the board with custom positions
     * @param whitePiece starting position of white
     * @param blackPiece starting position af black
     */
    public HorsieGameModel(Position whitePiece, Position blackPiece){
        currentPlayer=PlayerTurn.WHITE;

        whitePosition.setX(whitePiece.getX());
        whitePosition.setY(whitePiece.getY());

        blackPosition.setX(blackPiece.getX());
        blackPosition.setY(blackPiece.getY());

        for (int i=0; i<=7; i++) {
            for (int j=0; j<8; j++) {
                if (i==whitePiece.getX() && j==whitePiece.getY()) {
                    board[i][j] = SquareState.WHITE_HORSE;
                }
                else if (i==blackPiece.getX() && j==blackPiece.getY()){
                    board[i][j] = SquareState.BLACK_HORSE;
                }
                else {
                    board[i][j] = SquareState.EMPTY;
                }
            }
        }

        Logger.debug("Horsie chess backend initialized");
        Logger.debug("White position: x: {}, y: {}", whitePosition.getX(), whitePosition.getY());
        Logger.debug("White position: x: {}, y: {}", blackPosition.getX(), blackPosition.getY());
    }

    /**
     *
     * @param moveTo the coordinates of the square where the player wants to move the piece
     * @return logical value whether the move is valid or not
     * @throws RuntimeException if the game has not started yet we cannot validate moves
     */
    public boolean isValidMove(Position moveTo) throws RuntimeException {
        boolean moveValidation;
        switch (currentPlayer){
            case BLACK :
                moveValidation = validMovesOfPiece(blackPosition).contains(moveTo);
                break;
            case WHITE:
                moveValidation = validMovesOfPiece(whitePosition).contains(moveTo);
                break;
            default:
                Logger.error("No players selected at move");
                throw new RuntimeException("Bad value for currentPlayer (PlayerTurn.NONE_OF_THEM)");
        }

        return moveValidation;
    }

    /**
     *
     * @param currentPositionOfPiece position  to calculate the valid moves from
     * @return ArrayList of positions where the piece can be placed
     */
    public ArrayList<Position> validMovesOfPiece(Position currentPositionOfPiece){
        var validMovesPositions = new ArrayList<Position>();
        final int[][] moveDirections = {{2, -1}, {2, 1}, {-2, 1}, {-2, -1}, {1, 2}, {-1, 2}, {1, -2}, {-1, -2}};
        Arrays.stream(moveDirections)
                .forEach(poz -> {
                    if (poz[0] + currentPositionOfPiece.getX() > 0 && poz[0] + currentPositionOfPiece.getX() < 8 &&
                        poz[1] + currentPositionOfPiece.getY() > 0 && poz[1] + currentPositionOfPiece.getY() < 8 &&
                        board[poz[0] + currentPositionOfPiece.getX()][currentPositionOfPiece.getY()] != SquareState.BLACK_HORSE
                        && board[poz[0] + currentPositionOfPiece.getX()][currentPositionOfPiece.getY()] != SquareState.WHITE_HORSE
                        && board[poz[0] + currentPositionOfPiece.getX()][currentPositionOfPiece.getY()] != SquareState.FORBIDDEN){
                        validMovesPositions.add(new Position(poz[0] + currentPositionOfPiece.getX(),
                                poz[1] + currentPositionOfPiece.getY()));
                    }
                });

        return validMovesPositions;
    }

    public PlayerTurn getCurrentPlayer(){
        return currentPlayer;
    }

    public void setCurrentPlayer(PlayerTurn turn){
        currentPlayer = turn;
    }

    public SquareState[][] getBoard() {
        return board;
    }
}
