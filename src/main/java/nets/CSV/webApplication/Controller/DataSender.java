package nets.CSV.webApplication.Controller;

import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

public class DataSender {
    @Autowired
    RestTemplate restTemplate;

    Logger logger = LoggerFactory.getLogger(UploadFileController.class);

    @Async
    public void sendData(MultipartFile file, int id, JSONObject row) {
        logger.info("resolved valid id for group " + file.getOriginalFilename() + " : " + id);
        restTemplate.postForEntity("http://DQF-Analysis-Core/row/" + file.getOriginalFilename() + "/" + id, row, JSONObject.class);
        logger.info("Saved id:" + id);
    }
}
