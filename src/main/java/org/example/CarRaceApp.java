package org.example;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class CarRaceApp extends JFrame implements ActionListener {
    private JLabel car1Label;
    private JLabel car2Label;
    private JButton startButton;
    private JButton stopButton;

    private Thread car1Thread;
    private Thread car2Thread;

    private volatile boolean raceInProgress;
    private int car1Position;
    private int car2Position;

    public CarRaceApp() {

        setTitle("Car Race App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(400,700);
        setLayout(null); // Используем абсолютное позиционирование

        // Создание изображений автомобилей
        ImageIcon car1Icon = new ImageIcon("D:\\вуз\\course\\thread\\src\\main\\java\\org\\example\\car1.png");
        Image car1Image = car1Icon.getImage().getScaledInstance(100, 50, Image.SCALE_DEFAULT);
        car1Icon.setImage(car1Image);
        car1Label = new JLabel(car1Icon);
        car1Label.setBounds(10, 50, 100, 50);

        ImageIcon car2Icon = new ImageIcon("D:\\вуз\\course\\thread\\src\\main\\java\\org\\example\\car2.png");
        Image car2Image = car2Icon.getImage().getScaledInstance(100, 50, Image.SCALE_DEFAULT);
        car2Icon.setImage(car2Image);
        car2Label = new JLabel(car2Icon);
        car2Label.setBounds(10, 100, 100, 50);

        ImageIcon finishIcon = new ImageIcon("src/main/java/org/example/kisspng-finish-line-inc-running-clip-art-finish-line-png-transparent-image-5a782bb5afe1f5.9610071015178249497204.png");
        Image finishImage = finishIcon.getImage().getScaledInstance(100, 50, Image.SCALE_DEFAULT);
        finishIcon.setImage(finishImage);
        JLabel finishLabel = new JLabel(finishIcon);
        finishLabel.setBounds(400, 50, 100, 50);



        startButton = new JButton("Старт");
        startButton.setBounds(10, 150, 80, 30);
        startButton.addActionListener(this);

        stopButton = new JButton("Стоп");
        stopButton.setBounds(100, 150, 80, 30);
        stopButton.addActionListener(this);
        stopButton.setEnabled(false);

        // Добавление компонентов на форму
        getContentPane().setLayout(null);
        getContentPane().add(car1Label);
        getContentPane().add(car2Label);
        getContentPane().add(startButton);
        getContentPane().add(stopButton);
        getContentPane().add(finishLabel);

        pack();setSize(700,300);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CarRaceApp());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);

            // Запуск движения автомобилей
            raceInProgress = true;

            car1Thread = new Thread(new Car1Runnable());
            car2Thread = new Thread(new Car2Runnable());

            car1Thread.start();
            car2Thread.start();
        } else if (e.getSource() == stopButton) {
            stopButton.setEnabled(false);
            startButton.setEnabled(true);

            // Остановка движения автомобилей
            raceInProgress = false;

            try {
                car1Thread.join();
                car2Thread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            // Вывод результата
            if (car1Position > car2Position) {
                JOptionPane.showMessageDialog(this, "Автомобиль 1 победил!");
            } else if (car1Position < car2Position) {
                JOptionPane.showMessageDialog(this, "Автомобиль 2 победил!");
            } else {
                JOptionPane.showMessageDialog(this, "Гонка закончилась вничью!");
            }
        }
    }

    private class Car1Runnable implements Runnable {
        @Override
        public void run() {
            while (raceInProgress) {
                // Обновление позиции автомобиля 1
                car1Position += (int) (Math.random() * 10);
                car1Label.setLocation(car1Position, car1Label.getY());

                // Проверка достижения финишной черты
                if (car1Position >= 400) {
                    stopButton.doClick(); // Автоматическое нажатие кнопки "Стоп"
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class Car2Runnable implements Runnable {
        @Override
        public void run() {
            while (raceInProgress) {
                // Обновление позиции автомобиля 2
                car2Position += (int) (Math.random() * 10);
                car2Label.setLocation(car2Position, car2Label.getY());

                // Проверка достижения финишной черты
                if (car2Position >= 400) {
                    stopButton.doClick(); // Автоматическое нажатие кнопки "Стоп"
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
