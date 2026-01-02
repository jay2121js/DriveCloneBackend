<div align="center">

<h1>📂 DriveClone Backend</h1>

<p><strong>A backend service for a cloud file storage application (DriveClone)</strong></p>

<p>
  Spring Boot · Java · MongoDB · JWT · OAuth2 · Cloudinary
</p>

</div>

<hr/>

<h2>🌟 Overview</h2>

<p>
The <strong>DriveClone Backend</strong> is a RESTful API built with Spring Boot that provides backend support for a Google Drive–style application.  
It handles authentication, file and folder management, uploads to Cloudinary, user profiles, and secure API access for frontend clients.
</p>

<hr/>

<h2>✨ Features</h2>

<ul>
  <li>User authentication and authorization with JWT</li>
  <li>Google OAuth2 login integration</li>
  <li>File upload and download handling</li>
  <li>Folder and file organization</li>
  <li>Cloudinary integration for media storage</li>
  <li>Role-based access and permissions</li>
  <li>Email support via SMTP</li>
</ul>

<hr/>

<h2>🛠️ Tech Stack</h2>

<table border="1" cellpadding="8" cellspacing="0">
  <tr>
    <th>Component</th>
    <th>Technology</th>
  </tr>
  <tr>
    <td>Framework</td>
    <td>Spring Boot</td>
  </tr>
  <tr>
    <td>Language</td>
    <td>Java</td>
  </tr>
  <tr>
    <td>Database</td>
    <td>MongoDB</td>
  </tr>
  <tr>
    <td>Security</td>
    <td>Spring Security, JWT, OAuth2</td>
  </tr>
  <tr>
    <td>File Storage</td>
    <td>Cloudinary</td>
  </tr>
  <tr>
    <td>Build Tool</td>
    <td>Maven</td>
  </tr>
</table>

<hr/>

<h2>🚀 Getting Started</h2>

<h3>Clone the Repository</h3>
<pre>
git clone https://github.com/jay2121js/DriveCloneBackend.git
cd DriveCloneBackend
</pre>

<h3>Install Dependencies</h3>
<pre>
mvn clean install
</pre>

<h3>Run Locally</h3>
<pre>
mvn spring-boot:run
</pre>

Your backend will start on:
<code>http://localhost:8080</code>

---

<h2>🔐 Environment Variables (Required)</h2>

<p>Create <code>src/main/resources/application.properties</code> (ignored by Git) and define the following:</p>

<pre>
# Application
spring.application.name=DriveCloneBackend
frontend.uri=https://your-frontend-url.com

# MongoDB
spring.data.mongodb.uri=
spring.data.mongodb.database=DriveClone

# Cloudinary
cloudinary.cloud-name=
cloudinary.api-key=
cloudinary.api-secret=

# Email SMTP
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=
spring.mail.password=

# OAuth2 - Google
spring.security.oauth2.client.registration.google.client-id=
spring.security.oauth2.client.registration.google.client-secret=

# JWT
jwt.secret=
</pre>

> **Note:** Never commit sensitive credentials. Use environment variables or a secure secrets manager.

---

<hr/>

<h2>🌐 API Endpoints</h2>

Below are common backend endpoints. Add more detail if you have OpenAPI or Swagger.

<h3>Authentication</h3>
<pre>
POST /api/auth/login
POST /api/auth/register
GET  /api/auth/google
POST /api/auth/logout
</pre>

<h3>User Management</h3>
<pre>
GET    /api/user/profile
PUT    /api/user/update
DELETE /api/user/delete
</pre>

<h3>Folder & File Operations</h3>
<pre>
GET    /api/files/all
POST   /api/files/upload
GET    /api/files/download/{id}
DELETE /api/files/delete/{id}
</pre>

---

<hr/>

<h2>📁 Project Structure</h2>

<pre>
DriveCloneBackend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/driveclone/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       ├── entity/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       └── DriveCloneBackendApplication.java
│   ├── resources/
│   │   └── application.properties.example
├── pom.xml
└── README.md
</pre>

---

<hr/>

<h2>🤝 Contributing</h2>

<ul>
  <li>Fork the repository</li>
  <li>Create a feature branch</li>
  <li>Write clean, tested code</li>
  <li>Submit a Pull Request</li>
</ul>

All contributions are welcome.

---

<hr/>

<h2>💬 Support & Contact</h2>

<ul>
  <li>Email: <a href="mailto:jay21213.js@gmail.com">jay21213.js@gmail.com</a></li>
  <li>Open GitHub Issues for bug reports and feature requests</li>
</ul>

---

<div align="center">
  <p><strong>⭐ If you find this project helpful, please give it a star</strong></p>
  <p>Made by Jay Soni</p>
</div>
