# 统一数据查询接口

该项目是一个统一数据查询接口，支持多种数据源，支持多种数据查询方式，支持多种数据查询结果格式，支持多种数据查询结果缓存。

## 用法

```shell
curl -X GET http://localhost:9090/api/v1/query?selectId=xxxx&k1=v1&k2=v2&_sign=xxxx
```

- `selectId` 这是必须的参数，用来表示查询ID，后面会详细描述
- `k1=v1&k2=v2` 表示该查询需要的参数，参数名和参数值都必须是 `urlencode` 编码的
- `_sign=xxxx` 表示该查询的签名，后面详细描述签名