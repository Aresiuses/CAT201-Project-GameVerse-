# STAGE 1: Compilation
FROM eclipse-temurin:11-jdk AS build

# Create build directory
WORKDIR /app

# Copy the libraries and source code
COPY ./lib/ /app/lib/
COPY ./src/ /app/src/

# Compile the Java code manually
RUN mkdir -p /app/out && \
    javac -d /app/out -cp "lib/*" $(find src -name "*.java")

# STAGE 2: Run in Tomcat
FROM tomcat:9.0-jdk11-openjdk-slim

# Remove default apps to avoid conflicts
RUN rm -rf /usr/local/tomcat/webapps/*

# Create the standard ROOT webapp directory structure
RUN mkdir -p /usr/local/tomcat/webapps/ROOT/WEB-INF/classes \
    && mkdir -p /usr/local/tomcat/webapps/ROOT/WEB-INF/lib

# 1. Copy web content (HTML/JS/CSS)
# Note: If your web.xml is inside ./web/WEB-INF/, it will be copied automatically here
COPY ./web/ /usr/local/tomcat/webapps/ROOT/

# 2. Copy compiled classes from the build stage
COPY --from=build /app/out/ /usr/local/tomcat/webapps/ROOT/WEB-INF/classes/

# 3. Copy libraries to the server
COPY ./lib/ /usr/local/tomcat/webapps/ROOT/WEB-INF/lib/

# 4. Initialize JSON database files with correct permissions
# We place these in /usr/local/tomcat/bin/ where the DatabaseHandler expects them
RUN touch /usr/local/tomcat/bin/gameverse_data.json && \
    touch /usr/local/tomcat/bin/users.json && \
    touch /usr/local/tomcat/bin/wishlists.json && \
    touch /usr/local/tomcat/bin/purchase_history.json && \
    chmod 666 /usr/local/tomcat/bin/*.json

EXPOSE 8080
CMD ["catalina.sh", "run"]
