package configurationModule;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class ConfigurationFile {

	private int seasonYear, rankedRounds, pointingType, constant;
	private int[] pointingList;
	
	public ConfigurationFile(String configFile) {
		try {
			Scanner scanner = new Scanner(new File(configFile));
			seasonYear = takeIntegerInLine(scanner.nextLine());
			rankedRounds = takeIntegerInLine(scanner.nextLine());
			pointingType = takeIntegerInLine(scanner.nextLine());
			if(pointingType == 1) {
				String line = scanner.nextLine();
				int index = line.indexOf("//");
				line = line.substring(0, index).trim();
				String[] parts = line.split(",");
				pointingList = Arrays.stream(parts).mapToInt(Integer::parseInt).toArray();
			}
			else if(pointingType == 3) {
				scanner.nextLine();
				constant = takeIntegerInLine(scanner.nextLine());
				}
			scanner.close();
			}
			catch(IOException e) {
				System.out.println(e);
			}
	}
	
	
	private int takeIntegerInLine(String line) {
		int index = line.indexOf("//");
		return Integer.parseInt(line.substring(0, index).trim());
	}


	public int getSeasonYear() {
		return seasonYear;
	}


	public int getRankedRounds() {
		return rankedRounds;
	}


	public int getPointingType() {
		return pointingType;
	}


	public int[] getPointingList() {
		return pointingList;
	}


	public int getConstant() {
		return constant;
	}
	
}
