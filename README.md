# Spring AI Learn Project

一个基于 Spring AI 1.0 的多模型聊天应用学习项目，集成了多个主流 AI 模型，包括 DeepSeek、通义千问、豆包等，提供了完整的聊天功能、Token 统计、费用计算和聊天记忆等特性。

## 📋 项目概述

本项目是一个 Spring Boot 多模块项目，旨在学习和实践 Spring AI 框架的各种功能。项目采用聚合工程的方式组织，每个子模块对应一个特定的 AI 模型或功能场景。

### 技术栈

- **Spring Boot**: 3.3.6
- **Spring AI**: 1.0.0
- **Java**: 21
- **Maven**: 多模块聚合工程
- **Micrometer**: 指标监控
- **Spring Boot Actuator**: 应用监控

## 🏗️ 项目结构

```
spring-ai-learn/
├── pom.xml                          # 父工程 POM
└── spring-ai-chat/                  # 聊天功能模块
    ├── spring-ai-chat-deepseek/     # DeepSeek 聊天模块
    ├── spring-ai-chat-qwen/         # 通义千问聊天模块
    ├── spring-ai-chat-doubao/       # 豆包聊天模块
    ├── spring-ai-chat-multi/        # 多模型聊天模块
    ├── spring-ai-chat-multi-openai/ # 多 OpenAI 模型聊天模块
    └── spring-ai-chat-ollama/       # Ollama 本地模型聊天模块
```

## 🚀 模块详情

### 1. spring-ai-chat-deepseek (端口: 8081)

**功能特性:**
- DeepSeek 模型集成
- Token 使用统计和费用计算
- Prompt 模板支持
- Metrics 监控
- 聊天记忆功能

**主要接口:**
- `GET /api/deepseek/chatWithPrompt` - 基础聊天
- `GET /api/deepseek/chatWithMetric` - 带 Token 统计的聊天
- `GET /metrics/ai-tokens` - Token 使用统计
- `GET /metrics/ai-cost` - Token 费用计算

**环境变量:**
```bash
DEEPSEEK_API_KEY=your_deepseek_api_key
```

### 2. spring-ai-chat-qwen (端口: 8085)

**功能特性:**
- 通义千问模型集成
- 流式聊天响应
- Token 统计和费用计算
- Metrics 监控

**主要接口:**
- `GET /api/qwen/chat` - 流式聊天
- `GET /api/qwen/chatWithMetric` - 带 Token 统计的聊天
- `GET /metrics/ai-tokens` - Token 使用统计
- `GET /metrics/ai-cost` - Token 费用计算

**环境变量:**
```bash
DASHSCOPE_API_KEY=your_dashscope_api_key
```

### 3. spring-ai-chat-doubao (端口: 8082)

**功能特性:**
- 豆包模型集成（使用 OpenAI 协议）
- 基础聊天功能
- Actuator 监控

**环境变量:**
```bash
DOUBAO_API_KEY=your_doubao_api_key
```

### 4. spring-ai-chat-multi (端口: 8083)

**功能特性:**
- 多模型同时对话
- 支持 DeepSeek + 豆包组合
- 模型对比功能

**环境变量:**
```bash
DEEPSEEK_API_KEY=your_deepseek_api_key
DOUBAO_API_KEY=your_doubao_api_key
```

### 5. spring-ai-chat-multi-openai

**功能特性:**
- 多个 OpenAI 兼容模型集成
- 模型切换和对比

### 6. spring-ai-chat-ollama

**功能特性:**
- 本地 Ollama 模型集成
- 离线 AI 聊天功能

## 🛠️ 快速开始

### 环境要求

- Java 21+
- Maven 3.6+
- 相应的 AI 模型 API Key

### 1. 克隆项目

```bash
git clone <repository-url>
cd spring-ai-learn
```

### 2. 配置环境变量

创建环境变量或在 IDE 中配置：

```bash
# DeepSeek
export DEEPSEEK_API_KEY=your_deepseek_api_key

# 通义千问
export DASHSCOPE_API_KEY=your_dashscope_api_key

# 豆包
export DOUBAO_API_KEY=your_doubao_api_key
```

### 3. 编译项目

```bash
mvn clean compile
```

### 4. 运行特定模块

```bash
# 运行 DeepSeek 模块
cd spring-ai-chat/spring-ai-chat-deepseek
mvn spring-boot:run

# 运行通义千问模块
cd spring-ai-chat/spring-ai-chat-qwen
mvn spring-boot:run
```

## 📊 监控和指标

所有模块都集成了 Spring Boot Actuator，提供以下监控端点：

- **健康检查**: `http://localhost:{port}/actuator/health`
- **指标监控**: `http://localhost:{port}/actuator/metrics`
- **所有端点**: `http://localhost:{port}/actuator`

### Token 统计指标

- `ai.prompt.tokens` - 输入 Token 数量
- `ai.completion.tokens` - 输出 Token 数量
- `ai.total.tokens` - 总 Token 数量
- `ai.qwen.prompt.tokens` - 通义千问输入 Token
- `ai.qwen.completion.tokens` - 通义千问输出 Token
- `ai.qwen.total.tokens` - 通义千问总 Token

## 🧪 API 测试

每个模块都提供了 `api-test.http` 文件，包含完整的 API 测试用例：

- `spring-ai-chat-deepseek/src/test/resources/api-test.http`
- `spring-ai-chat-qwen/src/test/resources/api-test.http`

使用 IntelliJ IDEA 或 VS Code 的 REST Client 插件可以直接运行这些测试。

## 💰 费用计算

项目提供了基于官方定价的 Token 费用计算功能：

### DeepSeek 定价
- 输入 Token: 1.33 元/1M tokens
- 输出 Token: 2.66 元/1M tokens

### 通义千问定价（参考）
- 输入 Token: 0.50 元/1M tokens
- 输出 Token: 1.50 元/1M tokens

**注意**: 实际费用以官方账单为准。

## 🔧 核心组件

### Advisor 组件

- **SimpleMetricAdvisor**: Token 统计
- **SimpleLoggerAdvisor**: 日志记录（Spring AI 内置）

### 服务组件

- **TokenCostService**: Token 费用计算服务
- **PromptTemplateService**: Prompt 模板服务（DeepSeek模块）
- **MetricsController**: 指标查询控制器



## 📝 开发说明

### 添加新的 AI 模型

1. 在 `spring-ai-chat` 下创建新的子模块
2. 配置相应的 AI 模型依赖
3. 实现 ChatClient 配置
4. 添加控制器和服务类
5. 配置 Advisor 和监控

### 自定义 Advisor

```java
@Component
public class CustomAdvisor implements CallAdvisor {
    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        // 前置处理
        ChatClientResponse response = chain.nextCall(request);
        // 后置处理
        return response;
    }
}
```

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 🙏 致谢

- [Spring AI](https://spring.io/projects/spring-ai) - 强大的 AI 集成框架
- [DeepSeek](https://www.deepseek.com/) - 优秀的 AI 模型服务
- [阿里云通义千问](https://tongyi.aliyun.com/) - 智能对话服务
- [字节跳动豆包](https://www.volcengine.com/) - AI 模型服务

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 提交 Issue
- 发送邮件: cappuccinosgj@gmail.com
- 创建 Discussion

---

**Happy Coding! 🎉**
