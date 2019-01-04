<?xml version="1.0" encoding="UTF-8"?>
<template>
    <comment ID="addJavaFileComment"></comment>
    <comment ID="addComment"><![CDATA[
<!--${mgb}-->
        ]]>
    </comment>
    <comment ID="addRootComment"></comment>
    <comment ID="addFieldComment"><![CDATA[
<#if introspectedColumn??>
/**
    <#if introspectedColumn.remarks?? && introspectedColumn.remarks != ''>
        <#list introspectedColumn.remarks?split("\n") as remark>
 * ${remark}
        </#list>
    </#if>
 * ${mgb}
 */
<#else>
/**
 * ${mgb}
 */
</#if>
        ]]>
    </comment>
    <comment ID="addModelClassComment"><![CDATA[
/**
<#if introspectedTable.remarks?? && introspectedTable.remarks != ''>
    <#list introspectedTable.remarks?split("\n") as remark>
 * ${remark}
    </#list>
</#if>
 * ${mgb}
 */
        ]]>
    </comment>
    <comment ID="addClassComment"><![CDATA[
/**
 * ${mgb}
 */
        ]]>
    </comment>

    <comment ID="addEnumComment"><![CDATA[
/**
 * ${mgb}
 */
        ]]>
    </comment>

    <comment ID="addInterfaceComment"><![CDATA[
/**
 * ${mgb}
 */
        ]]>
    </comment>

    <comment ID="addGetterComment"><![CDATA[
/**
 * ${mgb}
 */
        ]]>
    </comment>

    <comment ID="addSetterComment"><![CDATA[
/**
 * ${mgb}
 */
        ]]>
    </comment>

    <comment ID="addGeneralMethodComment"><![CDATA[
/**
 * ${mgb}
 */
        ]]>
    </comment>
</template>