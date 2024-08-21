import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class Grid_v9 extends JPanel {
    private static final int TILE_RADIUS = 15; // 圓角矩形的角度半徑
    private static final int WIN_MARGIN = 20; // 窗口邊緣的邊距
    private static final int TILE_SIZE = 65; // 瓷磚的大小（寬度和高度）
    private static final int TILE_MARGIN = 15; // 瓷磚之間的邊距
    private static final String FONT = "Arial"; // 字型設定

    //按鈕相關變數宣告
    private static ImageIcon leave_icon; //離開按鈕icon
    private static ImageIcon playAgain_icon; //再玩一次按鈕icon
    private static ImageIcon reset_icon; //重來按鈕icon
    private static JButton resetButton; //重來按鈕
    private static JButton playAgainButton; //再玩一次按鈕
    private static JButton leaveButton; //離開按鈕
    private static Boolean showpg = false; //再玩一次按鈕是否顯示
    private static Boolean showleave = false; //離開按鈕是否顯示
    private static Boolean showReset = true;

    public Grid_v9() {
        super(true);
        setLayout(null);
        //0816更新
        initializeButtons();
    }
    
    private void initializeButtons() {
    	
    	reset_icon = new ImageIcon(getClass().getResource("/button_img/restart.png")); // 更改圖標為撤銷圖標
        resetButton = new JButton(reset_icon);
        resetButton.setBounds(130, 15, 44, 44);
        resetButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Game.BOARD.hasPreviousState()) {
                    Game.BOARD.restorePreviousState();
                    repaint();
                    Game.CONTROLS.unbind();
                    Game.CONTROLS.bind();
                    Game.WINDOW.requestFocus();
                } else {
                    JOptionPane.showMessageDialog(Game.WINDOW, "No more moves to undo!", "Undo", JOptionPane.INFORMATION_MESSAGE);
                    SwingUtilities.invokeLater(() -> {
                    Game.WINDOW.requestFocus();
                    });
                }
            }
        });
        
        playAgain_icon = new ImageIcon(getClass().getResource("/button_img/PlayAgain.png"));
        playAgainButton = new JButton(playAgain_icon);
        playAgainButton.setBounds(40, 290, 150, 44);
        playAgainButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("playAgain button clicked!");

                Game.BOARD.game_reset();
                repaint();
                Game.CONTROLS.unbind(); // 先解除鍵盤監聽器
                Game.CONTROLS.bind(); // 在取消重置時,重新綁定鍵盤輸入
                Game.WINDOW.requestFocus();
            }
        });
        
        leave_icon = new ImageIcon(getClass().getResource("/button_img/closen.png"));
        leaveButton = new JButton(leave_icon);
        leaveButton.setBounds(260, 290, 44, 44);
        leaveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("leave button clicked!");
                Game.BOARD.esc_game();
            }
        });
        
        add(leaveButton);
        add(playAgainButton);
        add(resetButton);
    }

    public void paintComponent(Graphics g2) {
        super.paintComponent(g2);

        Graphics2D g = ((Graphics2D) g2);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        drawBackground(g); // 繪畫出背景
        drawTitle(g); // 繪畫出標題

        drawScoreBoard(g); // 繪畫出記分板
        drawHighScoreBoard(g); // 繪畫出高分記分板
        drawBoard(g); // 繪畫出遊戲板
        
        //0816更新
        if (getShowpg() && getShowleave()) {
        		playAgainButton.setVisible(true);
        		leaveButton.setVisible(true);
        		playAgainButton.repaint();
                leaveButton.repaint();
        	}
        else {
              playAgainButton.setVisible(false);
              leaveButton.setVisible(false);
        }
        
        
        if (showReset && Game.BOARD.hasPreviousState()) {
        	  resetButton.setVisible(false);
        	  resetButton.repaint();} 
        else {
              resetButton.setVisible(false);
             } 


        g.dispose();
    }

    public static void setShowpg(Boolean visible) {
        showpg = visible;
    }

    public static Boolean getShowpg() {
        return showpg;
    }

    public static void setShowleave(Boolean visible) {
        showleave = visible;
    }

    public static Boolean getShowleave() {
        return showleave;
    }

    public static Boolean getShowreset() {
        return showReset;
    }

    public static void setShowreset(Boolean visible) {
        showReset = visible;
    }

    // 繪畫標題的方法
    private static void drawTitle(Graphics g) {
        g.setFont(new Font(FONT, Font.BOLD, 38)); // 設定字體：使用自定義字體，粗體，大小38
        g.setColor(ColorScheme.BRIGHT); // 設定顏色：使用亮色系
        g.drawString("2048", WIN_MARGIN, 50); // 繪製標題「2048」在指定位置
    }

    // 繪畫分數板的方法
    private void drawScoreBoard(Graphics2D g) {
        // 設定分數板的寬度和高度
        int width = 80;
        int height = 50;

        // 設定分數板在窗口中的偏移位置
        int xOffset = Game.WINDOW.getWidth() - WIN_MARGIN - width;
        int yOffset = 20;

        // 繪製分數板背景，使用圓角矩形
        g.fillRoundRect(xOffset - 10, yOffset - 10, width, height, TILE_RADIUS, TILE_RADIUS);
        // 設定字體：使用自定義字體，粗體，大小14
        g.setFont(new Font(FONT, Font.BOLD, 14));
        // 設定文字顏色為白色
        g.setColor(new Color(0XFFFFFF));
        // 繪製「SCORE」文字在分數板上
        g.drawString("Score", xOffset, yOffset + 10);
        // 再次設定字體（這一步其實不必要，因為字體沒有變）
        g.setFont(new Font(FONT, Font.BOLD, 14));
        // 繪製當前分數
        g.drawString(String.valueOf(Game.BOARD.getScore()), xOffset + 0, yOffset + 30);
    }

    // 繪畫高分分數板的方法
    private void drawHighScoreBoard(Graphics2D g) {
        // 設定分數板的寬度和高度
        int width = 80;
        int height = 50;

        // 設定分數板在窗口中的偏移位置
        int xOffset = 200;
        int yOffset = 20;

        // 繪製分數板背景，使用圓角矩形
        g.setColor(ColorScheme.BRIGHT);
        g.fillRoundRect(xOffset - 10, yOffset - 10, width, height, TILE_RADIUS, TILE_RADIUS);
        // 設定字體：使用自定義字體，粗體，大小14
        g.setFont(new Font(FONT, Font.BOLD, 12));
        // 設定文字顏色為白色
        g.setColor(new Color(0XFFFFFF));
        // 繪製「SCORE」文字在分數板上
        g.drawString("High Score", xOffset, yOffset + 10);
        // 再次設定字體（這一步其實不必要，因為字體沒有變）20240722 因為塞不下
        g.setFont(new Font(FONT, Font.BOLD, 14));
        // 繪製當前分數
        g.drawString(String.valueOf(Game.BOARD.getHighScore()), xOffset + 0, yOffset + 30);
    }

    // 繪畫背景的方法
    private static void drawBackground(Graphics g) {
        // 設定背景顏色
        g.setColor(ColorScheme.WINBG);
        // 繪製填滿整個窗口的矩形作為背景
        g.fillRect(0, 0, Game.WINDOW.getWidth(), Game.WINDOW.getHeight());
    }

    // 繪畫遊戲板的方法
    private static void drawBoard(Graphics g) {
        g.translate(WIN_MARGIN + 1, 80); // 將繪圖原點移動到瓷磚板板的起始位置
        g.setColor(ColorScheme.GRIDBG); // 設置瓷磚板背景顏色
        // // 繪製圓角矩形作為瓷磚板板的背景，留出邊緣空間
        g.fillRoundRect(0, 0, Game.WINDOW.getWidth() - (WIN_MARGIN * 2) - 20,
                Game.WINDOW.getHeight() - (WIN_MARGIN * 2) - 120, TILE_RADIUS, TILE_RADIUS);

        // 繪製每個瓷磚
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                drawTile(g, Game.BOARD.getTileAt(row, col), col, row);
            }
        }
    }

    // 繪畫瓷磚的方法
    private static void drawTile(Graphics g, Tile tile, int x, int y) {
        int value = tile.getValue();// 獲取瓷磚的值
        int xOffset = x * (TILE_MARGIN + TILE_SIZE) + TILE_MARGIN; // 計算瓷磚在 x 軸上的偏移量
        int yOffset = y * (TILE_MARGIN + TILE_SIZE) + TILE_MARGIN; // 計算瓷磚在 y 軸上的偏移量

        g.setColor(Game.COLORS.getTileBackground(value)); // 設置瓷磚背景顏色
        g.fillRoundRect(xOffset, yOffset, TILE_SIZE, TILE_SIZE, TILE_RADIUS, TILE_RADIUS); // 繪製圓角矩形作為瓷磚背景
        g.setColor(Game.COLORS.getTileColor(value)); // 設置瓷磚數字的顏色

        // 根據瓷磚值的大小選擇適當的字體大小
        final int size = value < 100 ? 36 : value < 1000 ? 32 : 24;
        final Font font = new Font(FONT, Font.BOLD, size);
        g.setFont(font); // 設置字體

        String s = String.valueOf(value); // 將瓷磚值轉換為字串
        final FontMetrics fm = g.getFontMetrics(font); // 獲取字體的測量信息

        final int w = fm.stringWidth(s); // 獲取字串的寬度
        final int h = -(int) fm.getLineMetrics(s, g).getBaselineOffsets()[2]; // 獲取字串的高度偏移

        if (value != 0) { // 如果瓷磚值不為零，則顯示瓷磚數字
            Game.BOARD.getTileAt(y, x).setPosition(y, x); // 更新瓷磚的位置信息
            // 繪製瓷磚數字
            g.drawString(s, xOffset + (TILE_SIZE - w) / 2, yOffset + TILE_SIZE - (TILE_SIZE - h) / 2 - 2);
        }
        drawGameResult(g); // 繪製遊戲結果
    }

    // 繪畫遊戲結果的方法
    private static void drawGameResult(Graphics g) {
        // 如果遊戲狀態為贏或輸，則顯示相應的消息
        if (Game.BOARD.getWonOrLost() != null && !Game.BOARD.getWonOrLost().isEmpty()) {
            g.setColor(new Color(255, 255, 255, 40)); // 設置半透明的白色
            g.fillRect(0, 0, Game.WINDOW.getWidth(), Game.WINDOW.getHeight()); // 填充整個視窗區域

            g.setColor(Color.DARK_GRAY); // 設置文字顏色

            if (Game.BOARD.getWonOrLost().equals("Won") || Game.BOARD.getWonOrLost().equals("Lost")) {
                g.setFont(new Font(FONT, Font.BOLD, 30)); // 設置大字體
                String resultMessage = Game.BOARD.getWonOrLost().equals("Won") ? "You Won!" : "You Lost!";
                g.drawString(resultMessage, 20, 80); // 繪製遊戲結果消息
                g.drawString("Your score is " + Game.BOARD.getScore(), 20, 130);
                g.drawString("High score is " + Game.BOARD.getHighScore(), 20, 180);

                setShowpg(true);
                setShowleave(true);
                showReset = false; // 在遊戲結束時隱藏RESET按鈕
            }
            Game.CONTROLS.unbind(); // 解除遊戲控制
        }
    }
}