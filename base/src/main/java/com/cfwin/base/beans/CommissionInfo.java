package com.cfwin.base.beans;

/**
 * Created by Administrator on 2018/10/10.
 */

public class CommissionInfo {
    private String hashTransaction;
    private String payer;
    private String gas;
    private String commissionTimestamp;

    public CommissionInfo() {
    }

    public CommissionInfo(String hashV, String payer, String gas, String timestamp) {
        Init(hashV, payer, gas, timestamp);
    }

    public void Init(String hashTransaction, String payer, String gas, String commissionTimestamp) {
        this.hashTransaction = hashTransaction;
        this.payer = payer;
        this.gas = fixGas(gas);
        this.commissionTimestamp = commissionTimestamp;
    }


    private String fixGas(String gas)
    {
//        int pow = (int)Math.pow(10, 8);
        String value = gas;

        if (value.equals("") || value == null) {
            return null;
        }

//        Double aa = Double.parseDouble(value);
//        if(aa <= 0.0){
//            return null;
//        }
//        Double sbb = aa * pow;
//        DecimalFormat decimalFormat = new DecimalFormat("#,##0");//格式化设置
//        String gasStr = decimalFormat.format(sbb);
        return value;
    }

    public String getHashTransaction() {
        return hashTransaction;
    }

    public void setHashTransaction(String hashTransaction) {
        this.hashTransaction = hashTransaction;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public String getCommissionTimestamp() {
        return commissionTimestamp;
    }

    public void setCommissionTimestamp(String commissionTimestamp) {
        this.commissionTimestamp = commissionTimestamp;
    }

    @Override
    public String toString() {
        return "CommissionInfo{" +
                "hashTransaction='" + hashTransaction + '\'' +
                ", payer='" + payer + '\'' +
                ", gas=" + gas +
                ", commissionTimestamp='" + commissionTimestamp + '\'' +
                '}';
    }
}
