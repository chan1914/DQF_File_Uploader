package nets.CSV.webApplication.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CSVController {

    @RequestMapping("/uploadform")
    public String getCSV(){

        return "";
    }
}
