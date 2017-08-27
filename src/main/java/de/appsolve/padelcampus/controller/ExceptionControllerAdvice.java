package de.appsolve.padelcampus.controller;

import com.sparkpost.exception.SparkPostException;
import com.sparkpost.model.responses.ServerErrorResponse;
import com.sparkpost.model.responses.ServerErrorResponses;
import de.appsolve.padelcampus.exceptions.MailException;
import de.appsolve.padelcampus.exceptions.ResourceNotFoundException;
import de.appsolve.padelcampus.reporting.ErrorReporter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice
public class ExceptionControllerAdvice {

    private static final Logger LOG = Logger.getLogger(ExceptionControllerAdvice.class);

    @Autowired
    protected ErrorReporter errorReporter;

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleException(HttpServletRequest req, Exception ex) {
        errorReporter.notify(ex);
        LOG.error(ex.getMessage(), ex);
        return new ModelAndView("error/500", "Exception", ex);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleResourceNotFoundException(HttpServletRequest request, Exception ex) {
        LOG.error(ex.getMessage() + " " + request.getRequestURI());
        return new ModelAndView("error/404", "Exception", ex);
    }

    @ExceptionHandler(value = MailException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleMailException(HttpServletRequest request, MailException ex) {
        if (ex.getCause() != null && ex.getCause() instanceof SparkPostException) {
            SparkPostException e = (SparkPostException) ex.getCause();
            ServerErrorResponses serverErrorResponses = e.getServerErrorResponses();
            if (serverErrorResponses != null) {
                List<ServerErrorResponse> errors = serverErrorResponses.getErrors();
                if (errors != null && !errors.isEmpty()) {
                    return new ModelAndView("error/mailexception", "Exception", e);
                }
            }
        }
        return handleException(request, ex);
    }
}
