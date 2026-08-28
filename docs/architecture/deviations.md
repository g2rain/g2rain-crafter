# 架构与安全偏差

| ID | 当前偏差 | 风险 | 处理方向 |
| --- | --- | --- | --- |
| CRAFTER-001 | skeleton 普通资源复制使用 `REPLACE_EXISTING` | 可能覆盖目标目录已有文件，且不受 foundry `tables.overwrite` 控制 | 新增目标目录非空保护、显式 force 参数和覆盖清单测试 |
| CRAFTER-002 | 目标路径由项目名、包路径和模板相对路径替换构造，未看到统一的 normalize/root containment 检查 | 恶意或错误输入可能导致路径逃逸 | 对所有输出执行绝对规范化、根包含和符号链接检查 |
| CRAFTER-003 | 当前单元测试主要覆盖编排和配置，缺少已发布插件在临时目录生成并构建完整项目的证据 | 包内模板、descriptor 或依赖兼容问题可能漏检 | 增加 Maven Invoker/隔离目录端到端测试 |
| CRAFTER-004 | 生成项目文档未在当前模板清单中证明包含 `AGENTS.md` 和 `docs/project.yaml` | 新项目可能缺少架构采用和 Agent 入口 | 更新模板并验证生成身份、Profile 和文档链接 |
| CRAFTER-005 | Crafter/Generator/模板/Profile 的正式兼容组合未以机器可读方式记录 | 发布后难以复现生成结果 | 在发布元数据和生成项目中记录四方版本 |
