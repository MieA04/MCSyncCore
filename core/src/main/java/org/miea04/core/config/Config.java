package org.miea04.core.config;

import org.miea04.core.StartMode;
import org.miea04.core.StartParameter;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * config
 *
 * @author MieMie
 */
public class Config {
    private static final Logger log = LoggerFactory.getLogger(Config.class);

    private static final String CONFIG_PACKAGE_PATH = "org.miea04.core.config";

    private static StartMode START_MOD;

    public static StartMode getStartMod() {
        return START_MOD;
    }

    private static Set<Class<?>> scanConfig() {
        // 偷懒，包体积换开发舒适度，记得正式发布的时候要用原生代码替换掉引入的org.reflections包依赖
        Reflections reflections = new Reflections(Config.CONFIG_PACKAGE_PATH);
        return reflections.getTypesAnnotatedWith(MCSyncConfig.class);
    }

    private static void instanceConfig(StartParameter sp) {
        Set<Class<?>> classes = scanConfig();

        for (Class<?> clazz : classes) {
            try {
                Method init = clazz.getMethod("init", StartParameter.class);
                init.invoke(null, sp);
            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                Throwable realException = e.getCause();
                throw new RuntimeException("配置初始化失败: " + realException.getMessage(), realException);
            }
        }
    }

    public static void init(StartParameter sp) {
        System.out.println(sp);
        START_MOD = sp.getServiceMode();
        instanceConfig(sp);
    }
}
