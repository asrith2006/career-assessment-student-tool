# Project Architecture & Structure

## System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                        FRONTEND (React)                          │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │ Browser (http://localhost:3000)                            │ │
│  │ ┌──────────────────────────────────────────────────────┐   │ │
│  │ │             React Single Page Application             │   │ │
│  │ │  ┌─────────────────┐  ┌──────────────────────────┐  │   │ │
│  │ │  │  Auth Context   │  │  Pages & Components      │  │   │ │
│  │ │  │  - User State   │  │  - Login/Register        │  │   │ │
│  │ │  │  - Token Mgmt   │  │  - Dashboard             │  │   │ │
│  │ │  └─────────────────┘  │  - TakeTest              │  │   │ │
│  │ │                        │  - Results               │  │   │ │
│  │ │  ┌─────────────────┐  │  - AdminDashboard        │  │   │ │
│  │ │  │ API Services    │  └──────────────────────────┘  │   │ │
│  │ │  │ - authService   │                                │   │ │
│  │ │  │ - questionSvc   │     ┌─────────────────────┐   │   │ │
│  │ │  │ - testService   │     │ Responsive Styling  │   │   │ │
│  │ │  │ - careerSvc     │     │ CSS Utilities       │   │   │ │
│  │ │  └─────────────────┘     └─────────────────────┘   │   │ │
│  │ │                                                      │   │ │
│  │ │  Axios Interceptor                                  │   │ │
│  │ │  ├─ Add JWT Token to Headers                       │   │ │
│  │ │  └─ Handle 401 Responses                           │   │ │
│  │ └──────────────────────────────────────────────────────┘   │ │
│  └────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
                                 │
                        HTTP/REST API (JSON)
                                 │
                    CORS: Accept localhost:3000
                                 │
                                 ▼
