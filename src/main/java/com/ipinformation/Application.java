package com.ipinformation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableCaching
@EnableMongoRepositories
public class Application /*implements CommandLineRunner*/ {

//	private static Logger LOG = LoggerFactory
//			.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		LOG.info("EXECUTING : command line runner");
//
//		for (int i = 0; i < args.length; ++i) {
//			LOG.info("args[{}]: {}", i, args[i]);
//		}
//
//	}
}
