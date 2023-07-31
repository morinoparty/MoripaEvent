package party.morino.moripaevent.gson;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.UUID;

public class UUIDSerializer implements JsonSerializer<UUID>, JsonDeserializer<UUID> {

    @Override
    public JsonElement serialize(UUID uuid, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(uuid.toString());
    }

    @Override
    public UUID deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return UUID.fromString(json.getAsString());
    }
}
