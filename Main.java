import java.util.*;
import java.math.*;

public class Main {
    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        Scanner scanner = new Scanner(System.in);
        BigInteger p, q, n, t, e, d;
        System.out.println("\n\t\t\t\tHELLO FRIEND\t\n" +
                "\tIT'S PROGRAM FOR DEMONSTRATION OF RSA POWER AND RSA ENCODER/DECODER\t");

        System.out.print("\tWHAT RSA KEY SIZE YOU WANT TO GENERATE?" +
                "\n\t1.\t512 bits" +
                "\n\t2.\t1024 bits" +
                "\n\t3.\t2048 bits" +
                "\n\t4.\t4096 bits\n\n\t");
        int point = 512;
        try {
            point = scanner.nextInt();
        } catch (NumberFormatException exception) {
            System.out.println(exception.getMessage());
        }

        switch (point) {
            case 1:
                point = 512;
                break;
            case 2:
                point = 1024;
                break;
            case 3:
                point = 2048;
                break;
            case 4:
                point = 4096;
                break;
            default:
                point = 512;
                System.out.println("\t\"You have written incorrect value of bits -> default value = 512\"\n");
                break;
        }

        do {
            p = nextRandomBigInteger(point);
        } while (!isPrime(p, 20));

        do {
            q = nextRandomBigInteger(point);
        } while (!isPrime(q, 20));

        System.out.println("\n\tGENERATED NUMBER P :\t" + p);
        System.out.println("\tGENERATED NUMBER Q :\t" + q);

        n = q.multiply(p);

        BigInteger tmp = BigInteger.valueOf(1);
        t = (q.subtract(tmp)).multiply(p.subtract(tmp));

        e = eGeneration(t);
        d = dGeneration(e, t);

        System.out.println("\tRSA PUBLIC KEY\t:\t" + "(" + n + ", " + e + ")");
        System.out.println("\tRSA PRIVATE KEY\t:\t" + "(" + n + ", " + d + ")");

        long deltaTimeX = System.currentTimeMillis();

        System.out.print("\n\tENTER MESSAGE FOR ENCRYPTION :\t");
        Scanner scanner1 = new Scanner(System.in);
        String msg = scanner1.nextLine();

        long deltaTimeY = System.currentTimeMillis();

        /*Encryption*/
        ArrayList<BigInteger> encryptedMessage = new ArrayList<>();
        for (int i = 0; i < msg.length(); i++) {
            BigInteger m = BigInteger.valueOf((long)msg.charAt(i));
            encryptedMessage.add(m.modPow(e, n));
        }
        System.out.print("\n\tRESULT OF ENCRYPTION :\t");
        for (int i = 0; i < msg.length(); i++)
            System.out.print(encryptedMessage.get(i));
        System.out.println();

        /*Decryption*/
        ArrayList<BigInteger> decryptedMessage = new ArrayList<>();
        for (int i = 0; i < msg.length(); i++) {
            BigInteger m = encryptedMessage.get(i);
            decryptedMessage.add(m.modPow(d, n));
        }
        System.out.print("\n\tRESULT OF DECRYPTION :\t");
        for (int i = 0; i < msg.length(); i++)
            System.out.print((char)decryptedMessage.get(i).intValue());
        System.out.println("\n");

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        long deltaTime = deltaTimeY - deltaTimeX;
        System.out.println("\tTIME OF PROGRAM WORK :\t" + (elapsedTime - deltaTime) * 1.0/1000.0 + " sec\n");


    }
    static BigInteger nextRandomBigInteger(int k) {
        Random random = new Random();
        long n = random.nextLong();
        long number_bits = 65536; // 2^16 --> 16 bits
        n = n % number_bits;
        BigInteger bignumber = BigInteger.valueOf(n);
        do {
            bignumber = bignumber.multiply(bignumber);
        } while (bignumber.bitLength() < k);
        BigInteger result;
        do {
            result = new BigInteger(k, random);
        } while (result.compareTo(bignumber) >= 0);
        return result;
    }
    static boolean isPrime(BigInteger n, int k) {
        if (n.compareTo(BigInteger.valueOf(2)) == 0 || n.compareTo(BigInteger.valueOf(3)) == 0)
            return true;
        if (n.compareTo(BigInteger.valueOf(2)) == -1 || n.mod(BigInteger.valueOf(2)).equals(BigInteger.valueOf(0)))
            return false;
        BigInteger t = n.subtract(BigInteger.valueOf(1));
        int s = 0;
        while (t.mod(BigInteger.valueOf(2)).compareTo(BigInteger.valueOf(0)) == 0) {
            t = t.divide(BigInteger.valueOf(2));
            s++;
        }
        for (int i = 0; i < k; i++)
        {
            BigInteger a;
            do {
                long generatedLong = new Random().nextLong();
                a = BigInteger.valueOf(generatedLong);
            } while (
                    a.compareTo(BigInteger.valueOf(2)) == -1 ||
                    a.compareTo(n.subtract(BigInteger.valueOf(2))) == 0 ||
                    a.compareTo(n.subtract(BigInteger.valueOf(2))) == 1
            );

            BigInteger x = a.modPow(t, n);

            if (x.equals(BigInteger.valueOf(1)) || x.equals(n.subtract(BigInteger.valueOf(1))))
                continue;
            for (int r = 1; r < s; r++) {
                x = x.modPow(BigInteger.valueOf(2), n);
                if (x.equals(BigInteger.valueOf(1)))
                    return false;
                if (x.equals(n.subtract(BigInteger.valueOf(1))))
                    break;
            }
            if (!x.equals(n.subtract(BigInteger.valueOf(1))))
                return false;
        }
        return true;
    }
    static BigInteger eGeneration(BigInteger t) {
        BigInteger one = BigInteger.valueOf(1);
        for (BigInteger e = BigInteger.valueOf(2); e.compareTo(t) == -1; e = e.add(one)) {
            if (gcd(e, t).equals(BigInteger.valueOf(1)))
                return e;
        }
        return BigInteger.valueOf(-1);
    }
    static BigInteger gcd(BigInteger e, BigInteger t) {
        while (e.compareTo(BigInteger.valueOf(0)) == 1) {
            BigInteger tmp = e;
            e = t.mod(e);
            t = tmp;
        }
        return t;
    }
    static BigInteger dGeneration(BigInteger e, BigInteger t) {
        BigInteger d, k = BigInteger.valueOf(1);
        while (true) {
            k = k.add(t);
            if (k.mod(e).equals(BigInteger.valueOf(0))) {
                d = k.divide(e);
                return d;
            }
        }
    }
}
