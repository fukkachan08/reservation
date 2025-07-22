package app.gui;

import app.AppException;
import app.reservation.LookupReservationForm;
import domain.reservation.Reservation;
import util.DateUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Panel for looking up reservation information
 */
public class ReservationLookupPanel extends JPanel {

    private JTextField reservationNumberField = new JTextField(15);
    private JButton searchButton = new JButton("検索");

    private JLabel nameLabel = new JLabel();
    private JLabel dateLabel = new JLabel();
    private JLabel nightsLabel = new JLabel();
    private JLabel roomTypeLabel = new JLabel();
    private JLabel peopleLabel = new JLabel();

    public ReservationLookupPanel() {
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(new JLabel("予約番号:"));
        inputPanel.add(reservationNumberField);
        inputPanel.add(searchButton);
        add(inputPanel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        infoPanel.add(new JLabel("お名前:"));
        infoPanel.add(nameLabel);
        infoPanel.add(new JLabel("チェックイン日:"));
        infoPanel.add(dateLabel);
        infoPanel.add(new JLabel("宿泊数:"));
        infoPanel.add(nightsLabel);
        infoPanel.add(new JLabel("部屋タイプ:"));
        infoPanel.add(roomTypeLabel);
        infoPanel.add(new JLabel("人数:"));
        infoPanel.add(peopleLabel);
        add(infoPanel, BorderLayout.CENTER);

        searchButton.addActionListener(e -> lookupReservation());
    }

    private void lookupReservation() {
        String number = reservationNumberField.getText();
        if (number == null || number.isEmpty()) {
            JOptionPane.showMessageDialog(this, "予約番号を入力してください。", "エラー", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            LookupReservationForm form = new LookupReservationForm();
            form.setReservationNumber(number);
            Reservation reservation = form.findReservation();
            if (reservation == null) {
                JOptionPane.showMessageDialog(this, "予約が見つかりません。", "結果", JOptionPane.INFORMATION_MESSAGE);
                clearLabels();
                return;
            }
            // 現在の実装では顧客名などの情報は保持していないため表示できない
            nameLabel.setText("N/A");
            dateLabel.setText(DateUtil.convertToString(reservation.getStayingDate()));
            nightsLabel.setText("N/A");
            roomTypeLabel.setText("N/A");
            peopleLabel.setText("N/A");
        } catch (AppException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "エラー", JOptionPane.ERROR_MESSAGE);
            clearLabels();
        }
    }

    private void clearLabels() {
        nameLabel.setText("");
        dateLabel.setText("");
        nightsLabel.setText("");
        roomTypeLabel.setText("");
        peopleLabel.setText("");
    }
}
