`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: University of Edinburgh
// Engineer: Cen Yang && Lei Xia
// 
// Create Date: 07.03.2023 14:15:43
// Design Name: Test bench of processor
// Module Name: TOP_TB
// Project Name: MicroProcessor
// Target Devices: XC7A35TCPG236-1
// Tool Versions: Vivado 2015.2
// Description: Test bench of the whole processor
// 
// Dependencies: 
// 
// Revision:
// Revision 0.01 - File Created
// Additional Comments:
// 
//////////////////////////////////////////////////////////////////////////////////


module TOP_TB();

    reg CLK, RESET;

    TOP UUT(
        .CLK(CLK),
        .RESET(RESET)
        );
    
    always #5 CLK = ~CLK;
    
    initial begin
        CLK = 0;
        RESET = 1;
        #50 RESET = 0;
    end

endmodule
