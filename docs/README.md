# Career Assessment System - Complete Setup Guide

A production-level full-stack web application for career assessment and recommendation.

**Tech Stack:**
- Frontend: React 18 + Vite
- Backend: Spring Boot 3.1.5
- Database: MySQL 8
- Authentication: JWT

## Project Structure

```
career-assessment-system/
├── career-assessment-backend/      # Spring Boot Backend
│   ├── src/main/java/com/careers/assessment/
│   │   ├── config/                # Security & configuration
│   │   ├── controller/            # REST endpoints
│   │   ├── entity/                # JPA entities
│   │   ├── dto/                   # Data Transfer Objects
│   │   ├── service/               # Business logic
│   │   ├── repository/            # Data access layer
│   │   ├── security/              # JWT & authentication
│   │   └── exception/             # Error handling
│   ├── src/main/resources/
│   │   └── application.yml        # Configuration
│   └── pom.xml                    # Maven dependencies
│
├── career-assessment-frontend/     # React Frontend
│   ├── src/
│   │   ├── components/            # Reusable components
│   │   ├── pages/                 # Page components
│   │   ├── services/              # API services
│   │   ├── context/               # React context
│   │   ├── styles/                # CSS files
│   │   ├── hooks/                 # Custom hooks
│   │   ├── App.jsx                # Main app component
│   │   └── main.jsx               # Entry point
│   ├── package.json
│   ├── vite.config.js
│   └── index.html
│
└── docs/
    ├── database_schema.sql        # MySQL schema
    └── API_DOCUMENTATION.md       # API endpoints
```

## Features

### Student Features
- ✅ User Registration & Login
- ✅ Take Dynamic MCQ Tests (questions from database)
- ✅ Submit Answers & Get Scores
- ✅ Career Recommendations Based on Answers
- ✅ View Previous Test Results
- ✅ Explore Career Catalog

### Admin Features
- ✅ Add/Edit/Delete Questions
- ✅ Manage Users (Activate/Deactivate)
- ✅ View All Student Results
- ✅ Manage Career Recommendations
- ✅ Update Career Information

### Technical Features
- ✅ JWT-based Authentication
- ✅ Role-based Access Control (Student/Admin)
- ✅ Layered Architecture (Controller-Service-Repository)
- ✅ Exception Handling & Validation
- ✅ CORS Configuration
- ✅ Responsive UI

## Setup Instructions

### Prerequisites
- Java 17+
- Node.js 16+
- MySQL 8.0+
- Maven 3.8+
- npm or yarn

### 1. Database Setup

```bash
# Open MySQL and run the schema file
mysql -u root -p < docs/database_schema.sql

# Or manually in MySQL CLI:
# CREATE DATABASE career_assessment_db;
# INSERT users, questions, answers...
```

### 2. Backend Setup

```bash
cd career-assessment-backend

# Install dependencies
mvn clean install

# Run the application
mvn spring-boot:run

# Backend will start on http://localhost:8080/api
```

**Backend Configuration (application.yml):**
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/career_assessment_db
    username: root
    password: root
    
jwt:
  secret: your-super-secret-key-change-this-in-production
  expiration: 86400000  # 24 hours
```

### 3. Frontend Setup

```bash
cd career-assessment-frontend

# Install dependencies
npm install

# Start development server
npm run dev

