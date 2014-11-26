/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.org.cofriends.rsa.algo;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import ua.org.cofriends.rsa.coder.ConfigEncoder;
import ua.org.cofriends.rsa.entity.Config;

public class RSA {

    private final static int N_BITS = 256;
    private final static int LENGTH_CIPHER_CHUNK = 154; // = N_BITS / 128 * 77

    private final static BigInteger TWO = new BigInteger("2");

    public static Config generate() {
        Random random = new Random();
        // Choose two large primes p and q, let N  = pq, and let p1p1 = (p-1)(q-1).
        BigInteger p = new BigInteger(N_BITS, 50, random);
        BigInteger q = new BigInteger(N_BITS, 50, random);

        return new Config(p.toString(), q.toString(), null, Config.Command.GENERATE);
    }

    /**
     * Convert a string into a BigInteger. The string should consist of ASCII
     * characters only. The ASCII codes are simply concatenated to give the
     * integer.
     *
     * @param str to be converted
     * @return big integer representation
     */
    public static BigInteger string2int(String str) {
        byte[] b = new byte[str.length()];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) str.charAt(i);
        }
        return new BigInteger(1, b);
    }

    /**
     * Convert a BigInteger into a string of ASCII characters. Each byte in the
     * integer is simply converted into the corresponding ASCII code.
     *
     * @param n to be converted
     * @return string representation
     */
    public static String int2string(BigInteger n) {
        byte[] b = n.toByteArray();
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            s.append((char) b[i]);
        }
        return s.toString();
    }

    public static Config code(Config config) {
        BigInteger p = new BigInteger(config.p);
        BigInteger q = new BigInteger(config.q);
        BigInteger N = p.multiply(q);
        BigInteger p1 = p.subtract(BigInteger.ONE);
        BigInteger q1 = q.subtract(BigInteger.ONE);;
        BigInteger p1q1 = p1.multiply(q1);
        BigInteger e = new BigInteger("" + 0x10000);
        while (e.compareTo(p1q1) >= 0 || e.add(BigInteger.ONE).gcd(p1q1).compareTo(BigInteger.ONE) != 0) {
            e = e.divide(TWO);
        }
        e = e.add(BigInteger.ONE);
        BigInteger d = e.modInverse(p1q1);
        if (config.command == Config.Command.DECODE) {
            String[] chunks = ConfigEncoder.GSON.fromJson(config.text, String[].class);
            BigInteger[] ciphertext = new BigInteger[chunks.length];
            int i = 0;
            for (String chunk : chunks) {
                ciphertext[i++] = new BigInteger(chunk);
            }

            return new Config(null, null, decode(ciphertext, N, d), config.command);
        } else {
            if ((N.bitLength() - 1) / 8 == 0) {
                return new Config(null, null, "p * q < 256(least chunk of message), but needs to be more than m", Config.Command.ERROR);
            }

            BigInteger[] ciphertext = encode(config.text, N, e);
            String[] builder = new String[ciphertext.length];
            int i = 0;
            for (BigInteger chunk : ciphertext) {
                builder[i++] = chunk.toString();
            }
            return new Config(null, null, ConfigEncoder.GSON.toJson(builder), config.command);
        }
    }

    /**
     * Apply RSA encryption to a string, using the key (N,e). The string is
     * broken into chunks, and each chunk is converted into an integer. Then
     * that integer, x, is encoded by computing x^e (mod N).
     *
     * @param plaintext to be encoded
     * @param N that is p*q
     * @param e public exponent
     * @return cipher text
     */
    public static BigInteger[] encode(String plaintext, BigInteger N, BigInteger e) {
        int charsperchunk = (N.bitLength() - 1) / 8;
        while (plaintext.length() % charsperchunk != 0) {
            plaintext += ' ';
        }
        int chunks = plaintext.length() / charsperchunk;
        BigInteger[] c = new BigInteger[chunks];
        for (int i = 0; i < chunks; i++) {
            String s = plaintext.substring(charsperchunk * i, charsperchunk * (i + 1));
            c[i] = string2int(s);
            c[i] = c[i].modPow(e, N);
        }
        return c;
    }

    /**
     * Apply RSA decryption to a string, using the key (N,d). Each integer x in
     * the array of integers is first decoded by computing x^d (mod N). Then
     * each decoded integers is converted into a string, and the strings are
     * concatenated into a single string.
     *
     * @param cyphertext to decode
     * @param N as stated in the RSA
     * @param d private exponent
     * @return plain text
     */
    public static String decode(BigInteger[] cyphertext, BigInteger N, BigInteger d) {
        StringBuilder s = new StringBuilder();
        for (BigInteger chunk : cyphertext) {
            s.append(int2string(chunk.modPow(d, N)));
        }
        return s.toString().trim();
    }
}
