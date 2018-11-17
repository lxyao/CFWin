package com.cfwin.base.utils.encoded;

import android.util.Log;

import com.subgraph.orchid.encoders.Hex;

import org.web3j.crypto.MnemonicUtils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.ECPublicKeySpec;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;
import io.github.novacrypto.bip39.MnemonicGenerator;
import io.github.novacrypto.bip39.Words;
import io.github.novacrypto.bip39.wordlists.English;

import static org.web3j.crypto.Hash.sha256;

/**
 * Created by Administrator on 2018/9/28.
 */
public class EcKeyUtils {
    public static final String ALGORITHM = "EC";
    public static final String SPACE_NAME= "secp256r1";// eq prime256v1

    /**
     * 生成助记词
     */
    public static String createMnemonic(){
        StringBuilder sb = new StringBuilder();
        byte[] entropy = new byte[Words.TWELVE.byteLength()];
        new SecureRandom().nextBytes(entropy);
        new MnemonicGenerator(English.INSTANCE).createMnemonic(entropy, sb::append);

        String mnemonics = sb.toString();
        return mnemonics;
    }


    public static boolean  saveFile(byte[] bytes, String fileName, File directory) {
        // 创建String对象保存文件名路径
        try {
            // 创建指定路径的文件
            File file = new File(directory, fileName);
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            // 如果文件不存在
            if (!file.exists()) {
                file.createNewFile();
            }

            // 获取文件的输出流对象
            FileOutputStream outStream = new FileOutputStream(file);
            // 获取字符串对象的byte数组并写入文件流
            outStream.write(bytes);
            // 最后关闭文件输出流
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return  true;
    }

    /**
     * 保存助记词
     * password 密码
     * path 保存文件路径
     */
    public static boolean saveMnemonic(String password, String mnemonic, String dir, String filename){
        //
        if (password == "") {
            return false;
        }

        byte[] passwordbyte = sha256(password.getBytes());
        byte[] rawKey = AESUtil.getRawKey(passwordbyte);

        // AES crypto
        byte[] encryptedByteArr = AESUtil.encrypt(passwordbyte,mnemonic.trim() );
        String encryptedPwd = new String(encryptedByteArr);
        Log.e("encryptedPwd",encryptedPwd);
        // save

        return saveFile(encryptedByteArr, filename, new File(dir));
    }

    /**
     * 显示助记词
     */
    public static String getMnemonic(String password, String dir, String filename) throws IOException {
        // 打开文件，读取密文

        // 创建文件
        File file = new File(dir,filename);
        if (!file.exists())
        {
            return null;
        }
        long length = file.length();
        if (length > 512 || length <= 16)
        {
            return null;
        }
        FileInputStream in1 = null;
        DataInputStream data_in = null;
        byte[] b = new byte[(int) length];
        try {
            in1 = new FileInputStream(file);
            data_in = new DataInputStream(in1);


            data_in.read(b, 0, (int) length);

            data_in.close();
            in1.close();
        }
        catch (IOException e){
            data_in.close();
            in1.close();
        }

        // password 转 sha256
        byte[] passwordbyte = sha256(password.getBytes());

        // AES 解密
        byte[] decryptedByteArr = AESUtil.decrypt(b, passwordbyte);
        if (decryptedByteArr == null){
            return null;
        }

        // return
        return new String(decryptedByteArr);

    }
    /**
     * 通过助记词的到一个16进制字符串(通过助记词的到私钥)
     */
    public static String mnemonic2PrivateKey(String mnemonic)
    {
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);
        byte[] privkeytemp = sha256(seed);

        //BASE64Encoder enc = new BASE64Encoder();
        //String hexStr = enc.encode(privkeytemp);

        String hexStr = bytesToHex(privkeytemp);
        return hexStr;
        // 返回16进制 hex 字符串
    }

