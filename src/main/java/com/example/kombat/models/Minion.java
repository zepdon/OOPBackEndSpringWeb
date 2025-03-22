package com.example.kombat.models;

public class Minion {
    private int id;
    private String src;
    private int def;
    private int hp;

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    @Override
    public String toString() {
        return "Minion{" +
                "id=" + id +
                ", src='" + src + '\'' +
                ", def=" + def +
                ", hp=" + hp +
                '}';
    }
}