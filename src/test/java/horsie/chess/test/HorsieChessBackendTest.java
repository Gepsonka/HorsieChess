package horsie.chess.test;

import horsie.chess.model.HorsieGameModel;
import horsie.chess.model.PlayerTurn;
import horsie.chess.model.Position;
import horsie.chess.model.SquareState;

import javafx.geometry.Pos;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Stream;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HorsieChessBackendTest {

    private HorsieGameModel underTest;

    @BeforeEach
    public void setup(){
        underTest = new HorsieGameModel();
    }

    private Stream<Position> positionProvider(){
        return Stream.of(new Position(), new Position(2, 5), new Position(1, 7));
    }

    /*
    Initialize a board from the three files and test for those.
     */
    private Stream<SquareState[][]> boardSupplier() throws FileNotFoundException {
        ArrayList<SquareState[][]> boards = new ArrayList<>();

        ClassLoader classLoader = getClass().getClassLoader();
        for (int i = 1; i <= 3; i++){
            // all board files include a state where black loses
            String resourceName = "board" + i + ".txt";
            File boardFile = new File(Objects.requireNonNull(classLoader.getResource(resourceName)).getFile());
            SquareState[][] board = new SquareState[8][8];
            Scanner scanner = new Scanner(boardFile);
            int lineNum=0;
            while (scanner.hasNext()){
                String line = scanner.nextLine();
                for (int s = 0; s < line.length(); s++){
                    switch (line.charAt(s)){
                        case '0':
                            board[lineNum][s]=SquareState.EMPTY;
                            break;
                        case 'W':
                            board[lineNum][s]=SquareState.WHITE_HORSE;
                            break;
                        case 'B':
                            board[lineNum][s]=SquareState.BLACK_HORSE;
                            break;
                        case 'X':
                            board[lineNum][s]=SquareState.FORBIDDEN;
                            break;
                    }
                }
                lineNum++;
            }
            boards.add(board.clone());
        }
        return boards.stream();
    }

    @Test
    public void testValidMovesOfPieceByGivenPositionWithOnePositionFromTheEdgeOfBoard(){
        // GIVEN
        // position is in the corner of the board
        Position poz = new Position(0,7);
//        ArrayList<Position> testPositions = new ArrayList<Position>();
//        testPositions.add(new Position(2,  6));
//        testPositions.add(new Position(1 , 5));


        // WHEN
        ArrayList<Position> result = underTest.validMovesOfPiece(poz);

        //THEN
        Assertions.assertEquals(result.size(), 2);
        Assertions.assertAll("result",
                () -> Assertions.assertEquals(result.get(0).getX(), 2),
                () -> Assertions.assertEquals(result.get(0).getY(), 6),
                () -> Assertions.assertEquals(result.get(1).getX(), 1),
                () -> Assertions.assertEquals(result.get(1).getY(), 5)
        );
    }

    @Test
    public void testValidMovesOfPieceByGivenPositionWithOnePositionFromTheCenterOfBoard(){
        // GIVEN
        // position is in the corner of the board
        Position poz = new Position(3,3);
//        ArrayList<Position> testPositions = new ArrayList<Position>();
//        testPositions.add(new Position(2,  6));
//        testPositions.add(new Position(1 , 5));


        // WHEN
        ArrayList<Position> result = underTest.validMovesOfPiece(poz);

        //THEN
        Assertions.assertEquals(result.size(), 8);
        Assertions.assertAll("result",
                () -> Assertions.assertEquals(result.get(0).getX(), 5),
                () -> Assertions.assertEquals(result.get(0).getY(), 2),

                () -> Assertions.assertEquals(result.get(1).getX(), 5),
                () -> Assertions.assertEquals(result.get(1).getY(), 4),

                () -> Assertions.assertEquals(result.get(2).getX(), 1),
                () -> Assertions.assertEquals(result.get(2).getY(), 4),

                () -> Assertions.assertEquals(result.get(3).getX(), 1),
                () -> Assertions.assertEquals(result.get(3).getY(), 2),

                () -> Assertions.assertEquals(result.get(4).getX(), 4),
                () -> Assertions.assertEquals(result.get(4).getY(), 5),

                () -> Assertions.assertEquals(result.get(5).getX(), 2),
                () -> Assertions.assertEquals(result.get(5).getY(), 5),

                () -> Assertions.assertEquals(result.get(6).getX(), 4),
                () -> Assertions.assertEquals(result.get(6).getY(), 1),

                () -> Assertions.assertEquals(result.get(7).getX(), 2),
                () -> Assertions.assertEquals(result.get(7).getY(), 1)
        );
    }

    @Test
    public void testIsValidMoveByGivenPositionFromStartingPositionWithOnePosition(){
        // GIVEN
        Position poz = new Position(2, 6);

        // WHEN
        boolean result = underTest.isValidMove(poz);

        // THEN
        Assertions.assertTrue(result);

    }

    @Test
    public void testMovePieceGivenWhiteTurnsFromStartingPosition(){
        // GIVEN
        Position poz = new Position(1,5);

        // WHEN
        Position result = underTest.movePiece(poz);

        // THEN
        Assertions.assertEquals(poz, result);
        Assertions.assertEquals(underTest.getCurrentPlayer(), PlayerTurn.BLACK);
        Assertions.assertEquals(underTest.getWhitePosition(), poz);
    }


    @ParameterizedTest
    @MethodSource("boardSupplier")
    public void testCheckPlayerLostGivenABoard(SquareState[][] board){
        // GIVEN
        // test starts with white steps one
        var game = new HorsieGameModel(board, PlayerTurn.WHITE);

        // WHEN
        // white always moves to that square
        // in this test
        game.movePiece(new Position(1,5));

        // THEN
        Assertions.assertEquals(PlayerTurn.WHITE, game.getPlayerWon() );

    }




}
