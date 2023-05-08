`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 31.03.2021 22:08:56
// Design Name: 
// Module Name: Nibble2SevenSeg
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


module Nibble2SevenSeg(
    input [3:0] nibble,
    output reg [7:0] segments
    );
    
    always @(nibble)
        case (nibble)
            4'h0: segments=7'b1111110;
            4'h1: segments=7'b0110000;
            4'h2: segments=7'b1101101;
            4'h3: segments=7'b1111001;
            4'h4: segments=7'b0110011;
            4'h5: segments=7'b1011011;
            4'h6: segments=7'b1011111;
            4'h7: segments=7'b1110000;
            4'h8: segments=7'b1111111;
            4'h9: segments=7'b1111011;
            default: segments=7'b0000000;
        endcase
    
endmodule
