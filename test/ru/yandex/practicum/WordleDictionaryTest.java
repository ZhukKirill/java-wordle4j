package ru.yandex.practicum;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryTest {

    @Test
    void shouldReturnOnlyTheWordsWithTheCorrectLength() {
        List<String> words = new ArrayList<>();
        words.add("голос");
        words.add("номер");
        words.add("лиса");
        WordleDictionary dictionary = new WordleDictionary(words);
        List<String> filteredWords = dictionary.getFilteredDictionary();
        assertEquals(2, filteredWords.size(), "Должно быть 2 слова длиной 5");
        assertTrue(filteredWords.contains("голос"));
        assertTrue(filteredWords.contains("номер"));
        assertFalse(filteredWords.contains("лиса"));
    }

    @Test
    void shouldReplaceLetterYoWithYe() {
        List<String> words = new ArrayList<>();
        words.add("житьё");
        WordleDictionary dictionary = new WordleDictionary(words);
        List<String> filteredWords = dictionary.getFilteredDictionary();
        assertFalse(filteredWords.getFirst().contains("ё"));
    }
}
