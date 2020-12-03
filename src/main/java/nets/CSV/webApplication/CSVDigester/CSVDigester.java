package nets.CSV.webApplication.CSVDigester;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVDigester {

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

    }

    private String FindType(String line){


        return "";
    }





}
