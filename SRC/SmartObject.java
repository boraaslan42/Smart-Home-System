import java.time.LocalDateTime;
import java.util.ArrayList;

public class SmartObject {
	protected String name;
	protected double mbStorage;
	protected LocalDateTime startTime;
	protected LocalDateTime oldSwitchTime;

	protected String initialStatus;
	protected LocalDateTime switchTime;
	protected double ampere;
	protected double powerConsumption;

	protected int brightnessValue;

	protected int kelvinValue;


	/**

	 Gets the brightness value of the device.
	 @return the brightness value
	 */
	public int getBrightnessValue() {
		return brightnessValue;
	}
	/**

	 Gets the storage capacity of the device in megabytes (MB).
	 @return the storage capacity in MB
	 */
	public double getMbStorage() {
		return mbStorage;
	}
	/**

	 Sets the brightness value of the device.
	 @param brightnessValue the brightness value to set
	 */
	public void setBrightnessValue(int brightnessValue) {
		this.brightnessValue = brightnessValue;
	}
	/**

	 Gets the Kelvin value of the device.
	 @return the Kelvin value
	 */
	public int getKelvinValue() {
		return kelvinValue;
	}
	/**

	 Sets the Kelvin value of the device.
	 @param kelvinValue the Kelvin value to set
	 */
	public void setKelvinValue(int kelvinValue) {
		this.kelvinValue = kelvinValue;
	}

	protected Boolean plugStatus;
	private int megabytesConsumedPerRecord;
	/**

	 Skips time to the first switch in the list of SmartObjects and updates the time accordingly.
	 If the SmartObject list is empty or there are no switches in the list, an error message will be displayed.
	 @param smartObjectArray the ArrayList of SmartObjects to search for the first switch
	 */
	public static void skipToFirstSwitch(ArrayList<SmartObject> smartObjectArray) {
		OutputWriter.write("COMMAND: Nop");
		if (!(smartObjectArray.size() == 0)) {
			ZreportUtilities.sort(MainCoordinator.smartObjectArray);
			LocalDateTime firstSwitchTime = smartObjectArray.get(0).getSwitchTime();
			if (firstSwitchTime == null) {
				OutputWriter.write("ERROR: There is nothing to switch!");
				return;
			}
			String formattedFirstSwitchTime = firstSwitchTime.format(TimeCoordinator.formatter());
			TimeCoordinator.setTime(formattedFirstSwitchTime, "skipToFirstSwitch");
			ZreportUtilities.checkForSwitches(MainCoordinator.smartObjectArray);
		} else {
			OutputWriter.write("ERROR: There is nothing to switch!");

		}


	}
	/**

	 Changes the name of a SmartObject in the list of SmartObjects.

	 @param line the command line that contains the old and new name of the SmartObject

	 @param smartObjectArray the ArrayList of SmartObjects to search for the SmartObject to rename
	 */
	public static void changeName(String line, ArrayList<SmartObject> smartObjectArray) {
		String oldName = line.split("\t")[1];
		String newName = line.split("\t")[2];
		OutputWriter.write("COMMAND: " + line);
		if (oldName.equals(newName)) {
			OutputWriter.write("ERROR: Both of the names are the same, nothing changed!");
			return;
		}

		for (SmartObject smartObject : smartObjectArray) {
			if (newName.equals(smartObject.getName())) {
				OutputWriter.write("ERROR: There is already a smart device with same name!");
				return;
			}
		}
		for (SmartObject smartObject : smartObjectArray) {
			if (oldName.equals(smartObject.getName())) {
				smartObject.setName(newName);
				return;
			}
		}
	}
	/**

	 Sets the switch time of a SmartObject in the list of SmartObjects.

	 @param line the command line that contains the name of the SmartObject and the new switch time

	 @param smartObjectArray the ArrayList of SmartObjects to search for the SmartObject to set the switch time
	 */
	public static void setSwitchTime(String line, ArrayList<SmartObject> smartObjectArray) {
		OutputWriter.write("COMMAND: " + line);
		String name = line.split("\t")[1];
		String time = line.split("\t")[2];
		try {
			LocalDateTime.parse(time, TimeCoordinator.formatter());
		} catch (Exception e) {
			OutputWriter.write("ERROR: Time format is not correct!");
			return;
		}
		LocalDateTime testTime = LocalDateTime.parse(time, TimeCoordinator.formatter());
		int result = testTime.compareTo(TimeCoordinator.getDateTime());
		if (result < 0) {
			OutputWriter.write("ERROR: Switch time cannot be in the past!");
			return;
		}
		for (SmartObject smartObject : smartObjectArray) {
			if (name.equals(smartObject.getName())) {
				if (result==0){
					smartObject.setOldSwitchTime(TimeCoordinator.dateTime);
					if (smartObject.getInitialStatus().equals("On")){
						smartObject.setInitialStatus("Off");
						return;
					}
					else{
						smartObject.setInitialStatus("On");
					}
				}
				smartObject.setSwitchTime(testTime);
				smartObject.setOldSwitchTime(null);


				return;
			}
		}
	}

