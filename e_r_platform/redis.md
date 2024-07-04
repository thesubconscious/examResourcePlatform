Redis 是一个高性能的键值对（key-value）存储系统，常被用来做缓存、会话存储、消息队列等。Spring Data Redis 提供了简洁的方法以便与 Redis 进行交互。以下是一些常用的方法及其应用：

### 1. opsForValue() 常用的方法

`RedisTemplate` 提供了多种操作接口，包括 `opsForValue()`、`opsForHash()`、`opsForList()` 等。`opsForValue()` 对应的是 Redis 的字符串操作。

```java
// 设置值
opsForValue().set("key", "value");

// 获取值
String value = opsForValue().get("key");

// 设置值并指定过期时间（参数分别为：键，值，过期时间，时间单位）
opsForValue().set("key", "value", 10, TimeUnit.MINUTES);

// 如果键不存在则设置值（SETNX 命令）
opsForValue().setIfAbsent("key", "value");

// 获取并设置值（返回旧值）
String oldValue = opsForValue().getAndSet("key", "new_value");

// 增加值（对于数值类型，INCR 命令）
Long incrementedValue = opsForValue().increment("numeric_key", 1);
```

### 2. 删除缓存

`delete` 方法用于删除指定的键：

```java
redisTemplate.delete("key");
```

### 3. 其他常用操作

#### 操作 Hash

```java
// 设置 Hash 值
opsForHash().put("hash_key", "field", "value");

// 获取 Hash 值
String fieldValue = opsForHash().get("hash_key", "field");

// 获取 Hash 所有字段和值
Map<Object, Object> allFields = opsForHash().entries("hash_key");

// 删除 Hash 字段
opsForHash().delete("hash_key", "field");
```

#### 操作 List

```java
// 从左边插入值
opsForList().leftPush("list_key", "value");

// 从右边插入值
opsForList().rightPush("list_key", "value");

// 移除并从左边获取列表中的第一个元素
String value = opsForList().leftPop("list_key");

// 获取列表中的所有元素
List<String> allValues = opsForList().range("list_key", 0, -1);
```

#### 操作 Set

```java
// 添加值到 Set 集合
opsForSet().add("set_key", "value1", "value2");

// 判断值是否在 Set 中
boolean isMember = opsForSet().isMember("set_key", "value1");

// 获取 Set 集合中的所有值
Set<String> members = opsForSet().members("set_key");

// 移除 Set 集合中的某个值
opsForSet().remove("set_key", "value1");
```

#### 操作 ZSet（有序集合）

```java
// 添加元素到有序集合
opsForZSet().add("zset_key", "value", 1.0);

// 获取有序集合中的元素数量
Long size = opsForZSet().zCard("zset_key");

// 获取有序集合中的所有元素
Set<String> allValues = opsForZSet().range("zset_key", 0, -1);

// 根据分数范围获取元素
Set<String> valuesByScore = opsForZSet().rangeByScore("zset_key", 0, 10);

// 删除有序集合中的元素
opsForZSet().remove("zset_key", "value");
```

### 4. 设置过期时间

```java
// 设置过期时间（键，时间，时间单位）
redisTemplate.expire("key", 10, TimeUnit.MINUTES);

// 获取剩余过期时间
Long remainTime = redisTemplate.getExpire("key");
```

### 5. 批量操作

```java
// 批量删除
redisTemplate.delete(Arrays.asList("key1", "key2", "key3"));

// 批量获取值
List<String> values = redisTemplate.opsForValue().multiGet(Arrays.asList("key1", "key2", "key3"));
```

### 6. 事务和管道支持

RedisTemplate 支持事务和管道，可用于批量操作，提高效率。

```java
// 事务示例
redisTemplate.execute(new SessionCallback<Object>() {
    @Override
    public Object execute(RedisOperations operations) throws DataAccessException {
        operations.multi(); // 开启事务
        operations.opsForValue().set("key1", "value1");
        operations.opsForValue().set("key2", "value2");
        return operations.exec(); // 提交事务
    }
});

// 管道示例
List<Object> results = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
    connection.set("key1".getBytes(), "value1".getBytes());
    connection.set("key2".getBytes(), "value2".getBytes());
    return null;
});
```

### 总结

- `opsForValue()` 用于字符串操作，常用方法包括 `set`、`get`、`increment` 等。
- `opsForHash()` 用于操作哈希表。
- `opsForList()` 用于操作列表。
- `opsForSet()` 用于操作集合。
- `opsForZSet()` 用于操作有序集合。
- `delete` 用于删除缓存。
- 可以设置键的过期时间和进行批量操作。
- 支持事务和管道，适用于需要保证原子性的批量操作。

通过合理地使用上述方法，您可以更加高效地管理和操作Redis缓存。