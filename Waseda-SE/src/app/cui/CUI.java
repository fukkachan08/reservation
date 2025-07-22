/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package app.cui;

import app.AppException;
import app.ManagerFactory;
import app.checkin.CheckInRoomForm;
import app.checkout.CheckOutRoomForm;
import app.reservation.ReserveRoomForm;
import domain.room.Room;
import domain.room.RoomException;
import domain.room.RoomManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import util.DateUtil;

/**
 * CUI class for Hotel Reservation Systems
 * */
public class CUI {

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private BufferedReader reader;

	CUI() {
		reader = new BufferedReader(new InputStreamReader(System.in));
	}

	private void execute() throws IOException {
		try {
			while (true) {
				int selectMenu;
				System.out.println("");
				System.out.println("Menu");
				System.out.println("1: Reservation");
				System.out.println("2: Check-in");
				System.out.println("3: Check-out");
				System.out.println("4: Show Empty Rooms");
				System.out.println("9: End");
				System.out.print("> ");

				try {
					String menu = reader.readLine();
					selectMenu = Integer.parseInt(menu);
				}
				catch (NumberFormatException e) {
					selectMenu = 0;
				}

				if (selectMenu == 9) {
					break;
				}

				switch (selectMenu) {
					case 1:
						reserveRoom();
						break;
					case 2:
						checkInRoom();
						break;
					case 3:
						checkOutRoom();
						break;
					case 4:
						showEmptyRooms();
						break;
				}
			}
			System.out.println("Ended");
		}
		catch (AppException e) {
			System.err.println("Error");
			System.err.println(e.getFormattedDetailMessages(LINE_SEPARATOR));
		}
		finally {
			reader.close();
		}
	}

	private void reserveRoom() throws IOException, AppException {
		System.out.println("Input arrival date in the form of yyyy/mm/dd");
		System.out.print("> ");

		String dateStr = reader.readLine();

		Date stayingDate = DateUtil.convertToDate(dateStr);
		if (stayingDate == null) {
			System.out.println("Invalid input");
			return;
		}

		System.out.println("Select room type:");
		System.out.println("1: Twin (10,000 JPY, max 2 people)");
		System.out.println("2: Double (15,000 JPY, max 2 people)");
		System.out.println("3: Suite (30,000 JPY, max 4 people)");
		System.out.print("> ");

		String roomTypeStr = reader.readLine();
		String selectedRoomType;
		int price;
		int maxCapacity; // 上限人数を保持する変数

		try {
			int roomType = Integer.parseInt(roomTypeStr);
			switch (roomType) {
				case 1:
					selectedRoomType = "Twin";
					price = 10000;
					maxCapacity = 2;
					break;
				case 2:
					selectedRoomType = "Double";
					price = 15000;
					maxCapacity = 2;
					break;
				case 3:
					selectedRoomType = "Suite";
					price = 30000;
					maxCapacity = 4;
					break;
				default:
					System.out.println("Invalid input");
					return;
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input");
			return;
		}

		// ▼▼▼ 人数入力と上限チェックの処理 ▼▼▼
		System.out.println("Input number of people");
		System.out.print("> ");
		int numberOfPeople;
		try {
			String numberOfPeopleStr = reader.readLine();
			// 小数点が含まれているかチェック
			if (numberOfPeopleStr.contains(".")) {
				System.out.println("Please enter a valid integer for the number of people.");
				return;
			}
			numberOfPeople = Integer.parseInt(numberOfPeopleStr);

			if (numberOfPeople <= 0) {
				System.out.println("Please enter a positive number for the number of people.");
				return;
			}
			// 上限人数チェック
			if (numberOfPeople > maxCapacity) {
				System.out.println("The number of people exceeds the maximum capacity for a " + selectedRoomType + " room (" + maxCapacity + " people).");
				return;
			}
		} catch (NumberFormatException e) {
			System.out.println("Please enter a valid integer for the number of people.");
			return;
		}
		// ▲▲▲ 人数入力と上限チェックの処理 ▲▲▲

		int totalPrice = price * numberOfPeople;

		ReserveRoomForm reserveRoomForm = new ReserveRoomForm();
		reserveRoomForm.setStayingDate(stayingDate);
		String reservationNumber = reserveRoomForm.submitReservation();

		System.out.println("Reservation has been completed.");
		System.out.println("Arrival (staying) date is " + DateUtil.convertToString(stayingDate) + ".");
		System.out.println("Room type is " + selectedRoomType + ".");
		System.out.println("Number of people: " + numberOfPeople);
		System.out.println("Total price is " + totalPrice + " JPY.");
		System.out.println("Reservation number is " + reservationNumber + ".");
	}

	private void checkInRoom() throws IOException, AppException {
		System.out.println("Input reservation number");
		System.out.print("> ");

		String reservationNumber = reader.readLine();

		if (reservationNumber == null || reservationNumber.length() == 0) {
			System.out.println("Invalid reservation number");
			return;
		}

		CheckInRoomForm checkInRoomForm = new CheckInRoomForm();
		checkInRoomForm.setReservationNumber(reservationNumber);

		String roomNumber = checkInRoomForm.checkIn();
		System.out.println("Check-in has been completed.");
		System.out.println("Room number is " + roomNumber + ".");
	}

	private void checkOutRoom() throws IOException, AppException {
		System.out.println("Input room number");
		System.out.print("> ");

		String roomNumber = reader.readLine();

		if (roomNumber == null || roomNumber.length() == 0) {
			System.out.println("Invalid room number");
			return;
		}

		CheckOutRoomForm checkoutRoomForm = new CheckOutRoomForm();
		checkoutRoomForm.setRoomNumber(roomNumber);
		checkoutRoomForm.checkOut();
		System.out.println("Check-out has been completed.");
	}

	private void showEmptyRooms() throws AppException {
		System.out.println("--- Empty Rooms ---");
		try {
			RoomManager roomManager = ManagerFactory.getInstance().getRoomManager();
			List<Room> emptyRooms = roomManager.getEmptyRooms();

			if (emptyRooms.isEmpty()) {
				System.out.println("No empty rooms available.");
			} else {
				for (Room room : emptyRooms) {
					System.out.println("Room Number: " + room.getRoomNumber());
				}
			}
		} catch (RoomException e) {
			throw new AppException("Failed to retrieve empty rooms.", e);
		}
		System.out.println("--------------------");
	}

	public static void main(String[] args) throws Exception {
		CUI cui = new CUI();
		cui.execute();
	}
}