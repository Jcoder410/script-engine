package com.jcoder.script.pool;

import com.jcoder.script.engine.CustomScriptEngine;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.DestroyMode;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.List;
import java.util.Map;

/**
 * 可执行对象池工厂类
 *
 * @author Qiu
 */
public class ScriptEnginePoolFactory extends BasePooledObjectFactory<CustomScriptEngine> {

    private List<String> resourceList;

    public ScriptEnginePoolFactory(List<String> resourceList) {
        this.resourceList = resourceList;
    }

    @Override
    public CustomScriptEngine create() {
        CustomScriptEngine engine = new CustomScriptEngine();
        engine.initExtension(resourceList);
        return engine;
    }

    @Override
    public PooledObject<CustomScriptEngine> wrap(CustomScriptEngine engine) {
        return new DefaultPooledObject(engine);
    }

    @Override
    public void destroyObject(PooledObject<CustomScriptEngine> executorObject, DestroyMode destroyMode) throws Exception {
        CustomScriptEngine engine = executorObject.getObject();
        engine.close();
    }
}
