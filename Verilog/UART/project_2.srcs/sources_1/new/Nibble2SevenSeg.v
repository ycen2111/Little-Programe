`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 11.03.2021 13:59:07
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
            4'hA: segments=7'b1110111;
            4'hb: segments=7'b0001111;
            4'hC: segments=7'b1001110;
            4'hd: segments=7'b0111101;
            4'hE: segments=7'b1001111;
            4'hF: segments=7'b1000111;
        endcase
endmodule
