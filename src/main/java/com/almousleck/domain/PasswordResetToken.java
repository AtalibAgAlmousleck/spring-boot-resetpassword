package com.almousleck.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@Entity
public class PasswordResetToken {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;
    private String token;
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
    private Date expiryDate;

    public void setExpiryDate(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minutes);
        this.expiryDate = calendar.getTime();
    }

    public boolean isExpired() {
        return new Date().after(this.expiryDate);
    }
}
