import java.time.LocalDateTime;
import java.util.ArrayList;


public class SmartPlug extends SmartObject{

	protected double ampere;
	protected double powerConsumption;

	protected LocalDateTime oldSwitchTime;


	protected Boolean plugStatus;
	protected String initialStatus;
	protected LocalDateTime switchTime;
	protected LocalDateTime startTime;

	/**

	 Constructs a new SmartPlug with the given name, ampere, power consumption, plug status, initial status,
	 start time, switch time, and old switch time.
	 @param name the name of the SmartPlug
	 @param ampere the ampere of the SmartPlug
	 @param powerConsumption the power consumption of the SmartPlug
	 @param plugStatus the plug status of the SmartPlug
	 @param initialStatus the initial status of the SmartPlug
	 @param startTime the start time of the SmartPlug
	 @param switchTime the switch time of the SmartPlug
	 @param oldSwitchTime the old switch time of the SmartPlug
	 */
	public SmartPlug(String name,double ampere,double powerConsumption, Boolean plugStatus, String initialStatus,
					 LocalDateTime startTime,LocalDateTime switchTime,LocalDateTime oldSwitchTime){
		this.startTime=startTime;
		this.ampere=ampere;
		this.switchTime=switchTime;
		this.name=name;
		this.powerConsumption=powerConsumption;
		this.plugStatus=plugStatus;
		this.oldSwitchTime = oldSwitchTime;

		this.initialStatus=initialStatus;

	}
	/**

	 Returns a string representation of the SmartPlug.
	 The string contains information about the SmartPlug's name, initial status, power consumption, and switch time.
	 @return a string representation of the SmartPlug
	 */
	@Override
	public String toString() {
		String strSwitchTime;
		if (switchTime==null){
			strSwitchTime="null";
		}
		else{
			strSwitchTime=switchTime.format(TimeCoordinator.formatter());
		}
		String strPowConsumption = String.format("%.2f", powerConsumption);
		return "Smart Plug "+name+" is "+initialStatus.toLowerCase()+" and consumed "+strPowConsumption+"W so far " +
				"(excluding current device), and its time to switch its status is "+strSwitchTime+".";
	}

	/**
	 * Gets the ampere rating of the Smart Plug.
	 *
	 * @return The ampere rating of the Smart Plug.
	 */
	public double getAmpere() {
		return ampere;
	}

	/**
	 * Sets the ampere rating of the Smart Plug.
	 *
	 * @param ampere The new ampere rating of the Smart Plug.
	 */
	public void setAmpere(double ampere) {
		this.ampere = ampere;
	}

	/**
	 * Gets the power consumption of the Smart Plug.
	 *
	 * @return The power consumption of the Smart Plug.
	 */
	public double getPowerConsumption() {
		return powerConsumption;
	}

	/**
	 * Sets the power consumption of the Smart Plug.
	 *
	 * @param powerConsumption The new power consumption of the Smart Plug.
	 */
	public void setPowerConsumption(double powerConsumption) {
		this.powerConsumption = powerConsumption;
	}

	/**
	 * Gets the status of the Smart Plug (ON/OFF).
	 *
	 * @return The status of the Smart Plug.
	 */
	public Boolean getPlugStatus() {
		return plugStatus;
	}

	/**
	 * Sets the status of the Smart Plug (ON/OFF).
	 *
	 * @param plugStatus The new status of the Smart Plug.
	 */
	public void setPlugStatus(Boolean plugStatus) {
		this.plugStatus = plugStatus;
	}

	/**
	 * Gets the initial status of the Smart Plug (ON/OFF).
	 *
	 * @return The initial status of the Smart Plug.
	 */
	@Override
	public String getInitialStatus() {
		return initialStatus;
	}

	/**
	 * Sets the initial status of the Smart Plug (ON/OFF).
	 *
	 * @param initialStatus The new initial status of the Smart Plug.
	 */
	@Override
	public void setInitialStatus(String initialStatus) {
		this.initialStatus = initialStatus;
	}

