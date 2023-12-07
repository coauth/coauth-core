package dev.coauth.core.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CoreAppDetails {
    private int appId;
    private String appName;
    private String appDesc;

}
