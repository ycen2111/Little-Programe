`timescale 1ns / 1ps
module tb_top( );

parameter BAUDns=104*1000;

reg sysClk=0;
reg nRst=0, txWrite=0, rxRead=0;
reg [7:0] dataout;
reg rxD;
reg [2:0] baudSelect=3'd0;

wire baudClk, txEmpty, txUnderrun, txD, rxEmpty, rxOverrun, rxFramingError, rxBreakDetect;
wire [7:0] datain;

UartBaudGen baudGen (.sysClk(sysClk),.nRst(nRst),.baudSelect(baudSelect),.baudClk(baudClk));
UartTx tx (.sysClk(sysClk),.nRst(nRst),.baudClk(baudClk),.dataout(dataout),.txWrite(txWrite),.txEmpty(txEmpty),.txUnderrun(txUnderrun),.txD(txD));
UartRx rx (.sysClk(sysClk),.nRst(nRst),.baudClk(baudClk),.datain(datain),.rxRead(rxRead),.rxEmpty(rxEmpty),.rxOverrun(rxOverrun),.rxFramingError(rxFramingError),.rxBreakDetect(rxBreakDetect),.rxD(rxD));

always #5 sysClk=!sysClk;
always @(sysClk) rxD=txD;

initial begin
    dataout=9'b110100111;
    #BAUDns nRst=1;
    #BAUDns rxRead=1;
    #BAUDns rxRead=0; txWrite=1;
    #BAUDns txWrite=0; 
    
end

endmodule
