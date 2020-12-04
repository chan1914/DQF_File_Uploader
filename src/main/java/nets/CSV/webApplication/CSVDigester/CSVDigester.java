package nets.CSV.webApplication.CSVDigester;

import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

@Service
public class CSVDigester {

    public static ArrayList<java.lang.String> CSVToJSON(String file){

        //Input file which needs to be parsed
        String fileToParse = "src/users.csv";
        BufferedReader fileReader = null;
        ArrayList<String> lines = new ArrayList<String>();

        //Delimiter used in CSV file
        final String DELIMITER = ",";
        try
        {
            String line = "";
            //Create the file reader
            fileReader = new BufferedReader(new FileReader(fileToParse));

            //Read the file line by line
            while ((line = fileReader.readLine()) != null)
            {
                lines.add(line);
                //Print all lines
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
        return generateJSON(lines);

    }

    public static ArrayList<java.lang.String> generateJSON(ArrayList<String> lines){
        ArrayList<String> JSONCollection = new ArrayList<String>();
        for (int i = 0; i < lines.size(); i++) {
            JSONObject lineJSON = new JSONObject();
            if (i > 0){

                int iterator = 0;
                for (String str : lines.get(i).split(",")) {

                    if (typeSelector(str) == "string"){
                        lineJSON.put(String.valueOf(lines.get(0).split(",")[iterator]), String.valueOf(str));
                    }
                    else {
                        lineJSON.put(String.valueOf(lines.get(0).split(",")[iterator]), str);
                    }

                    }
                JSONCollection.add(String.valueOf(lineJSON));
                iterator ++;
                }
            }
        return JSONCollection;
        }

    private static String typeSelector( String element){

        String result;
        result = checkDouble(element);
        if (!(result == "N/A")){
            return result;
        }
        result = checkInt(element);
        if (!(result == "N/A")){
            return result;
        }
        result = checkBoolean(element);
        if (!(result == "N/A")){
            return result;
        }
        return "string";
    }

    private static String checkDouble(String element){
        String result;

        try {
            Double.parseDouble(element);
            result = "double";
        }
        catch (NumberFormatException e) {
            result = "N/A";
        }
        return result;
    }

    private static String checkInt(String element){
        String result;

        try {
            Integer.parseInt(element);
            result = "int";
        }
        catch (NumberFormatException e) {
            result = "N/A";
        }
        return result;
    }

    private static String checkBoolean(String element){
        String result;

        if (element.toLowerCase() == "false"){
            result = "boolean";
        }
        else if (element.toLowerCase() == "true"){
            result = "boolean";
        }
        else{
            result = "N/A";
        }
        return result;
    }






    public String SerializeRow(String headers, String row) {
        throw new java.lang.UnsupportedOperationException("Not supported yet");
    }
}
