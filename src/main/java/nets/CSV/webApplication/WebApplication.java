package nets.CSV.webApplication;

import nets.CSV.webApplication.filestorage.FileStorage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class WebApplication implements CommandLineRunner {

	@Resource
	FileStorage fileStorage;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(WebApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		fileStorage.deleteAll();
		fileStorage.init();
	}

}
