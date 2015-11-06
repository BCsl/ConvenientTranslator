package com.lidroid.xutils.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by HelloCsl(cslgogogo@gmail.com) on 2015/10/28 0028.
 */
public class EncryptUtils {
    public final static String AES_PASS = "EncryptUtils";
    public static final String VIPARA = "0102030405060708";

    /**
     * AES加密
     *
     * @param content 需要加密的内容
     * @param seed    加密密码
     * @return
     */

    public static String encryptAESAndroid(String content, String seed) {
        try {
            IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
            SecretKeySpec key = new SecretKeySpec(transformStrToXXBitByteArr(seed, 16), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
            byte[] encryptedData = cipher.doFinal(content.getBytes("utf-8"));
            return parseByte2HexStr(encryptedData); // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES解密
     * param content  待解密内容
     *
     * @param seed 解密密钥
     * @return
     *
     * @throws BadPaddingException
     */
    public static String decryptAESAndroid(String contentStr, String seed) throws BadPaddingException {
        try {
            byte[] content = parseHexStr2Byte(contentStr);
            IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
            SecretKeySpec key = new SecretKeySpec(transformStrToXXBitByteArr(seed, 16), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
            byte[] decryptedData = cipher.doFinal(content);
            return new String(decryptedData); // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将任意字符串转换为capacity个元素的byte数组
     * 长度超过32位的将被截断，不够32位的将被循环填充进数组
     *
     * @param key      源字符串
     * @param capacity 数组的容量
     * @return Byte数组
     * 入参的串只能是ASCII码值，不能含有中文
     */

    public static byte[] transformStrToXXBitByteArr(String key, int capacity) {
        if (key == null || key.equals("")) {
            return new byte[capacity];
        } else {
            char[] tempStr = key.toCharArray();
            byte[] resultByteArr = new byte[capacity];
            int count = capacity / key.length();
            int mod = capacity % key.length();
            //判断是否需要循环填充数组
            //需要循环
            if (count > 0) {
                for (int i = 0; i < capacity; i++) {
                    byte tempByte;
                    //判断，是否已经遍历一次
                    int flag = i / key.length();
                    //如果已经遍历了一次，取余数作为下标
                    if (flag > 0) {
                        tempByte = (byte) tempStr[i % key.length()];
                        resultByteArr[i] = tempByte;
                    } else {
                        //如果没有遍历结束，取i作为下标
                        tempByte = (byte) tempStr[i];
                        resultByteArr[i] = (byte) tempStr[i];
                    }
                }
                //不需要循环
            } else if (mod > 0) {
                for (int i = 0; i < capacity; i++) {
                    Byte tempChar = (byte) tempStr[i];
                    resultByteArr[i] = tempChar;
                }
            }
            return resultByteArr;
        }
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    private static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
}
