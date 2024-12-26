import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Product {
    private final String name;
    private final int weight;

    // Конструктор класса
    public Product(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    // Получение веса
    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return name + " (" + weight + "kg)";
    }
}

class Warehouse {
    private final List<Product> products;

    public Warehouse() {
        products = new ArrayList<>();
        // Инициализируем склад товаром с случайным весом
        for (int i = 1; i <= 20; i++) {
            products.add(new Product("Товар " + i, new Random().nextInt(60) + 1)); // вес от 1 до 60
        }
    }

    public synchronized List<Product> getProducts(int maxWeight) {
        List<Product> loadedProducts = new ArrayList<>();
        int currentWeight = 0;

        for (Product product : products) {
            if (currentWeight + product.getWeight() <= maxWeight) {
                loadedProducts.add(product);
                currentWeight += product.getWeight();
            }

            if (currentWeight >= maxWeight) {
                break; // достичь предела веса
            }
        }

        // Удаляем загруженные продукты из склада
        products.removeAll(loadedProducts);
        return loadedProducts;
    }
}

class Loader extends Thread {
    private final Warehouse warehouse;
    private static final int MAX_LOAD_WEIGHT = 150;

    public Loader(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
        while (true) {
            List<Product> load = warehouse.getProducts(MAX_LOAD_WEIGHT);
            if (load.isEmpty()) {
                break; // Если нет больше продуктов, прекращаем работу
            }

            deliver(load);
        }
    }

    private void deliver(List<Product> load) {
        int totalWeight = load.stream().mapToInt(Product::getWeight).sum();
        System.out.println(this.getName() + " доставил: " + load + ", общий вес: " + totalWeight + "kg");
        try {
            Thread.sleep(1000); // Имитация времени на разгрузку
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class WarehouseSimulation {
    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse();
        Loader loader1 = new Loader(warehouse);
        Loader loader2 = new Loader(warehouse);
        Loader loader3 = new Loader(warehouse);

        // Запускаем грузчиков
        loader1.start();
        loader2.start();
        loader3.start();

        try {
            loader1.join();
            loader2.join();
            loader3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Все грузы доставлены!");
    }
}
