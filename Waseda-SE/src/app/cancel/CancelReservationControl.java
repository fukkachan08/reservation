/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package app.cancel;

import app.AppException;
import app.ManagerFactory;
import domain.reservation.ReservationException;
import domain.reservation.ReservationManager;
import domain.room.RoomException;
import domain.room.RoomManager;
import java.util.Date;

/**
 * Control class for Cancel Reservation
 *
 */
public class CancelReservationControl {

	/**
	 * 予約をキャンセルします。
	 *
	 * @param reservationNumber 予約番号
	 * @throws AppException アプリケーション例外
	 */
	public void cancelReservation(String reservationNumber) throws AppException {
		try {
			// 1. 予約を検索し、存在すれば宿泊日を取得する
			ReservationManager reservationManager = getReservationManager();
			Date stayingDate = reservationManager.cancelReservation(reservationNumber);

			// 2. 空室在庫を増やす
			RoomManager roomManager = getRoomManager();
			roomManager.updateRoomAvailableQty(stayingDate, 1); // 在庫を1つ増やす

		} catch (ReservationException e) {
			AppException exception = new AppException("予約のキャンセルに失敗しました。", e);
			exception.getDetailMessages().add(e.getMessage());
			exception.getDetailMessages().addAll(e.getDetailMessages());
			throw exception;
		} catch (RoomException e) {
			AppException exception = new AppException("予約のキャンセルに失敗しました。", e);
			exception.getDetailMessages().add(e.getMessage());
			exception.getDetailMessages().addAll(e.getDetailMessages());
			throw exception;
		}
	}

	private ReservationManager getReservationManager() {
		return ManagerFactory.getInstance().getReservationManager();
	}

	private RoomManager getRoomManager() {
		return ManagerFactory.getInstance().getRoomManager();
	}
}