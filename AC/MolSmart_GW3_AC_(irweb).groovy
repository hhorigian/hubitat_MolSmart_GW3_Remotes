/**
 *   MolSmart GW3 Driver - Versão Usando IR MolSmart Database. Versão para controles de AC. 
 *   You must create your remote control template, at http://ir.molsmart.com.br. Then you can import your remote control over by using just the sharing URL. 
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
 *            --- Driver para GW3 - IR - para AC ---
 *              V.1.0   17/09/2024 - V1 para trazer os controles remotos prontos. 
  *             V.1.1   17/10/2024 - Fixed Links for AC
  *             V.1.2   04/11/2024 - Added setCoolingSetpoint, setHeatingSetpoint for API commands
 *
 *
 */
metadata {
  definition (name: "MolSmart - GW3 - AC (irweb)", namespace: "TRATO", author: "VH", vid: "generic-contact") {
  // capability "Switch"
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

  attribute "supportedThermostatFanModes", "JSON_OBJECT"
	attribute "supportedThermostatModes", "JSON_OBJECT"

  command "setSupportedThermostatFanModes", ["JSON_OBJECT"]
	command "setSupportedThermostatModes", ["JSON_OBJECT"]
	command "setTemperature", ["NUMBER"]
      
  command "GetRemoteDATA"
  command "cleanvars"   //command to clean all variables and states. For debug only. 

command "poweroff"
command "poweron"
command "auto"
command "heat"
command "cool"
command "fan"
command "dry"
command "setautocool"
command "comandoextra1"
command "comandoextra2"
command "comandoextra3"
command "comandoextra4"
command "comandoextra5"
command "fanAuto"
command "fanLow"
command "fanMed"
command "fanHigh"   
command "comandoextra6"  
command "comandoextra7"  
command "comandoextra8"   
command "fastcold"
command "temp18"
command "temp20"
command "temp22"
command "clock"
command "sweep"
command "turbo"
command "fan"
command "temp17"
command "temp23"
command "temp26"
command "onoff"
command "temp19"
command "temp21"
command "swing"
command "manual"
command "mode"
command "up"
command "timer"
command "cancel"
command "down"
command "display"
command "io"
command "tempup"
command "tempdown"
command "fanspeed"	  

	  
  }

	
}

    import groovy.transform.Field
    @Field static final String DRIVER = "by TRATO"
    @Field static final String USER_GUIDE = "https://github.com/hhorigian/hubitat_MolSmart_GW3_IR/tree/main/AC"

    String fmtHelpInfo(String str) {
    String prefLink = "<a href='${USER_GUIDE}' target='_blank'>${str}<br><div style='font-size: 70%;'>${DRIVER}</div></a>"
    return "<div style='font-size: 160%; font-style: bold; padding: 2px 0px; text-align: center;'>${prefLink}</div>"
    }


    @Field static final String DRIVER1 = "IR MolSmart"
    @Field static final String USER_GUIDE1 = "https://ir.molsmart.com.br/"

    String fmtHelpInfo1(String str) {
    String prefLink1 = "<a href='${USER_GUIDE1}' target='_blank'>${str}<br><div style='font-size: 70%;'>${DRIVER1}</div></a>"
    return "<div style='font-size: 160%; font-style: bold; padding: 2px 0px; text-align: center;'>${prefLink1}</div>"
    }



  preferences {
    input name: "logEnable", type: "bool", title: "Enable debug logging", defaultValue: false
    input name: "molIPAddress", type: "text", title: "MolSmart GW3 IP Address", submitOnChange: true, required: true, defaultValue: "192.168.1.100" 
    input name: "serialNum", title:"Numero de serie (Etiqueta GW3)", type: "string", required: true
	  input name: "verifyCode", title:"Verify code (Etiqueta GW3)", type: "string", required: true
	  input name: "channel", title:"Canal Infravermelho (1/2 ou 3)", type: "string", required: true        
    //input name: "repeatSendHEX", title:"Repeat for SendHex", type: "string", defaultValue: "1"   // REPEAT SEND PRONTO HEX
     input name: "setCoolingSetpointIRsend", title:"setCooling-HEX(7)", type: "string"
	  input name: "setHeatingSetpointIRsend", title:"setHeating-HEX(8)", type: "string"      
	  
        //help guide
        input name: "UserGuide", type: "hidden", title: fmtHelpInfo("Manual do Driver") 
        input name: "SiteIR", type: "hidden", title: fmtHelpInfo1("Site IR MolSmart") 
        input name: "webserviceurl", title:"URL Do Controle Remoto", type: "string"

  }   
  

