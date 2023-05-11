package com.jcoder.script.controller;

import com.jcoder.script.controller.vo.ExecuteVO;
import com.jcoder.script.pool.ScriptEnginePool;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jcoder
 */
@RestController
public class ScriptExecuteController {

    @PostMapping("/v1/script/execute")
    public ResponseEntity<Object> execute(@RequestBody ExecuteVO paramVO) {
        Object result = ScriptEnginePool.execute(paramVO.getScript(), paramVO.getParam());
        return ResponseEntity.ok(result);
    }
}
