package example.myapp;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class Client {

    long sinceTime;
    long tillTime;

    public Client(long since, long till) {
        this.sinceTime = since;
        this.tillTime = till;
    }

    private int count;
    private String place;

    public Map<String, Double> getContent() {
        String dishName;
        Double amount;

        Map<String, Double> positionsAndAmount = new ConcurrentHashMap<>();

        ArrayList<Long> idCh = new ArrayList<>();

        String userpass = "nh780:jXb9Z5R7";
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
        String payload = "{\"filters\": [{\"field\": \"createDate\", \"operation\": \"range\", \"value\": {\"since\":" + sinceTime + ", \"till\":" + tillTime + "}}]}"; // рабочий
//       String payload = "{\"filters\": [{\"field\": \"createDate\", \"operation\": \"range\", \"value\": {\"since\": \"1626086634000\", \"till\": \"1626086694000\"}}]}"; // 1 чек
//       String payload = "{\"filters\": [{\"field\": \"createDate\", \"operation\": \"range\", \"value\": {\"since\": \"1625755263000\", \"till\": \"1625755323000\"}}]}"; // 2 чека сразу
//       String payload = "{\"filters\": [{\"field\": \"createDate\", \"operation\": \"range\", \"value\": {\"since\": \"1626691150000\", \"till\": \"1626691210000\"}}]}"; // еще 2 чека сразу
//       String payload = "{\"filters\": [{\"field\": \"createDate\", \"operation\": \"range\", \"value\": {\"since\": \"1626691150000\", \"till\": \"1626691156000\"}}]}"; // пустой

        try {
            final URL url = new URL("https://nh780.quickresto.ru/platform/online/api/list?moduleName=front.orders");

            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

            con.setDoOutput(true);
            con.setUseCaches(false);

            con.setRequestProperty("Authorization", basicAuth);
            con.setRequestProperty("Content-Type", "application/json");

            OutputStream os = con.getOutputStream();
            os.write(payload.getBytes(StandardCharsets.UTF_8));
            os.flush();
            os.close();


            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            try {
                Object obj = new JSONParser().parse(String.valueOf(content));
                JSONArray jArray = (JSONArray) obj;
                Iterator jArrayItr = jArray.iterator();
                while (jArrayItr.hasNext()) {
                    JSONObject jObj = (JSONObject) jArrayItr.next();

                    JSONObject test = (JSONObject) jObj.get("createTerminalSalePlace");
                    place = (String) test.get("title");
                    if (place.equals("Б2")){
                        idCh.add((Long) jObj.get("id"));
                    }
                }
            } catch (ParseException exception) {
                System.out.println("нет чека");
            }
//             System.out.println(idCh);
        } catch (IOException ex) {
            System.out.println("Ошибка соединения! Сервер не доступен");
        }
        for (int i = 0; i < idCh.size(); i++) {
            try {
                final URL url2 = new URL("https://nh780.quickresto.ru/platform/online/api/read?moduleName=front.orders&objectId=" + idCh.get(i));
                HttpsURLConnection con2 = (HttpsURLConnection) url2.openConnection();
                con2.setRequestProperty("Authorization", basicAuth);
                con2.setRequestProperty("Content-Type", "application/json");

                BufferedReader in2 = new BufferedReader(new InputStreamReader(con2.getInputStream()));
                String inputLine2;
                StringBuilder content2 = new StringBuilder();
                while ((inputLine2 = in2.readLine()) != null) {
                    content2.append(inputLine2);
                }
                try {
                    Object obj2 = new JSONParser().parse(String.valueOf(content2));
                    JSONObject test2 = (JSONObject) obj2;
                    JSONArray orderItems = (JSONArray) test2.get("orderItemList");
                    Iterator itemsItr = orderItems.iterator();
                    while (itemsItr.hasNext()) {
                        JSONObject dish = (JSONObject) itemsItr.next();

                        dishName = (String) dish.get("name");
                        amount = (Double) dish.get("amount");

                        if (positionsAndAmount.containsKey(dishName)) {
                            amount += positionsAndAmount.get(dishName).doubleValue();
                            positionsAndAmount.put(dishName, amount);
                        } else {
                            positionsAndAmount.put(dishName, amount);
                        }
                    }
                } catch (ParseException ex) {
                    System.out.println("Ошибка парсинга списка продаж");
                }
            } catch (IOException ex) {
                System.out.println("Нет соединения! Сервер сломался");
            }
        }
        return positionsAndAmount;
    }

    public void checkSales(Map<String, Double> sales) {
        String mirel = "Мирель";
        String shagal = "Шагал";
        String noel = "Ноэль";
        String cherry = "Черришок";
        String infiniki = "Инфиники";
        String svoya = "Своя Каша";
        String kombo = "Комбо - каша";
        String kombo1 = "Комбо с чаем";
        String prosto = "Каша овсяная";

        String nordicPear = "Nordic – Груша";
        String nordicBanana = "Nordic – Банан с арахисовой пастой";
        String nordicHalva = "Nordic – Халва";

        for (Map.Entry<String, Double> entry : sales.entrySet()) {

            if (entry.getKey().equals(mirel) || entry.getKey().equals(shagal) || entry.getKey().equals(noel) || entry.getKey().equals(cherry)
            || entry.getKey().equals(infiniki) || entry.getKey().equals(svoya) || entry.getKey().equals(kombo) || entry.getKey().equals(kombo1)
            || entry.getKey().equals(prosto)) {
                count += entry.getValue().intValue() * 3;
            } else if (entry.getKey().equals(nordicPear) || entry.getKey().equals(nordicBanana) || entry.getKey().equals(nordicHalva)) {
                count += entry.getValue().intValue() * 2;
            } else {
                count += 0;
            }
        }
        sales.clear();
    }

    public int getCount() {
        return count;
    }

}

