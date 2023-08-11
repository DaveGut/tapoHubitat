/*	TP-Link SMART API / PROTOCOL DRIVER SERIES for plugs, switches, bulbs, hubs and Hub-connected devices.
		Copyright Dave Gutheinz
License:  https://github.com/DaveGut/HubitatActive/blob/master/KasaDevices/License.md
=================================================================================================*/
def type() {return "tpLink_hub_newType" }
def gitPath() { return "DaveGut/tpLink_Hubitat/main/Drivers/" }
def driverVer() { return parent.driverVer() }

metadata {
	definition (name: "tpLink_hub_newType", namespace: "davegut", author: "Dave Gutheinz", 
				importUrl: "https://raw.githubusercontent.com/${gitPath()}${type()}.groovy")
	{
		capability "Contact Sensor"
	}
}

def installed() { 
	updateAttr("commsError", "OK")
	runIn(1, updated)
}

def updated() {
	unschedule()
	def logData = [:]
	logData << setLogsOff()
	logData << [status: "OK"]
	if (logData.status == "ERROR") {
		logError("updated: ${logData}")
	} else {
		logInfo("updated: ${logData}")
	}
}

//	Parse Methods
def devicePollParse(childData, data=null) {
}

def parseTriggerLog(resp, data) {
}

//	Library Inclusion



// ~~~~~ start include (1338) davegut.lib_tpLink_sensors ~~~~~
library ( // library marker davegut.lib_tpLink_sensors, line 1
	name: "lib_tpLink_sensors", // library marker davegut.lib_tpLink_sensors, line 2
	namespace: "davegut", // library marker davegut.lib_tpLink_sensors, line 3
	author: "Dave Gutheinz", // library marker davegut.lib_tpLink_sensors, line 4
	description: "Common Tapo Sensor Methods", // library marker davegut.lib_tpLink_sensors, line 5
	category: "utilities", // library marker davegut.lib_tpLink_sensors, line 6
	documentationLink: "" // library marker davegut.lib_tpLink_sensors, line 7
) // library marker davegut.lib_tpLink_sensors, line 8

def getTriggerLog() { // library marker davegut.lib_tpLink_sensors, line 10
	Map cmdBody = [ // library marker davegut.lib_tpLink_sensors, line 11
		method: "control_child", // library marker davegut.lib_tpLink_sensors, line 12
		params: [ // library marker davegut.lib_tpLink_sensors, line 13
			device_id: getDataValue("deviceId"), // library marker davegut.lib_tpLink_sensors, line 14
			requestData: [ // library marker davegut.lib_tpLink_sensors, line 15
				method: "get_trigger_logs", // library marker davegut.lib_tpLink_sensors, line 16
				params: [page_size: 5,"start_id": 0] // library marker davegut.lib_tpLink_sensors, line 17
			] // library marker davegut.lib_tpLink_sensors, line 18
		] // library marker davegut.lib_tpLink_sensors, line 19
	] // library marker davegut.lib_tpLink_sensors, line 20
	parent.asyncPassthrough(cmdBody, device.getDeviceNetworkId(), "distTriggerLog") // library marker davegut.lib_tpLink_sensors, line 21
} // library marker davegut.lib_tpLink_sensors, line 22

command "getDeveloperData" // library marker davegut.lib_tpLink_sensors, line 24
def getDeveloperData() { // library marker davegut.lib_tpLink_sensors, line 25
	def attrData = device.getCurrentStates() // library marker davegut.lib_tpLink_sensors, line 26
	Map attrs = [:] // library marker davegut.lib_tpLink_sensors, line 27
	attrData.each { // library marker davegut.lib_tpLink_sensors, line 28
		attrs << ["${it.name}": it.value] // library marker davegut.lib_tpLink_sensors, line 29
	} // library marker davegut.lib_tpLink_sensors, line 30
	Date date = new Date() // library marker davegut.lib_tpLink_sensors, line 31
	Map devData = [ // library marker davegut.lib_tpLink_sensors, line 32
		currentTime: date.toString(), // library marker davegut.lib_tpLink_sensors, line 33
		name: device.getName(), // library marker davegut.lib_tpLink_sensors, line 34
		status: device.getStatus(), // library marker davegut.lib_tpLink_sensors, line 35
		dataValues: device.getData(), // library marker davegut.lib_tpLink_sensors, line 36
		attributes: attrs, // library marker davegut.lib_tpLink_sensors, line 37
		devInfo: getDeviceInfo(), // library marker davegut.lib_tpLink_sensors, line 38
		compList: getDeviceComponents() // library marker davegut.lib_tpLink_sensors, line 39
	] // library marker davegut.lib_tpLink_sensors, line 40
	logWarn("DEVELOPER DATA: ${devData}") // library marker davegut.lib_tpLink_sensors, line 41
} // library marker davegut.lib_tpLink_sensors, line 42

