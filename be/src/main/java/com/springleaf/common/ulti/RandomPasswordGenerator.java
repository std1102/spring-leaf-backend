package com.springleaf.common.ulti;

import java.security.SecureRandom;
import java.util.Random;

public class RandomPasswordGenerator {

    public static final String ALPHANUMERIC
            = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static final String NUMERIC = "0123456789";
    private static final int DEFAULT_NUM_WORDS = 1;
    private static final int DEFAULT_NUM_CHARACTERS = 8;
    private static RandomPasswordGenerator instance;
    Random random = new SecureRandom();

    private RandomPasswordGenerator() {
    }

    public static RandomPasswordGenerator getInstance() {
        if (instance == null) {
            instance = new RandomPasswordGenerator();
        }
        return instance;
    }

    public static void main(String[] args) {
        String alphabet = ALPHANUMERIC;
        int words = DEFAULT_NUM_WORDS;
        int characters = DEFAULT_NUM_CHARACTERS;

        // Check number of arguments
        if (args.length < 1 || args.length > 4 || !"generate".equals(args[0])) {
            System.err.println("USAGE: java RandPassGen generate"
                    + " [LENGTH] [WORDS] [ALPHABETH]");
            System.err.println("Example:");
            System.err.println("randompasswordgenerator generate 9 1 "
                    + ALPHANUMERIC);
            return;
        }

        // Read arguments
        if (args.length > 1) {
            characters = Integer.parseInt(args[1]);
        }
        if (args.length > 2) {
            words = Integer.parseInt(args[2]);
        }
        if (args.length > 3) {
            alphabet = args[3];
        }

        // Generate all passwords
        for (int word = 0; word < words; word++) {
            System.out.println(RandomPasswordGenerator.getInstance().generate(characters,
                    alphabet));
        }
    }
    public String generateNumeric(final int length) {
        return new String(fill(new char[length], NUMERIC));
    }
    public String generate(final int length) {
        return new String(fill(new char[length]));
    }

    public char[] generate(final int length, final String alphabeth) {
        return fill(new char[length], alphabeth);
    }

    public char[] fill(final char[] buff) {
        return fill(buff, ALPHANUMERIC);
    }

    public char[] fill(final char[] buff, final String alphabeth) {
        for (int i = 0; i < buff.length; i++) {
            buff[i] = alphabeth.charAt(random.nextInt(alphabeth.length()));
        }
        return buff;
    }

}