def initialized()
{
    state.currentip = ""  
    log.debug "initialized()"
    
}


def GetRemoteDATA()
{
  
    def params = [
        uri: webserviceurl,
        contentType: "application/json"
    ]
    try {
        httpGet(params) { resp ->
            if (resp.success) {                
                sendEvent(name: "GetRemoteData", value: "Sucess")
                //log.debug "RESULT = " + resp.data
      
    sendEvent(name: "Controle", value: resp.data.name)   
    sendEvent(name: "TipoControle", value: resp.data.type)   
    sendEvent(name: "Formato", value: resp.data.conversor)  

    state.encoding = resp.data.conversor
			 		
state.poweroff = resp.data.functions.function[0]
state.poweron = resp.data.functions.function[1]
state.auto = resp.data.functions.function[2]
state.heat = resp.data.functions.function[3]
state.cool = resp.data.functions.function[4]
state.fan = resp.data.functions.function[5]
state.dry = resp.data.functions.function[6]
state.setautocool = resp.data.functions.function[7]
state.fanauto = resp.data.functions.function[8]
state.fanLow = resp.data.functions.function[9]
state.fanmed = resp.data.functions.function[10]
state.fanhigh = resp.data.functions.function[11]
state.comandoextra1 = resp.data.functions.function[12]
state.comandoextra2 = resp.data.functions.function[13]
state.comandoextra3 = resp.data.functions.function[14]
state.comandoextra4 = resp.data.functions.function[15]
state.comandoextra5 = resp.data.functions.function[16]
state.comandoextra6 = resp.data.functions.function[17]
state.comandoextra7 = resp.data.functions.function[18]
state.comandoextra8 = resp.data.functions.function[19]
state.fastcold = resp.data.functions.function[20]
state.temp18 = resp.data.functions.function[21]
state.temp20 = resp.data.functions.function[22]
state.temp22 = resp.data.functions.function[23]
state.clock = resp.data.functions.function[24]
state.sweep = resp.data.functions.function[25]
state.turbo = resp.data.functions.function[26]
state.fan2 = resp.data.functions.function[27]
state.temp17 = resp.data.functions.function[28]
state.temp23 = resp.data.functions.function[29]
state.temp26 = resp.data.functions.function[30]
state.onoff = resp.data.functions.function[31]
state.temp19 = resp.data.functions.function[32]
state.temp21 = resp.data.functions.function[33]
state.swing = resp.data.functions.function[34]
state.manual = resp.data.functions.function[35]
state.operationmode = resp.data.functions.function[36]
state.up = resp.data.functions.function[37]
state.timer = resp.data.functions.function[38]
state.cancel = resp.data.functions.function[39]
state.down = resp.data.functions.function[40]
state.display = resp.data.functions.function[41]
state.io = resp.data.functions.function[42]
state.tempup = resp.data.functions.function[43]
state.tempdown = resp.data.functions.function[44]
state.fanspeed = resp.data.functions.function[45]
        

    
    }
            
	}
    } catch (Exception e) {
        log.warn "Get Remote Control Info failed: ${e.message}"
    }    

}



def cleanvars()  //Usada para limpar todos os states e controles aprendidos. 
{
//state.remove()
  state.clear() 
  AtualizaDadosGW3()  
}

def installed()
{
    log.debug "installed()"
	off()
    setdefaults()  

}


def updated()
{  
    sendEvent(name:"numberOfButtons", value:60)  
	log.debug "updated()"
    setdefaults()
    off()
    AtualizaDadosGW3()   
}


