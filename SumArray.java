public class SumArray {
    //Создание класса потока
    private static class SumThread extends Thread {
        private final int[] array;
        private final int start;
        private final int end;
        private int sum;
        //конструктор
        public SumThread(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }
        //переопределения метода run
        @Override
        public void run() {
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
        }

        //сумма
        public int getSum() {
            return sum;
        }
    }

    public static void main(String[] args) {
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8};

        int mid = array.length / 2;

        // Создаем потоки для вычисления сумм
        SumThread thread1 = new SumThread(array, 0, mid);
        SumThread thread2 = new SumThread(array, mid, array.length);

        // Запускаем потоки
        thread1.start();
        thread2.start();

        try {
            // Ожидаем завершения потоков
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Складываем результаты
        int totalSum = thread1.getSum() + thread2.getSum();

        System.out.println("Сумма элементов массива: " + totalSum);
    }
}