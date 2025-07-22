package app.gui;

import java.awt.*;
import javax.swing.*;

public class MainGUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("ホテル予約システム");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 400);

            JTabbedPane tabbedPane = new JTabbedPane();

            // ▼▼▼ ここを修正 ▼▼▼
            tabbedPane.addTab("予約", new ReservationPanel());
            tabbedPane.addTab("チェックイン", new CheckInPanel());
            tabbedPane.addTab("チェックアウト", new CheckOutPanel());
            tabbedPane.addTab("予約キャンセル", new CancellationPanel()); // この行を追加
            tabbedPane.addTab("空き部屋一覧", new EmptyRoomPanel()); 

            // ▲▲▲ ここまで修正 ▲▲▲

            frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
}