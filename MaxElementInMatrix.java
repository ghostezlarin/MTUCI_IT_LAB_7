import java.util.concurrent.CountDownLatch;

public class MaxElementInMatrix {
    //Создание класса-потока
    private static class MaxElementThread extends Thread {
        private final int[] row;
        private final CountDownLatch latch;
        private int maxElement;
        //конструктор
        public MaxElementThread(int[] row, CountDownLatch latch) {
            this.row = row;
            this.latch = latch;
        }
        //переопределения метода run
        @Override
        public void run() {
            maxElement = row[0];
            for (int element : row) {
                if (element > maxElement) {
                    maxElement = element;
                }
            }
            latch.countDown(); // Уменьшаем счетчик завершенных потоков
        }

        public int getMaxElement() {
            return maxElement;
        }
    }

    public static void main(String[] args) {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        CountDownLatch latch = new CountDownLatch(matrix.length);
        MaxElementThread[] threads = new MaxElementThread[matrix.length];

        // Создаем и запускаем потоки
        for (int i = 0; i < matrix.length; i++) {
            threads[i] = new MaxElementThread(matrix[i], latch);
            threads[i].start();
        }

        try {
            // Ожидаем завершения всех потоков
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Находим наибольший элемент
        int overallMax = threads[0].getMaxElement();
        for (MaxElementThread thread : threads) {
            int currentMax = thread.getMaxElement();
            if (currentMax > overallMax) {
                overallMax = currentMax;
            }
        }

        System.out.println("Наибольший элемент матрицы: " + overallMax);
    }
}