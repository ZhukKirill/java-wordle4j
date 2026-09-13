package ru.yandex.practicum;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.PrintWriter;

class WordleGameTest {

    private static WordleDictionary dictionary;
    private static Logger logger;
    private WordleGame game;

    @BeforeAll
    static void setUp() {
        WordleDictionaryLoader wordleDictionaryLoader = new WordleDictionaryLoader();
        logger = new Logger(new PrintWriter(System.out, true));
        try {
            dictionary = wordleDictionaryLoader.loadTheDictionary("words_ru.txt");
        } catch (DictionaryFileNotFoundException | EmptyDictionaryException e) {
            logger.log(e.getMessage());
            System.out.println("Критическая ошибка. Позовите разработчика.");
        } catch (IOException e) {
            logger.logError(e);
            System.out.println("Критическая ошибка. Позовите разработчика.");
        }
    }

    @BeforeEach
    void startGame() {
        game = new WordleGame(dictionary);
        game.startGame();
        logger.log("Введите слово из 5 букв или нажмите ENTER для получения подсказки");
    }

    @AfterAll
    static void loggerClose() {
        logger.close();
    }

    @Test
    void withCorrectWordShouldReturnWinMessage() {
        String usersAnswer = game.getAnswer();
        String result = game.processingTheUsersAnswer(usersAnswer);
        logger.log("Ответ игрока: " + usersAnswer + "\n" + result + "\n" + "steps = "
                + game.getSteps() + "\n");
        assertTrue(result.endsWith("Вы победили!"));
    }

    @Test
    void withInvalidLengthShouldReturnErrorMessage() {
        String usersAnswer = "волк";
        String result = game.processingTheUsersAnswer(usersAnswer);
        logger.log("Ответ игрока: " + usersAnswer + "\n" + result + "\n" + "steps = "
                + game.getSteps() + "\n");
        assertTrue(result.endsWith("слово должно состоять из пяти символов"));
    }

    @Test
    void withNonRussianLettersShouldReturnErrorMessage() {
        String usersAnswer = "apple";
        String result = game.processingTheUsersAnswer(usersAnswer);
        logger.log("Ответ игрока: " + usersAnswer + "\n" + result + "\n" + "steps = "
                + game.getSteps() + "\n");
        assertTrue(result.endsWith("разрешены только символы русского алфавита"));
    }

    @Test
    void withWordNotInDictionaryShouldReturnErrorMessage() {
        String usersAnswer = "звать";
        String result = game.processingTheUsersAnswer(usersAnswer);
        logger.log("Ответ игрока: " + usersAnswer + "\n" + result + "\n" + "steps = "
                + game.getSteps() + "\n");
        assertTrue(result.endsWith("Этого слова нет в словаре, попробуйте еще раз"));
    }

    @Test
    void withMultipleInvalidAttemptsShouldNotIncreaseSteps() {
        String usersAnswer = "волк";
        String result = game.processingTheUsersAnswer(usersAnswer);
        logger.log("Ответ игрока: " + usersAnswer + "\n" + result + "\n" + "steps = "
                + game.getSteps() + "\n");
        usersAnswer = "apple";
        result = game.processingTheUsersAnswer("apple");
        logger.log("Ответ игрока: " + usersAnswer + "\n" + result + "\n" + "steps = "
                + game.getSteps() + "\n");
        assertEquals(0, game.getSteps());
    }

    @Test
    void withCorrectWordShouldIncreaseSteps() {
        int initialStep = game.getSteps();
        String usersAnswer = game.getAnswer();
        String result = game.processingTheUsersAnswer(usersAnswer);
        logger.log("Ответ игрока: " + usersAnswer + "\n" + result + "\n" + "steps = "
                + game.getSteps() + "\n");
        assertEquals(initialStep + 1, game.getSteps());
    }

    @Test
    void withHintsOnlyShouldReturnWinMessage() {
        String result = "";
        String usersAnswer = "";
        while (game.getSteps() < 6) {
            result = game.processingTheUsersAnswer(usersAnswer);
            if (usersAnswer.isBlank()) {
                logger.log("Подсказка:\n" + result + "\n" + "steps = " + game.getSteps() + "\n");
            } else {
                logger.log("Ответ игрока: " + usersAnswer + "\n" + result + "\n" + "steps = "
                        + game.getSteps() + "\n");
            }
            if (result.endsWith("Вы победили!")) {
                break;
            }
        }
        assertTrue(result.endsWith("Вы победили!"));
    }
}
