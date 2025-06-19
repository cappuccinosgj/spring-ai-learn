//package com.jinnian.ai.in.memory.service;
//
//import org.springframework.ai.chat.messages.Message;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * @ClassName ChatMemoryService
// * @Description 聊天记忆服务
// * @Author cappuccinosgj
// * @Date 2025/6/18
// */
//@Service
//public class ChatMemoryService {
//
//    private final Map<String, ChatMemoryAdvisor> advisors = new ConcurrentHashMap<>();
//    private final int defaultMaxHistorySize = 10;
//
//    /**
//     * 获取或创建ChatMemoryAdvisor
//     * @param sessionId 会话ID
//     * @return ChatMemoryAdvisor实例
//     */
//    public ChatMemoryAdvisor getOrCreateAdvisor(String sessionId) {
//        return advisors.computeIfAbsent(sessionId,
//            id -> new ChatMemoryAdvisor(id, defaultMaxHistorySize));
//    }
//
//    /**
//     * 获取或创建ChatMemoryAdvisor（自定义历史记录大小）
//     * @param sessionId 会话ID
//     * @param maxHistorySize 最大历史记录数
//     * @return ChatMemoryAdvisor实例
//     */
//    public ChatMemoryAdvisor getOrCreateAdvisor(String sessionId, int maxHistorySize) {
//        return advisors.computeIfAbsent(sessionId,
//            id -> new ChatMemoryAdvisor(id, maxHistorySize));
//    }
//
//    /**
//     * 获取指定会话的聊天历史
//     * @param sessionId 会话ID
//     * @return 聊天历史
//     */
//    public List<Message> getChatHistory(String sessionId) {
//        ChatMemoryAdvisor advisor = advisors.get(sessionId);
//        if (advisor != null) {
//            return advisor.getChatHistory(sessionId);
//        }
//        return new ArrayList<>();
//    }
//
//    /**
//     * 清除指定会话的聊天历史
//     * @param sessionId 会话ID
//     */
//    public void clearChatHistory(String sessionId) {
//        ChatMemoryAdvisor advisor = advisors.get(sessionId);
//        if (advisor != null) {
//            advisor.clearChatHistory(sessionId);
//        }
//        advisors.remove(sessionId);
//    }
//
//    /**
//     * 清除所有会话的聊天历史
//     */
//    public void clearAllChatHistory() {
//        advisors.values().forEach(ChatMemoryAdvisor::clearAllChatHistory);
//        advisors.clear();
//    }
//
//    /**
//     * 获取所有会话ID
//     * @return 会话ID列表
//     */
//    public Set<String> getAllSessionIds() {
//        return new HashSet<>(advisors.keySet());
//    }
//
//    /**
//     * 获取会话统计信息
//     * @return 统计信息
//     */
//    public Map<String, Object> getSessionStats() {
//        Map<String, Object> stats = new HashMap<>();
//        stats.put("totalSessions", advisors.size());
//        stats.put("sessionIds", getAllSessionIds());
//
//        Map<String, Integer> sessionHistoryCounts = new HashMap<>();
//        advisors.forEach((sessionId, advisor) -> {
//            List<Message> history = advisor.getChatHistory(sessionId);
//            sessionHistoryCounts.put(sessionId, history.size());
//        });
//        stats.put("sessionHistoryCounts", sessionHistoryCounts);
//
//        return stats;
//    }
//}