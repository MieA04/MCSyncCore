package org.miea04.core.tasks.task;

import org.miea04.core.tasks.parameter.TaskParams;
import org.miea04.core.tasks.result.TaskResult;

/**
 * task
 *
 * @author MieMie
 */
public interface Task {

    TaskResult start(TaskParams taskParams);

}
