# Quick Start Guide - Career Assessment System

Get up and running in 5 minutes!

## Prerequisites
- Java 17+ installed
- MySQL 8.0+ running
- Node.js 16+ installed
- Port 8080 and 3000 available

## 1. Database Setup (1 minute)

```bash
# Open MySQL and run:
mysql -u root -p < docs/database_schema.sql

# Or manually:
# 1. Open MySQL Workbench or mysql cli
# 2. Run contents of docs/database_schema.sql
```

## 2. Start Backend (2 minutes)

```bash
# Terminal 1
cd career-assessment-backend

# Install and run
mvn clean install
mvn spring-boot:run

# Wait for: "Started CareerAssessmentApplication..."
# Backend runs at http://localhost:8080/api
```

## 3. Start Frontend (1 minute)

```bash
# Terminal 2
cd career-assessment-frontend

# Install and run
npm install
npm run dev

# Frontend opens at http://localhost:3000
```

## 4. Login & Test

**Admin Account:**
- Email: `admin@assessment.com`
- Password: `admin123`

**First Time:**
1. Login as admin
2. Go to `/admin/dashboard`
3. Create questions and careers
4. Logout and register as student
5. Take test and view results

## Quick Commands

### Backend
```bash
# Build
mvn clean install

# Run
mvn spring-boot:run

# Run tests
mvn test

# Build JAR
mvn clean package
```

### Frontend
```bash
# Install dependencies
npm install

# Dev server
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview
```

## File Locations

- Database schema: `docs/database_schema.sql`
- Backend config: `career-assessment-backend/src/main/resources/application.yml`
- Frontend config: `career-assessment-frontend/src/services/axiosInstance.js`
- API docs: `docs/API_DOCUMENTATION.md`
- Full docs: `docs/README.md`

## Common Issues

**Backend won't start:**
```bash
# Check MySQL is running
# Check port 8080 is free
# Check application.yml database config
lsof -i :8080  # Check port usage
```

**Frontend won't connect:**
```bash
# Check backend is running: http://localhost:8080/api
# Clear browser cache: Ctrl+Shift+Delete
# Check CORS: Should allow localhost:3000
```

**Database errors:**
```bash
# Check MySQL is running
# Verify credentials in application.yml
# Re-run the schema file
```

## API Test (Using curl)

```bash
# Test backend
curl http://localhost:8080/api/questions/public/all

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@assessment.com","password":"admin123"}'
```

## File Structure

```
Backend Layers:
Controller → Service → Repository → Database

Frontend Structure:
pages/ → components/ → services/ → context → API
```

## Key Endpoints

| Path | Method | Purpose |
|------|--------|---------|
| `/auth/login` | POST | User login |
| `/auth/register` | POST | User registration |
| `/questions/public/random` | GET | Get random questions |
| `/tests/submit` | POST | Submit test |
| `/tests/my-results` | GET | View results |
| `/admin/users` | GET | Manage users |

## Development Tips

1. **Backend Development:**
   - Make changes to Java files
   - Maven auto-reloads (devtools enabled)
   - Check `application.yml` for logging level

2. **Frontend Development:**
   - Changes hot-reload automatically
   - Check browser console for errors
   - Use React DevTools extension

3. **Database:**
   - Use MySQL Workbench for inspection
   - Don't modify schema directly
   - Backup before testing

## Production Checklist

- [ ] Change JWT secret in `application.yml`
- [ ] Update database credentials
- [ ] Set appropriate logging levels
- [ ] Build frontend: `npm run build`
- [ ] Build backend: `mvn package`
- [ ] Test on staging first
- [ ] Setup HTTPS/SSL
- [ ] Configure firewall
- [ ] Setup automated backups

## Next Steps

1. **Explore**
   - Navigate around the application
   - Create test questions
   - Take a test
   - Review admin features

2. **Customize**
   - Update career data
   - Modify scoring algorithm
   - Add more questions
   - Adjust UI styling

3. **Extend**
   - Add more features
   - Implement analytics
   - Setup email notifications
   - Add more validations

## Support

- Full README: `docs/README.md`
- API Docs: `docs/API_DOCUMENTATION.md`
- Database Schema: `docs/database_schema.sql`
- Backend Logs: `target/logs/`
- Browser Console: F12 in browser

## Testing Account Creation

1. Go to `http://localhost:3000/register`
2. Fill in:
   - Name: Any name
   - Email: yourname@example.com
   - Password: test123456
3. Click Register
4. Auto-login and redirect to dashboard

## Troubleshoot Commands

```bash
# Check if MySQL is running
mysql -u root -p -e "SELECT 1"

# Check Java version
java -version

# Check Node version
node -v

# Check ports
lsof -i :8080
lsof -i :3000

# Kill process on port
kill -9 $(lsof -t -i :8080)
```

---

**Version**: 1.0.0  
**Last Updated**: April 6, 2024  
**Status**: Production Ready
