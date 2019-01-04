package com.hellomrpc.mybatis.generator.plugins;

import com.itfsw.mybatis.generator.plugins.utils.BasePlugin;
import com.itfsw.mybatis.generator.plugins.utils.XmlElementGeneratorTools;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.xml.*;

import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoCreateTimePlugin extends BasePlugin {

    public static final String CREATE_TIME_COLUMN_NAME = "createTimeColumnName";  // 配置createTimeColumn名
    public static final String MODIFIED_TIME_COLUMN_NAME = "modifiedTimeColumnName";  // 配置modifiedTimeColumn名
    private static String createTimeColumnName;   // 创建时间
    private static String modifiedTimeColumnName;   // 修改时间

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate(List<String> warnings) {
        // 获取配置的创建时间字段名称
        Properties properties = getProperties();
        this.createTimeColumnName = properties.getProperty(CREATE_TIME_COLUMN_NAME);
        this.modifiedTimeColumnName = properties.getProperty(MODIFIED_TIME_COLUMN_NAME);
        if (this.createTimeColumnName == null){
            warnings.add("请配置com.hellomrpc.mybatis.generator.plugins.AutoCreateTimePlugin插件的创建时间(createTimeColumn)！");
            return false;
        }
        if (this.modifiedTimeColumnName == null){
            warnings.add("请配置com.hellomrpc.mybatis.generator.plugins.AutoCreateTimePlugin插件的修改时间(modifiedTimeColumnName)！");
            return false;
        }
        return super.validate(warnings);
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        List<Element> rootElements = document.getRootElement().getElements();
        for (Element rootElement : rootElements) {
            if (rootElement instanceof XmlElement) {
                XmlElement xmlElement = (XmlElement) rootElement;
                List<Attribute> attributes = xmlElement.getAttributes();
                // 查找ID
                String id = "";
                for (Attribute attribute : attributes) {
                    if (attribute.getName().equals("id")) {
                        id = attribute.getValue();
                    }
                }

                // ====================================== 1. insert ======================================
                if ("insert".equals(id)) {
                    replaceInsert(xmlElement, introspectedTable);
                }

                // ====================================== 1. insertSelective ======================================
                if ("insertSelective".equals(id)) {

                    List<XmlElement> eles1 = XmlElementGeneratorTools.findXmlElements(xmlElement, "trim");
                    for(XmlElement ele1 : eles1){
                        XmlElement ele2 = XmlElementGeneratorTools.findXmlElements(ele1, "choose").get(0);
                        XmlElement ele3 = XmlElementGeneratorTools.findXmlElements(ele2, "when").get(0);
                        XmlElement ele4 = XmlElementGeneratorTools.findXmlElements(ele2, "otherwise").get(0);
                        if (null != ele3)
                            replaceInsertSelective(ele3, introspectedTable);
                        if (null != ele4)
                            replaceInsertSelective(ele4, introspectedTable);
                    }
                }
            }
        }
        return true;
    }


    private void replaceInsert(XmlElement element, IntrospectedTable introspectedTable) {
        List<Element> oldElements = element.getElements();
        Element[] newElementsArr = new Element[oldElements.size()];
        for(int i = 0; i < oldElements.size(); i++){
            newElementsArr[i] = oldElements.get(i);
        }
        String createTimeJavaProperty = null;
        String modifiedTimeJavaProperty = null;
        for (IntrospectedColumn column : introspectedTable.getAllColumns()) {
            if (column.getActualColumnName().equals(createTimeColumnName)) {
                createTimeJavaProperty = column.getJavaProperty();
            }
            if (column.getActualColumnName().equals(modifiedTimeColumnName)) {
                modifiedTimeJavaProperty = column.getJavaProperty();
            }
        }
        for(int i = 0; i < oldElements.size(); i++) {
            Element ele = oldElements.get(i);
            if (null != ele && ele instanceof TextElement) {
                String text = ((TextElement) ele).getContent();
                Pattern pattern = Pattern.compile("#\\{.*?,.*?\\}");
                Matcher matcher = pattern.matcher(text);
                while (matcher.find()) {
                    String target = matcher.group(0);
                    if(null != createTimeJavaProperty && target.contains(createTimeJavaProperty)) {
                        text = text.replace(target, "now()");
                    }
                    if(null != modifiedTimeJavaProperty && target.contains(modifiedTimeJavaProperty)) {
                        text = text.replace(target, "now()");
                    }
                }
                if(text.matches("\\s+")){
                    newElementsArr[i] = null;
                } else {
                    newElementsArr[i] = new TextElement(text);
                }
            }
        }
        element.getElements().clear();
        for(int i = 0; i < newElementsArr.length; i++){
            if(null != newElementsArr[i]){
                element.addElement(newElementsArr[i]);
            }
        }
    }

    private void replaceInsertSelective(XmlElement element, IntrospectedTable introspectedTable){
        List<Element> oldElements = element.getElements();
        Element[] newElementsArr = new Element[oldElements.size()];
        for(int i = 0; i < oldElements.size(); i++){
            newElementsArr[i] = oldElements.get(i);
        }

        String createTimeJavaProperty = null;
        String modifiedTimeJavaProperty = null;
        for (IntrospectedColumn column : introspectedTable.getAllColumns()) {
            if (column.getActualColumnName().equals(createTimeColumnName)) {
                createTimeJavaProperty = column.getJavaProperty();
            }
            if (column.getActualColumnName().equals(modifiedTimeColumnName)) {
                modifiedTimeJavaProperty = column.getJavaProperty();
            }
        }

        for (int i = 0; i < oldElements.size(); i++) {
            Element ele = oldElements.get(i);
            // 对于字符串主键，是没有if判断节点的
            if (ele instanceof XmlElement) {
                // if的text节点
                XmlElement xmlElement = (XmlElement) ele;

                // 找出field 名称
                String text = ((TextElement) xmlElement.getElements().get(0)).getContent();
                if(text.matches("#\\{(.*?),.*?\\},?")){
                    Pattern pattern = Pattern.compile("#\\{(.*?),.*?\\}");
                    Matcher matcher = pattern.matcher(text);
                    while (matcher.find()) {
                        String target = matcher.group(0);
                        // 查找对应column
                        if(null != createTimeJavaProperty && target.contains(createTimeJavaProperty)) {
                            text = text.replace(target, "now()");
                            xmlElement.getAttributes().clear();
                            xmlElement.addAttribute(new Attribute("test", "1 == 1"));
                        }
                        if(null != modifiedTimeJavaProperty && target.contains(modifiedTimeJavaProperty)) {
                            text = text.replace(target, "now()");
                            xmlElement.getAttributes().clear();
                            xmlElement.addAttribute(new Attribute("test", "1 == 1"));
                        }
                        xmlElement.getElements().clear();
                        xmlElement.addElement(new TextElement(text));
                        newElementsArr[i] = xmlElement;
                    }
                } else {
                    if(text.contains(createTimeColumnName)) {
                        xmlElement.getAttributes().clear();
                        xmlElement.addAttribute(new Attribute("test", "1 == 1"));
                    }
                    if(text.contains(modifiedTimeColumnName)) {
                        xmlElement.getAttributes().clear();
                        xmlElement.addAttribute(new Attribute("test", "1 == 1"));
                    }
                }
            }
        }

        element.getElements().clear();
        for(int i = 0; i < newElementsArr.length; i++){
            if(null != newElementsArr[i]){
                element.addElement(newElementsArr[i]);
            }
        }
    }
}
