package nets.CSV.webApplication.CSVDigester;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class Test_CSVDigester {

    @Autowired
    CSVDigester csvDigester;

    @Test
    public void CSVRowToJson() throws JSONException {
        String headers = "id,name,email,country,age";
        String row = "103,Greg Hover,greg@example.com,US,45";
        String expected = "{\"id\":103,\"name\":\"Greg Hover\",\"email\":\"greg@example.com\",\"country\":\"us\",\"age\":45}";

        String result = csvDigester.SerializeRow(headers, row);

        JSONObject resultingObject = new JSONObject(result);

        assertEquals(103, resultingObject.getInt("id"));
        assertEquals("Greg Hover", resultingObject.getString("name"));
        assertEquals("greg@example.com", resultingObject.getString("email"));
        assertEquals("US", resultingObject.getString("country"));
        assertEquals(45, resultingObject.getInt("age"));
    }
}