# AGENTS.md

评审或开发前读取 `docs/project.yaml`、`docs/architecture/deviations.md`、中央 `platform-tools/g2rain-crafter.md`、当前需求、模板、测试和 Git Diff。

## 强制规则

- Crafter 是开发期 Maven 插件，不是生产服务，也不采用生成项目的运行时 Profile。
- skeleton 生成 API/Biz/Startup 骨架，foundry 复用底层 Generator；两类输出都必须与目标 Profile 一致。
- 输出路径必须规范化并限制在目标根内；不得通过项目名、包名、模板路径、绝对路径、`..` 或符号链接逃逸。
- 默认不覆盖现有文件；允许覆盖时必须展示目标范围并保护生成范围之外的文件。
- 数据库密码、Maven Central/GPG 凭据不得写入日志、生成文件、测试快照或仓库。
- 模板、Goal 参数、默认值和生成结构是跨项目契约，变化必须执行真实生成集成验证。

至少执行 `mvn test`。生成契约变化还要在临时目录运行 skeleton、foundry 和完整模式，并构建生成项目。需求仅选择 `aiCoding.activeRequirement` 或唯一 `开发中` 文档。
