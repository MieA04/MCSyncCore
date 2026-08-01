package org.miea04.core.tasks.task;

import org.miea04.core.tasks.parameter.TaskParams;

/**
 * task
 *
 * @author MieMie
 */
public interface Task<P extends TaskParams> {

    Class<?> start(P params);

}
