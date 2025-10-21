# Spring Boot Crash Course - Notes Application

A full-stack notes management application built with Kotlin, Spring Boot, MongoDB, and JWT authentication. Features both a RESTful API and a server-side rendered web interface (MPA).

## Features

### Core Functionality
- 📝 **CRUD Operations**: Create, Read, Update, and Delete notes
- 🔐 **JWT Authentication**: Secure token-based authentication with access and refresh tokens
- 🎨 **Colorful Notes**: 5 predefined color options for better organization
- 👤 **User Management**: Secure registration and login system
- 🔄 **Dual Interface**: RESTful API for programmatic access and Web UI for browser access

### Technical Features
- **Backend**: Kotlin + Spring Boot 3.4.3
- **Database**: MongoDB with Spring Data
- **Security**: JWT tokens with 15-minute access tokens and 30-day refresh tokens
- **View Layer**: Thymeleaf templates for server-side rendering
- **Cookie Management**: HttpOnly cookies for secure token storage in web interface
- **Validation**: Jakarta Bean Validation with custom error handling

## Architecture

### REST API Endpoints
- `POST /auth/register` - Register a new user
- `POST /auth/login` - Login and receive JWT tokens
- `POST /auth/refresh` - Refresh access token
- `GET /notes` - Get all notes for authenticated user
- `POST /notes` - Create or update a note
- `DELETE /notes/{id}` - Delete a note

### Web Interface Routes
- `GET /web/home` - Landing page
- `GET /web/register` - Registration page
- `POST /web/register` - Process registration
- `GET /web/login` - Login page
- `POST /web/login` - Process login (sets JWT cookies)
- `GET /web/notes` - Notes management page (protected)
- `POST /web/notes` - Create/update note via form
- `POST /web/notes/{id}/delete` - Delete note
- `POST /web/logout` - Logout (clears cookies)

## Getting Started

### Prerequisites
- Java 17 or higher
- MongoDB instance (local or Atlas)
- Gradle (wrapper included)

### Environment Variables
```bash
MONGODB_CONNECTION_STRING=mongodb://localhost:27017/spring_crash_course
JWT_SECRET_BASE64=<your-base64-encoded-secret>
```

### Running the Application

1. **Clone the repository**
```bash
git clone https://github.com/sebaslagu/SpringBootCrashCourse.git
cd SpringBootCrashCourse
```

2. **Set environment variables**
```bash
export MONGODB_CONNECTION_STRING="mongodb://localhost:27017/spring_crash_course"
export JWT_SECRET_BASE64="<your-secret>"
```

3. **Run with Gradle**
```bash
./gradlew bootRun
```

4. **Access the application**
- Web Interface: http://localhost:8085/web/home
- API: http://localhost:8085/

### Docker Setup (MongoDB)
```bash
docker run -d --name mongodb-notes -p 27017:27017 mongo:latest
```

## Project Structure

```
src/main/
├── kotlin/com/plcoding/spring_boot_crash_course/
│   ├── controllers/
│   │   ├── AuthController.kt          # REST API authentication
│   │   ├── NoteController.kt          # REST API notes CRUD
│   │   ├── StatusController.kt        # Health check endpoint
│   │   └── web/
│   │       ├── WebHomeController.kt   # Web home page
│   │       ├── WebAuthController.kt   # Web auth pages
│   │       └── WebNoteController.kt   # Web notes pages
│   ├── database/
│   │   ├── model/
│   │   │   ├── User.kt
│   │   │   ├── Note.kt
│   │   │   └── RefreshToken.kt
│   │   └── repository/              # MongoDB repositories
│   └── security/
│       ├── SecurityConfig.kt        # Spring Security configuration
│       ├── JwtAuthFilter.kt         # JWT validation filter
│       ├── JwtService.kt            # JWT token management
│       ├── AuthService.kt           # Authentication service
│       └── HashEncoder.kt           # Password hashing
└── resources/
    ├── templates/                   # Thymeleaf templates
    │   ├── home.html
    │   ├── login.html
    │   ├── register.html
    │   └── notes.html
    ├── static/
    │   ├── css/
    │   │   └── styles.css
    │   └── js/
    │       ├── notes.js
    │       └── auth.js
    └── application.properties
```

## Security

### Authentication Flow

**API Authentication:**
1. Client calls `/auth/login` with credentials
2. Server returns `accessToken` and `refreshToken` in JSON response
3. Client includes `Authorization: Bearer <accessToken>` header in subsequent requests
4. When access token expires, client calls `/auth/refresh` with refresh token

**Web Authentication:**
1. User submits login form to `/web/login`
2. Server validates credentials and generates tokens
3. Tokens are stored in HttpOnly cookies:
   - `access_token`: 15-minute expiry
   - `refresh_token`: 30-day expiry
4. Cookies are automatically sent with subsequent requests
5. `JwtAuthFilter` validates tokens from either cookies or Authorization header

### Password Requirements
- Minimum 9 characters
- At least one uppercase letter
- At least one lowercase letter
- At least one digit

### Security Notes
- **CSRF Protection**: Currently disabled for simplicity. In production, consider enabling CSRF protection for web forms while exempting API endpoints.
- **Cookie Security**: Cookies use `HttpOnly` flag to prevent JavaScript access. In production, also set `Secure` flag to require HTTPS.
- **Token Storage**: Refresh tokens are hashed before storage in MongoDB.
- **Stateless Sessions**: Application uses stateless JWT authentication; no server-side session storage.

## API Usage Examples

### Register a User
```bash
curl -X POST http://localhost:8085/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"Password123"}'
```

### Login
```bash
curl -X POST http://localhost:8085/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"Password123"}'
```

Response:
```json
{
  "accessToken": "eyJhbGc...",
  "refreshToken": "eyJhbGc..."
}
```

### Create a Note
```bash
curl -X POST http://localhost:8085/notes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-access-token>" \
  -d '{
    "title":"Shopping List",
    "content":"Buy milk and eggs",
    "color":4293848814
  }'
```

### Get All Notes
```bash
curl http://localhost:8085/notes \
  -H "Authorization: Bearer <your-access-token>"
```

## Development

### Building
```bash
./gradlew build
```

### Running Tests
```bash
./gradlew test
```

### Code Style
The project follows Kotlin coding conventions with Spring Boot best practices.

## Technologies Used

- **Kotlin** 1.9.25
- **Spring Boot** 3.4.3
- **Spring Security** with JWT
- **Spring Data MongoDB**
- **Thymeleaf** template engine
- **MongoDB** for data persistence
- **JJWT** 0.12.6 for JWT handling
- **Gradle** for build management

## Color Options

Notes can be assigned one of five colors:
- 🟡 Peach (`4294951115`)
- 🩷 Pink (`4293848814`)
- 🟢 Green (`4289374890`)
- 🟣 Purple (`4292149695`)
- ⚪ White (`4278255615`)

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is open source and available for educational purposes.

## Author

Based on the Spring Boot Crash Course by Philipp Lackner.
MPA layer implementation by GitHub Copilot.

## Acknowledgments

- Spring Boot team for the excellent framework
- MongoDB for the database
- Thymeleaf for the template engine
