package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
*/
public class WordleDictionary {

    private List<String> words;

    public WordleDictionary(List<String> words) {
        this.words = words;
    }

    public List<String> getFilteredDictionary() {
        List<String> words = new ArrayList<>();
        for (String word: this.words) {
            if (word.length() != 5) continue;
            word = word.toLowerCase().replace("ё", "е");
            words.add(word);
        }
        return words;
    }
}
