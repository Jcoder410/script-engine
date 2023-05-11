package com.jcoder.script.engine;

import com.jcoder.script.exception.CommonException;
import com.oracle.truffle.js.runtime.JSContextOptions;
import org.apache.commons.io.IOUtils;
import org.graalvm.polyglot.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.graalvm.polyglot.HostAccess.newBuilder;

/**
 * 自定义js执行的context，基于GraalJS进行封装，包含注入扩展的java函数，注入自定义的js函数
 *
 * @author Jcoder
 * @date 2023-05-08
 */
public class CustomScriptEngine implements Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomScriptEngine.class);
    public static Map<String, Object> instanceMap = new HashMap<>();

    private static final HostAccess HOST_ACCESS = newBuilder()
            .allowPublicAccess(true)
            .allowAllImplementations(true)
            .allowAllClassImplementations(true)
            .allowArrayAccess(true)
            .allowListAccess(true)
            .allowBufferAccess(true)
            .allowIterableAccess(true)
            .allowIteratorAccess(true)
            .allowMapAccess(true)
            .targetTypeMapping(
                    Value.class, Object.class,
                    Value::hasArrayElements,
                    (v) -> v.as(List.class)
            )
            .build();
    private static final Map<String, String> OPTION_MAP = new HashMap<String, String>() {
        {
            put(JSContextOptions.CONSOLE_NAME, "false");
            put(JSContextOptions.JAVA_PACKAGE_GLOBALS_NAME, "false");
            put(JSContextOptions.PRINT_NAME, "false");
            put(JSContextOptions.SHELL_NAME, "false");
            put(JSContextOptions.GRAAL_BUILTIN_NAME, "false");
            put(JSContextOptions.POLYGLOT_BUILTIN_NAME, "false");
            put(JSContextOptions.POLYGLOT_EVALFILE_NAME, "false");
            put(JSContextOptions.LOAD_FROM_CLASSPATH_NAME, "false");
            put(JSContextOptions.LOAD_NAME, "false");
            put(JSContextOptions.LOAD_FROM_URL_NAME, "false");
        }
    };

    private static final Engine GRAAL_ENGINE = Engine.newBuilder().option("engine.WarnInterpreterOnly", "false").build();
    private final Context context;
    private Map<String, Object> GLOBAL_OBJECT_MAP;

    public CustomScriptEngine() {
        Context.Builder contextBuilder = getContextBuilder();
        this.context = contextBuilder
                .option("js.script-engine-global-scope-import", "true")
                .engine(GRAAL_ENGINE)
                .build();
        GLOBAL_OBJECT_MAP = injectOuterJsFunction("this", true);
    }

    /**
     * @param outerScript 外部js函数信息
     * @return
     */
    public Map<String, Object> injectOuterJsFunction(String outerScript, Boolean internalFlag) {
        String uid = UUID.randomUUID().toString();
        Source source = Source.newBuilder("js", outerScript, "internal-script-" + uid)
                .internal(internalFlag)
                .cached(true)
                .buildLiteral();
        return this.context.eval(source).as(new TypeLiteral<Map<String, Object>>() {
        });
    }

    /**
     * 初始化会话信息
     *
     * @param dataMap
     */
    public void initSessionData(Map<String, Object> dataMap) {
        GLOBAL_OBJECT_MAP.put("_session", dataMap);
    }

    /**
     * 注入java实例中包含的函数
     *
     * @param name         名称
     * @param javaInstance java实例
     */
    public void injectJavaInstance(String name, Object javaInstance) {
        GLOBAL_OBJECT_MAP.put(name, javaInstance);
    }

    /**
     * 执行脚本
     *
     * @param script    脚本
     * @param paramData 参数
     * @return
     */
    public Object execute(String script, Object paramData) {
        //注入入参信息
        GLOBAL_OBJECT_MAP.put("_param", paramData);
        //构建执行脚本
        StringBuilder sb = new StringBuilder(script);
        sb.append("\nprocess(_param);");
        //执行脚本
        Object result = this.context.eval("js", sb.toString()).as(Object.class);
        return result;
    }

    /**
     * 获取GraalJS的可执行实例
     *
     * @return
     */
    private Context.Builder getContextBuilder() {
        return Context.newBuilder()
                .allowExperimentalOptions(true)
                .allowPolyglotAccess(PolyglotAccess.ALL)
                .allowHostAccess(HOST_ACCESS)
                .allowNativeAccess(false)
                .allowHostClassLookup((className) -> {
                    return false;
                })
                .allowEnvironmentAccess(EnvironmentAccess.NONE)
                .allowIO(false)
                .allowCreateProcess(false)
                .allowExperimentalOptions(true)
                .options(OPTION_MAP)
                .allowCreateThread(false)
                .allowValueSharing(true);
    }

    /**
     * 初始化扩展信息
     *
     * @param resourceList 自定义js函数资源文件
     */
    public void initExtension(List<String> resourceList) {

        instanceMap.forEach(this::injectJavaInstance);

        StringBuilder outJs = new StringBuilder();
        for (String location : resourceList) {

            if (outJs.length() > 0) {
                outJs.append("\n");
            }

            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(location)) {
                String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                outJs.append(content);
            } catch (IOException e) {
                LOGGER.error("read outer js resource file with error", e);
                throw new CommonException("read outer js resource file with error", e);
            }
        }
        injectOuterJsFunction(outJs.toString(), false);
    }

    @Override
    public void close() throws IOException {
        this.context.close();
    }
}
