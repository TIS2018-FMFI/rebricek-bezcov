package DataVerificationModule;

import InputFileClasses.Event;
import InputFileClasses.ResultList;
import InputFileClasses.StartTime;
import InteractionModule.InteractionModule;

import java.util.List;

/**
 * @author TomasTakacs
 */
public class InputFileVerificator {

    public static ResultList verifyInputFile(ResultList resultList){
        // skontroluje ...
        Event event = resultList.getEvent();

        if (event.getRank() == null){
            Integer value = InteractionModule.requestRaceRank();
            event.setRank(value.toString());
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
