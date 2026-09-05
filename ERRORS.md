# API error contract

Every error response uses this shape:

```json
{
  "code": "2.1.1",
  "message": "Invalid admin key.",
  "data": null
}
```

`code` is stable for client handling. `message` is intended for a human or application log.

## Common errors

| Code | Meaning |
| --- | --- |
| `0.0.0` | Unexpected internal error |
| `0.0.1` | Invalid body field |
| `0.0.2` | Required header missing |
| `0.0.3` | Required query/form parameter missing |
| `0.0.4` | Missing or invalid JSON body |
| `0.0.5` | Upload exceeds the configured size limit |
| `0.0.6` | Required multipart file part missing |
| `0.0.7` | Invalid multipart request |
| `0.0.8` | Invalid path or query value |
| `0.0.9` | HTTP method not supported |
| `0.0.10` | Content type not supported |
| `0.0.11` | Route/resource not found |
| `0.0.12` | File storage operation failed |
| `0.0.13` | Request conflicts with existing data |
| `0.1.1` | Authentication is missing or token is invalid |
| `0.1.2` | Authenticated user has no permission for the resource |

## Module and route codes

The remaining codes follow `module.route.error`.

| Module | Routes |
| --- | --- |
| `1` Users | `1` register, `2` login, `3` verification, `4` create data, `5` list data, `6` upload file, `7` list file, `8` download/delete file |
| `2` Apps | `1` register app, `2` list apps |

For example, `2.1.1` means the first error in the app-registration route: an invalid admin key.
