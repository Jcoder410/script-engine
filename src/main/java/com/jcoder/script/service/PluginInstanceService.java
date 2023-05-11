package com.jcoder.script.service;

import java.util.Map;

/**
 * java插件函数信息获取接口
 *
 * @author Jcoder
 */
public interface PluginInstanceService {

    /**
     * 获取插件函数信息
     *
     * @return
     */
    Map<String, Object> getPlugin();

}