# Frontend will start on http://localhost:3000
```

**Frontend Configuration (axiosInstance.js configures backend URL):**
```javascript
const API_BASE_URL = 'http://localhost:8080/api';
```

## API Endpoints

### Authentication
```
POST   /auth/register          Register new student
POST   /auth/login             Login and get JWT token
POST   /auth/validate-token    Validate JWT token
```

### Questions (Public)
```
GET    /questions/public/all           Get all active questions
GET    /questions/public/random        Get random questions
GET    /questions/public/category/:category  Get questions by category
```

### Questions (Admin)
```
POST   /questions/admin               Create question
PUT    /questions/admin/:id           Update question
DELETE /questions/admin/:id           Deactivate question
DELETE /questions/admin/permanent/:id Delete permanently
```

### Tests
```
POST   /tests/submit                  Submit test answers
GET    /tests/my-results              Get student's results
GET    /tests/result/:id              Get specific result
GET    /tests/admin/all               Get all results (Admin)
```

### Careers (Public)
```
GET    /careers/public/all            Get all careers
GET    /careers/public/:id            Get career by ID
GET    /careers/public/search         Search career by name
```

### Careers (Admin)
```
POST   /careers/admin                 Create career
PUT    /careers/admin/:id             Update career
DELETE /careers/admin/:id             Deactivate career
DELETE /careers/admin/permanent/:id   Delete permanently
```

### Users (Admin)
```
GET    /admin/users                   Get all users
GET    /admin/users/:id               Get user by ID
GET    /admin/users/email/:email      Get user by email
PUT    /admin/users/:id/activate      Activate user
PUT    /admin/users/:id/deactivate    Deactivate user
PUT    /admin/users/:id/role          Update user role
DELETE /admin/users/:id               Delete user
```

## Test Credentials

**Admin Login:**
- Email: `admin@assessment.com`
- Password: `admin123`

**Sample Student Registration:**
- Create new account with any email/password (min 6 characters)

## Default Test Data

The database includes:
- 5 Sample Career Recommendations
- 5 Sample Questions
- 8 Sample Answers

## Component Architecture

### Frontend Components
- `AuthContext`: Global authentication state
- `ProtectedRoute`: Route protection for authenticated users
- `Navbar`: Navigation and user menu
- `Login/Register`: Authentication pages
- `StudentDashboard`: Main student interface
- `TakeTest`: Test interface with MCQ
- `TestResults`: Result display and history
- `AdminDashboard`: Admin control center
- `ManageQuestions/Users/Careers`: Admin management pages

### Backend Layers
- **Controller**: HTTP request handling
- **Service**: Business logic & validation
- **Repository**: Database queries
- **Entity**: JPA models
- **DTO**: Data transfer between layers
- **Security**: JWT & authentication
- **Exception**: Global error handling

## Security Features

1. **JWT Authentication**: Stateless token-based auth
2. **Password Encryption**: BCrypt hashing
3. **Role-based Access Control**: Student/Admin roles
4. **CORS Configuration**: Restricted origins
5. **Request Validation**: Input validation on all endpoints
6. **Exception Handling**: Centralized error responses

## Scoring Algorithm

```javascript
// Test Score Calculation:
scorePercentage = (correctAnswers / totalQuestions) * 100

// Career Recommendation:
matchPercentage = (scorePercentage * 0.6) + (careerMapping * 40)
```

## Error Handling Examples

```json
{
  "success": false,
  "message": "User not found with email: test@example.com",
  "statusCode": 404,
  "timestamp": "2024-04-06T10:30:00"
}
```

## Validation Examples

### Login
- Email: Valid email format
- Password: Minimum 6 characters

### Register
- Full Name: 2-100 characters
- Email: Valid email format
- Password: 6-50 characters
- Confirm Password: Must match password

### Question Creation
- Question Text: Required
- Category: Required
- Difficulty: 1-3
- Answers: At least one correct answer

## Database Schema Highlights

**Users Table**
- Stores student and admin accounts
- Role-based access control
- Account status (active/inactive)

**Questions Table**
- Question text and category
- Difficulty levels (1-3)
- Soft delete support

**Answers Table**
- Multiple choice options
- Correct answer marking
- Career path mapping

**TestResults Table**
- Student responses and scores
- Recommended careers (JSON)
- Answer details (JSON)

**CareerRecommendations Table**
- Career information
- Skills, salary, outlook
- Related universities

## Performance Optimization

1. **Lazy Loading**: JPA lazy fetch in relationships
2. **Pagination**: Ready for implementation
3. **Caching**: Can be added with Spring Cache
4. **Database Indexing**: Indexes on frequently queried columns

## Future Enhancements

1. Email notifications
2. Admin analytics dashboard
3. Export results as PDF
4. Interview preparation recommendations
5. Progress tracking over time
6. Mobile app (React Native)
7. Advanced recommendation engine
8. AI-powered career suggestions
9. University integration
10. Job portal integration

## Troubleshooting

**Backend won't start:**
- Check MySQL connection
- Verify `application.yml` configuration
- Check port 8080 is available

**Frontend won't connect to backend:**
- Verify backend is running on port 8080
- Check CORS configuration
- Clear browser cache

**Database errors:**
- Ensure database exists and is running
- Check user credentials in `application.yml`
- Run schema file again

**Token errors:**
- JWT secret in backend matches configuration
- Token expiration time is set correctly
- Clear localStorage and re-login

## Building for Production

**Backend:**
```bash
mvn clean package
java -jar target/assessment-system-1.0.0.jar
```

**Frontend:**
```bash
npm run build
# Serves optimized build from dist/ folder
```

## License

This project is for educational purposes.

## Support

For issues or questions, refer to the database schema and API documentation files.

---

**Created**: April 6, 2024  
**Version**: 1.0.0  
**Status**: Production Ready
