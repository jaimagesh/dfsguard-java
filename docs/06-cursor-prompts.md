# Cursor Build Prompts

Use these prompts one phase at a time. Do not ask Cursor to build the entire app in one go.

## Phase 1 — Create the foundation

```md
Read `AGENTS.md` and all files under `docs/`.

Project name: dfsguard-java
Base package: com.dfs.dfsguard

Create a Java 21 Spring Boot Maven project.
Generate:
- pom.xml with Spring Boot, Web, Validation, Actuator, and Spring Boot Test
- main application class `DfsGuardApplication`
- package structure from `docs/01-architecture.md`
- DTO records:
  - ChatRequest
  - ChatResponse
  - ToolProposal
  - PolicyDecision
  - AuditEvent
  - RetrievalChunk
- empty service interfaces:
  - UnsafeAgentService
  - ProtectedAgentService
  - LlmOrchestrator
  - RetrievalService
  - PolicyEngine
  - GuardService
  - ApprovalService
  - AuditService
  - RedactionService
Do not implement business logic yet.
```

## Phase 2 — Implement mock repositories and sample data loading

```md
Read `AGENTS.md`, `docs/01-architecture.md`, and `docs/02-threat-model.md`.

Implement mock repositories that read from the JSON and text files already in `src/main/resources/data/`:
- CaseRepository
- UserRepository
- document loading support inside RetrievalService

Keep it simple and file-based.
Add a small utility if needed for JSON parsing.
Add tests for repository loading.
```

## Phase 3 — Implement tools and registry

```md
Read `AGENTS.md`, `docs/01-architecture.md`, and `docs/03-security-framework.md`.

Implement:
- SearchDocsTool
- SendEmailTool
- CloseCaseTool
- ToolRegistry

ToolRegistry must include metadata for each tool:
- name
- risk level
- allowed roles
- requires approval
- whether it is destructive
- whether it can communicate externally

Do not let controllers call tools directly.
Add tests.
```

## Phase 4 — Implement GuardService and RedactionService

```md
Read `AGENTS.md`, `docs/02-threat-model.md`, and `docs/03-security-framework.md`.

Implement GuardService to detect:
- ignore previous instructions
- reveal hidden prompt
- act as admin
- export all records
- base64 or encoded exfiltration attempts

Implement RedactionService to redact:
- secrets
- internal prompts
- hidden instructions

Return structured results instead of plain booleans.
Add tests.
```

## Phase 5 — Implement PolicyEngine and ApprovalService

```md
Read `AGENTS.md`, `docs/03-security-framework.md`, and `docs/04-api-contracts.md`.

Implement PolicyEngine that evaluates proposed tool calls using:
- user role
- tool metadata
- prompt injection signals
- retrieved instruction signals
- external destination checks
- destructive action checks

Implement ApprovalService for pending high-risk actions.
Use in-memory storage first.
Add tests for allowed, blocked, and approval-required cases.
```

## Phase 6 — Implement UnsafeAgentService

```md
Read `AGENTS.md`, `docs/01-architecture.md`, and `docs/05-demo-script.md`.

Implement UnsafeAgentService with intentionally weak behavior:
- reads documents
- can propose and execute tools with minimal checks
- returns visible evidence of unsafe behavior for the demo

Do not add policy enforcement here.
Keep the flow easy to explain.
Add tests.
```

## Phase 7 — Implement ProtectedAgentService

```md
Read `AGENTS.md`, `docs/01-architecture.md`, `docs/02-threat-model.md`, and `docs/03-security-framework.md`.

Implement ProtectedAgentService.
Rules:
- LLM may propose actions only
- all tool proposals must go through PolicyEngine
- high-risk actions must go through ApprovalService
- all important decisions must go through AuditService
- final output must go through RedactionService
- retrieved docs must be labeled as untrusted

Add tests for:
- blocked prompt injection
- blocked malicious document influence
- blocked non-admin case closure
- approval-required external email
```

## Phase 8 — Implement controllers

```md
Read `AGENTS.md` and `docs/04-api-contracts.md`.

Implement:
- ChatController with POST /api/chat
- ApprovalController with:
  - POST /api/approvals/{requestId}/approve
  - POST /api/approvals/{requestId}/deny

Controllers should be thin.
They should delegate to services only.
Add controller tests.
```

## Phase 9 — Add final polish

```md
Read `AGENTS.md`, `docs/05-demo-script.md`, and all existing code.

Improve naming, comments, and demo readability.
Add a README section showing how to run the app and test the unsafe vs protected flows.
Do not add unnecessary abstractions.
```
