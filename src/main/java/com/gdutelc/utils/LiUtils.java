package com.gdutelc.utils;

import com.gdutelc.framework.common.HttpStatus;
import com.gdutelc.framework.exception.ServiceException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;
import java.util.Hashtable;

/**
 * @author shan-liangguang
 * @version 1.0
 * @since 2023/9/29 01:05
 * LiUtil
 */
@Component
@Slf4j
public class LiUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(LiUtils.class);

    private static final String KEY_STR = "f345159c1f9a08a9"; // f345159c1f9a08a9

    private static final String initVector = "as5d41gg09789ghy";

    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    private static final String ECB_ALGORITHM = "AES/ECB/PKCS7Padding";

    // Deprecated secret
    //private static final String SECRET = "rYnZ5jv7rLfsnd96";

    /**
     * AES/CBC/PKCS5Padding Secret
     */
    @Value(value = "${gdutday.secret}")
    private String secret;

    private static String secretKey;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @PostConstruct
    public void init() {
        secretKey = secret;
    }

    /**
     * 使用 AES 加密和 CBC 模式加密给定的明文字符串。
     *
     * @param plaintext 明文字符串
     * @param key       密钥
     * @return 加密后的字符串，或者在出现异常时返回 null
     */
    public static String cbcEncrypt(String plaintext, String key) {
        String iv = "Jisniwqjwqjwqjww"; // 长度固定为 aes.BlockSize ，16位
        // 自动拓宽
        String s = "J69IVxcXqvqNhvk1J69IVxcXqvqNhvk1J69IVxcXqvqNhvk1J69IVxcXqvqNhvk1" + plaintext;
        try {
            return Encrypt(s, key, iv);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String Encrypt(String plainText, String key, String iv) throws Exception {
        byte[] data = aesCBCEncrypt(plainText.getBytes(), key.getBytes(), iv.getBytes());
        return Base64.getEncoder().encodeToString(data);
    }

    public static byte[] aesCBCEncrypt(byte[] plaintext, byte[] key, byte[] iv) throws Exception {
        // AES
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        // CBC 加密
        byte[] encrypted = cipher.doFinal(plaintext);
        return encrypted;
    }

    // PKCS7 填充
    public static byte[] paddingPKCS7(byte[] plaintext, int blockSize) {
        int paddingSize = blockSize - (plaintext.length % blockSize);
        byte[] paddingText = new byte[paddingSize];
        Arrays.fill(paddingText, (byte) paddingSize);
        byte[] paddedData = new byte[plaintext.length + paddingSize];
        System.arraycopy(plaintext, 0, paddedData, 0, plaintext.length);
        System.arraycopy(paddingText, 0, paddedData, plaintext.length, paddingSize);
        return paddedData;
    }

    public static String makeBase64(byte[] bytes) {
        Base64.Encoder encoder = Base64.getEncoder();
        String base64 = encoder.encodeToString(bytes);
        return base64;
    }

    public static String base64Decoder(String base64Str) {
        Base64.Decoder decoder = Base64.getDecoder();
        String text = new String(decoder.decode(base64Str));
        return text;
    }

    public static Integer calPageCount(int sum, int limitCount) {
        if (sum % limitCount != 0) {
            return sum / limitCount + 1;
        } else {
            return sum / limitCount;
        }
    }


    public static String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(KEY_STR.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return makeBase64(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public static String decrypt(String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(KEY_STR.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            encrypted = encrypted.replaceAll(" ", "+");
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));
            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }


    public static boolean isEmptyOrIsNullStr(String... strs) {
        if (strs.length == 0) {
            return false;
        }
        for (String str : strs) {
            if (str == null || "".equals(str)) {
                return false;
            }
        }
        return true;
    }

    public static String makeQRCode(String stuId, int width, int height) {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(stuId, BarcodeFormat.QR_CODE, width, height, hints);
            if (bitMatrix == null) {
                return "";
            }
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] ans = baos.toByteArray();
            String base64Str = LiUtils.makeBase64(ans);
            if (base64Str != null) {
                return base64Str;
            }
        } catch (WriterException | IOException | NullPointerException e) {
            throw new ServiceException("网络请求异常，请重试！", HttpStatus.f5001);
        }
        return "";
    }

    /**
     * AES加密ECB模式PKCS7Padding填充方式
     *
     * @param str 字符串
     * @param key 密钥
     * @return 加密字符串
     * @throws Exception 异常信息
     */
    public static String aes256ECBPkcs7PaddingEncrypt(String str, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(ECB_ALGORITHM);
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, secretKey));
        byte[] doFinal = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
        return new String(Base64.getEncoder().encode(doFinal));
    }


    public static String loginECBPkcs7PAddingDecrypt(String str) {
        if (StringUtils.isEmpty(str)) {
            throw new ServiceException("账号或密码错误！", 4005);
        }
        return LiUtils.aes256ECBPkcs7PaddingDecrypt(str, secretKey);
    }

    /**
     * AES解密ECB模式PKCS7Padding填充方式
     *
     * @param str 字符串
     * @param key 密钥
     * @return 解密字符串
     * @throws Exception 异常信息
     */
    public static String aes256ECBPkcs7PaddingDecrypt(String str, String key) {
        try {
            Cipher cipher = Cipher.getInstance(ECB_ALGORITHM);
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            // 用来填充的东西
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, secretKey));
            byte[] doFinal = cipher.doFinal(Base64.getDecoder().decode(str));
            return new String(doFinal);
        } catch (Exception e) {
            throw new ServiceException("账号或密码错误！", 4005);
        }


    }


}
