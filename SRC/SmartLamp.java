import java.time.LocalDateTime;
import java.util.ArrayList;

public class SmartLamp extends SmartObject{
	protected String name;

	protected LocalDateTime oldSwitchTime;


	protected String initialStatus;
	protected LocalDateTime switchTime;
	protected LocalDateTime startTime;
	protected int kelvinValue;

	protected int brightnessValue;


	/**

	 Returns the name of this device.
	 @return the name of this device
	 */
	@Override
	public String getName() {
		return name;
	}
	/**

	 Sets the name of this device.
	 @param name the new name for this device
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}
	/**

	 Returns the initial status of this device.
	 @return the initial status of this device
	 */
	@Override
	public String getInitialStatus() {
		return initialStatus;
	}
	/**

	 Sets the initial status of this device.
	 @param initialStatus the new initial status for this device
	 */
	@Override
	public void setInitialStatus(String initialStatus) {
		this.initialStatus = initialStatus;
	}
	/**

	 Returns the time when the device was last switched.
	 @return the time when the device was last switched
	 */
	@Override
	public LocalDateTime getSwitchTime() {
		return switchTime;
	}
	/**

	 Sets the time when the device was last switched.
	 @param switchTime the new switch time for this device
	 */
	@Override
	public void setSwitchTime(LocalDateTime switchTime) {
		this.switchTime = switchTime;
	}
	/**

	 Returns the time when the device was started.
	 @return the time when the device was started
	 */
	@Override
	public LocalDateTime getStartTime() {
		return startTime;
	}
	/**

	 Sets the time when the device was started.
	 @param startTime the new start time for this device
	 */
	@Override
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	/**

	 Sets the Kelvin value for this device.
	 @param kelvinValue the new Kelvin value for this device
	 */
	public void setKelvinValue(int kelvinValue) {
		this.kelvinValue = kelvinValue;
	}
	/**

	 Sets the brightness value for this device.
	 @param brightnessValue the new brightness value for this device
	 */
	public void setBrightnessValue(int brightnessValue) {
		this.brightnessValue = brightnessValue;
	}
	/**

	 Returns the previous switch time for this device.
	 @return the previous switch time for this device
	 */
	@Override
	public LocalDateTime getOldSwitchTime() {
		return oldSwitchTime;
	}


	/**

	 Returns a string representation of this SmartLamp object.

	 @return a string representation of this SmartLamp object
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

		return "Smart Lamp "+name+" is "+initialStatus.toLowerCase()+" and its kelvin value is "+kelvinValue+"K with "+brightnessValue
				+"% brightness, and its time to switch its status is "+strSwitchTime+".";
	}

	/**

	 Sets the previous switch time for this device.
	 @param oldSwitchTime the new previous switch time for this device
	 */
	@Override
	public void setOldSwitchTime(LocalDateTime oldSwitchTime) {
		this.oldSwitchTime = oldSwitchTime;
	}

	/**

	 Constructs a new SmartLamp object with the specified parameters.
	 @param name the name of the SmartLamp
	 @param initialStatus the initial status of the SmartLamp ("On" or "Off")
	 @param switchTime the time to switch the status of the SmartLamp
	 @param startTime the time when the SmartLamp was started
	 @param kelvinValue the kelvin value of the SmartLamp
	 @param brightnessValue the brightness value of the SmartLamp
	 @param oldSwitchTime the previous switch time of the SmartLamp
	 */
	public SmartLamp(String name, String initialStatus, LocalDateTime switchTime,
					 LocalDateTime startTime, int kelvinValue, int brightnessValue, LocalDateTime oldSwitchTime) {
		this.name = name;
		this.switchTime = switchTime;
		this.kelvinValue = kelvinValue;
		this.oldSwitchTime = oldSwitchTime;
		this.brightnessValue = brightnessValue;
		this.initialStatus = initialStatus;
		if (initialStatus.equals("On")){
			this.startTime=TimeCoordinator.dateTime;
		}
		else{
			this.startTime=null;
		}
	}

	/**

	 Parses the input command string to create a new SmartLamp object and add it to the given ArrayList of SmartObjects.
	 The input command string should be in the format "save lamp lampName [initialStatus] [kelvinValue] [brightnessValue]"
	 where the square brackets indicate optional parameters. If initialStatus is not provided, it is set to "Off" by default.
	 If kelvinValue is not provided, it is set to 4000K by default. If brightnessValue is not provided, it is set to 100% by default.
	 The input parameters are validated and error messages are printed if they do not meet the required conditions.
	 @param line the input command string in the format "save lamp lampName [initialStatus] [kelvinValue] [brightnessValue]"
	 @param smartObjectArray the ArrayList of SmartObjects to which the new SmartLamp object is added
	 */
	public static void saveSmartLamp(String line, ArrayList<SmartObject> smartObjectArray) {
		OutputWriter.write("COMMAND: "+line);
		String[] lineArray = line.split("\t");
		String objectName = lineArray[2];
		String initialStatus;
		if (lineArray.length==3){
			initialStatus = "Off";
			SmartLamp smartLamp = new SmartLamp(objectName,initialStatus,null,
					null,4000,100,null);
			smartObjectArray.add(smartLamp);
			return;
		}
		else{
			initialStatus = lineArray[3];
		}
		if (!initialStatus.equals("On") && !initialStatus.equals("Off")){
			OutputWriter.write("ERROR: Erroneous command!");
			return;
		}
		if(lineArray.length==4){
			if (initialStatus.equals("On")){
				SmartLamp smartLamp = new SmartLamp(objectName,initialStatus,null,null,4000,100,null);
				smartObjectArray.add(smartLamp);
				return;
			}
			SmartLamp smartLamp = new SmartLamp(objectName,initialStatus,null,null,4000,100,null);
			smartObjectArray.add(smartLamp);
			return;
		}
		int kelvinValue;
		try {
			kelvinValue = Integer.parseInt(lineArray[4]);
		} catch (Exception e) {
			OutputWriter.write("ERROR: Kelvin value must be a positive number!");
			return;
		}
		//(!initialStatus.equals("On") && !initialStatus.equals("Off"))
		if(!(kelvinValue>=2000) || !(kelvinValue<=6500)){
			OutputWriter.write("ERROR: Kelvin value must be in range of 2000K-6500K!");
			return;
		}

		if (lineArray.length==6){
			int brightnessValue;
			try {
				brightnessValue = Integer.parseInt(lineArray[5]);
			} catch (Exception e) {
				OutputWriter.write("ERROR: Brightness value must be a positive number!");
				return;
			}
			if(!(brightnessValue>=0) || !(brightnessValue<=100)){
				OutputWriter.write("ERROR: Brightness must be in range of 0%-100%!");
				return;
			}
			SmartLamp smartLamp = new SmartLamp(objectName,initialStatus,null,null,kelvinValue,brightnessValue,null);
			smartObjectArray.add(smartLamp);
		}





	}



}
