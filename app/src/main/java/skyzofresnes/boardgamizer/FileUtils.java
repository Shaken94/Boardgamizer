package skyzofresnes.boardgamizer;

import android.app.Activity;
import android.content.Context;
import android.util.JsonReader;
import android.util.JsonWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LEROYJ on 26/07/2017.
 */

public final class FileUtils {

    public static List<CharacterModel> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readCharacterModelsArray(reader);
        } finally {
            reader.close();
        }
    }

    private static List<CharacterModel> readCharacterModelsArray(JsonReader reader) throws IOException {
        List<CharacterModel> characterModels = new ArrayList<>();

//        reader.beginObject();
//        String name = reader.nextName();
//        if (name.equals("characters")) {
            reader.beginArray();
            while (reader.hasNext()) {
                characterModels.add(readCharacterModel(reader));
            }
            reader.endArray();
//        }
        return characterModels;
    }

    private static CharacterModel readCharacterModel(JsonReader reader) throws IOException {
        String nameCharacter = null;
        String gender = null;
        String origin = null;
        String type = null;
        String image = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(Constantes.CHARACTER_NAME)) {
                nameCharacter = reader.nextString();
            } else if (name.equals(Constantes.CHARACTER_GENDER)) {
                gender = reader.nextString();
            } else if (name.equals(Constantes.CHARACTER_ORIGIN)) {
                origin = reader.nextString();
            } else if (name.equals(Constantes.CHARACTER_TYPE)) {
                type = reader.nextString();
            } else if (name.equals(Constantes.CHARACTER_IMAGE)) {
                image = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new CharacterModel(nameCharacter, gender, origin, type, image);
    }

    public static void writeJsonStream(OutputStream out, List<CharacterModel> characterModels) throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("  ");
        writeCharacterModelsArray(writer, characterModels);
        writer.close();
    }

    private static void writeCharacterModelsArray(JsonWriter writer, List<CharacterModel> characterModels) throws IOException {
        writer.beginArray();
        for (CharacterModel characterModel : characterModels) {
            writeCharacterModel(writer, characterModel);
        }
        writer.endArray();
    }

    private static void writeCharacterModel(JsonWriter writer, CharacterModel characterModel) throws IOException {
        writer.beginObject();
        writer.name(Constantes.CHARACTER_NAME).value(characterModel.getName());
        writer.name(Constantes.CHARACTER_GENDER).value(characterModel.getGender());
        writer.name(Constantes.CHARACTER_ORIGIN).value(characterModel.getOrigin());
        writer.name(Constantes.CHARACTER_TYPE).value(characterModel.getType());
        writer.name(Constantes.CHARACTER_IMAGE).value(characterModel.getImage());
        writer.endObject();
    };

    public static int countInJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return getJsonArraySize(reader);
        } finally {
            reader.close();
        }
    }

    private static int getJsonArraySize(JsonReader reader) throws IOException {
        int count = 0;

        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals(Constantes.CHARACTER_NAME)) {
                    count++;
                    reader.nextString();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        }
        reader.endArray();
        return count;
    }
}
