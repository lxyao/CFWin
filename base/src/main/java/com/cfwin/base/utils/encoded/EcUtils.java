package com.cfwin.base.utils.encoded;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bitcoinj.core.Base58;

import java.math.BigInteger;
import java.security.interfaces.ECPublicKey;

/**
 * Created by Administrator on 2018/9/28.
 */

public class EcUtils {
    /**
     * 通过公钥生成钱包地址
     */
    public static String generateAddressByPublickey(ECPublicKey publickey)
    {
        Base58 base58  =new Base58();
        try {
//			System.out.println(publickey);
            String hexStr = formatEcPublicKey(publickey);
//			System.out.println("hex:"+hexStr);
            String address = base58.encode(Hex.decodeHex(hexStr.toCharArray()));
            return "CFWCHAIN"+address;
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 格式化公钥
     */
    public static String formatEcPublicKey(ECPublicKey publicKey)
    {
        final int SZIE = 64;
        String res = "04"
                + getStandardSizeInteger(publicKey.getW().getAffineX(),SZIE)
                + getStandardSizeInteger(publicKey.getW().getAffineY(),SZIE);
        return res;
    }

    private static String getStandardSizeInteger(BigInteger value, int size)
    {
        String hex= value.toString(16);
        if(hex.length()<size)
        {
            int len = size -hex.length();
            String temp = "";
            for(int i=0;i<len;i++)
            {
                temp+="0";
            }
            hex = temp+hex;
        }
        return hex;
    }
//    /**
//     * 创建钱包
//     */
//    public static WalletBean createWallet() {
//        WalletBean bean = new WalletBean();
//        ECPrivateKey privateKey= EcKeyUtils.generateEcPrivateKey();
//        ECPublicKey publicKey = EcKeyUtils.getPublicKeyFromPrivateKey(privateKey);
//        bean.setPrivateKey(privateKey.getS().toString(16));
//        bean.setPublicKey(formatEcPublicKey(publicKey));
//        bean.setAddress(generateAddressByPublickey(publicKey));
//        return bean;
//    }
//
//    /**
//     * 通过16进制的 私钥D，生成钱包
//     * @param privateKey
//     * @return
//     */
//    public static WalletBean createByPrivateKey(String privateKey) {
//        if (privateKey.length() == 64) {
//            WalletBean bean = new WalletBean();
//            try{
//                ECPrivateKey prk = EcKeyUtils.getEcPrivateKeyFromHex(privateKey);
//                ECPublicKey puk = EcKeyUtils.getPublicKeyFromPrivateKey(prk);
//                bean.setPrivateKey(privateKey);
//                bean.setPublicKey(formatEcPublicKey(puk));
//                bean.setAddress(generateAddressByPublickey(puk));
//                return bean;
//            }
//            catch(Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }

}

