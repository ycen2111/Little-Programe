`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 01.04.2021 14:09:03
// Design Name: 
// Module Name: state_behavier
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


module state_behavier(
    input clk,
    input [1:0] state,
    output [5:0] light,
    output starting
    );
    
    reg yellow_state_r=1;
    reg yellow_state_n;
    reg [5:0] tx_light;
    reg tx_starting=0;
    
    always @(negedge clk) begin
        case (state)
            2'd0: begin tx_starting=1; tx_light=6'b000000; end//black
            2'd1: begin tx_starting=0; tx_light=6'b010010; end//green
            2'd2: begin if(yellow_state_r==1)
                            tx_light=6'b011011;//yellow
                        else
                            tx_light=6'b000000;
                        yellow_state_r=yellow_state_r+1;
                  end
            2'd3: begin tx_light=6'b001001; end//red
        endcase
    end
    
    assign light=tx_light;
    assign starting=tx_starting;
    
endmodule
