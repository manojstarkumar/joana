package com.avacado.stupidapps.joana.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.avacado.stupidapps.joana.annotations.JoanaRestController;
import com.avacado.stupidapps.joana.domain.pipe.JoanaPipeExecution;
import com.avacado.stupidapps.joana.service.interfaces.JoanaPipeService;

@JoanaRestController
@RequestMapping("/pipe/execution")
public class PipeExecutionController {

    @Autowired
    private JoanaPipeService joanaPipeService;
    
    @GetMapping("/{pipeId}/info")
    public JoanaPipeExecution getPipeExecution(@PathVariable String pipeId) {
	return joanaPipeService.getPipeExecution(pipeId);
    }
}
