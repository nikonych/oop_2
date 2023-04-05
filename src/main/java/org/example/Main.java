package org.example;

import org.example.DAO.BetDAO;
import org.example.DAO.HorseDAO;
import org.example.DAO.UserDAO;
import org.example.enums.BetStatus;
import org.example.enums.BetType;
import org.example.enums.Role;
import org.example.model.Bet;
import org.example.model.Horse;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static org.example.Decor.*;

public class Main {
    public static void main(String[] args) {
        runApp();
    }


    private static void runApp() {
        Scanner scanner = new Scanner(System.in);
        String answer;
        Boolean isRun = true;
        while(isRun) {
            printBlue("1) Войти \n" +
                    "2) Регистрация \n" +
                    "3) Выход :(");
            answer = scanner.next();
            switch (answer){
                case "1": {
                    clearScreen();
                    login(scanner);
                    break;
                }
                case "2": {
                    clearScreen();
                    signup(scanner);
                    break;
                }
                case "3": {
                    isRun = false;
                    break;
                }
            }
        }
    }
    private static void signup(Scanner scanner) {
        printBlue("Имя:");
        String name = scanner.next();
        User user = new User(name);
        try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            session.close();
            printGreen("Успешно!");
        }catch (Exception e){
            printRed("Такой пользователь уже есть :(");
        }
    }
    private static void login(Scanner scanner) {
        printBlue("Имя:");
        String name = scanner.next();
        UserDAO userDAO = new UserDAO();
        User user = userDAO.authenticate(name);
        if(user != null){
            printGreen("Успешный вход!");
            mainMenu(scanner, user);
        }else {
            printRed("Неверный пароль");
        }
    }

    private static void mainMenu(Scanner scanner, User user) {
        switch (user.getRole()){
            case USER -> {
                userMenu(scanner, user);
            }
            case ADMIN -> {
                adminMenu(scanner, user);
            }
        }
    }

    private static void userMenu(Scanner scanner, User user) {
        Boolean isRun = true;
        while (isRun) {
            printBlue("1) Сделать ставку\n" +
                            "2) История ставок \n" +
                    "3) Выход\n");
            String answer = scanner.next();
            switch (answer) {
                case "1" -> {
                    setBet(scanner, user);
                }
                case "2" -> {
                    showHistory(user);
                }
                case "3" -> {
                    isRun = false;
                }
            }
        }
    }

    private static void showHistory(User user) {
        BetDAO betDAO = new BetDAO();
        List<Bet> bets = betDAO.getBets(user);
        if(!bets.isEmpty()){
            for (Bet bet: bets) {
                printGreen("Ставка на лошадь " + bet.getHorse().getName() + " статус: " + bet.getBetStatus() + " сумма: " + bet.getCash() + " " + bet.getBet() + "x");
            }
        } else {
            printRed("У вас нет ставок");
        }
    }

    private static void adminMenu(Scanner scanner, User user) {
        Boolean isRun = true;
        while (isRun) {
            printBlue("1) Сделать ставку\n" +
                    "2) История ставок \n" +
                    "3) Добавить лошадь\n" +
                    "4) Результат\n" +
                    "5) Выход\n");
            String answer = scanner.next();
            switch (answer){
                case "1" -> {
                    setBet(scanner, user);
                }
                case "2" -> {
                    showHistory(user);
                }
                case "3" -> {
                    addHorse(scanner);
                }
                case "4" -> {
                    printResult();
                }
                case "5" -> {
                    isRun = false;
                }
            }
        }

    }

    private static void printResult() {
        HorseDAO horseDAO = new HorseDAO();
        List<Horse> horses = horseDAO.getHorses();
        Collections.shuffle(horses);
        BetDAO betDAO = new BetDAO();
        List<Bet> bets = betDAO.getBets();
        for (Bet bet: bets) {
            if (bet.getBetType() == BetType.WIN) {
                if(bet.getHorse().equals(horses.get(0))){
                    bet.setBetStatus(BetStatus.WIN);
                    User user = bet.getUser();
                    user.setBalance((int) (user.getBalance() + (bet.getCash() * bet.getBet())));
                    UserDAO userDAO = new UserDAO();
                    userDAO.updateUser(user);
                    betDAO.updateBet(bet);
                    printGreen("Cтавка игрока " + bet.getUser().getName() + " выиграла на сумму " + (bet.getCash() * bet.getBet()));
                } else {
                    bet.setBetStatus(BetStatus.LOSE);
                    betDAO.updateBet(bet);
                    printRed("Игрок " + bet.getUser().getName() + " потерял " + (bet.getCash()));
                }
            } else if (bet.getBetType() == BetType.PLACE) {
                if(!bet.getHorse().equals(horses.get(0))){
                    bet.setBetStatus(BetStatus.WIN);
                    User user = bet.getUser();
                    user.setBalance((int) (user.getBalance() + (bet.getCash() * bet.getBet())));
                    UserDAO userDAO = new UserDAO();
                    userDAO.updateUser(user);
                    betDAO.updateBet(bet);
                    printGreen("Cтавка игрока " + bet.getUser().getName() + " выиграла на сумму " + (bet.getCash() * bet.getBet()));
                } else {
                    bet.setBetStatus(BetStatus.LOSE);
                    betDAO.updateBet(bet);
                    printRed("Игрок " + bet.getUser().getName() + " потерял " + (bet.getCash()));
                }
            }
        }
    }

    private static void setBet(Scanner scanner, User user) {
        BetType betType = null;
        while (betType == null) {
            printBlue("Тип ставки: \n" +
                    "1) Кто выиграет\n" +
                    "2) Кто не выиграет");
            String answer = scanner.next();
            switch (answer) {
                case "1" -> {
                    betType = BetType.WIN;
                }
                case "2" -> {
                    betType = BetType.PLACE;
                }
            }
        }
        HorseDAO horseDAO = new HorseDAO();
        List<Horse> horses = horseDAO.getHorses();
        if(!horses.isEmpty()) {
            Integer count = printHorses(horseDAO.getHorses(), betType);
            printGreen("Выберите лошадь от 0 .. " + (horses.size()-1));
            String answer = scanner.next();
            try {
                if(Integer.parseInt(answer) >= 0 && Integer.parseInt(answer) < horses.size()){
                    printGreen("Введите сумму ставки:");
                    String cash = scanner.next();
                    try {
                        if(Integer.parseInt(cash) > 0 && Integer.parseInt(cash) <= user.getBalance()){
                            UserDAO userDAO = new UserDAO();
                            user.setBalance(user.getBalance()-Integer.parseInt(cash));
                            userDAO.updateUser(user);
                            Horse horse = horses.get(Integer.parseInt(answer));
                            Bet bet = new Bet(user, Integer.parseInt(cash), getBet(horses.get(Integer.parseInt(answer)), betType, count), horse, betType);
                            horse.setBetCount(horse.getBetCount()+1);
                            horseDAO.updateHorse(horse);
                            printGreen("Успешно");
                            BetDAO betDAO = new BetDAO();
                            betDAO.addBet(bet);
                        }
                    } catch (NumberFormatException e) {
                        printRed("Ошибка");
                    }
                }
            } catch (Exception e){
                printRed("Ошибка");
            }
        } else {
            printRed("Нету лошадей :(");
        }


    }

    private static Double getBet(Horse horse, BetType betType, Integer count) {
        if (betType == BetType.WIN) {
            return round(95/ ((double) horse.getBetCount()/count * 100), 2);
        } else if (betType == BetType.PLACE) {
            return round(95/( (((double) count - horse.getBetCount())/count * 100)), 2);
        }
        return null;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    private static void addHorse(Scanner scanner) {
        HorseDAO horseDAO = new HorseDAO();
        printBlue("Имя лошади:");
        String name = scanner.next();
        if(horseDAO.addHose(new Horse(name))){
            printGreen("Успешно");
        } else {
            printRed("Ошибка");
        }
    }

}