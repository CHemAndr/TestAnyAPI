package home.api;

import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.post;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;


public class MyAPITests {

    @Test
    public void test1(){
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("test1");
        Response getResponse = get("https://www.wiley.com/en-us/search/autocomplete/comp_00001H9J?term=Java");
        System.out.println("Status Code :" + getResponse.getStatusCode());
        assertEquals(200, getResponse.getStatusCode());
        String jsonStr = getResponse.asString();
        System.out.println("Response :" + jsonStr);
        try {
            ResponseObject responseObject = mapper.readValue(jsonStr, ResponseObject.class);
            //Количество suggestions
            int suggestions = responseObject.getSuggestions().length;
            Assertions.assertEquals(4,suggestions);
            System.out.println("Кол-во suggestions: "+suggestions);
            //Параметр term включает <span class=\"search-highlight\">java</span>
            for (int i = 0; i < suggestions; i++) {
                String term = responseObject.getSuggestions()[i].getTerm();
                System.out.println(term);
                Assertions.assertTrue(term.contains("<span class=\"search-highlight\">java</span>"));
            }
            //Параметр title содержит слово Wiley
            int pages = responseObject.getPages().length;
            System.out.println("Кол-во pages: " + pages);
            for (int i = 0; i < pages; i++) {
                String title = responseObject.getPages()[i].getTitle();
                System.out.println(title);
                Assertions.assertTrue(title.contains("Wiley"));
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test2() {
        long acceptableDelay = 300; //Допустимое отклонение, мсек
        long startTime = 0;//Время начала запроса
        long finishTime = 0;//Время получения ответа
        long avgDelay0 = 0;//Средняя задержка получения ответа при ее задании = 0 сек
        long delayi  = 0; // Задержка получения ответа при ее задании от 1 до 14 сек

        System.out.println("test2");
        //Десять запросов при нулевой задержке
        long[] mDelay0 = {0,0,0,0,0,0,0,0,0,0};
        for (int i=0; i<10; i++){
            startTime = System.currentTimeMillis();
            Response response = post("https://httpbin.org/delay/0");
            finishTime = System.currentTimeMillis();
            mDelay0[i] = finishTime - startTime;
        }
        //Средний результат задержки при ее задании = 0
        avgDelay0 = Math.round(Arrays.stream(mDelay0).sum() / mDelay0.length);
        System.out.println("Задержка 0 = " + avgDelay0);

        //Запросы при задержке от 1 до 14 сек
        for (int i = 1; i < 15; i++) {
            String delayPath = Integer.toString(i);
            startTime = System.currentTimeMillis();
            Response response = post("https://httpbin.org/delay/" +delayPath);
            finishTime = System.currentTimeMillis();
            Assertions.assertTrue(response.getStatusCode() == 200);
            delayi = finishTime - startTime - avgDelay0;
            if (i < 10) {
                Assertions.assertTrue(Math.abs(delayi - i * 1000) < acceptableDelay);
            }
            if (i >= 10) {
                Assertions.assertTrue(Math.abs(delayi - 10000) < acceptableDelay);
            }
            System.out.println(i+ "  Задержка = " + delayi);
        }

        //Запросы с отрицательной задержкой от -1 до -14 (как при нулевой задержке)
        for (int i = 1; i < 15; i++) {
            String delayPath = Integer.toString(i);
            startTime = System.currentTimeMillis();
            Response response = post("https://httpbin.org/delay/-" + delayPath);
            finishTime = System.currentTimeMillis();
            Assertions.assertTrue(response.getStatusCode() == 200);
            delayi = finishTime - startTime - avgDelay0;
            Assertions.assertTrue(Math.abs(delayi) < acceptableDelay);
            System.out.println(-i + "  Задержка = " + delayi);
        }

        //Запрос без задания задержки
        Response response = post("https://httpbin.org/delay/");
        Assertions.assertTrue(response.getStatusCode() > 400);
        System.out.println(response.getStatusCode());

        //Запрос с некорректной задержкой
        response = post("https://httpbin.org/delay/err");
        Assertions.assertTrue(response.getStatusCode() > 400);
        System.out.println(response.getStatusCode());

        //Запрос при вещественной задержке (интересно - точность задержки увеличилась !!!?)
        for (int i = 0; i < 10; i++) {
            String delayPath = Integer.toString(i);
            startTime = System.currentTimeMillis();
            response = post("https://httpbin.org/delay/3." + delayPath );
            finishTime = System.currentTimeMillis();
            Assertions.assertTrue(response.getStatusCode() == 200);
            delayi = finishTime - startTime - avgDelay0;
            //Assertions.assertTrue(Math.abs(delayi - (3000 + i*100)) < 100L);
            System.out.println("3." + i + "  Задержка = " + delayi);
            System.out.println(response.getStatusCode());
        }

        //Запрос при отрицательной вещественной задержке (как при нулевой задержке)
        startTime = System.currentTimeMillis();
        response = post("https://httpbin.org/delay/-2.7");
        finishTime = System.currentTimeMillis();
        Assertions.assertTrue(response.getStatusCode() == 200);
        delayi = finishTime - startTime - avgDelay0;
        Assertions.assertTrue(Math.abs(delayi)<acceptableDelay);
        System.out.println(-2.7 + "  Задержка = " + delayi);
        System.out.println(response.getStatusCode());
    }

    @Test
    public void test3() {
        System.out.println("test3");
        Response response = get("https://httpbin.org/image/png");
        System.out.println("Status Code :" + response.getStatusCode());
        assertEquals(200, response.getStatusCode());
        System.out.println("Response.header.content-type: " + response.getHeader("content-type"));
        assertEquals("image/png",response.getHeader("content-type").trim());
    }
}
