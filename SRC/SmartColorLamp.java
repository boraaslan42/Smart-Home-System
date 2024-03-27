import java.time.LocalDateTime;
import java.util.ArrayList;

public class SmartColorLamp extends SmartObject{
	int kelvinValue;
	int brightnessValue;
	protected LocalDateTime oldSwitchTime;

	String hexCode;



	boolean isWhite;
	/**
	 * Constructor for SmartColorLamp
	 * @param name id of the device
	 * @param initialStatus initial status of the device
	 * @param switchTime time when the device will switch
	 * @param startTime	time when the device started to work
	 * @param isWhite true if the lamp is white, false if it is colored
	 * @param hexCode hex code of the color
	 * @param kelvinValue kelvin value of the color
	 * @param brightnessValue brightness value of the color
	 */


	public SmartColorLamp(String name, String initialStatus, LocalDateTime switchTime, LocalDateTime startTime,
						  boolean isWhite,String hexCode,int kelvinValue,int brightnessValue,LocalDateTime oldSwitchTime) {
		this.name=name;
		this.oldSwitchTime = oldSwitchTime;
		this.initialStatus=initialStatus;
		this.startTime=startTime;
		this.hexCode = hexCode;
		this.kelvinValue = kelvinValue;
		this.brightnessValue = brightnessValue;
		this.isWhite = isWhite;

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
	 * Returns a string representation of the Smart Color Lamp.
	 *
	 * @return a string representation of the Smart Color Lamp
	 */
	@Override
	public String toString() {
		String strSwitchTime;
		if (switchTime == null) {
			strSwitchTime = "null";
		} else {
			strSwitchTime = switchTime.format(TimeCoordinator.formatter());
		}
		if (isWhite) {
			return "Smart Color Lamp " + name + " is " + initialStatus.toLowerCase() + " and its color value is " + kelvinValue +
					"K with " + brightnessValue + "% brightness, and its time to switch its status is " + strSwitchTime + ".";
		} else {
			return "Smart Color Lamp " + name + " is " + initialStatus.toLowerCase() + " and its color value is " + hexCode +
					" with " + brightnessValue + "% brightness, and its time to switch its status is " + strSwitchTime + ".";
		}
	}

	/**
	 * Returns the kelvin value.
	 *
	 * @return the kelvin value
	 */
	public int getKelvinValue() {
		return kelvinValue;
	}

	/**
	 * Sets the kelvin value.
	 *
	 * @param kelvinValue the kelvin value
	 */
	public void setKelvinValue(int kelvinValue) {
		this.kelvinValue = kelvinValue;
	}

	/**
	 * Returns the brightness value.
	 *
	 * @return the brightness value
	 */
	public int getBrightnessValue() {
		return brightnessValue;
	}

	/**
	 * Sets the brightness value.
	 *
	 * @param brightnessValue the brightness value
	 */
	public void setBrightnessValue(int brightnessValue) {
		this.brightnessValue = brightnessValue;
	}



	/**
	 * Sets the hex code.
	 *
	 * @param hexCode the hex code
	 */
	public void setHexCode(String hexCode) {
		this.hexCode = hexCode;
	}



	/**
	 * Sets whether the lamp is white.
	 *
	 * @param isWhite true if the lamp is white, false otherwise
	 */
	public void setIsWhite(boolean isWhite) {
		this.isWhite = isWhite;
	}
	/**

	 Sets the color and brightness of a smart color lamp object based on the given command line input.
	 @param line The command line input to set the color and brightness of a smart color lamp object.
	 @param smartObjectArray The array list of smart objects.
	 The command line input should have the following format:
	 SetColor\t[ObjectName]\t[ColorValue]\t[BrightnessValue]
	 @throws IllegalArgumentException If the input format is incorrect or the color or brightness values are invalid.
	 The color value must be a hexadecimal string of the form 0x[0-9A-Fa-f]+, and must be in the range of 0x000000-0xFFFFFF.
	 The brightness value must be a positive integer between 0 and 100 (inclusive).
	 If the input format is incorrect or the values are invalid, an error message will be written to the output log and the method will return without making any changes to the smart object.
	 If the specified object is not a smart color lamp, an error message will be written to the output log and the method will return without making any changes to the smart object.
	 */
	public static void setColor(String line, ArrayList<SmartObject> smartObjectArray) {
		//SetColor	Camera1	0xFFFFFF	100
		OutputWriter.write("COMMAND: "+line);
		String[] lineArray = line.split("\t");
		String objectName = lineArray[1];
		for (SmartObject smartObject : smartObjectArray) {
			if (smartObject.getName().equals(objectName)) {
				if (smartObject instanceof SmartColorLamp) {
					if (lineArray[2].matches("0x[0-9A-Fa-f]+")){
						String hexCode = lineArray[2];
						// Convert the hexadecimal string to an integer
						int value;
						try{
						 value = Integer.parseInt(hexCode.substring(2), 16);}
						catch (Exception e){
							OutputWriter.write("ERROR: Color code value must be in range of 0x0-0xFFFFFF!");
							return;
						}
						// Check if the integer value is within the valid range [0x000000, 0xFFFFFF]
						if (value >= 0 && value <= 16777215) {
							hexCode="1";
						}
						else{
							OutputWriter.write("ERROR: Color code value must be in range of 0x0-0xFFFFFF!");
							return;
						}}
					else{
						OutputWriter.write("ERROR: Erroneous command!");
						return;
					}

					int brightness;
					try {
						brightness = Integer.parseInt(lineArray[3]);
					} catch (Exception e) {
						OutputWriter.write("ERROR: Brightness value must be a positive number!");
						return;
					}
					if(!(brightness>=0) || !(brightness<=100)){
						OutputWriter.write("ERROR: Brightness must be in range of 0%-100%!");
						return;
					}
					((SmartColorLamp) smartObject).setHexCode(lineArray[2]);

					smartObject.setBrightnessValue(brightness);
					((SmartColorLamp) smartObject).setIsWhite(false);
					return;





				}
			}

			}
		OutputWriter.write("ERROR: This device is not a smart color lamp!");

	}

	/**
	 * Sets the color code of a smart color lamp object based on the command given in the input line.
	 *
	 * @param line a String that represents the command to set the color code of a smart color lamp object. The expected
	 *             format of the command is "SetColorCode\t<SmartObject Name>\t<Hex Code>".
	 * @param smartObjectArray an ArrayList of SmartObject objects that holds all the smart objects in the system.
	 */

	public static void setColorCode(String line, ArrayList<SmartObject> smartObjectArray) {
		String[] lineArray = line.split("\t");
		String objectName = lineArray[1];
		OutputWriter.write("COMMAND: "+line);
		for (SmartObject smartObject : smartObjectArray) {
			if (smartObject.getName().equals(objectName)) {
				if (smartObject instanceof SmartColorLamp) {
					if (lineArray[2].matches("0x[0-9A-Fa-f]+")){
						String hexCode = lineArray[2];

						// Convert the hexadecimal string to an integer
						int value = Integer.parseInt(hexCode.substring(2), 16);

						// Check if the integer value is within the valid range [0x000000, 0xFFFFFF]
						if (value >= 0 && value <= 16777215) {
							((SmartColorLamp) smartObject).setHexCode(lineArray[2]);
							((SmartColorLamp) smartObject).setIsWhite(false);

						}
						else{
							OutputWriter.write("ERROR: Color code value must be in range of 0x0-0xFFFFFF!");
						}
						return;
					}
					else {
						OutputWriter.write("ERROR: Erroneous command!");
						return;
					}


	}
				OutputWriter.write("ERROR: This device is not a smart color lamp!");
				return;
			}}}
	/**

	 This method takes in a command line containing information about a smart color lamp and saves the lamp in the given ArrayList of smart objects.
	 If the command is erroneous or contains invalid information, an error message is printed out.
	 @param line a string representing the command line containing information about the smart color lamp to be saved
	 @param smartObjectArray an ArrayList of SmartObjects containing all the smart devices in the smart home system
	 */

	public static void saveSmartColorLamp(String line, ArrayList<SmartObject> smartObjectArray) {
		OutputWriter.write("COMMAND: "+line);
		String[] lineArray = line.split("\t");
		String objectName = lineArray[2];
		String initialStatus;
		String hexCode = null;
		boolean isWhite;
		int value;
		int kelvin = 0;
		if (lineArray.length==3){
			initialStatus = "Off";}
		else{initialStatus = lineArray[3];}
		if (!initialStatus.equals("On") && !initialStatus.equals("Off")){
			OutputWriter.write("ERROR: Erroneous command!");
			return;}
		if(lineArray.length!=6){
			SmartColorLamp smartColorLamp = new SmartColorLamp(objectName,initialStatus,null, null, true,null,4000,100,null);
			smartObjectArray.add(smartColorLamp);
			return;
		}
		int isKelvin;
		try{
			kelvin = Integer.parseInt(lineArray[4]);
		}
		catch (Exception e){
			if (!(lineArray[4].matches("0x[0-9A-Fa-f]+"))) {
				OutputWriter.write("ERROR: Erroneous command!");
				return;
			}
		}
		int isHex=0;

		if (lineArray[4].matches("0x[0-9A-Fa-f]+")){

			hexCode = lineArray[4];

			// Convert the hexadecimal string to an integer
			value = Integer.parseInt(lineArray[4].substring(2), 16);

			// Check if the integer value is within the valid range [0x000000, 0xFFFFFF]
			if (value >= 0 && value <= 16777215) {
				isWhite=false;
				isHex=1;
			}

			else{
				OutputWriter.write("ERROR: Color code value must be in range of 0x0-0xFFFFFF!");
				return;
			}

		}
		else {
			try{
				kelvin = Integer.parseInt(lineArray[4]);
			}
			catch (Exception e){
					OutputWriter.write("ERROR: Erroneous command!");
					return;
				}
		}

		if(isHex==0){
		try {
			kelvin = Integer.parseInt(lineArray[4]);
		} catch (Exception e) {
			OutputWriter.write("ERROR: Kelvin value must be a positive number!");
			return;
		}
		if(!(kelvin>=2000) || !(kelvin<=6500)){
			OutputWriter.write("ERROR: Kelvin value must be in range of 2000K-6500K!");
			return;
		}
		isWhite=true;}
		else{
			isWhite=false;
		}



		int brightness;
		try {
			brightness = Integer.parseInt(lineArray[5]);
		} catch (Exception e) {
			OutputWriter.write("ERROR: Brightness value must be a positive number!");
			return;
		}
		if(!(brightness>=0) || !(brightness<=100)){
			OutputWriter.write("ERROR: Brightness must be in range of 0%-100%!");
			return;
		}
		SmartColorLamp smartColorLamp = new SmartColorLamp(objectName,initialStatus,null, null,isWhite,hexCode,kelvin,brightness,null);
		smartObjectArray.add(smartColorLamp);
	}
}