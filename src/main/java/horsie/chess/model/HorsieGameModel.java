package horsie.chess.model;

import horsie.chess.data.HorsieChessCacheData;
import horsie.chess.data.HorsieChessScoreboardData;
import horsie.chess.model.SquareState;
import horsie.chess.model.PlayerTurn;
import horsie.chess.model.Position;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;


import javax.xml.transform.Result;

/**
 * The whole backend of the ongoing game.
 * The virtual space where the game happens.
 */
public class HorsieGameModel {

    private final HorsieChessCacheData cache = new HorsieChessCacheData();
    private final HorsieChessScoreboardData scoreboard = new HorsieChessScoreboardData();
    private SquareState[][] board = new SquareState[8][8];

    private PlayerTurn currentPlayer = PlayerTurn.NONE_OF_THEM;

    private PlayerTurn playerWon = PlayerTurn.NONE_OF_THEM;
    private final Position whitePosition = new Position();
    private final Position blackPosition = new Position();


    /**
     * Initialize the starting {@link Position} of the board.
     * Black and white horses are in the opposite corners of the board.
     * starting {@link Position}
     * black: (0, 7)
     * white: (7, 0)
     */
    public HorsieGameModel(){
        currentPlayer=PlayerTurn.WHITE;

        whitePosition.setX(0);
        whitePosition.setY(7);

        blackPosition.setX(7);
        blackPosition.setY(0);

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
        Logger.debug("Black position: x: {}, y: {}", blackPosition.getX(), blackPosition.getY());
    }

