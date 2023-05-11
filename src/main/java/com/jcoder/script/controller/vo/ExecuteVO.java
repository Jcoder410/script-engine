package com.jcoder.script.controller.vo;

/**
 * 执行参数vo
 * @author Jcoder
 */
public class ExecuteVO {
    private String script;
    private Object param;

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }
}