    /**
     * 保存私钥
     * @param privkey
     * @param password
     * @param dir
     * @param filename
     * @return
     */
    public static boolean savePrivateKey(String privkey, String password, String dir, String filename)
    {
        if (password == null || password.trim() == "") {
            // 组合json字符串，存储
            return saveFile(privkey.getBytes(), filename, new File(dir));
        }

        byte[] passwordbyte = sha256(password.getBytes());

        // AES crypto
        byte[] encryptedByteArr = AESUtil.encrypt(passwordbyte,privkey.trim() );
        String encryptedPwd = new String(encryptedByteArr);
        Log.e("encryptedPwd",encryptedPwd);
        // save

        return saveFile(encryptedByteArr, filename, new File(dir));
    }

    /**
     * 从文件中获取私钥
     * @param password
     * @param dir
     * @param filename
     * @return
     */
    public static String getPrivateKey(String password, String dir, String filename) throws IOException {
        // 返回16进制 hex 字符串
        // 打开文件，读取密文

        // 创建文件
        File file = new File(dir,filename);
        if (!file.exists()) {
            return "error";
        }
        long length = file.length();
        if (length > 256 || length <= 16)
        {
            return "error";
        }
        FileInputStream in1 = null;
        DataInputStream data_in = null;
        byte[] b = new byte[(int) length];
        try {
            in1 = new FileInputStream(file);
            data_in = new DataInputStream(in1);


            data_in.read(b, 0, (int) length);

            data_in.close();
            in1.close();
        }
        catch (IOException e){
            data_in.close();
            in1.close();
        }

        if (password == null || password.trim() == "")
        {
            return new String(b);
        }

        // password 转 sha256
        byte[] passwordbyte = sha256(password.getBytes());

        // AES 解密
        byte[] decryptedByteArr = AESUtil.decrypt(b, passwordbyte);

        if (decryptedByteArr == null){
            return "ERROR";
        }

        String str= new String(decryptedByteArr);
        return str;
        // return
        //return bytesToHex(decryptedByteArr);

    }

    /**
     * 从私钥得到地址
     * @param privkey
     * @return
     */
    public static String privateKey2Address(String privkey)
    {
        // privkey 2 public
        ECPrivateKey prvatekey = EcKeyUtils.getEcPrivateKeyFromHex(privkey);
        ECPublicKey publickey = EcKeyUtils.getPublicKeyFromPrivateKey(prvatekey);
        return EcUtils.generateAddressByPublickey(publickey);
    }

    /**
     * 签名
     * @param privkey
     * @param data
     * @return
     * @throws Exception
     */
    public static byte [] sign(String privkey, byte[] data) throws Exception {
        ECPrivateKey prvatekey = EcKeyUtils.getEcPrivateKeyFromHex(privkey);
        byte[] signdata = EcSignUtil.signData(data, prvatekey);
        return signdata;
    }

    /**
     * 签名
     * @param privkey
     * @param data
     * @return BASE64编码的字符串
     * @throws Exception
     */
    public static String signReturnBase64(String privkey, byte[] data) throws Exception {
        ECPrivateKey prvatekey = EcKeyUtils.getEcPrivateKeyFromHex(privkey);
        byte[] signdata = EcSignUtil.signData(data, prvatekey);
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(signdata);
    }

    /**
     * 签名
     * @param privkey
     * @param data
     * @return Hex编码的字符串
     * @throws Exception
     */
    public static String signReturnHexString(String privkey, byte[] data) throws Exception {
        ECPrivateKey prvatekey = EcKeyUtils.getEcPrivateKeyFromHex(privkey);
        byte[] signdata = EcSignUtil.signData(data, prvatekey);
        return new String(Hex.encode(signdata));
    }

