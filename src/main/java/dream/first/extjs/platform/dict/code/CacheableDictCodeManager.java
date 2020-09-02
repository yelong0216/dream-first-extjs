package dream.first.extjs.platform.dict.code;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.yelong.core.cache.CacheManager;

import dream.first.core.cache.CacheManagerable;
import dream.first.core.platform.dict.manage.DictManager;
import dream.first.extjs.platform.support.AbstractCacheableCodeManager;

/**
 * 缓存支持的字典代码管理器
 * 
 * @since 2.0
 */
public class CacheableDictCodeManager extends AbstractCacheableCodeManager implements DictCodeManager {

	protected final DictCodeManager dictCodeManager;

	public CacheableDictCodeManager(DictCodeManager dictCodeManager) {
		this.dictCodeManager = dictCodeManager;
	}

	@Override
	public String getExtJSCode() throws DictCodeManageException {
		return putCacheObjIfAbsentAndSupportCache("DICT_CODE_EXTJS_ALL", x -> dictCodeManager.getExtJSCode());
	}

	@Override
	public String getExtJSCode(String dictType) throws DictCodeManageException {
		return putCacheObjIfAbsentAndSupportCache("DICT_CODE_EXTJS_DICTTYPE:" + dictType,
				x -> dictCodeManager.getExtJSCode(dictType));
	}

	@Override
	public String getExtJSCode(String[] dictTypes) throws DictCodeManageException {
		return putCacheObjIfAbsentAndSupportCache(
				"DICT_CODE_EXTJS_DICTTYPES:" + Stream.of(dictTypes).collect(Collectors.joining(",")),
				x -> dictCodeManager.getExtJSCode(dictTypes));
	}

	@Override
	public DictManager getDictManager() {
		return dictCodeManager.getDictManager();
	}

	@Override
	protected CacheManager getCacheManager() {
		DictManager dictManager = getDictManager();
		if (dictManager instanceof CacheManagerable) {
			return ((CacheManagerable) dictManager).getCacheManager();
		}
		return null;
	}

}
