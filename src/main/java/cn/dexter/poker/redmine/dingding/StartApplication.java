package cn.dexter.poker.redmine.dingding;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.treeleafj.xdoc.boot.EnableXDoc;

@EnableScheduling
@SpringBootApplication
public class StartApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(StartApplication.class).run(args);
	}

}

