# Hyfata-AIChan 🌸

일본 서브컬처 스타일의 귀여운 여고생 AI 봇 "아이짱"입니다. Discord에서 접두사나 슬래시 명령어를 통해 아이짱과 대화할 수 있습니다.

## 기능

- **접두사 메시지**: `아이짱`으로 시작하는 메시지에 응답
- **슬래시 명령어**: `/아이짱 [메시지]`로 직접 대화
- **세션 관리**: 사용자별 독립적인 대화 히스토리 유지
- **세션 초기화**: `/reset`으로 대화 히스토리 초기화
- **AI 응답**: Google Gemini API를 활용한 자연스러운 대화
- **상태메시지**: Discord에 아이짱의 현재 상태 표시
- **캐릭터**: 귀여운 여고생 느낌의 응답과 이모티콘

## 설치

### 필수 요구사항

- Java 21 이상
- Kotlin 2.2.20
- Gradle 8.14

### 1. API 키 준비

**Discord Bot Token**
- [Discord Developer Portal](https://discord.com/developers/applications)에서 새 애플리케이션 생성
- "Bot" 메뉴에서 새 봇 추가
- "TOKEN" 복사

**Google Gemini API Key**
- [Google AI Studio](https://aistudio.google.com/apikey)에서 API 키 생성

### 2. 설정 파일 생성

프로젝트 디렉토리에서 다음 명령어로 봇을 실행하면 자동으로 `config.json` 템플릿이 생성됩니다:

```bash
./gradlew run
```

생성된 `config.json` 파일을 열고 다음과 같이 채워주세요:

```json
{
  "discord": {
    "token": "YOUR_DISCORD_BOT_TOKEN_HERE"
  },
  "gemini": {
    "apiKey": "YOUR_GEMINI_API_KEY_HERE"
  }
}
```

### 3. 봇 실행

```bash
./gradlew run
```

봇이 정상적으로 실행되면:
```
아이짱 봇이 준비되었습니다! 🌸
슬래시 명령어 등록 성공: /아이짱
```

## 사용 방법

### 접두사 사용

Discord 채팅창에 `아이짱`으로 시작하는 메시지를 입력하세요:

```
아이짱 안녕! 오늘 날씨 좋네~
```

아이짱이 응답합니다:
```
선배! 오늘 정말 날씨가 좋네요! 이런 날씨면 밖에 나가고 싶은데... 😊✨
```

### 슬래시 명령어 사용

#### /아이짱 (채팅)
채팅창에 `/아이짱`을 입력하면 "메시지" 옵션이 표시됩니다:

```
/아이짱 메시지: 선배는 뭘 좋아해?
```

#### /reset (세션 초기화)
대화 히스토리를 초기화하고 새로운 대화를 시작하려면:

```
/reset
```

실행하면 선배와의 대화 히스토리가 모두 삭제되고 새로운 대화를 시작할 수 있습니다.

## 대화 세션 관리

- **사용자별 독립 세션**: 각 사용자는 독립적인 대화 히스토리를 유지합니다
- **대화 컨텍스트**: 아이짱은 이전 대화를 기억하고 맥락에 맞는 응답을 제공합니다
- **세션 초기화**: `/reset` 명령어로 언제든지 대화 히스토리를 초기화할 수 있습니다

## 프로젝트 구조

```
src/main/kotlin/kr/najoan/
├── config/
│   └── Config.kt          # 설정 관리
├── ai/
│   ├── GeminiClient.kt    # Gemini API 클라이언트
│   └── CharacterPrompt.kt # AI 캐릭터 설정
└── discord/
    ├── BotMain.kt        # 봇 메인 진입점
    └── handler/
        ├── MessageHandler.kt    # 메시지/슬래시 명령어 처리
        └── CommandRegistrar.kt  # 슬래시 명령어 등록
```

## 개발 명령어

### 빌드

```bash
./gradlew build
```

### 실행

```bash
./gradlew run
```

### 테스트

```bash
./gradlew test
```

## 기술 스택

- **언어**: Kotlin
- **빌드**: Gradle
- **Discord 라이브러리**: JDA 5.0.0-beta.24
- **AI API**: Google Gemini API (gemini-2.5-flash)
- **HTTP 클라이언트**: OkHttp3
- **로깅**: SLF4J
- **JSON 처리**: Gson

## 주의사항

- `config.json` 파일에는 민감한 정보가 포함되어 있으므로 절대 Git에 커밋하지 마세요
- Discord 봇 토큰과 Gemini API 키는 안전하게 관리하세요
- 봇이 메시지를 읽을 수 있도록 Discord 서버 설정에서 필요한 권한을 부여하세요

## 라이선스

MIT License

## 작가

[@najoan](https://github.com/najoan)

---

선배, 뭐가 궁금하신가요? 아이짱이 도와드릴게요~ 🌸
