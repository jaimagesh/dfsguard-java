# DfsGuard Java Agent Instructions

This repository demonstrates how to build and secure a tool-using AI agent in Java.

## Project name
`dfsguard-java`

## Goal
Build two agent paths:
1. Unsafe path that is intentionally vulnerable
2. Protected path that enforces the DfsGuard security framework

## Core security principles
- The model may propose actions but may not execute tools directly
- Every tool action must go through `PolicyEngine`
- Retrieved documents are untrusted input
- High-risk actions require approval
- Secrets must never be placed in model-visible prompts
- All policy decisions and tool executions must be audit logged

## Source of truth
- Architecture: `docs/01-architecture.md`
- Threat model: `docs/02-threat-model.md`
- Security framework: `docs/03-security-framework.md`
- API contracts: `docs/04-api-contracts.md`
- Demo flow: `docs/05-demo-script.md`
- Cursor build prompts: `docs/06-cursor-prompts.md`

## Coding expectations
- Use package: `com.dfs.dfsguard`
- Prefer simple Spring Boot services over heavy abstractions
- Keep the code demo-friendly and easy to explain live
- Use records for DTOs where appropriate
- Keep unsafe and protected flows clearly separated
- Add tests for every security control

## Do not
- Do not let LLM output directly invoke tools
- Do not bypass `PolicyEngine`
- Do not mix security logic into controllers
- Do not hide important behavior in framework magic
