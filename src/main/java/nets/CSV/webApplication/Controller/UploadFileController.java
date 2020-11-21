package nets.CSV.webApplication.Controller;

import nets.CSV.webApplication.filestorage.FileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
    
    
}
