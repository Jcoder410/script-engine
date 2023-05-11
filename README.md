## 目标
* use GraalJS to execute java script<br/>
* 支持自定义java函数注册成js函数<br/>
* 可以通过js函数进行逻辑书写和实现<br/>
* 结合aop可以修改程序执行逻辑<br/>
* 结合接口，可以任意构建出需要的接口逻辑<br/>
* 导入自定义js函数<br/>
* 导入外部js library<br/>
## 基础依赖
* 21.3.0版本的graaljs<br/>
* 2.7.2版本的spring-boot-starter-parent<br/>
## 待定
* 文件支持<br/>
* 多线程交互支持<br/>
## 使用说明
* 拓展函数注册信息在service下
* 直接调用可使用方法：ScriptEnginePool.execute(String script, Object param);
* 接口调用实现在controller
* 配置项相关信息对应类为EngineProperties
