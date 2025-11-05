package kr.najoan.ai

/**
 * 사용자의 대화 세션을 관리합니다.
 * 각 사용자마다 독립적인 대화 히스토리를 유지합니다.
 */
data class UserSession(
    val userId: String,
    val conversationHistory: MutableList<ConversationTurn> = mutableListOf()
) {
    /**
     * 대화 턴을 추가합니다.
     */
    fun addTurn(userMessage: String, aiResponse: String) {
        conversationHistory.add(
            ConversationTurn(
                userMessage = userMessage,
                aiResponse = aiResponse
            )
        )
    }

    /**
     * 대화 히스토리를 초기화합니다.
     */
    fun reset() {
        conversationHistory.clear()
    }

    /**
     * 대화 히스토리를 문자열로 변환합니다.
     * AI에게 컨텍스트로 전달할 때 사용합니다.
     */
    fun getContextString(): String {
        return conversationHistory.joinToString("\n") { turn ->
            "사용자: ${turn.userMessage}\n아이짱: ${turn.aiResponse}"
        }
    }
}

/**
 * 단일 대화 턴을 나타냅니다.
 */
data class ConversationTurn(
    val userMessage: String,
    val aiResponse: String
)

/**
 * 모든 사용자의 세션을 관리합니다.
 */
object SessionManager {
    private val sessions = mutableMapOf<String, UserSession>()

    /**
     * 사용자의 세션을 가져옵니다.
     * 없으면 새로 생성합니다.
     */
    fun getOrCreateSession(userId: String): UserSession {
        return sessions.getOrPut(userId) {
            UserSession(userId = userId)
        }
    }

    /**
     * 사용자의 세션을 초기화합니다.
     */
    fun resetSession(userId: String) {
        sessions[userId]?.reset()
    }

    /**
     * 사용자의 세션을 삭제합니다.
     */
    fun deleteSession(userId: String) {
        sessions.remove(userId)
    }

    /**
     * 모든 세션을 초기화합니다. (관리용)
     */
    fun clearAllSessions() {
        sessions.clear()
    }

    /**
     * 활성 세션의 개수를 반환합니다.
     */
    fun getActiveSessions(): Int {
        return sessions.size
    }
}
