package com.telusko.bankingapplication.bankingObjects;

import jakarta.persistence.*;


@Entity
@Table(name = "Users")
public class User {
    private long id;
    private String name;
    private String status;
    private String cnic;

    public User() {
    }

    public User(long id, String name, String status, String cnic) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.cnic = cnic;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    @Column(name = "user_name", nullable = false)
    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "status", nullable = false)
    public String getStatus(){
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "cnic", nullable = false)
    public String getCnic(){
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }
}
