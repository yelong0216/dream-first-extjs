/**
 * 
 */
package org.yelong.dream.first.extjs.platform.dict.tpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.yelong.core.model.service.ModelService;
import org.yelong.dream.first.core.platform.dict.model.Dict;
import org.yelong.dream.first.core.platform.dict.service.DictCommonService;
import org.yelong.dream.first.extjs.platform.dict.bean.DictStore;
import org.yelong.support.freemarker.FreeMarkerConfigurationFactory;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

/**
 * @author PengFei
 * @since 1.0.0
 */
public class DictFreeMarkerSupport {

	private static Configuration freemarkerConfiguration = FreeMarkerConfigurationFactory.createConfigurationByClass(DictFreeMarkerSupport.class);

	private static final String DICT_FTL_NAME = "dict.ftl";

	@Resource
	private DictCommonService dictService;

	@Resource
	private ModelService modelService;
	
	/**
	 * 生成内容
	 * @param dictStore 字典
	 * @return js脚本
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws MalformedTemplateNameException 
	 * @throws TemplateNotFoundException 
	 * @throws TemplateException 
	 */
	public String generateContent( List<DictStore> dictStores) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		Map<String,List<DictStore>> dictStoreMap = new HashMap<>();
		dictStoreMap.put("dictStores", dictStores);
		Template dictTemplate = getDictTemplate();
		return FreeMarkerTemplateUtils.processTemplateIntoString(dictTemplate, dictStoreMap);
	}

	/**
	 * 生成脚本
	 * @param dictTypes 字典类型
	 * @return js脚本
	 */
	public String generateContent( String [] dictTypes) throws Exception{
		List<Dict> dictList = dictService.findByDictTypes(dictTypes);
		Map<String, List<Dict>> dictGroup = dictList.stream().collect(Collectors.groupingBy(Dict::getDictType));
		List<DictStore> dictStores = new ArrayList<DictStore>();
		for (Entry<String, List<Dict>> entry : dictGroup.entrySet()) {
			dictStores.add(dictStore(entry.getKey(), entry.getValue()));
		}
		return generateContent(dictStores);
	}

	/**
	 * 生成脚本
	 * @param dictTypes 字典类型
	 * @return js脚本
	 */
	public String generateContent() throws Exception{
		List<Dict> dictList = modelService.findAll(Dict.class);
		Map<String, List<Dict>> dictGroup = dictList.stream().collect(Collectors.groupingBy(Dict::getDictType));
		List<DictStore> dictStores = new ArrayList<DictStore>();
		for (Entry<String, List<Dict>> entry : dictGroup.entrySet()) {
			dictStores.add(dictStore(entry.getKey(), entry.getValue()));
		}
		return generateContent(dictStores);
	}
	
	/**
	 * 生成脚本
	 * 根据字典类型查询字典
	 * @param dictType 字典类型
	 * @return js脚本
	 */
	public String generateContent( String dictType ) throws Exception{
		List<Dict> dictList = dictService.findByDictType(dictType);
		return generateContent(dictType,dictList);
	}

	/**
	 * 根据类型和字典集合生成脚本
	 * @param dictType 字典类型
	 * @param dictList 字典集合
	 * @return js脚本
	 */
	public String generateContent( String dictType , List<Dict> dictList) throws Exception{
		List<DictStore> dictStores = new ArrayList<DictStore>();
		dictStores.add(dictStore(dictType,dictList));
		return generateContent(dictStores);
	}
	/**
	 * 字典的js脚本数据
	 * @param dictType 字典类型
	 * @param dictList 字典集合
	 * @return 单个字典的js脚本数据
	 * @throws Exception 如果dictType为空，或者dictList不存在元素
	 */
	public DictStore dictStore(String dictType ,List<Dict> dictList) throws Exception{
		if( StringUtils.isEmpty(dictType) || dictList.isEmpty() ) {
			return null;
		}
		StringBuilder data = new StringBuilder();
		dictList.stream().sorted().forEach(dict->{
			data.append("\n\t\t{dictValue : \"" + dict.getDictValue() + "\", dictText : \"" + dict.getDictText() + "\"},");
		});
		//删除最后一个逗号
		if( data.length() > 0 ) {
			data.deleteCharAt(data.length()-1);
		}
		return new DictStore(dictType, data.toString());
	}


	/**
	 * 获取字典模板
	 * @return 模板
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws MalformedTemplateNameException 
	 * @throws TemplateNotFoundException 
	 */
	public Template getDictTemplate() throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {
		return freemarkerConfiguration.getTemplate(DICT_FTL_NAME,"UTF-8");


	}


}
