package meetup_objects;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import java.lang.reflect.Type;
import java.util.Date;

public abstract class MUModel {

    public abstract String uniqueKey();


    public static class JsonTimeSerializer implements com.google.gson.JsonSerializer<Date> {
        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext
                context) {
            return src == null ? null : new JsonPrimitive(src.getTime()/1000);
        }

    }

    public static class JsonTimeDeserializer implements JsonDeserializer {

        @Override
        public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new Date(json.getAsLong() * 1000);
        }
    }

}
