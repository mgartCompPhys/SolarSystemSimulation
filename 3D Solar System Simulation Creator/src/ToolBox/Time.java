package ToolBox;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Time{
   double time;
   String units;
   
   public Time(){
       this.time = 0;
       this.units = new String("sec");
   }
   
       
   public Time(double time, String units){
       this.time = time;
       this.units = units;
  }
  
  public void secondsToMinutes(){
      if(this.units.equals("sec")){
          this.units = "min";
          this.time = this.time / 60;
      }
  }
  
  public void minutesToHours(){
          secondsToMinutes();
      if(units.equals("min")){
          this.units = "hours";
          this.time = this.time / 60;
      }
  }
  
  public void HoursToDays(){
          minutesToHours();
      if(units.equals("hours")){
          this.units = "days";
          this.time = this.time / 24;
      }    
  }
  
  public void DaysToYears(){
            HoursToDays();
      if(units.equals("days")){
          this.units = "years";
          this.time = this.time / 365;
      }
  }
  
  public double getTime(){
      return this.time;
  }
  
  public String getUnit(){
      return this.units;
  }
}
