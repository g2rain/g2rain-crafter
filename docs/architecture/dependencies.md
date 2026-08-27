# 依赖与版本

Crafter `1.0.7` 当前依赖 `g2rain-generator-maven-plugin 1.0.6`，内置模板目标为 `java-domain-service 1.0.0` 的 API/Biz/Startup 结构。

三者独立演进：Crafter 负责入口与骨架，Generator 负责表到代码，Profile 负责目标边界。任何一方改变模块、依赖、DTO、事务、数据库或配置规则，都要重新验证兼容组合，不能让已发布 Crafter 静默跟随未固定的 Generator 或模板。
