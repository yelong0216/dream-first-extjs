/**
 * 
 */
package dream.first.extjs.platform.service.code;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.yelong.core.cache.CacheManager;

import dream.first.core.platform.service.manage.CacheableModuleServiceManager;
import dream.first.core.platform.service.manage.ModuleServiceManager;
import dream.first.extjs.platform.support.AbstractCacheableCodeManager;

/**
 * 缓存支持的模块服务接口代码管理器
 * 
 * @since 2.0
 */
public class CacheableModuleServiceInterfaceCodeManager extends AbstractCacheableCodeManager
		implements ModuleServiceInterfaceCodeManager {

	protected final ModuleServiceInterfaceCodeManager moduleServiceInterfaceCodeManager;

	public CacheableModuleServiceInterfaceCodeManager(
			ModuleServiceInterfaceCodeManager moduleServiceInterfaceCodeManager) {
		this.moduleServiceInterfaceCodeManager = moduleServiceInterfaceCodeManager;
	}

	@Override
	public String getJSCode() throws ModuleServiceInterfaceCodeManageException {
		return putCacheObjIfAbsentAndSupportCache("JS_CODE_ALL", x -> moduleServiceInterfaceCodeManager.getJSCode());
	}

	@Override
	public String getJSCode(String[] serviceNames) throws ModuleServiceInterfaceCodeManageException {
		if (ArrayUtils.isEmpty(serviceNames)) {
			return getJSCode();
		}
		return putCacheObjIfAbsentAndSupportCache(
				"JS_CODE_SERVICENAMES:" + Stream.of(serviceNames).collect(Collectors.joining(",")),
				x -> moduleServiceInterfaceCodeManager.getJSCode(serviceNames));
	}

	@Override
	public String getJSCode(String[] serviceNames, String[] serviceInterfaceNames)
			throws ModuleServiceInterfaceCodeManageException {
		if (ArrayUtils.isEmpty(serviceInterfaceNames)) {
			return getJSCode(serviceNames);
		}
		String key = "";
		if (ArrayUtils.isEmpty(serviceNames)) {
			key += "JS_CODE_SERVICENAMES:null";
		}
		key += "__";
		key += "INTERFACES:" + Stream.of(serviceInterfaceNames).collect(Collectors.joining(","));

		return putCacheObjIfAbsentAndSupportCache(key,
				x -> moduleServiceInterfaceCodeManager.getJSCode(serviceNames, serviceInterfaceNames));
	}

	@Override
	public ModuleServiceManager getModuleServiceManager() {
		return moduleServiceInterfaceCodeManager.getModuleServiceManager();
	}

	@Override
	protected CacheManager getCacheManager() {
		ModuleServiceManager moduleServiceManager = getModuleServiceManager();
		if (moduleServiceManager instanceof CacheableModuleServiceManager) {
			return ((CacheableModuleServiceManager) moduleServiceManager).getCacheManager();
		}
		return null;
	}

}
