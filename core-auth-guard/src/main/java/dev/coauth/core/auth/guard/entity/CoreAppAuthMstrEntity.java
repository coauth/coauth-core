package dev.coauth.core.auth.guard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "CORE_APP_AUTH_MSTR")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CoreAppAuthMstrEntity {
    @Id
    @Column(name = "AUTH_ID")
    private String authId;
    @Basic
    @Column(name = "APP_ID")
    private int appId;
    @Basic
    @Column(name = "APP_KEY")
    private String appKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoreAppAuthMstrEntity that = (CoreAppAuthMstrEntity) o;
        return appId == that.appId && Objects.equals(authId, that.authId) && Objects.equals(appKey, that.appKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authId, appId, appKey);
    }
}
