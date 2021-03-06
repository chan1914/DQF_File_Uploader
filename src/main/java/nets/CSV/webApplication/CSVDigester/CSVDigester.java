package nets.CSV.webApplication.CSVDigester;

import com.fasterxml.jackson.annotation.JsonAlias;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class CSVDigester {

    public  ArrayList<java.lang.String> CSVToJSON(String file){

        //Input file which needs to be parsed
//        String fileToParse = "src/users.csv";
//        BufferedReader fileReader = null;

        List<String> lines = Arrays.asList(file.split("\\r?\\n"));

//        //Delimiter used in CSV file
//        final String DELIMITER = ",";
//        try
//        {
//            String line = "";
//            //Create the file reader
//            fileReader = new BufferedReader(new FileReader(fileToParse));
//
//            //Read the file line by line
//            while ((line = fileReader.readLine()) != null)
//            {
//                lines.add(line);
//                //Print all lines
//                System.out.println(line);
//
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        finally
//        {
//            try {
//                fileReader.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        // return generateJSON(lines);

        ArrayList<String> JSONCollection = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            JSONCollection.add(SerializeRow(lines.get(0), lines.get(i)));
        }
        return JSONCollection;
    }

    public  ArrayList<JSONObject> CSVToJSONList(String file){

        //Input file which needs to be parsed
//        String fileToParse = "src/users.csv";
//        BufferedReader fileReader = null;

        List<String> lines = Arrays.asList(file.split("\\r?\\n"));

//        //Delimiter used in CSV file
//        final String DELIMITER = ",";
//        try
//        {
//            String line = "";
//            //Create the file reader
//            fileReader = new BufferedReader(new FileReader(fileToParse));
//
//            //Read the file line by line
//            while ((line = fileReader.readLine()) != null)
//            {
//                lines.add(line);
//                //Print all lines
//                System.out.println(line);
//
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        finally
//        {
//            try {
//                fileReader.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        // return generateJSON(lines);
        List<CompletableFuture<JSONObject>> completableFutures = new ArrayList<>();
        ArrayList<JSONObject> JSONCollection = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            completableFutures.add(SerializeRowAsJson(lines.get(0), lines.get(i)));
        }
        do {
            if (completableFutures.get(0).isDone()) {
                try {
                    JSONCollection.add(completableFutures.get(0).get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                completableFutures.remove(0);
            }
        }while (completableFutures.size() != 0);
        return JSONCollection;
    }

    public String SerializeRow(String headers, String row) {

        int iterator = 0;
        JSONObject lineJSON = new JSONObject();
        String[] rowValue = row.split(",");

        for (String str : headers.split(",")) {

            if (typeSelector(rowValue[iterator]).equals("string")) {
                try {
                    lineJSON.put(String.valueOf(str), rowValue[iterator]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    lineJSON.put(String.valueOf(str), rowValue[iterator]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            iterator++;
        }
        return String.valueOf(lineJSON);
    }

    @Async
    public CompletableFuture<JSONObject> SerializeRowAsJson(String headers, String row) {

        int iterator = 0;
        JSONObject lineJSON = new JSONObject();
        String[] rowValue = row.split(",");

        for (String str : headers.split(",")) {
            try {
                if (rowValue[iterator].equals("null")){
                    try {
                        lineJSON.put(str, JSONObject.NULL);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if (typeSelector(rowValue[iterator]).equals("string")) {
                    try {
                        lineJSON.put(String.valueOf(str), rowValue[iterator]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        lineJSON.put(String.valueOf(str), rowValue[iterator]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }catch (IndexOutOfBoundsException e){
                try {
                    lineJSON.put(String.valueOf(str), JSONObject.NULL);
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }

            iterator++;
        }
        return CompletableFuture.completedFuture(lineJSON);
    }







//    public ArrayList<java.lang.String> generateJSON(ArrayList<String> lines){
//        ArrayList<String> JSONCollection = new ArrayList<String>();
//        for (int i = 0; i < lines.size(); i++) {
//            JSONObject lineJSON = new JSONObject();
//            if (i > 0){
//
//                int iterator = 0;
//                for (String str : lines.get(i).split(",")) {
//
//
//                    if (typeSelector(str) == "string"){
//                        lineJSON.put(String.valueOf(lines.get(0).split(",")[iterator]), String.valueOf(str));
//                    }
//                    else {
//                        lineJSON.put(String.valueOf(lines.get(0).split(",")[iterator]), str);
//                    }
//
//                }
//                JSONCollection.add(String.valueOf(lineJSON));
//                iterator ++;
//            }
//        }
//        return JSONCollection;
//    }

    //  public ArrayList<java.lang.String> generateJSON(ArrayList<String> lines){
    //    ArrayList<String> JSONCollection = new ArrayList<String>();
    //    for (int i = 0; i < lines.size(); i++) {
    //        JSONObject lineJSON = new JSONObject();
    //        if (i > 0){
    //
      //          int iterator = 0;
      //          for (String str : lines.get(i).split(",")) {
    //
      //              if (typeSelector(str) == "string"){
      //                  lineJSON.put(String.valueOf(lines.get(0).split(",")[iterator]), String.valueOf(str));
      //              }
      //              else {
      //                  lineJSON.put(String.valueOf(lines.get(0).split(",")[iterator]), str);
      //              }
      //
     //               }
     //           JSONCollection.add(String.valueOf(lineJSON));
     //           iterator ++;
     //           }
     //       }
     //   return JSONCollection;
     //   }

    private String typeSelector( String element){

        boolean result;
        result = checkDouble(element);
        if (!(result)){
            return "double";
        }
        result = checkInt(element);
        if (!(result)){
            return "int";
        }
        result = checkBoolean(element);
        if (!(result)){
            return "boolean";
        }
        return "string";
    }

    private boolean checkDouble(String element){
        boolean result;

        try {
            Double.parseDouble(element);
            result = true;
        }
        catch (NumberFormatException e) {
            result = false;
        }
        return result;
    }

    private boolean checkInt(String element){
        boolean result;

        try {
            Integer.parseInt(element);
            result = true;
        }
        catch (NumberFormatException e) {
            result = false;
        }
        return result;
    }

    private boolean checkBoolean(String element){
        boolean result;

        if (element.toLowerCase().equals("false")){
            result = true;
        }
        else if (element.toLowerCase().equals("true")){
            result = true;
        }
        else{
            result = false;
        }
        return result;
    }
}
