package com.alaitp.api.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.event.EventListener;

import java.lang.management.ManagementFactory;

@EnableConfigurationProperties
@EnableDiscoveryClient
@SpringBootApplication
public class ApiGatewayApplication {
	private static final Logger log = LoggerFactory.getLogger(ApiGatewayApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@EventListener(ApplicationContextInitializedEvent.class)
	public void initLogging() {
		String defaultVal = "not specified";
		log.info("** Your OS name is : " + System.getProperty("os.name", defaultVal));
		log.info("** The version of the JVM you are running is : " + System.getProperty("java.version", defaultVal));
		log.info("** Your user home directory is : " + System.getProperty("user.home", defaultVal));
		log.info("** Your JRE installation directory is : " + System.getProperty("java.home", defaultVal));
		log.info("** Amount of Your CPU cores : " + Runtime.getRuntime().availableProcessors());
		log.info("** Amount of Your JVM memory: " + (double) ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getInit() / 1073741.824 + "MB");
	}
}
