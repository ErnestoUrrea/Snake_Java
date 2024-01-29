package src.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner; // Import the Scanner class to read text files

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
    boolean start = false;

    // Screen Settings
    final int originalTileSize = 8;
    final int scale = 1;

    final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 33;
    final int maxScreenRow = 33;

    final int centerCol = maxScreenCol / 2;
    final int centerRow = maxScreenRow / 2;

    final int screenWidth  = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    // Time settings
    final double FPS = 4;
    final double drawInterval = 1000000000/FPS;
    final double SUPS = 10; // Snake Update Per Second

    // Player settings
    Player player = new Player(centerCol,centerRow);
    Apple apple = new Apple(
        (int)Math.floor(Math.random()*maxScreenCol),
        (int)Math.floor(Math.random()*maxScreenCol)
    );

    // Game settings
    final int safeStartAreaRadious = 14;
    int gameMenu = 0; // 0 = Start Menu, 1 = Game, 2 = Pause, 3 = Game Over
    boolean changeMenu = false;
    boolean previousStartValue = false;

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        
        while (gameThread != null) { // Game Loop

            if (!start) {
                this.start();
                start = true;
            }

            if(keyHandler.startPressed) { // Para que los menus no se cambien tan rapido, espera a que se suelte start y vuelva a presionarse
                if (!changeMenu && previousStartValue);
                else changeMenu = true;
            }
            previousStartValue = keyHandler.startPressed;
            // si ;

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime)/drawInterval;
            lastTime = currentTime;

            if(delta >= 1){
                update();
                repaint();
                delta--;
            }           
        }
    }

    public void start() {
        //! Revisar donde poner esto para que se ejecute al inicio
        boolean appleInsideSafeArea = true; 

        while (appleInsideSafeArea){
            appleInsideSafeArea = ((apple.x > centerCol-safeStartAreaRadious) && (apple.x < centerCol+safeStartAreaRadious) && (apple.y > centerRow-safeStartAreaRadious) && (apple.y < centerRow+safeStartAreaRadious));
            
            if (appleInsideSafeArea){
                apple.x = (int)Math.floor(Math.random()*maxScreenCol);
                apple.y = (int)Math.floor(Math.random()*maxScreenCol);
            } 
        }
    }

    public void update() {
        if (gameMenu == 0){
            if (changeMenu) {
                gameMenu = 1;
                changeMenu = false;
                keyHandler.resetDirection();
            }
        }
        if (gameMenu == 1){
            keyHandler.lastDirection[0] = keyHandler.lastDDirection[0];
            keyHandler.lastDirection[1] = keyHandler.lastDDirection[1];

            player.update(keyHandler.lastDirection[0], keyHandler.lastDirection[1]); 

            if(player.x >= maxScreenCol || player.x < 0 || player.y >= maxScreenRow || player.y < 0){
                player.gameOver = true;
            }

            if(player.gameOver){
                saveHighScore(player.score, "score/highscore.txt");
                gameMenu = 3;
                keyHandler.resetDirection();
                player = new Player(centerCol,centerRow);
                System.out.println("Game Over");
                start = false;
            }
            
            if (player.eat(apple)){
                this.generateNewApple();
            }
        }
        if(gameMenu == 3) {
            if (changeMenu) {
                gameMenu = 0;
                changeMenu = false;
            }
        }
    }

    private void generateNewApple() {
        apple = new Apple(
            (int)Math.floor(Math.random()*maxScreenCol),
            (int)Math.floor(Math.random()*maxScreenCol)
        );
        boolean appleInsideBody = true;
        boolean appleFound = false;
        while (appleInsideBody){
            appleFound = false;
            for(int[] bd:player.body){
                if (bd[0] == apple.x && bd[1] == apple.y) appleFound = true;
            }
            if(appleFound){
                apple = new Apple((int)Math.floor(Math.random()*maxScreenCol), (int)Math.floor(Math.random()*maxScreenCol));
            }
            appleInsideBody = appleFound;
        }
    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        if (gameMenu == 0) {
            paintGraphic(g2, "media/menu.txt");
        }
        if (gameMenu == 1) {
            g2.setColor(Color.WHITE);
            for (int i = 0; i < (player.score + player.initialSize); i++){
                g2.fillRect(player.body[i][0]*tileSize, player.body[i][1]*tileSize, tileSize, tileSize);
            }

            g2.setColor(Color.GRAY);
            g2.fillRect(apple.x*tileSize, apple.y*tileSize, tileSize, tileSize);
        }
        if (gameMenu == 3) {
            paintGraphic(g2, "media/gameover.txt");
        }
    }

    public void paintGraphic(Graphics2D g, String dir){
        try {
            File graphicsFile = new File(dir);
            Scanner graphicsScanner = new Scanner(graphicsFile);
            int row = 0;
            g.setColor(Color.WHITE);
            while (graphicsScanner.hasNextLine()) {
                String rowData = graphicsScanner.nextLine();
                for(int col = 0; col < rowData.length(); col++){
                    if (rowData.charAt(col) == '1') g.fillRect(col*tileSize, row*tileSize, tileSize, tileSize);
                }
                row++;
            }
            graphicsScanner.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("An error ocurred reading the graphics file.");
            e.printStackTrace();;
        }
    }
    public void saveHighScore(int newScore, String dir){
        // TODO: Hacer que se cree el archivo si no existe
        // TODO: Hacer que se muestre en pantalla
        // TODO: Revisar que pasa cuando esta vacio el archivo
        try {
            File scoreFile = new File(dir);
            Scanner scoreScanner = new Scanner(scoreFile);
            int score = scoreScanner.nextInt();
            if (newScore > score){
                System.out.println("New high score: " + newScore);
                System.out.println("Previous high score: " + score);
                scoreScanner.close();
                FileWriter scoreWriter = new FileWriter(dir);
                scoreWriter.write(newScore + "");
                scoreWriter.close();
            }
            else if (newScore == score){
                System.out.println("High score matched: " + newScore);
                scoreScanner.close();
            }
            else {
                System.out.println("Score: " + newScore);
                System.out.println("High score: " + score);
                scoreScanner.close();
            }
            
        }
        catch (FileNotFoundException e) {
            System.out.println("An error ocurred reading the score file.");
            e.printStackTrace();
        }
        catch (IOException e){
            System.out.println("An error ocurred writing the score file.");
            e.printStackTrace();
        }
    }
}