//Get Device info and set as state to use during driver.
def AtualizaDadosGW3() {
    state.currentip = settings.molIPAddress
    state.serialNum = settings.serialNum
    state.verifyCode = settings.verifyCode
    state.channel = settings.channel
    log.info "Dados do GW3 atualizados: " + state.currentip + " -- " + state.serialNum + " -- " +  state.verifyCode + " -- " + state.channel 

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
    /* sendEvent(name: "thermostatMode", value: "on", descriptionText: "Thermostat Mode set to on", isStateChange: true)
    sendEvent(name: "switch", value: "on", isStateChange: true)

	def ircode =  state.poweron   
     log.info "ircode = " + ircode
     EnviaComando(ircode)
    
    */  
    
    poweron()
      

}

def off() {
    /* sendEvent(name: "thermostatMode", value: "off", descriptionText: "Thermostat Mode set to off", isStateChange: true)
    sendEvent(name: "switch", value: "off", isStateChange: true)

	def ircode =  state.poweroff    
     EnviaComando(ircode)
   */  
    
    poweroff()
         
}


//Case para os botões se usar no Dashboard 
def push(pushed) {
	logDebug("push: button = ${pushed}")
	if (pushed == null) {
		logWarn("push: pushed is null.  Input ignored")
		return
	}
	//pushed = pushed.toInteger()
	switch(pushed) {
	case "0" : poweroff(); break
	case "1" : poweron(); break
	case "2" : auto(); break
	case "3" : heat(); break
	case "4" : cool(); break
    case "5" : fan(); break
    case "6" : dry(); break
    case "7" : setautocool(); break                
    case "8" : comandoextra1(); break    
    case "9" : comandoextra2(); break            
    case "10" : comandoextra3(); break            
    case "11" : comandoextra4(); break    
    case "12" : comandoextra5(); break    
    case "13" : fanAuto(); break    
    case "14" : fanLow(); break    
    case "15" : fanMed(); break    
    case "16" : fanHigh(); break   
    case "17" : comandoextra6(); break  
    case "18" : comandoextra7(); break  
    case "19" : comandoextra8(); break   
	case "20" : fastcold(); break
	case "21" : temp18(); break
	case "22" : temp20(); break
	case "23" : temp22(); break
	case "24" : clock(); break
    case "25" : sweep(); break
    case "26" : turbo(); break
    case "27" : fan(); break
    case "28" : temp17(); break
    case "29" : temp23(); break
    case "30" : temp26(); break
    case "31" : onoff(); break
    case "32" : temp19(); break
    case "33" : temp21(); break
    case "34" : swing(); break
    case "35" : manual(); break
    case "36" : mode(); break
    case "37" : up(); break
    case "38" : timer(); break
    case "39" : cancel(); break
    case "40" : down(); break
    case "41" : display(); break
    case "42" : io(); break
    case "43" : tempup(); break
    case "44" : tempdown(); break
    case "45" : fanspeed(); break
		
		default:
           "${pushed}"()
			//logDebug("push: Botão inválido.")
			break
	}
}


//Botão #0 para dashboard
def poweroff(){
    sendEvent(name: "thermostatMode", value: "off", descriptionText: "Thermostat Mode set to off", isStateChange: true)
    def ircode =  state.poweroff
    EnviaComando(ircode)    

}

//Botão #1 para dashboard
def poweron(){
    sendEvent(name: "thermostatMode", value: "on", descriptionText: "Thermostat Mode set to on", isStateChange: true)
    def ircode =  state.poweron
    EnviaComando(ircode)    
}

//Botão #2 para dashboard
def auto(){
    sendEvent(name: "thermostatMode", value: "auto")
    def ircode =  state.muteIRsend
    EnviaComando(ircode)    
}


//Botão #3 para dashboard
def heat(){
    sendEvent(name: "thermostatMode", value: "heat")
    def ircode =  state.heat
    EnviaComando(ircode)    
}

//Botão #4 para dashboard
def cool(){
    sendEvent(name: "thermostatMode", value: "cool")
    def ircode = state.cool
    EnviaComando(ircode)    
}

