package horsie.chess.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import horsie.chess.model.HorsieGameModel;
import horsie.chess.model.PlayerTurn;
import horsie.chess.model.Position;
import horsie.chess.model.SquareState;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import org.tinylog.Logger;



public class HorsieGameController {

    private final HorsieGameModel gameModel = new HorsieGameModel();

    // helps calculate valid moves compared
    // to a specific position
    private final int[][] moveDirections = {{2, -1}, {2, 1}, {-2, 1}, {-2, -1}, {1, 2}, {-1, 2}, {1, -2}, {-1, -2}};

    private boolean showValidMoves = false;

    @FXML
    private GridPane board;


    @FXML
    private void initialize() throws FileNotFoundException {
        createBoard();
        placePieces();
//        setSelectablePositions();
//        showSelectablePositions();
        Logger.debug("Board initialised");
    }

    /**
     * Filling the GridPane up with squares
     */
    private void createBoard() {
        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare();
                board.add(square, j, i);
            }
        }

    }

    /**
     *
     * @return Square on the board
     */
    private StackPane createSquare() {
        var square = new StackPane();
        square.getStyleClass().add("square");
        square.setOnMouseClicked((event) -> {
            try {
                handleMouseClick(event);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        return square;
    }


    /**
     * PLace the pieces on the board
     * @throws FileNotFoundException
     */
    public void placePieces() throws FileNotFoundException {

        var board = gameModel.getBoard();

        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                StackPane square = getSquare(new Position(i, j));

                switch (board[i][j]){
                    case BLACK_HORSE -> square.setBackground(new Background(new BackgroundImage(
                            getPieceImages(SquareState.BLACK_HORSE),
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundPosition.CENTER,
                            BackgroundSize.DEFAULT)));

                    case WHITE_HORSE -> square.setBackground(new Background(new BackgroundImage(
                            getPieceImages(SquareState.WHITE_HORSE),
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundPosition.CENTER,
                            BackgroundSize.DEFAULT)));

                    case FORBIDDEN -> square.setBackground(new Background(new BackgroundImage(
                            getPieceImages(SquareState.FORBIDDEN),
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundPosition.CENTER,
                            BackgroundSize.DEFAULT)));

                    default -> {}
                }
            }
        }
    }

    @FXML
    private void handleMouseClick(MouseEvent event) throws FileNotFoundException {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        var position = new Position(row, col);
        Logger.debug("Click on square {}", position);
        handleClickOnSquare(position);
    }

    private void handleClickOnSquare(Position position) throws FileNotFoundException {
        SquareState boardSquare = gameModel.getBoard()[position.getX()][position.getY()];
        switch (boardSquare) {
            case WHITE_HORSE -> {
                if (gameModel.getCurrentPlayer() == PlayerTurn.WHITE){
                    displayValidMovesVisibility();
                    Logger.debug("Valid moves: {}", gameModel.validMovesOfPiece(position).toString());
                }
            }
            case BLACK_HORSE -> {
                if (gameModel.getCurrentPlayer() == PlayerTurn.BLACK){
                    displayValidMovesVisibility();
                    Logger.debug("Valid moves: {}", gameModel.validMovesOfPiece(position).toString());
                }
            }
            case EMPTY -> {
                if (showValidMoves){
                    if (gameModel.isValidMove(position)){
                        undisplayValidMovesVisibility();
                        stepWithPiece(position);
                        checkIfGameHasFinished();
                    } else {
                        undisplayValidMovesVisibility();
                    }
                }
            }
        }
    }

    private void stepWithPiece(Position moveToPosition) throws FileNotFoundException {
        if (gameModel.isValidMove(moveToPosition)){
            Position pieceCurrentPoz;
            StackPane pieceCurrentSquare;
            StackPane moveToSquare = getSquare(moveToPosition);
            switch (gameModel.getCurrentPlayer()) {
                case WHITE -> {
                    pieceCurrentPoz = new Position(gameModel.getWhitePosition().getX(),
                            gameModel.getWhitePosition().getY());
                    pieceCurrentSquare = getSquare(pieceCurrentPoz);
                    moveToSquare.setBackground(new Background(new BackgroundImage(
                            getPieceImages(SquareState.WHITE_HORSE),
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundPosition.CENTER,
                            BackgroundSize.DEFAULT)));
                }
                case BLACK -> {
                    pieceCurrentPoz = new Position(gameModel.getBlackPosition().getX(),
                            gameModel.getBlackPosition().getY());
                    pieceCurrentSquare = getSquare(pieceCurrentPoz);
                    moveToSquare.setBackground(new Background(new BackgroundImage(
                            getPieceImages(SquareState.BLACK_HORSE),
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundPosition.CENTER,
                            BackgroundSize.DEFAULT)));
                }
                default -> {
                    Logger.error("Tried to move piece while the game has not started.");
                    Logger.debug("Player to move: {}", gameModel.getCurrentPlayer());
                    Logger.debug("Position to move to: {}", moveToPosition.toString());
                    throw new RuntimeException("Tried to move piece while the game has not started.");
                }
            }

            pieceCurrentSquare.setBackground(new Background(new BackgroundImage(
                    getPieceImages(SquareState.FORBIDDEN),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    BackgroundSize.DEFAULT)));

            gameModel.movePiece(moveToPosition);
        }
    }

    private boolean checkIfGameHasFinished(){

    }


    private void displayValidMovesVisibility(){
        showValidMoves = true;
        ArrayList<Position> validPositions = new ArrayList<>();
        switch (gameModel.getCurrentPlayer()){
            case WHITE -> validPositions = gameModel.validMovesOfPiece(gameModel.getWhitePosition());
            case BLACK -> validPositions = gameModel.validMovesOfPiece(gameModel.getBlackPosition());
            default -> {}
        }

        for (var x : validPositions){
            StackPane square = getSquare(x);
            square.getStyleClass().add("selected");
        }
        Logger.info("Valid moves disappeared");
    }

    private void undisplayValidMovesVisibility(){
        showValidMoves = false;
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                StackPane square = getSquare(new Position(i, j));
                square.getStyleClass().remove("selected");
            }
        }
    }

    /**
     * Returns the requested javafx Image
     * @param imgType determines what kind of image will be returned
     * @return javafx Image object
     * @throws FileNotFoundException the image is not found in the resources dir
     * @throws RuntimeException SquareState.EMPTY has no attached image
     */
    private Image getPieceImages(SquareState imgType) throws FileNotFoundException, RuntimeException {
        try{
            switch (imgType){
                case BLACK_HORSE -> {
                    FileInputStream fileStream = new FileInputStream(getClass().getClassLoader().
                            getResource("black_horsie.png").getFile().toString());

                    return new Image(fileStream, 70, 70, false, false);
                }

                case WHITE_HORSE -> {
                    FileInputStream fileStream = new FileInputStream(getClass().getClassLoader().
                            getResource("white_horsie.png").getFile().toString());

                    return new Image(fileStream, 70, 70, false, false);
                }

                case FORBIDDEN -> {
                    FileInputStream fileStream = new FileInputStream(getClass().getClassLoader().
                            getResource("XX.png").getFile().toString());

                    return new Image(fileStream, 70, 70, false, false);
                }

                default -> {
                    Logger.error("The empty square has no picture");
                    throw new RuntimeException("The empty square has no picture");
                }
            }

        } catch (FileNotFoundException e){
            Logger.debug("Picture of square has not found");
            throw new FileNotFoundException(e.getMessage());
        }
    }

    /**
     * Returns the square at the given position
     * @param position coordinates of the square
     *                wrapped in a Position obj
     * @return the square object
     */
    private StackPane getSquare(Position position) {
        for (var child : board.getChildren()) {
            if (GridPane.getRowIndex(child) == position.getX() && GridPane.getColumnIndex(child) == position.getY()) {
                return (StackPane) child;
            }
        }
        throw new AssertionError();
    }




}
