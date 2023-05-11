package com.jcoder.script.config;

import com.jcoder.script.engine.CustomScriptEngine;
import com.jcoder.script.service.PluginInstanceService;
import com.jcoder.script.pool.ScriptEnginePool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 初始化
 *
 * @author Jcode
 */
@Component
public class EngineInitialize implements ApplicationListener<ApplicationStartedEvent> {

    private final EngineProperties engineProperties;
    private final ApplicationContext applicationContext;

    @Autowired
    public EngineInitialize(EngineProperties engineProperties,
                            ApplicationContext applicationContext) {
        this.engineProperties = engineProperties;
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        Map<String, PluginInstanceService> instanceServiceMap = applicationContext.getBeansOfType(PluginInstanceService.class);
        instanceServiceMap.forEach((beanName, bean) -> CustomScriptEngine.instanceMap.putAll(bean.getPlugin()));
        ScriptEnginePool.initializePool(engineProperties);
    }
}
