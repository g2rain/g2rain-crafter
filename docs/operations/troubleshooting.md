# 排障

| 现象 | 优先检查 |
| --- | --- |
| Goal 无法解析 | 插件坐标、版本、Maven Central/本地仓库和 plugin descriptor |
| 非交互运行卡住 | 必填参数、`System.console()` 和 config.file |
| 模板找不到 | 开发文件系统与发布 Jar 两种资源协议 |
| 文件被覆盖 | skeleton `REPLACE_EXISTING` 与 foundry `tables.overwrite` 的独立行为 |
| 包路径错误 | `archetype.package`、项目名替换和模板相对路径 |
| 生成代码失败 | JDBC、驱动、表名、Generator 版本和 codegen.properties |
| 发布失败 | Central 凭据、GPG、sources/javadoc 和 POM 元数据 |

排障输出必须隐藏数据库密码和发布凭据。
