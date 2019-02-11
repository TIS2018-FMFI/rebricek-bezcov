package configurationModule;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class ConfigurationFile {

	private int seasonYear, rankedRounds, pointingType, constant;
	private int[] pointingList;
	
	public ConfigurationFile() {
		try {
			FileReader fileReader = new FileReader(new File("config.properties"));
			Properties properties = new Properties();
			properties.load(fileReader);
			seasonYear = Integer.parseInt(properties.getProperty("config.year"));
			rankedRounds = Integer.parseInt(properties.getProperty("config.numberOfRankedRounds"));
			pointingType = Integer.parseInt(properties.getProperty("config.pointingType"));
			if(pointingType == 1) {
				String list = properties.getProperty("config.list");
				String[] parts = list.split(",");
				pointingList = Arrays.stream(parts).mapToInt(Integer::parseInt).toArray();
			}
			else if(pointingType == 3) {
				constant = Integer.parseInt(properties.getProperty("config.constant"));
				}
			}
			catch(IOException e) {
				System.out.println(e);
			}
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