//Botão #5 para dashboard
def fan(){
    sendEvent(name: "thermostatMode", value: "fan")
    def ircode =  state.fan
    EnviaComando(ircode)    
}


//Botão #6 para dashboard
def dry(){
    sendEvent(name: "thermostatMode", value: "dry")
    def ircode =   state.dry
    EnviaComando(ircode)
}

//Botão #7 para dashboard
def setautocool(){
    sendEvent(name: "thermostatMode", value: "setautocool")
    def ircode =  state.setautocool
    EnviaComando(ircode)
}



//Botão #8 para dashboard
def comandoextra1(){
    sendEvent(name: "action", value: "comandoextra1")
    def ircode =   state.comandoextra1
    EnviaComando(ircode)
}

//Botão #9 para dashboard
def comandoextra2(){
    sendEvent(name: "action", value: "comandoextra2")
     def ircode =  state.comandoextra2
    EnviaComando(ircode)
}



//Botão #10 para dashboard
def comandoextra3(){
    sendEvent(name: "action", value: "comandoextra3")
    def ircode =  state.comandoextra3
    EnviaComando(ircode)
}

//Botão #11 para dashboard
def comandoextra4(){
    sendEvent(name: "action", value: "comandoextra4")
    def ircode =  state.comandoextra4
    EnviaComando(ircode)
}

//Botão #12 para dashboard
def comandoextra5(){
    sendEvent(name: "action", value: "comandoextra5")
    def ircode =  state.comandoextra5
    EnviaComando(ircode)
}


//Botão #13 para dashboard
def fanAuto(){
    sendEvent(name: "setThermostatFanMode", value: "fanAuto")
    sendEvent(name: "FanMode", value: "fanAuto")
    def ircode =  state.fanAuto
    EnviaComando(ircode)    
}



//Botão #14 para dashboard
def fanLow(){
    sendEvent(name: "setThermostatFanMode", value: "fanLow")
    def ircode =  state.fanLow
    EnviaComando(ircode)
}



//Botão #18 para dashboard
def fanMed(){
    sendEvent(name: "setThermostatFanMode", value: "fanMed")
   def ircode =   state.fanMed
    EnviaComando(ircode)    
}

//Botão #19 para dashboard
def fanHigh(){
    sendEvent(name: "setThermostatFanMode", value: "fanHigh")
    def ircode =  state.fanHigh
    EnviaComando(ircode)    
}

//Botão #21 para dashboard
def comandoextra6(){
    sendEvent(name: "action", value: "comandoextra6")
    def ircode = state.comandoextra6
    EnviaComando(ircode)    
}

//Botão #22 para dashboard
def comandoextra7(){
    sendEvent(name: "action", value: "comandoextra7")
    def ircode = state.comandoextra7
    EnviaComando(ircode)    
}


//Botão #23 para dashboard
def comandoextra8(){
    sendEvent(name: "action", value: "comandoextra8")
    def ircode =  state.comandoextra8
    EnviaComando(ircode)
}

//Botão #24 para dashboard
def fastcold(){
    sendEvent(name: "action", value: "fastcold")
   def ircode =  state.fastcold
    EnviaComando(ircode)
}

//Botão #25 para dashboard
def temp18(){
    sendEvent(name: "setCoolingSetpoint", value: 18 )
    def ircode =  state.temp18
    EnviaComando(ircode)
}


//Botão #26 para dashboard
def temp20(){
    sendEvent(name: "setCoolingSetpoint", value: 20 )
    def ircode =  state.temp20
    EnviaComando(ircode)
}

//Botão #27 para dashboard
def temp22(){
    sendEvent(name: "setCoolingSetpoint", value: 22 )
    def ircode =  state.temp22
    EnviaComando(ircode)
}

//Botão #28 para dashboard
def clock(){
    sendEvent(name: "action", value: "clock")
    def ircode =   state.clock
    EnviaComando(ircode)
}

