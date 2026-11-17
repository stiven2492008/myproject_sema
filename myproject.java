
class MySemaphore {
    private int tokens;

    public MySemaphore(int tokens) {
        this.tokens = tokens;
    }

    public synchronized void takeTokens() {
        while (tokens == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        tokens--;
    }

    public synchronized void backTokens() {
        tokens++;
        notifyAll();
    }
}

public class Main {
    static int[] houseCounts = new int[4];
    static int totalStudents = 0;
    static MySemaphore lock = new MySemaphore(1);

    static class Teacher extends Thread {
        int teacherId;

        public Teacher(int teacherId) {
            this.teacherId = teacherId;
        }

        public void run() {
            int first = (teacherId - 1) * 20 + 1;
            int last = teacherId * 20;

            for (int n = first; n <= last; n++) {
                int house;
                if (n % 4 == 0) house = 0;
                else if (n % 4 == 1) house = 1;
                else if (n % 4 == 2) house = 2;
                else house = 3;

                lock.takeTokens();
                houseCounts[house]++;
                totalStudents++;
                lock.backTokens();
            }
        }
    }

    public static void main(String[] args) {
        Teacher t1 = new Teacher(1);
        Teacher t2 = new Teacher(2);
        Teacher t3 = new Teacher(3);

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
        }

        System.out.println("Falcons: " + houseCounts[0]);
        System.out.println("Eagles: " + houseCounts[1]);
        System.out.println("Lions: " + houseCounts[2]);
        System.out.println("Tigers: " + houseCounts[3]);
        System.out.println("Total: " + totalStudents);
    }
}