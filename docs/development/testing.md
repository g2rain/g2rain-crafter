# 测试策略

- `mvn test` 验证 Mojo、配置优先级、模板渲染和基本生成行为。
- 验证 help/bootstrap descriptor 和发布 Jar 内模板可读取。
- 临时目录覆盖 skeleton、foundry、full、交互/非交互、缺参、非法 phase、目标非空、路径逃逸和 overwrite。
- 检查数据库密码不出现在日志和生成文件。
- 构建生成项目，检查模块、包路径、占位符、文档和目标 Profile。
- 发布前验证 POM、插件 descriptor、sources、javadoc、签名和 Maven Central 坐标。
