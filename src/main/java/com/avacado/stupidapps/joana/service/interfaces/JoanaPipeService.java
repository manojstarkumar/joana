package com.avacado.stupidapps.joana.service.interfaces;

import com.avacado.stupidapps.joana.domain.JoanaUser;
import com.avacado.stupidapps.joana.domain.pipe.JoanaPipe;
import com.avacado.stupidapps.joana.domain.pipe.JoanaPipeExecution;
import com.avacado.stupidapps.joana.protocol.request.JoanaCreatePipeRequest;

public interface JoanaPipeService {

    JoanaPipeExecution startPipeExecution(JoanaUser currentUser, String pipeId);

    JoanaPipeExecution getPipeExecution(String pipeId);

    void deletePipe(JoanaUser currentUser, String pipeId);

    JoanaPipe getPipe(String pipeId);

    JoanaPipe createPipe(JoanaUser currentUser, JoanaCreatePipeRequest createPipeRequest);

    JoanaPipe updatePipe(JoanaUser currentUser, String pipeId, JoanaCreatePipeRequest createPipeRequest);

}