┌─────────────────────────────────────────────────────────────────┐
│                      BACKEND (Spring Boot)                       │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │ Application Server (http://localhost:8080/api)            │ │
│  │                                                            │ │
│  │ ┌──────────────────────────────────────────────────────┐  │ │
│  │ │              Controller Layer                         │  │ │
│  │ │  @RestController                                      │  │ │
│  │ │  - AuthController       (Login, Register)             │  │ │
│  │ │  - QuestionController   (CRUD, Random)                │  │ │
│  │ │  - TestController       (Submit, Results)             │  │ │
│  │ │  - CareerController     (CRUD)                        │  │ │
│  │ │  - UserController       (Manage)                      │  │ │
│  │ └──────────────────────────────────────────────────────┘  │ │
│  │                           │ Request Processing             │ │
│  │ ┌──────────────────────────────────────────────────────┐  │ │
│  │ │              Security Layer                          │  │ │
│  │ │  - JwtAuthenticationFilter (Token Validation)         │  │ │ 
│  │ │  - SecurityConfig (Role-based Access)                │  │ │
│  │ │  - CustomUserDetailsService                          │  │ │
│  │ │  - JwtTokenProvider (Token Generation)                │  │ │
│  │ └──────────────────────────────────────────────────────┘  │ │
│  │                           │ Business Logic                 │ │
│  │ ┌──────────────────────────────────────────────────────┐  │ │
│  │ │              Service Layer                           │  │ │
│  │ │  - AuthService      (Register, Login)                │  │ │
│  │ │  - QuestionService  (Question CRUD, Logic)           │  │ │
│  │ │  - TestService      (Score Calculation, Scoring)     │  │ │
│  │ │  - CareerService    (Recommendation Logic)           │  │ │
│  │ │  - UserService      (User Management)                │  │ │
│  │ └──────────────────────────────────────────────────────┘  │ │
│  │                           │ Data Access                    │ │
│  │ ┌──────────────────────────────────────────────────────┐  │ │
│  │ │           Repository Layer (Spring Data)             │  │ │
│  │ │  extends JpaRepository<Entity, Long>                │  │ │
│  │ │  - UserRepository                                    │  │ │
│  │ │  - QuestionRepository  (Random query)                │  │ │
│  │ │  - AnswerRepository                                  │  │ │
│  │ │  - TestResultRepository                             │  │ │
│  │ │  - CareerRecommendationRepository                    │  │ │
│  │ └──────────────────────────────────────────────────────┘  │ │
│  │                           │ SQL Queries                    │ │
│  │ ┌──────────────────────────────────────────────────────┐  │ │
│  │ │              Entity Layer (JPA)                      │  │ │
│  │ │  @Entity @Table(name = "...")                        │  │ │
│  │ │  - User @Entity(Role: STUDENT/ADMIN)                │  │ │
│  │ │  - Question (Category, Difficulty)                  │  │ │
│  │ │  - Answer (IsCorrect, CareerMapping)                │  │ │
│  │ │  - TestResult (Score, Recommendations)              │  │ │
│  │ │  - CareerRecommendation (Info)                      │  │ │
│  │ └──────────────────────────────────────────────────────┘  │ │
│  └────────────────────────────────────────────────────────────┘ │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │            Exception Handling & Validation               │  │
│  │  GlobalExceptionHandler                                  │  │
│  │  - ResourceNotFoundException                             │  │
│  │  - BadRequestException                                   │  │
│  │  - MethodArgumentNotValidException                       │  │
│  │  - AuthenticationException                               │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                                 │
                         Database Queries
                                 │
                                 ▼
┌─────────────────────────────────────────────────────────────────┐
│                   DATABASE (MySQL 8.0)                          │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │ career_assessment_db                                       │ │
│  │                                                            │ │
│  │  ┌────────────────┐  ┌───────────────┐                  │ │
│  │  │ users          │  │ questions     │                  │ │
│  │  ├────────────────┤  ├───────────────┤                  │ │
│  │  │ id (PK)        │  │ id (PK)       │                  │ │
│  │  │ email (UNIQUE) │  │ text          │                  │ │
│  │  │ password       │  │ category      │                  │ │
│  │  │ full_name      │  │ difficulty    │                  │ │
│  │  │ role           │  │ active        │                  │ │
│  │  │ active         │  └───────────────┘                  │ │
│  │  │ created_at     │                                      │ │
│  │  └────────────────┘         │ 1:M                       │ │
│  │           │                 │                           │ │
│  │           │ 1:M       ┌──────────────┐                  │ │
│  │           │           │ answers      │                  │ │
│  │           │           ├──────────────┤                  │ │
│  │      ┌────────────────┐ │ id (PK)    │                  │ │
│  │      │test_results    │ │ question_id│ (FK)            │ │
│  │      ├────────────────┤ │ text       │                  │ │
│  │      │ id (PK)        │ │ is_correct │                  │ │
│  │      │ user_id (FK) ──┼─┤ career_map │                  │ │
│  │      │ test_name      │ └──────────────┘                  │ │
│  │      │ score          │                                  │ │
│  │      │ correct_ans    │  ┌──────────────────────┐     │ │
│  │      │ careers (JSON) │  │ career_recommendations
 │     │ │
│  │      │ answers (JSON) │  ├──────────────────────┤     │ │
│  │      │ completed_at   │  │ id (PK)              │     │ │
│  │      └────────────────┘  │ career_name (UNIQUE) │     │ │
│  │                          │ description          │     │ │
│  │                          │ salary_range         │     │ │
│  │                          │ job_outlook          │     │ │
│  │                          │ active               │     │ │
│  │                          └──────────────────────┘     │ │
│  │                                                       │ │
│  │ Indexes: email, role, category, active, created_at  │ │
│  └────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

## Data Flow Diagram

```
User Registration/Login Flow:
─────────────────────────────
[User] → [React Login Form] → [authService.login()] 
    ↓
[Axios POST] → [Spring AuthController] → [AuthService]
    ↓
[BCrypt Password Check] → [JwtTokenProvider.generateToken()]
    ↓
[Return JWT Token] → [Store in LocalStorage] → [Set Auth Header]
    ↓
[Protected Routes Accessible]

Student Test Flow:
──────────────────
[Student] → [Dashboard] → [Take Test Button]
    ↓
[Load Random Questions] → [QuestionController.getRandomQuestions()]
    ↓
[Display MCQ] → [Answer Questions]
    ↓
[Submit Test] → [TestController.submitTest()]
    ↓
[Score Calculation] → [Career Recommendation Logic]
    ↓
[Save Results] → [Return Score & Recommendations]
    ↓
[Display Results] → [Show Recommended Careers]

Admin Management Flow:
──────────────────────
[Admin Dashboard] → [Select Management Option]
    ↓
[Manage Questions/Users/Careers]
    ↓
[CRUD Operations] → [Controller] → [Service] → [Repository]
    ↓
[Database Update] → [Return Success/Error]
    ↓
[Refresh List] → [Display Updated Data]
```

## File Organization

```
Backend Structure:
─────────────────

config/
├── SecurityConfig.java          (Security beans & JWT config)
└── CustomUserDetailsService.java (User details loading)

entity/
├── User.java                    (User with roles)
├── Question.java                (Assessment questions)
├── Answer.java                  (MCQ options)
├── TestResult.java              (Scores & results)
└── CareerRecommendation.java   (Career info)

dto/
├── AuthResponse.java            (Login response)
├── LoginRequest.java            (Login input)
├── RegisterRequest.java         (Registration input)
├── QuestionDTO.java             (Question transfer)
├── TestSubmissionRequest.java   (Test submission)
└── ApiResponse.java             (Standard response)

repository/
├── UserRepository.java          (User queries)
├── QuestionRepository.java      (Question queries)
├── AnswerRepository.java        (Answer queries)
├── TestResultRepository.java    (Result queries)
└── CareerRecommendationRepository.java (Career queries)

service/
├── AuthService.java             (Auth logic)
├── QuestionService.java         (Question logic)
├── TestService.java             (Test & scoring logic)
├── CareerRecommendationService.java (Career logic)
└── UserService.java             (User management)

controller/
├── AuthController.java          (Auth endpoints)
├── QuestionController.java      (Question endpoints)
├── TestController.java          (Test endpoints)
├── CareerRecommendationController.java (Career endpoints)
└── UserController.java          (User endpoints)

security/
├── JwtTokenProvider.java        (Token generation)
├── JwtAuthenticationFilter.java (Token validation)
└── (Added to SecurityConfig)

exception/
├── ResourceNotFoundException.java
├── BadRequestException.java
└── GlobalExceptionHandler.java

Frontend Structure:
──────────────────

pages/
├── Login.jsx                    (Login page)
├── Register.jsx                 (Registration page)
├── StudentDashboard.jsx         (Main student page)
├── TakeTest.jsx                 (Test interface)
├── TestResults.jsx              (Results display)
├── CareerCatalog.jsx            (Career browse)
├── AdminDashboard.jsx           (Admin main page)
├── ManageQuestions.jsx          (Question management)
├── ManageUsers.jsx              (User management)
├── ManageCareers.jsx            (Career management)
└── AdminResults.jsx             (View all results)

components/
├── ProtectedRoute.jsx           (Route protection)
└── Navbar.jsx                   (Navigation)

services/
├── axiosInstance.js             (HTTP client)
├── authService.js               (Auth API calls)
├── questionService.js           (Question API calls)
├── testService.js               (Test API calls)
├── careerService.js             (Career API calls)
└── userService.js               (User API calls)

context/
└── AuthContext.jsx              (Global auth state)

styles/
├── global.css                   (Global utilities)
├── auth.css                     (Auth pages styling)
├── navbar.css                   (Navbar styling)
├── dashboard.css                (Dashboard styling)
├── test.css                     (Test styling)
└── results.css                  (Results styling)
```

## Request/Response Flow

```
Authentication Request:
────────────────────────
POST /auth/login
{
  email: "user@example.com",
  password: "password123"
}
        ↓
Validation in LoginRequest DTO
        ↓
AuthController.login()
        ↓
AuthService.login()
        ↓
1. Authenticate with AuthenticationManager
2. Load user from database
3. Generate JWT token
4. Return AuthResponse with token
        ↓
Response (200 OK)
{
  success: true,
  data: {
    token: "eyJ...",
    id: 1,
    email: "user@example.com",
    role: "STUDENT"
  }
}

Test Submission Request:
─────────────────────────
POST /tests/submit
Headers: Authorization: Bearer <JWT>
{
  testName: "Assessment",
  responses: [
    { questionId: 1, selectedAnswerId: 1 },
    { questionId: 2, selectedAnswerId: 4 }
  ]
}
        ↓
JwtAuthenticationFilter validates token
        ↓
SecurityConfig checks authorization
        ↓
TestController.submitTest()
        ↓
TestService.submitTest()
        ↓
1. Validate responses
2. Calculate correct answers
3. Calculate score percentage
4. Get career recommendations
5. Save TestResult to database
6. Return TestResultDTO
        ↓
Response (201 Created)
{
  success: true,
  data: {
    id: 1,
    testName: "Assessment",
    totalQuestions: 10,
    correctAnswers: 7,
    scorePercentage: 70.0,
    recommendedCareers: [...]
  }
}
```

## State Management (Frontend)

```
Global State (AuthContext):
──────────────────────────
┌─────────────────────────────────┐
│      AuthContext Provider       │
│                                 │
│  ┌─────────────────────────┐   │
│  │ user State              │   │
│  │ ├── id                  │   │
│  │ ├── email               │   │
│  │ ├── fullName            │   │
│  │ ├── role                │   │
│  │ └── token (localStorage) │   │
│  └─────────────────────────┘   │
│                                 │
│  ┌─────────────────────────┐   │
│  │ Methods                 │   │
│  │ ├── login()             │   │
│  │ ├── register()          │   │
│  │ └── logout()            │   │
│  └─────────────────────────┘   │
│                                 │
│  ┌─────────────────────────┐   │
│  │ Computed Values         │   │
│  │ ├── isAuthenticated     │   │
│  │ ├── isAdmin             │   │
│  │ └── isStudent           │   │
│  └─────────────────────────┘   │
└─────────────────────────────────┘
         ↓ useAuth() hook
    All Components Can Access
```

---

**Diagram Type**: ASCII Architecture Diagrams
**Last Updated**: April 6, 2024