def getDeviceComponents() { // library marker davegut.lib_tpLink_sensors, line 44
	Map logData = [:] // library marker davegut.lib_tpLink_sensors, line 45
	Map cmdBody = [ // library marker davegut.lib_tpLink_sensors, line 46
		device_id: getDataValue("deviceId"), // library marker davegut.lib_tpLink_sensors, line 47
		method: "get_child_device_component_list" // library marker davegut.lib_tpLink_sensors, line 48
	] // library marker davegut.lib_tpLink_sensors, line 49
	def compList = parent.syncPassthrough(cmdBody) // library marker davegut.lib_tpLink_sensors, line 50
	if (compList == "ERROR") { // library marker davegut.lib_tpLink_sensors, line 51
		logWarn("getDeviceComponents: [ERROR: Error in Sysn Comms]") // library marker davegut.lib_tpLink_sensors, line 52
	} // library marker davegut.lib_tpLink_sensors, line 53
	return compList // library marker davegut.lib_tpLink_sensors, line 54
} // library marker davegut.lib_tpLink_sensors, line 55

def getDeviceInfo() { // library marker davegut.lib_tpLink_sensors, line 57
	logDebug("getChildDeviceInfo") // library marker davegut.lib_tpLink_sensors, line 58
	Map cmdBody = [ // library marker davegut.lib_tpLink_sensors, line 59
		method: "control_child", // library marker davegut.lib_tpLink_sensors, line 60
		params: [ // library marker davegut.lib_tpLink_sensors, line 61
			device_id: getDataValue("deviceId"), // library marker davegut.lib_tpLink_sensors, line 62
			requestData: [ // library marker davegut.lib_tpLink_sensors, line 63
				method: "get_device_info" // library marker davegut.lib_tpLink_sensors, line 64
			] // library marker davegut.lib_tpLink_sensors, line 65
		] // library marker davegut.lib_tpLink_sensors, line 66
	] // library marker davegut.lib_tpLink_sensors, line 67
	def devInfo = parent.syncPassthrough(cmdBody) // library marker davegut.lib_tpLink_sensors, line 68
	if (devInfo == "ERROR") { // library marker davegut.lib_tpLink_sensors, line 69
		logWarn("getDeviceInfo: [ERROR: Error in Sysn Comms]") // library marker davegut.lib_tpLink_sensors, line 70
	} // library marker davegut.lib_tpLink_sensors, line 71
	return devInfo // library marker davegut.lib_tpLink_sensors, line 72
} // library marker davegut.lib_tpLink_sensors, line 73

def updateAttr(attr, value) { // library marker davegut.lib_tpLink_sensors, line 75
	if (device.currentValue(attr) != value) { // library marker davegut.lib_tpLink_sensors, line 76
		sendEvent(name: attr, value: value) // library marker davegut.lib_tpLink_sensors, line 77
	} // library marker davegut.lib_tpLink_sensors, line 78
} // library marker davegut.lib_tpLink_sensors, line 79

/*	Currently disabled.  Future. // library marker davegut.lib_tpLink_sensors, line 81
attribute "lowBattery", "string" // library marker davegut.lib_tpLink_sensors, line 82
attribute "status", "string" // library marker davegut.lib_tpLink_sensors, line 83
def deviceRefreshParse(childData, data=null) { // library marker davegut.lib_tpLink_sensors, line 84
	try { // library marker davegut.lib_tpLink_sensors, line 85
		def devData = childData.find {it.mac = device.getDeviceNetworkId()} // library marker davegut.lib_tpLink_sensors, line 86
		logDebug("deviceInfoParse: ${devData}") // library marker davegut.lib_tpLink_sensors, line 87
		updateAttr("lowBattery", devData.atLowBattery) // library marker davegut.lib_tpLink_sensors, line 88
		updateAttr("status", status) // library marker davegut.lib_tpLink_sensors, line 89
	} catch (error) { // library marker davegut.lib_tpLink_sensors, line 90
		logWarn("deviceRefreshParse: Failed to capture deviceData from ChildData") // library marker davegut.lib_tpLink_sensors, line 91
	} // library marker davegut.lib_tpLink_sensors, line 92
} // library marker davegut.lib_tpLink_sensors, line 93
def setReportInterval() { // library marker davegut.lib_tpLink_sensors, line 94
	def repInt = sensorReportInt.toInteger() // library marker davegut.lib_tpLink_sensors, line 95
	Map cmdBody = [ // library marker davegut.lib_tpLink_sensors, line 96
		method: "control_child", // library marker davegut.lib_tpLink_sensors, line 97
		params: [ // library marker davegut.lib_tpLink_sensors, line 98
			device_id: getDataValue("deviceId"), // library marker davegut.lib_tpLink_sensors, line 99
			requestData: [ // library marker davegut.lib_tpLink_sensors, line 100
				method: "multipleRequest", // library marker davegut.lib_tpLink_sensors, line 101
				params: [ // library marker davegut.lib_tpLink_sensors, line 102
					requests: [ // library marker davegut.lib_tpLink_sensors, line 103
						[method: "set_device_info", // library marker davegut.lib_tpLink_sensors, line 104
						 params: [report_interval: repInt]], // library marker davegut.lib_tpLink_sensors, line 105
						[method: "get_device_info"] // library marker davegut.lib_tpLink_sensors, line 106
					]]]]] // library marker davegut.lib_tpLink_sensors, line 107
	def devInfo = parent.syncPassthrough(cmdBody) // library marker davegut.lib_tpLink_sensors, line 108
	devInfo = devInfo.result.responseData.result.responses.find{it.method == "get_device_info"}.result // library marker davegut.lib_tpLink_sensors, line 109
	updateAttr("reportInterval", devInfo.report_interval) // library marker davegut.lib_tpLink_sensors, line 110
	return buttonReportInt // library marker davegut.lib_tpLink_sensors, line 111
} // library marker davegut.lib_tpLink_sensors, line 112
*/ // library marker davegut.lib_tpLink_sensors, line 113

