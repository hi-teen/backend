package backend.hiteen.global.util;

import java.security.SecureRandom;

public class Base62 {
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE = ALPHABET.length();
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String encode(long num) {
        if (num == 0) return "0";
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            sb.append(ALPHABET.charAt((int) (num % BASE)));
            num /= BASE;
        }
        return sb.reverse().toString();
    }

    public static String generateReferralCode(long id) {
        String base = encode(id); // Base62 변환 (짧은 고유값)
        // 랜덤 2자리 추가
        char r1 = ALPHABET.charAt(RANDOM.nextInt(BASE));
        char r2 = ALPHABET.charAt(RANDOM.nextInt(BASE));
        return base + r1 + r2;
    }
}
