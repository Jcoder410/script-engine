package com.jcoder.script.service.impl;

import com.jcoder.script.service.PluginInstanceService;
import com.jcoder.script.plugin.SpelUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EnginePluginImpl implements PluginInstanceService {
    @Override
    public Map<String, Object> getPlugin() {
        return new HashMap<String, Object>() {{
            put("_spel", new SpelUtils());
        }};
    }
}
