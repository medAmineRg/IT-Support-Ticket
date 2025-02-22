# IT Support Ticket API Documentation

## Authentication

All endpoints require a `X-User` header containing the username (either employee or IT support staff).

## Endpoints

### Create Ticket

`POST /api/tickets`

Creates a new support ticket.

**Headers:**

- X-User: username (required)

**Request Body:**

```json
{
  "title": "Cannot access email",
  "description": "Unable to login to my work email since this morning"
}
```

**Response:**

```json
{
  "id": 1,
  "title": "Cannot access email",
  "description": "Unable to login to my work email since this morning",
  "status": "OPEN",
  "createdBy": "employee1",
  "createdAt": "2023-12-20T10:30:00Z",
  "comments": []
}
```

### Get All Tickets

`GET /api/tickets`

Retrieves all tickets accessible to the user.

**Headers:**

- X-User: username (required)

**Response:**

```json
[
  {
    "id": 1,
    "title": "Cannot access email",
    "status": "OPEN",
    "createdBy": "employee1",
    "createdAt": "2023-12-20T10:30:00Z",
    "comments": []
  }
]
```

### Update Ticket Status

`PUT /api/tickets/{ticketId}/status`

Updates the status of an existing ticket.

**Headers:**

- X-User: username (required)

**Parameters:**

- status: OPEN, IN_PROGRESS, RESOLVED, or CLOSED

**Response:**

```json
{
  "id": 1,
  "title": "Cannot access email",
  "status": "IN_PROGRESS",
  "createdBy": "employee1",
  "updatedAt": "2023-12-20T11:30:00Z",
  "comments": []
}
```

### Add Comment

`POST /api/tickets/{ticketId}/comments`

Adds a comment to an existing ticket.

**Headers:**

- X-User: username (required)

**Request Body:**

```json
{
  "content": "Checking email server status"
}
```

**Response:**

```json
{
  "id": 1,
  "title": "Cannot access email",
  "status": "IN_PROGRESS",
  "comments": [
    {
      "id": 1,
      "content": "Checking email server status",
      "createdBy": "itsupport1",
      "createdAt": "2023-12-20T11:35:00Z"
    }
  ]
}
```

### Search Tickets

`GET /api/tickets/search`

Search tickets by ID and/or status.

**Headers:**

- X-User: username (required)

**Query Parameters:**

- ticketId (optional): Ticket ID to search for
- status (optional): Filter by ticket status

**Response:**

```json
[
  {
    "id": 1,
    "title": "Cannot access email",
    "status": "IN_PROGRESS",
    "createdBy": "employee1",
    "comments": []
  }
]
```
