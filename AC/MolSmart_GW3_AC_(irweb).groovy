/**
 *  MolSmart GW3 Driver - AC - iDoor (usando os códigos e controles do idoor)
 *
 *  Copyright 2024 VH 
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *
 *            --- Driver para GW3 - AC - idoor
 *            V.1.1 30/05/2024 - Removed the APP file. Only Driver File. 
 *            V.1.2 13/06/2024 - Added User Guide link 
  *           V.1.3 17/10/2024 - Fixed AC links to IR Web Remote Links 
 *
 */


metadata {
  definition (name: "MolSmart - GW3 - IR(idoor) - AC", namespace: "TRATO", author: "VH", vid: "generic-contact") {
    capability "Switch"
	capability "Thermostat"
	capability "Thermostat Cooling Setpoint"
	capability "Thermostat Setpoint"
	capability "Sensor"
	capability "Actuator"
	capability "Configuration"
	capability "Refresh"
	capability "HealthCheck"   
    capability "PushableButton"
    capability "FanControl"
    capability "ThermostatMode"      



		attribute "supportedThermostatFanModes", "JSON_OBJECT"
		attribute "supportedThermostatModes", "JSON_OBJECT"

       	command "setSupportedThermostatFanModes", ["JSON_OBJECT"]
		command "setSupportedThermostatModes", ["JSON_OBJECT"]
		command "setTemperature", ["NUMBER"]

  }
    
    
  }

    import groovy.transform.Field
    @Field static final String DRIVER = "by TRATO"
    @Field static final String USER_GUIDE = "https://github.com/hhorigian/hubitat_MolSmart_GW3_IR/tree/main/AC/Idoor"


    String fmtHelpInfo(String str) {
    String prefLink = "<a href='${USER_GUIDE}' target='_blank'>${str}<br><div style='font-size: 70%;'>${DRIVER}</div></a>"
    return "<div style='font-size: 160%; font-style: bold; padding: 2px 0px; text-align: center;'>${prefLink}</div>"
    }


  preferences {
        input name: "molIPAddress", type: "text", title: "MolSmart GW3 IP Address", submitOnChange: true, required: true, defaultValue: "192.168.1.100" 
    	input name: "serialNum", title:"Numero de serie (Etiqueta GW3)", type: "string", required: true
	    input name: "verifyCode", title:"Verify code (Etiqueta GW3)", type: "string", required: true
	    input name: "channel", title:"Canal Infravermelho (1/2 ou 3)", type: "string", required: true
    	input name: "cId", title:"Control ID (pego no idoor)", type: "string", required: true  
    	input name: "setCoolingSetpointIRsend", title:"setCooling-HEX(7)", type: "string"
	    input name: "setHeatingSetpointIRsend", title:"setHeating-HEX(8)", type: "string"      
        input name: "logEnable", type: "bool", title: "Enable debug logging", defaultValue: false 
        //help guide
        input name: "UserGuide", type: "hidden", title: fmtHelpInfo("Manual do Driver") 	  
  }   
  
def initialized()
{
    log.debug "initialized()"   
    off()  
}

def installed()
{
    log.debug "installed()"
	off()
    setdefaults()  

}

def updated()
{  
    log.debug "updated()"
    setdefaults()
    off()
    AtualizaDadosGW3()   
}

def AtualizaDadosGW3() {
    state.currentip = settings.molIPAddress
    state.serialNum = settings.serialNum
    state.verifyCode = settings.verifyCode
    state.channel = settings.channel
    state.cId = settings.cId
//    state.rcId = "52"
    log.info "Dados do GW3 atualizados: " + state.currentip + " -- " + state.serialNum + " -- " +  state.verifyCode + " -- " + state.channel + " -- " + state.cId 
}

