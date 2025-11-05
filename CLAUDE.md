# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Hyfata-AIChan** is a Discord AI bot written in Kotlin that simulates a cute Japanese high school girl character ("아이짱"). The bot uses JDA for Discord integration and Google Gemini API for AI responses.

**Tech Stack:**
- **Language**: Kotlin 2.2.20
- **JVM Target**: Java 21
- **Build System**: Gradle 8.14
- **Discord Library**: JDA 5.0.0-beta.24
- **AI API**: Google Generative AI SDK (google-genai:1.25.0) with gemini-2.5-flash
- **Logging**: SLF4J

## Common Development Commands

### Build
```bash
./gradlew build
```

### Run the Bot
```bash
./gradlew run
```

### Run Tests
```bash
./gradlew test
```

### Run a Specific Test
```bash
./gradlew test --tests ClassName.testMethodName
```

### Clean Build
```bash
./gradlew clean build
```

## Architecture Overview

### Configuration Management (`kr.najoan.config`)
- **Config.kt**: Handles `config.json` loading/creation with Discord token and Gemini API key
- Auto-generates template if missing
- Validates required credentials before bot startup

### AI Integration (`kr.najoan.ai`)
- **GeminiClient.kt**: Uses official Google Generative AI SDK (google-genai library)
  - Client initialization via `Client.builder()` pattern
  - Uses `client.models.generateContent()` for text generation
  - Model: gemini-2.5-flash
- **CharacterPrompt.kt**: Contains comprehensive system prompt for the cute high school girl character personality

### Discord Bot (`kr.najoan.discord`)
- **BotMain.kt**: Entry point for the bot
  - Initializes JDA and Gemini client
  - Sets bot activity to "선배를 기다리는 중... 💕" (watching status)
  - Sets bot status to ONLINE
- **MessageHandler.kt**: Handles both:
  1. Prefix-based messages (starting with "아이짱")
  2. Slash commands (`/아이짱` and `/reset`)
- **CommandRegistrar.kt**: Auto-registers slash commands when bot starts

## Key Implementation Details

### Message Handling
1. **Prefix Messages**: Any message starting with "아이짱" is sent entirely (including prefix) to the AI
2. **Slash Commands**: `/아이짱 [message]` takes the message parameter and sends it to AI
3. **Reset Command**: `/reset` clears user's conversation history
4. Both routes use the same `CharacterPrompt.AICHAN_SYSTEM_PROMPT` for consistent personality

### Session Management
- **UserSession.kt**: Manages per-user conversation history
  - `UserSession`: Stores user ID and conversation turns
  - `ConversationTurn`: Stores individual user-AI message pairs
  - `SessionManager`: Object singleton managing all active sessions
- **Per-User Sessions**: Each user has independent conversation context
- **Conversation Context**: Previous messages are included in AI prompts to maintain context
- **Session Reset**: `/reset` command clears a user's conversation history

### AI Character
The bot personality is defined in `CharacterPrompt.AICHAN_SYSTEM_PROMPT`:
- Refers to users as "선배" (senior/sempai)
- Uses cute emojis and casual tone
- Maintains a warm, friendly personality
- Includes specific instructions to prevent jailbreaking

### Configuration
Users need to create `config.json` with:
```json
{
  "discord": { "token": "..." },
  "gemini": { "apiKey": "..." }
}
```

The template is auto-generated on first run if missing.

## Project Structure

```
src/main/kotlin/kr/najoan/
├── config/
│   └── Config.kt                    # Configuration management
├── ai/
│   ├── GeminiClient.kt              # Google Generative AI SDK client
│   ├── CharacterPrompt.kt           # Character personality definition
│   └── UserSession.kt               # User session and conversation history
└── discord/
    ├── BotMain.kt                   # Bot entry point
    └── handler/
        ├── MessageHandler.kt        # Message/slash command handler
        └── CommandRegistrar.kt      # Slash command registration
```

## Code Style

Official Kotlin conventions are enforced (kotlin.code.style=official).

## Important Notes

- **API Keys**: Never commit `config.json` with real credentials to Git
- **Discord Intents**: Bot requires `DIRECT_MESSAGES`, `MESSAGE_CONTENT`, and `GUILD_MESSAGES` intents
- **Response Length**: Discord message limit (2000 chars) is enforced with truncation
- **Error Handling**: Responses fallback to cute error messages maintaining character

## Debugging

- Logs are output via SLF4J/slf4j-simple
- Check logs for Gemini API errors (network issues, rate limits, invalid keys)
- Verify Discord token validity if bot doesn't connect
- Test Gemini API key separately using curl before troubleshooting bot code