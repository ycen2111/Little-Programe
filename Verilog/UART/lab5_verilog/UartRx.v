module UartRx(  
    input sysClk, nRst, baudClk,    
    output reg [7:0] datain,
    input rxRead,
    output reg rxEmpty, reg rxOverrun, reg rxFramingError,
    output rxBreakDetect,
    input rxD
  );
  
localparam STARTBIT=1'b1;
localparam STOPBIT=1'b0;
  
reg [7:0] rxShiftReg;
reg [2:0] bitCount=0;
reg [2:0] baudCount=0;
reg [2:0] baudSync=0;

/******** TODO declare any other variables/signals you need **********/
reg baudTick,lastBaudCount;
reg rxInProgress=0;
 
// declare our FSM states
localparam IDLE=0, START=1, WAIT=2, SHIFT=3, STOP=4, READ=5;
reg [2:0] state = IDLE;


/************** TODO: define counter baudCountbehaviour    *************/
/************** HINT:   use baudSync reset to mid position  *************/
always @(posedge baudClk && baudSync==3'd2)
    baudCount=baudCount+1;

always @(posedge baudClk && baudSync<3'd2)
    baudSync=baudSync+1;

/************* TODO:baudTickaspulse when baudCount=0  *************/
always @(posedge sysClk) begin
    if (lastBaudCount&&!baudCount[2]&&nRst)
        baudTick=1;
    else
        baudTick=0;
    lastBaudCount=baudCount[2];
end
//always @(posedge sysClk) lastZ<=baudZ; 
//assign baudTick = baudZ & !lastZ & nRst;

always @(posedge sysClk)
   if (!nRst) begin state <= IDLE; bitCount=0; baudCount=0; 
                    rxOverrun=0; rxFramingError=0; rxInProgress=0; rxShiftReg=8'bXXXXXXXX;/********** TODO **************/ end
   else   
   begin      
   case (state)
        IDLE:  begin
                    rxEmpty=1;
                    if (rxD==STARTBIT) state<=START;
               end
        START: begin
                    bitCount=7;
                    rxShiftReg=8'bXXXXXXXX;
                    if (rxInProgress==1) rxOverrun=1;
                    if (baudTick) begin 
                        rxInProgress=1;
                        state<=WAIT;
                    end
               end
        WAIT:  begin 
                    if (baudTick) state<=SHIFT;
               end
        SHIFT: begin 
                    if (bitCount==0) state<=STOP;
                    else state<=WAIT;
                    bitCount=bitCount-1;
                    rxShiftReg={rxD,rxShiftReg[7:1]};
               end       
        STOP:  begin 
                    if (baudTick) state<=READ;
                    datain=rxShiftReg[7:0];
                    rxInProgress=0;
               end
        READ:  begin 
                    if (rxD!=STOPBIT) rxFramingError=1;
                    rxEmpty=0;
                    if (rxRead) state<=IDLE;
               end
        default: state <= IDLE;
  endcase
  end
  
  /************* TODO any other outputs for the module *******************/
  assign rxBreakDetect=rxFramingError&(!rxShiftReg)&(!rxEmpty);

endmodule
    

