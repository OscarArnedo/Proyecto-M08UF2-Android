package com.example.proyectom08uf2android;

public class User implements Comparable<User>{

    private String name, passwd, email;
    private int money;

    public User(String name, String passwd, String email) {
        this.name = name;
        this.passwd = passwd;
        this.email = email;
        this.money = 5000;
    }

    public User(String passwd, String email) {
        this.passwd = passwd;
        this.email = email;
        //this.money = money;
    }

    public User(String name, int money){
        this.name = name;
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String toStringRanking(){
        return name+" - $"+String.valueOf(money);
    }

    @Override
    public int compareTo(User u) {
        if (u.getMoney() < money ) return -1;
        else if (u.getMoney() == money) return 0;
        else return 1;
    }
}
