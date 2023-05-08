module UartTx(
    input sysClk, nRst, baudClk,
    // Transmitter interface
    input [7:0] dataout,
    input txWrite,
    output txEmpty, reg txUnderrun,
    output reg txD
    );
    
localparam STARTBIT=1'b1;
localparam STOPBIT=1'b0;    
    
reg [7:0] txShiftReg;
reg [2:0] bitCount=0;
reg [2:0] baudCount=0;


/******** TODO declare any other variables/signals you need **********/
reg baudTick,lastBaudCount;

// declare our FSM states
localparam IDLE=0, SYNC=1, START=2,SHIFT=3, WAIT=4, STOP=5;
reg [2:0] state = IDLE;

/********** TODO define baudCount behaviour ***********/
always @(posedge baudClk)
    baudCount=baudCount+1;

/********** TODO Define baudTick pulse *************/
always @(posedge sysClk) begin
    if (lastBaudCount&&!baudCount[2]&&nRst)
        baudTick=1;
    else
        baudTick=0;
    lastBaudCount=baudCount[2];
end


always @(posedge sysClk)
   if (!nRst) begin state <= IDLE; bitCount=0; baudCount=0;/********** TODO **************/ end
   else
   case (state)
        IDLE:  begin 
                    txShiftReg=dataout[7:0]; 
                    if (txWrite) state<=SYNC; 
                    else state<=IDLE;
               end
        SYNC:  begin 
                    bitCount=7; 
                    if (baudTick) state<=START; 
                    else state<=SYNC;
               end
        START: begin 
                    txD=STARTBIT; 
                    txUnderrun=1; 
                    if (baudTick) state<=WAIT; 
                    else state<=START;
               end 
        WAIT:  begin 
                    txD=txShiftReg[0]; 
                    if (baudTick) state<=SHIFT; 
                    else state<=WAIT;
               end 
        SHIFT: begin 
                    if (bitCount==0) state<=STOP; 
                    else state<=WAIT;
                    bitCount=bitCount-1; 
                    txShiftReg={1'bX,txShiftReg[7:1]}; 
               end       
        STOP:  begin 
                    txD=STOPBIT; 
                    txUnderrun=0; 
                    if (baudTick) state<=IDLE;  
                    else state<=STOP;
               end
        default: state <= IDLE;
  endcase

assign txEmpty = (state==IDLE)?1:0;
     
endmodule
