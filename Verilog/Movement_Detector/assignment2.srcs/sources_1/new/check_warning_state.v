`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 14.04.2021 14:21:22
// Design Name: 
// Module Name: check_warning_state
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


module check_warning_state(
    input clk,
    input state,
    output warning
    );
    
    reg tx_warning;
    assign warning=tx_warning;
    
    always @(negedge clk) begin
        if (state==2'd2||state==2'd3)
            tx_warning=1;
        else tx_warning=0;
    end
    
endmodule
