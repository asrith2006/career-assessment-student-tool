# API Documentation - Career Assessment System

Base URL: `http://localhost:8080/api`

## Authentication Endpoints

### Register
```
POST /auth/register

Request Body:
{
  "fullName": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "confirmPassword": "password123"
}

Response (201 Created):
{
  "success": true,
  "message": "Registration successful",
  "data": {
    "token": "eyJhbGc...",
    "type": "Bearer",
    "id": 1,
    "email": "john@example.com",
    "fullName": "John Doe",
    "role": "STUDENT",
    "expiresIn": 86400000
  }
}
```

### Login
```
POST /auth/login

Request Body:
{
  "email": "john@example.com",
  "password": "password123"
}

Response (200 OK):
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGc...",
    "type": "Bearer",
    "id": 1,
    "email": "john@example.com",
    "fullName": "John Doe",
    "role": "STUDENT",
    "expiresIn": 86400000
  }
}
```

### Validate Token
```
POST /auth/validate-token

Headers:
Authorization: Bearer <token>

Response (200 OK):
{
  "success": true,
  "message": "Token is valid",
  "statusCode": 200
}
```

## Question Endpoints

### Get All Questions (Public)
```
GET /questions/public/all

Response (200 OK):
{
  "success": true,
  "message": "Questions retrieved successfully",
  "data": [
    {
      "id": 1,
      "questionText": "What is your primary interest?",
      "category": "General",
      "difficulty": 1,
      "active": true,
      "answers": [
        {
          "id": 1,
          "answerText": "Building software applications",
          "displayOrder": 1,
          "careerMapping": "1",
          "active": true
        }
      ]
    }
  ]
}
```

### Get Random Questions (Public)
```
GET /questions/public/random?count=10

Query Parameters:
- count: number of questions (1-50, default: 10)

Response (200 OK): Same as above
```

### Get Questions by Category (Public)
```
GET /questions/public/category/General

Response (200 OK): Same as above
```

### Get Single Question (Public)
```
GET /questions/public/1

Response (200 OK): Same as above (single object)
```

### Create Question (Admin)
```
POST /questions/admin

Headers:
Authorization: Bearer <admin-token>

Request Body:
{
  "questionText": "Your question here?",
  "category": "Technology",
  "description": "Optional description",
  "difficulty": 2,
  "answers": [
    {
      "answerText": "Answer option 1",
      "isCorrect": true,
      "displayOrder": 1,
      "careerMapping": "1,2"
    }
  ]
}

Response (201 Created): Question object
```

### Update Question (Admin)
```
PUT /questions/admin/1

Headers:
Authorization: Bearer <admin-token>

Request Body: Same as create

Response (200 OK): Updated question object
```

### Delete Question (Admin - Soft Delete)
```
DELETE /questions/admin/1

Headers:
Authorization: Bearer <admin-token>

Response (200 OK):
{
  "success": true,
  "message": "Question deactivated successfully"
}
```

### Permanently Delete Question (Admin)
```
DELETE /questions/admin/permanent/1

Headers:
Authorization: Bearer <admin-token>

Response (200 OK):
{
  "success": true,
  "message": "Question permanently deleted successfully"
}
```

## Test Endpoints

### Submit Test
```
POST /tests/submit

Headers:
Authorization: Bearer <student-token>

Request Body:
{
  "testName": "Career Assessment Test",
  "responses": [
    {
      "questionId": 1,
      "selectedAnswerId": 1
    },
    {
      "questionId": 2,
      "selectedAnswerId": 4
    }
  ]
}

Response (201 Created):
{
  "success": true,
  "message": "Test submitted successfully",
  "data": {
    "id": 1,
    "testName": "Career Assessment Test",
    "totalQuestions": 2,
    "correctAnswers": 1,
    "scorePercentage": 50.0,
    "completedAt": "2024-04-06T10:30:00",
    "recommendedCareers": [
      {
        "id": 1,
        "careerName": "Software Engineer",
        "matchPercentage": 60.0
      }
    ]
  }
}
```

### Get My Results (Student)
```
GET /tests/my-results

Headers:
Authorization: Bearer <student-token>

Response (200 OK):
{
  "success": true,
  "message": "Results retrieved successfully",
  "data": [
    {
      "id": 1,
      "testName": "Career Assessment Test",
      "totalQuestions": 10,
      "correctAnswers": 7,
      "scorePercentage": 70.0,
      "completedAt": "2024-04-06T10:30:00",
      "recommendedCareers": [...]
    }
  ]
}
```

### Get Specific Result
```
GET /tests/result/1

Headers:
Authorization: Bearer <token>

Response (200 OK): Single result object with full details
```

### Get All Results (Admin)
```
GET /tests/admin/all

Headers:
Authorization: Bearer <admin-token>

Response (200 OK): Array of all results
```

## Career Endpoints

