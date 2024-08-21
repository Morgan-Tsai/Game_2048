import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Window_v3 extends JFrame {
    private int width = 395;
    private int height = 500;

    public Window_v3(String title) {
        super(title); //設置窗口標題
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 設置佈局為邊界佈局
        this.setSize(this.width, this.height); // 設置窗口大小
        this.setLocationRelativeTo(null); // 將窗口置於屏幕中央

        this.setResizable(false); // 禁止調整窗口大小
        this.setFocusable(true); // 設置窗口可獲得焦點，使KeyListener生效

        getContentPane().add(new Grid_v9(), BorderLayout.CENTER); // 將Grid_v1添加到窗口的中央
        this.setVisible(true); // 顯示窗口
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}