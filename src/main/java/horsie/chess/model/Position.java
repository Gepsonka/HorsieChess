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
    public boolean equals(Object o){
        // TODO: continue
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
