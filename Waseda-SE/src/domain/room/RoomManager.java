
package domain.room;

import domain.DaoFactory;
import java.util.Date;
import java.util.List;
import util.DateUtil;

/**
 * Manager for rooms<br>
 * */
public class RoomManager {
	
       public void updateRoomAvailableQty(Date stayingDate, String roomType, int qtyOfChange) throws RoomException,
                        NullPointerException {
               if (stayingDate == null) {
                       throw new NullPointerException("stayingDate");
               }
               if (roomType == null) {
                       throw new NullPointerException("roomType");
               }
               if (qtyOfChange == 0) {
                       return;
               }

               AvailableQtyDao availableQtyDao = getAvailableQtyDao();
               AvailableQty availableQty = availableQtyDao.getAvailableQty(stayingDate, roomType);
               //Create new AvailableQty if corresponding AvailableQty on stayingDate does not exist
               if (availableQty == null) {
                       availableQty = new AvailableQty();
                       availableQty.setQty(AvailableQty.AVAILABLE_ALL);
                       availableQty.setDate(stayingDate);
                       availableQty.setRoomType(roomType);
               }

		// Obtain maximum number of available rooms
               int maxAvailableQty = getMaxAvailableQty(roomType);
		if (availableQty.getQty() == AvailableQty.AVAILABLE_ALL) {
			// If all rooms are available,  
			// set then maximum number obtained to number of available rooms 
			availableQty.setQty(maxAvailableQty);

			// Newly register availableQty data on stayingDate to DB
                       availableQtyDao.createAbailableQty(availableQty);
               }

		int changedAvailableQty = availableQty.getQty() + qtyOfChange;
		if (changedAvailableQty >= 0 && changedAvailableQty <= maxAvailableQty) {
			// If it is possible to update
			availableQty.setQty(changedAvailableQty);
			availableQty.setDate(stayingDate);
                       availableQty.setRoomType(roomType);
                       availableQtyDao.updateAvailableQty(availableQty);
		}
		else {
			// If it is impossible to update
			RoomException exception = new RoomException(
					RoomException.CODE_AVAILABLE_QTY_OUT_OF_BOUNDS);
			exception.getDetailMessages().add("staying_date[" + DateUtil.convertToString(stayingDate) + "]");
			throw exception;

		}
	}

       private int getMaxAvailableQty(String roomType) throws RoomException {
               if ("twin".equalsIgnoreCase(roomType)) {
                       return 3;
               } else if ("double".equalsIgnoreCase(roomType)) {
                       return 1;
               } else if ("suite".equalsIgnoreCase(roomType)) {
                       return 1;
               }
               return 0;
       }

        public String assignCustomer(Date stayingDate) throws RoomException, NullPointerException {
                return assignCustomer(stayingDate, null);
        }

        public String assignCustomer(Date stayingDate, String roomType) throws RoomException, NullPointerException {
                if (stayingDate == null) {
                        throw new NullPointerException("stayingDate");
                }
                RoomDao roomDao = getRoomDao();
                // Obtain all of empty available rooms
                List emptyRooms = roomDao.getEmptyRooms();
                // If there is no empty available rooms
                if (emptyRooms.size() == 0) {
                        RoomException exception = new RoomException(RoomException.CODE_EMPTYROOM_NOT_FOUND);
                        throw exception;
                }
                for (int i = 0; i < emptyRooms.size(); i++) {
                        Room room = (Room) emptyRooms.get(i);
                        if (roomType == null || roomType.equalsIgnoreCase(room.getRoomType())) {
                                String roomNumber = room.getRoomNumber();
                                room.setStayingDate(stayingDate);
                                roomDao.updateRoom(room);
                                return roomNumber;
                        }
                }
                RoomException exception = new RoomException(RoomException.CODE_EMPTYROOM_NOT_FOUND);
                throw exception;
        }

	public Date removeCustomer(String roomNumber) throws RoomException, NullPointerException {
		if (roomNumber == null) {
			throw new NullPointerException("roomNumber");
		}
		RoomDao roomDao = getRoomDao();
		Room room = roomDao.getRoom(roomNumber);
		//If corresponding room does not exist
		if (room == null) {
			RoomException exception = new RoomException(RoomException.CODE_ROOM_NOT_FOUND);
			exception.getDetailMessages().add("room_number[" + roomNumber + "]");
			throw exception;
		}
		Date stayingDate = room.getStayingDate();
		if (stayingDate == null) {
			RoomException exception = new RoomException(RoomException.CODE_ROOM_NOT_FULL);
			exception.getDetailMessages().add("room_number[" + roomNumber + "]");
			throw exception;
		}
		room.setStayingDate(null);
		roomDao.updateRoom(room);
		return stayingDate;
	}

	/**
	 * 空き部屋のリストを取得します。
	 * @return 空き部屋のリスト
	 * @throws RoomException
	 */
	public List<Room> getEmptyRooms() throws RoomException {
		RoomDao roomDao = getRoomDao();
		return roomDao.getEmptyRooms();
	}

	private AvailableQtyDao getAvailableQtyDao() {
		return DaoFactory.getInstance().getAvailableQtyDao();
	}

	private RoomDao getRoomDao() {
		return DaoFactory.getInstance().getRoomDao();
	}
}