/**
 * 
 */
package dream.first.extjs.controller;

import java.util.Collection;

import com.github.pagehelper.PageInfo;

import dream.first.base.model.DreamFirstBaseModelable;
import dream.first.base.queryinfo.filter.DFQueryFilterInfo;
import dream.first.base.queryinfo.filter.DFQueryFilterInfoResolver;
import dream.first.base.queryinfo.sort.DFQuerySortInfo;
import dream.first.extjs.base.controller.DFBaseExtJSCrudModelControllerable;

/**
 * 执行模型通用增删改查方法的Controller
 * 
 * @since 2.0
 */
public abstract class BaseExtJSCrudModelController<M extends DreamFirstBaseModelable> extends BaseExtJSCrudController<M>
		implements DFBaseExtJSCrudModelControllerable<M> {

	@Override
	public DFQueryFilterInfoResolver getDFQueryFilterInfoResolver() {
		return getModelService().getQueryFilterInfoResolver();
	}

	@Override
	public PageInfo<?> queryModel(M model, Collection<DFQueryFilterInfo> queryFilterInfos,
			Collection<DFQuerySortInfo> querySortInfos, Integer pageNum, Integer pageSize) throws Exception {
		return DFBaseExtJSCrudModelControllerable.super.queryModel(model, queryFilterInfos, querySortInfos, pageNum,
				pageSize);
	}

}
