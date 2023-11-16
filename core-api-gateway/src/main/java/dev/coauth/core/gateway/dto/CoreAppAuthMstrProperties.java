package dev.coauth.core.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CoreAppAuthMstrProperties {
    private int appId;
    private String appName;
    private String appDesc;

}
