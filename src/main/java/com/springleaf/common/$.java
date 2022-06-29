package com.springleaf.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.springleaf.common.ulti.RandomPasswordGenerator;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;

@Slf4j
public class $ {
    private static final RandomPasswordGenerator rpg = RandomPasswordGenerator.getInstance();
    private static final Gson parser = initGsonParser();
    private static final Gson prettyJson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    private static Gson initGsonParser() {
//        CustomizedObjectTypeAdapter adapter = new CustomizedObjectTypeAdapter();
//        return new GsonBuilder()
//                .registerTypeAdapter(Map.class, adapter)
//                .registerTypeAdapter(List.class, adapter)
//                .create();
//        return new GsonBuilder().registerTypeAdapter(new TypeToken<Map<String, Object>>() {
//        }.getType(), new MapDeserializerDoubleAsIntFix()).create();
        GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping();
        gsonBuilder.registerTypeAdapter(new TypeToken<HashMap<String, Object>>() {
        }.getType(), new $.MapDeserializerDoubleAsIntFix());
        Gson gson = gsonBuilder.create();
        return gson;
    }

    public static <T> T[] concat(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    public static String random(int length) {
        return rpg.generate(length);
    }

    public static String randomNumeric(int length) {
        return rpg.generateNumeric(length);
    }
    public static long elapse(long t) {
        return System.currentTimeMillis() - t;
    }
    public static byte[] randomByte(int length) {
        byte[] result = new byte[length];
        new SecureRandom().nextBytes(result);
        return result;
    }

    public static boolean isNull(Object o) {
        if (o == null)
            return true;
        return false;
    }

    public static boolean isNull(Object... o) {
        for (Object obj : o) {
            if (isNull(obj)) return true;
        }
        return false;
    }

    public static boolean isEmpty(Object dn) {
        return dn == null || "".equals(dn);
    }

    public static boolean isEmpty(Collection dn) {
        return dn == null || dn.isEmpty();
    }

    public static Map<String, Object> parseJSONData(String jsonString) throws IllegalArgumentException {
//        log.debug("Json string to parse: " + jsonString);
        try {
            Map<String, Object> retMap = parser.fromJson(jsonString, new TypeToken<HashMap<String, Object>>() {
            }.getType());
//            Map<String, Object> retMap = new Gson().fromJson(jsonString, new TypeToken<HashMap<String, Object>>() {
//            }.getType());
            return retMap;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid json");
        }
    }

    public static String prettyStr(Object obj) {
        return prettyJson.toJson(obj);
    }
    public static <T> T parse(String json, Class<? extends T> clazz) {
        return parser.fromJson(json, clazz);
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String encodeHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] decodeHex(String s) {
        int len = s.length();
        if (len % 2 == 1) {
            s = s + "0";
            len += 1;
        }
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static List<String> sort(Collection<String> collection) {
        String[] arr = collection.toArray(new String[0]);
        Arrays.sort(arr);
        return new ArrayList<>(Arrays.asList(arr));
    }
    public static List<Object> sortObj(Collection<? extends Object> collection) {
        Object[] arr = collection.toArray(new Object[0]);
        Arrays.sort(arr);
        return new ArrayList<>(Arrays.asList(arr));
    }
    public static byte[] getDigest(Map<? extends Object, ? extends Object> map, List<Object> ignoresList) {
        List<Object> keySorted = sortObj(map.keySet());
        StringBuilder sb = new StringBuilder();
        for (Object key : keySorted) {
            if (ignoresList.contains(key)) continue;
            Object value = map.get(key);
            if (value == null || "".equals(value)) continue;
            sb.append(key + $.toString(value));
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
    public static List<String> parseListData(String jsonString) {
        try {
            ArrayList<String> retMap = parser.fromJson(jsonString, new TypeToken<ArrayList<String>>() {
            }.getType());
            return retMap;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid json");
        }
    }

    public static <T> List<T> parseListObj(String jsonString, Class<T> clazz) {
        log.debug("Json string to parse list: " + jsonString);
        List<T> result = new ArrayList<>();
        try {
            ArrayList retMap = parser.fromJson(jsonString, new TypeToken<ArrayList>() {
            }.getType());
            for (Object obj : retMap) {
                String objStr = $.toString(obj);
                T t = parser.fromJson(objStr, clazz);
                result.add(t);
            }
            return result;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid json");
        }
    }

    public static <T> T parseObj(String json, Class<T> clazz) {
        try {
            return parser.fromJson(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T parseObj(Map<String, Object> map, Class<T> clazz) {
        try {
            JsonElement jsonElement = parser.toJsonTree(map);
            return parser.fromJson(jsonElement, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public static String toString(Object o) {
        return parser.toJson(o);
    }

    public static String toString(Object o, Type type) {
        return parser.toJson(o, type);
    }

    public static byte[] toByte(Object obj) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            return baos.toByteArray();
        }
    }

    public static byte[] concat(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    public static byte[] concats(byte[]... a) {
        if (a.length == 1) return a[0];
        if (a.length > 1) {
            byte[] result = concat(a[0], a[1]);
            for (int i = 2; i < a.length; i++) {
                result = concat(result, a[i]);
            }
            return result;
        } else throw new IllegalArgumentException();
    }

    public static String mapTo(Map<String, Object> keyAttr) {
        return parser.toJson(keyAttr);
    }

    public static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.toString(e.getEnumConstants()).replaceAll("^.|.$", "").split(", ");
    }

    public static Map<String, Object> toMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(obj));
            } catch (Exception e) {
            }
        }
        return map;
    }
    public static Map<String, Map> analyzeToMap(String[] strsWithDot) {
        Map<String, Map> result = new HashMap<>();
        Arrays.stream(strsWithDot).forEach(s -> addToMap(s, result, "."));
        return result;
    }
    private static void addToMap(String s, Map<String, Map> upperLevel, String delimiter) {
        int dotPos = s.indexOf(delimiter);
        if (dotPos < 0) {
            upperLevel.put(s, null);
        } else {
            String key = s.substring(0, dotPos);
            addToMap(s.substring(dotPos + 1), upperLevel.computeIfAbsent(key, k -> new HashMap<>()), delimiter);
        }
    }

    public static String toString(Map<String, Object> keyAttr) {
        return parser.toJson(keyAttr);
    }

    public static String encode(byte[] a) {
        return Base64.getEncoder().encodeToString(a);
    }

    public static String encodeUrl(byte[] a) {
        return Base64.getUrlEncoder().encodeToString(a);
    }

    public static byte[] decode(String a) {
        return Base64.getDecoder().decode(a);
    }

    public static byte[] decodeUrl(String a) {
        return Base64.getUrlDecoder().decode(a);
    }


    public static void setNewEnvironmentHack(Map<String, String> newenv) throws Exception {
        Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
        Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
        theEnvironmentField.setAccessible(true);
        Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
        env.clear();
        env.putAll(newenv);
        Field theCaseInsensitiveEnvironmentField = processEnvironmentClass
                .getDeclaredField("theCaseInsensitiveEnvironment");
        theCaseInsensitiveEnvironmentField.setAccessible(true);
        Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
        cienv.clear();
        cienv.putAll(newenv);
    }

    public static void close(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {

        }
    }

    static class CustomizedObjectTypeAdapter extends TypeAdapter<Object> {

        private final TypeAdapter<Object> delegate = new Gson().getAdapter(Object.class);

        @Override
        public void write(JsonWriter out, Object value) throws IOException {
            delegate.write(out, value);
        }

        @Override
        public Object read(JsonReader in) throws IOException {
            JsonToken token = in.peek();
            switch (token) {
                case BEGIN_ARRAY:
                    List<Object> list = new ArrayList<Object>();
                    in.beginArray();
                    while (in.hasNext()) {
                        list.add(read(in));
                    }
                    in.endArray();
                    return list;

                case BEGIN_OBJECT:
                    Map<String, Object> map = new LinkedTreeMap<String, Object>();
                    in.beginObject();
                    while (in.hasNext()) {
                        map.put(in.nextName(), read(in));
                    }
                    in.endObject();
                    return map;

                case STRING:
                    return in.nextString();

                case NUMBER:
                    //return in.nextDouble();
                    String n = in.nextString();
                    if (n.indexOf('.') != -1) {
                        return Double.parseDouble(n);
                    }
                    return Integer.valueOf(n);

                case BOOLEAN:
                    return in.nextBoolean();

                case NULL:
                    in.nextNull();
                    return null;

                default:
                    throw new IllegalStateException();
            }
        }
    }

    public static class MapDeserializerDoubleAsIntFix implements JsonDeserializer<Map<String, Object>> {

        @Override
        public Map<String, Object> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return (Map<String, Object>) read(json);
        }

        public Object read(JsonElement in) {
            if (in.isJsonArray()) {
                List<Object> list = new ArrayList<Object>();
                JsonArray arr = in.getAsJsonArray();
                for (JsonElement anArr : arr) {
                    list.add(read(anArr));
                }
                return list;
            } else if (in.isJsonObject()) {
                return readRecursively(in.getAsJsonObject());
            } else if (in.isJsonPrimitive()) {
                JsonPrimitive prim = in.getAsJsonPrimitive();
                if (prim.isBoolean()) {
                    return prim.getAsBoolean();
                } else if (prim.isString()) {
                    return prim.getAsString();
                } else if (prim.isNumber()) {
                    Number num = prim.getAsNumber();
                    if (Math.ceil(num.doubleValue()) == num.intValue())
                        return num.intValue();
                    else {
                        return num.doubleValue();
                    }
                }
            }
            return null;
        }
        private Map<String, Object> readRecursively(JsonObject obj) {
            Map<String, Object> map = new LinkedTreeMap<String, Object>();
            Set<Map.Entry<String, JsonElement>> entitySet = obj.entrySet();
            for (Map.Entry<String, JsonElement> entry : entitySet) {
                if (entry.getValue().isJsonObject()) {
                    map.put(entry.getKey(), readRecursively(entry.getValue().getAsJsonObject()));
                } else {
                    map.put(entry.getKey(), read(entry.getValue()));
                }
            }
            return map;
        }
    }

    private static class CustomType<T> {
        protected CustomType() {

        }
    }

    public static String getPathByEnviroment(String path) {
        String _path = "";
        if (Config.isWindowsOS()) {
            _path = path.replace("/", "\\");
            return _path;
        }
        else {
            _path = path.replace("\\", "/");
            return _path;
        }
    }

    public static String slashTrim(String path) {
        if (Config.isWindowsOS()) {
            return path.replaceAll("\\$", "").replaceAll("^\\\\", "");
        }
        else {
            return path.replaceAll("^/", "").replaceAll("/$","");
        }
    }

    public static boolean isMultipleEmpty(Object... strs) {
        for (Object str : strs) {
            if (isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    public static String genUUID() {
        String s = UUID.randomUUID().toString();
        return s;
    }

    public static <T> T mapToObject(Map map, Class<? extends T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(map, clazz);
    }
}
