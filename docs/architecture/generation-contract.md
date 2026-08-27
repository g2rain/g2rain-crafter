# 生成契约

- skeleton 输入包括 groupId、artifactId、version、package 和 description，输出根 POM、API/Biz/Startup 模块、启动类、配置及 `codegen.properties`。
- foundry 输入包括 JDBC、表、覆盖和数据隔离配置；显式 `-D` 参数优先于配置文件。
- 非交互运行缺少必填项时必须失败，不得等待控制台或猜测值。
- 数据库密码只用于连接，不能打印或写入模板默认配置。
- 所有模板占位符必须完成替换；生成后检查残留 `.ftl`、示例身份和未解析变量。
- 生成项目应声明目标 Profile、工具版本、Generator 版本和项目偏差入口。
- 模板或结构变化必须生成完整项目并执行该项目构建。
