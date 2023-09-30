package com.gdutelc.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Hashtable;
import java.util.Properties;

/**
 * @author shan-liangguang
 * @version 1.0
 * @since 2023/9/29 01:05
 * LiUtil
 */
public class LiUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(LiUtils.class);

    private static final String KEY_STR = "f345159c1f9a08a9";

    private static final String initVector = "as5d41gg09789ghy";

    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    public static JSONObject entityToJsonObject(HttpEntity entity) {
        try {
            return JSON.parseObject(EntityUtils.toString(entity, "utf-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用 AES 加密和 CBC 模式加密给定的明文字符串。
     *
     * @param plaintext 明文字符串
     * @param key       密钥
     * @return 加密后的字符串，或者在出现异常时返回 null
     */
    public static String cbcEncrypt(String plaintext, String key) {
        try {
            // 长度固定为 AES 的块大小，16位
            String iv = "Jisniwqjwqjwqjww";
            // 自动拓宽
            String s = "J69IVxcXqvqNhvk1J69IVxcXqvqNhvk1J69IVxcXqvqNhvk1J69IVxcXqvqNhvk1" + plaintext;
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getUrlFromProFile(String key) throws IOException {
        Properties properties = new Properties();
        Resource resource = new ClassPathResource("urls/usingUrl.properties");
        properties.load(resource.getInputStream());
        String url = (String) properties.get(key);
        return url;
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
        } catch (WriterException | IOException
                 | NullPointerException e) {
            e.printStackTrace();
        }
        return "";
    }

}
