package com.gdutelc;
/*
用于获得加密后密码
 */

import com.gdutelc.utils.LiUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EncryptTest {

    public static String password;

    @Test
    public void testEncrypt() throws Exception {
        //加密密码并输出
        password = LiUtils.aes256ECBPkcs7PaddingEncrypt("Il02mayi", "rYnZ5jv7rLfsnd96");

        System.out.println(password);
    }

    @Test
    public void testDecrypt() throws Exception {
        String s = LiUtils.aes256ECBPkcs7PaddingDecrypt(password, "rYnZ5jv7rLfsnd96");
        System.out.println(s);
    }

    @Test
    public void transferBase64() {
        String s = LiUtils.base64Decoder("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");
        byte[] bytes = s.getBytes();
        String floderPath = "E:/桌面/重要文件";
        String fileName = "codeImg.jpg";
        saveImage(bytes, floderPath, fileName);
    }

    public static void saveImage(byte[] binaryData, String folderPath, String fileName) {
        // 创建文件夹（如果不存在）
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // 创建文件对象
        File imageFile = new File(folder, fileName);

        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            // 将二进制数据写入文件
            fos.write(binaryData);
            System.out.println("图片已保存到: " + imageFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEncrypt2() {
        String string = LiUtils.encrypt("2025-02-24").toString();
        System.out.println(string);
    }
}
