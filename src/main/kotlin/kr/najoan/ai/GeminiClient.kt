package kr.najoan.ai

import com.google.genai.Client
import com.google.genai.types.Content
import com.google.genai.types.GenerateContentConfig
import com.google.genai.types.GoogleSearch
import com.google.genai.types.HarmBlockThreshold
import com.google.genai.types.HarmCategory
import com.google.genai.types.Part
import com.google.genai.types.SafetySetting
import com.google.genai.types.ThinkingConfig
import com.google.genai.types.Tool
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("GeminiClient")

object GeminiClient {
    private lateinit var client: Client
    private const val MODEL_NAME = "gemini-2.5-flash"

    fun initialize(apiKey: String) {
        client = Client.builder()
            .apiKey(apiKey)
            .build()
    }

    /**
     * AI에게 메시지를 보내고 응답을 받습니다.
     * @param userMessage 사용자 메시지
     * @param systemPrompt 시스템 프롬프트 (AI의 성격/행동 정의)
     * @param conversationContext 이전 대화 히스토리 (선택사항)
     * @return AI의 응답
     */
    fun generateResponse(
        userMessage: String,
        systemPrompt: String,
        conversationContext: String = ""
    ): String {
        return try {
            val safetySettings = listOf(
                SafetySetting.builder()
                    .category(HarmCategory.Known.HARM_CATEGORY_HARASSMENT)
                    .threshold(HarmBlockThreshold.Known.BLOCK_ONLY_HIGH)
                    .build(),
                SafetySetting.builder()
                    .category(HarmCategory.Known.HARM_CATEGORY_HATE_SPEECH)
                    .threshold(HarmBlockThreshold.Known.BLOCK_ONLY_HIGH)
                    .build(),
                SafetySetting.builder()
                    .category(HarmCategory.Known.HARM_CATEGORY_SEXUALLY_EXPLICIT)
                    .threshold(HarmBlockThreshold.Known.BLOCK_ONLY_HIGH)
                    .build(),
                SafetySetting.builder()
                    .category(HarmCategory.Known.HARM_CATEGORY_DANGEROUS_CONTENT)
                    .threshold(HarmBlockThreshold.Known.BLOCK_ONLY_HIGH)
                    .build()
            )

            val systemInstruction = Content.fromParts(Part.fromText(systemPrompt))

            val googleSearchTool = Tool.builder().googleSearch(GoogleSearch.builder()).build()

            val config =
                GenerateContentConfig.builder()
                    // Sets the thinking budget to 0 to disable thinking mode
                    .thinkingConfig(ThinkingConfig.builder().thinkingBudget(0))
                    .candidateCount(1)
                    .maxOutputTokens(1024)
                    .safetySettings(safetySettings)
                    .systemInstruction(systemInstruction)
                    .tools(googleSearchTool)
                    .build()

            // 프롬프트 구성: 시스템 프롬프트 + 대화 컨텍스트 + 현재 메시지
            val fullPrompt = buildString {
                if (conversationContext.isNotEmpty()) {
                    append("=== 이전 대화 ===\n")
                    append(conversationContext)
                    append("\n\n=== 새로운 메시지 ===\n")
                }

                append("사용자: $userMessage")
            }

            val response = client.models.generateContent(
                MODEL_NAME,
                fullPrompt,
                config
            )

            response.text() ?: "응답을 받을 수 없었습니다..."

        } catch (e: Exception) {
            logger.error("Gemini API 오류: ${e.message}", e)
            "죄송합니다... 뭔가 이상한데요? 🥺"
        }
    }
}
