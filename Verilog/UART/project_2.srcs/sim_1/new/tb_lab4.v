`timescale 1ns / 1ps

module tb_lab4( );

parameter BAUDns=104*1000;

reg sysClk=0;
reg b_clk=0;
reg nRst=0, txWrite=0, rxD=1, rxRead=0;
reg [7:0] dataout;
reg [3:0] n;
reg [31:0]total_count=0, pass_count=0;

wire baudClk, txEmpty, txUnderrun, txD, rxEmpty, rxOverrun, rxFramingError, rxBreakDetect;
wire [7:0] datain;

model_uart UUT (.sysClk(sysClk), 
                .nRst(nRst),
                .baudClk(baudClk),
                .dataout(dataout),
                .txWrite(txWrite),
                .txEmpty(txEmpty),
                .txUnderrun(txUnderrun),
                .txD(txD),
                .rxD(rxD),
                .datain(datain),
                .rxRead(rxRead),
                .rxEmpty(rxEmpty),
                .rxOverrun(rxOverrun),
                .rxFramingError(rxFramingError),
                .rxBreakDetect(rxBreakDetect));

always #5 sysClk=!sysClk;
always #(BAUDns/2) b_clk=!b_clk;

always @(posedge b_clk && baudClk) pass_count=pass_count+1;
always @(posedge baudClk) total_count=total_count+1;

always @(posedge rxRead) begin
    #BAUDns rxD=0;
    for (n=0;n<8;n=n+1) #BAUDns rxD=dataout[n];
    #BAUDns 
    rxD=1;
    //rxD=0;
end

always @(posedge rxOverrun) $display("overrunning");
always @(posedge rxFramingError) $display("framingerror");
always @(posedge rxBreakDetect) $display("BreakDetect");

always @(posedge txEmpty)
    $display("Test value of dataout is %d, datain is %d, deviate rate is %d / %d", dataout[7:0], datain[7:0], pass_count[31:0],total_count[31:0]); 

initial begin
    //dataout=8'b11001011;
    dataout=8'b01100011;
    nRst=1;
    #BAUDns txWrite=1;
    #BAUDns txWrite=0; rxRead=1;
    #BAUDns rxRead=0;
    //rxRead=1;
    
end

endmodule
