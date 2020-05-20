/**
 * 
 */
package org.yelong.dream.first.extjs.controller;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.util.NumberUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.InitBinder;
import org.yelong.core.model.ModelNullProperty;
import org.yelong.core.model.service.ModelService;
import org.yelong.dream.first.core.controller.BaseCoreController;
import org.yelong.dream.first.core.queryinfo.QueryInfo;
import org.yelong.dream.first.core.queryinfo.filter.QueryFilterInfo;
import org.yelong.dream.first.core.service.DreamFirstModelService;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;

/**
 * 基础平台 controller
 * 
 * @author PengFei
 * @since 1.0.0
 */
@CrossOrigin
public abstract class BaseExtjsController extends BaseCoreController{

	@Resource
	protected DreamFirstModelService modelService;

	/**
	 * 获取排序的字段集合
	 * 
	 * 前台默认会传入sort参数作为排序属性
	 * 
	 * @return 排序的字段集合
	 */
	@SuppressWarnings("unchecked")
	protected Map<String,String> getSortFieldMap(){
		String sortInfoJson = getRequest().getParameter("sort");
		if(StringUtils.isBlank(sortInfoJson)) {
			return Collections.emptyMap();
		} else {
			Gson gson = getGson();
			ArrayList<Map<String,String>> sortInfoMapList = gson.fromJson(sortInfoJson, ArrayList.class);
			Map<String,String> sortFieldMap = new HashMap<>(sortInfoMapList.size());
			for (Map<String, String> map : sortInfoMapList) {
				sortFieldMap.put(map.get("property"), map.getOrDefault("direction","DESC"));
			}
			return sortFieldMap;
		}
	}

	/**
	 * 分页信息转换为json
	 * @param pageInfo 分页信息
	 * @return json
	 */
	protected String pageInfoToJson(PageInfo<?> pageInfo) {
		Gson gson = getGson();
		Map<String,Object> result = new HashMap<String,Object>(2);
		result.put("total", pageInfo.getTotal());
		result.put("root", pageInfo.getList());
		return gson.toJson(result);
	}

	/**
	 * 修改model注入的前缀
	 * @param binder web 数据装订者
	 */
	@InitBinder
	public void initBinderModel(WebDataBinder binder) {    
		binder.setFieldDefaultPrefix("model.");    
		//加入时间解析
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, new NumberFormat() {
			private static final long serialVersionUID = -8790771485055558186L;

			@Override
			public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
				return null;
			}

			@Override
			public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
				return null;
			}

			@Override
			public Number parse(String source, ParsePosition parsePosition) {
				if(StringUtils.isBlank(source)) {
					parsePosition.setIndex(1);
					return ModelNullProperty.INTEGER_NULL;
				} else {
					parsePosition.setIndex(source.length());
					return NumberUtils.parseNumber(source, Integer.class);
				}
			}
		},false));
	} 

	/**
	 * @return 获取请求中传入的查询过滤信息
	 */
	protected List<QueryFilterInfo> getQueryFilterInfos(){
		//增加queryFilterInfos查询条件
		String filters = getRequest().getParameter("filters");
		if(StringUtils.isBlank(filters)) {
			return Collections.emptyList();
		} else {
			QueryInfo queryInfo = getGson().fromJson(filters, QueryInfo.class);
			List<QueryFilterInfo> queryFilterInfos = queryInfo.getFilters();
			if(CollectionUtils.isEmpty(queryFilterInfos)) {
				return Collections.emptyList();
			} else {
				return queryFilterInfos;
			}
		}
	}

	//=====================Get/Set==================

	public ModelService getModelService() {
		return modelService;
	}

}
