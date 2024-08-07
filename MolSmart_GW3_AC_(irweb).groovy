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
 *            --- Driver para GW3 - IR - para TV ---
 *              V.1.0   5/8/2024 - V1 para trazer os controles remotos prontos. 
 *
 *
 */
metadata {
  definition (name: "MolSmart - GW3 - AC (irweb)", namespace: "TRATO", author: "VH", vid: "generic-contact") {
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

  attribute "supportedThermostatFanModes", "JSON_OBJECT"
	attribute "supportedThermostatModes", "JSON_OBJECT"

  command "setSupportedThermostatFanModes", ["JSON_OBJECT"]
	command "setSupportedThermostatModes", ["JSON_OBJECT"]
	command "setTemperature", ["NUMBER"]
      
  command "GetRemoteDATA"
  command "cleanvars"   //command to clean all variables and states. For debug only. 
  }
      
      


}

    import groovy.transform.Field
    @Field static final String DRIVER = "by TRATO"
    @Field static final String USER_GUIDE = "https://github.com/hhorigian/hubitat_MolSmart_GW3_IR/tree/main/TV"

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
    input name: "repeatSendHEX", title:"Repeat for SendHex", type: "string", defaultValue: "1"   // REPEAT SEND PRONTO HEX
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
state.fan = resp.data.functions.function[27]
state.temp17 = resp.data.functions.function[28]
state.temp23 = resp.data.functions.function[29]
state.temp26 = resp.data.functions.function[30]
state.onoff = resp.data.functions.function[31]
state.temp19 = resp.data.functions.function[32]
state.temp21 = resp.data.functions.function[33]
state.swing = resp.data.functions.function[34]
state.manual = resp.data.functions.function[35]
state.operation mode = resp.data.functions.function[35]
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
    sendEvent(name: "thermostatMode", value: "on", descriptionText: "Thermostat Mode set to on", isStateChange: true)
    sendEvent(name: "switch", value: "on", isStateChange: true)

	def ircode =  state.poweron   
     log.info "ircode = " + ircode
     EnviaComando(ircode)

}

def off() {
    sendEvent(name: "thermostatMode", value: "off", descriptionText: "Thermostat Mode set to off", isStateChange: true)
    sendEvent(name: "switch", value: "off", isStateChange: true)

	def ircode =  state.poweroff    
     EnviaComando(ircode)
         
}




//Case para os botões se usar no Dashboard 
def push(pushed) {
	logDebug("push: button = ${pushed}")
	if (pushed == null) {
		logWarn("push: pushed is null.  Input ignored")
		return
	}
	pushed = pushed.toInteger()
	switch(pushed) {
		case 0 : poweroff(); break
		case 1 : poweron(); break
		case 2 : auto(); break
		case 3 : heat(); break
		case 4 : cool(); break
    case 5 : fan(); break
    case 6 : dry(); break
    case 7 : setautocool(); break                
    case 8 : comandoextra1(); break    
    case 9 : comandoextra2(); break            
    case 10 : comandoextra3(); break            
    case 11 : comandoextra4(); break    
    case 12 : comandoextra5(); break    
    case 13 : fanAuto(); break    
    case 14 : fanLow(); break    
    case 15 : fanMed(); break    
    case 16 : fanHigh(); break   
    case 17 : comandoextra6(); break  
    case 18 : comandoextra7(); break  
    case 19 : comandoextra8(); break   
		case 20 : fastcold(); break
		case 21 : temp18(); break
		case 22 : temp20(); break
		case 23 : temp22(); break
		case 24 : clock(); break
		case 25 : sweep(); break
		case 26 : turbo(); break
		case 27 : fan(); break
		case 28 : temp17(); break
		case 29 : temp23(); break
		case 30 : temp26(); break
		case 31 : onoff(); break
		case 32 : temp19(); break
		case 33 : temp21(); break
		case 34 : swing(); break
		case 35 : manual(); break
		case 36 : mode(); break
		case 37 : up(); break
		case 38 : timer(); break
		case 39 : cancel(); break
		case 40 : down(); break
		case 41 : display(); break
		case 42 : io(); break
		case 43 : tempup(); break
		case 44 : tempdown(); break
		case 45 : fanspeed(); break
		
		default:
			logDebug("push: Botão inválido.")
			break
	}
}



//Botão #0 para dashboard
def poweroff(){
	sendEvent(name: "power", value: "off")
    def ircode =  state.OFFIRsend
    EnviaComando(ircode)    
}

//Botão #1 para dashboard
def poweron(){
	sendEvent(name: "power", value: "on")
    def ircode =  state.OnIRsend
    EnviaComando(ircode)    
}

//Botão #2 para dashboard
def mute(){
	sendEvent(name: "action", value: "mute")
    def ircode =  state.muteIRsend
    EnviaComando(ircode)    
}


