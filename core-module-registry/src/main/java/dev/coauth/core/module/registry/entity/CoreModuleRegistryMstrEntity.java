package dev.coauth.core.module.registry.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "CORE_MODULE_REGISTRY_MSTR")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CoreModuleRegistryMstrEntity {
    @Id
    @Column(name = "REG_ID")
    private String regId;
    @Column(name = "USER_ID")
    private String userId;
    @Column(name = "APP_ID")
    private int appId;
    @Column(name = "SERVICE_NAME")
    private String serviceName;
    @Column(name = "STATUS")
    private String status;



}
