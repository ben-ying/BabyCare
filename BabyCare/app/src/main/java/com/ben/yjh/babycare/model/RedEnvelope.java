package com.ben.yjh.babycare.model;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.io.Serializable;


public class RedEnvelope extends SugarRecord implements Serializable {
    @Unique
    @SerializedName("red_envelope_id") int redEnvelopeId;
    @SerializedName("user_id") int userId;
    @SerializedName("money") String money;
    @SerializedName("money_type") int moneyType;
    @SerializedName("money_from") String moneyFrom;
    @SerializedName("remark") String remark;
    @SerializedName("created") String created;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRedEnvelopeId() {
        return redEnvelopeId;
    }

    public void setRedEnvelopeId(int redEnvelopeId) {
        this.redEnvelopeId = redEnvelopeId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(int moneyType) {
        this.moneyType = moneyType;
    }

    public String getMoneyFrom() {
        return moneyFrom;
    }

    public void setMoneyFrom(String moneyFrom) {
        this.moneyFrom = moneyFrom;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreated() {
        return created;
    }

    public String getCreatedDate() {
        return created == null ? "" : created.split(" ")[0];
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
