package home.api;


import io.restassured.response.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.get;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fasterxml.jackson.databind.ObjectMapper;






public class MyAPITests {



    @Test
    public  void test1(){

        ObjectMapper mapper = new ObjectMapper();

        Response rget = get("https://www.wiley.com/en-us/search/autocomplete/comp_00001H9J?term=Java");

        System.out.println("Status Code :" + rget.getStatusCode());
        assertEquals(200, rget.getStatusCode());
        String jsonStr = rget.asString();
        System.out.println("Response :" + jsonStr);

        try {
            Jresponse jresponse = mapper.readValue(jsonStr, Jresponse.class);
            //Количество suggestions
            int suggestions = jresponse.getSuggestions().length;
            Assertions.assertEquals(4,suggestions);
            System.out.println("Кол-во suggestions: "+suggestions);
            //Параметр term включает <span class=\"search-highlight\">java</span>
            for (int i=0; i<suggestions; i++) {
                String sTerm = jresponse.getSuggestions()[i].getTerm();
                System.out.println(sTerm);
                Assertions.assertTrue(sTerm.contains("<span class=\"search-highlight\">java</span>"));
            }
            //Параметр title содержит слово Wiley
            int pages = jresponse.getPages().length;
            System.out.println("Кол-во pages: "+pages);
            for (int i=0; i<pages; i++) {
                String sTitle = jresponse.getPages()[i].getTitle();
                System.out.println(sTitle);
                Assertions.assertTrue(sTitle.contains("Wiley"));
            }


        } catch (Exception e){
            System.out.println(e.getMessage());}
    }
}
