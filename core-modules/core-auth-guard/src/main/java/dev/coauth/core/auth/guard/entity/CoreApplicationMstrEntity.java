package dev.coauth.core.auth.guard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "CORE_APPLICATION_MSTR")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CoreApplicationMstrEntity {
    @Id
    @Column(name = "APP_ID")
    private int appId;
    @Basic
    @Column(name = "APP_NAME")
    private String appName;
    @Basic
    @Column(name = "APP_DESC")
    private String appDesc;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoreApplicationMstrEntity that = (CoreApplicationMstrEntity) o;
        return appId == that.appId && Objects.equals(appName, that.appName) && Objects.equals(appDesc, that.appDesc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appId, appName, appDesc);
    }
}
