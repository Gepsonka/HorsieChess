package horsie.chess.test;

import horsie.chess.model.HorsieGameModel;
import horsie.chess.model.PlayerTurn;
import horsie.chess.model.Position;
import horsie.chess.model.SquareState;

import javafx.geometry.Pos;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.ArrayList;
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


}
