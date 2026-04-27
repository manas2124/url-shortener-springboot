# url-shortener-springboot
# 🔗 URL Shortener (Spring Boot + MongoDB)

A full-stack URL Shortener application that converts long URLs into short links, tracks analytics, and handles expiry.

---

## 🚀 Features

- Generate short URLs (6–8 character slug)
- Idempotency (same URL → same slug until expiry)
- Redirect to original URL
- Analytics (track number of clicks)
- Expiry handling (links expire after a fixed time)
- Auto-delete expired links
- Live countdown timer (frontend)
- Real-time analytics update
- URL validation
- Clear button for better UX
---

## 🛠 Tech Stack

- **Backend:** Spring Boot (Java)
- **Database:** MongoDB
- **Frontend:** HTML, CSS, JavaScript
- **Build Tool:** Maven

---

## 📂 Project Structure
src/
└── main/
├── java/com/manas/urlshortener/
│ ├── controller/
│ ├── service/
│ ├── repository/
│ ├── model/
│ └── util/
└── resources/
├── static/
│ ├── index.html
│ ├── script.js
│ └── style.css
└── application.properties

---

## ⚙️ How It Works

1. User enters a long URL
2. Backend validates URL
3. System checks if URL already exists
4. Generates unique slug
5. Stores data in MongoDB
6. Returns short URL
7. Clicking short URL redirects to original URL
8. Access count increases for analytics

---

## ⏳ Expiry Logic

- Each URL has an expiry time (default: 5 minutes)
- After expiry:
  - Link becomes invalid
  - Entry is removed from database

---

## 📊 Analytics

- Tracks how many times each URL is accessed
- Automatically updates when user returns to the page

---

## 🧠 Key Concepts Used

- Idempotent API design
- Base62 slug generation
- Expiry lifecycle management
- MongoDB integration
- Frontend-backend synchronization
- Page Visibility API for auto-refresh

---

## ▶️ How to Run

1. Start MongoDB
net start MongoDB
2. Run Spring Boot
mvn spring-boot:run
3. Open in browser
http://localhost:8080
