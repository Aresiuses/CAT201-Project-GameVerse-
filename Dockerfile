# STAGE 1: Compilation
# Switched to eclipse-temurin as the openjdk-slim image is often unavailable or deprecated
FROM eclipse-temurin:11-jdk AS build

# Create build directory
WORKDIR /app

# Copy the libraries and source code
COPY ./lib/ /app/lib/
COPY ./src/ /app/src/

# Compile the Java code manually
# We include all jars in the lib folder in the classpath
RUN mkdir -p /app/out && \
    javac -d /app/out -cp "lib/*" $(find src -name "*.java")

# STAGE 2: Run in Tomcat
FROM tomcat:9.0-jdk11-openjdk-slim

# Remove default apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Create ROOT directory
RUN mkdir -p /usr/local/tomcat/webapps/ROOT

# 1. Copy web content (HTML/JS/CSS)
COPY ./web/ /usr/local/tomcat/webapps/ROOT/

# 2. Copy compiled classes from the build stage
COPY --from=build /app/out/ /usr/local/tomcat/webapps/ROOT/WEB-INF/classes/

# 3. Copy libraries to the server
COPY ./lib/ /usr/local/tomcat/webapps/ROOT/WEB-INF/lib/

# 4. Copy web.xml to the correct location
COPY ./web/WEB-INF/web.xml /usr/local/tomcat/webapps/ROOT/WEB-INF/

# 5. Initialize JSON database files with correct permissions
RUN touch /usr/local/tomcat/bin/gameverse_data.json && \
    touch /usr/local/tomcat/bin/users.json && \
    touch /usr/local/tomcat/bin/wishlists.json && \
    touch /usr/local/tomcat/bin/purchase_history.json && \
    chmod 666 /usr/local/tomcat/bin/*.json

EXPOSE 8080
CMD ["catalina.sh", "run"]
