#### 访问方式为：

#### GET访问方式

#### http://192.168.2.186:9091/sys/linker

#### 传输的参数为：

{
"dsname": "新产品测试库",
"comCode": "00",
"dspassword": "devpass",
"establish": [{
"tabName": "aaaa",
"tabDes": "测试预测订单头",
"fields": [{
"fieldName": "com_code",
"fieldDes": "公司号",
"fieldType": "Number",
"fieldLength": "10",
"indFlag": "Y"
}, {
"fieldName": "dep_code",
"fieldDes": "公司号",
"fieldType": "Number",
"fieldLength": "10",
"indFlag": "N"
}]
}, {
"tabName": "wwww",
"tabDes": "产品物料清单表",
"fields": [{
"fieldName": "com_code",
"fieldDes": "公司号",
"fieldType": "Number",
"fieldLength": "10",
"indFlag": "Y"
}, {
"fieldName": "dep_code",
"fieldDes": "公司号",
"fieldType": "Number",
"fieldLength": "10",
"indFlag": "Y"
}]
}
],
"maintain": [{
"tabName": "JIU",
"addFields": [{
"fieldName": "COM_CODEJIU",
"fieldDes": "公司号11",
"fieldType": "Number",
"fieldLength": "20",
"indFlag": "N"
}, {
"fieldName": "DEP_CODEFFF",
"fieldDes": "公司号22",
"fieldType": "Number",
"fieldLength": "10",
"indFlag": "N"
}],
"altFields": [{
"fieldName": "DEP_CODEFFF",
"fieldDes": "公司号",
"fieldType": "Number",
"fieldLength": "2",
"indFlag": "Y"
}, {
"fieldName": "COM_CODEJIU",
"fieldDes": "房间号",
"fieldType": "Number",
"fieldLength": "5",
"indFlag": "Y"
}],
"delFields": [{
"fieldName": "COM_CODEJIU",
"fieldDes": "公司号",
"fieldType": "Number",
"fieldLength": "10",
"indFlag": "Y"
}, {
"fieldName": "dep_code1",
"fieldDes": "公司号",
"fieldType": "Number",
"fieldLength": "10",
"indFlag": "Y"
}]
}, {
"tabName": "cxjtest2",
"addFields": [{
"fieldName": "com_code",
"fieldDes": "公司号",
"fieldType": "Number",
"fieldLength": "10",
"indFlag": "Y"
}, {
"fieldName": "dep_code",
"fieldDes": "公司号",
"fieldType": "Number",
"fieldLength": "10",
"indFlag": "Y"
}],
"altFields": [{
"fieldName": "com_code",
"fieldDes": "公司号",
"fieldType": "Number",
"fieldLength": "10",
"indFlag": "Y"
}, {
"fieldName": "dep_code",
"fieldDes": "公司号",
"fieldType": "Number",
"fieldLength": "10",
"indFlag": "Y"
}],
"delFields": [{
"fieldName": "com_code",
"fieldDes": "公司号",
"fieldType": "Number",
"fieldLength": "10",
"indFlag": "Y"
}, {
"fieldName": "dep_code",
"fieldDes": "公司号",
"fieldType": "Number",
"fieldLength": "10",
"indFlag": "Y"
}]
}],
"remove": [
"JIU",
"tabName2"
],
"inquire1": {
"TAB_NAME": [
]
},
"inquire2": {
"TAB_NAME": [
"tabName1",
"tabName2"
]
} }
