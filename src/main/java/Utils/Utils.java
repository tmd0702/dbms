package Utils;

import com.example.GraphicalUserInterface.ManagementMain;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class Utils {
    public static ArrayList<ArrayList<String>> getKeysValuesFromMap(HashMap<String, String> map) {
        ArrayList keysValuesList = new ArrayList <String>(2);
        Set<String> keySet = map.keySet();

        // Creating an ArrayList of keys
        // by passing the keySet
        ArrayList<String> listOfKeys = new ArrayList<String>(keySet);

        // Getting Collection of values from HashMap
        Collection<String> values = map.values();

        // Creating an ArrayList of values
        ArrayList<String> listOfValues = new ArrayList<>(values);

        // Adding key, value list to new array list
        keysValuesList.add(listOfKeys);
        keysValuesList.add(listOfValues);

        return keysValuesList;
    }
    public static ArrayList<String> convertJSONArrayToArrayList(JSONArray ja) {
        ArrayList<String> retArray = new ArrayList<String>();
        for (int i = 0; i < ja.length(); ++i) {
            retArray.add(ja.get(i).toString());
        }
        return retArray;
    }
    public static String addDateByNDays(Date date, int nDays) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, nDays);
        date = c.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
    public static ArrayList<ArrayList<String>> concat2dArray(ArrayList<ArrayList<String>> firstArray, ArrayList<ArrayList<String>> secondArray) {
        for (int i = 0; i < secondArray.size();++i) {
            for (int j=0; j<secondArray.get(i).size();++j) {
                firstArray.get(i).add(secondArray.get(i).get(j));

            }
        }
        return firstArray;
    }
    public static long getDiffBetweenDates(Date date1, Date date2) {
        long diff;
        LocalDate localDate1 = new Date(date1.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = new Date(date2.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        diff = ChronoUnit.DAYS.between(localDate1, localDate2);
        return diff;
    }
    public static String getDateStringWithFormat(String pattern, Date date) {
        DateFormat df = new SimpleDateFormat(pattern);
        String dateAsString = df.format(date);
        return dateAsString;
    }
    public static HashMap<String, Double> jsonToMap(JSONObject json) throws JSONException {
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        HashMap<String, Double> doubleRetMap = new HashMap<String, Double>();
        for (String key : retMap.keySet()) {
            doubleRetMap.put(key, ((BigDecimal)retMap.get(key)).doubleValue());
        }
        return doubleRetMap;
    }
    public static HashMap<String, Double> sortMapByValue(HashMap<String, Double> hm) {
        HashMap<String, Double> result =
                hm.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .collect(Collectors.toMap(e -> e.getKey(),
                                e -> e.getValue(),
                                (e1, e2) -> null, // or throw an exception
                                () -> new LinkedHashMap<String, Double>()));
        return result;
    }

    public static HashMap<String, Object> toMap(JSONObject object) throws JSONException {
        HashMap<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }
    public static String toCamelCase(String text) {
        String[] words = text.split("[\\W_]+");
        String result = "";
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0) {
                word = word.isEmpty() ? word : word.toLowerCase();
            } else {
                word = word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
            }
            result += word;
        }
        return result;
    }
    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public static void writeProperties(Properties prop,String username, String password, String filePath) throws Exception{
        System.out.println(filePath + "123");
        FileOutputStream is = new FileOutputStream("/" + filePath);
        prop.setProperty("USERNAME", username);
        prop.setProperty("PASSWORD", password);
        prop.store(is,null);
    }
    public static String getRowValueByColumnName(int index, String columnName, ArrayList<ArrayList<String>> data) {
        String ret = null;
        try {
            for (String dataColumnName : data.get(0)) {
                if (columnName.equals(dataColumnName)) {
                    ret = data.get(index).get(data.get(0).indexOf(dataColumnName));
                    break;
                }
            }
        } catch (Exception e) {
//            System.out.println(e);
        }
        return ret;
    }
    public static ArrayList<String> getDataValuesByColumnName(ArrayList<ArrayList<String>> data, String columnName) {
        ArrayList<String> ret = new ArrayList<String>();
        int index = -1;
        for (String dataColumnName : data.get(0)) {
            if (columnName.equals(dataColumnName)) {
                index = data.get(0).indexOf(dataColumnName);
                break;
            }
        }
        if (index < 0) {
            System.out.println("Error: Column not found");
        } else if (data.size() < 3) {
            System.out.println("Warning: Empty data");
        } else {
            for (int i=2; i<data.size(); ++i) {
                ret.add(data.get(i).get(index));
            }
        }
        return ret;
    }
}
