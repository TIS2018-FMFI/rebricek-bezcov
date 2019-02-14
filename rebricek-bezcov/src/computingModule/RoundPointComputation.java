package computingModule;
import java.util.List;

import InputFileClasses.ClassResult;
import InputFileClasses.PersonResult;
import InputFileClasses.ResultList;
import assigningModule.TotalResults;
import configurationModule.ConfigurationFile;


public class RoundPointComputation {
	ConfigurationFile configurationFile = new ConfigurationFile();
	List<ResultList> resultList;
	
	
	public RoundPointComputation(List<ResultList> resultList) {
		this.resultList = resultList;
		switch(configurationFile.getPointingType()) {
			case(1):
				countPointsWay1();
				break;
			case(2):
				countPointsWay2();
				break;
			case(3):
				countPointsWay3();
		}
		new TotalResults().resultsAssignment(resultList);
	}

	public void countPointsWay1(){
		int points;
		int[] rankedList = configurationFile.getPointingList();
		for(ResultList rl : resultList) {
			for(ClassResult cr : rl.getClassResult()) {
				for(PersonResult pr : cr.getPersonResult()) {
					if(pr.getResult().getPosition() == null)
						points = -1;
					else if(pr.getResult().getPosition() >= rankedList.length)
						points = rankedList[rankedList.length - 1];
					else
						points = rankedList[pr.getResult().getPosition()-1];
					pr.getResult().setPoints(points);
					logingPersonResult(pr);
				}
			}
		}
		
	}
	
	public void countPointsWay2(){
		for(ResultList rl : resultList) {
			for(ClassResult cr : rl.getClassResult()) {
				int numberRunnersInCategory = cr.getPersonResult().size();
				for(PersonResult pr : cr.getPersonResult()) {
					if(pr.getResult().getPosition() == null) {
						pr.getResult().setPoints(-1);
						logingPersonResult(pr);
						continue;
					}
					pr.getResult().setPoints(numberRunnersInCategory + 1 - pr.getResult().getPosition());
					logingPersonResult(pr);
				}
			}
		}
	}
	
	public void countPointsWay3(){
		for(ResultList rl : resultList) {
			for(ClassResult cr : rl.getClassResult()) {
				int bestTimeInCategory = cr.getPersonResult().get(0).getResult().getTime();   //findBestTime(cr);
				for(PersonResult pr : cr.getPersonResult()) {
					if(pr.getResult().getPosition() == null) {
						pr.getResult().setPoints(-1);
						logingPersonResult(pr);
						continue;
					}
					pr.getResult().setPoints(countPointsByTime(bestTimeInCategory, pr.getResult().getTime()));
					logingPersonResult(pr);
				}	
			}
		}
	}

	private void logingPersonResult(PersonResult pr) {
		System.out.println(pr.getPerson().getName().getGiven() +"-"+ pr.getPerson().getName().getFamily()+ ":" + pr.getResult().getPoints());
	}

	private int countPointsByTime(int winnerTime, int runnerTime) {
		int constant = configurationFile.getConstant();
		double ratio = (double)winnerTime / (double)runnerTime;
		return (int)Math.round(ratio * constant);
	}
}