// ~~~~~ end include (1338) davegut.lib_tpLink_sensors ~~~~~

// ~~~~~ start include (1339) davegut.Logging ~~~~~
library ( // library marker davegut.Logging, line 1
	name: "Logging", // library marker davegut.Logging, line 2
	namespace: "davegut", // library marker davegut.Logging, line 3
	author: "Dave Gutheinz", // library marker davegut.Logging, line 4
	description: "Common Logging and info gathering Methods", // library marker davegut.Logging, line 5
	category: "utilities", // library marker davegut.Logging, line 6
	documentationLink: "" // library marker davegut.Logging, line 7
) // library marker davegut.Logging, line 8

preferences { // library marker davegut.Logging, line 10
	input ("logEnable", "bool",  title: "Enable debug logging for 30 minutes", defaultValue: false) // library marker davegut.Logging, line 11
	input ("infoLog", "bool", title: "Enable information logging",defaultValue: true) // library marker davegut.Logging, line 12
	input ("traceLog", "bool", title: "Enable trace logging as directed by developer", defaultValue: false) // library marker davegut.Logging, line 13
} // library marker davegut.Logging, line 14

def listAttributes() { // library marker davegut.Logging, line 16
	def attrData = device.getCurrentStates() // library marker davegut.Logging, line 17
	Map attrs = [:] // library marker davegut.Logging, line 18
	attrData.each { // library marker davegut.Logging, line 19
		attrs << ["${it.name}": it.value] // library marker davegut.Logging, line 20
	} // library marker davegut.Logging, line 21
	return attrs // library marker davegut.Logging, line 22
} // library marker davegut.Logging, line 23

def setLogsOff() { // library marker davegut.Logging, line 25
	def logData = [logEnagle: logEnable, infoLog: infoLog, traceLog:traceLog] // library marker davegut.Logging, line 26
	if (logEnable) { // library marker davegut.Logging, line 27
		runIn(1800, debugLogOff) // library marker davegut.Logging, line 28
		logData << [debugLogOff: "scheduled"] // library marker davegut.Logging, line 29
	} // library marker davegut.Logging, line 30
	if (traceLog) { // library marker davegut.Logging, line 31
		runIn(1800, traceLogOff) // library marker davegut.Logging, line 32
		logData << [traceLogOff: "scheduled"] // library marker davegut.Logging, line 33
	} // library marker davegut.Logging, line 34
	return logData // library marker davegut.Logging, line 35
} // library marker davegut.Logging, line 36

def logTrace(msg){ // library marker davegut.Logging, line 38
	if (traceLog == true) { // library marker davegut.Logging, line 39
		log.trace "${device.displayName}-${driverVer()}: ${msg}" // library marker davegut.Logging, line 40
	} // library marker davegut.Logging, line 41
} // library marker davegut.Logging, line 42

def traceLogOff() { // library marker davegut.Logging, line 44
	device.updateSetting("traceLog", [type:"bool", value: false]) // library marker davegut.Logging, line 45
	logInfo("traceLogOff") // library marker davegut.Logging, line 46
} // library marker davegut.Logging, line 47

def logInfo(msg) {  // library marker davegut.Logging, line 49
	if (textEnable || infoLog) { // library marker davegut.Logging, line 50
		log.info "${device.displayName}-${driverVer()}: ${msg}" // library marker davegut.Logging, line 51
	} // library marker davegut.Logging, line 52
} // library marker davegut.Logging, line 53

def debugLogOff() { // library marker davegut.Logging, line 55
	device.updateSetting("logEnable", [type:"bool", value: false]) // library marker davegut.Logging, line 56
	logInfo("debugLogOff") // library marker davegut.Logging, line 57
} // library marker davegut.Logging, line 58

def logDebug(msg) { // library marker davegut.Logging, line 60
	if (logEnable || debugLog) { // library marker davegut.Logging, line 61
		log.debug "${device.displayName}-${driverVer()}: ${msg}" // library marker davegut.Logging, line 62
	} // library marker davegut.Logging, line 63
} // library marker davegut.Logging, line 64

def logWarn(msg) { log.warn "${device.displayName}-${driverVer()}: ${msg}" } // library marker davegut.Logging, line 66

def logError(msg) { log.error "${device.displayName}-${driverVer()}: ${msg}" } // library marker davegut.Logging, line 68

// ~~~~~ end include (1339) davegut.Logging ~~~~~