//Botão #29 para dashboard
def sweep(){
    sendEvent(name: "action", value: "sweep")
    def ircode =  state.sweep
    EnviaComando(ircode)
}


//Botão #30 para dashboard
def turbo(){
    sendEvent(name: "action", value: "turbo")
    def ircode =  state.turbo
    EnviaComando(ircode)
}

//Botão #32 para dashboard
def temp17(){
    sendEvent(name: "setCoolingSetpoint", value: 17 )
    def ircode = state.temp17
    EnviaComando(ircode)
}

//Botão #33 para dashboard
def temp23(){
    sendEvent(name: "setCoolingSetpoint", value: 23 )
    def ircode =  state.temp23
    EnviaComando(ircode)
}

//Botão #34 para dashboard
def temp26(){
    sendEvent(name: "setCoolingSetpoint", value: 26 )
    def ircode =  state.temp26
    EnviaComando(ircode)
}

//Botão #35 para dashboard
def onoff(){
    sendEvent(name: "action", value: "onoff")
    def ircode =  state.onoff
    EnviaComando(ircode)
}

//Botão #36 para dashboard
def temp19(){
    sendEvent(name: "setCoolingSetpoint", value: 19 )
    def ircode =  state.temp19
    EnviaComando(ircode)
}

//Botão #38 para dashboard
def temp21(){
    sendEvent(name: "setCoolingSetpoint", value: 21 )
    def ircode =   state.temp21
    EnviaComando(ircode)
}

//Botão #39 para dashboard
def swing(){
    sendEvent(name: "action", value: "swing")
   def ircode =  state.swing
    EnviaComando(ircode)
}


//Botão #40 para dashboard
def manual(){
    sendEvent(name: "action", value: "manual")
    def ircode =  state.manual
    EnviaComando(ircode)
}

//Botão #41 para dashboard
def mode(){
    sendEvent(name: "action", value: "btnextra4")
    def ircode =  state.mode
    EnviaComando(ircode)
}

//Botão #40 para dashboard
def up(){
    sendEvent(name: "action", value: "up")
    def ircode =  state.up
    EnviaComando(ircode)
}


//Botão #40 para dashboard
def timer(){
    sendEvent(name: "action", value: "timer")
    def ircode =  state.timer
    EnviaComando(ircode)
}

//Botão #44 para dashboard
def cancel(){
    sendEvent(name: "action", value: "cancel")
    def ircode =  state.cancel
    EnviaComando(ircode)
}

//Botão #45 para dashboard
def down(){
    sendEvent(name: "action", value: "down")
    def ircode =  state.down
    EnviaComando(ircode)
}

//Botão #46 para dashboard
def display(){
    sendEvent(name: "action", value: "display")
    def ircode =  state.display
    EnviaComando(ircode)
}

//Botão #47 para dashboard
def io(){
    sendEvent(name: "action", value: "io")
    def ircode =  state.io
    EnviaComando(ircode)
}

//Botão #48 para dashboard
def tempup(){
    sendEvent(name: "action", value: "tempup")
    def ircode =  state.tempup
    EnviaComando(ircode)
}

//Botão #49 para dashboard
def tempdown(){
    sendEvent(name: "action", value: "tempdown")
    def ircode =  state.tempdown
    EnviaComando(ircode)
}

//Botão #50 para dashboard
def fanspeed(){
    sendEvent(name: "action", value: "fanspeed")
    def ircode =  state.fanspeed
    EnviaComando(ircode)
}


def setCoolingSetpoint(temperature) {    
    if (device.currentValue("thermostatMode") == "cool") {               
        logInfo "Colocando a temp em  ${temperature}"  
        def previousTempCool = device.currentValue("coolingSetpoint")
        
        if (previousTempCool <= temperature) {
            tempup()
            sendEvent(name: "setCoolingSetpoint", value: temperature )
            logInfo "Aumentando a temp"
        } else {
            sendEvent(name: "setCoolingSetpoint", value: temperature )
            tempdown()
            logInfo "Diminuindo a temp"
        }
    } else {
        log.warn "Sem alterar a temp para  ${temperature}, porque o modo atual é ${device.currentValue("thermostatMode")}"
    }
}

