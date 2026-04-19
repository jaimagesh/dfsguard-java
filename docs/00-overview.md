# DfsGuard Java Overview

DfsGuard is a Java/Spring demo that shows how AI agents can be compromised and how to defend them with a practical security framework.

## Demo objective
Show the same request in two modes:
- Unsafe mode executes dangerous actions
- Protected mode blocks or gates them

## Threats demonstrated
1. Direct prompt injection
2. Indirect prompt injection from retrieved documents
3. Unauthorized tool use
4. Sensitive information disclosure

## Security controls demonstrated
1. Input trust labeling
2. Prompt injection detection
3. Policy enforcement before tool execution
4. Approval gate for risky actions
5. Output redaction
6. Audit logging
