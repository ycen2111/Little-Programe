`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 31.03.2021 21:59:11
// Design Name: 
// Module Name: up_count_unit
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


module up_count_unit(
    input clk,
    input active,
    input movement,
    output [4:0] value
    );
    
    reg [4:0] count=0;
    reg clear=0,clear_m;
    always @(negedge clk) begin
        if (clear!=clear_m)
            count=0;
        else if (count>5'd20)
            count=5'd21;
        else count=count+1;
        clear_m=clear;
    end
    
    always @(posedge active || movement)
        clear=!clear;
    
    assign value=count;
    
endmodule
