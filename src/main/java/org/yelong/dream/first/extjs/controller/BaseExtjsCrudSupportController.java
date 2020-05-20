/**
 * 
 */
package org.yelong.dream.first.extjs.controller;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yelong.core.annotation.Nullable;
import org.yelong.core.model.Modelable;
import org.yelong.core.model.sql.SqlModel;
import org.yelong.dream.first.core.model.BaseModelable;
import org.yelong.dream.first.core.queryinfo.filter.QueryFilterInfo;
import org.yelong.dream.first.extjs.support.msg.JsonFormData;
import org.yelong.dream.first.extjs.support.msg.JsonMsg;

import com.github.pagehelper.PageInfo;

/**
 * 基础增删改查支持的controller
 * 
 * @author PengFei
 * @since 1.0.0
 */
public abstract class BaseExtjsCrudSupportController <M extends BaseModelable> extends BaseExtjsController{

	/**
	 * 保存和修改均调用此方法
	 * 判断是否是保存和修改的方式通过id是否存在进行判断
	 * @param model model
	 * @return 响应的接口一般为json
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public Object save(@ModelAttribute M model) throws Exception{
		JsonMsg msg = new JsonMsg(false,"数据保存失败！");
		//验证model
		if( !validateModel(model, msg) ) {
			msg.setMsg(StringUtils.isNotBlank(msg.getMsg()) ? msg.getMsg() : "数据验证未通过，保存失败！");
		} else {
			boolean isNew = isNew(model);
			if( isNew ) {
				beforeSave(model);
				saveModel(model);
				afterSave(model);
			} else {
				beforeModify(model);
				modifyModel(model);
				afterModify(model);
			}
			afterSave(model);
			msg.setSuccess(true);
			msg.setMsg("保存成功！");
		}
		return toJson(msg);
	}

	protected void beforeSave(M model) {

	}

	protected void beforeModify(M model) {

	}

	protected void afterSave(M model) {

	}

	protected void afterModify(M model) {

	}

	/**
	 * 是否是新增model
	 * @param model model 
	 * @return <tt>true</tt> 是新增model
	 */
	protected boolean isNew(M model) {
		return StringUtils.isBlank(model.getId());
	}

	/**
	 * 是否是修改model
	 * @param model model
	 * @return <tt>true</tt> 是修改model
	 */
	protected boolean isModify(M model) {
		return !isNew(model);
	}

	/**
	 * 保存model。
	 * 重写此方法覆盖默认的保存方法
	 * @param model model
	 */
	protected void saveModel(M model) throws Exception {
		getModelService().saveSelective(model);
	}

	/**
	 * 修改model
	 * 重写此方法覆盖默认的修改方法
	 * @param model model
	 * @throws Exception
	 */
	protected void modifyModel(M model) throws Exception{
		getModelService().modifySelectiveById(model);
	}

	/**
	 * save时验证model。
	 * 未通过验证则不进行保存且返回异常信息。
	 * 异常信息可以设置在msg中
	 * @param model model
	 * @param msg 异常信息
	 * @return <tt>true</tt> 验证成功
	 */
	protected boolean validateModel(M model , JsonMsg msg) {
		return true;
	}

	/**
	 * 删除
	 * @return 响应信息
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public Object delete() throws Exception {
		JsonMsg msg = new JsonMsg(false, "数据删除失败！");
		String deleteIds = getRequest().getParameter("deleteIds");
		if(StringUtils.isBlank(deleteIds)) {
			msg.setMsg("数据删除失败！未发现数据标识！");
		} else {
			deleteIds = beforeDeleteModel(deleteIds);
			deleteModel(deleteIds);
			afterDeleteModel(deleteIds);
			msg.setSuccess(true);
			msg.setMsg("删除成功！");
		}
		return toJson(msg);
	}

	protected String beforeDeleteModel(String deleteIds) {
		return deleteIds;
	}

	/**
	 * 删除数据
	 * 覆盖此方法重写删除功能
	 * @param deleteIds 删除的id。根据业务不同，可能不是id，或许是其他唯一标识性的字段
	 * @return 是否删除成功
	 * @throws Exception 
	 */
	protected boolean deleteModel(String deleteIds) throws Exception {
		return getModelService().removeByIds(getModelClass(), deleteIds.split(",")) > 0;
	}

