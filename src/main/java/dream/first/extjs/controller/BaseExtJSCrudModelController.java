/**
 * 
 */
package dream.first.extjs.controller;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.yelong.core.jdbc.sql.sort.support.Sort;
import org.yelong.core.model.Modelable;
import org.yelong.core.model.sql.SqlModel;

import com.github.pagehelper.PageInfo;

import dream.first.core.model.BaseModelable;
import dream.first.core.queryinfo.filter.QueryFilterInfo;
import dream.first.core.queryinfo.sort.QuerySortInfo;
import dream.first.core.queryinfo.sort.QuerySortInfos;

/**
 * 执行模型通用增删改查方法的Controller
 * 
 * @since 2.0
 */
public abstract class BaseExtJSCrudModelController<M extends BaseModelable> extends BaseExtJSCrudController<M> {

	@Override
	protected void saveModel(M model) throws Exception {
		modelService.saveSelective(model);
	}

	@Override
	protected void modifyModel(M model) throws Exception {
		modelService.modifySelectiveById(model);
	}

	@Override
	protected boolean isNew(M model) {
		return StringUtils.isBlank(model.getId());
	}

	@Override
	protected boolean deleteModel(String deleteIds) throws Exception {
		return modelService.removeByIds(getModelClass(), deleteIds.split(",")) > 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected final PageInfo<?> queryModel(M model, List<QueryFilterInfo> queryFilterInfos,
			List<QuerySortInfo> querySortInfos, Integer pageNum, Integer pageSize) throws Exception {
		SqlModel<M> sqlModel;
		//如果bind的model是SqlModel的子类则直接使用，否则实例化SqlModel
		if (model instanceof SqlModel) {
			sqlModel = (SqlModel<M>)model;
		} else {
			sqlModel = new SqlModel<>(model);
		}
		if (CollectionUtils.isNotEmpty(querySortInfos)) {
			List<Sort> sorts = QuerySortInfos.toSort(querySortInfos);
			sorts.forEach(x -> addModelSortField(sqlModel, x.getColumn(), x.getDirection()));
		}
		if (CollectionUtils.isNotEmpty(queryFilterInfos)) {
			sqlModel.addConditions(modelService.getQueryFilterInfoResolver().resolve(queryFilterInfos));
		}
		return queryModel(sqlModel, pageNum, pageSize);
	}

	/**
	 * 添加SqlModel的排序字段。重写此方法可以实现不同的字段添加别名等功能
	 * 
	 * @param sqlModel  SqlModel
	 * @param sortField 排序字典
	 * @param sortOrder 排序方向
	 */
	protected void addModelSortField(SqlModel<M> sqlModel, String sortField, String sortOrder) {
		sqlModel.addSortField(sortField, sortOrder);
	}

	/**
	 * 重写此方法，覆盖查询功能
	 * 
	 * @param sqlModel SqlModel
	 * @param pageNum  页码
	 * @param pageSize 页面大小
	 * @return 查询分页信息
	 * @throws Exception 查询异常
	 */
	protected PageInfo<?> queryModel(SqlModel<M> sqlModel, Integer pageNum, Integer pageSize) throws Exception {
		Class<? extends Modelable> modelClass = getModelClass();
		return new PageInfo<>(modelService.findPageBySqlModel(modelClass, sqlModel, pageNum, pageSize));
	}

	@Override
	protected M retrieveModel(M model) throws Exception {
		SqlModel<M> sqlModel = new SqlModel<>(model);
		Class<M> modelClass = getModelClass();
		return modelService.findFirstBySqlModel(modelClass, sqlModel);
	}

}
