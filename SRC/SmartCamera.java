import java.time.LocalDateTime;
import java.util.ArrayList;

public class SmartCamera extends SmartObject {
	public String name;
	private double megabytesConsumedPerRecord;
	protected double mbStorage;
	protected LocalDateTime oldSwitchTime;

	private LocalDateTime startTime;
	private LocalDateTime switchTime;
	private String initialStatus;
	/**
	 * Constructor for SmartCamera objects.
	 * @param name the name of the SmartCamera
	 * @param megabytesConsumedPerRecord the amount of megabytes consumed per record
	 * @param initialStatus the initial status of the SmartCamera
	 * @param switchTime the time at which the SmartCamera should switch its status
	 * @param mbStorage the amount of storage in megabytes
	 * @param startTime the start time of the SmartCamera
	 * @param oldSwitchTime the time of the SmartCamera's previous status switch
	 */
	public SmartCamera(String name, double megabytesConsumedPerRecord, String initialStatus, LocalDateTime switchTime,
					   int mbStorage, LocalDateTime startTime,LocalDateTime oldSwitchTime) {
		this.name = name;
		this.oldSwitchTime = oldSwitchTime;
		this.megabytesConsumedPerRecord = megabytesConsumedPerRecord;
		this.initialStatus = initialStatus;
		this.startTime = startTime;
	}

	/**
	 This static method creates a new SmartCamera object and adds it to the ArrayList of SmartObjects. It receives a command line
	 in the form of a string containing the object name, the megabytes consumed per record, and the initial status of the SmartCamera.
	 The command line can also include a fourth argument representing the amount of storage allocated to the SmartCamera.
	 If no storage value is provided, it will be initialized to 0.
	 If the initial status is "On", the SmartCamera's start time will be set to the current time, otherwise, it will be null.
	 @param line the command line containing the SmartCamera's attributes
	 @param smartObjectArray the ArrayList containing all the SmartObjects
	 @throws NumberFormatException if the megabytes consumed per record value is not a positive number
	 @throws IllegalArgumentException if the initial status is neither "On" nor "Off"
	 */
	public static void saveSmartCamera(String line, ArrayList<SmartObject> smartObjectArray) {
		OutputWriter.write("COMMAND: " + line);
		String[] lineArray = line.split("\t");
		String objectName = lineArray[2];
		String initialStatus;
		LocalDateTime startTime;
		if (lineArray.length == 4) {
			initialStatus = "Off";
		} else {
			initialStatus = lineArray[4];
		}
		if (!initialStatus.equals("On") && !initialStatus.equals("Off")) {
			OutputWriter.write("ERROR: Erroneous command!");
			return;
		}
		double megabytesConsumedPerRecord;
		try {
			megabytesConsumedPerRecord = Double.parseDouble(lineArray[3]);
		} catch (Exception e) {
			OutputWriter.write("ERROR: Megabyte value must be a positive number!");
			return;
		}
		if (megabytesConsumedPerRecord <= 0) {
			OutputWriter.write("ERROR: Megabyte value must be a positive number!");
			return;
		}

		if (initialStatus.equals("On")) {
			startTime = TimeCoordinator.dateTime;
		} else {
			startTime = null;
		}
		SmartCamera smartCamera = new SmartCamera(objectName, megabytesConsumedPerRecord, initialStatus,
				null, 0, startTime,null);
		smartObjectArray.add(smartCamera);
	}
	/**
	 * This method returns the amount of megabytes consumed by the SmartCamera since its start time.
	 * @return the amount of megabytes consumed by the SmartCamera since its start time
	 */
	public double getMegabytesConsumedPerRecord() {
		return megabytesConsumedPerRecord;
	}
	/**
	 * Sets the number of megabytes consumed per record.
	 *
	 * @param megabytesConsumedPerRecord the number of megabytes consumed per record
	 */
	public void setMegabytesConsumedPerRecord(int megabytesConsumedPerRecord) {
		this.megabytesConsumedPerRecord = megabytesConsumedPerRecord;
	}

	/**
	 * Returns the total megabytes of storage.
	 *
	 * @return the total megabytes of storage
	 */
	@Override
	public double getMbStorage() {
		return mbStorage;
	}

	/**
	 * Sets the total megabytes of storage.
	 *
	 * @param mbStorage the total megabytes of storage
	 */
	public void setMbStorage(double mbStorage) {
		this.mbStorage = mbStorage;
	}

	/**
	 * Returns the switch time.
	 *
	 * @return the switch time
	 */
	public LocalDateTime getSwitchTime() {
		return switchTime;
	}

	/**
	 * Sets the switch time.
	 *
	 * @param switchTime the switch time
	 */
	public void setSwitchTime(LocalDateTime switchTime) {
		this.switchTime = switchTime;
	}

	/**
	 * Returns the start time.
	 *
	 * @return the start time
	 */
	@Override
	public LocalDateTime getStartTime() {
		return startTime;
	}

	/**
	 * Sets the start time.
	 *
	 * @param startTime the start time
	 */
	@Override
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	/**
	 * Returns the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the initial status.
	 *
	 * @return the initial status
	 */
	public String getInitialStatus() {
		return initialStatus;
	}

	/**
	 * Sets the initial status.
	 *
	 * @param initialStatus the initial status
	 */
	public void setInitialStatus(String initialStatus) {
		this.initialStatus = initialStatus;
	}

	/**
	 * Returns the old switch time.
	 *
	 * @return the old switch time
	 */
	@Override
	public LocalDateTime getOldSwitchTime() {
		return oldSwitchTime;
	}

	/**
	 * Sets the old switch time.
	 *
	 * @param oldSwitchTime the old switch time
	 */
	@Override
	public void setOldSwitchTime(LocalDateTime oldSwitchTime) {
		this.oldSwitchTime = oldSwitchTime;
	}
	/**
	 * This method returns a string representation of the SmartCamera object.
	 * @return a string representation of the SmartCamera object
	 */
	public String toString() {
		String strMbStorage = String.format("%.2f", mbStorage);
		String strSwitchTime;
		if (switchTime==null){
			strSwitchTime="null";
		}
		else{
			strSwitchTime=switchTime.format(TimeCoordinator.formatter());
		}
		return "Smart Camera "+name+" is "+initialStatus.toLowerCase()+" and used "+strMbStorage+" MB of storage so far " +
				"(excluding current status), and its time to" +
				" switch its status is "+strSwitchTime+".";
		}
}