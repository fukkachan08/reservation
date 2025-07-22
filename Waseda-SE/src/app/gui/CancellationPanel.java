package app.gui;

import app.AppException;
import app.cancel.CancelReservationControl;

import javax.swing.*;
import java.awt.*;

public class CancellationPanel extends JPanel {

    private JTextField reservationNumberField = new JTextField(15);
    private JComboBox<String> roomTypeComboBox;
    private JButton cancelButton = new JButton("予約をキャンセルする");

    public CancellationPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        String[] roomTypes = {"ツイン", "ダブル", "スイート"};
        roomTypeComboBox = new JComboBox<>(roomTypes);

        add(new JLabel("予約番号:"));
        add(reservationNumberField);
        add(new JLabel("部屋タイプ:"));
        add(roomTypeComboBox);
        add(cancelButton);

        cancelButton.addActionListener(e -> {
            String reservationNumber = reservationNumberField.getText();
            if (reservationNumber == null || reservationNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "予約番号を入力してください。", "エラー", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 確認ダイアログを表示
            int result = JOptionPane.showConfirmDialog(this,
                "本当にこの予約をキャンセルしますか？\n予約番号: " + reservationNumber,
                "キャンセル確認",
                JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                try {
                    CancelReservationControl control = new CancelReservationControl();
                    String type;
                    int idx = roomTypeComboBox.getSelectedIndex();
                    if (idx == 0) type = "twin"; else if (idx == 1) type = "double"; else type = "suite";
                    control.cancelReservation(reservationNumber, type);
                    JOptionPane.showMessageDialog(this, "予約をキャンセルしました。", "キャンセル完了", JOptionPane.INFORMATION_MESSAGE);
                } catch (AppException ex) {
                    JOptionPane.showMessageDialog(this, "キャンセルに失敗しました:\n" + ex.getMessage(), "エラー", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}