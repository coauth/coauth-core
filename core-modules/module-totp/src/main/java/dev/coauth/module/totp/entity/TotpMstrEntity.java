package dev.coauth.module.totp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "TOTP_MSTR")
@AllArgsConstructor
@NoArgsConstructor
public class TotpMstrEntity {
    @Id
    @Column(name = "ROW_ID")
    private String rowId;

    @Column(name = "USER_ID")
    private String userId;
    @Column(name = "APP_ID")
    private int appId;
    @Column(name = "SECRET")
    private String secret;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TotpMstrEntity that = (TotpMstrEntity) o;
        return appId == that.appId && Objects.equals(userId, that.userId) && Objects.equals(rowId, that.rowId) && Objects.equals(secret, that.secret);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, appId, rowId, secret);
    }
}
