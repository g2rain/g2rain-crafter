# 本地开发

需要 JDK 25 和 Maven 3.9。foundry 集成测试使用隔离数据库账号，只授予读取目标表元数据的权限。

```text
mvn test
mvn clean package
mvn clean install
```

验证生成行为时使用新建临时目录，不在真实业务仓库直接试验覆盖和异常输入。
