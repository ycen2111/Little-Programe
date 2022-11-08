module uart
# ( parameter SYSTEM_CLOCK_MHZ = 100*1000000 /********** TODO *********/,
    parameter MAX_BAUD_RATE = 8.0    /********** TODO *********/ )
(
    input sysClk, nRst,
    output baudClk,
    input [2:0] baudSelect,
    // Transmitter interface
    input [7:0] dataout,
    input txWrite,
    output txEmpty, txUnderrun,
    output txD,
    // Receiver interface
    input  rxD,
    output [7:0] datain,
    input rxRead,
    output rxEmpty, rxOverrun, rxFramingError, rxBreakDetect,
    output [7:0] AN,CA
    );
    localparam BAUD_CLOCK_MULT=8;
    
    reg[31:0] errorNumber=2;
    
    wire bxclk;    
    assign baudClk = bxclk; // for debugging
 
    UartBaudGen #(.SYSTEM_CLOCK_MHZ(SYSTEM_CLOCK_MHZ), .BAUD_CLK_MULT(BAUD_CLOCK_MULT), .MAX_BAUD_RATE(MAX_BAUD_RATE) )
         BaudGenInstance ( .sysClk(sysClk), .nRst(nRst), .baudSelect(baudSelect), .baudClk(bxclk) );

    UartTx TxInstance( .sysClk(sysClk), .nRst(nRst), .baudClk(bxclk),
                       .dataout(dataout), .txWrite(txWrite),
                       .txEmpty(txEmpty), .txUnderrun(txUnderrun), .txD(txD) );    

    UartRx RxInstance( .sysClk(sysClk), .nRst(nRst), .baudClk(bxclk),
                       .datain(datain), .rxRead(rxRead),
                       .rxEmpty(rxEmpty), .rxOverrun(rxOverrun), .rxFramingError(rxFramingError),
                       .rxBreakDetect(rxBreakDetect), .rxD(rxD) ); 
                       
    DigitDisplay my_display (sysClk,errorNumber,AN,CA);
     
endmodule
