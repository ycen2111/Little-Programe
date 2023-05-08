`timescale 1ns / 1ps

module tb_combi(   );

reg [13:0] num;
reg clk=0;

wire [3:0] thousands,hundreds,tens,ones;

bin2bcd_12bit UUT (.binIN(num), .thousands(thousands), .hundreds(hundreds), .tens(tens), .ones(ones), .valid(valid));

always #50 clk=!clk;

task TASK(input [13:0] i);
    begin
        if (valid==1)
        begin
         @(negedge clk) #5 $display("Test for %d Digits are [%d] [%d] [%d] [%d]",i, thousands, hundreds, tens, ones);
        end
    end
    endtask

initial begin
    #100 num=0;TASK(num);
    #100 num=100;TASK(num);
    #100 num=4095;TASK(num);
    //#100 num=4096;TASK(num);
    //#100 num=8191;TASK(num);
    //#100 num=8192;TASK(num);
    //#100 num=9999;TASK(num);
    #100 num=10000;TASK(num);
    #100 num=10001;TASK(num);
end

endmodule
