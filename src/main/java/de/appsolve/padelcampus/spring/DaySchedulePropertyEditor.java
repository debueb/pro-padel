/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.spring;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.appsolve.padelcampus.db.model.DaySchedule;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.Set;

/**
 * @author dominik
 */
@Component
public class DaySchedulePropertyEditor extends PropertyEditorSupport {

    private static final Logger LOG = Logger.getLogger(DaySchedulePropertyEditor.class);

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void setAsText(String text) {
        Object obj;
        try {
            obj = objectMapper.readValue(text, new TypeReference<Set<DaySchedule>>() {
            });
            setValue(obj);
        } catch (IOException ex) {
            LOG.error(ex);
        }
    }
}
