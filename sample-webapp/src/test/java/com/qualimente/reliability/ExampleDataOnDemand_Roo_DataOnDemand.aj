// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.qualimente.reliability;

import com.qualimente.reliability.Example;
import com.qualimente.reliability.ExampleDataOnDemand;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;

privileged aspect ExampleDataOnDemand_Roo_DataOnDemand {
    
    declare @type: ExampleDataOnDemand: @Component;
    
    private Random ExampleDataOnDemand.rnd = new SecureRandom();
    
    private List<Example> ExampleDataOnDemand.data;
    
    public Example ExampleDataOnDemand.getNewTransientExample(int index) {
        Example obj = new Example();
        return obj;
    }
    
    public Example ExampleDataOnDemand.getSpecificExample(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Example obj = data.get(index);
        Long id = obj.getId();
        return Example.findExample(id);
    }
    
    public Example ExampleDataOnDemand.getRandomExample() {
        init();
        Example obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Example.findExample(id);
    }
    
    public boolean ExampleDataOnDemand.modifyExample(Example obj) {
        return false;
    }
    
    public void ExampleDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = Example.findExampleEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Example' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Example>();
        for (int i = 0; i < 10; i++) {
            Example obj = getNewTransientExample(i);
            try {
                obj.persist();
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
    
}