package assigningModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import configurationModule.ConfigurationFile;

public class RunnerOverall implements Comparable<RunnerOverall> {
	@Override
    public int compareTo(RunnerOverall runner){
        if (this.points > runner.points) {
            return -1;
        }if (this.points == runner.points) {
            return 0;
        }
        return 1;
	}
	
	static ConfigurationFile configurationFile = new ConfigurationFile();
	List<Integer> rounds = new ArrayList<>();
	int points = sumOfPoints();
	String familyName;
	String givenName;
	String identificator;

	
	
	public RunnerOverall(String identificator, String givenName, String familyName) {
		this.familyName = familyName;
		this.givenName = givenName;
		this.identificator = identificator;
	}


	public List<Integer> getRounds() {
		return rounds;
	}


	public void setRounds(List<Integer> rounds) {
		this.rounds = rounds;
	}


	 int sumOfPoints() {
		int sum = 0;
		int maxRounds = configurationFile.getRankedRounds();
		List<Integer> temp = new ArrayList<>();
		temp.addAll(rounds);
		Collections.sort(temp);
		Collections.reverse(temp);
		for(int i=0; i<temp.size();i++) {
			int tempPoints = temp.get(i);
			if(tempPoints == -1 || i == maxRounds)
				return sum;
			sum += temp.get(i);
		}
		return sum;
	}


	public String getFamilyName() {
		return familyName;
	}


	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}


	public String getGivenName() {
		return givenName;
	}


	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}


	public String getIdentificator() {
		return identificator;
	}


	public void setIdentificator(String identificator) {
		this.identificator = identificator;
	}


	public int getPoints() {
		return points;
	}


	public void setPoints(int points) {
		this.points = points;
	}
	
	
	
	
}
