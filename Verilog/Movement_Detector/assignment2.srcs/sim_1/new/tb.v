`timescale 1ns / 1ps

module tb_state( );

    parameter BAUDs=2*500000000;
    
    reg reset=0;
    reg active_button;
    reg correct_password;
    reg wrong_password;
    reg find_movement;
    reg [4:0] d_unit;
    reg clk=0;
    
    wire [5:0] light;
    wire [1:0] current_state;
    wire starting_log;
    
    always #500000000 clk=!clk;
    
    always @(posedge reset) begin
        active_button=0;correct_password=0;wrong_password=0;find_movement=0;d_unit=0;
    end
    
    always @(negedge clk) begin
        if (d_unit>20)
            d_unit=21;
        else d_unit=d_unit+1;
    end
    
    always @(posedge active_button || find_movement)
        d_unit=0;
    
    state_change_logic UUT (clk,reset,active_button,correct_password,wrong_password,find_movement,d_unit,current_state,light,starting_log);
    
    initial begin
        #BAUDs reset=1;
        #BAUDs reset=0;
        #BAUDs active_button=1;
        #BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs active_button=0;//10s
        #BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;
        #BAUDs find_movement=1;
        #BAUDs find_movement=0;
        #BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;
        #BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;
        #BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;//10s
        #BAUDs correct_password=1;
        #BAUDs correct_password=0;
        #BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;#BAUDs;//10s
        #BAUDs find_movement=1;
        #BAUDs find_movement=0;
        #BAUDs correct_password=1;
        #BAUDs correct_password=0;
    end

endmodule
