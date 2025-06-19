# Spring AI Chat In-Memory

基于 Spring AI 1.0 的内存聊天记忆功能模块，提供完整的聊天记忆管理、多会话支持、Token 统计和费用计算功能。

## 🚀 功能特性

- **聊天记忆功能**: 支持多会话的聊天历史记录管理
- **流式响应**: 支持流式和非流式聊天响应
- **多会话管理**: 支持同时管理多个独立的聊天会话
- **Token 统计**: 实时统计 Prompt、Completion 和 Total Token 使用量
- **费用计算**: 基于通义千问定价的费用估算
- **监控集成**: 集成 Micrometer 和 Actuator 监控
- **RESTful API**: 提供完整的 REST API 接口

## 📚 参考文档

- [Spring AI 官方文档 - 聊天记忆](https://docs.spring.io/spring-ai/reference/api/chat-memory.html#_memory_in_chat_model)

## 🏗️ 项目结构

```
src/main/java/com/jinnian/ai/in/memory/
├── InMemoryApplication.java          # 主启动类
├── advisors/                          # Advisor 组件
│   ├── ChatMemoryAdvisor.java        # 聊天记忆 Advisor
│   └── SimpleMetricAdvisor.java      # Token 统计 Advisor
├── configs/                           # 配置类
│   └── InMemoryConfigs.java          # 主配置类
├── controller/                        # 控制器
│   ├── InMemoryChatController.java   # 聊天控制器
│   └── MetricsController.java        # 监控控制器
└── service/                           # 服务类
    ├── ChatMemoryService.java        # 聊天记忆服务
    └── TokenCostService.java         # Token 费用服务
```

## 🔧 环境配置

### 环境变量

```bash
# 通义千问 API Key
DASHSCOPE_API_KEY=your_dashscope_api_key
```

### 应用配置

```properties
# 应用基本配置
spring.application.name=spring-ai-chat-in-memory
server.port=8086
spring.profiles.active=memory

# 通义千问模型配置
spring.ai.openai.api-key=${DASHSCOPE_API_KEY}
spring.ai.openai.base-url=https://dashscope.aliyuncs.com/compatible-mode
spring.ai.openai.chat.completions-path=/v1/chat/completions
spring.ai.openai.chat.options.model=qwen-plus-2025-04-28
spring.ai.openai.chat.options.temperature=0.7

# Actuator 监控配置
management.endpoints.web.exposure.include=*
management.endpoints.web.base-path=/actuator

# 日志配置
logging.level.org.springframework.ai.chat.client.advisor=DEBUG
```

## 📡 API 接口

### 聊天接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/memory/chat` | GET | 基础聊天（无记忆） |
| `/api/memory/chatStream` | GET | 流式聊天（无记忆） |
| `/api/memory/chatWithMemory` | GET | 带记忆的聊天 |
| `/api/memory/chatWithMemoryAndMetric` | GET | 带记忆和Token统计的聊天 |

### 监控接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/metrics/ai-tokens` | GET | 获取AI Token使用统计 |
| `/metrics/ai-cost` | GET | 获取AI Token费用统计 |
| `/actuator/metrics` | GET | 获取所有监控指标 |
| `/actuator/health` | GET | 获取应用健康状态 |

## 🎯 使用示例

### 1. 基础聊天（无记忆）

```http
GET http://localhost:8086/api/memory/chat?userInput=你好，请问你是谁？
```

### 2. 带记忆的聊天

```http
# 第一次对话（默认会话ID：007）
GET http://localhost:8086/api/memory/chatWithMemory?userInput=我的名字是张三

# 第二次对话（AI会记住之前的对话）
GET http://localhost:8086/api/memory/chatWithMemory?userInput=你还记得我的名字吗？&conversationId=007
```

### 3. 获取Token统计

```http
GET http://localhost:8086/metrics/ai-tokens
```

响应示例：
```json
{
  "promptTokens": 150.0,
  "completionTokens": 200.0,
  "totalTokens": 350.0
}
```

### 4. 获取费用统计

```http
GET http://localhost:8086/metrics/ai-cost
```

响应示例：
```json
{
  "tokenUsage": {
    "promptTokens": 150,
    "completionTokens": 200,
    "totalTokens": 350
  },
  "cost": {
    "promptCost": 0.000075,
    "completionCost": 0.0003,
    "totalCost": 0.000375,
    "currency": "CNY"
  },
  "pricing": {
    "promptTokenPrice": "0.50 CNY/1M tokens",
    "completionTokenPrice": "1.50 CNY/1M tokens",
    "note": "价格基于通义千问参考定价，实际费用以阿里云官方账单为准"
  },
  "timestamp": 1703123456789
}
```

## 🔍 核心组件

### MessageWindowChatMemory

Spring AI 内置的聊天记忆实现，负责管理聊天历史记录：

- **消息窗口管理**: 维护指定最大大小的消息窗口（默认20条）
- **自动清理**: 当消息数超过最大值时，自动删除较旧的消息
- **系统消息保留**: 在清理过程中保留系统消息
- **多会话支持**: 通过conversationId支持多个独立的聊天会话

### MessageChatMemoryAdvisor

Spring AI 内置的聊天记忆Advisor：

- **自动记忆管理**: 自动将用户消息和AI回复添加到聊天历史
- **上下文注入**: 在每次对话时自动注入历史上下文
- **会话隔离**: 基于conversationId实现会话隔离

### SimpleMetricAdvisor

Token 统计 Advisor，负责统计 Token 使用情况：

- **实时统计**: 实时统计每次对话的 Token 使用量
- **分类统计**: 分别统计 Prompt、Completion 和 Total Token
- **Micrometer 集成**: 集成 Micrometer 监控框架

### TokenCostService

Token 费用计算服务：

- **费用计算**: 基于通义千问定价计算 Token 费用
- **统计报告**: 提供详细的费用统计报告
- **实时监控**: 实时获取 Token 使用统计

## 🚀 快速开始

### 1. 环境准备

```bash
# 设置环境变量
export DASHSCOPE_API_KEY=your_dashscope_api_key
```

### 2. 启动应用

```bash
# 编译项目
mvn clean compile

# 启动应用
mvn spring-boot:run
```

### 3. 测试接口

使用提供的 `api-test.http` 文件测试各个接口功能。

## 📊 监控指标

### Token 统计指标

- `ai.memory.prompt.tokens` - 输入 Token 数量
- `ai.memory.completion.tokens` - 输出 Token 数量
- `ai.memory.total.tokens` - 总 Token 数量

### Actuator 端点

- `/actuator/health` - 应用健康状态
- `/actuator/metrics` - 所有监控指标
- `/actuator/metrics/{metricName}` - 特定指标详情

## 🔧 配置说明

### 聊天记忆配置

- **默认历史记录大小**: 20 条消息（可在 MessageWindowChatMemory 中配置）
- **会话管理**: 支持通过conversationId管理多个并发会话
- **内存管理**: 自动限制历史记录大小，防止内存溢出
- **系统消息保护**: 清理历史记录时保留系统消息

### Token 费用配置

- **Prompt Token 价格**: 0.50 元/1M tokens
- **Completion Token 价格**: 1.50 元/1M tokens
- **价格更新**: 可在 TokenCostService 中修改定价

## 🤝 贡献指南

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](../../LICENSE) 文件了解详情。

## 📞 联系方式

- 项目地址: [https://github.com/your-username/spring-ai-learn](https://github.com/your-username/spring-ai-learn)
- 问题反馈: [Issues](https://github.com/your-username/spring-ai-learn/issues)

---

**注意**: 本项目仅用于学习和演示目的，生产环境使用请根据实际需求进行相应的安全和性能优化。