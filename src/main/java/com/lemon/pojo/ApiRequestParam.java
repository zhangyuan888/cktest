package com.lemon.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * InnoDB free: 3072 kB
 * </p>
 *
 * @author kk
 * @since 2020-02-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ApiRequestParam对象", description="InnoDB free: 3072 kB")
public class ApiRequestParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "关联的接口编号")
    private Integer apiId;

    @ApiModelProperty(value = "参数名称")
    private String name;

    @ApiModelProperty(value = "字段类型（string、int...）")
    private String paramType;

    @ApiModelProperty(value = "参数类型（1：query参数；2：body参数；3：header）")
    private Integer type;

    @ApiModelProperty(value = "参数值示例")
    private String exampleData;

    @ApiModelProperty(value = "字段描述")
    private String description;
    
    @TableField(exist=false)   //这个字段是接口运行时加的，这个列数据库里没有，用来封装数据
    private String value;
    
    @TableField(exist=false) //不是数据库真实存在的，是用来封装数据的
    private Integer valueId;


}
