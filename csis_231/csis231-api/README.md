# Verve Planner

Verve Planner is a full-stack Java application for user management, habit tracking, event planning, and task management, featuring a Spring Boot backend with REST APIs and a JavaFX 3D visualization frontend.

---

## Table of Contents

- [Features](#features)  
- [Technologies](#technologies)  
- [Backend](#backend)  
  - [Authentication](#authentication)  
  - [User Management](#user-management)  
  - [Planner Users](#planner-users)  
  - [Habits](#habits)  
  - [Tasks](#tasks)  
  - [Events](#events)  
  - [Messages](#messages)  
  - [Chats](#chats)  
- [Frontend](#frontend)  
  - [Login & Registration](#login--registration)  
  - [3D User Visualization](#3d-user-visualization)  
  - [Track Planner](#track-planner)  
  - [Calendar & Notes](#calendar--notes)  
- [Setup & Run](#setup--run)  
- [API Endpoints](#api-endpoints)  
- [License](#license)

---

## Features

- JWT-based authentication (register, login, validate token).  
- User management with roles (`USER`, `ADMIN`).  
- CRUD operations for habits, tasks, events, messages, and chats.  
- Planner users management.  
- JavaFX frontend with:  
  - 3D user visualization.  
  - Interactive calendar with notes.  
  - Task tracking with streaks and completion progress.  
- Admin access automatically assigned to `admin@gmail.com`.

---

## Technologies

- **Backend:** Java 17, Spring Boot, Spring Security, Spring Data JPA, Hibernate, JWT  
- **Frontend:** JavaFX, FXML, CSS  
- **Database:** H2 / MySQL (configurable)  
- **HTTP Client:** Java 11 HttpClient for frontend-backend communication  
- **JSON Processing:** Jackson  

---

## Backend

### Authentication

- `/api/auth/register` → Register a new user.  
- `/api/auth/login` → Login with username & password.  
- `/api/auth/validate` → Validate JWT token.  
- Admin email: `admin@gmail.com` automatically gets `ADMIN` role.

### User Management

- `/api/users` → CRUD operations on users.  
- Service layer handles role assignment and creation timestamps.

### Planner Users

- `/api/planner-users` → CRUD operations for planner-specific users.

### Habits

- `/api/habits` → CRUD operations for user habits.

### Tasks

- `/api/tasks` → CRUD operations for user tasks.

### Events

- `/api/events` → CRUD operations for events.

### Messages

- `/api/messages` → CRUD operations for messages.

### Chats

- `/api/chats` → CRUD operations for chat conversations.

---

# **Verve Planner – Database Schema Documentation**

## **Tables Overview**

| Table Name      | Description                              |
| --------------- | ---------------------------------------- |
| `users`         | Stores application users                 |
| `planners`      | Stores planner entities (private/team)   |
| `planner_users` | Links users to planners with roles       |
| `habits`        | User habits                              |
| `habit_entries` | Daily entries for habits                 |
| `tasks`         | Tasks within a planner                   |
| `tags`          | Tags for tasks                           |
| `task_tags`     | Many-to-many link between tasks and tags |
| `events`        | Events in planners                       |
| `chats`         | Chat threads within planners             |
| `messages`      | Messages in a chat                       |

---

## **1. Users Table**

| Column Name   | Data Type    | Constraints        | Notes                      |
| ------------- | ------------ | ------------------ | -------------------------- |
| id            | BIGINT       | PK, auto-increment | Primary key                |
| username      | VARCHAR(100) | NOT NULL, UNIQUE   | Login name                 |
| email         | VARCHAR(255) | NOT NULL, UNIQUE   | Email address              |
| password_hash | VARCHAR      | NOT NULL           | Hashed password            |
| role          | VARCHAR(20)  |                    | User role (USER/ADMIN)     |
| created_at    | TIMESTAMP    |                    | Account creation timestamp |

---

## **2. Planners Table**

| Column Name   | Data Type    | Constraints              | Notes                      |
| ------------- | ------------ | ------------------------ | -------------------------- |
| id            | BIGINT       | PK, auto-increment       | Primary key                |
| owner_id      | BIGINT       | NOT NULL, FK → users(id) | Owner of the planner       |
| name          | VARCHAR(200) | NOT NULL                 | Planner name               |
| type          | VARCHAR(20)  |                          | PRIVATE or TEAM            |
| password_hash | VARCHAR      |                          | Optional for team planners |
| created_at    | TIMESTAMP    |                          | Creation timestamp         |

**Relationships:**

* Each `planner` belongs to one `user` (owner).
* Can have multiple `planner_users`.

---

## **3. Planner Users Table**

| Column Name | Data Type   | Constraints                 | Notes                      |
| ----------- | ----------- | --------------------------- | -------------------------- |
| id          | BIGINT      | PK, auto-increment          | Primary key                |
| planner_id  | BIGINT      | NOT NULL, FK → planners(id) | Planner reference          |
| user_id     | BIGINT      | NOT NULL, FK → users(id)    | User reference             |
| role        | VARCHAR(20) |                             | Role inside planner        |
| added_at    | TIMESTAMP   |                             | Timestamp added to planner |

**Relationships:**

* Many-to-many between `users` and `planners` (via `planner_users`).

---

## **4. Habits Table**

| Column Name | Data Type    | Constraints                 | Notes                    |
| ----------- | ------------ | --------------------------- | ------------------------ |
| id          | BIGINT       | PK, auto-increment          | Primary key              |
| planner_id  | BIGINT       | NOT NULL, FK → planners(id) | Planner owning the habit |
| title       | VARCHAR(255) | NOT NULL                    | Habit title              |
| description | TEXT         |                             | Habit description        |
| frequency   | VARCHAR(50)  |                             | Daily/Weekly/Custom      |
| goal_count  | INTEGER      |                             | Target repetitions       |
| created_at  | TIMESTAMP    |                             | Creation timestamp       |

---

## **5. Habit Entries Table**

| Column Name | Data Type | Constraints               | Notes               |
| ----------- | --------- | ------------------------- | ------------------- |
| id          | BIGINT    | PK, auto-increment        | Primary key         |
| habit_id    | BIGINT    | NOT NULL, FK → habits(id) | Habit reference     |
| entry_date  | DATE      | NOT NULL                  | Date of habit entry |
| completed   | BOOLEAN   |                           | Completed or not    |
| created_at  | TIMESTAMP |                           | Timestamp created   |

**Constraints:**

* Unique constraint on `(habit_id, entry_date)`.

---

## **6. Tasks Table**

| Column Name | Data Type    | Constraints                 | Notes                   |
| ----------- | ------------ | --------------------------- | ----------------------- |
| id          | BIGINT       | PK, auto-increment          | Primary key             |
| planner_id  | BIGINT       | NOT NULL, FK → planners(id) | Planner reference       |
| title       | VARCHAR(255) | NOT NULL                    | Task title              |
| description | TEXT         |                             | Task description        |
| status      | VARCHAR(20)  |                             | e.g., TODO, IN_PROGRESS |
| priority    | INTEGER      |                             | Task priority           |
| due_date    | TIMESTAMP    |                             | Task due date           |
| created_at  | TIMESTAMP    |                             | Creation timestamp      |

---

## **7. Tags Table**

| Column Name | Data Type    | Constraints                 | Notes             |
| ----------- | ------------ | --------------------------- | ----------------- |
| id          | BIGINT       | PK, auto-increment          | Primary key       |
| planner_id  | BIGINT       | NOT NULL, FK → planners(id) | Planner reference |
| name        | VARCHAR(100) | NOT NULL                    | Tag name          |

---

## **8. Task Tags Table**

| Column Name | Data Type | Constraints        | Notes          |
| ----------- | --------- | ------------------ | -------------- |
| task_id     | BIGINT    | PK, FK → tasks(id) | Task reference |
| tag_id      | BIGINT    | PK, FK → tags(id)  | Tag reference  |

**Relationships:**

* Many-to-many between `tasks` and `tags`.

---

## **9. Events Table**

| Column Name    | Data Type    | Constraints                 | Notes              |
| -------------- | ------------ | --------------------------- | ------------------ |
| id             | BIGINT       | PK, auto-increment          | Primary key        |
| planner_id     | BIGINT       | NOT NULL, FK → planners(id) | Planner reference  |
| title          | VARCHAR(255) | NOT NULL                    | Event title        |
| description    | TEXT         |                             | Event description  |
| start_datetime | TIMESTAMP    |                             | Event start time   |
| end_datetime   | TIMESTAMP    |                             | Event end time     |
| location       | VARCHAR(255) |                             | Event location     |
| created_at     | TIMESTAMP    |                             | Creation timestamp |

---

## **10. Chats Table**

| Column Name | Data Type | Constraints                 | Notes              |
| ----------- | --------- | --------------------------- | ------------------ |
| id          | BIGINT    | PK, auto-increment          | Primary key        |
| planner_id  | BIGINT    | NOT NULL, FK → planners(id) | Planner reference  |
| created_at  | TIMESTAMP |                             | Creation timestamp |

---

## **11. Messages Table**

| Column Name | Data Type | Constraints              | Notes              |
| ----------- | --------- | ------------------------ | ------------------ |
| id          | BIGINT    | PK, auto-increment       | Primary key        |
| chat_id     | BIGINT    | NOT NULL, FK → chats(id) | Chat reference     |
| sender_id   | BIGINT    | FK → users(id)           | Message sender     |
| content     | TEXT      | NOT NULL                 | Message content    |
| created_at  | TIMESTAMP |                          | Creation timestamp |

---

## **Entity Relationships**

1. **Users → Planners (1:N)**:

    * A user can own multiple planners (`planners.owner_id → users.id`).

2. **Planners → Planner Users (1:N)**:

    * Each planner can have multiple users (`planner_users.planner_id → planners.id`).

3. **Planners → Tasks/Habits/Events/Tags/Chats (1:N)**

4. **Tasks → Tags (M:N)**

    * Via `task_tags` table.

5. **Habits → HabitEntries (1:N)**

    * Each habit can have multiple daily entries.

6. **Chats → Messages (1:N)**

    * Each chat contains multiple messages.

---

## Frontend

### Login & Registration

- Login page (`LoginController`) communicates with backend using HttpClient.  
- Admin login bypasses authentication and opens the 3D user visualization.

### 3D User Visualization

- JavaFX 3D view (`Animation3DUsersController`) shows users as rotating bars with names.  
- Includes camera controls and refresh functionality.

### Track Planner

- Display tasks, progress bar, streak, and water tracker.  
- Buttons for adding tasks and settings.

### Calendar & Notes

- Calendar view with zoomable cells.  
- Click on a day to add/edit notes.

---

## Setup & Run

1. Clone the repository:

```bash
git clone <repo-url>
cd verve-planner
````

2. Configure database in `application.properties` (H2 or MySQL).

3. Build and run Spring Boot backend:

```bash
./mvnw spring-boot:run
```

4. Open JavaFX frontend from IDE or packaged JAR.

5. Admin login: `admin@gmail.com` (bypasses backend authentication).

6. Regular users must register via the frontend.

---

## API Endpoints

### Authentication

| Method | URL                  | Description    |
| ------ | -------------------- | -------------- |
| POST   | `/api/auth/register` | Register user  |
| POST   | `/api/auth/login`    | Login          |
| GET    | `/api/auth/validate` | Validate token |

### Users

| Method | URL               | Description    |
| ------ | ----------------- | -------------- |
| GET    | `/api/users`      | Get all users  |
| POST   | `/api/users`      | Create user    |
| GET    | `/api/users/{id}` | Get user by ID |
| PUT    | `/api/users/{id}` | Update user    |
| DELETE | `/api/users/{id}` | Delete user    |

### Planner Users

| Method | URL                       | Description            |
| ------ | ------------------------- | ---------------------- |
| GET    | `/api/planner-users`      | Get all planner users  |
| POST   | `/api/planner-users`      | Create planner user    |
| GET    | `/api/planner-users/{id}` | Get planner user by ID |
| PUT    | `/api/planner-users/{id}` | Update planner user    |
| DELETE | `/api/planner-users/{id}` | Delete planner user    |

### Habits, Tasks, Events, Messages, Chats

* Each entity supports standard CRUD endpoints with `/api/{entity}`.
* Example: `/api/tasks`, `/api/events`, `/api/messages`, `/api/chats`.

---

## License

MIT License © 2025 Verve Planner
