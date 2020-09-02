/**
 * 
 */
package dream.first.extjs.controller;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yelong.core.annotation.Nullable;

import com.github.pagehelper.PageInfo;

import dream.first.core.queryinfo.filter.QueryFilterInfo;
import dream.first.core.queryinfo.sort.QuerySortInfo;
import dream.first.extjs.support.msg.JsonFormData;
import dream.first.extjs.support.msg.JsonMsg;

/**
 * 
 * 支持通用增删改查请求的抽象Controller
 * 
 * @since 2.0
 */
public abstract class BaseExtJSCrudController<M> extends BaseExtJSController {

	// ==================================================static/final/==================================================

	/**
	 * 页码参数名
	 */
	public static final String PAGE_NUM_PARAMETER_NAME = "page";

	/**
	 * 页面大小参数名
	 */
	public static final String PAGE_SIZE_PARAMETER_NAME = "limit";

	// ==================================================save/modify==================================================

	/**
	 * save时验证model。 未通过验证则不进行保存且返回异常信息。 异常信息可以设置在msg中
	 * 
	 * @param model bind model
	 * @param msg   异常信息
	 * @return <tt>true</tt> 验证成功
	 * @throws Exception 验证异常
	 */
	protected boolean validateModel(M model, JsonMsg msg) throws Exception {
		return true;
	}

	/**
	 * 在保存之前执行的操作
	 * 
	 * @param model bind model
	 * @throws Exception 操作异常
	 */
	protected void beforeSave(M model) throws Exception {

	}

	/**
	 * 在修改之前执行的操作
	 * 
	 * @param model bind model
	 * @throws Exception 操作异常
	 */
	protected void beforeModify(M model) throws Exception {

	}

	/**
	 * 保存和修改均调用此方法 判断是否是保存和修改的方式通过 {@link #isNew(Object)} 进行判断
	 * 
	 * @param model bind model
	 * @return 响应结果 一般为JSON
	 * @throws Exception 保存失败
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public Object save(@ModelAttribute M model) throws Exception {
		JsonMsg msg = new JsonMsg(false, "数据保存失败！");
		// 验证model
		if (!validateModel(model, msg)) {
			msg.setMsg(StringUtils.isNotBlank(msg.getMsg()) ? msg.getMsg() : "数据验证未通过，保存失败！");
		} else {
			boolean isNew = isNew(model);
			if (isNew) {
				beforeSave(model);
				saveModel(model);
				afterSave(model);
			} else {
				beforeModify(model);
				modifyModel(model);
				afterModify(model);
			}
			msg.setSuccess(true);
			msg.setMsg("保存成功！");
		}
		return toJson(msg);
	}

	/**
	 * 保存model。 重写此方法覆盖默认的保存方法
	 * 
	 * @param model bind model
	 * @throws Exception 保存异常
	 */
	protected abstract void saveModel(M model) throws Exception;

	/**
	 * 修改model 重写此方法覆盖默认的修改方法
	 * 
	 * @param model bind model
	 * @throws Exception 修改异常
	 */
	protected abstract void modifyModel(M model) throws Exception;

	/**
	 * 在保存之后执行的操作
	 * 
	 * @param model bind model
	 * @throws Exception 操作异常
	 */
	protected void afterSave(M model) throws Exception {

	}

	/**
	 * 在修改之后执行的操作
	 * 
	 * @param model bind model
	 * @throws Exception 操作异常
	 */
	protected void afterModify(M model) throws Exception {

	}

	/**
	 * 是否是新增model
	 * 
	 * @param model model
	 * @return <tt>true</tt> 是新增model
	 */
	protected abstract boolean isNew(M model);

	/**
	 * 是否是修改model
	 * 
	 * @param model model
	 * @return <tt>true</tt> 是修改model
	 */
	protected boolean isModify(M model) {
		return !isNew(model);
	}

	// ==================================================delete==================================================

	/**
	 * 在删除之前执行的操作
	 * 
	 * @param deleteIds 删除的标识值，多个通过逗号分隔
	 * @return 删除的标识值，多个通过逗号分隔
	 * @throws Exception 操作异常
	 */
	protected String beforeDelete(String deleteIds) throws Exception {
		return deleteIds;
	}

	/**
	 * 删除。根据传入的参数 {deleteIds} 进行删除。这个值为删除数据的某个标识的值，多个用逗号分隔
	 * 
	 * @return 响应的结果信息，一般为JSON
	 * @throws Exception 删除失败
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public Object delete() throws Exception {
		JsonMsg msg = new JsonMsg(false, "数据删除失败！");
		String deleteIds = getRequest().getParameter("deleteIds");
		if (StringUtils.isBlank(deleteIds)) {
			msg.setMsg("数据删除失败！未发现数据标识！");
		} else {
			deleteIds = beforeDelete(deleteIds);
			deleteModel(deleteIds);
			afterDelete(deleteIds);
			msg.setSuccess(true);
			msg.setMsg("删除成功！");
		}
		return toJson(msg);
	}

	/**
	 * 删除数据 覆盖此方法重写删除功能
	 * 
	 * @param deleteIds 删除的标识值。多个用逗号分隔
	 * @return 是否删除成功
	 */
	protected abstract boolean deleteModel(String deleteIds) throws Exception;

