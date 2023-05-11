package com.jcoder.script.pool;

import com.jcoder.script.config.EngineProperties;
import com.jcoder.script.engine.CustomScriptEngine;
import com.jcoder.script.exception.CommonException;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Objects;

/**
 * @author Jcoder
 */
public class ScriptEnginePool {

    private static GenericObjectPool<CustomScriptEngine> enginePool;
    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptEnginePool.class);

    /**
     * 初始化平台级对象池
     *
     * @param poolProperties 对象池属性
     */
    public static void initializePool(EngineProperties poolProperties) {

        long start = System.currentTimeMillis();
        enginePool = new GenericObjectPool<>(new ScriptEnginePoolFactory(poolProperties.getOuterJsRsList()));
        //最大空闲连接数
        enginePool.setMaxIdle(poolProperties.getMaxIdle());
        //最小空闲连接数
        enginePool.setMinIdle(poolProperties.getMinIdle());
        //最大连接数
        enginePool.setMaxTotal(poolProperties.getMaxTotal());
        //空闲连接检测周期
        enginePool.setTimeBetweenEvictionRuns(Duration.ofSeconds(poolProperties.getBetweenEvictionRuns()));
        //当对象池没有空闲对象时, 新获取对象的请求是否阻塞
        enginePool.setBlockWhenExhausted(true);
        //true为后进先出(LIFO)模式, 否则为先进先出(FIFO)模式
        enginePool.setLifo(false);
        //在向对象池中归还对象时是否检测对象有效 这个一般不建议设置为true，归还的对象校验有效性的意义不是很大，还会降低性能。重点保证获取到的对象是有效的就行
        enginePool.setTestOnReturn(false);
        try {
            enginePool.preparePool();
            LOGGER.debug("script engine pool has been initialized, it cost {}ms", (System.currentTimeMillis() - start));
        } catch (Exception e) {
            throw new CommonException("initialize pool for engine with error", e);
        }
    }

    /**
     * 执行js脚本
     *
     * @param script 脚本
     * @param param  参数
     * @return
     */
    public static Object execute(String script, Object param) {
        CustomScriptEngine engine = null;
        try {
            engine = enginePool.borrowObject();
            return engine.execute(script, param);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new CommonException(e.getMessage(), e);
        } finally {
            if (Objects.nonNull(engine)) {
                enginePool.returnObject(engine);
            }
        }
    }
}
