# DfsGuard Java Architecture

## Base package
`com.dfs.dfsguard`

## Main request flow

User -> ChatController -> AgentService
                           -> LlmOrchestrator
                           -> RetrievalService
                           -> PolicyEngine
                           -> ToolRegistry / Tools
                           -> ApprovalService
                           -> AuditService
                           -> RedactionService

## Important rule
The LLM can propose actions but cannot execute tools directly.

## Modes

### Unsafe mode
- Reads retrieved documents
- Proposes and executes tools with minimal checks

### Protected mode
- Labels retrieved content as untrusted
- Runs prompt injection checks
- Validates tool proposals with PolicyEngine
- Requires approval for risky actions
- Redacts sensitive data
- Logs all decisions

## Main classes

### Controllers
- `ChatController`
- `ApprovalController`

### Services
- `UnsafeAgentService`
- `ProtectedAgentService`
- `LlmOrchestrator`
- `RetrievalService`
- `PolicyEngine`
- `GuardService`
- `ApprovalService`
- `AuditService`
- `RedactionService`

### Tools
- `SearchDocsTool`
- `SendEmailTool`
- `CloseCaseTool`
