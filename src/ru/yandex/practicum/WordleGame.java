package ru.yandex.practicum;

import java.util.*;
/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    private String answer;
    private HashMap<String, String> usersAnswers;
    private int steps;
    private WordleDictionary dictionary;
    private Random random = new Random();

    public WordleGame(WordleDictionary dictionary) {
        this.dictionary = dictionary;
    }

    public void startGame() {
        int dictionarySize = dictionary.getFilteredDictionary().size();
        answer = dictionary.getFilteredDictionary().get(random.nextInt(dictionarySize));
        System.out.println("Вам необходимо угадать загаданное существительное из пяти букв");
    }

    public int getSteps() {
        return steps;
    }

    public String processingTheUsersAnswer(String usersAnswer) {
        String result;
        if (usersAnswer.isBlank()) {
            return getHint();
        } else {
            usersAnswer = usersAnswer.toLowerCase().replace("ё", "е");
            if (usersAnswer.equals(answer)) {
                steps += 1;
                return "Вы победили!";
            }
            try {
                if (usersAnswer.length() != 5) throw new UserInputException("слово должно " +
                        "состоять из пяти символов");
                if (!usersAnswer.matches("[а-я\\s-]+")) throw new UserInputException("разрешены " +
                        "только символы русского алфавита");
                if (!dictionary.getFilteredDictionary().contains(usersAnswer)) throw
                        new WordNotFoundInDictionaryException("Этого слова нет " +
                                "в словаре, попробуйте еще раз");
                if (usersAnswers == null) usersAnswers = new HashMap<>();
                result = findTheRightLetters(usersAnswer);
                usersAnswers.put(usersAnswer, result);
                steps += 1;
                return result;
            } catch (UserInputException | WordNotFoundInDictionaryException e) {
                return "Ошибка ввода: " + e.getMessage();

            }
        }
    }

    private String findTheRightLetters(String usersAnswer) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < usersAnswer.length(); i++) {
            if (usersAnswer.charAt(i) == answer.charAt(i)) {
                result.append("+");
            } else if (answer.indexOf(usersAnswer.charAt(i)) != -1) result.append("^");
                else result.append("-");
        }
        return result.toString();
    }

    public String getAnswer() {
        return answer;
    }

    private String getHint() {
        String hint;
        if (usersAnswers == null) {
            int dictionarySize = dictionary.getFilteredDictionary().size();
            hint = dictionary.getFilteredDictionary().get(random.nextInt(dictionarySize));
            while (hint.equals(answer)) {
                hint = dictionary.getFilteredDictionary().get(random.nextInt(dictionarySize));
            }
            String result = processingTheUsersAnswer(hint);
            usersAnswers = new HashMap<>();
            usersAnswers.put(hint, result);
            return hint + "\n" + result;
        } else {
            HashSet<Integer> generalResultsSet = new HashSet<>();
            for (String result: usersAnswers.values()) {
                for (int i = 0; i < result.length(); i++) {
                    if (result.charAt(i) == '+') {
                        generalResultsSet.add(i);
                    }
                }
            }

            int correctRandomCharNumber = random.nextInt(answer.length());
            if (generalResultsSet.contains(correctRandomCharNumber) && generalResultsSet.size() != answer.length()) {
                boolean isNotTheRandomCharacterNumberCorrect = true;
                while (isNotTheRandomCharacterNumberCorrect) {
                    correctRandomCharNumber = random.nextInt(answer.length());
                    isNotTheRandomCharacterNumberCorrect = generalResultsSet.contains(correctRandomCharNumber);
                }
            }
            generalResultsSet.add(correctRandomCharNumber);


            List<String> filteredDictionary = dictionary.getFilteredDictionary();
            for (Integer symbolNumber: generalResultsSet) {
                List<String> temp = new ArrayList<>();
                for (String word: filteredDictionary) {
                    if (word.charAt(symbolNumber) == answer.charAt(symbolNumber))
                        temp.add(word);
                }
                filteredDictionary = temp;
            }
            hint = filteredDictionary.get(random.nextInt(filteredDictionary.size()));
            String result = processingTheUsersAnswer(hint);
            usersAnswers.put(hint, result);
            return hint + "\n" + result;
        }
    }
}