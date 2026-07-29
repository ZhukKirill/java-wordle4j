package ru.yandex.practicum;

import java.nio.file.*;
import java.io.IOException;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {

    private Scanner scanner;
    private Logger logger;
    private  WordleDictionary dictionary;
    private WordleGame game;

    public Wordle() {
        scanner = new Scanner(System.in);
        logger = new Logger();
    }

    public static void main(String[] args) {
        Wordle wordle = new Wordle();
        wordle.run();
    }

    public void run() {
        try {
            loadDictionary();
            start();
            play();
        } catch (DictionaryFileNotFoundException | EmptyDictionaryException e) {
            logger.log(e.getMessage());
            System.out.println("Критическая ошибка. Позовите разработчика.");
        } catch (IOException e) {
            logger.logError(e);
            System.out.println("Критическая ошибка. Позовите разработчика.");
        } catch (Exception e) {
            logger.logError(e);
            System.out.println("Критическая ошибка. Позовите разработчика.");
        } finally {
            logger.close();
        }
    }

    private void play() {
        String result;
        while (game.getSteps() < 6) {
            System.out.println("Введите слово из 5 букв или нажмите ENTER для получения подсказки");
            logger.log("Введите слово из 5 букв или нажмите ENTER для получения подсказки");
            String usersAnswer = scanner.nextLine();
            result = game.processingTheUsersAnswer(usersAnswer);
            System.out.println(result);
            logStep(usersAnswer, result);
            if (result.endsWith("Вы победили!")) {
                return;
            }
        }
        System.out.println("Вы проиграли:( \nОтвет - " + game.getAnswer());
        logger.log("Вы проиграли" + "\nОтвет - " + game.getAnswer() + "\nsteps = " + game.getSteps());
    }

    private void logStep(String usersAnswer, String result) {
        if (usersAnswer.isBlank()) {
            logger.log("Подсказка:\n" + result + "\n" + "steps = " + game.getSteps() + "\n");
        } else {
            logger.log("Ответ игрока: " + usersAnswer + "\n" + result + "\n" + "steps = "
                    + game.getSteps() + "\n");
        }
    }

    private void loadDictionary() throws DictionaryFileNotFoundException, EmptyDictionaryException, IOException {
        WordleDictionaryLoader wordleDictionaryLoader = new WordleDictionaryLoader();
        dictionary = wordleDictionaryLoader.loadTheDictionary("words_ru.txt");
        logger.log("Словарь загружен\n");
    }

    private void start() {
        game = new WordleGame(dictionary);
        game.startGame();
        logger.log("Вам необходимо угадать загаданное существительное из пяти букв");
        logger.log("Игра началась\n");
    }

}