	/**
	 * 在删除之后执行的操作
	 * 
	 * @param deleteIds 删除的标识值。多个用逗号分隔
	 * @throws Exception 操作异常
	 */
	protected void afterDelete(String deleteIds) throws Exception {

	}

	// ==================================================query==================================================

	/**
	 * 保存之前执行的操作
	 * 
	 * @param model bind model
	 * @throws Exception 操作异常
	 */
	protected void beforeQuery(M model) throws Exception {

	}

	/**
	 * 查询
	 * 
	 * @param model bind model
	 * @return 响应的结果信息。一般为JSON
	 * @throws Exception 查询失败
	 */
	@ResponseBody
	@RequestMapping(value = "query")
	public Object query(@ModelAttribute M model) throws Exception {
		JsonMsg msg = new JsonMsg(false, "服务端异常，数据获取失败！");
		PageInfo<?> pageInfo = null;
		beforeQuery(model);
		Integer pageNum = getPageNum();
		Integer pageSize = getPageSize();
		Objects.requireNonNull(pageNum, "必填参数缺失：pageNum");
		Objects.requireNonNull(pageNum, "必填参数缺失：pageSize");
		List<QuerySortInfo> querySortInfos = getQuerySortInfos();
		List<QueryFilterInfo> queryFilterInfos = getQueryFilterInfos();
		pageInfo = queryModel(model, queryFilterInfos, querySortInfos, pageNum, pageSize);
		afterQuery(model, pageInfo);
		msg.setSuccess(true);
		msg.setMsg("查询成功");
		return msg.isSuccess() ? pageInfoToJson(pageInfo) : toJson(msg);
	}

	/**
	 * 重写此方法，覆盖查询功能
	 * 
	 * @param model            bind model
	 * @param queryFilterInfos 过滤条件集合
	 * @param querySortInfos   排序信息集合
	 * @param pageNum          页码
	 * @param pageSize         页面大小
	 * @return 查询分页信息
	 * @throws Exception 查询异常
	 */
	protected abstract PageInfo<?> queryModel(M model, @Nullable List<QueryFilterInfo> queryFilterInfos,
			@Nullable List<QuerySortInfo> querySortInfos, Integer pageNum, Integer pageSize) throws Exception;

	/**
	 * 查询之后的操作
	 * 
	 * @param model    bind model
	 * @param pageInfo 查询的结果信息
	 * @throws Exception 操作异常
	 */
	protected void afterQuery(M model, PageInfo<?> pageInfo) throws Exception {

	}

	/**
	 * 获取页码。这参数一般只会在分页查询时传入
	 * 
	 * @return 页码。如果没有传入参数则为null
	 * @see #PAGE_NUM_PARAMETER_NAME
	 */
	@Nullable
	protected Integer getPageNum() {
		return getParameterInteger(PAGE_NUM_PARAMETER_NAME);
	}

	/**
	 * 获取页面大小。这个参数一般只会在分页查询时传入
	 * 
	 * @return 页面大小。如果没有传入参数则为null
	 * @see #PAGE_SIZE_PARAMETER_NAME
	 */
	@Nullable
	protected Integer getPageSize() {
		return getParameterInteger(PAGE_SIZE_PARAMETER_NAME);
	}

	// ==================================================retrieve==================================================

	/**
	 * 在获取之前的操作
	 * 
	 * @param model bind model
	 * @throws Exception 操作异常
	 */
	protected void beforeRetrieve(M model) throws Exception {

	}

	/**
	 * 获取单个数据
	 * 
	 * @param model bind model
	 * @return 响应的结果信息，一般为JSON
	 * @throws Exception 获取异常
	 */
	@ResponseBody
	@RequestMapping(value = "retrieve")
	public Object retrieve(@ModelAttribute M model) throws Exception {
		JsonMsg msg = new JsonMsg(false, "服务端异常，数据获取失败！");
		JsonFormData<M> jsonFormData = null;
		beforeRetrieve(model);
		M queryAfterModel = retrieveModel(model);
		afterRetrieve(model, queryAfterModel);
		if (null == queryAfterModel) {
			msg.setMsg("数据标识错误，获取数据失败！");
		} else {
			jsonFormData = new JsonFormData<>(true, queryAfterModel);
			msg.setSuccess(true);
		}
		return msg.isSuccess() ? toJson(jsonFormData) : toJson(msg);
	}

	/**
	 * 执行获取。重写此方法可以重写获取方式
	 * 
	 * @param model bind model
	 * @return 获取的数据
	 * @throws Exception 获取异常
	 */
	@Nullable
	protected abstract M retrieveModel(M model) throws Exception;

	/**
	 * 在获取之后执行的操作
	 * 
	 * @param bindModel bind model
	 * @param model     获取的数据
	 * @throws Exception 操作异常
	 */
	protected void afterRetrieve(M bindModel, M model) throws Exception {

	}

	@SuppressWarnings("unchecked")
	protected Class<M> getModelClass() {
		Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(getClass(),
				BaseExtJSCrudController.class);
		if (MapUtils.isNotEmpty(typeArguments)) {
			for (Entry<TypeVariable<?>, Type> entry : typeArguments.entrySet()) {
				if (entry.getKey().getName().contentEquals("M")) {
					return (Class<M>) entry.getValue();
				}
			}
		}
		throw new RuntimeException("未发现泛型model");
	}

}
