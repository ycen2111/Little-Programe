`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 31.03.2021 22:11:30
// Design Name: 
// Module Name: DigitDisplay
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


module DigitDisplay(
    input clk,
    input [31:0] value,
    input warning,
    output reg [7:0] AN,
    output [7:0] CA
    );
    
    reg [3:0] nibble;
    reg [2:0] count=0;
    wire [6:0] digit;
    
    always @(posedge clk) count=count+1;
    
    always @(posedge clk) begin
        if (!warning) begin
            case (count)
                0:begin AN=8'b01111111; nibble=4'b0000; end
                1:begin AN=8'b10111111; nibble=4'b0000; end
                2:begin AN=8'b11011111; nibble=4'b0000; end
                3:begin AN=8'b11101111; nibble=4'b0000; end
                4:begin AN=8'b11110111; nibble=4'b0000; end
                5:begin AN=8'b11111011; nibble=4'b0000; end
                6:begin AN=8'b11111101; nibble=4'b0000; end
                7:begin AN=8'b11111110; nibble=4'b0000; end
            endcase
        end
        
        else begin
            case (count)
                0:begin AN=8'b01111111; nibble=value[31:28]; end
                1:begin AN=8'b10111111; nibble=value[27:24]; end
                2:begin AN=8'b11011111; nibble=value[23:20]; end
                3:begin AN=8'b11101111; nibble=value[19:16]; end
                4:begin AN=8'b11110111; nibble=value[15:12]; end
                5:begin AN=8'b11111011; nibble=value[11:8]; end
                6:begin AN=8'b11111101; nibble=value[7:4]; end
                7:begin AN=8'b11111110; nibble=value[3:0]; end
            endcase
        end
    end
    
    Nibble2SevenSeg myssd (nibble,digit);
    assign CA={~(digit),1'b1};

endmodule
