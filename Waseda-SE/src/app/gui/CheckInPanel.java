package app.gui;

import app.AppException;
import app.checkin.CheckInRoomForm;

import javax.swing.*;
import java.awt.*;

public class CheckInPanel extends JPanel {

    private JTextField reservationNumberField = new JTextField(15);
    private JButton checkInButton = new JButton("チェックイン");

    public CheckInPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20)); // レイアウトを設定

        add(new JLabel("予約番号:"));
        add(reservationNumberField);
        add(checkInButton);

        // チェックインボタンがクリックされたときの処理
        checkInButton.addActionListener(e -> {
            String reservationNumber = reservationNumberField.getText();
            if (reservationNumber == null || reservationNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "予約番号を入力してください。", "エラー", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // 既存のチェックインロジックを呼び出す
                CheckInRoomForm form = new CheckInRoomForm();
                form.setReservationNumber(reservationNumber);
                String roomNumber = form.checkIn();

                // 結果をダイアログで表示
                String message = "チェックインが完了しました。\n部屋番号: " + roomNumber;
                JOptionPane.showMessageDialog(this, message, "チェックイン完了", JOptionPane.INFORMATION_MESSAGE);

            } catch (AppException ex) {
                JOptionPane.showMessageDialog(this, "チェックインに失敗しました:\n" + ex.getMessage(), "エラー", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}