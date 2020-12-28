package nets.CSV.webApplication.Controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONArray;
import org.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import nets.CSV.webApplication.CSVDigester.CSVDigester;
import nets.CSV.webApplication.WebApplication;
import nets.CSV.webApplication.filestorage.FileStorage;
import org.apache.http.HttpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.*;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantLock;

@Controller
public class UploadFileController {
    private int webclientLimit = 200;

    @Autowired
    FileStorage fileStorage;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    WebClient.Builder webClientBuilder;

    @Autowired
    CSVDigester csvDigester;

    private int openWebClients = 0;

    Logger logger = LoggerFactory.getLogger(UploadFileController.class);

    ReentrantLock lock = new ReentrantLock();

    public void addToOpenWebClients (int i){
        lock.lock();
        openWebClients = openWebClients + i;
        lock.unlock();
    }

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

            /*while (rows.size() > 0){
                if (openWebClients < webclientLimit) {
                    JSONObject row = rows.get(0);
                    rows.remove(0);
                    int finalId = id;
                    webClientBuilder.build().post()
                            .uri("http://DQF-Analysis-Core/row/" + file.getOriginalFilename())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(row.toString()))
                            .exchangeToMono(e -> e.bodyToMono(JSONObject.class))
                            .doOnError(x -> logger.error("failed to send " + finalId))
                            .subscribe(jObject -> onPostCoplete(jObject));
                    addToOpenWebClients(1);
                    //logger.info("Saved id:" + id);
                    id++;
                }else {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }*/

            JSONArray rowsArr = new JSONArray();
            for (JSONObject row : rows){
                rowsArr.put(row);
            }
            restTemplate.postForObject(
                    "http://DQF-Analysis-Core/row/addList/" + file.getOriginalFilename(),
                    rowsArr,
                    JSONArray.class);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return "uploadform";
    }

    private void onPostCoplete(JSONObject jsonObject){
        addToOpenWebClients(-1);
        //logger.info("Open web clients at: " + openWebClients);
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
