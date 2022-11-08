module UartBaudGen
# ( parameter SYSTEM_CLOCK_MHZ = 100*1000000 /***** TODO *****/,
    parameter BAUD_CLK_MULT = 8.0 /***** TODO *****/,        // baud clock multiplier eg Bclk x 8    
    parameter MAX_BAUD_RATE = 651.0 /***** TODO *****/)   // maximum baud rate used to define initial divider
  (
    input sysClk, nRst,
    input  [2:0] baudSelect,
    output baudClk );

localparam CLK_DIV = $rtoi(SYSTEM_CLOCK_MHZ/(MAX_BAUD_RATE*BAUD_CLK_MULT*2));
localparam DIV_BITS = $clog2(CLK_DIV) + 1;

reg [DIV_BITS-1:0] count = 0;
reg [7:0] masterClk=0;

// TODO: define two counters and the multiplexer
reg bClk=0;
assign baudClk=bClk;

always @(negedge nRst) begin //reset module
    count=0;
    masterClk=0;
end

always @(posedge sysClk) begin //Modulo counter
    if (count==CLK_DIV[31:0])
        count=0;
    else
        count=count+1;
end

always @(*) begin //8-bit counter
    if (count==0)
        masterClk=masterClk+1;
end

always @(*) begin //baud clk select
    bClk=masterClk[baudSelect];
end

endmodule
