//========================== 数据字典 =========================
//!!平台自动生成，请勿修改!!
Co.defineModel("DictPair", ["dictValue", "dictText"]);
<#list dictStores as dictStore>
var ${dictStore.type}Store = Ext.create("Ext.data.Store",{
	storeId : "${dictStore.type}Store",
	model : "DictPair",
	data : [${dictStore.data}
	]
});
</#list>
