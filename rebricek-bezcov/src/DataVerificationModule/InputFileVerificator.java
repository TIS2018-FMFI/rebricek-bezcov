package DataVerificationModule;

import InputFileClasses.Event;
import InputFileClasses.ResultList;
import InputFileClasses.StartTime;
import InteractionModule.InteractionModule;


/**
 * @author TomasTakacs
 */
public class InputFileVerificator {


    public static ResultList verifyInputFile(ResultList resultList){
        if (resultList.getEvent() == null){
            resultList.setEvent(new Event());
        }

        Event event = resultList.getEvent();

        if (event.getRound() == null){
            Integer value = InteractionModule.requestRaceRank();
            event.setRound(value);
        }
        if (event.getName() == null){
            String value = InteractionModule.requestRaceName();
            event.setName(value);
        }
        if (event.getStartTime() == null || event.getStartTime().getDate() == null){
            String value = InteractionModule.requestRaceStartTime();
            StartTime startTime = new StartTime();
            startTime.setDate(value);
            event.setStartTime(startTime);
        }

        return resultList;
    }
}
