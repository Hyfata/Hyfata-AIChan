package kr.najoan.discord.handler

import kr.najoan.ai.CharacterPrompt
import kr.najoan.ai.GeminiClient
import kr.najoan.ai.SessionManager
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("MessageHandler")
private const val PREFIX = "아이짱"

class MessageHandler : ListenerAdapter() {

    override fun onMessageReceived(event: MessageReceivedEvent) {
        // 봇 자신의 메시지는 무시
        if (event.author.isBot) return

        val content = event.message.contentRaw

        // 접두사로 시작하는 메시지 처리
        if (content.startsWith(PREFIX)) {
            handlePrefixMessage(event, content)
        }
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        when (event.name) {
            "아이짱" -> handleSlashCommand(event)
            "reset" -> handleResetCommand(event)
        }
    }

    private fun handlePrefixMessage(event: MessageReceivedEvent, fullContent: String) {
        try {
            // 메시지 전체를 AI에 전달 (접두사 포함)

            event.channel.sendTyping().queue()

            val userId = event.author.id
            val session = SessionManager.getOrCreateSession(userId)

            val aiResponse = GeminiClient.generateResponse(
                userMessage = fullContent,
                systemPrompt = CharacterPrompt.AICHAN_SYSTEM_PROMPT,
                conversationContext = session.getContextString()
            )

            // 세션에 대화 추가
            session.addTurn(fullContent, aiResponse)

            // 응답 길이 제한 (Discord 메시지 제한 2000자)
            val response = if (aiResponse.length > 1900) {
                aiResponse.substring(0, 1897) + "..."
            } else {
                aiResponse
            }

            event.message.reply(response).queue()

        } catch (e: Exception) {
            logger.error("접두사 메시지 처리 오류: ${e.message}", e)
            event.message.reply("어... 뭔가 이상한데요? 선배... 🥺").queue()
        }
    }

    private fun handleSlashCommand(event: SlashCommandInteractionEvent) {
        try {
            // 슬래시 명령어 메시지 옵션 가져오기
            val message = event.getOption("메시지")?.asString ?: ""

            if (message.isEmpty()) {
                event.reply("선배... 메시지가 없는데요? 😊").setEphemeral(true).queue()
                return
            }

            // 로딩 표시
            event.deferReply().queue()

            val userId = event.user.id
            val session = SessionManager.getOrCreateSession(userId)

            val aiResponse = GeminiClient.generateResponse(
                userMessage = message,
                systemPrompt = CharacterPrompt.AICHAN_SYSTEM_PROMPT,
                conversationContext = session.getContextString()
            )

            // 세션에 대화 추가
            session.addTurn(message, aiResponse)

            // 응답 길이 제한
            val response = if (aiResponse.length > 1900) {
                aiResponse.substring(0, 1897) + "..."
            } else {
                aiResponse
            }

            event.hook.editOriginal(response).queue()

        } catch (e: Exception) {
            logger.error("슬래시 명령어 처리 오류: ${e.message}", e)
            event.hook.editOriginal("어... 뭔가 이상한데요? 선배... 🥺").queue()
        }
    }

    private fun handleResetCommand(event: SlashCommandInteractionEvent) {
        try {
            val userId = event.user.id
            SessionManager.resetSession(userId)

            event.reply("선배와의 대화를 초기화했어요! 이제 새로운 얘기를 나눠요~ 🌸")
                .setEphemeral(true)
                .queue()

        } catch (e: Exception) {
            logger.error("리셋 명령어 처리 오류: ${e.message}", e)
            event.reply("어... 뭔가 이상한데요? 선배... 🥺")
                .setEphemeral(true)
                .queue()
        }
    }
}
