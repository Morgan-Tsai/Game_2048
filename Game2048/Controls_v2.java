import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controls_v2 implements KeyListener {

    @Override
    // 當鍵盤按鍵被輸入時調用，這裡不需要實現
    public void keyTyped(KeyEvent e) {
    }

    // 當鍵盤按鍵被釋放時調用，這裡不需要實現
    public void keyReleased(KeyEvent e) {
    }


    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // 根據按下的鍵執行相應的操作
        switch (keyCode) {
            case KeyEvent.VK_UP:
                Game.BOARD.moveUp();
                break;
            case KeyEvent.VK_DOWN:
                Game.BOARD.moveDown();
                break;
            case KeyEvent.VK_LEFT:
                Game.BOARD.moveLeft();
                break;
            case KeyEvent.VK_RIGHT:
                Game.BOARD.moveRight();
                break;
            case KeyEvent.VK_ESCAPE:
                Game.WINDOW.dispose();
                break;
            default:
                break;
        }

        Game.BOARD.isGameOver(); // 檢查遊戲是否結束
        // Game.BOARD.show(); //顯示遊戲板
        Game.WINDOW.repaint(); // 重繪遊戲窗口
    }

    public void bind() {
        unbind(); // 先移除現有的監聽器
        Game.WINDOW.addKeyListener(this);
    }

    public void unbind() {
        Game.WINDOW.removeKeyListener(this); // 從遊戲窗口移除鍵盤監聽器
    }
}
