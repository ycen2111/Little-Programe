`timescale 1ns / 1ps

module tb_top( );

    parameter onetens_BAUDs=2*500000000;
    parameter BAUDs=10*onetens_BAUDs;
    
    reg clk=0;
    reg button,correct,wrong,movement;
    
    wire [5:0] tricol;
    wire [7:0] AN,CA;
    wire [1:0] current_state;
    wire [4:0] second;
    wire active;
    
    always #500 clk=!clk;
    
    top UUT (clk,button,correct,wrong,movement,tricol,AN,CA,current_state,second,active);
    
    initial begin
        button=0;correct=0;wrong=0;movement=0;
        #BAUDs;#BAUDs;
        #BAUDs button=1;
        #BAUDs button=0;
        #BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;//10s
        #BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;
        #BAUDs movement=1;
        #BAUDs movement=0;
        #BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;
        #BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;
        #BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;//10s
        #BAUDs correct=1;
        #BAUDs correct=0;
        #BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;//10s
        #BAUDs movement=1;
        #BAUDs movement=0;
        #BAUDs correct=1;
        #BAUDs correct=0;
        #BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;
        #BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;
        #BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;//10s
        #BAUDs movement=1;
        #BAUDs movement=0;
    end
    
endmodule
