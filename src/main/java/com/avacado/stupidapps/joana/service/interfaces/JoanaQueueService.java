package com.avacado.stupidapps.joana.service.interfaces;

import com.avacado.stupidapps.joana.domain.pipe.JoanaPipeExecution;
import com.avacado.stupidapps.joana.domain.task.JoanaTaskExecution;

public interface JoanaQueueService {

    void queueTaskForProcessing(JoanaTaskExecution persistedExecution);

    void queuePipeForProcessing(JoanaPipeExecution persistedExecution);
}
