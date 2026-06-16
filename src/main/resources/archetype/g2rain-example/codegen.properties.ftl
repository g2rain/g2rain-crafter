# =============================================
# G2Rain Crafter 代码生成配置文件
# =============================================
# 项目配置
# =============================================
# Java 基础包名
# 所有生成的 Java 类将基于此包名创建子包结构
# 示例：com.g2rain.demo 会生成 com.g2rain.demo.controller, com.g2rain.demo.service 等包
archetype.package=${package}
# 数据库连接配置
# =============================================
# 数据库连接 URL
# 格式：jdbc:mysql://主机:端口/数据库名?连接参数
# 支持 MySQL 等数据库
database.url=jdbc:mysql://localhost:3306/${projectName}?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
# 数据库驱动类名
# MySQL 8.0+ 推荐使用 com.mysql.cj.jdbc.Driver
database.driver=com.mysql.cj.jdbc.Driver
# 数据库用户名
# 需要具有读取表结构权限的数据库用户
database.username=root
# 代码生成配置
# =============================================
# 数据库密码
# 对应用户名数据库访问密码
database.password=root123456
# 待生成的数据库表名列表
# 支持多表生成，表名之间用英文逗号分隔
# 表名需与数据库中实际表名完全一致，区分大小写
database.tables=user,product,trade

# 文件覆盖控制
# true - 覆盖已存在的文件
# false - 跳过已存在的文件（推荐用于生产环境）
tables.overwrite=false

# 数据隔离 codegen 配置
# =============================================
# 是否为租户表生成隔离相关代码
# true - 生成 @DataIsolation 及 WithoutIsolation 方法（默认）
# false - 不生成隔离相关代码
# 运行时是否注入租户条件由 application.yml 的 g2rain.data.isolation.enabled 控制
data.isolation.withIsolation=true
# 租户列识别，逗号分隔
# 表含以下任一列时视为租户表，默认 organ_id
data.isolation.tenantColumns=organ_id
# 即使命中租户列也排除的表，逗号分隔
# 示例：dict_type,config（留空表示不排除）
data.isolation.excludeTables=
