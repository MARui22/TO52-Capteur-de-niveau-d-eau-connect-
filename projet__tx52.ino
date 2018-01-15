#include <Wire.h>
#include <SmeSFX.h>
#include <Arduino.h>
#include <HTS221.h>
#include "cc2541.h"
#include <avr/dtostrf.h>
#include <RTCZero.h>
#include "ArduinoLowPower.h"

char SensorMsg[3];
bool messageSent;
int distance_avant;
/* Constantes pour les broches */
const int Ping_Pin = 8;     /*branche sig*/

const unsigned long MEASURE_TIMEOUT = 25000UL;    // 25ms = ~8m à 340m/s

RTCZero rtc;
/* initial  des donnees de temps*/
const byte seconds = 00;
const byte minutes = 21;
const byte heures = 16;
const byte jours = 15;
const byte mois = 1;
const byte annee = 18; 
  
float pd_avant = 0;

void setup() {
  smeBle.begin();
  smeHumidity.begin();
  SerialUSB.begin(115200);

  rtc.begin();      // initial de rtc
  /* donner les valeurs du temps*/
  rtc.setTime(heures, minutes, seconds);
  rtc.setDate(jours, mois, annee);
}

void loop() {
  // put your main code here, to run repeatedly:

  float n = temperature ( );
  //delay (500);
  float pd = (3000 - distance())/10;
  char mes[200];

  int t = int(n);
  int nt = (n-t)*100;
  int pc = int(pd);
  int pm = (pd - pc)*100;
  String str = String(t)+";"+String(nt)+";"+String(pc)+";"+String(pm)+";"+calendar();
 /* if (pd_avant == pd){
  LowPower.sleep(10000);
  }*/
  
 // float pd_avant = pd;

  str.toCharArray(mes,200);

 
 // Transmitt_data(charBuf);
  Transmitt_data(mes);

 
}
char* cast( float n,float profondeur)
{

}

//Fonction d'envoie sur le réseau sigfox du niveau d'eau et de la température.
void sendMessage(float niv, float temp)
{

  SerialUSB.println("sending Hello over the network");

  //remplissage du buffer d'envoie de message
  int t = int(temp);
  int n = int(niv);
  SensorMsg[0] = lowByte(t); //temperature dans le buffer
  SensorMsg[1] = lowByte(n); //niveau dans le buffer
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
  //delayMicroseconds(2);
  digitalWrite(Ping_Pin, HIGH);       // Start ranging
 // delayMicroseconds(5);              //   with 5 microsecond burst
  digitalWrite(Ping_Pin, LOW);        // End ranging
  pinMode(Ping_Pin, INPUT);           // Set pin to INPUT
  float measure = pulseIn(Ping_Pin, HIGH);//,MEASURE_TIMEOUT); // la durée d'implusion
  //SerialUSB.println(measure);

  const float T = temperature();
  const float Son_Vit = 331.4 + 0.607 * T; //  331.4*(1+T/273)^0.5 = ; // mm/ms

  SerialUSB.print("la vitesse de son est :");
  SerialUSB.println(Son_Vit);

  float distance = (measure / 2.0/1000) * Son_Vit; //
  SerialUSB.print("Distance : ");
  SerialUSB.print(distance);
  SerialUSB.println (F("mm")); // distance en mm
  SerialUSB.print(distance / 10.0, 2); // distance en cm
  SerialUSB.println (F("cm"));
  SerialUSB.print (distance / 1000.0, 2); // distance en m
  SerialUSB.println (F("m"));


  delay (500);
  return distance;
}

//
String calendar()
{
  //imprimer le jour 
  print2digits(rtc.getDay());
  Serial.print("/");
  print2digits(rtc.getMonth());
  Serial.print("/");
  print2digits(rtc.getYear());
  Serial.print(" ");
  int j = rtc.getDay();
  int m = rtc.getMonth();
  int a = rtc.getYear();
  String calendar = String(a)+ "/"+String(m)+"/"+String(j);
  return calendar;
}
String horloge(){
  // imprimer le temps
  print2digits(rtc.getHours());
  Serial.print(":");
  print2digits(rtc.getMinutes());
  Serial.print(":");
  print2digits(rtc.getSeconds());
  
  int se = rtc.getSeconds();
  int mi = rtc.getMinutes();
  int he = rtc.getHours();
 
  
  String horloge = String(he)+":"+String(mi)+":"+String(se);
  
  return horloge;
  }

//Fonction de transmission vers ble
void Transmitt_data(char *str)
{
  char len = strlen(str);
  smeBle.write(str, len);

  
}

//Fonction de reception du ble
void Receive_data()
{
  volatile char data;

  while (smeBle.available()) {

    data = smeBle.read();

    SerialUSB.print(data, HEX);
    SerialUSB.print(" ");

    //convert char into Hex;
  }
  //delay(10);
}

//Fonction de imprimer le temps
void print2digits(int number)
{
  if(number < 10){
    Serial.print("0");
    }
    Serial.print(number);
  }

void domrmir()
{
  Serial.println("");
  }
