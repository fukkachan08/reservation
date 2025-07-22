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

            tabbedPane.addTab("予約", new ReservationPanel());
            tabbedPane.addTab("チェックイン", new CheckInPanel());
            tabbedPane.addTab("チェックアウト", new CheckOutPanel());
            tabbedPane.addTab("予約キャンセル", new CancellationPanel());
            tabbedPane.addTab("空き部屋一覧", new EmptyRoomPanel());


            frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
}