	/**

	 Removes a SmartObject from the list of SmartObjects and prints its information to the output file.

	 @param line the command line that contains the name of the SmartObject to be removed

	 @param smartObjectArray the ArrayList of SmartObjects to search for the SmartObject to be removed
	 */
	public static void removeObj(String line, ArrayList<SmartObject> smartObjectArray) {
		String name = line.split("\t")[1];
		for (SmartObject smartObject : smartObjectArray) {
			if (name.equals(smartObject.getName())) {
				OutputWriter.write("COMMAND: " + line);
				OutputWriter.write("SUCCESS: Information about removed smart device is as follows:");
				if(smartObject.getInitialStatus().equals("Off")){
					OutputWriter.write(smartObject.toString());
					smartObjectArray.remove(smartObject);
					return;
				}
				smartObject.setSwitchTime(TimeCoordinator.getDateTime());
				ZreportUtilities.calculator(smartObject);
				smartObject.setOldSwitchTime(smartObject.getSwitchTime());
				smartObject.setSwitchTime(null);
				smartObject.setStartTime(null);
				smartObject.setInitialStatus("Off");
				OutputWriter.write(smartObject.toString());
				smartObjectArray.remove(smartObject);
				return;
			}
		}



	}

	/**

	 Sets the old switch time of the smart device.
	 @param oldSwitchTime the LocalDateTime value representing the old switch time
	 */
	public void setOldSwitchTime(LocalDateTime oldSwitchTime) {
		this.oldSwitchTime = oldSwitchTime;
	}
	/**

	 Returns the old switch time of the smart device.
	 @return the LocalDateTime value representing the old switch time
	 */
	public LocalDateTime getOldSwitchTime() {
		return oldSwitchTime;
	}
	/**

	 Method that switches the status of a given SmartObject between On and Off according to the given command line.
	 If the SmartObject is a SmartPlug and it is turned off, then its status will not be changed.
	 If the wanted status is not "On" or "Off", an error message is printed to the output file.
	 If the given SmartObject is not found in the given ArrayList, an error message is printed to the output file.
	 If the SmartObject is already switched to the wanted status, an error message is printed to the output file.
	 @param line a string that contains the command to be executed
	 @param smartObjectArray an ArrayList that contains all the SmartObjects
	 */
	public static void switchOnOff(String line, ArrayList<SmartObject> smartObjectArray) {
		String name = line.split("\t")[1];
		String wantedStatus = line.split("\t")[2];
		// write if for wanted status not equals on or off
		if (!wantedStatus.equals("On") && !wantedStatus.equals("Off")) {
			OutputWriter.write("COMMAND: "+line+"\nERROR: Erroneous command!");
			return;
		}
		//ERROR: This device is already switched on!
		OutputWriter.write("COMMAND: " + line);
		for (SmartObject smartObject : smartObjectArray) {
			if (name.equals(smartObject.getName())) {
				String initialStatus = smartObject.getInitialStatus();
				if (initialStatus.equals(wantedStatus)){
					OutputWriter.write(String.format("ERROR: This device is already switched %s!",initialStatus.toLowerCase()));
					return;
				}
				if(smartObject instanceof SmartPlug){
					if (smartObject.getPlugStatus()){//if plug is on
						if (initialStatus.equals("On")){
							smartObject.setSwitchTime(TimeCoordinator.getDateTime());

							ZreportUtilities.calculator(smartObject);
							smartObject.setOldSwitchTime(smartObject.getSwitchTime());

							smartObject.setSwitchTime(null);

							//checked
							//smartobhectsetOldSwtichtime(switchtime)

							smartObject.setStartTime(null);
							smartObject.setInitialStatus("Off");
							return;

						}
						else if (initialStatus.equals("Off")){
							smartObject.setStartTime(TimeCoordinator.getDateTime());
							smartObject.setInitialStatus("On");
							return;
						}
					}
					else {//plug is off
						if(smartObject.getInitialStatus().equals("On")){
							smartObject.setInitialStatus("Off");
							return;
						}
						else if(smartObject.getInitialStatus().equals("Off")){
							smartObject.setInitialStatus("On");
							return;
						}
					}
				}
				if (initialStatus.equals("On")) {//it will be switched off
					smartObject.setSwitchTime(TimeCoordinator.getDateTime());
					ZreportUtilities.calculator(smartObject);
					smartObject.setOldSwitchTime(smartObject.getSwitchTime());
					smartObject.setSwitchTime(null);
					//checked
					smartObject.setStartTime(null);
					//smartobhectsetOldSwtichtime(switchtime)

					smartObject.setInitialStatus("Off");
					return;
				} else if (initialStatus.equals("Off")) {
					smartObject.setStartTime(TimeCoordinator.getDateTime());
					smartObject.setInitialStatus("On");
					if (smartObject instanceof SmartPlug){
						smartObject.setPlugStatus(false);
					}
					return;
				}
			}
		}
		OutputWriter.write("ERROR: There is not such a device!");
	}
	/**

	 Sets the brightness value of a SmartLamp or a SmartColorLamp.
	 @param line the command line which includes the object name and the desired brightness value
	 @param smartObjectArray the list of all smart objects in the smart home system
	 */
	public static void setBrightnessCommand(String line, ArrayList<SmartObject> smartObjectArray) {
		OutputWriter.write("COMMAND: "+line);
		String[] lineArray = line.split("\t");
		String objectName = lineArray[1];

		for (SmartObject smartObject: smartObjectArray){
			if (smartObject.getName().equals(objectName)){
				if ((smartObject instanceof SmartLamp)||(smartObject instanceof SmartColorLamp)){
					int brightnessValue;
					try {
						brightnessValue = Integer.parseInt(lineArray[2]);
					} catch (Exception e) {
						OutputWriter.write("ERROR: BRIGHTNESS value must be a positive number!");

						return;
					}
					if(!(brightnessValue>=0) || !(brightnessValue<=100)){
						OutputWriter.write("ERROR: Brightness value must be in range of 0%-100%!");
						return;
					}

					smartObject.setBrightnessValue(brightnessValue);
					return;
				}
				else{
					OutputWriter.write("ERROR: This device is not a smart lamp!");

					return;
				}
			}
		}
		OutputWriter.write("ERROR: This device is not a smart lamp!");
	}
	/**

	 Sets the Kelvin value and brightness value of a smart lamp or smart color lamp to display white light.
	 If the device is not a smart lamp or smart color lamp, an error message is displayed.
	 If the input values for Kelvin or brightness are invalid, an error message is displayed.
	 @param line The input command line to set the white light for a smart object
	 @param smartObjectArray The ArrayList of smart objects to search for the object to set the white light for
	 */
	public static void setWhiteCommand(String line,ArrayList<SmartObject> smartObjectArray){
		OutputWriter.write("COMMAND: "+line);
		String[] lineArray = line.split("\t");
		String objectName = lineArray[1];
		int kelvinValue;
		try {
			kelvinValue = Integer.parseInt(lineArray[2]);
		} catch (Exception e) {
			OutputWriter.write("ERROR: KELVIN value must be a positive number!");
			return;
		}
		if(!(kelvinValue>=2000) || !(kelvinValue<=6500)){
			OutputWriter.write("ERROR: Kelvin value must be in range of 2000K-6500K!");
			return;
		}
		{int brightnessValue;
			try {
				brightnessValue = Integer.parseInt(lineArray[3]);
			} catch (Exception e) {
				OutputWriter.write("ERROR: BRIGHTNESS value must be a positive number!");

				return;
			}
			if(!(brightnessValue>=0) || !(brightnessValue<=100)){
				OutputWriter.write("ERROR: Brightness must be in range of 0%-100%!");
				return;
			}

		for (SmartObject smartObject: smartObjectArray){
			if (smartObject.getName().equals(objectName)){
				if (smartObject instanceof SmartLamp){
					smartObject.setKelvinValue(kelvinValue);
					smartObject.setBrightnessValue(brightnessValue);
					return;
				}
				else if(smartObject instanceof  SmartColorLamp){
					smartObject.setKelvinValue(kelvinValue);
					smartObject.setBrightnessValue(brightnessValue);
					((SmartColorLamp) smartObject).setIsWhite(true);
					return;
				}
				else{
					OutputWriter.write("ERROR: This device is not a smart lamp!");
					return;
				}
			}
		}
			OutputWriter.write("ERROR: This device is not a smart lamp!");
	}}
	/**

	 Sets the Kelvin value of a smart lamp or smart color lamp based on a command line.
	 @param line The command line to be executed.
	 @param smartObjectArray An ArrayList of SmartObject instances.
	 */
	public static void setKelvinCommand(String line, ArrayList<SmartObject> smartObjectArray){
		OutputWriter.write("COMMAND: "+line);
		String[] lineArray = line.split("\t");
		String objectName = lineArray[1];
		int kelvinValue;
		try {
			kelvinValue = Integer.parseInt(lineArray[2]);
		} catch (Exception e) {
			OutputWriter.write("ERROR: KELVIN value must be a positive number!");
			return;
		}
		if(!(kelvinValue>=2000) || !(kelvinValue<=6500)){
			OutputWriter.write("ERROR: Kelvin value must be in range of 2000K-6500K!");
			return;
		}
		for (SmartObject smartObject: smartObjectArray){
			if (smartObject.getName().equals(objectName)){
				if ((smartObject instanceof SmartLamp)||(smartObject instanceof SmartColorLamp)){
					smartObject.setKelvinValue(kelvinValue);
				}
				else{
					OutputWriter.write("ERROR: This device is not a smart lamp!");
				}
				return;
			}
		}
		OutputWriter.write("ERROR: This device is not a smart lamp!");

	}



