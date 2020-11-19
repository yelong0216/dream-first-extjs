/**
 * 
 */
package dream.first.extjs.controller;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.github.pagehelper.PageInfo;

import dream.first.base.queryinfo.filter.DFQueryFilterInfo;
import dream.first.base.queryinfo.sort.DFQuerySortInfo;
import dream.first.core.queryinfo.filter.QueryFilterInfo;
import dream.first.core.queryinfo.sort.QuerySortInfo;
import dream.first.extjs.base.controller.DFBaseExtJSCrudControllerable;
import dream.first.extjs.base.msg.DFEJsonMsg;
import dream.first.extjs.support.msg.JsonMsg;

/**
 * 
 * 支持通用增删改查请求的抽象Controller
 * 
 * @since 2.0
 */
public abstract class BaseExtJSCrudController<M> extends BaseExtJSController
		implements DFBaseExtJSCrudControllerable<M> {

	@Override
	public boolean validateModel(M model, DFEJsonMsg msg) throws Exception {
		JsonMsg jsonMsg = new JsonMsg();
		jsonMsg.putAll(msg);
		return validateModel(model, jsonMsg);
	}

	public boolean validateModel(M model, JsonMsg msg) throws Exception {
		return true;
	}

	@Override
	public PageInfo<?> queryModel(M model, Collection<DFQueryFilterInfo> queryFilterInfos,
			Collection<DFQuerySortInfo> querySortInfos, Integer pageNum, Integer pageSize) throws Exception {
		List<QueryFilterInfo> queryFilterInfoList = queryFilterInfos.stream().map(QueryFilterInfo::new)
				.collect(Collectors.toList());
		List<QuerySortInfo> querySortInfoList = querySortInfos.stream().map(QuerySortInfo::new)
				.collect(Collectors.toList());
		return queryModel(model, queryFilterInfoList, querySortInfoList, pageNum, pageSize);
	}

	/**
	 * 为兼容老版本。
	 * @see {@link #queryModel(Object, Collection, Collection, Integer, Integer)}
	 */
	public PageInfo<?> queryModel(M model, List<QueryFilterInfo> queryFilterInfos, List<QuerySortInfo> querySortInfos,
			Integer pageNum, Integer pageSize) throws Exception {
		throw new UnsupportedOperationException();
	}

}
