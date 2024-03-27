import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

public class ZreportUtilities {
	/**

	 Sorts the given ArrayList of SmartObjects based on their switch time and old switch time.
	 If no SmartObject in the list has a switch time, the list remains unchanged.
	 If multiple SmartObjects have the same switch time, their old switch times are compared to determine the order.
	 If multiple SmartObjects have no switch time, their old switch times are compared to determine the order.
	 @param smartObjectArray The ArrayList of SmartObjects to be sorted
	 */
	public static void sort(ArrayList<SmartObject> smartObjectArray) {
		boolean anyWithSwitchTime = false;
		for (SmartObject smartObject:smartObjectArray){
			if (smartObject.getSwitchTime() != null) {
				anyWithSwitchTime = true;
				break;
			}
		}
		if (!(anyWithSwitchTime)){
			return;
		}

		smartObjectArray.sort(new Comparator<SmartObject>() {
			public int compare(SmartObject o1, SmartObject o2) {
				LocalDateTime switchTime1 = o1.getSwitchTime();
				LocalDateTime switchTime2 = o2.getSwitchTime();
				LocalDateTime oldSwitchTime1 = o1.getOldSwitchTime();
				LocalDateTime oldSwitchTime2 = o2.getOldSwitchTime();
				if (switchTime1 == null && switchTime2 == null) {
					if (oldSwitchTime1 == null && oldSwitchTime2 == null) {
						return 0;
					} else if (oldSwitchTime1 == null) {
						return 1;
					} else if (oldSwitchTime2 == null) {
						return -1;
					} else {
						return oldSwitchTime2.compareTo(oldSwitchTime1);
					}
				} else if (switchTime1 == null) {
					return 1;
				} else if (switchTime2 == null) {
					return -1;
				} else {
					int switchTimeCompare = switchTime1.compareTo(switchTime2);
					if (switchTimeCompare != 0) {
						return switchTimeCompare;
					} else if (oldSwitchTime1 == null && oldSwitchTime2 == null) {
						return 0;
					} else if (oldSwitchTime1 == null) {
						return 1;
					} else if (oldSwitchTime2 == null) {
						return -1;
					} else {
						return oldSwitchTime2.compareTo(oldSwitchTime1);
					}
				}
			}
		});
	}
	/**

	 This method checks for switches of all SmartObjects in the given ArrayList. If any SmartObject has a switch time
	 that has passed, it updates the status of the SmartObject according to its previous status. If the SmartObject is a SmartPlug or a SmartCamera and was switched off, it calculates the energy consumption and records it. It also updates the old switch time, switch time and start time of the SmartObject accordingly. If the SmartObject was switched on, it updates the old switch time and sets the switch time and start time of the SmartObject to null. If there is no SmartObject in the given ArrayList or none of them have a switch time, the method returns without doing anything.
	 @param smartObjectArray an ArrayList of SmartObjects to check for switches
	 */
	public static void checkForSwitches(ArrayList<SmartObject> smartObjectArray) {
		if(smartObjectArray.size()==0){
			return;
		}
		else if(smartObjectArray.get(0).getSwitchTime()==null){
			return;
		}
		for (SmartObject smartObject : smartObjectArray) {
			if (smartObject.getSwitchTime() == null) {
				return;
			}
			int hasSwitchTimePast = smartObject.getSwitchTime().compareTo(TimeCoordinator.dateTime);
			if (hasSwitchTimePast <= 0) {
				if (smartObject.getInitialStatus().equals("Off")) {
					smartObject.setInitialStatus("On");

					smartObject.setOldSwitchTime(smartObject.getSwitchTime());
					smartObject.setSwitchTime(null);
					//smartobhectsetOldSwtichtime(switchtime)

					smartObject.setStartTime(TimeCoordinator.dateTime);
				}
				else{
					smartObject.setInitialStatus("Off");
					if ((smartObject instanceof SmartCamera) || (smartObject instanceof SmartPlug)) {
						calculator(smartObject);



						smartObject.setOldSwitchTime(smartObject.getSwitchTime());


						smartObject.setSwitchTime(null);
						//smartobhectsetOldSwtichtime(switchtime)

						smartObject.setStartTime(null);
					}
					else{
						smartObject.setOldSwitchTime(smartObject.getSwitchTime());

					smartObject.setSwitchTime(null);
						//smartobhectsetOldSwtichtime(switchtime)

					smartObject.setStartTime(null);}
				}

			}
		}
	}

	/**

	 Calculates the resource consumption of a SmartObject of type SmartCamera or SmartPlug between its start time and switch time.
	 If the SmartObject is a SmartCamera, calculates the additional Megabytes consumed during the duration between the start time
	 and switch time, based on the Megabytes consumed per record. The calculated value is added to the current MbStorage of the SmartCamera.
	 If the SmartObject is a SmartPlug, calculates the additional Power consumption during the duration between the start time
	 and switch time, based on the Amperes and the Voltage of the SmartPlug. The calculated value is added to the current power consumption of the SmartPlug.
	 @param smartObject The SmartObject for which the resource consumption is to be calculated
	 */
	public static void calculator(SmartObject smartObject) {
		LocalDateTime startTime = smartObject.getStartTime();
		LocalDateTime switchTime = smartObject.getSwitchTime();
		Duration duration = Duration.between(startTime, switchTime);
		if (smartObject instanceof SmartCamera) {
			double newMbStorage = duration.toMinutes() * smartObject.getMegabytesConsumedPerRecord();
			newMbStorage = newMbStorage + smartObject.getMbStorage();
			smartObject.setMbStorage(newMbStorage);
		} else if (smartObject instanceof SmartPlug) {
			double consumption = (double) duration.toMinutes() / 60 * 220 * smartObject.getAmpere();
			consumption = consumption + smartObject.getPowerConsumption();
			smartObject.setPowerConsumption(consumption);
		}


	}
}
