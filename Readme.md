# Corporate Banking & Client Management System

A full-stack role-based corporate banking workflow system built with **Java Spring Boot**, **MongoDB**, **React**, and **JWT Security** supporting 3 roles:

- **Relationship Manager (RM)**
- **Analyst**
- **Admin**

---

## 📌 1. PROJECT OVERVIEW

This system simulates a corporate banking process where:

- RM registers corporate clients and submits credit requests
- Analyst evaluates the requests and approves/rejects them
- Admin manages user accounts, roles, and visibility

The system demonstrates authorization, workflows, security, UI dashboards, and real-world business logic.

---

## 🧱 2. TECHNOLOGY STACK

| Layer | Tech |
|---|---|
| Frontend | React, TypeScript, Vite, Material UI |
| Backend | Spring Boot, Spring Security, JWT |
| Database | MongoDB |
| Build Tools | Maven, NPM |
| Optional | Docker, Postman |

---

## 🔐 3. AUTHENTICATION & SECURITY

- **JWT-based Authentication**
- **Role-Based Access Control (RBAC)**
- **Stateless Sessions**
- **BCrypt Password Encoding**
- **CORS Enabled**

JWT Token Format:


---

## 👥 4. USER ROLES & PERMISSIONS

| Feature                     | RM | Analyst | Admin  |
|---------------              |----|---------|------  | 
| Create Client               | ✅ |    ❌  | ❌     | 
| Submit Credit Request       | ✅ |    ❌  | ❌     |
| View Requests               | ❌ |    ✅  | ❌     |
| Approve/Reject Requests     | ❌ |    ✅  | ❌     |
| Manage Users                | ❌ |    ❌  | ✅     |

---

## 🗄 5. DATABASE DOCUMENTS (MongoDB)

### `users` Collection
```json
{
  "_id": "...",
  "username": "rm",
  "password": "hashed",
  "role": "RM",
  "active": true
}
clients Collection
{
  "companyName": "ABC Textiles Ltd",
  "industry": "Manufacturing",
  "annualTurnover": 25.5,
  "documentsSubmitted": true
}

creditRequests Collection
{
  "clientId": "...",
  "status": "PENDING/APPROVED/REJECTED",
  "remarks": "Strong Balance Sheet"
}

📡 6. REST API ENDPOINTS
Auth
POST /api/auth/login
POST /api/auth/register
GET  /api/auth/me

RM APIs
POST /api/rm/clients
POST /api/rm/credit-requests
GET  /api/rm/credit-requests

Analyst APIs
GET  /api/analyst/credit-requests
PUT  /api/analyst/credit-requests/{id}

Admin APIs
GET  /api/admin/users
PUT  /api/admin/users/{id}/status

🧪 7. TESTING WITH POSTMAN

Sample login:

{
  "username": "rm",
  "password": "rm1234"
}


Token usage:

Authorization: Bearer <token>


Demo user accounts:

User	  Password	    Role
rm   	  rm1234	     RM
analyst	  analyst123	ANALYST
admin	  admin123	    ADMIN
🏦 8. WORKFLOW DEMO
Step 1 — Login as RM

✔ Create Client
✔ Submit Credit Request

Step 2 — Login as Analyst

✔ Review Request
✔ Approve/Reject

Step 3 — Login as Admin

✔ View All Users
✔ Enable/Disable User

🖥️ 9. FRONTEND FLOW (React)

After login:

Role	Redirect
RM	/rm/dashboard
Analyst	/analyst/dashboard
Admin	/admin/dashboard

Protected using React Context + JWT.

🐳 10. DOCKER SUPPORT (Optional)

Backend:

docker build -t banking-backend .
docker run -p 8080:8080 banking-backend


Frontend:

npm run build
docker build -t banking-frontend .
docker run -p 3000:80 banking-frontend

📦 11. PROJECT STRUCTURE
corporate-banking/
 ├── backend/
 │   ├── src/
 │   └── pom.xml
 ├── frontend/
 │   ├── src/
 │   └── package.json
 └── README.md