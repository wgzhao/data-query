# 统一数据查询接口

该项目是一个统一数据查询接口，支持多种数据源，支持多种数据查询方式，支持多种数据查询结果格式，支持多种数据查询结果缓存。

前端管理界面代码在[data-query-ui](https://github.com/wgzhao/data-query-ui)

## 用法

```shell
curl -X GET http://localhost:9090/api/v1/query?selectId=xxxx&k1=v1&k2=v2&_sign=xxxx&_appId=xxxx
```

-   `selectId` 查询ID， 这是必须的参数
-   `k1=v1&k2=v2` 查询参数，表示该查询需要的参数，参数名和参数值都必须是 `urlencode` 编码的
-   `_appId=xxx` 应用ID，你所获取的查询授权 ID
-   `_sign=xxxx` 查询签名，表示该查询的签名，后面详细描述签名

最新版本已经加入了 `POST` 方法，目的是为了传递的参数值能表类型，其用法如下：

```shell
curl -X POST http://localhost:9090/api/v1/query -d '{"selectId":"xxxx","k1":"v1","k2":"v2","_appId":"xxxx","_sign":"xxxx"}'
```

下面描述

### 查询ID

查询 ID，既 `selectId` 用来表示需要进行数据查询的编号，该编号一般有数据开发部门拟定并给出其背后具体的查询逻辑。

### 查询参数

查询参数是本次查询你替换替换实际查询 SQL 中的变量值

### 应用 ID

在发起查询前，你需要先获得查询的授权信息，也就是 `appId` 以及 `appKey`，这授权信息可以从架构运维部获得，前者是一个 16 位的字符串，后者是 32 位字符串， 假定你或的授权信息如下：

-   `appId`: `72f392f2e6a90e9a`
-   `appKey`: `70162f804fd30a7179b8ebded08e8dda`

注： 该授权也是测试环境的通用授权信息，测试环境下，直接可使用该授权，不需要额外申请。 只有生产环境才需要。

### 查询签名

为了确定查询发起者合法有效，特对签名进行校验，

签名的实现方式为：

1. 将所有的查询参数按照参数名的字典序排序，拼接成一个字符串，比如：查询参数为 `n1=v1&m2=v2&selectId=q1`，排序后的字符串为 `m2=v2&n1=vv1&selectId=q1`，假定为字符串 `str1`
2. 将分配给应用的 `appId` 以及 `appKey` 直接拼接在 `str1` 后面，变成 `m2=v2&n1=v1&selectId=q172f392f2e6a90e9a70162f804fd30a7179b8ebded08e8dda`，假定为字符串 `str2`
3. 对 `str2` 进行 `md5` 签名，得到签名字符串 `0fe35a85735c112dae68ced204850fe4`，这便是该查询的签名

如果是 Java 编程的话，实现逻辑如下：

```java
public class TestSign {

    private final String appId = "72f392f2e6a90e9a";
    private final String appKey = "70162f804fd30a7179b8ebded08e8dda";

    private final String validSign = "0fe35a85735c112dae68ced204850fe4";
    private final String sortedParams = "m2=v2&n1=v1&selectId=q172f392f2e6a90e9a70162f804fd30a7179b8ebded08e8dda";

    @Test
    public void testSign()
    {
        Map<String, String> queryParam = new HashMap<>();
        queryParam.put("n1","v1");
        queryParam.put("m2","v2");
        queryParam.put("selectId","q1");
        StringJoiner joiner = new StringJoiner("&");
        queryParam.keySet().stream().sorted().forEach(key -> joiner.add(key + "=" + queryParam.get(key)));
        String str1 = joiner + appId + appKey;
        Assert.assertEquals(str1, sortedParams);
        String _sign= DigestUtils.md5DigestAsHex(str1.getBytes(StandardCharsets.UTF_8));
        Assert.assertEquals(_sign, validSign);
    }
}
```

如果是 Python 语言，实现逻辑如下：

```python
def test_sign():

	appId = "72f392f2e6a90e9a"
	appKey = "70162f804fd30a7179b8ebded08e8dda"
	validSign = "0fe35a85735c112dae68ced204850fe4"
	sortedParams = "m2=v2&n1=v1&selectId=q172f392f2e6a90e9a70162f804fd30a7179b8ebded08e8dda"

	# passwed params
	params = {'n1': 'v1', 'm2': 'v2', 'selectId': 'q1'}
	# sorted params
	sparmas = '&'.join([x + '=' + params[x] for x in sorted(params.keys(), key=lambda x: x.lower())]) + appId + appKey

	assert sparmas == sortedParams

	sign = hashlib.md5(sparmas.encode('utf-8')).hexdigest()

	assert sign == validSign
```

输出结果类似如下：

```json
{
    "message": "success",
    "total": 9,
    "data":  [
            {
                "Table": "c_broker_client_relations"
            },
            {
                "Table": "c_financial_manager_clien_relations"
            },
            {
                "Table": "tbl_test"
            },
            {
                "Table": "tmp"
            },
            {
                "Table": "tmp1"
            },
            {
                "Table": "tmp2"
            },
            {
                "Table": "tmp3"
            },
            {
                "Table": "tmp4"
            },
            {
                "Table": "tmpx"
            }
        ],
    "success": true
}
```

测试环境可以使用上文中提到的 `appId` 以及 `appKey` 而不需要额外申请。
