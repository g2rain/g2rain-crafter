# 发布

1. 执行 `mvn test` 和 `mvn clean package`。
2. 在临时目录验证已打包插件的 skeleton、foundry 和 full 模式。
3. 构建生成项目并记录 Crafter、Generator、模板和 Profile 组合。
4. 检查插件 descriptor、sources、javadoc、POM 和签名。
5. 使用 `release` Profile 发布；Maven Central 和 GPG 凭据只从 CI Secret 注入。
6. 发布后通过完整坐标运行 `help` 和 `bootstrap` 冒烟验证。

已发布版本不得静默改变模板或底层 Generator；兼容性改变需要发布新 Crafter 版本。
