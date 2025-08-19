# Histology Application

A Spring Boot web application for managing users, jobs, and histology data.

---

## Getting Started

### 1. Prerequisites
- **Java 17 or higher** ([Download OpenJDK](https://adoptium.net/))
- (Optional for developers) **Maven 3.8+** ([Download Maven](https://maven.apache.org/download.cgi))

### 2. Running the Application (Recommended for End Users)
1. **Obtain the JAR file:**
   - `histology-0.0.1-SNAPSHOT.jar` (located in the `target/` directory)
2. **(Optional) Edit Configuration:**
   - If you need to change settings (like database connection), edit `src/main/resources/application.properties` or provide your own `application.properties` in the same directory as the JAR.
3. **Run the Application:**
   ```sh
   java -jar histology-0.0.1-SNAPSHOT.jar
   ```
4. **Access the App:**
   - Open your browser and visit: [http://localhost:8080](http://localhost:8080)

### 3. Building from Source (For Developers)
1. **Clone the repository:**
   ```sh
   git clone <your-repo-url>
   cd histology
   ```
2. **Build the project:**
   ```sh
   ./mvnw clean package -DskipTests
   ```
   - The JAR will be in `target/histology-0.0.1-SNAPSHOT.jar`

### 4. Database
- By default, the app uses the configuration in `application.properties`.
- If you use an external DB (e.g., PostgreSQL, MySQL), update the connection settings accordingly.

### 5. Customization
- For advanced configuration, see the `application.properties` file.
- You can override properties by passing `--spring.config.location=...` to the `java -jar` command.

### 6. Troubleshooting
- Ensure Java 17+ is installed: `java -version`
- Check logs in the terminal for startup errors.
- Make sure port 8080 is not in use.

---

## Git Workflow

### Add and Commit Changes
```sh
git add README.md
git commit -m "Add README with setup and usage instructions"
git push
```

---

## License
This project is proprietary and confidential unless otherwise specified.
