package nets.CSV.webApplication.FileUploader;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileUploadController {

    //destination folder to upload the files
    public static String uploadDirectory = System.getProperty("user.dir");

    @RequestMapping("/")
    public String UploadPage(Model model) {
        return "uploadView";
    }

    @RequestMapping("/upload")
    public String upload(Model model, @RequestParam("files")MultipartFile[] files) {
        StringBuilder filenames = new StringBuilder();
        for(MultipartFile file : files) {
            Path fileNameAndPath = Paths.get(uploadDirectory,file.getOriginalFilename());
            try {
                Files.write(fileNameAndPath, file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        model.addAttribute("msg", "Succesfully uploaded files" + filenames.toString());
        return "uploadStatusView";
    }
}