### Get All Careers (Public)
```
GET /careers/public/all

Response (200 OK):
{
  "success": true,
  "message": "Careers retrieved successfully",
  "data": [
    {
      "id": 1,
      "careerName": "Software Engineer",
      "description": "Develops software applications...",
      "requiredSkills": "[\"Java\", \"Python\", \"Problem Solving\"]",
      "salaryRange": "$100,000 - $150,000",
      "jobOutlook": "Growing",
      "active": true
    }
  ]
}
```

### Get Career by ID (Public)
```
GET /careers/public/1

Response (200 OK): Single career object
```

### Search Career by Name (Public)
```
GET /careers/public/search?name=Software

Response (200 OK): Matching career object
```

### Create Career (Admin)
```
POST /careers/admin

Headers:
Authorization: Bearer <admin-token>

Request Body:
{
  "careerName": "Data Scientist",
  "description": "Analyzes complex data...",
  "requiredSkills": "[\"Python\", \"R\", \"SQL\"]",
  "salaryRange": "$120,000 - $180,000",
  "jobOutlook": "Very Growing"
}

Response (201 Created): Career object
```

### Update Career (Admin)
```
PUT /careers/admin/1

Headers:
Authorization: Bearer <admin-token>

Request Body: Same as create

Response (200 OK): Updated career object
```

### Delete Career (Admin - Soft Delete)
```
DELETE /careers/admin/1

Headers:
Authorization: Bearer <admin-token>

Response (200 OK):
{
  "success": true,
  "message": "Career deactivated successfully"
}
```

## User Management Endpoints (Admin)

### Get All Users
```
GET /admin/users

Headers:
Authorization: Bearer <admin-token>

Response (200 OK):
{
  "success": true,
  "data": [
    {
      "id": 1,
      "email": "student@example.com",
      "fullName": "John Doe",
      "role": "STUDENT",
      "active": true,
      "createdAt": "2024-04-06T10:00:00"
    }
  ]
}
```

### Get User by ID
```
GET /admin/users/1

Headers:
Authorization: Bearer <admin-token>

Response (200 OK): Single user object
```

### Get User by Email
```
GET /admin/users/email/student@example.com

Headers:
Authorization: Bearer <admin-token>

Response (200 OK): Single user object
```

### Activate User
```
PUT /admin/users/1/activate

Headers:
Authorization: Bearer <admin-token>

Response (200 OK):
{
  "success": true,
  "message": "User activated successfully"
}
```

### Deactivate User
```
PUT /admin/users/1/deactivate

Headers:
Authorization: Bearer <admin-token>

Response (200 OK):
{
  "success": true,
  "message": "User deactivated successfully"
}
```

### Update User Role
```
PUT /admin/users/1/role?role=ADMIN

Headers:
Authorization: Bearer <admin-token>

Query Parameters:
- role: STUDENT | ADMIN

Response (200 OK): Updated user object
```

### Delete User
```
DELETE /admin/users/1

Headers:
Authorization: Bearer <admin-token>

Response (200 OK):
{
  "success": true,
  "message": "User deleted successfully"
}
```

## Error Responses

### 400 Bad Request
```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "email": "Email is required",
    "password": "Password must be at least 6 characters"
  },
  "statusCode": 400
}
```

### 401 Unauthorized
```json
{
  "success": false,
  "message": "Authentication failed: Bad credentials",
  "statusCode": 401
}
```

### 403 Forbidden
```json
{
  "success": false,
  "message": "You don't have permission to access this resource",
  "statusCode": 403
}
```

### 404 Not Found
```json
{
  "success": false,
  "message": "Question not found with id : '999'",
  "statusCode": 404
}
```

### 500 Internal Server Error
```json
{
  "success": false,
  "message": "Internal server error: [error message]",
  "statusCode": 500
}
```

## Request Headers

All authenticated requests should include:
```
Authorization: Bearer <jwt-token>
Content-Type: application/json
```

## Response Format

All responses follow this standard format:
```json
{
  "success": true/false,
  "message": "Response message",
  "data": {},
  "statusCode": 200,
  "timestamp": "2024-04-06T10:30:00"
}
```

## Rate Limiting

Not currently implemented, but recommended for production.

## CORS Configuration

Allowed Origins:
- http://localhost:3000
- http://localhost:5173

Methods: GET, POST, PUT, DELETE, OPTIONS
Headers: *
Credentials: true

## Authentication Flow

1. User registers/logs in
2. Server returns JWT token
3. Client stores token in localStorage
4. Client includes token in Authorization header for authenticated requests
5. Server validates token on each request
6. If token expires, client must login again

## Data Types

- **stringified JSON**: `requiredSkills`, `salaryRange`, `careerMapping` are usually JSON strings
- **timestamps**: ISO 8601 format (e.g., "2024-04-06T10:30:00")
- **percentages**: Decimal values (e.g., 85.5 for 85.5%)

---

**Last Updated**: April 6, 2024
