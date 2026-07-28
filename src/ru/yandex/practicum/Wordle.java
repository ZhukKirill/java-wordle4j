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

    public static void main(String[]args) {

        Scanner scanner = new Scanner(System.in);
        Logger logger = new Logger();

        try {
            WordleDictionaryLoader wordleDictionaryLoader = new WordleDictionaryLoader();
            WordleDictionary dictionary = wordleDictionaryLoader.loadTheDictionary("words_ru.txt");
            logger.log("Словарь загружен\n");
            WordleGame wordleGame = new WordleGame(dictionary, logger);
            wordleGame.startGame();
            logger.log("Вам необходимо угадать загаданное существительное из пяти букв");
            logger.log("Игра началась\n");
            String result;
            while (wordleGame.getSteps() < 6) {
                System.out.println("Введите слово из 5 букв или нажмите ENTER для получения подсказки");
                logger.log("Введите слово из 5 букв или нажмите ENTER для получения подсказки");
                String usersAnswer = scanner.nextLine(); // обработать исключениями случай, когда введено не то что надо
                result = wordleGame.processingTheUsersAnswer(usersAnswer);
                System.out.println(result);
                if (usersAnswer.isBlank()) {
                    logger.log("Подсказка:\n" + result + "\n" + "steps = " + wordleGame.getSteps() + "\n");
                } else {
                    logger.log("Ответ игрока: " + usersAnswer + "\n" + result + "\n" + "steps = "
                            + wordleGame.getSteps() + "\n");
                }
                if (result.endsWith("Вы победили!")) {
                    return;
                }
            }
            System.out.println("Вы проиграли:( \nОтвет - " + wordleGame.getAnswer());
            logger.log("Вы проиграли" + "\nОтвет - " + wordleGame.getAnswer() + "\nsteps = " + wordleGame.getSteps());

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
}
