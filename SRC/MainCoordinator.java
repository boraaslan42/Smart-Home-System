import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * MainCoordinator is the main class of the program which controls the flow of
 * the system by receiving and interpreting user input, creating smart objects
 * and sending commands to them as well as managing and reporting their status.
 */
public class MainCoordinator {
	static ArrayList<SmartObject> smartObjectArray = new ArrayList<>();

	/**
	 * Checks if a smart object with the given name already exists in the system.
	 *
	 * @param line          the user input command line
	 * @param smartObjectArray  the ArrayList containing all smart objects in the system
	 * @param arrayPosition the index of the name field in the command line
	 * @return -1 if a smart object with the same name exists, 1 otherwise
	 */

	public static int existenceChecker(String line, ArrayList<SmartObject> smartObjectArray, int arrayPosition) {
		String name = line.split("\t")[arrayPosition];
		for (SmartObject smartObject : smartObjectArray) {
			if (name.equals(smartObject.getName())) {
				return -1;
			}
		}
		return 1;
	}

	/**
	 * Prints an error message for an erroneous command and logs it to the output file.
	 *
	 * @param line the erroneous command line
	 */
	public static void erroneousCommand(String line) {
		OutputWriter.write("COMMAND: " + line + "\nERROR: Erroneous command!");
	}
	/**
	 * Checks if the length of the command line is within the given range.
	 *
	 * @param line      the command line
	 * @param lineLength the length of the command line
	 * @param minLength the minimum length of the command line
	 * @param maxLength the maximum length of the command line
	 * @return -1 if the length of the command line is not within the given range, 1 otherwise
	 */
	static int lengthCheck(String line, int lineLength, int minLength, int maxLength) {
		if (!(minLength <= lineLength && lineLength <= maxLength)) {
			erroneousCommand(line);
			return -1;
		} else return 0;
	}
	/**
	 * Checks if the length of the command line is equal to one of the given lengths.
	 *
	 * @param line      the command line
	 * @param lineLength the length of the command line
	 * @param Length1 the first length to be checked
	 * @param Length2 the second length to be checked
	 * @param Length3 the third length to be checked
	 * @return -1 if the length of the command line is not equal to one of the given lengths, 1 otherwise
	 */
	static int lengthCheck(String line, int lineLength, int Length1, int Length2, int Length3) {
		if (!(lineLength == Length1 || lineLength == Length2 || lineLength == Length3)) {
			erroneousCommand(line);
			return -1;

		} else return 0;
	}

