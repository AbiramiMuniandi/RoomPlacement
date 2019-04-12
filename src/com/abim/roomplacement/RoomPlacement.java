package com.abim.roomplacement;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class RoomPlacement {

	int numberofGuests;//can be initialized with required value
	int numberofOptions;//can be initialized with required value
	Date startDate = new Date();//can be initialized with required date
	Date endDate = new Date();//can be initialized with required date
	
	List<List<RoomInventory>> roomsCombinationList = new ArrayList<List<RoomInventory>>();
	
	private List<List<RoomInventory>> findAllPossibleRoomCombinations(List<RoomInventory> inventoryList) {
		
		numberofGuests = 7;
		numberofOptions = 3;
		
		HashMap<String, Integer> roomTypeandAvailableCount = new HashMap<String, Integer>();
		List<RoomInventory> availableRoomList = new ArrayList<RoomInventory>();

		/*find the available rooms, count of each room type*/
		inventoryList.forEach(room->{
			if((startDate.compareTo(room.getStartDate()) >= 0) && (endDate.compareTo(room.getEndDate()) <= 0)) {
				room.setAvailability(1);
				if(roomTypeandAvailableCount.containsKey(room.getRoomType())) {
					roomTypeandAvailableCount.put(room.getRoomType(), roomTypeandAvailableCount.get(room.getRoomType()) + 1);
				}
				else {
					roomTypeandAvailableCount.put(room.getRoomType(), 1);
				}
				availableRoomList.add(room);
			}
		});

		/*sort the available rooms based on price*/
		availableRoomList.sort((room1, room2) -> room1.getPrice() - room2.getPrice());

		/*find the 3 cheapest room combinations*/
		int optionsCount = 0;
		int loopStarter = 0;
		int listIterator;
		int allottedGuestCount = 0;
		RoomInventory currentRoom;
		List<RoomInventory> currentRoomsList = new ArrayList<>();
		List<String> roomsTypeCombo = new ArrayList<String>();
		List<List<String>> roomTypesComboList = new ArrayList<List<String>>();
		
		while(optionsCount < numberofOptions && loopStarter < availableRoomList.size() - 1) {

			listIterator = loopStarter;
			while(allottedGuestCount < numberofGuests && listIterator < availableRoomList
					.size()) {
				/*this check is done to minimize iterations because family room is pu which gives a lesser price when compared to double rooms which is pp*/
				if(((roomTypeandAvailableCount.get("TWIN") == 0 && roomTypeandAvailableCount.get("TWINTRIPLE") == 0
						&& roomTypeandAvailableCount.get("FAMILY") > 0)&&(numberofGuests - allottedGuestCount == 3)
						||(numberofGuests - allottedGuestCount == 4)
					)) {
					currentRoom = availableRoomList.stream().filter(room->room.getRoomType()=="FAMILY").findFirst().get();
					allottedGuestCount = allottedGuestCount + (numberofGuests - allottedGuestCount);
				}
				currentRoom = availableRoomList.get(listIterator);
				if(currentRoom.getMaxGuests() <= (numberofGuests - allottedGuestCount)) {
					allottedGuestCount = allottedGuestCount + currentRoom.getMaxGuests();
					currentRoomsList.add(currentRoom);
					roomsTypeCombo.add(currentRoom.getRoomType());
				}
				else {
					if((numberofGuests - allottedGuestCount) >= currentRoom.getMinGuests()) {
						allottedGuestCount = allottedGuestCount + (numberofGuests - allottedGuestCount);
						currentRoomsList.add(currentRoom);
						roomsTypeCombo.add(currentRoom.getRoomType());
					}
				}
				listIterator ++;
			}

			if(listIterator == availableRoomList.size() - 1 && allottedGuestCount > 0) {
				loopStarter = loopStarter +  1;
			}
			if(allottedGuestCount <= 0) {
				if(!roomTypesComboList.contains(roomsTypeCombo)) {
					roomTypesComboList.add(new ArrayList<String>(roomsTypeCombo));
					roomsCombinationList.add(new ArrayList<RoomInventory>(currentRoomsList));
					optionsCount ++;
				}
			}
			roomsTypeCombo.clear();
			currentRoomsList.clear();
		}
		
		return roomsCombinationList;
	}
	
}
