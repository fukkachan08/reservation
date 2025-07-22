package app.gui;

import app.AppException;
import app.reservation.ReserveRoomForm;
import util.DateUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class ReservationPanel extends JPanel {

    // ▼▼▼ 画面の部品を定義（宿泊数フィールドを追加） ▼▼▼
    private JTextField nameField = new JTextField(15);
    private JTextField dateField = new JTextField("2024/07/18", 10);
    private JComboBox<String> roomTypeComboBox;
    private JTextField peopleField = new JTextField(5);
    private JTextField nightsField = new JTextField(5); // 宿泊数フィールド
    private JButton reserveButton = new JButton("この内容で予約する");
    // ▲▲▲ 画面の部品を定義（宿泊数フィールドを追加） ▲▲▲

    public ReservationPanel() {
        // ▼▼▼ レイアウトを7行に変更 ▼▼▼
        setLayout(new GridLayout(7, 2, 10, 10));
        // ▲▲▲ レイアウトを7行に変更 ▲▲▲

        String[] roomTypes = {
            "ツイン (10,000円, 2名まで)",
            "ダブル (15,000円, 2名まで)",
            "スイート (30,000円, 4名まで)"
        };
        roomTypeComboBox = new JComboBox<>(roomTypes);

        // ▼▼▼ 部品をパネルに追加（宿泊数フィールドを追加） ▼▼▼
        add(new JLabel("お名前:"));
        add(nameField);
        add(new JLabel("チェックイン日 (yyyy/mm/dd):"));
        add(dateField);
        add(new JLabel("部屋タイプ:"));
        add(roomTypeComboBox);
        add(new JLabel("人数:"));
        add(peopleField);
        add(new JLabel("宿泊数:"));
        add(nightsField);
        add(new JLabel(""));
        add(reserveButton);
        // ▲▲▲ 部品をパネルに追加（宿泊数フィールドを追加） ▲▲▲

        reserveButton.addActionListener(e -> {
            try {
                // 名前の検証
                String name = nameField.getText();
                if (name == null || name.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "お名前を入力してください。", "入力エラー", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 日付の検証
                String dateStr = dateField.getText();
                Date stayingDate = DateUtil.convertToDate(dateStr);
                if (stayingDate == null) {
                    JOptionPane.showMessageDialog(this, "日付の形式が正しくありません。(例: 2024/07/18)", "入力エラー", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 部屋タイプに応じた設定
                int selectedIndex = roomTypeComboBox.getSelectedIndex();
                int price;
                int maxCapacity;
                String type;
                if (selectedIndex == 0) { price = 10000; maxCapacity = 2; type = "twin";
                } else if (selectedIndex == 1) { price = 15000; maxCapacity = 2; type = "double";
                } else { price = 30000; maxCapacity = 4; type = "suite"; }

                // 人数の検証
                String peopleStr = peopleField.getText();
                if (peopleStr.contains(".")) {
                    JOptionPane.showMessageDialog(this, "人数には整数を入力してください。", "入力エラー", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int numberOfPeople = Integer.parseInt(peopleStr);
                if (numberOfPeople <= 0 || numberOfPeople > maxCapacity) {
                    JOptionPane.showMessageDialog(this, "人数の入力が正しくないか、部屋の最大宿泊人数を超えています。", "人数エラー", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // ▼▼▼ 宿泊数の検証を追加 ▼▼▼
                String nightsStr = nightsField.getText();
                if (nightsStr.contains(".")) {
                    JOptionPane.showMessageDialog(this, "宿泊数には整数を入力してください。", "入力エラー", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int numberOfNights = Integer.parseInt(nightsStr);
                if (numberOfNights <= 0) {
                    JOptionPane.showMessageDialog(this, "宿泊数には1以上の数値を入力してください。", "入力エラー", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // ▲▲▲ 宿泊数の検証を追加 ▲▲▲

                // ▼▼▼ 合計金額の計算式を更新 ▼▼▼
                int totalPrice = price * numberOfPeople * numberOfNights;
                // ▲▲▲ 合計金額の計算式を更新 ▲▲▲

                // 予約処理の呼び出し
                ReserveRoomForm form = new ReserveRoomForm();
                form.setStayingDate(stayingDate);
                form.setRoomType(type);
                String reservationNumber = form.submitReservation();

                // ▼▼▼ 完了メッセージに宿泊数と合計金額を追加 ▼▼▼
                String roomTypeName = roomTypeComboBox.getSelectedItem().toString().split(" ")[0];
                String message = "以下の内容で予約が完了しました。\n\n" +
                                 "お名前: " + name + " 様\n" +
                                 "部屋タイプ: " + roomTypeName + "\n" +
                                 "人数: " + numberOfPeople + "名\n" +
                                 "宿泊数: " + numberOfNights + "泊\n" +
                                 "合計金額: " + totalPrice + "円\n\n" +
                                 "予約番号: " + reservationNumber;
                JOptionPane.showMessageDialog(this, message, "予約完了", JOptionPane.INFORMATION_MESSAGE);
                // ▲▲▲ 完了メッセージに宿泊数と合計金額を追加 ▲▲▲

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "人数または宿泊数には有効な数値を入力してください。", "入力エラー", JOptionPane.ERROR_MESSAGE);
            } catch (AppException ex) {
                JOptionPane.showMessageDialog(this, "予約に失敗しました:\n" + ex.getMessage(), "予約エラー", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}