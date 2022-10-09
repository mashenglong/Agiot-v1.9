package cn.edu.nwpu.fabric.Bean;

/**
 * @Author: ch
 * @Description:
 * @Date: Created in 15:04 2021/12/23
 * @Modified By:
 */
public class AgriculturalChemicals {

    private String landNumber;
    private String mobile;
    private String operator;
    private String pesticideName;
    private String pesticideUsed;
    private String operateDate;

    public String getLandNumber() {
        return landNumber;
    }

    public void setLandNumber(String landNumber) {
        this.landNumber = landNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getPesticideName() {
        return pesticideName;
    }

    public void setPesticideName(String pesticideName) {
        this.pesticideName = pesticideName;
    }

    public String getPesticideUsed() {
        return pesticideUsed;
    }

    public void setPesticideUsed(String pesticideUsed) {
        this.pesticideUsed = pesticideUsed;
    }

    public String getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(String operateDate) {
        this.operateDate = operateDate;
    }

    @Override
    public String toString() {
        return "AgriculturalChemicals{" +
                "landNumber='" + landNumber + '\'' +
                ", mobile='" + mobile + '\'' +
                ", operator='" + operator + '\'' +
                ", pesticideName='" + pesticideName + '\'' +
                ", pesticideUsed='" + pesticideUsed + '\'' +
                ", operateDate='" + operateDate + '\'' +
                '}';
    }
}
