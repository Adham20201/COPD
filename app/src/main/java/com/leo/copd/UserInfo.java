package com.leo.copd;

public class UserInfo {
    String age, mwt1, packHistory, fev1, fvc, smoking, gender;

    public UserInfo(String age, String mwt1, String packHistory, String fev1, String fvc, String smoking, String gender) {
        this.age = age;
        this.mwt1 = mwt1;
        this.packHistory = packHistory;
        this.fev1 = fev1;
        this.fvc = fvc;
        this.smoking = smoking;
        this.gender = gender;
    }

    public UserInfo() {
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMwt1() {
        return mwt1;
    }

    public void setMwt1(String mwt1) {
        this.mwt1 = mwt1;
    }

    public String getPackHistory() {
        return packHistory;
    }

    public void setPackHistory(String packHistory) {
        this.packHistory = packHistory;
    }

    public String getFev1() {
        return fev1;
    }

    public void setFev1(String fev1) {
        this.fev1 = fev1;
    }

    public String getFvc() {
        return fvc;
    }

    public void setFvc(String fvc) {
        this.fvc = fvc;
    }

    public String getSmoking() {
        return smoking;
    }

    public void setSmoking(String smoking) {
        this.smoking = smoking;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
