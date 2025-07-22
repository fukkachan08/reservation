package app.gui;

import app.AppException;
import app.ManagerFactory;
import domain.room.Room;
import domain.room.RoomManager;
import domain.room.RoomException;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EmptyRoomPanel extends JPanel {

    private JTextArea roomListArea = new JTextArea(10, 20);
    private JButton refreshButton = new JButton("一覧を更新");

    public EmptyRoomPanel() {
        setLayout(new BorderLayout(10, 10));

        // 部屋一覧を表示するテキストエリアの設定
        roomListArea.setEditable(false); // 編集不可にする
        JScrollPane scrollPane = new JScrollPane(roomListArea); // スクロールできるようにする

        add(scrollPane, BorderLayout.CENTER);
        add(refreshButton, BorderLayout.SOUTH);

        // 更新ボタンがクリックされたときの処理
        refreshButton.addActionListener(e -> {
            updateRoomList();
        });
        
        // パネルが最初に表示されたときにもリストを更新する
        updateRoomList();
    }

    /**
     * 空き部屋のリストを最新の情報に更新します。
     */
    private void updateRoomList() {
        try {
            // RoomManager を通じて空き部屋リストを取得
            RoomManager roomManager = ManagerFactory.getInstance().getRoomManager();
            List<Room> emptyRooms = roomManager.getEmptyRooms();

            // テキストエリアをクリア
            roomListArea.setText("");

            if (emptyRooms.isEmpty()) {
                roomListArea.setText("現在、空き部屋はありません。");
            } else {
                roomListArea.append("--- 空き部屋一覧 ---\n");
                for (Room room : emptyRooms) {
                    roomListArea.append("部屋番号: " + room.getRoomNumber() + "\n");
                }
            }
        } catch (RoomException ex) {
            JOptionPane.showMessageDialog(this, "空き部屋情報の取得に失敗しました:\n" + ex.getMessage(), "エラー", JOptionPane.ERROR_MESSAGE);
        }
    }
}