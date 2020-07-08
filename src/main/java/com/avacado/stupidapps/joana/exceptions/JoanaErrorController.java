package com.avacado.stupidapps.joana.exceptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import com.avacado.stupidapps.joana.annotations.JoanaRestController;

@JoanaRestController
@RequestMapping("/error")
public class JoanaErrorController implements ErrorController {

    private static Logger logger = LoggerFactory.getLogger(JoanaErrorController.class);

    @Value("${error.show.details}")
    private boolean debug;

    @Autowired
    private ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
	return "/error";
    }

    @RequestMapping
    public Map<String, Object> error(WebRequest request, HttpServletResponse response) throws IOException {
	Map<String, Object> error = getErrorAttributes(request);
	response.setStatus((int) error.get("status"));
	return error;
    }

    private Map<String, Object> getErrorAttributes(WebRequest request) {
	Set<Include> errorIncludeSet = new HashSet<>();
	if (debug) {
	    errorIncludeSet.add(Include.MESSAGE);
	    errorIncludeSet.add(Include.EXCEPTION);
	    errorIncludeSet.add(Include.STACK_TRACE);
	    errorIncludeSet.add(Include.BINDING_ERRORS);
	}
	Map<String, Object> map = new HashMap<>();
	map.putAll(this.errorAttributes.getErrorAttributes(request, ErrorAttributeOptions.of(errorIncludeSet)));
	Throwable error = this.errorAttributes.getError(request);
	logger.error("Unhandled exception from code", error);
	if (error instanceof JoanaException) {
	    map.put("message", ((JoanaException) error).getExceptionMessage());
	    map.put("status", ((JoanaException) error).getHttpStatus().value());
	    if (!debug)
		map.put("error", ((JoanaException) error).getExceptionMessage());
	}
	return map;
    }

}
