package com.demo.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.demo.common.utilities.Constant;
import com.demo.common.utilities.DateUtil;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author DigiEx Group
 */
@Data
@ToString
@MappedSuperclass
@JsonInclude(Include.NON_NULL)
@EntityListeners(AuditingEntityListener.class)
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonType.class)
})
public abstract class BaseEntity implements Serializable {

    @CreatedDate
    @Column(name = "created_date")
    protected long createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    protected Long updatedDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = DateUtil.convertToUTC(new Date()).getTime();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = DateUtil.convertToUTC(new Date()).getTime();
    }

}