    /**
     * Initialize the starting {@link Position} of the board with custom positions
     * @param whitePiece starting {@link Position} of white
     * @param blackPiece starting {@link Position} af black
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
        Logger.debug("Black position: x: {}, y: {}", blackPosition.getX(), blackPosition.getY());
    }

    /**
     *  For testing purposes.
     * @param board board to initialize the game with
     * @param player which player is next? {@link PlayerTurn}
     * @throws RuntimeException there must be exactly one horse on the board form each color
     */
    public HorsieGameModel(SquareState[][] board, PlayerTurn player) throws RuntimeException{
        this.board = board;
        this.currentPlayer = player;
        boolean gotWhiteHorse = false, gotBlackHorse = false;

        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (board[i][j] == SquareState.BLACK_HORSE){
                    if (gotBlackHorse){
                        throw new RuntimeException("There cannot be two black horses on the board");
                    } else {
                        gotBlackHorse = true;
                        blackPosition.setX(i);
                        blackPosition.setY(j);
                    }
                } else if (board[i][j] == SquareState.WHITE_HORSE){
                    if (gotWhiteHorse){
                        throw new RuntimeException("There cannot be two white horses on the board");
                    } else {
                        gotWhiteHorse = true;
                        whitePosition.setX(i);
                        whitePosition.setY(j);
                    }
                }
            }
        }

        if (!gotBlackHorse || !gotWhiteHorse){
            throw new RuntimeException("There must be 1 horse from each color on the board");
        }

    }

    /**
     * Moving one of the pieces on the backend
     * @param positionToMovePiece position, where to move the piece
     * @return If the move was successful returns the {@link Position}
     * where the piece was moved to.
     * @throws RuntimeException If one of the player has already won.
     * @throws RuntimeException If the game has not strated yet.
     * @throws RuntimeException Not valid move was tried .
     */
    public Position movePiece(Position positionToMovePiece) throws RuntimeException{
        if (playerWon != PlayerTurn.NONE_OF_THEM){
            throw new RuntimeException("One of the player has already won, neither of them can move.");
        }

        if (isValidMove(positionToMovePiece)){
            switch (currentPlayer) {
                case WHITE -> {
                    board[whitePosition.getX()][whitePosition.getY()] = SquareState.FORBIDDEN;
                    board[positionToMovePiece.getX()][positionToMovePiece.getY()] = SquareState.WHITE_HORSE;
                    whitePosition.setX(positionToMovePiece.getX());
                    whitePosition.setY(positionToMovePiece.getY());
                    Logger.debug(currentPlayer + " has moved to position: " + positionToMovePiece.toString());
                    currentPlayer = PlayerTurn.BLACK;
                }
                case BLACK -> {
                    board[blackPosition.getX()][blackPosition.getY()] = SquareState.FORBIDDEN;
                    board[positionToMovePiece.getX()][positionToMovePiece.getY()] = SquareState.BLACK_HORSE;
                    blackPosition.setX(positionToMovePiece.getX());
                    blackPosition.setY(positionToMovePiece.getY());
                    Logger.debug(currentPlayer + " has moved to position: " + positionToMovePiece.toString());
                    currentPlayer = PlayerTurn.WHITE;
                }
                case NONE_OF_THEM -> {
                    Logger.error("Cannot move a piece if the game has not started yet.");
                    throw new RuntimeException("Game has not started yet, so neither of the players can move");

                }
            }
            checkPlayerLost();

            return positionToMovePiece;
        }
        else {
            Logger.error("Bad move. To: {}, Color: {}", positionToMovePiece.toString(), currentPlayer);
            throw new RuntimeException("Bad move.");
        }
    }

    /*
     * Listener which called after every move to check if there
     * are any remaining moves for the player.
     */
    private void checkPlayerLost(){
        // be careful bc the turn of the players
        // is determined after a move happens
        switch (currentPlayer){
            case WHITE: // check if white lost
                if (validMovesOfPiece(whitePosition).isEmpty()){
                    playerWon = PlayerTurn.BLACK;
                    cache.setPlayerWon(PlayerTurn.BLACK);
                    scoreboard.incrementPlayerScore(cache.getPlayer2());
                    Logger.info("Black horse won");
                }
                break;
            case BLACK: // check if black lost
                if (validMovesOfPiece(blackPosition).isEmpty()){
                    playerWon = PlayerTurn.WHITE;
                    cache.setPlayerWon(PlayerTurn.WHITE);
                    scoreboard.incrementPlayerScore(cache.getPlayer1());
                    Logger.info("White horse won");
                }
                break;
        }
    }


    /**
     * Checks if the desired move is valid.
     * Requires for the {@link Position} to override the {@code compareTo()} method
     * @param moveTo the {@link Position} of the square where the player wants to move the piece
     * @return logical value whether the move is valid or not
     * @throws RuntimeException if the game has not started, yet, we cannot validate moves
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
     * Returns an {@link ArrayList<Position>} with all valid moves for the piece
     * @param currentPositionOfPiece {@link Position} to calculate the valid moves from
     * @return ArrayList of {@link Position} where the piece can be placed
     */
    public ArrayList<Position> validMovesOfPiece(Position currentPositionOfPiece){
        var validMovesPositions = new ArrayList<Position>();
        final int[][] moveDirections = {{2, -1}, {2, 1}, {-2, 1}, {-2, -1}, {1, 2}, {-1, 2}, {1, -2}, {-1, -2}};
        for (int[] moveDirection : moveDirections) {
            if (!areIndexesOnTheTable(moveDirection[0] + currentPositionOfPiece.getX(),
                    currentPositionOfPiece.getY() + moveDirection[1])) {
                continue;
            }

            if (board[moveDirection[0] + currentPositionOfPiece.getX()][currentPositionOfPiece.getY()
                    + moveDirection[1]]
                    != SquareState.BLACK_HORSE
                    && board[moveDirection[0] + currentPositionOfPiece.getX()][currentPositionOfPiece.getY()
                    + moveDirection[1]]
                    != SquareState.WHITE_HORSE
                    && board[moveDirection[0] + currentPositionOfPiece.getX()][currentPositionOfPiece.getY()
                    + moveDirection[1]]
                    != SquareState.FORBIDDEN) {
                validMovesPositions.add(new Position(moveDirection[0] + currentPositionOfPiece.getX(),
                        moveDirection[1] + currentPositionOfPiece.getY()));
            }
        }

        return validMovesPositions;
    }


    private boolean areIndexesOnTheTable(int x, int y){
        if (x < 0 || x >= 8 || y < 0 || y >= 8){
            return false;
        }

        return true;

    }

    public PlayerTurn getCurrentPlayer(){
        return currentPlayer;
    }

    public void setCurrentPlayer(PlayerTurn turn){
        currentPlayer = turn;
    }

    public Position getBlackPosition() {
        return blackPosition;
    }

    public Position getWhitePosition() {
        return whitePosition;
    }

    public PlayerTurn getPlayerWon() {
        return playerWon;
    }

    public SquareState[][] getBoard() {
        return board;
    }
}
