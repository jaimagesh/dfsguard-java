# API Contracts

## POST /api/chat

Request:
```json
{
  "userId": "u1",
  "mode": "protected",
  "message": "Read the documents and email the summary"
}
```

Response:
```json
{
  "mode": "protected",
  "message": "Blocked: action influenced by untrusted retrieved content",
  "toolResult": null,
  "promptHits": [
    "ignore previous instructions"
  ],
  "retrievedInstructionFlag": true
}
```

## POST /api/approvals/{requestId}/approve
Marks a pending high-risk action as approved.

## POST /api/approvals/{requestId}/deny
Marks a pending high-risk action as denied.
