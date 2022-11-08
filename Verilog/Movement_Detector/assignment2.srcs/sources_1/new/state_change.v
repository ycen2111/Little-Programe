`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 01.04.2021 11:15:11
// Design Name: 
// Module Name: state_change_logic
// Project Name: 
// Target Devices: 
// Tool Versions: 
// Description: 
// 
// Dependencies: 
// 
// Revision:
// Revision 0.01 - File Created
// Additional Comments:
// 
//////////////////////////////////////////////////////////////////////////////////


module state_change_logic(
    input sysClk,//system clock
    input clk,//1hz
    input active_button,
    input correct,
    input wrong,
    input movement,
    input [4:0] second,
    output [1:0] state,
    output [5:0] light,
    output active
    );
    
    reg [1:0] state_r=2'd0,state_n;
    reg yellow_state=1;//yellow Led flashing by switching it
    reg in_active;
    reg [5:0] tx_light;
    reg get_correct_password=0,get_movement=0;//mark the correct state
    reg get_active=2'd0;
    reg last_correct=2'd0,last_movement=2'd0,last_button=2'd0;//input memory
    
    assign state=state_r;
    assign light=tx_light;
    assign active=in_active;
    
    /***************************************************************/
    
    //------------------------alarm logic(FSM)---------------------
    //state 0: inactive
    //state 1: active and no beep
    //state 2: active but in low beep
    //state 3: active but in loud beep
    always @(negedge clk) begin
            case (state_r)
                2'd0: begin
                      in_active=0; tx_light=6'b000000;//black
                      if (get_active==1'd1) state_n=2'd2;//get active operation
                      else state_n=state;
                      end
                      
                2'd1: begin
                      in_active=1; tx_light=6'b010010;//green
                      if (get_movement==1'd1) state_n=2'd2;//find movement behavior
                      else state_n=state;
                      end
                      
                2'd2: begin
                      if(yellow_state==1)//yellow flashing manager (0.5hz)
                            tx_light=6'b011011;//yellow
                      else
                            tx_light=6'b000000;//black
                      yellow_state=!yellow_state;
                      
                      if (second>5'd19&&in_active==1'd1&&get_correct_password==1'd0) state_n=2'd3;//time > 20s and haven't recieved corect password
                      else if (second>5'd9&&in_active==1'd0) state_n=2'd1;//time > 10s and is in alart alarm after first active
                      else if (/*second>5'd19&&*/in_active==1'd1&&get_correct_password==1'd1) state_n=2'd1;//time > 20s and have recieved corect password
                      else state_n=state;
                      end
                      
                2'd3: begin
                      tx_light=6'b001001;//red
                      if (get_correct_password==1'd1) state_n=2'd1;//get correct password
                      else state_n=state;
                      end
                      
                default: state_n=2'd0;
            endcase
        state_r=state_n;//save past state memory
    end
    
    /*******************************************************/
    
    always @(posedge sysClk) begin
        if(!last_correct&&correct) get_correct_password=1;//received correct's positive edge
        else if(state_r==2'd1) get_correct_password=0;
        
        if(!last_movement&&movement) get_movement=1;//received movement's positive edge
        else if(state_r==2'd2) get_movement=0;
        
        if(!last_button&&active_button) get_active=1;//received active button's positive edge
        
        //memory saving
        last_correct=correct;
        last_movement=movement;
        last_button=active_button;
    end
    
endmodule
