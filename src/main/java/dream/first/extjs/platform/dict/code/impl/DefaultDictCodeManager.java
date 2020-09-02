package dream.first.extjs.platform.dict.code.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.yelong.support.freemarker.FreeMarkerConfigurationFactory;

import dream.first.core.platform.dict.manage.DictManager;
import dream.first.core.platform.dict.model.Dict;
import dream.first.extjs.platform.dict.code.DictCodeManageException;
import dream.first.extjs.platform.dict.code.DictCodeManager;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;

/**
 * 字典ExtJS管理器默认实现
 * 
 * @since 2.0
 */
public class DefaultDictCodeManager implements DictCodeManager {

	private static Configuration freemarkerConfiguration = FreeMarkerConfigurationFactory
			.createConfigurationByClass(DefaultDictCodeManager.class);

	private static final String DICT_FTL_NAME = "dict.ftl";

	private final DictManager dictManager;

	public DefaultDictCodeManager(DictManager dictManager) {
		this.dictManager = dictManager;
	}

	@Override
	public String getExtJSCode() throws DictCodeManageException {
		List<Dict> dictList = dictManager.getAll();
		Map<String, List<Dict>> dictGroup = dictList.stream().collect(Collectors.groupingBy(Dict::getDictType));
		List<DictStore> dictStores = new ArrayList<DictStore>();
		for (Entry<String, List<Dict>> entry : dictGroup.entrySet()) {
			dictStores.add(dictStore(entry.getKey(), entry.getValue()));
		}
		return generateContent(dictStores);
	}

	@Override
	public String getExtJSCode(String dictType) throws DictCodeManageException {
		List<Dict> dictList = dictManager.getByDictType(dictType);
		return generateContent(dictType, dictList);
	}

	@Override
	public String getExtJSCode(String[] dictTypes) throws DictCodeManageException {
		List<Dict> dictList = dictManager.getByDictTypes(dictTypes);
		Map<String, List<Dict>> dictGroup = dictList.stream().collect(Collectors.groupingBy(Dict::getDictType));
		List<DictStore> dictStores = new ArrayList<DictStore>();
		for (Entry<String, List<Dict>> entry : dictGroup.entrySet()) {
			dictStores.add(dictStore(entry.getKey(), entry.getValue()));
		}
		return generateContent(dictStores);
	}

	@Override
	public DictManager getDictManager() {
		return this.dictManager;
	}

	/**
	 * 字典的JS脚本数据
	 * 
	 * @param dictType 字典类型
	 * @param dictList 字典集合
	 * @return 单个字典的JS脚本数据
	 */
	public DictStore dictStore(String dictType, List<Dict> dictList) {
		if (StringUtils.isEmpty(dictType) || dictList.isEmpty()) {
			return null;
		}
		StringBuilder data = new StringBuilder();
		dictList.stream().sorted().forEach(dict -> {
			data.append(
					"\n\t\t{dictValue : \"" + dict.getDictValue() + "\", dictText : \"" + dict.getDictText() + "\"},");
		});
		// 删除最后一个逗号
		if (data.length() > 0) {
			data.deleteCharAt(data.length() - 1);
		}
		return new DictStore(dictType, data.toString());
	}

	/**
	 * 根据类型和字典集合生成脚本
	 * 
	 * @param dictType 字典类型
	 * @param dictList 字典集合
	 * @return JS脚本
	 */
	public String generateContent(String dictType, List<Dict> dictList) throws DictCodeManageException {
		List<DictStore> dictStores = new ArrayList<DictStore>();
		dictStores.add(dictStore(dictType, dictList));
		return generateContent(dictStores);
	}

	/**
	 * 生成内容
	 * 
	 * @param dictStore 字典
	 * @return JS脚本字符串
	 * @throws DictCodeManageException 字典JS管理异常
	 */
	public String generateContent(List<DictStore> dictStores) throws DictCodeManageException {
		Map<String, List<DictStore>> dictStoreMap = new HashMap<>();
		dictStoreMap.put("dictStores", dictStores);
		try {
			Template dictTemplate = getDictTemplate();
			return FreeMarkerTemplateUtils.processTemplateIntoString(dictTemplate, dictStoreMap);
		} catch (Exception e) {
			throw new DictCodeManageException(e);
		}
	}

	/**
	 * 获取字典模板
	 */
	protected Template getDictTemplate()
			throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {
		return freemarkerConfiguration.getTemplate(DICT_FTL_NAME, "UTF-8");
	}

}
