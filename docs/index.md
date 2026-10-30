# g2rain-crafter 文档

本目录记录 Crafter 的工具身份、生成契约和验证要求。中央登记与测试状态固定在 g2rain 架构库提交 `37b4dd4`。

Crafter 是谷雨G2rain智能体平台的开发期 Maven 工具。当前源码版本为 `1.0.9`；中央登记的历史版本与当前源码应分别追溯，不能据此推定新版本兼容性已验收。

2026-10-30 执行 `mvn test` 在依赖解析阶段失败，尚未运行测试；实际 POM 请求 Generator `1.0.8`。详细冲突见[依赖与版本](architecture/dependencies.md)，本轮状态见 `project.yaml` 的 `initializationVerification`。

- [项目元数据](project.yaml)
- [架构概览](architecture/overview.md)
- [生成契约](architecture/generation-contract.md)
- [依赖与版本](architecture/dependencies.md)
- [偏差](architecture/deviations.md)
- [本地开发](development/local-development.md)
- [测试策略](development/testing.md)
- [完成定义](development/definition-of-done.md)
- [发布](operations/release.md)
- [排障](operations/troubleshooting.md)
- [安全边界](security/security-boundaries.md)
- [需求入口](requirements/README.md)
