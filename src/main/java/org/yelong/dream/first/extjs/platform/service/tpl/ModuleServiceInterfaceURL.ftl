//========================== 模块服务接口 =========================
//!!平台自动生成，请勿修改!!
var ModuleServiceInterface = {
<#list moduleServiceInterfaceList as moduleServiceInterface>
	${moduleServiceInterface.interfaceName} : "${moduleServiceInterface.interfaceURL}"
	<#if moduleServiceInterface_index + 1 < moduleServiceInterfaceList?size>,</#if>
</#list>
}