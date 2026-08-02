package org.miea04.core.config;

import org.miea04.core.StartMode;
import org.miea04.core.StartParameter;
import org.miea04.core.model.DefaultServerConfig;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Config
 *
 * @author MieMie
 */
public class Config {
    private static final String CONFIG_PACKAGE_PATH = "org.miea04.core.config";
    private static StartMode START_MODE;
    private static DefaultServerConfig.NodeType NODE_TYPE;
    private static String SERVER_PORT;
    private static String DELEGATE_HOST;

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

    private static void initStaticAttribute(StartParameter sp) {
        START_MODE = sp.getServiceMode();
        NODE_TYPE = sp.getNodeType();
        SERVER_PORT = sp.getServerPort();
    }

    public static void init(StartParameter sp) {
        initStaticAttribute(sp);
        instanceConfig(sp);
    }

    public static StartMode getStartMode() {
        return START_MODE;
    }

    public static DefaultServerConfig.NodeType getNodeType() {
        return NODE_TYPE;
    }

    public static String getServerPort() {
        return SERVER_PORT;
    }

    public static String getDelegateHost() {
        return DELEGATE_HOST;
    }
}
