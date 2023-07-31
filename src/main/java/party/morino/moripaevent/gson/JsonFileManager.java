package party.morino.moripaevent.gson;

import com.google.gson.Gson;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;

@DefaultQualifier(NonNull.class)
public class JsonFileManager<T> {
    private final Gson gson;

    public JsonFileManager(final Gson gson) {
        this.gson = gson;
    }

    public void writeJsonToFile(T data, Path filePath) throws IOException {
        String json = gson.toJson(data);
        Files.writeString(filePath, json);
    }

    public @Nullable T readJsonFromFile(Path filePath, Class<T> clazz) throws IOException {
        String json = Files.readString(filePath);
        return gson.fromJson(json, clazz);
    }

    public @Nullable T readJsonFromFile(Path filePath, Type type) throws IOException {
        String json = Files.readString(filePath);
        return gson.fromJson(json, type);
    }
}