	/**
	 * Gets the time at which the Smart Plug will switch its status.
	 *
	 * @return The time at which the Smart Plug will switch its status.
	 */
	@Override
	public LocalDateTime getSwitchTime() {
		return switchTime;
	}

	/**
	 * Sets the time at which the Smart Plug will switch its status.
	 *
	 * @param switchTime The new time at which the Smart Plug will switch its status.
	 */
	@Override
	public void setSwitchTime(LocalDateTime switchTime) {
		this.switchTime = switchTime;
	}

	/**
	 * Gets the time at which the Smart Plug was started.
	 *
	 * @return The time at which the Smart Plug was started.
	 */
	@Override
	public LocalDateTime getStartTime() {
		return startTime;
	}

	/**
	 * Sets the time at which the Smart Plug was started.
	 *
	 * @param startTime The new time at which the Smart Plug was started.
	 */
	@Override
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	/**

	 This method is used to unplug an item from a smart plug. It takes in a command line and an ArrayList of smart objects
	 as parameters. The method first logs the command in the output file. It then extracts the name of the smart object
	 from the command line and searches for the object in the ArrayList. If the object is a SmartPlug, it checks whether
	 there is an item plugged in or not. If there is no item plugged in, the method logs an error message and returns. If
	 there is an item plugged in, it sets the plug status to "false" and performs the necessary calculations to update the
	 energy consumption of the device. It then sets the necessary fields to null or zero and returns.
	 @param line a String representing the command line to be executed
	 @param smartObjectArray an ArrayList of SmartObject objects representing the list of smart objects
	 */
	public static void plugOut(String line, ArrayList<SmartObject> smartObjectArray) {
		OutputWriter.write("COMMAND: "+line);
		String[] lineArray = line.split("\t");
		String objectName = lineArray[1];
		for (SmartObject smartObject : smartObjectArray) {
			if (smartObject.getName().equals(objectName)) {
				if (smartObject instanceof SmartPlug) {
					SmartPlug smartPlug = (SmartPlug) smartObject;
					if (!smartPlug.getPlugStatus()) {
						OutputWriter.write("ERROR: This plug has no item to plug out from that plug!");
						return;
					}
					smartPlug.setPlugStatus(false);
					if (smartPlug.getInitialStatus().equals("On")) {
						smartObject.setSwitchTime(TimeCoordinator.getDateTime());
						ZreportUtilities.calculator(smartObject);

						smartObject.setSwitchTime(null);
						//smartobhectsetOldSwtichtime(switchtime)

						smartObject.setStartTime(null);
						smartObject.setAmpere(0);
						return;
					} else if (smartPlug.getInitialStatus().equals("Off")) {
						smartObject.setAmpere(0);

						smartObject.setSwitchTime(null);
						//smartobhectsetOldSwtichtime(switchtime)

						smartObject.setStartTime(null);
						return;
					//SmartObject.switchOnOff(line, smartObjectArray);
				}}
			}
		}
		OutputWriter.write("ERROR: " +"This device is not a smart plug!");

	}

