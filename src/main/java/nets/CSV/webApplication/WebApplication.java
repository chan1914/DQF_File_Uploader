package nets.CSV.webApplication;

import nets.CSV.webApplication.FileUploader.FileUploadController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import java.io.File;

@SpringBootApplication
@ComponentScan({"nets.CSV.webApplication.FileUploader"})
public class WebApplication {

	public static void main(String[] args) {
		new File(FileUploadController.uploadDirectory).mkdir();
		SpringApplication.run(WebApplication.class, args);
	}

}
