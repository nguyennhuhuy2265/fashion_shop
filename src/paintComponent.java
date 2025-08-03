import javax.swing.*;
import java.awt.*;

public class paintComponent extends JPanel {

    private int[] data = {30, 80, 60, 100, 45}; // Dữ liệu ví dụ
    private String[] labels = {"A", "B", "C", "D", "E"};

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();
        int barWidth = width / data.length - 20;
        int max = 100;

        for (int i = 0; i < data.length; i++) {
            int barHeight = (int)((double)data[i] / max * (height - 50));
            int x = i * (barWidth + 20) + 30;
            int y = height - barHeight - 30;

            g2.setColor(new Color(70, 130, 180));
            g2.fillRect(x, y, barWidth, barHeight);
            g2.setColor(Color.BLACK);
            g2.drawString(labels[i], x + barWidth / 4, height - 10);
            g2.drawString(String.valueOf(data[i]), x + barWidth / 4, y - 5);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Biểu đồ cột đơn giản");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.add(new paintComponent());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
