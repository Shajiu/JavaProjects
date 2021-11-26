package com.springboot.entity;

/**
 * @author shajiu
 * @date 2021/11/9 14:05
 */
import lombok.*;

@Data
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DevTabH {

    private String dbsName;
    private String tabName;
    private String tabDes;
    private String sysCode;
    private String tabRec;
    private String updCode;
    private String updName;
    private String updTime;
    private String uuId;

}