    /**
     * 验签
     * @param data
     * @param address
     * @param sign
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String address , String sign) throws Exception {
        BASE64Decoder base64Decoder = new BASE64Decoder();
        byte[] decodeBuffer = base64Decoder.decodeBuffer(sign);
        return EcSignUtil.verifySign(data, address, decodeBuffer);
    }


    /**
     * 生成私钥
     */
    public static ECPrivateKey generateEcPrivateKey()
    {
        try {
            Provider provider = new org.bouncycastle.jce.provider.BouncyCastleProvider();
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM,provider);
            kpg.initialize(new ECGenParameterSpec(SPACE_NAME), new SecureRandom());
            KeyPair keyPair = kpg.generateKeyPair();
            return (ECPrivateKey)keyPair.getPrivate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 从16进制的字符中获取私钥
     * @param hexStr
     * @return
     */
    public static ECPrivateKey getEcPrivateKeyFromHex(String hexStr)
    {
        try {
            //BASE64Decoder dec = new BASE64Decoder();
            //byte[] hexdata = dec.decodeBuffer(hexStr);
            //String hexString = bytesToHex(hexdata);

            Provider provider = new org.bouncycastle.jce.provider.BouncyCastleProvider();

            org.bouncycastle.jce.spec.ECNamedCurveParameterSpec spec = org.bouncycastle.jce.ECNamedCurveTable.getParameterSpec(SPACE_NAME);
            KeyFactory kf = KeyFactory.getInstance(ALGORITHM, provider);
            org.bouncycastle.jce.spec.ECNamedCurveSpec params = new org.bouncycastle.jce.spec.ECNamedCurveSpec(SPACE_NAME, spec.getCurve(), spec.getG(), spec.getN());
            BigInteger s = new BigInteger(hexStr, 16);
            ECPrivateKeySpec keySpec =new ECPrivateKeySpec(s, params);
            ECPrivateKey pk = (ECPrivateKey)kf.generatePrivate(keySpec);
            return pk;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从私钥中获取公钥
     */
    public static ECPublicKey getPublicKeyFromPrivateKey(ECPrivateKey privateKey) {
        // prime256v1
        try {
            Provider provider = new org.bouncycastle.jce.provider.BouncyCastleProvider();

            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, provider);
            org.bouncycastle.jce.spec.ECParameterSpec ecSpec = org.bouncycastle.jce.ECNamedCurveTable.getParameterSpec(SPACE_NAME);
            org.bouncycastle.math.ec.ECPoint Q = ecSpec.getG().multiply(((org.bouncycastle.jce.interfaces.ECPrivateKey) privateKey).getD());
            org.bouncycastle.jce.spec.ECPublicKeySpec pubSpec = new org.bouncycastle.jce.spec.ECPublicKeySpec(Q, ecSpec);
            ECPublicKey publicKey= (ECPublicKey) keyFactory.generatePublic(pubSpec);
            return publicKey;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从16进制的字符中获取公钥
     *
     */
    public static ECPublicKey getPublicKeyFromHex(String hexstr) {
        ECPublicKey pk;
        try {
            Provider provider = new org.bouncycastle.jce.provider.BouncyCastleProvider();

//			byte[] pubKey = Hex.decodeHex(hexstr.toCharArray());
            byte[] pubKey = Hex.decode(new String(hexstr.toCharArray()));
            org.bouncycastle.jce.spec.ECNamedCurveParameterSpec spec = org.bouncycastle.jce.ECNamedCurveTable.getParameterSpec(SPACE_NAME);
            KeyFactory kf = KeyFactory.getInstance(ALGORITHM, provider);
            org.bouncycastle.jce.spec.ECNamedCurveSpec params = new org.bouncycastle.jce.spec.ECNamedCurveSpec(SPACE_NAME, spec.getCurve(), spec.getG(), spec.getN());
            ECPoint point =  org.bouncycastle.jce.ECPointUtil.decodePoint(params.getCurve(), pubKey);
            ECPublicKeySpec pubKeySpec = new ECPublicKeySpec(point, params);
            pk = (ECPublicKey) kf.generatePublic(pubKeySpec);
            return pk;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字节数组转16进制
     * @param bytes 需要转换的byte数组
     * @return  转换后的Hex字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if(hex.length() < 2){
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}


