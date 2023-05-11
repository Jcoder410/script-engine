package com.jcoder.script.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

/**
 * @author Jcoder
 */
@ConfigurationProperties(prefix = "script.engine")
public class EngineProperties {

    /**
     * 外部js资源文件匹配表达式
     */
    private List<String> outerJsRsList = defalutRsList();

    /**
     * 执行器对象池最大空闲连接数
     */
    private Integer maxIdle = 20;

    /**
     * 执行器对象池最小空闲连接数
     */
    private Integer minIdle = 5;

    /**
     * 执行器对象池最大连接数
     */
    private Integer maxTotal = 50;

    /**
     * 执行器对象池空闲连接检测周期(秒),默认60
     */
    private Integer betweenEvictionRuns = 30;

    public List<String> getOuterJsRsList() {
        return outerJsRsList;
    }

    public void setOuterJsRsList(List<String> outerJsRsList) {
        this.outerJsRsList = outerJsRsList;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Integer getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getBetweenEvictionRuns() {
        return betweenEvictionRuns;
    }

    public void setBetweenEvictionRuns(Integer betweenEvictionRuns) {
        this.betweenEvictionRuns = betweenEvictionRuns;
    }

    private List<String> defalutRsList() {
        return Arrays.asList("classpath*:lodash.js");
    }
}
