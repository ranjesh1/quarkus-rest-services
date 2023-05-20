package com.demo.app.ws.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "orders")
public class Order implements Serializable {

    private static final long serialVersionUID = -4651255750078090696L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 120)
    private String description;

    @Column(nullable = false, length = 120)
    private long priceInPence;

    @Column(nullable = false)
    private boolean completedStatus = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPriceInPence() {
        return priceInPence;
    }

    public void setPriceInPence(long priceInPence) {
        this.priceInPence = priceInPence;
    }

    public boolean isCompletedStatus() {
        return completedStatus;
    }

    public void setCompletedStatus(boolean completedStatus) {
        this.completedStatus = completedStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
