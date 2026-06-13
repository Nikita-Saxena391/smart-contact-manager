# 📇 Smart Contact Manager

A full-stack **Spring Boot web application** to manage personal contacts securely with authentication, image upload, and a clean user interface.

---

## 🚀 Features

* 🔐 Secure login & signup (Spring Security)
* 👤 Add, update, delete contacts
* 📸 Upload contact profile images
* 🔍 Search and manage contacts efficiently
* 📱 Responsive UI using Thymeleaf
* 🗂️ User-specific contact management

---

## 🛠️ Tech Stack

* **Backend:** Java, Spring Boot
* **Frontend:** HTML, CSS, JavaScript, Thymeleaf
* **Database:** MySQL
* **Security:** Spring Security
* **Build Tool:** Maven

---

## 📂 Project Structure

```
smartcontactmanager/
│── src/
│   ├── main/
│   │   ├── java/com/smart/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dao/
│   │   │   ├── entities/
│   │   │   ├── helper/
│   │   ├── resources/
│   │       ├── templates/
│   │       ├── static/
│── pom.xml
│── mvnw
│── mvnw.cmd
```

---

## ⚙️ Setup Instructions

### 1️⃣ Clone the Repository

```
git clone https://github.com/Nikita-Saxena391/smart-contact-manager.git
cd smart-contact-manager
```

### 2️⃣ Configure Database

Update `application.properties`:

```
spring.datasource.url=jdbc:mysql://localhost:3306/smartcontact
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

---

### 3️⃣ Run the Application

Using Maven:

```
mvn spring-boot:run
```

---

### 4️⃣ Access the App

Open in browser:

```
http://localhost:8080
```



## 🌟 Future Improvements

* 📧 Email integration
* 🤖 AI chatbot integration (Spring AI / Ollama)
* ☁️ Cloud deployment (AWS / Render)

---

## 🤝 Contributing

Contributions are welcome!
Fork the repo and submit a pull request.

---
