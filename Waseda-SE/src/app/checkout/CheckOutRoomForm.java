/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package app.checkout;

import app.AppException;

/**
 * Form class for Check-out Customer
 * */
public class CheckOutRoomForm {

	private CheckOutRoomControl checkOutRoomControl = new CheckOutRoomControl();

	private CheckOutRoomControl getCheckOutRoomControl() {
		return checkOutRoomControl;
	}

	private String roomNumber;

	/**
	 * チェックアウト処理を実行します。
	 * @throws AppException
	 */
	public void checkOut() throws AppException {
		// ▼▼▼ この一行を追加 ▼▼▼
		getCheckOutRoomControl().checkOut(roomNumber);
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

}