	/**

	 Returns the old switch time of the SmartPlug object.
	 @return the old switch time of the SmartPlug object
	 */
	@Override
	public LocalDateTime getOldSwitchTime() {
		return oldSwitchTime;
	}
	/**

	 Sets the old switch time of the SmartPlug object.
	 @param oldSwitchTime the old switch time to be set for the SmartPlug object
	 */
	@Override
	public void setOldSwitchTime(LocalDateTime oldSwitchTime) {
		this.oldSwitchTime = oldSwitchTime;

	}
	/**

	 This method is used to plug in an item to a smart plug. It takes a string command and an array list of SmartObjects as input.
	 The command is in the format "PlugIn \t ObjectName \t AmpereValue", where ObjectName is the name of the smart plug and AmpereValue is the amount of electricity consumed by the item being plugged in.
	 The method first checks if the input AmpereValue is a valid positive number, and if it is not, an error message is printed.
	 If the input is valid, the method checks if there is already an item plugged in to the smart plug and if there is, an error message is printed.
	 If the smart plug is available, the method sets the plug status to true, sets the Ampere value, and starts the timer for the item plugged in.
	 If the smart plug is initially off, the timer is not started, and if the smart plug is not a SmartPlug object, an error message is printed.
	 @param line A string command in the format "PlugIn \t ObjectName \t AmpereValue"
	 @param smartObjectArray An ArrayList of SmartObjects containing all the smart devices in the system
	 */
	public static void plugIn(String line, ArrayList<SmartObject> smartObjectArray) {
		//PlugIn	Plug2	-3
		OutputWriter.write("COMMAND: "+line);
		String[] lineArray = line.split("\t");
		String objectName = lineArray[1];
		double ampere ;
		try{
			ampere=Double.parseDouble(lineArray[2]);
		}
		catch (Exception e){
			OutputWriter.write("ERROR: Ampere value must be a number!");
			return;
		}
		if (ampere <= 0){
			OutputWriter.write("ERROR: Ampere value must be a positive number!");
			return;
		}
		for (SmartObject smartObject : smartObjectArray) {
			if (smartObject.getName().equals(objectName)) {
				if (smartObject instanceof SmartPlug) {
					SmartPlug smartPlug = (SmartPlug) smartObject;
					if (smartPlug.getAmpere() != 0) {
						OutputWriter.write("ERROR: There is already an item plugged in to that plug!");
						return;
					}
					smartPlug.setPlugStatus(true);
					smartPlug.setAmpere(ampere);
					if (smartPlug.getInitialStatus().equals("On")) {
						smartObject.setStartTime(TimeCoordinator.getDateTime());
						smartObject.setSwitchTime(null);
						//smartobhectsetOldSwtichtime(switchtime)

						return;
					} else if (smartPlug.getInitialStatus().equals("Off")) {
						smartObject.setSwitchTime(null);
						//smartobhectsetOldSwtichtime(switchtime)

						smartObject.setStartTime(null);
						return;
					}
				}			}
		}
		OutputWriter.write("ERROR: This device is not a smart plug!");
	}
	/**
	 Saves a new SmartPlug object with the given parameters to the list of SmartObjects.
	 If only the object name is given, a SmartPlug object is created with default values.
	 If the initial status is "On", the start time is set to the current time and the plug is turned on.
	 If the initial status is "Off", the plug is turned off.
	 If the ampere value is also given, the SmartPlug object is created with the given ampere value.
	 @param line the input command line
	 @param smartObjectArray the list of SmartObjects to add the new SmartPlug object to
	 */
	public static void saveSmartPlug(String line, ArrayList<SmartObject> smartObjectArray) {
		OutputWriter.write("COMMAND: "+line);
		String[] lineArray = line.split("\t");
		String objectName = lineArray[2];
		if (lineArray.length==3){
			SmartPlug smartPlug = new SmartPlug(objectName,0,0, false, "Off",
					null,null,null);
			smartObjectArray.add(smartPlug);
			return;
		}
		String initialStatus=lineArray[3];
		boolean plugStatus=false ;
		if (!initialStatus.equals("On") && !initialStatus.equals("Off")){
			OutputWriter.write("ERROR: Initial status must be either on or off!");
			return;}
		LocalDateTime startTime = null;
		if (initialStatus.equals("On")){
			startTime= TimeCoordinator.dateTime;
			plugStatus=true;
		}
		if(lineArray.length==4){
			SmartPlug smartPlug = new SmartPlug(objectName,0,0,plugStatus, initialStatus,startTime,null,null);
			smartObjectArray.add(smartPlug);
			return;
		}

		double ampere ;
		try{
			ampere=Double.parseDouble(lineArray[4]);
		}
		catch (Exception e){
			OutputWriter.write("ERROR: Ampere value must be a number!");
			return;
		}
		if (ampere<=0){
			OutputWriter.write("ERROR: Ampere value must be a positive number!");
			return;
		}
		SmartPlug smartPlug = new SmartPlug(objectName,ampere,0,plugStatus, initialStatus,startTime,null,null);
		smartObjectArray.add(smartPlug);
	}
}