//Botão #3 para dashboard
def source(){
	sendEvent(name: "action", value: "source")
    def ircode =  state.sourceIRsend
    EnviaComando(ircode)    
}

//Botão #4 para dashboard
def back(){
	sendEvent(name: "action", value: "back")
    def ircode = state.backIRsend
    EnviaComando(ircode)    
}

//Botão #5 para dashboard
def menu(){
	sendEvent(name: "action", value: "menu")
    def ircode =  state.menuIRsend
    EnviaComando(ircode)    
}


//Botão #6 para dashboard
def hdmi1(){
    sendEvent(name: "input", value: "hdmi1")
    def ircode =   state.hdmi1IRsend
    EnviaComando(ircode)
}

//Botão #7 para dashboard
def hdmi2(){
    sendEvent(name: "input", value: "hdmi2")
    def ircode =  state.hdmi2IRsend
    EnviaComando(ircode)
}



//Botão #8 para dashboard
def left(){
    sendEvent(name: "action", value: "left")
    def ircode =   state.leftIRsend
    EnviaComando(ircode)
}

//Botão #9 para dashboard
def right(){
    sendEvent(name: "action", value: "right")
     def ircode =  state.rightIRsend
    EnviaComando(ircode)
}



//Botão #10 para dashboard
def up(){
    sendEvent(name: "action", value: "up")
    def ircode =  state.upIRsend
    EnviaComando(ircode)
}

//Botão #11 para dashboard
def down(){
    sendEvent(name: "action", value: "down")
    def ircode =  state.downIRsend
    EnviaComando(ircode)
}

//Botão #12 para dashboard
def confirm(){
    sendEvent(name: "action", value: "confirm")
    def ircode =  state.confirmIRsend
    EnviaComando(ircode)
}


//Botão #13 para dashboard
def exit(){
	sendEvent(name: "action", value: "exit")
    def ircode =  state.exitIRsend
    EnviaComando(ircode)    
}



//Botão #14 para dashboard
def home(){
    sendEvent(name: "action", value: "home")
    def ircode =  state.homeIRsend
    EnviaComando(ircode)
}



//Botão #18 para dashboard
def channelUp(){
	sendEvent(name: "channel", value: "chup")
   def ircode =   state.ChanUpIRsend
    EnviaComando(ircode)    
}

//Botão #19 para dashboard
def channelDown(){
	sendEvent(name: "channel", value: "chdown")
    def ircode =  state.ChanDownIRsend
    EnviaComando(ircode)    
}

//Botão #21 para dashboard
def volumeUp(){
	sendEvent(name: "action", value: "volup")
    def ircode = state.VolUpIRsend
    EnviaComando(ircode)    
}

//Botão #22 para dashboard
def volumeDown(){
	sendEvent(name: "action", value: "voldown")
    def ircode = state.VolDownIRsend
    EnviaComando(ircode)    
}


//Botão #23 para dashboard
def num0(){
    sendEvent(name: "action", value: "num0")
    def ircode =  state.num0IRsend
    EnviaComando(ircode)
}

//Botão #24 para dashboard
def num1(){
    sendEvent(name: "action", value: "num1")
   def ircode =  state.num1IRsend
    EnviaComando(ircode)
}

//Botão #25 para dashboard
def num2(){
    sendEvent(name: "action", value: "num2")
    def ircode =  state.num2IRsend
    EnviaComando(ircode)
}


//Botão #26 para dashboard
def num3(){
    sendEvent(name: "action", value: "num3")
    def ircode =  state.num3IRsend
    EnviaComando(ircode)
}

//Botão #27 para dashboard
def num4(){
    sendEvent(name: "action", value: "num4")
    def ircode =  state.num4IRsend
    EnviaComando(ircode)
}

//Botão #28 para dashboard
def num5(){
    sendEvent(name: "action", value: "num5")
    def ircode =   state.num5IRsend
    EnviaComando(ircode)
}

//Botão #29 para dashboard
def num6(){
    sendEvent(name: "action", value: "num6")
    def ircode =  state.num6IRsend
    EnviaComando(ircode)
}


//Botão #30 para dashboard
def num7(){
    sendEvent(name: "action", value: "num7")
    def ircode =  state.num7IRsend
    EnviaComando(ircode)
}

//Botão #31 para dashboard
def num8(){
    sendEvent(name: "action", value: "num8")
    def ircode =  state.num8IRsend
    EnviaComando(ircode)
}

//Botão #32 para dashboard
def num9(){
    sendEvent(name: "action", value: "num9")
    def ircode = state.num9IRsend
    EnviaComando(ircode)
}

