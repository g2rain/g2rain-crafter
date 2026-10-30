# 依赖与版本

Crafter `1.0.9` 当前依赖 `g2rain-generator-maven-plugin 1.0.8`，内置模板目标为 `java-domain-service 1.0.0` 的 API/Biz/Startup 结构。

2026-10-30 核对：中央工具登记仍列 Crafter `1.0.7` / Generator `1.0.6`；当前根 POM 的 `g2rain.generator.version` 为 `1.0.8`。工作区已有的 `project.yaml` 修改将 `toolchain.generatorDependency` 写为 `1.0.6`，与 POM 不一致。本轮保留该未提交修改；依赖解析事实以 POM 为准，不能将中央历史组合当作当前组合。

本轮 `mvn test` 因无法解析 `com.g2rain:g2rain-generator-maven-plugin:1.0.8` 失败，本地缓存报告此前从配置的仓库解析失败。测试未开始，因此不能沿用历史 12 个测试通过的结果证明当前版本可构建。需先明确采用的 Generator 版本并确保其制品可解析，再重跑验证。

三者独立演进：Crafter 负责入口与骨架，Generator 负责表到代码，Profile 负责目标边界。任何一方改变模块、依赖、DTO、事务、数据库或配置规则，都要重新验证兼容组合，不能让已发布 Crafter 静默跟随未固定的 Generator 或模板。
