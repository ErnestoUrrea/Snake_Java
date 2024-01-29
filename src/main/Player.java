package src.main;

public class Player {
    public int x;
    public int y;
    private boolean newPoint;
    public int score;
    public boolean gameOver;
    public boolean gameStarted;
    final public int initialSize = 3;
    //private int[] direction = {0,0};

    //! player.score + player.initialSize guardar como una variable

    public int[][] body = new int[33*33][2]; // make dynamic depending on screen size

    public Player(int posx, int posy){
        this.x = posx;
        this.y = posy;
        this.score = 0;
        this.newPoint = false;
        this.gameOver = false;
        this.gameStarted = false;
        for(int i = 0; i < initialSize; i++) {
            body[i][0] = this.x;
            body[i][1] = this.y;
        }
    }

    public Player(){
        this(0, 0);
    }

    public boolean eat(Apple apple){
        this.newPoint = (apple.x == this.x) && (apple.y == this.y);
        return this.newPoint;
    }

    public void update(int dx, int dy){
        if(!gameOver){
            if(dx != 0 || dy != 0) this.gameStarted = true;

            if (newPoint) {
                this.score ++;
                System.out.println(this.score);
            }
            

            this.x += dx;
            this.y += dy;
            for(int i = (this.score + this.initialSize - 1); i > 0; i--) { // revisar edge cases
                body[i][0] = body[i-1][0];
                body[i][1] = body[i-1][1];
                if ((this.x == body[i][0] && this.y == body[i][1]) && gameStarted) gameOver = true;
            }
            body[0][0] = this.x;
            body[0][1] = this.y;
        }
    }
}
