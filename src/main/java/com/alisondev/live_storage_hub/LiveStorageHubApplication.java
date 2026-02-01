package com.alisondev.live_storage_hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class LiveStorageHubApplication {
	public static void main(String[] args) {
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