def setdefaults() {
    sendEvent(name: "thermostatSetpoint", value: "20", descriptionText: "Thermostat thermostatSetpoint set to 20")
    sendEvent(name: "coolingSetpoint", value: "20", descriptionText: "Thermostat coolingSetpoint set to 20") 
    sendEvent(name: "heatingSetpoint", value: "20", descriptionText: "Thermostat heatingSetpoint set to 20")     
    sendEvent(name: "temperature", value: convertTemperatureIfNeeded(68.0,"F",1))    
    sendEvent(name: "thermostatOperatingState", value: "idle", descriptionText: "Set thermostatOperatingState to Idle")     
    sendEvent(name: "thermostatFanMode", value: "auto", descriptionText: "Set thermostatFanMode auto")     
    sendEvent(name: "setHeatingSetpoint", value: "15", descriptionText: "Set setHeatingSetpoint to 15")     
	sendEvent(name: "supportedThermostatFanModes", value: fanModes, descriptionText: "supportedThermostatFanModes set")
	sendEvent(name: "supportedThermostatModes", value: modes, descriptionText: "supportedThermostatModes set ")
}


def on() {
    sendEvent(name: "thermostatMode", value: "on", descriptionText: "Thermostat Mode set to on", isStateChange: true)
    def ircodetemp = 1
    state.pw = "1"
    def ircode = ircodetemp 
    EnviaComando(ircode)

}

def off() {
    sendEvent(name: "thermostatMode", value: "off", descriptionText: "Thermostat Mode set to off", isStateChange: true)
    def ircodetemp ="0"
    state.pw = "0"    
    def ircode = ircodetemp 
    EnviaComando(ircode)
         
}

//Case para los botones de push en el dashboard. 
def push(pushed) {
	logDebug("push: button = ${pushed}")
	if (pushed == null) {
		logWarn("push: pushed is null.  Input ignored")
		return
	}
	pushed = pushed.toInteger()
	switch(pushed) {
		case 3 : auto(); break
		case 4 : heat(); break
		case 5 : cool(); break
        case 6 : fan(); break
        case 7 : dry(); break
        case 8 : fanAuto(); break                
        case 9 : fanOn(); break                
        case 10 : fanCirculate(); break    
        case 13 : fanAuto(); break    
        case 14 : fanLow(); break    
        case 15 : fanMed(); break    
        case 16 : fanHigh(); break  
		default:
			logDebug("push: Botão inválido.")
			break
	}
}

//Botão #2 para dashboard
def auto(){
    sendEvent(name: "thermostatMode", value: "auto")
    def ircodetemp = 1
    state.pw = "1"
    def ircode = ircodetemp + "&md=0&tp=2"
    EnviaComando(ircode)  
}


//Botão #3 para dashboard
def heat(){
    sendEvent(name: "thermostatMode", value: "heat")
    def ircodetemp = 1
    state.pw = "1"
    def ircode = ircodetemp + "&md=2&tp=2"
    EnviaComando(ircode)  
}

//Botão #4 para dashboard
def cool(){
    sendEvent(name: "thermostatMode", value: "cool")
    def ircodetemp = 1
    state.pw = "1"
    def ircode = ircodetemp + "&md=1&tp=2"
    EnviaComando(ircode)     
}

//Botão #5 para dashboard
def fan(){
    sendEvent(name: "thermostatMode", value: "fan")
    def ircodetemp = 1
    state.pw = "1"
    def ircode = ircodetemp + "&md=4&tp=2"
    EnviaComando(ircode)      
}


//Botão #6 para dashboard
def dry(){
    sendEvent(name: "thermostatMode", value: "dry")
    def ircodetemp = 1
    state.pw = "1"
    def ircode = ircodetemp + "&md=3&tp=2"
    EnviaComando(ircode)  
}

//Botão #7 para dashboard
def setCoolingSetpoint(temperature){
    sendEvent(name: "setCoolingSetpoint", value: temperature )
    def ircodetemp = 1
    state.pw = "1"
    def ircode = ircodetemp + "&t=" + temperature + "&tp=1"
    EnviaComando(ircode)  
}

//Botão #8 para dashboard
def fanAuto(){
    sendEvent(name: "FanMode", value: "fanAuto")
    def ircodetemp = 1
    state.pw = "1"
    def ircode = ircodetemp + "&s=0&tp=3"
    EnviaComando(ircode)  
}

