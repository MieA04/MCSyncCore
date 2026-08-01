package org.miea04.core.tasks.parameter;

import org.miea04.core.StartParameter;

/**
 * StartupCheckParam
 *
 * @author MieMie
 */
public class StartupCheckParam implements TaskParams{

    private StartParameter startParameter;

    public StartupCheckParam(StartParameter startParameter) {
        this.startParameter = startParameter;
    }

    public StartParameter getStartParameter() {
        return startParameter;
    }

    public void setStartParameter(StartParameter startParameter) {
        this.startParameter = startParameter;
    }
}
