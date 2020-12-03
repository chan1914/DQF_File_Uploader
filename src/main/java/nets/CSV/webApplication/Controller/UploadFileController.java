package nets.CSV.webApplication.Controller;

import nets.CSV.webApplication.filestorage.FileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

@Controller
public class UploadFileController {

    @Autowired
    FileStorage fileStorage;


    @GetMapping("/")
    public String index() {
        return "uploadform.html";
    }

    @PostMapping("/")
    public String upload(@RequestParam("uploadfile") MultipartFile file, Model model) {
        try {
            fileStorage.store(file);
            model.addAttribute("message", "File uploaded successfully! -> filename = " + file.getOriginalFilename());

        } catch (Exception e) {
            model.addAttribute("message", "Fail! -> uploaded filename: " + file.getOriginalFilename());
        }
        return "uploadform";
    }

    public void CSVtoJSON(String file){

        //Input file which needs to be parsed
        String fileToParse = "SampleCSVFile.csv";
        BufferedReader fileReader = null;

        //Delimiter used in CSV file
        final String DELIMITER = ",";
        try
        {
            String line = "";
            ArrayList<String> lines = new ArrayList<String>();
            //Create the file reader
            fileReader = new BufferedReader(new FileReader(fileToParse));

            //Read the file line by line
            while ((line = fileReader.readLine()) != null)
            {
                lines.add(line);
                //Print all tokens
                System.out.println(line);

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally
        {
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    /*
    @PostMapping("/upload-csv-file")
    public String UploadCSVFile(@RequestParam("uploadfile") MultipartFile file, Model model) {
        // validate file
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a CSV file to upload.");
            model.addAttribute("status", false);
        } else {

            // parse CSV file to create a list of `User` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean reader
                CsvToBean csvToBean = new CsvToBeanBuilder(reader)
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