def setHeatingSetpoint(temperature) {    
    if (device.currentValue("thermostatMode") == "heat") {               
        logInfo "Colocando a temp em  ${temperature}"  
        def previousTempHeat = device.currentValue("heatingSetpoint")
        
        if (previousTempHeat <= temperature) {
            tempup()
            sendEvent(name: "setHeatingSetpoint", value: temperature )
            logInfo "Aumentando a temp"
        } else {
            sendEvent(name: "setHeatingSetpoint", value: temperature )
            tempdown()
            logInfo "Diminuindo a temp"
        }
    } else {
        log.warn "Sem alterar a temp para  ${temperature}, porque o modo atual é ${device.currentValue("thermostatMode")}"
    }
}


def setThermostatFanMode(fanmode) {
    switch(fanmode) {
        case "on":
            fan()
            break
        case "circulate":
            //
            break
        case "auto":
            //
            break
        case "quiet":
            //
            break
        case "1":
            fanLow()
            break
        case "2":
            fanMed()
            break
        case "3":
            fanHigh()
            break        
        case "4":
            //
            break
        default:
            log.warn "Unknown fan mode ${fanmode}"
            break
    }
}


def convertCelciusToLocalTemp(temp) {
    return (location.temperatureScale == "F") ? ((temp * 1.8) + 32) : temp
}

def convertLocalToCelsiusTemp(temp) {
    return (location.temperatureScale == "F") ? Math.round((temp - 32) / 1.8) : temp
}



def setThermostatMode(thermostatmode) {
    switch(thermostatmode) {
        case "auto":
            auto()
            break
        case "off":
            off()
            break
        case "heat":
            heat()
            break
        case "emergency heat":
            emergencyHeat()
            break
        case "cool":
            cool()
            break
        case "fan":
            fan()
            break
        case "dry":
            dry()
            break
        default:
            log.warn "Unknown mode ${thermostatmode}"
            break
    }
}

      

def EnviaComando(command) {    
    if (state.encoding == "sendir") {   //if the remote is SendIR(Global Cache) uses one URL, if it's HEX format, uses another URL. 
        
             URI1 = "http://" + state.currentip + "/api/device/deviceDetails/smartHomeAutoHttpControl?serialNum=" + state.serialNum + "&verifyCode="  + state.verifyCode + "&c=" + state.channel + "&gc=" + command       
   
    } else {
             URI1 = "http://" + state.currentip + "/api/device/deviceDetails/smartHomeAutoHttpControl?serialNum=" + state.serialNum + "&verifyCode="  + state.verifyCode + "&pronto=" + command + "&c=" + state.channel + "&r=" + settings.repeatSendHEX        
        
    }       
    httpPOSTExec(URI1)
    log.info "HTTP " +  URI1 + " | commando IR = " + command
  
}


/* def EnviaComandoHEX(command) {
    
    def URI = "http://" + state.currentip + "/api/device/deviceDetails/smartHomeAutoHttpControl?serialNum=" + state.serialNum + "&verifyCode="  + state.verifyCode + "&pronto=" + command + "&c=" + state.channel + "&r=" + settings.repeatSendHEX        
    httpPOSTExec(URI)
    log.info "HTTP" +  URI + " | commando IR = "

    
}


def EnviaComandoSendIR(command) {
    
    def URI = "http://" + state.currentip + "/api/device/deviceDetails/smartHomeAutoHttpControl?serialNum=" + state.serialNum + "&verifyCode="  + state.verifyCode + "&c=" + state.channel + "&gc=" + command       
    httpPOSTExec(URI)
    log.info "Enviado...HTTP" +  URI + " commando = "   
    
}
*/


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

def info(msg) {
    if (logLevel == "INFO" || logLevel == "DEBUG") {
        log.info(msg)
    }
}

def logInfo(msg) {
    if (logEnable) log.info msg
}



//DEBUG
private logDebug(msg) {
  if (settings?.debugOutput || settings?.debugOutput == null) {
    log.debug "$msg"
  }
}
