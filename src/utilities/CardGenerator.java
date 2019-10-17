package utilities;

import cards.Card;
import cards.UnitCard;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CardGenerator {
    public List<Card> generateFromJson(String path, Type collectionType) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        List<Card> cards = gson.fromJson(br, collectionType);

        Collections.shuffle(cards);


        return cards;
    }
}
