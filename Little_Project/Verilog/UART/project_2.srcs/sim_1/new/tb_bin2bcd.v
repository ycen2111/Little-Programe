`timescale 1ns / 1ps

module tb_bin2bcd(   );

reg clk=0;
reg [13:0] num;
reg start=0;

wire [15:0] bcd;
wire data_valid;

Binary_to_BCD #(.INPUT_WIDTH(14), .DECIMAL_DIGITS(4))
    UUT (.i_Clock(clk), .i_Binary(num), .i_Start(start), .o_BCD(bcd), .o_DV(data_valid));

always #5 clk=!clk;

    task TAST(input [13:0] v);
    begin
    num=v;
    @(posedge clk) start=1;
    @(posedge clk) start=0;
    @(data_valid==1) $display("Test for %d Digits are [%d] [%d] [%d] [%d]",num, bcd[15:12], bcd[11:8], bcd[7:4], bcd[3:0]);
    end
    endtask

initial begin
    #100;
    TAST(0);
    TAST(1);
    TAST(4095);
    TAST(4096);
    TAST(8191);
    TAST(8192);
    TAST(9999);
    TAST(10010);
end

endmodule
