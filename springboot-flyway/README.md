# springboot-flyway

## 示例说明

+ 修改 `application.yaml` 数据库地址.
+ 启动后访问 [http://localhost:8080/user/info/1](http://localhost:8080/user/info/1)

> 为避免迁移脚本冲突，如果不建多个数据库脚本的情况为，请先清空数据库里的内容

## Flyway 的工作原理

flyway 需要在 DB 中先创建一个 metdata 表 (缺省表名为 flyway_schema_history), 在该表中保存着每次 migration 的记录, 记录包含 migration 脚本的版本号和 SQL 脚本的 checksum 值. 当一个新的 SQL 脚本被扫描到后, Flyway 解析该 SQL 脚本的版本号, 并和 metadata 表已 apply 的的 migration 对比, 如果该 SQL 脚本版本更新的话, 将在指定的 DB 上执行该 SQL 文件, 否则跳过该 SQL 文件.

## 版本号大小说明

两个 flyway 版本号的比较, 采用左对齐原则, 缺位用 0 代替. 举例如下: 

+ 1.2.9.4 比 1.2.9   版本高
+ 1.2.10  比 1.2.9.4 版本高
+ 1.2.10  和 1.2.010 版本号一样高, 每个版本号部分的前导 0 会被忽略

## 脚本分类
Flyway SQL 文件可以分为两类:
+ Versioned 
+ Repeatable

### Versioned

Versioned migration 用于版本升级, 每个版本有唯一的版本号并只能 apply 一次

### Repeatable

Repeatable migration 是指可重复加载的 migration. 一旦 SQL 脚本的 checksum 有变动, flyway 就会重新应用该脚本. 它并不用于版本更新, 这类的 migration 总是在 versioned migration 执行之后才被执行.

### Migration SQL的命名规则图

![](https://img2018.cnblogs.com/blog/194640/201809/194640-20180917181704995-1479103124.png)

其中的文件名由以下部分组成，除了使用默认配置外，某些部分还可自定义规则.

+ prefix: 可配置，前缀标识，默认值 V 表示 Versioned, R 表示 Repeatable
+ version: 标识版本号, 由一个或多个数字构成, 数字之间的分隔符可用点.或下划线_
+ separator: 可配置, 用于分隔版本标识与描述信息, 默认为两个下划线 '__'
+ description: 描述信息, 文字之间可以用下划线或空格分隔
+ suffix: 可配置, 后续标识, 默认为.sql

### 关于开发|生产环境版本说明

1. 开发环境 SQL 文件建议采用时间戳作为版本号. 
多人一起开发不会导致版本号争用, 同时再加上生产环境的版本号, 这样的话, 将来手工 merge 成生产环境 V1.2d migration 脚本也比较方便, SQL 文件示例:
V20200317.1059__V1.0_Unique_User_Names.sql
V20180317.1459__V1.0_Add_SomeTables.sql

2. 生产环境 SQL 文件, 应该是手动 merge 开发环境的 SQL 脚本, 版本号按照正常的版本, 比如 V2.1.5_001__Release.sql