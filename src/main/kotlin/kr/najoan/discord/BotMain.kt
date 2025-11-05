package kr.najoan.discord

import kr.najoan.config.ConfigManager
import kr.najoan.ai.GeminiClient
import kr.najoan.discord.handler.MessageHandler
import kr.najoan.discord.handler.CommandRegistrar
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("AIChanBot")

fun main() {
    try {
        logger.info("Config 로드 중...")
        val config = ConfigManager.loadConfig()

        logger.info("Gemini API 초기화 중...")
        GeminiClient.initialize(config.gemini.apiKey)

        logger.info("Discord JDA 초기화 중...")
        val jda = JDABuilder.createDefault(config.discord.token)
            .addEventListeners(MessageHandler())
            .enableIntents(
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.GUILD_MESSAGES
            )
            .setActivity(Activity.watching("선배를 기다리는 중... 💕"))
            .setStatus(OnlineStatus.ONLINE)
            .build()

        jda.awaitReady()
        logger.info("아이짱 봇이 준비되었습니다! 🌸")

        // 슬래시 명령어 등록
        CommandRegistrar.registerCommands(jda)

    } catch (e: Exception) {
        logger.error("봇 시작 실패: ${e.message}", e)
        System.exit(1)
    }
}
