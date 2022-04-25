package horsie.chess.model;


/*
* These enums will represent a state on the board
 */
public enum SquareState {
    /*
    * A piece has already been here, so neither of the players can step here
     */
    FORBIDDEN,
    BLACK_HORSE,
    WHITE_HORSE,
    EMPTY,
    /*
    * This will help highlight the possible steps of the piece
     */
    POSSIBLE_STEP
}