//Botão #33 para dashboard
def btnextra1(){
    sendEvent(name: "action", value: "confirm")
    def ircode =  state.btnextra1IRsend
    EnviaComando(ircode)
}

//Botão #34 para dashboard
def btnextra2(){
    sendEvent(name: "action", value: "btnextra2")
    def ircode =  state.btnextra2IRsend
    EnviaComando(ircode)
}

//Botão #35 para dashboard
def btnextra3(){
    sendEvent(name: "action", value: "btnextra3")
    def ircode =  state.btnextra3IRsend
    EnviaComando(ircode)
}

//Botão #36 para dashboard
def mode(){
    sendEvent(name: "action", value: "mode")
    def ircode =  state.mode
    EnviaComando(ircode)
}

//Botão #38 para dashboard
def appAmazonPrime(){
    sendEvent(name: "input", value: "amazon")
    def ircode =   state.amazonIRsend
    EnviaComando(ircode)
}

//Botão #39 para dashboard
def appyoutube(){
    sendEvent(name: "input", value: "youtube")
   def ircode =  state.youtubeIRsend
    EnviaComando(ircode)
}


//Botão #40 para dashboard
def appnetflix(){
    sendEvent(name: "input", value: "netflix")
    def ircode =  state.netflixIRsend
    EnviaComando(ircode)
}

//Botão #41 para dashboard
def btnextra4(){
    sendEvent(name: "action", value: "btnextra4")
    def ircode =  state.btnextra4IRsend
    EnviaComando(ircode)
}

//Botão #40 para dashboard
def btnextra5(){
    sendEvent(name: "action", value: "btnextra5")
    def ircode =  state.btnextra5IRsend
    EnviaComando(ircode)
}


//Botão #40 para dashboard
def btnextra6(){
    sendEvent(name: "action", value: "btnextra6")
    def ircode =  state.btnextra6IRsend
    EnviaComando(ircode)
}

//Botão #44 para dashboard
def btnextra7(){
    sendEvent(name: "action", value: "btnextra7")
    def ircode =  state.btnextra7IRsend
    EnviaComando(ircode)
}

//Botão #45 para dashboard
def btnAIRsend(){
    sendEvent(name: "action", value: "btnAIRsend")
    def ircode =  state.btnAIRsend
    EnviaComando(ircode)
}

//Botão #46 para dashboard
def btnBIRsend(){
    sendEvent(name: "action", value: "btnBIRsend")
    def ircode =  state.btnBIRsend
    EnviaComando(ircode)
}

//Botão #47 para dashboard
def btnCIRsend(){
    sendEvent(name: "action", value: "btnCIRsend")
    def ircode =  state.btnCIRsend
    EnviaComando(ircode)
}

//Botão #48 para dashboard
def btnDIRsend(){
    sendEvent(name: "action", value: "btnDIRsend")
    def ircode =  state.btnDIRsend
    EnviaComando(ircode)
}

//Botão #49 para dashboard
def playIRsend(){
    sendEvent(name: "action", value: "playIRsend")
    def ircode =  state.playIRsend
    EnviaComando(ircode)
}

//Botão #50 para dashboard
def pauseIRsend(){
    sendEvent(name: "action", value: "pauseIRsend")
    def ircode =  state.pauseIRsend
    EnviaComando(ircode)
}

//Botão #51 para dashboard
def nextIRsend(){
    sendEvent(name: "action", value: "nextIRsend")
    def ircode =  state.nextIRsend
    EnviaComando(ircode)
}

//Botão #52 para dashboard
def guideIRsend(){
    sendEvent(name: "action", value: "guideIRsend")
    def ircode =  state.guideIRsend
    EnviaComando(ircode)
}

//Botão #53 para dashboard
def infoIRsend(){
    sendEvent(name: "action", value: "infoIRsend")
    def ircode =  state.infoIRsend
    EnviaComando(ircode)
}

//Botão #54 para dashboard
def toolsIRsend(){
    sendEvent(name: "action", value: "toolsIRsend")
    def ircode =  state.toolsIRsend
    EnviaComando(ircode)
}

//Botão #55 para dashboard
def smarthubIRsend(){
    sendEvent(name: "action", value: "smarthubIRsend")
    def ircode =  state.smarthubIRsend
    EnviaComando(ircode)
}

//Botão #56 para dashboard
def previouschannelIRsend(){
    sendEvent(name: "action", value: "previouschannelIRsend")
    def ircode =  state.previouschannelIRsend
    EnviaComando(ircode)
}

//Botão #57 para dashboard
def backIRsend(){
    sendEvent(name: "action", value: "backIRsend")
    def ircode =  state.backIRsend
    EnviaComando(ircode)
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


//DEBUG
private logDebug(msg) {
  if (settings?.debugOutput || settings?.debugOutput == null) {
    log.debug "$msg"
  }
}
