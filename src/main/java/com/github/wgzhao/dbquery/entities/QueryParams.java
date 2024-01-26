package com.github.wgzhao.dbquery.entities;


import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Setter
@Getter
@Data
@IdClass(QueryParams.Pk.class)
public class QueryParams
{
    @Id
    private String selectId;

    @Id
    private String paramName;

    @Id
    private String paramType;

    @Embeddable
    public class Pk {
        private String selectId;
        private String paramName;
        private String paramType;

        public String getSelectId() {
            return selectId;
        }

        public void setSelectId(String selectId) {
            this.selectId = selectId;
        }

        public String getParamName() {
            return paramName;
        }

        public void setParamName(String paramName) {
            this.paramName = paramName;
        }

        public String getParamType() {
            return paramType;
        }

        public void setParamType(String paramType) {
            this.paramType = paramType;
        }
    }
}
