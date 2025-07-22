package app.gui;

import app.AppException;
import app.checkout.CheckOutRoomForm;

import javax.swing.*;
import java.awt.*;

public class CheckOutPanel extends JPanel {

    private JTextField roomNumberField = new JTextField(10);
    private JButton checkOutButton = new JButton("チェックアウト");

    public CheckOutPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20)); // レイアウトを設定

        add(new JLabel("部屋番号:"));
        add(roomNumberField);
        add(checkOutButton);

        // チェックアウトボタンがクリックされたときの処理
        checkOutButton.addActionListener(e -> {
            String roomNumber = roomNumberField.getText();
            if (roomNumber == null || roomNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "部屋番号を入力してください。", "エラー", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // 既存のチェックアウトロジックを呼び出す
                CheckOutRoomForm form = new CheckOutRoomForm();
                form.setRoomNumber(roomNumber);
                form.checkOut();

                // 結果をダイアログで表示
                JOptionPane.showMessageDialog(this, "チェックアウトが完了しました。", "チェックアウト完了", JOptionPane.INFORMATION_MESSAGE);

            } catch (AppException ex) {
                JOptionPane.showMessageDialog(this, "チェックアウトに失敗しました:\n" + ex.getMessage(), "エラー", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}