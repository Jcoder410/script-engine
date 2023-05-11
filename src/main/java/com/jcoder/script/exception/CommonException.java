package com.jcoder.script.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * 通用异常处理类
 *
 * @author Jcoder
 */
public class CommonException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(CommonException.class);
    private static final long serialVersionUID = 0;
    private final transient Object[] parameters;
    private String code;

    public CommonException(String code, Object... parameters) {
        super(code);
        this.parameters = parameters;
        this.code = code;
    }

    public CommonException(String code, Throwable cause, Object... parameters) {
        super(code, cause);
        this.parameters = parameters;
        this.code = code;
    }

    public CommonException(String code, Throwable cause) {
        super(code, cause);
        this.code = code;
        this.parameters = new Object[0];
    }

    public CommonException(Throwable cause, Object... parameters) {
        super(cause);
        this.parameters = parameters;
    }

    public Object[] getParameters() {
        return this.parameters;
    }

    public String getCode() {
        return this.code;
    }

    public String getTrace() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = null;

        label57:
        {
            Object var4;
            try {
                ps = new PrintStream(baos, false, StandardCharsets.UTF_8.name());
                break label57;
            } catch (UnsupportedEncodingException var8) {
                logger.error("Error get trace, unsupported encoding.", var8);
                var4 = null;
            } finally {
                if (ps != null) {
                    ps.close();
                }

            }

            return (String) var4;
        }

        this.printStackTrace(ps);
        ps.flush();
        return new String(baos.toByteArray(), StandardCharsets.UTF_8);
    }

}