	/**

	 This method reads the input file and performs the operations specified in the file.

	 Before reading the file, it checks if the first line is "SetInitial". If it is, it sets the time using the

	 given date and deletes the first line from the array. If it is not, it prints an error message and terminates the program.

	 It then performs the following operations based on the command specified in the input file:

	 <ul>
	 <li>"Add" command: Adds the specified smart device to the list of smart devices.
	 <li>"Remove" command: Removes the specified smart device from the list of smart devices.
	 <li>"Switch" command: Switches the specified smart device on or off.
	 <li>"Set" command: Changes the mode of the specified smart device to the specified mode.
	 </ul>
	 If any of the commands is not valid, it prints an error message.
	 */
	public static void run() {
		ArrayList<String> inputArray;

		inputArray = new ArrayList<>(Arrays.asList(Objects.requireNonNull(InputReader.readFile(Main.inputFileName,
				true, true))));
        /*Before reading the file, we need to check if the first line is "SetInitial"
        If it is, we need to set the timeCoordinator.dateTime to the given date and delete the first line from the array
        If it is not, we need to print an error message and terminate the program*/
		if(inputArray.size()==0){
			OutputWriter.write("Input file is empty! Program is going to terminate!");
			OutputWriter.close();
			System.exit(0);

		}
		if (inputArray.get(0).contains("SetInitial")) {
			if (inputArray.get(0).split("\t").length != 2) {
				OutputWriter.write("COMMAND: "+inputArray.get(0));
				OutputWriter.write("ERROR: First command must be set initial time! Program is going to terminate!");
				OutputWriter.close();
				System.exit(0);
			}
			String givenDate = inputArray.get(0).split("\t")[1];
			TimeCoordinator.setTime(givenDate, inputArray.get(0));
			OutputWriter.write("COMMAND: " + inputArray.get(0));
			OutputWriter.write("SUCCESS: Time has been set to " + TimeCoordinator.dateTimeDisplay() + "!");
			inputArray.remove(0);
		} else {
			OutputWriter.write("COMMAND: " + inputArray.get(0));
			OutputWriter.write("ERROR: First command must be set initial time! Program is going to terminate!");
			OutputWriter.close();
			System.exit(0);
		}
		//start of program
		for (String line : inputArray) {
			if (line.contains("SetInitial")) {
				erroneousCommand(line);
				continue;
			}
			String command = line.split("\t")[0];
			int lineLength = line.split("\t").length;

			if (lineLength > 6) {
				erroneousCommand(line);
				continue;
			}
			switch (command) {
				case "Add":
					String objType = line.split("\t")[1];
					if (objType.equals("SmartCamera")) {
						if (lengthCheck(line, lineLength, 4, 5) == -1) continue;
						if (existenceChecker(line, smartObjectArray, 2) == -1) {
							OutputWriter.write("COMMAND: " + line + "\nERROR: There is already a smart device with same name!");
							break;
						}
						SmartCamera.saveSmartCamera(line, smartObjectArray);
					} else if (objType.equals("SmartLamp")) {
						if (lengthCheck(line, lineLength, 3, 4, 6) == -1) continue;
						if (existenceChecker(line, smartObjectArray, 2) == -1) {
							OutputWriter.write("COMMAND: " + line + "\nERROR: There is already a smart device with same name!");
							break;
						}
						SmartLamp.saveSmartLamp(line, smartObjectArray);
					} else if (objType.equals("SmartColorLamp")) {
						if (lengthCheck(line, lineLength, 3, 4, 6) == -1) continue;
						if (existenceChecker(line, smartObjectArray, 2) == -1) {
							OutputWriter.write("COMMAND: " + line + "\nERROR: There is already a smart device with same name!");
							break;
						}
						SmartColorLamp.saveSmartColorLamp(line, smartObjectArray);
					} else if (objType.equals("SmartPlug")) {
						if (lengthCheck(line, lineLength, 3, 4, 5) == -1) continue;
						if (existenceChecker(line, smartObjectArray, 2) == -1) {
							OutputWriter.write("COMMAND: " + line + "\nERROR: There is already a smart device with same name!");
							break;
						}
						SmartPlug.saveSmartPlug(line, smartObjectArray);
					} else {
						erroneousCommand(line);
					}
					break;
				case "Remove":
					if (lengthCheck(line, lineLength, 2, 2) == -1) continue;
					if (existenceChecker(line, smartObjectArray, 1) == 1) {
						OutputWriter.write("COMMAND: " + line + "\nERROR: There is not such a device!");
						break;
					}
					SmartObject.removeObj(line, smartObjectArray);
					break;
				case "Switch":
					if (lengthCheck(line, lineLength, 3, 3) == -1) continue;
					if (existenceChecker(line, smartObjectArray, 1) == 1) {
						OutputWriter.write("COMMAND: " + line + "\nERROR: There is not such a device!");
						break;
					}
					SmartObject.switchOnOff(line, smartObjectArray);
					break;
				case "ChangeName":
					if (lengthCheck(line, lineLength, 3, 3) == -1) continue;
					if (line.split("\t")[1].equals(line.split("\t")[2])) {
						OutputWriter.write("COMMAND: " + line + "\nERROR: Both of the names are the same, nothing changed!");
						break;
					}
					if (existenceChecker(line, smartObjectArray, 1) == 1) {
						OutputWriter.write("COMMAND: " + line + "\nERROR: There is not such a device!");
						break;
					}
					SmartObject.changeName(line, smartObjectArray);
					break;
				case "SetTime":
					if (lengthCheck(line, lineLength, 2, 2) == -1) continue;
					OutputWriter.write("COMMAND: " + line);
					String givenDate = line.split("\t")[1];
					TimeCoordinator.setTime(givenDate, line);
					break;
				case "SkipMinutes":
					if (lengthCheck(line, lineLength, 2, 2) == -1) continue;
					String minutes = line.split("\t")[1];
					TimeCoordinator.skipMinutes(minutes, line);
					/*if (!(smartObjectArray.size() == 0)) {
						ZreportUtilities.sort(MainCoordinator.smartObjectArray);
					} else {
						ZreportUtilities.checkForSwitches(MainCoordinator.smartObjectArray);
					}*/

					break;
				case "SetSwitchTime":
					if (lengthCheck(line, lineLength, 3, 3) == -1) continue;
					if (existenceChecker(line, smartObjectArray, 1) == 1) {
						//does not ekle
						OutputWriter.write("COMMAND: " + line + "\nERROR: There is not such a device!");
						break;
					}
					SmartObject.setSwitchTime(line, smartObjectArray);
					break;
				case "PlugIn":
					if (lengthCheck(line, lineLength, 3, 3) == -1) continue;
					if (existenceChecker(line, smartObjectArray, 1) == 1) {
						OutputWriter.write("COMMAND: " + line + "\nERROR: There is not such a device!");
						break;
					}
					SmartPlug.plugIn(line, smartObjectArray);
					break;
				case "PlugOut":
					if (lengthCheck(line, lineLength, 2, 2) == -1) continue;
					if (existenceChecker(line, smartObjectArray, 1) == 1) {
						OutputWriter.write("COMMAND: " + line + "\nERROR: There is not such a device!");
						break;
					}
					SmartPlug.plugOut(line, smartObjectArray);
					break;
				case "SetKelvin":
					if (lengthCheck(line, lineLength, 3, 3) == -1) continue;
					if (existenceChecker(line, smartObjectArray, 1) == 1) {
						OutputWriter.write("COMMAND: " + line + "\nERROR: There is not such a device!");
						break;
					}
					SmartObject.setKelvinCommand(line, smartObjectArray);
					break;
				case "SetBrightness":
					if (lengthCheck(line, lineLength, 3, 3) == -1) continue;
					SmartObject.setBrightnessCommand(line, smartObjectArray);
					break;
				case "SetColorCode":
					if (lengthCheck(line, lineLength, 3, 3) == -1) continue;
					SmartColorLamp.setColorCode(line, smartObjectArray);
					break;
				case "SetColor":
					if (lengthCheck(line, lineLength, 4, 4) == -1) continue;
					if (existenceChecker(line, smartObjectArray, 1) == 1) {
						OutputWriter.write("COMMAND: " + line + "\nERROR: There is not such a device!");
						break;
					}
					SmartColorLamp.setColor(line, smartObjectArray);
					break;
				case "SetWhite":
					if (lengthCheck(line, lineLength, 4, 4) == -1) continue;
					if (existenceChecker(line, smartObjectArray, 1) == 1) {
						OutputWriter.write("COMMAND: "+line+"\nERROR: There is not such a device!");
						break;
					}
					SmartObject.setWhiteCommand(line, smartObjectArray);
					break;
				case "ZReport":
					if (lengthCheck(line, lineLength, 1, 1) == -1) continue;
					ZreportUtilities.checkForSwitches(MainCoordinator.smartObjectArray);
					OutputWriter.write("COMMAND: ZReport");
					OutputWriter.write("Time is:\t" + TimeCoordinator.dateTimeDisplay());
					ZreportUtilities.sort(smartObjectArray);
					for (SmartObject obj : smartObjectArray) {
						OutputWriter.write(obj.toString());
					}

					break;
				case "Nop":
					SmartObject.skipToFirstSwitch(MainCoordinator.smartObjectArray);
					break;
				default:
					erroneousCommand(line);
					break;

			}


		}
		String lastLine = inputArray.get(inputArray.size() - 1);
		if (!lastLine.equals("ZReport")) {
			ZreportUtilities.checkForSwitches(MainCoordinator.smartObjectArray);
			OutputWriter.write("ZReport: ");
			OutputWriter.write("Time is:\t" + TimeCoordinator.dateTimeDisplay());
			ZreportUtilities.sort(smartObjectArray);
			for (SmartObject obj : smartObjectArray) {
				OutputWriter.write(obj.toString());
			}
		}
	}
}





