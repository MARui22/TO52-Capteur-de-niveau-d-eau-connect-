  /*Capteur de niveau d'au connecté
   * Send the 
   * 
   * 
   */
  
  #include <Wire.h>
  #include <SmeSFX.h>
  #include <Arduino.h>
  #include <HTS221.h>
  
  char SensorMsg[3];
  bool messageSent;

  /* Constantes pour les broches */
const int Ping_Pin = 8;/*branche sig*/

const unsigned long MEASURE_TIMEOUT = 25000UL; // 25ms = ~8m à 340m/s
  
  void setup() {
  
  SerialUSB.begin(115200);
      sfxAntenna.begin();
      smeHumidity.begin();
      int initFinish=1;
      
  
      while (!SerialUSB) {
          ; 
      }    
  
  }
  
  void loop() {
    // put your main code here, to run repeatedly:
    
    float a = temperature ( );
    delay (500);
    float b = distance ( );
  }
  
  //Fonction d'envoie sur le réseau sigfox du niveau d'eau et de la température.
  void sendMessage(float niv, float temp)
  {
  
  SerialUSB.println("sending Hello over the network");
  
  //remplissage du buffer d'envoie de message   
      int t=int(temp); 
      int n = int(niv);                  
      SensorMsg[0]=lowByte(t);  //temperature dans le buffer          
      SensorMsg[1]=lowByte(n);  //niveau dans le buffer 
      //SensorMsg[2]=lowByte(lum);          
      
      //envoie du message
      sfxAntenna.sfxSendData(SensorMsg, strlen((char*)SensorMsg));
    
    bool answerReady = sfxAntenna.hasSfxAnswer();
  
      if (answerReady) {
          if (sfxAntenna.getSfxMode() == sfxDataMode) {
  
              switch (sfxAntenna.sfxDataAcknoledge()) {
              case SFX_DATA_ACK_START:
                  SerialUSB.println("Waiting Answer");
                  break;
  
              case SFX_DATA_ACK_PROCESSING:
                  SerialUSB.print('.');
                  break;
  
              case SFX_DATA_ACK_OK:
  #ifndef ASME3_REVISION
                  ledGreenLight(HIGH);
  #endif
                  SerialUSB.println(' ');
                  SerialUSB.println("Answer OK :) :) :) :)");
                  break;
  
              case SFX_DATA_ACK_KO:
  #ifndef ASME3_REVISION
                  ledRedLight(HIGH);
  #endif
                  SerialUSB.println(' ');
                  SerialUSB.println("Answer KO :( :( :( :(");
                  break;
              }
          }
      }
  }
  
  
  float temperature ( )
  {
  double temperature = smeHumidity.readTemperature();
  Serial.print("Temperature :");
  Serial.println(temperature);
  delay(1000);
  return temperature;
  }
  
  float distance ( )
  {

  pinMode(Ping_Pin, OUTPUT);          // Set pin to OUTPUT
  digitalWrite(Ping_Pin, LOW);        // Ensure pin is low
  delayMicroseconds(2);
  digitalWrite(Ping_Pin, HIGH);       // Start ranging
  delayMicroseconds(5);              //   with 5 microsecond burst
  digitalWrite(Ping_Pin, LOW);        // End ranging
  pinMode(Ping_Pin, INPUT);           // Set pin to INPUT
  float measure = pulseIn(Ping_Pin, HIGH);//,MEASURE_TIMEOUT); // la durée d'implusion
  SerialUSB.println(measure);
 float distance =88;//= (measure /2.0) * Son_Vit; //
  

  const float T = temperature();
  const float Son_Vit = 331.4+0.607*T;//  331.4*(1+T/273)^0.5 = ; // mm/us

SerialUSB.print("la vitesse de son est :");
SerialUSB.println(Son_Vit);
  

  
  float distance = (measure /2.0) * Son_Vit; //
  SerialUSB.print("Distance : ");
  SerialUSB.print(distance);
  SerialUSB.println (F("mm")); // distance en mm
  SerialUSB.print(distance/10.0, 2); // distance en cm
  SerialUSB.println (F("cm"));
  SerialUSB.print (distance/1000.0, 2); // distance en m
  SerialUSB.println (F("m"));


  delay (500);
  return distance;
  }

