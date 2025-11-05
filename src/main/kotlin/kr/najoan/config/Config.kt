package kr.najoan.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File

data class ConfigData(
    val discord: DiscordConfig = DiscordConfig(),
    val gemini: GeminiConfig = GeminiConfig()
)

data class DiscordConfig(
    val token: String = "YOUR_DISCORD_BOT_TOKEN_HERE"
)

data class GeminiConfig(
    val apiKey: String = "YOUR_GEMINI_API_KEY_HERE"
)

object ConfigManager {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    private val configFile = File("config.json")
    private lateinit var config: ConfigData

    fun loadConfig(): ConfigData {
        if (!configFile.exists()) {
            createDefaultConfig()
        }

        val configJson = configFile.readText()
        config = gson.fromJson(configJson, ConfigData::class.java)

        // 기본값이 설정되지 않았다면 예외 발생
        if (config.discord.token == "YOUR_DISCORD_BOT_TOKEN_HERE" ||
            config.gemini.apiKey == "YOUR_GEMINI_API_KEY_HERE"
        ) {
            throw IllegalStateException(
                "Config file requires actual API keys. " +
                "Please update ${configFile.absolutePath} with your credentials."
            )
        }

        return config
    }

    private fun createDefaultConfig() {
        val defaultConfig = ConfigData()
        val json = gson.toJson(defaultConfig)
        configFile.writeText(json)
        println("기본 config.json이 생성되었습니다: ${configFile.absolutePath}")
        println("Discord 봇 토큰과 Gemini API 키를 설정해주세요.")
    }

    fun getConfig(): ConfigData = config
}
