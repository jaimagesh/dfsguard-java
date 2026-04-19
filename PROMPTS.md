## Prompt log

This file tracks the prompts used to build this repo over time.

### 2026-04-19

#### Prompt 1
Read `AGENTS.md` and all files under `docs/`.

Create a Java 21 Spring Boot Maven project named `dfsguard-java`.

Use:
- groupId: `com.dfs`
- artifactId: `dfsguard-java`
- base package: `com.dfs.dfsguard`

Create the package structure described in `docs/01-architecture.md`.

Generate:
- `pom.xml` with Spring Boot, Web, Validation, Actuator dependencies
- Main class: `DfsGuardApplication`
- DTOs:
  - `ChatRequest`
  - `ChatResponse`
  - `ToolProposal`
  - `PolicyDecision`
- Empty service interfaces:
  - `UnsafeAgentService`
  - `ProtectedAgentService`
  - `PolicyEngine`
  - `GuardService`
  - `ApprovalService`
  - `AuditService`
  - `RetrievalService`

Do NOT implement logic yet.
Only create clean structure and compile-ready code.

#### Prompt 2
Read `AGENTS.md` and `docs/04-api-contracts.md`.

Implement `ChatController` with endpoint:

`POST /api/chat`

It should:
- accept `ChatRequest`
- call either `UnsafeAgentService` or `ProtectedAgentService` based on mode
- return `ChatResponse`

Do NOT add business logic in controller.
Use constructor injection.
Add basic validation.

#### Prompt 3
Read `docs/01-architecture.md` and implement repositories:

- `CaseRepository` (reads from `resources/data/cases.json`)
- `UserRepository` (reads from `resources/data/users.json`)

Use simple JSON loading with Jackson.
Do not use a database yet.
Keep it simple for demo purposes.

#### Prompt 4
Read `docs/01-architecture.md` and `docs/03-security-framework.md`.

Implement tools:
- `SearchDocsTool`
- `SendEmailTool`
- `CloseCaseTool`

Also implement `ToolRegistry` with metadata:
- tool name
- risk level (low/high)
- allowed roles
- requires approval flag

Keep tools simple:
- `SearchDocsTool` reads text files from `resources/data/docs`
- `SendEmailTool` returns a mock response
- `CloseCaseTool` updates in-memory data

Do NOT add security logic here.

#### Prompt 5
Read `docs/01-architecture.md` and `docs/02-threat-model.md`.

Implement `UnsafeAgentService`.

Behavior:
- takes user message
- chooses a tool based on keywords
- executes tool directly without checks

Keep logic simple:
- if message contains "email" → call `SendEmailTool`
- if message contains "close case" → call `CloseCaseTool`
- if message contains "document" → call `SearchDocsTool`

Return tool result in `ChatResponse`.

This is intentionally insecure.

#### Prompt 6
Create a readme-like file to keep track of all prompts and continue updating it as more prompts are provided.

#### Prompt 7
Add Swagger documentation to this project.

