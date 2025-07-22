/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package app.checkout;

import app.AppException;
import app.ManagerFactory;
import domain.payment.PaymentException;
import domain.payment.PaymentManager;
import domain.room.RoomException;
import domain.room.RoomManager;
import java.util.Date;

/**
 * Control class for Check-out Customer
 * */
public class CheckOutRoomControl {
	
	public void checkOut(String roomNumber) throws AppException {
		try {
			// ▼▼▼ ここから修正 ▼▼▼
			
			// 1. 部屋を空ける (顧客情報を削除する)
			RoomManager roomManager = getRoomManager();
			Date stayingDate = roomManager.removeCustomer(roomNumber);

			// 2. 支払いを完了済みにする
			PaymentManager paymentManager = getPaymentManager();
			paymentManager.consumePayment(stayingDate, roomNumber);
			
			// ▲▲▲ ここまで修正 ▲▲▲
		}
		catch (RoomException e) {
			AppException exception = new AppException("Failed to check-out", e);
			exception.getDetailMessages().add(e.getMessage());
			exception.getDetailMessages().addAll(e.getDetailMessages());
			throw exception;
		}
		catch (PaymentException e) {
			AppException exception = new AppException("Failed to check-out", e);
			exception.getDetailMessages().add(e.getMessage());
			exception.getDetailMessages().addAll(e.getDetailMessages());
			throw exception;
		}
	}

	private RoomManager getRoomManager() {
		return ManagerFactory.getInstance().getRoomManager();
	}

	private PaymentManager getPaymentManager() {
		return ManagerFactory.getInstance().getPaymentManager();
	}
}