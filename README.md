# hubitat_MolSmart_GW3_Remotes

Driver para usar Controles Remotos criados no http://ir.molsmart.com.br. 

1. Criar o seu usuário e senha no http://ir.molsmart.com.br.
2. Criar um novo, ou importar um controle remoto compartilhado.
3. Instalar o Driver na sua Hubitat.
4. Criar um Virtual Device, do tipo "MolSmart - GW3 - TV (irweb)" / MolSmart - GW3 - SOM (irweb) / MolSmart - GW3 - AC (irweb)"
5. Inserir todos os dados do seu MolSmart GW3 (Ip, verifycode, numero de serie).
6. Voltar para o Site do Controle IR, e copiar o endereço URL do controle remoto para compartir. (ex: https://molsmart-integration.web.app/controle-integration?token=e7eca569-67a8-4176-9ebc-b6d6d84892b7ó ) - sem acentos. 

   ![image](https://github.com/user-attachments/assets/6c4847cf-3cbe-410f-85d9-822dc9a4e5e4)
7. Colar o link do controle, no input no driver na parte de preferenças.

![image](https://github.com/user-attachments/assets/669cd741-d543-4188-b394-19102c844f60)

8. Executar o comando "GetRemoteData" dentro da página do Driver e deveria receber o State = "Sucess". Os comandos foram criados dentro do device.

   ![image](https://github.com/user-attachments/assets/fd05dd73-af69-4b7c-a172-41da9c36a222)

9. Se precisar alterar algum comando IR do controle, pode voltar no site IR MolSmart, entrar no controle remoto e salvar o comando novo. Após ter feito isso, é só repetir o passo #8 para trazer as informações
    atualizadas do controle para dentro do seu device. 


- Pode usar o Device, pode usar como Botões no seu dashboard. Adicionar o device no dashboard. Embaixo a relação e numeros para cada botão. 
   

	  case 0 : poweroff(); break
		case 1 : poweron(); break
		case 2 : mute(); break
		case 3 : source(); break
		case 4 : back(); break
		case 5 : menu(); break
		case 6 : hdmi1(); break
		case 7 : hdmi2(); break
		case 8 : left(); break
		case 9 : right(); break
		case 10: up(); break
		case 11: down(); break
		case 12: confirm(); break
		case 13: exit(); break
		case 14: home(); break
		case 18: channelUp(); break
		case 19: channelDown(); break
		case 21: volumeUp(); break
		case 22: volumeDown(); break
		case 23: num0(); break
		case 24: num1(); break
		case 25: num2(); break
		case 26: num3(); break
		case 27: num4(); break
		case 28: num5(); break
		case 29: num6(); break
		case 30: num7(); break
		case 31: num8(); break
		case 32: num9(); break
		case 33: btnextra1(); break
		case 34: btnextra2(); break
		case 35: btnextra3(); break
		case 38: appAmazonPrime(); break
		case 39: appYouTube(); break
		case 40: appNetflix(); break
		case 41: btnextra4(); break
		case 42: btnextra5(); break
		case 43: btnextra6(); break
		case 44: btnextra7(); break
		case 45: btnAIRsend(); break
		case 46: btnBIRsend(); break
		case 47: btnCIRsend(); break
		case 48: btnDIRsend(); break
		case 49: playIRsend(); break
		case 50: pauseIRsend(); break
		case 51: nextIRsend(); break
		case 52: guideIRsend(); break
		case 53: infoIRsend(); break
		case 54: toolsIRsend(); break
		case 55: smarthubIRsend(); break
		case 56: previouschannelIRsend(); break
		case 57: backIRsend(); break


