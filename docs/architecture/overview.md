# 架构概览

Crafter 是 `maven-plugin`，通过 `g2rain:bootstrap` 提供两个阶段：

```text
skeleton: 内置 FreeMarker 模板 → API/Biz/Startup 多模块项目
foundry: 数据库配置 → g2rain-generator-maven-plugin → 业务代码
full: skeleton → foundry
```

`BootstrapMojo` 解析参数和编排阶段，`SkeletonConfig` 保存骨架输入，`SkeletonGenerator` 读取文件系统或 Jar 内模板并写入目标，底层 `FoundryGenerator` 负责业务代码生成。