	/**

	 Sets the megabyte storage of the device.
	 @param mbStorage the new megabyte storage of the device
	 */
	public void setMbStorage(double mbStorage) {
		this.mbStorage = mbStorage;
	}
	/**

	 Gets the start time of the device.
	 @return the start time of the device
	 */
	public LocalDateTime getStartTime() {
		return startTime;
	}
	/**

	 Sets the start time of the device.
	 @param startTime the new start time of the device
	 */
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	/**

	 Gets the number of megabytes consumed per record by the device.
	 @return the number of megabytes consumed per record by the device
	 */
	public double getMegabytesConsumedPerRecord() {
		return megabytesConsumedPerRecord;
	}
	/**

	 Sets the number of megabytes consumed per record by the device.
	 @param megabytesConsumedPerRecord the new number of megabytes consumed per record by the device
	 */
	public void setMegabytesConsumedPerRecord(int megabytesConsumedPerRecord) {
		this.megabytesConsumedPerRecord = megabytesConsumedPerRecord;
	}
	/**

	 Gets the ampere of the device.
	 @return the ampere of the device
	 */
	public double getAmpere() {
		return ampere;
	}
	/**

	 Sets the ampere of the device.
	 @param ampere the new ampere of the device
	 */
	public void setAmpere(double ampere) {
		this.ampere = ampere;
	}
	/**

	 Gets the power consumption of the device.
	 @return the power consumption of the device
	 */
	public double getPowerConsumption() {
		return powerConsumption;
	}
	/**

	 Sets the power consumption of the device.
	 @param powerConsumption the additional power consumption of the device
	 */
	public void setPowerConsumption(double powerConsumption) {
		this.powerConsumption += powerConsumption;
	}
	/**

	 Gets the plug status of the device.
	 @return the plug status of the device
	 */
	public Boolean getPlugStatus() {
		return plugStatus;
	}
	/**

	 Sets the plug status of the device.
	 @param plugStatus the new plug status of the device
	 */
	public void setPlugStatus(Boolean plugStatus) {
		this.plugStatus = plugStatus;
	}
	/**

	 Gets the switch time of the device.
	 @return the switch time of the device
	 */
	public LocalDateTime getSwitchTime() {
		return switchTime;
	}
	/**

	 Sets the switch time of the device.
	 @param switchTime the new switch time of the device
	 */
	public void setSwitchTime(LocalDateTime switchTime) {
		this.switchTime = switchTime;
	}
	/**

	 Gets the name of the device.
	 @return the name of the device
	 */
	protected String getName() {
		return name;
	}
	/**

	 Sets the name of the device.
	 @param newName the new name of the device
	 */
	protected void setName(String newName) {
		this.name = newName;
	}
	/**

	 Returns a string representation of the SmartObject.
	 @return a string representation of the SmartObject
	 */
	@Override
	public String toString() {
		return "SmartObject{" + "name='" + name + '\'' + ", initialStatus='" + initialStatus + '\'' + ", switchTime=" + switchTime;
	}
	/**

	 Returns the initial status of the SmartObject.
	 @return the initial status of the SmartObject
	 */
	public String getInitialStatus() {
		return initialStatus;
	}

	/**

	 Sets the initial status of the SmartObject.
	 @param initialStatus the new initial status for the SmartObject
	 */

	public void setInitialStatus(String initialStatus) {
		this.initialStatus = initialStatus;
	}
}