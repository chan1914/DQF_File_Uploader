package nets.CSV.webApplication.Controller;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import nets.CSV.webApplication.CSVDigester.CSVDigester;
import nets.CSV.webApplication.WebApplication;
import nets.CSV.webApplication.filestorage.FileStorage;
import org.apache.http.HttpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller
public class UploadFileController {

    @Autowired
    FileStorage fileStorage;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CSVDigester csvDigester;

    Logger logger = LoggerFactory.getLogger(UploadFileController.class);

    @GetMapping("/")
    public String index() {
        return "uploadform.html";
    }

    @PostMapping("/")
    public String upload(@RequestParam("uploadfile") MultipartFile file, Model model) {
        logger.info("File being uplaoded\t" + file.getOriginalFilename());
        try {
            fileStorage.store(file);
            model.addAttribute("message", "File uploaded successfully! -> filename = " + file.getOriginalFilename());


        } catch (Exception e) {
            model.addAttribute("message", "Fail! -> uploaded filename: " + file.getOriginalFilename());
            return "uploadform";
        }

        try {
            String file1 = Files.readString(Paths.get("filestorage/" + file.getOriginalFilename()));
            List<JSONObject> rows = csvDigester.CSVToJSONList(file1);
            logger.info("Sending rows\t" + rows.size());
            int id = restTemplate.getForObject("http://DQF-Analysis-Repo/GetValidId/" + file.getOriginalFilename(), int.class);

            List<Runnable> runnables = new ArrayList<>();

            for(JSONObject row : rows){
                JSONObject jsonObject = new JSONObject(row);
                logger.info("Posting row\t" + row);

                int finalId = id;
                _dataSender dataSender = new _dataSender();
                Runnable task = () -> dataSender.sendData(file, finalId, row);
                task.run();

                runnables.add(task);
                id++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "uploadform";
    }

    private class _dataSender{
        @Autowired
        RestTemplate restTemplate;

        @Async
        public void sendData(MultipartFile file, int id, JSONObject row) {
            logger.info("resolved valid id for group " + file.getOriginalFilename() + " : " + id);
            restTemplate.postForEntity("http://DQF-Analysis-Core/row/" + file.getOriginalFilename() + "/" + id, row, JSONObject.class);
            logger.info("Saved id:" + id);
        }
    }



    /*
    @PostMapping("/upload-csv-file")
    public String uploadCSVFile(@RequestParam("file") MultipartFile file, Model model) {

        // validate file
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a CSV file to upload.");
            model.addAttribute("status", false);
        } else {

            // parse CSV file to create a list of `User` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean reader
                CsvToBean<User> csvToBean = new CsvToBeanBuilder(reader)
                        .withType(User.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of users
                List<User> users = csvToBean.parse();

                // TODO: save users in DB?

                // save users list on model
                model.addAttribute("users", users);
                model.addAttribute("status", true);

            } catch (Exception ex) {
                model.addAttribute("message", "An error occurred while processing the CSV file.");
                model.addAttribute("status", false);
            }
        }

        return "file-upload-status";
    }
    */
}
