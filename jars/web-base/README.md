# web-base

是统一基础操作的使用，以及基于旧的web-base的一些功能重写，也瘦身这个jar的依赖。目前依赖主要是hutool工具。 具体依赖：`hutool-all`、`lombok`以及`swagger-annotations`。

### 1、通用实体类统一维护

对于之前有项目`code-generator`生成的项目中会带有相关实体类，将移`web-base`中来维护。

- `BaseEnum` 枚举维护接口
- `IsDel` 删除标志
- `ResultMessage` 系统通用错误码维护
- `BusinessException` 业务异常处理
- `BaseEntity` 数据库实体类基类（`id`、`createBy`、`createTime`、`updateBy`、`updateTime`和`isDel`）字段。
- `QueryPage` 查询分页请求类
- `PagedDate`和`PagedInfo` 分页信息
- `Result` feign或controller返回结果结构信息

> 注：PageHelper不在web-base中维护，因为该类需要依赖myabtis-plus包所以放弃在工具包中维护。由项目`code-generator`生成。
>

##### 1.1、变动

有些操作写法发生变化需要注意

- `BaseEntity`中isDel改成int，逻辑操作可以根据`EntityHelper`使用枚举操作，并对方法名称重新命名。
    ```java
    //创建数据
    EntityHelper.fillDateCreate(entity,operatorId);
    //删除数据
    EntityHelper.fillDateDel(entity,operatorId);
    //更新数据
    EntityHelper.fillDataUpdate(entity,operatorId);
    ```

- `ResultMessage`将不在提供业务相关错误码维护，只维护系统相关或者常用业务错误码维护，即不会在项目`code-generator`生成。如果业务项目上需要维护可以自行实现`BaseEnum<Integer>`接口即可.
    ```java
    //先创建业务错误码维护类
    public enum BusinessMessage  implements BaseEnum<Integer> {
        BUS_ERROR(199, "****业务出错"),
        ;
        // 构造方法省略
        private final Integer code;
        private final String message;
        //of方法省略
    }
    //业务错误校验可以直接抛出
    if(Objects.isNull(Integer)){
        throw new BussinessException(BusinessMessage.BUS_ERROR);
    }
    ```
- `Operation` 维护操作者id跟名称，简化之前`BaseMode`，`BaseMode`中依赖没用的字段比如创建时间、更新时间等。只维护与操作者相关内容。
- `QueryPage` 是不携带操作者信息分页请求类
- `OpQueryPage` 是携带操作者信息分页请求类

### 2、通用工具扩展统一维护

是基于hutool工具功能上进行补充，如果hutool提供相关功能那么就不需要自己重新在写对应类或者方法来覆盖。比如DateHelper是基于DateUtil基础上扩展而不是重写。

- AssertHelper 断言扩展，是基于hutool的Assert类，主要是处理项目容易对数据校验并抛出BusinessException内容提供一些简单方法。 自身提供方法必须以`assert***`
  开头,比如`assertNotNull`等。
    ```java
    //业务错误校验可以直接抛出
    if(Objects.isNull(Object)){
        throw new BussinessException(BusinessMessage.BUS_ERROR);
    }
    //替换
    AssertHelper.assertNotNull(Object,BusinessMessage.BUS_ERROR);
    //如果没有提供业务错误说明，可以使用默认
    AssertHelper.assertNotNull(Object);
    ```

- `BeanHelper` 是基于hutool的bean复制,补加一个对集合复制
- `DateHelper`和`LocalDateTimeHelper`分别是基于hutool的`DateUtil`和`LocalDateTimeHelper`提供一些周期型方法操作
- `DateConstantHelper`是基于hutool的`DatePattern`时间格式提供通配符替换，`DatePattern`已经提供很多时间格式定义
- `EntityHelper`是提供对数据库数据的create和update信息提供一些方法。