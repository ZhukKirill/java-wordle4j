package ru.yandex.practicum;

import java.io.IOException;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {

    public WordleDictionary loadTheDictionary(String path) throws DictionaryFileNotFoundException,
            EmptyDictionaryException, IOException {
        if (!Files.exists(Paths.get(path))) throw new  DictionaryFileNotFoundException("Файл словаря " +
                path + " не найден\n");
        List<String> words = Files.readAllLines(Path.of(path), StandardCharsets.UTF_8);
        if (words.isEmpty()) throw new EmptyDictionaryException("Словарь " + path + " пустой\n");
        return new WordleDictionary(words);
    }
}
