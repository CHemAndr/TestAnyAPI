package home.api;


//import io.restassured.RestAssured;
//import io.restassured.config.HttpClientConfig;
//import io.restassured.config.RestAssuredConfig;

import io.restassured.response.Response;
import io.restassured.config.ConnectionConfig;

//import io.restassured.response.ValidatableResponse;
//import org.apache.http.params.CoreConnectionPNames;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.post;


import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.Date;






public class MyAPITests {

    @Test
    public void test1(){

        ObjectMapper mapper = new ObjectMapper();

        System.out.println("test1");

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

    @Test
    public void test2() {
        long delAcceptable = 300; //Допустимое отклонение, мсек
        long tmStart = 0;//Время начала запроса
        long tmFinish = 0;//Время получения ответа
        long avgDelay0 = 0;//Средняя задержка получения ответа при ее задании = 0 сек
        long delayi  = 0; // Задержка получения ответа при ее задании от 1 до 14 сек

        System.out.println("test2");
        //Десять запросов при нулевой задержке
        long[] mDelay0 = {0,0,0,0,0,0,0,0,0,0};
        for (int i=0; i<10; i++){
            tmStart = System.currentTimeMillis();
            Response rpost = post("https://httpbin.org/delay/0");
            tmFinish = System.currentTimeMillis();
            mDelay0[i]=tmFinish - tmStart;
        }
        //Средний результат задержки при ее задании = 0
        avgDelay0 = Math.round(Arrays.stream(mDelay0).sum()/mDelay0.length);
        System.out.println("Задержка  0 = " + avgDelay0);

        //Запросы при задержке от 1 до 14 сек
        for (int i=1;i<15;i++) {
            String delayUrl = Integer.toString(i);
            tmStart = System.currentTimeMillis();
            Response rpost = post("https://httpbin.org/delay/" +delayUrl);
            tmFinish = System.currentTimeMillis();
            Assertions.assertTrue(rpost.getStatusCode() == 200);
            delayi = tmFinish - tmStart - avgDelay0;
            if (i<10) {Assertions.assertTrue(Math.abs(delayi - i*1000)<delAcceptable);}
            if (i>=10) {Assertions.assertTrue(Math.abs(delayi - 10000)<delAcceptable);}
            System.out.println(i+ "  Задержка = " + delayi);
        }

        //Запросы с отрицательной задержкой от -1 до -14 (как при нулевой задержке)
        for (int i=1;i<15;i++) {
            String delayUrl = Integer.toString(i);
            tmStart = System.currentTimeMillis();
            Response rpost = post("https://httpbin.org/delay/-" +delayUrl);
            tmFinish = System.currentTimeMillis();
            Assertions.assertTrue(rpost.getStatusCode() == 200);
            delayi = tmFinish - tmStart - avgDelay0;
            Assertions.assertTrue(Math.abs(delayi) < delAcceptable);
            System.out.println(-i+ "  Задержка = " + delayi);
        }

        //Запрос без задания задержки
        Response rpost = post("https://httpbin.org/delay/");
        Assertions.assertTrue(rpost.getStatusCode() > 400);
        System.out.println(rpost.getStatusCode());

        //Запрос с некорректной задержкой
        rpost = post("https://httpbin.org/delay/err");
        Assertions.assertTrue(rpost.getStatusCode() > 400);
        System.out.println(rpost.getStatusCode());

        //Запрос при вещественной задержке (интересно - точность задержки увеличилась !!!?)
        for (int i=0; i<10; i++) {
            String delayUrl = Integer.toString(i);
            tmStart = System.currentTimeMillis();
            rpost = post("https://httpbin.org/delay/3." +delayUrl );
            tmFinish = System.currentTimeMillis();
            Assertions.assertTrue(rpost.getStatusCode() == 200);
            delayi = tmFinish - tmStart - avgDelay0;
            //Assertions.assertTrue(Math.abs(delayi - (3000 + i*100))<100L);
            System.out.println("3." + i + "  Задержка = " + delayi);
            System.out.println(rpost.getStatusCode());
        }

        //Запрос при отрицательной вещественной задержке (как при нулевой задержке)
        tmStart = System.currentTimeMillis();
        rpost = post("https://httpbin.org/delay/-2.7");
        tmFinish = System.currentTimeMillis();
        Assertions.assertTrue(rpost.getStatusCode() == 200);
        delayi = tmFinish - tmStart - avgDelay0;
        Assertions.assertTrue(Math.abs(delayi)<delAcceptable);
        System.out.println(-2.7 + "  Задержка = " + delayi);
        System.out.println(rpost.getStatusCode());
    }

    @Test
    public void test3() {
        System.out.println("test3");

        Response rget = get("https://httpbin.org/image/png");

        System.out.println("Status Code :" + rget.getStatusCode());
        assertEquals(200, rget.getStatusCode());
        System.out.println("Response.header.content-type: " + rget.getHeader("content-type"));
        assertEquals("image/png",rget.getHeader("content-type").trim());
    }
}
