package org.miea04.core.PlatForm;

import java.lang.management.ManagementFactory;

/**
 * EnvironmentTools
 *
 * @author MieMie
 */
public class Environment {

    public static PlatForm platformVersion() {
        String platFormName = ManagementFactory.getOperatingSystemMXBean().getName().toUpperCase();

        if (platFormName.contains("WIN")){
            return PlatForm.WIN;
        }

        if (platFormName.contains("LINUX")){
            return PlatForm.LINUX;
        }

        return null;

    }

}
