package com.geekhome.coremodule.automation;

import com.geekhome.common.DescriptiveName;
import com.geekhome.coremodule.automation.EvaluableAutomationUnit;

import java.util.Calendar;

public class AlwaysOnAutomationUnit extends EvaluableAutomationUnit {

    public AlwaysOnAutomationUnit() {
        super(new DescriptiveName("alwayson", "alwayson"));
    }

    @Override
    protected boolean doEvaluate(Calendar now) {
        return true;
    }
}