/*

XXXXXXX       XXXXXXXhhhhhhh                                                       000000000                       
X:::::X       X:::::Xh:::::h                                                     00:::::::::00                     
X:::::X       X:::::Xh:::::h                                                   00:::::::::::::00                   
X::::::X     X::::::Xh:::::h                                                  0:::::::000:::::::0                  
XXX:::::X   X:::::XXX h::::h hhhhh         aaaaaaaaaaaaa  rrrrr   rrrrrrrrr   0::::::0   0::::::0nnnn  nnnnnnnn    
   X:::::X X:::::X    h::::hh:::::hhh      a::::::::::::a r::::rrr:::::::::r  0:::::0     0:::::0n:::nn::::::::nn  
    X:::::X:::::X     h::::::::::::::hh    aaaaaaaaa:::::ar:::::::::::::::::r 0:::::0     0:::::0n::::::::::::::nn 
     X:::::::::X      h:::::::hhh::::::h            a::::arr::::::rrrrr::::::r0:::::0 000 0:::::0nn:::::::::::::::n
     X:::::::::X      h::::::h   h::::::h    aaaaaaa:::::a r:::::r     r:::::r0:::::0 000 0:::::0  n:::::nnnn:::::n
    X:::::X:::::X     h:::::h     h:::::h  aa::::::::::::a r:::::r     rrrrrrr0:::::0     0:::::0  n::::n    n::::n
   X:::::X X:::::X    h:::::h     h:::::h a::::aaaa::::::a r:::::r            0:::::0     0:::::0  n::::n    n::::n
XXX:::::X   X:::::XXX h:::::h     h:::::ha::::a    a:::::a r:::::r            0::::::0   0::::::0  n::::n    n::::n
X::::::X     X::::::X h:::::h     h:::::ha::::a    a:::::a r:::::r            0:::::::000:::::::0  n::::n    n::::n
X:::::X       X:::::X h:::::h     h:::::ha:::::aaaa::::::a r:::::r             00:::::::::::::00   n::::n    n::::n
X:::::X       X:::::X h:::::h     h:::::h a::::::::::aa:::ar:::::r               00:::::::::00     n::::n    n::::n
XXXXXXX       XXXXXXX hhhhhhh     hhhhhhh  aaaaaaaaaa  aaaarrrrrrr                 000000000       nnnnnn    nnnnnn

*/

package com.example.trafficlight

import android.os.Bundle
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    //Timer declaration
    private var timer = Timer()
    private var timerStarted = false

    //Circles (traffic light) declaration
    lateinit var lightUp: TextView
    lateinit var lightMiddle: TextView
    lateinit var lightDown: TextView
    
    //Buttons declaration
    private lateinit var pow: ToggleButton
    private lateinit var nig: ToggleButton
    private lateinit var nor: ToggleButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        //pow = Power ON/OFF button
        pow = findViewById(R.id.pow)
        //nig = Night mode button
        nig = findViewById(R.id.nig)
        //nor = Normal mode button
        nor = findViewById(R.id.nor)

        //lightUp = red light
        lightUp = findViewById(R.id.light_up)
        //lightMiddle = orange light
        lightMiddle = findViewById(R.id.light_middle)
        //lightDown = green light
        lightDown = findViewById(R.id.light_down)
        
        buttons()
    }
    
    //Traffic light off (clear)
    private fun clearAll(){
        lightUp.setBackgroundResource(R.drawable.clear)
        lightMiddle.setBackgroundResource(R.drawable.clear)
        lightDown.setBackgroundResource(R.drawable.clear)
    }
    
    //Traffic light on and off
    private fun stopTimer(){
        if(!timerStarted) return

        timer.cancel()
        timer = Timer()

        timerStarted = false
    }

    //Normal mode function
    private fun normalMode() {
        timerStarted = true

        //draft
        //   5 (2)  5    5
        //R _____
        //Y    __     _____
        //G      _____

        //Tick = Something like timer
        //Tick declaration
        var tick = 0

        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                tick += 1

                //red 5s, orange 2s
                if (tick <= 5){
                    if(tick > 3){
                        lightMiddle.setBackgroundResource(R.drawable.orange)
                        return
                    }
                    //orange OFF
                    clearAll()
                    lightUp.setBackgroundResource(R.drawable.red)
                }

                //green 5-10s
                else if (tick <= 10){

                    //red, orange OFF
                    clearAll()
                    lightDown.setBackgroundResource(R.drawable.green)
                }

                //orange 10-15s
                else if (tick <= 15){
                    //green OFF
                    clearAll()
                    lightMiddle.setBackgroundResource(R.drawable.orange)
                }
                //reset Tick
                if(tick == 15) tick = 0
            }
        //This is time in millisecond's (delay/period)
        }, 1000, 1000)

    }

    //Night mode function
    //Is responsible for starting the function, pressing the Night mode button and starting the orange flashing cycle
    private fun nightMode() {
        var isOn = false

        clearAll()
        stopTimer()

        timerStarted = true
        
        //Timer that toggles the orange light every 1s
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {

                if(isOn) lightMiddle.setBackgroundResource(R.drawable.orange)
                else lightMiddle.setBackgroundResource(R.drawable.clear)
                isOn = !isOn

            }

        //This is time in millisecond's (delay/period)
        }, 1000, 1000)
    }

    //Buttons and their proper functioning
    private fun buttons(){
        
        //These buttons are inaccessible at start-up
        nig.isClickable = false
        nor.isClickable = false

        pow.setOnCheckedChangeListener { _, isChecked -> 
            if (isChecked) {
                nig.isClickable = true
                nor.isClickable = true
            } else {
                nig.isClickable = false
                nor.isClickable = false

                nig.isChecked = false
                nor.isChecked = false
                stopTimer()
            }
        }

        //Power button is inaccessible but sounds is playing (why?)
        nig.setOnCheckedChangeListener { _, isChecked -> run { 
                if(!pow.isChecked) {
                    nig.isChecked = false
                    nig.isClickable = false
                    return@run
                }

                nor.isClickable = !isChecked
                if(isChecked) nightMode() 
                else {
                    stopTimer()
                    clearAll()
                }
            }
        }

        //This is functionality between normal mode button and night mode button (if normal mode button is pressed we can't press night mode button)
        nor.setOnCheckedChangeListener { _, isChecked -> run { 
                nig.isClickable = !isChecked
                
                if(isChecked) normalMode()
                else{
                    stopTimer()
                    clearAll()
                }
            }
        }
    }
}