	protected void afterDeleteModel(String deleteIds) {

	}

	/**
	 * 查询
	 * @param model model
	 * @return 响应的信息
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "query",method = RequestMethod.POST)
	public Object query( @ModelAttribute M model ) throws Exception {
		JsonMsg msg = new JsonMsg(false, "服务端异常，数据获取失败！");
		PageInfo<?> pageInfo = null;
		SqlModel sqlModel = new SqlModel(model);
		beforeQueryModel(model);
		Map<String, String> sortFieldMap = getSortFieldMap();
		for (Entry<String, String> entry : sortFieldMap.entrySet()) {
			addModelSortField(sqlModel, entry.getKey(), entry.getValue());
		}
		Integer pageNum = Integer.valueOf(getRequest().getParameter("page"));
		Integer pageSize = Integer.valueOf(getRequest().getParameter("limit"));

		//增加queryFilterInfos查询条件
		List<QueryFilterInfo> queryFilterInfos = getQueryFilterInfos();
		if(CollectionUtils.isNotEmpty(queryFilterInfos)) {
			for (QueryFilterInfo queryFilterInfo : queryFilterInfos) {
				sqlModel.addCondition(modelService.getQueryFilterInfoResolver().resolve(queryFilterInfo));
			}
		}
		pageInfo = queryModel(sqlModel, pageNum, pageSize);
		afterQueryModel(model, pageInfo);
		msg.setSuccess(true);
		msg.setMsg("查询成功");
		return msg.isSuccess() ? pageInfoToJson(pageInfo) : toJson(msg);
	}

	/**
	 * 添加model 的排序字段
	 * 重写此方法实现不同的字段添加别名等功能
	 * @param <M>
	 * @param model model
	 * @param sortField 排序字段
	 * @param sortOrder 排序方向
	 */
	protected void addModelSortField(SqlModel model,String sortField, String sortOrder){
		model.addSortField(sortField,sortOrder);
	}

	protected void beforeQueryModel(M model) {

	}

	/**
	 * 重写此方法，覆盖查询功能
	 * @param model 条件model
	 * @param pageNum 页码
	 * @param pageSize 页面大小
	 * @return 查询信息
	 * @throws Exception
	 */
	protected PageInfo<?> queryModel(SqlModel model,Integer pageNum , Integer pageSize) throws Exception {
		Class<? extends Modelable> c = getModelClass();
		return new PageInfo<>(modelService.findPageBySqlModel(c,model, pageNum, pageSize));
	}

	protected void afterQueryModel(M model,PageInfo<?> pageInfo) {

	}

	/**
	 * 获取
	 * @param model model
	 * @return 响应的信息
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "retrieve",method = RequestMethod.POST)
	public Object retrieve( @ModelAttribute M model ) throws Exception{
		JsonMsg msg = new JsonMsg(false, "服务端异常，数据获取失败！");
		JsonFormData<M> jsonFormData = null;
		beforeRetrieveModel(model);
		M queryAfterModel = retrieveModel(model);
		afterRetrieveModel(model, queryAfterModel);
		if( null == queryAfterModel ) {
			msg.setMsg("数据标识错误，获取数据失败！");
		} else {
			jsonFormData = new JsonFormData<>(true, queryAfterModel);
			msg.setSuccess(true);
		}
		return msg.isSuccess() ? toJson(jsonFormData) : toJson(msg);
	}

	protected void beforeRetrieveModel(M model) {

	}

	@Nullable
	protected M retrieveModel(M model) throws Exception {
		return (M) modelService.findFirstBySqlModel(getModelClass(),new SqlModel(model));
	}

	/**
	 * 
	 * @param conditionModel 查询信息的model
	 * @param model 查询到的model
	 */
	protected void afterRetrieveModel(M conditionModel , M model) {

	}

	@SuppressWarnings("unchecked")
	protected Class<M> getModelClass() {
		Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(getClass(), BaseExtjsCrudSupportController.class);
		if(MapUtils.isNotEmpty(typeArguments)) {
			for (Entry<TypeVariable<?>, Type> entry : typeArguments.entrySet()) {
				if(entry.getKey().getName().contentEquals("M")) {
					return (Class<M>) entry.getValue();
				}
			}
		}
		throw new RuntimeException("未发现泛型model");
	}

}
