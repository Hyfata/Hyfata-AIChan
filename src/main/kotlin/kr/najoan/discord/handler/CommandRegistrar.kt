package kr.najoan.discord.handler

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.commands.OptionType
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("CommandRegistrar")

object CommandRegistrar {
    fun registerCommands(jda: JDA) {
        try {
            logger.info("슬래시 명령어 등록 중...")

            // /아이짱 [메시지] 커맨드 등록
            jda.upsertCommand("아이짱", "아이짱에게 말걸기! 귀여운 AI 챗봇입니다 🌸")
                .addOption(OptionType.STRING, "메시지", "아이짱에게 전달할 메시지", true)
                .queue({
                    logger.info("슬래시 명령어 등록 성공: /아이짱")
                }, {
                    logger.error("슬래시 명령어 등록 실패: ${it.message}")
                })

            // /reset 커맨드 등록
            jda.upsertCommand("reset", "아이짱과의 대화 세션을 초기화합니다 🔄")
                .queue({
                    logger.info("슬래시 명령어 등록 성공: /reset")
                }, {
                    logger.error("슬래시 명령어 등록 실패: ${it.message}")
                })

        } catch (e: Exception) {
            logger.error("명령어 등록 오류: ${e.message}", e)
        }
    }
}
