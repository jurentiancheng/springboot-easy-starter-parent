# springboot-easy-starter-parent

本项目为一个基于 Spring Boot 的多模块 Maven 父项目，旨在为企业级应用开发提供统一、易用的基础依赖和常用功能组件，提升开发效率，规范项目结构。
基于Spring 官方 2.3.3.RELEASE 版本，兼容SprintCloud Hoxton.SR5 版本生态的组建。
### 1.官方Spring Boot 父依赖版本及 兼容SpringCloud Hoxton.SR5 版本兼容组建
     <parent>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-parent</artifactId>
          <version>2.3.3.RELEASE</version>
          <relativePath /> <!-- lookup parent from repository -->
     </parent>
<properties>
     <spring-cloud.version>Hoxton.SR5</spring-cloud.version>
<properties>

### 2.快速创建SprinBoot 微服务
     <parent>
          <groupId>com.easyapi</groupId>
          <artifactId>easy-starter-parent</artifactId>
          <version>1.1.1-SNOWFLAKEID-RELEASE</version>
          <relativePath /> <!-- lookup parent from repository -->
     </parent>

## 项目结构与模块说明

```
springboot-easy-starter-parent/
├── easy-starter-parent/         # Maven父依赖管理
├── jars/                       # 通用基础功能JAR包
│   ├── excel/                  # Excel解析与导出
│   ├── jdbc/                   # JDBC SQL生成与类型封装
│   ├── supre-rule/             # QLExpress表达式引擎扩展
│   ├── web-annotations/        # 通用AOP注解与切面
│   ├── web-base/               # 统一基础实体、异常、工具
│   ├── web-base-feign/         # Feign相关扩展
│   ├── web-base-mbp/           # MyBatis-Plus相关基础封装
├── starters/                   # Spring Boot Starter扩展
│   ├── kafka-spring-boot-starter/ # Kafka快速集成Starter
│   ├── minio-spring-boot-starter/ # Minio对象存储Starter
```

### 1. easy-starter-parent
- 统一管理所有子模块的依赖版本，简化依赖冲突。

### 2. jars/excel
- 基于 Alibaba EasyExcel，提供 Excel 文件的解析、导出等服务。
- 支持自定义表头、内容处理，适合大批量数据导入导出场景。

### 3. jars/jdbc
- 提供 SQL 语句生成、字段类型封装等能力，简化数据库操作。
- 通过 `ISql` 接口和 `SqlMakerService` 实现 SQL 动态拼装。

### 4. jars/supre-rule
- 基于 QLExpress 的表达式引擎扩展，支持自定义宏、操作符、服务方法绑定。
- 适用于规则引擎、动态脚本计算等场景。
- 典型用法：可通过表达式实现 HTTP 请求、日期转换、数学运算等。

### 5. jars/web-annotations
- 提供如 `@SystemLog` 等通用注解及AOP切面，便于统一日志、埋点等横切关注点处理。

### 6. jars/web-base
- 统一基础实体（如 `BaseEntity`、`QueryPage`）、异常处理（如 `BusinessException`）、常用工具类（如 `DateHelper`、`AssertHelper`）。
- 依赖 `hutool-all`、`lombok`、`swagger-annotations` 等常用工具包。

### 7. jars/web-base-feign
- 扩展 Feign 相关功能，提供参数编码、日期处理等工具。

### 8. jars/web-base-mbp
- 针对 MyBatis-Plus 的基础封装，提供通用实体、辅助工具等。

### 9. starters/kafka-spring-boot-starter
- 快速集成 Kafka，提供消息消费、重试、作业模板等能力。
- 依赖 `spring-boot-starter`、`kafka-clients`、`web-base` 等。

### 10. starters/minio-spring-boot-starter
- 快速集成 Minio 对象存储，提供文件上传、下载等服务。
- 依赖 `spring-boot-starter`、`minio` 等。

## 依赖管理

- 推荐使用 `easy-starter-parent` 作为父POM，自动继承所有依赖版本。
- 各功能模块可单独引入，按需依赖。

## 适用场景

- 适合企业级微服务项目，需统一基础能力、提升开发效率的场景。
- 支持灵活扩展，便于二次开发和定制。

## 贡献与反馈

- 欢迎 issue、PR 反馈和贡献代码。

--