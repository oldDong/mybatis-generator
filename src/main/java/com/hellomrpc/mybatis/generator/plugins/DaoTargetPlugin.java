package com.hellomrpc.mybatis.generator.plugins;

import com.itfsw.mybatis.generator.plugins.utils.BasePlugin;
import org.mybatis.generator.api.IntrospectedTable;

import java.util.List;

public class DaoTargetPlugin extends BasePlugin {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }


    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        String mapperType = introspectedTable.getMyBatis3JavaMapperType();
        String[] strArray = mapperType.split("\\.");
        int length = strArray.length;
        int length2 = strArray[length - 1].length();
        strArray[length - 1] = strArray[length - 1].substring(0, length2 - 6) + "Dao";
        introspectedTable.setMyBatis3JavaMapperType(Utils.join(".", strArray));
    }
}
