import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        AtomicBoolean button = new AtomicBoolean(false);
        int amountOfRepeats = 5;
        Supplier<Long> timeOut = () -> new Random().nextLong(2000);

        Runnable userRunnable = () -> {
            for (int i = 0; i < amountOfRepeats; i++) {
                System.out.println("Пользователь нажал кнопку");
                button.set(true);
                try {
                    Thread.sleep(timeOut.get());
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
            Thread.currentThread().interrupt();
        };

        Runnable boxRunnable = () -> {
            while (true) {
                if (button.get()) {
                    System.out.println("Коробка отжала кнопку");
                    button.set(false);
                }
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
            }
        };

        var userThread = new Thread(userRunnable);
        var boxTread = new Thread(boxRunnable);

        userThread.start();
        boxTread.start();

        userThread.join();

        if (userThread.isInterrupted()) {
            boxTread.interrupt();
        }

    }
}