//Botão #9 para dashboard
def fanOn(){
    sendEvent(name: "FanMode", value: "fanOn")
    def ircodetemp = 1
    state.pw = "1"
    def ircode = ircodetemp + "&s=3&tp=3"
    EnviaComando(ircode)  
}

//Botão #9 para dashboard
def fanCirculate(){
    sendEvent(name: "FanMode", value: "fanCirculate")
    def ircodetemp = 1
    state.pw = "1"
    def ircode = ircodetemp + "&s=1&tp=3"
    EnviaComando(ircode)  
}

//Botão #7 para dashboard
def setHeatingSetpoint(temperature){
    sendEvent(name: "setHeatingSetpoint", value: temperature )
    def ircodetemp = 1
    state.pw = "1"
    def ircode = ircodetemp + "&t=" + temperature + "&tp=1"
    EnviaComando(ircode)  
}

//Botão #7 para dashboard
def setThermostatMode(modo){
    def varmodo = modo
    sendEvent(name: "thermostatMode", value: modo)
    def ircodetemp = 1
    state.pw = "1"
    def valormodo = " "
    switch(modo) {
		case "auto" : 
            valormodo = "0"; 
            break
		case "heat" : 
            valormodo = "2"; 
            break  
		case "cool" : 
            valormodo = "1"  ; 
            break        
        case "off"  : 
            valormodo = "-1" ; 
            break
        default: 
            logDebug("push: Botão inválido.")
            break   
    }
    def ircode = ircodetemp + "&md=" + valormodo + "&tp=2"
    EnviaComando(ircode)  
}

def setThermostatFanMode(modo){
    def varmodo = modo
    sendEvent(name: "setThermostatFanMode", value: modo)
    def ircodetemp = 1
    state.pw = "1"
    def valormodo = " "
    switch(modo) {
		case "auto" : 
            valormodo = "0"; 
            break
		case "circulate" : 
            valormodo = "1"; 
            break  
        case "on"  : 
            valormodo = "3" ; 
            break
        default: 
            logDebug("push: Botão inválido.")
            break   
    }
    def ircode = ircodetemp + "&s=" + valormodo + "&tp=3"
    EnviaComando(ircode)  
}



//Botão #14 para dashboard
def fanLow(){
    sendEvent(name: "thermostatMode", value: "fanLow")
    def ircode = ircodetemp + "&s=" + valormodo + "&tp=1"
    EnviaComando(ircode)    
}

//Botão #15 para dashboard
def fanMed(){
    sendEvent(name: "thermostatMode", value: "fanMed")
    def ircode = ircodetemp + "&s=" + valormodo + "&tp=3"
    EnviaComando(ircode)    
}

//Botão #16 para dashboard
def fanHigh(){
    sendEvent(name: "thermostatMode", value: "fanHigh")
    def ircode = ircodetemp + "&s=" + valormodo + "&tp=5"
    EnviaComando(ircode)    
}


def EnviaComando(command) {
    
    def URI = "http://" + state.currentip + "/api/device/deviceDetails/smartHomeAutoHttpControl?serialNum=" + state.serialNum + "&verifyCode="  + state.verifyCode + "&cId=" + state.cId + "&rcId=52" + "&state=1" + "&pw=" + command 
    httpPOSTExec(URI)
    log.info "HTTP" +  URI + " + commando = " + command
        
}


def httpPOSTExec(URI)
{
    
    try
    {
        getString = URI
        segundo = ""
        httpPost(getString.replaceAll(' ', '%20'),segundo,  )
        { resp ->
            if (resp.data)
            {
                    
                        log.info "Response " + resp.data 
            
            }
        }
    }
                            

    catch (Exception e)
    {
        logDebug("httpPostExec() failed: ${e.message}")
    }
    
}

private logDebug(msg) {
  if (settings?.debugOutput || settings?.debugOutput == null) {
    log.debug "$msg"
  }
}
