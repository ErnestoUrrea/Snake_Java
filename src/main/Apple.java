package src.main;

// sugerencia: crear una clase objeto, y hacer que apple y player hereden de esa clase

public class Apple {
    public int x;
    public int y;

    public int eaten;

    public Apple(int posx, int posy){
        this.x = posx;
        this.y = posy;
        this.eaten = 0;
    }

    public Apple(){
        this(0, 0);
    }
}
