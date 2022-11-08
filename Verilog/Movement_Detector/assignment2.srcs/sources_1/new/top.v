`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 31.03.2021 18:30:34
// Design Name: 
// Module Name: top
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


module top(
    input clk,
    
    /********************************
    **active button:    left 1,V10 **
    **get movement:     left 2,U11 **
    **correct password: right 2,L16**
    **wrong password:   right 1,J15**
    ********************************/
    input active,correct,wrong,movement,
    
    output [5:0] tricol,
    output [7:0] AN,CA,
    //output [1:0] state_n,//only used in simulation
    //output [4:0] second,//only used in simulation
    output in_active
    );
  
    reg [23:0] SW=24'h000000;//keeping first 6 LCD digits places into 0(only need to count up to 20 seconds)
    reg warning=0;
    
    wire active_button,correct_password,wrong_password,find_movement;
    wire [1:0]current_state;
    wire fastclk,clk1hz;
    wire [4:0] d_unit; //timer, end in 20s but have slight difference with current time
    wire [4:0] run_d_unit;//curent time
    wire [31:0] count;
    wire [31:0] dispnum;
    wire [3:0] ones,tens;//results of ones and tens in bcd, from binary to decimal
    wire is_active;
    
    assign active_button=active;
    //assign state_n=current_state;
    assign correct_password=correct;
    assign wrong_password=wrong;
    assign find_movement=movement;
    //assign second=d_unit;
    assign run_d_unit=d_unit-1;
    assign in_active=is_active;
    
    assign fastclk=count[16];
    assign dispnum={SW,tens,ones};
    
    //1 hz frequency
    assign clk1hz=count[26];//26 for implementation board and 20 for simulation
        
    /*****************************************************/
    
    //let warning=1 if state=2 and 3 and start the timer counter
    always @(negedge clk1hz) begin
        if (current_state==2'd2||current_state==2'd3)
            warning=1;
        else warning=0;
    end
    
    /*****************************************************/
    
    //basic counter and calculate fastClk and clk1hz
    counter my_count (clk,count);
    
    //output a binary time number
    up_count_unit my_count_unit(clk1hz,active_button,find_movement,d_unit);//timer
    
    //output LCD controller, only start to display the current time if warning = 1
    DigitDisplay my_display (fastclk,dispnum,warning,AN,CA);//tricol output
    
    //the bcd function, changing binary input into decimal output
    bin2bcd_12bit b2b (run_d_unit,ones,tens);//time value, binary to bcd
    
    //----------basic home alarm logic system---------------
    //alarm status change and centralised management analysis of all four button inputs
    state_change_logic state (clk,clk1hz,active_button,correct_password,wrong_password,find_movement,d_unit,current_state,tricol,in_active);
    
    
endmodule
