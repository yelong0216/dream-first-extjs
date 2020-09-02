package dream.first.extjs.platform.service.code;

import org.yelong.core.annotation.Nullable;

import dream.first.core.platform.service.manage.ModuleServiceManager;

/**
 * 模块服务接口代码管理器。生成服务接口JS代码
 * 
 * @since 2.0
 */
public interface ModuleServiceInterfaceCodeManager {

	/**
	 * 获取所有服务接口的JS代码
	 * 
	 * @return 所有服务接口JS代码
	 * @throws ModuleServiceInterfaceCodeManageException 模块服务接口管理异常
	 */
	String getJSCode() throws ModuleServiceInterfaceCodeManageException;

	/**
	 * 获取指定服务的接口的JS代码
	 * 
	 * @param serviceNames 服务名称数组
	 * @return 指定服务的接口JS代码
	 * @throws ModuleServiceInterfaceCodeManageException 模块服务接口管理异常
	 */
	String getJSCode(@Nullable String[] serviceNames) throws ModuleServiceInterfaceCodeManageException;

	/**
	 * 获取指定服务指定接口的JS代码
	 * 
	 * @param serviceNames          服务名称数组
	 * @param serviceInterfaceNames 接口名称数组
	 * @return 指定服务指定接口的JS代码
	 * @throws ModuleServiceInterfaceCodeManageException 模块服务接口管理异常
	 */
	String getJSCode(@Nullable String[] serviceNames, @Nullable String[] serviceInterfaceNames)
			throws ModuleServiceInterfaceCodeManageException;

	/**
	 * @return 模块服务管理器
	 */
	ModuleServiceManager getModuleServiceManager();

}
