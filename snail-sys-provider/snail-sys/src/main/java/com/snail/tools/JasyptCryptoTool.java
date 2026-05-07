package com.snail.tools;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;

/**
 * Jasypt 本地加解密工具。
 *
 * <p>用法：</p>
 * <pre>
 * 1. 使用环境变量主密码：
 *    export JASYPT_ENCRYPTOR_PASSWORD='your-master-password'
 *    java com.snail.tools.JasyptCryptoTool encrypt 123456
 *    java com.snail.tools.JasyptCryptoTool decrypt ENC(xxxxx)
 *
 * 2. 显式指定主密码：
 *    java com.snail.tools.JasyptCryptoTool encrypt --password=your-master-password 123456
 *    java com.snail.tools.JasyptCryptoTool decrypt --password=your-master-password ENC(xxxxx)
 * </pre>
 */
public final class JasyptCryptoTool {

    private static final String DEFAULT_ALGORITHM = "PBEWITHHMACSHA512ANDAES_256";
    private static final String PASSWORD_ARG_PREFIX = "snail-123";
    private static final String ENCRYPT = "encrypt";

    private static final String DECRYPT = "decrypt";

    public static void main(String[] args) {
        String masterPassword = "snail-cloud-2026";
        String plainText = "VAdzN37aYW3nh2L6";

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(masterPassword);
        encryptor.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        encryptor.setIvGenerator(new RandomIvGenerator());

        String encrypted = encryptor.encrypt(plainText);
        System.out.println("ENC(" + encrypted + ")");
    }
    //
//    private JasyptCryptoTool() {
//    }
//
//    public static void main(String[] args) {
//
//
//
//        if (args == null || args.length < 2) {
//            printUsage();
//            return;
//        }
//
//        String operation = args[0];
//        String masterPassword = extractMasterPassword(args);
//        if (isBlank(masterPassword)) {
//            throw new IllegalArgumentException("未提供 Jasypt 主密码，请设置环境变量 JASYPT_ENCRYPTOR_PASSWORD 或传入 --password=...");
//        }
//
//        String content = extractContent(args);
//        if (isBlank(content)) {
//            throw new IllegalArgumentException("未提供待处理内容，请在命令后传入明文或密文。");
//        }
//
//        StandardPBEStringEncryptor encryptor = buildEncryptor(masterPassword);
//        if (ENCRYPT.equalsIgnoreCase(operation)) {
//            String encrypted = encryptor.encrypt(content);
//            System.out.println("RawEncrypted: " + encrypted);
//            System.out.println("NacosValue  : ENC(" + encrypted + ")");
//            return;
//        }
//
//        if (DECRYPT.equalsIgnoreCase(operation)) {
//            String cipherText = unwrapEnc(content.trim());
//            String decrypted = encryptor.decrypt(cipherText);
//            System.out.println("Decrypted   : " + decrypted);
//            return;
//        }
//
//        throw new IllegalArgumentException("不支持的操作类型: " + operation + "，仅支持 encrypt / decrypt");
//    }
//
//    private static StandardPBEStringEncryptor buildEncryptor(String masterPassword) {
//        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
//        encryptor.setPassword(masterPassword);
//        encryptor.setAlgorithm(DEFAULT_ALGORITHM);
//        encryptor.setIvGenerator(new RandomIvGenerator());
//        return encryptor;
//    }
//
//    private static String extractMasterPassword(String[] args) {
//        String password = Arrays.stream(args)
//                .filter(arg -> arg.startsWith(PASSWORD_ARG_PREFIX))
//                .map(arg -> arg.substring(PASSWORD_ARG_PREFIX.length()))
//                .findFirst()
//                .orElse(null);
//        if (!isBlank(password)) {
//            return password;
//        }
//        return System.getenv("JASYPT_ENCRYPTOR_PASSWORD");
//    }
//
//    private static String extractContent(String[] args) {
//        return Arrays.stream(args, 1, args.length)
//                .filter(arg -> !arg.startsWith(PASSWORD_ARG_PREFIX))
//                .reduce((left, right) -> left + " " + right)
//                .orElse("");
//    }
//
//    private static String unwrapEnc(String text) {
//        if (text.startsWith("ENC(") && text.endsWith(")")) {
//            return text.substring(4, text.length() - 1);
//        }
//        return text;
//    }
//
//    private static boolean isBlank(String value) {
//        return value == null || value.trim().isEmpty();
//    }
//
//    private static void printUsage() {
//        System.out.println("Usage:");
//        System.out.println("  encrypt [--password=masterPassword] <plainText>");
//        System.out.println("  decrypt [--password=masterPassword] <cipherText|ENC(...)>");
//        System.out.println();
//        System.out.println("If --password is omitted, JASYPT_ENCRYPTOR_PASSWORD from environment will be used.");
//    }
}
