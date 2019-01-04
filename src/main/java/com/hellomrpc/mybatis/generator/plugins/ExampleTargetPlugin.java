package com.hellomrpc.mybatis.generator.plugins;

import com.itfsw.mybatis.generator.plugins.utils.BasePlugin;
import org.mybatis.generator.api.IntrospectedTable;

import java.util.List;

public class ExampleTargetPlugin extends BasePlugin {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }


    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        String exampleType = introspectedTable.getExampleType();
        String[] strArray = exampleType.split("\\.");
        int length = strArray.length;
        strArray[length - 2] = "condition";
        int length2 = strArray[length - 1].length();
        strArray[length - 1] = strArray[length - 1].substring(0, length2 - 7) + "Condition";
        introspectedTable.setExampleType(Utils.join(".", strArray));

        introspectedTable.setCountByExampleStatementId("countByCondition");
        introspectedTable.setDeleteByExampleStatementId("deleteByCondition");
        introspectedTable.setSelectByExampleStatementId("selectByCondition");
        introspectedTable.setSelectByExampleWithBLOBsStatementId("selectByConditionWithBLOBs");
        introspectedTable.setUpdateByExampleStatementId("updateByCondition");
        introspectedTable.setUpdateByExampleSelectiveStatementId("updateByConditionSelective");
        introspectedTable.setUpdateByExampleWithBLOBsStatementId("updateByConditionWithBLOBs");
        introspectedTable.setExampleWhereClauseId("Condition_Where_Clause");
        introspectedTable.setMyBatis3UpdateByExampleWhereClauseId("Update_By_Condition_Where_Clause");

    }


}
