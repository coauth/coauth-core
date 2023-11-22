package dev.coauth.module.reconfirm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MODULE_RECONFIRM_MSTR")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ModuleReconfirmMstrEntity {
    @Id
    @Column(name = "ROW_ID")
    private String rowId;

    @Column(name = "USER_ID")
    private String userId;
    @Column(name = "APP_ID")
    private int appId;
    @Column(name = "STATUS")
    private String status;

}
