package com.alisondev.live_storage_hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class LiveStorageHubApplication {
	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		// Load aws keys
		System.setProperty("AWS_ACCESS_KEY_ID", dotenv.get("AWS_ACCESS_KEY_ID"));
		System.setProperty("AWS_SECRET_ACCESS_KEY", dotenv.get("AWS_SECRET_ACCESS_KEY"));

		// Load jwt secret key
		System.setProperty("JWT_SECRET_KEY", dotenv.get("JWT_SECRET_KEY"));

		// Load admin api secret key
		System.setProperty("ADMIN_SUPER_SECRET_KEY", dotenv.get("ADMIN_SUPER_SECRET_KEY"));

		SpringApplication.run(LiveStorageHubApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void logApplicationUrl(ApplicationReadyEvent event) {
		int port = event.getApplicationContext().getEnvironment()
				.getProperty("server.port", Integer.class, 8080);
		String contextPath = event.getApplicationContext().getEnvironment()
				.getProperty("server.servlet.context-path", "");

		System.out.println("\n🟢 API running on: http://localhost:" + port + contextPath);
		System.out.println("\n🔵 API Docs on: http://localhost:" + port + contextPath + "/swagger-ui/index.html\n");
	}
}
