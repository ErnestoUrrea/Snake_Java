package src.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{

    public boolean upPressed, downPressed, leftPressed, rightPressed, startPressed;
    public int[] newLastDirection = {0,0};
    public int[] lastDirection = {0,0};
    public int[] lastDDirection = {0,0}; //! cambiar nombre

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_W){
            upPressed = true;
            newLastDirection[0] = 0;
            newLastDirection[1] = -1;
        }
        if (keyCode == KeyEvent.VK_S){
            downPressed = true;
            newLastDirection[0] = 0;
            newLastDirection[1] = 1;
        }
        if (keyCode == KeyEvent.VK_A){
            leftPressed = true;
            newLastDirection[0] = -1;
            newLastDirection[1] = 0;
        }
        if (keyCode == KeyEvent.VK_D){
            rightPressed = true;
            newLastDirection[0] = 1;
            newLastDirection[1] = 0;
        }
        if(keyCode == KeyEvent.VK_SPACE){
            startPressed = true;
        }
        if (!(lastDirection[0] == -newLastDirection[0] && lastDirection[1] == -newLastDirection[1])){
            lastDDirection[0] = newLastDirection[0];
            lastDDirection[1] = newLastDirection[1];
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_W){
            upPressed = false;
        }
        if (keyCode == KeyEvent.VK_S){
            downPressed = false;
        }
        if (keyCode == KeyEvent.VK_A){
            leftPressed = false;
        }
        if (keyCode == KeyEvent.VK_D){
            rightPressed = false;
        }
        if(keyCode == KeyEvent.VK_SPACE){
            startPressed = false;
        }
    }

    public void resetDirection(){
        this.newLastDirection[0] = 0;
        this.newLastDirection[1] = 0;
        this.lastDirection[0] = 0;
        this.lastDirection[1] = 0;
        this.lastDDirection[0] = 0;
        this.lastDDirection[1] = 0;
    }
    
}
