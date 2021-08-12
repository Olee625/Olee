package com.olee.project.mapper;

import com.olee.project.enums.ResponseCode;
import com.olee.project.exception.BizException;
import com.olee.project.model.User;
import org.apache.ibatis.jdbc.SQL;

public class SqlProvider {
    public String update(User user) {
        if (user.getNickname() == null && user.getAddress() == null) {
            throw new BizException(ResponseCode.NICKNAME_AND_ADDRESS_ARE_EMPTY);
        }
        return new SQL() {{
            UPDATE("olee_user");
            SET("updateAt=#{updateAt}");
            //条件写法.
            if (user.getNickname() != null) {
                SET("nickname=#{nickname}");
            }
            if (user.getAddress() != null) {
                SET("address=#{address}");
            }
            WHERE("userId=#{userId}");
        }}.toString();
    }
}
