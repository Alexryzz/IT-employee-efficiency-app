package org.coursework.app.service.util;

import org.coursework.app.entity.Task;
import org.coursework.app.enums.taskEnums.TaskStatus;
import org.coursework.app.enums.taskEnums.TaskType;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatsUtil {
    public static Map<TaskType, Short> considerTypeEffectivity(List<Task> tasks){
        Map<TaskType, Short> map = new HashMap<>();

        short countTasks = (short) tasks.size();
        short prodDroppedTasks = 0;
        short newFeatureTasks = 0;
        short bagTasks = 0;
        short testingTasks = 0;

        for(Task task : tasks){
            if(task.getTaskType().equals(TaskType.PROD_DROPPED)){
                prodDroppedTasks++;
            }
            if(task.getTaskType().equals(TaskType.NEW_FEATURE)){
                newFeatureTasks++;
            }
            if(task.getTaskType().equals(TaskType.BAG)){
                bagTasks++;
            }
            if(task.getTaskType().equals(TaskType.TESTING)){
                testingTasks++;
            }
        }
        map.put(TaskType.PROD_DROPPED,
                (short) Math.round((float) prodDroppedTasks / countTasks * 100)
                );
        map.put(TaskType.NEW_FEATURE,
                (short) Math.round((float) newFeatureTasks / countTasks * 100)
        );
        map.put(TaskType.BAG,
                (short) Math.round((float) bagTasks / countTasks * 100)
                );
        map.put(TaskType.TESTING,
                (short) Math.round((float) testingTasks / countTasks * 100)
                );
        return map;
    }

    public static Short considerTimeEffectivity(Short oldEffective, int countTasks ,Task task){
        short timeEffectivity;
        LocalDateTime dateTime1 = task.getTaskGetDate();
        LocalDateTime dateTime2 = task.getDeadline();
        LocalDateTime dateTimeDone = task.getTaskDoneDate();

        long secondsForCompletion = ChronoUnit.SECONDS.between(dateTime1, dateTimeDone);
        if (secondsForCompletion == 0){
            secondsForCompletion = 1;
        }
        long startSecondsForEffectivity = ChronoUnit.SECONDS.between(dateTime1, dateTime2)/2;
        long endSecondsForEffectivity = ChronoUnit.SECONDS.between(dateTime2, dateTimeDone)*2;

        long percent = startSecondsForEffectivity/secondsForCompletion;
        if(percent > 1){
            timeEffectivity = 100;
            return timeEffectivity;
        }
        if (percent < startSecondsForEffectivity/endSecondsForEffectivity){
            timeEffectivity = 1;
            return timeEffectivity;
        }

        long onePercentSec = (endSecondsForEffectivity-startSecondsForEffectivity)/100;
        timeEffectivity = (short) Math.round((1.0 - (double) secondsForCompletion /onePercentSec) * 100);

        return (short) ((oldEffective + timeEffectivity) / countTasks);

    }
}
