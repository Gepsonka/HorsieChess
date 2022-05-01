package horsie.chess.model;

import java.util.Objects;

public class Position {
    private int x;
    private int y;

    public Position(){
        x=0;
        y=0;
    }

    public Position(int x, int y) throws RuntimeException{
        if (x > 7 || x < 0 || y > 7 || y < 0){
            throw new RuntimeException("Positions values must not be greater than 7 and must not be less than 0");
        }

        this.x=x;
        this.y=y;
    }

    @Override
    public String toString(){
        return "[x: " + getX() + ", y: " + getY() + "]";
    }
    @Override
    public boolean equals(Object o){
        if (!(o instanceof Position)){
            return false;
        }

        if (o == this){
            return true;
        }

        return ((Position) o).getX() == getX() && ((Position) o).getY() == getY();
    }

    public int getX(){
        return x;
    }

    public void setX(int x) throws RuntimeException {
        if (x > 7 || x < 0){
            throw new RuntimeException("Positions values must not be greater than 7 and must not be less than 0");
        }
        this.x = x;
    }
    public int getY() {
        return y;
    }

    public void setY(int y) throws RuntimeException{
        if (y > 7 || y < 0){
            throw new RuntimeException("Positions values must not be greater than 7 and must not be less than 0");
        }
        this.y = y;
    }
}
