//create the random maze using rand() and srand()
#include <iostream>
#include <cstdlib>//head file of rand() and srand()
#include <ctime>
#include <fstream>//head file of output result
using namespace std;

const int NUMBER=50;

void output(int,int,char[55][55]);//output matrix(maze)
void main_way_design(int,int,int,int,char[55][55]);//design the main pass way
void trick_way_design(int,int,char[55][55],char);//design the other pass way
bool check(int,int,char[55][55],char);//whether this place can be designed as a road
void package(int,int,char[55][55]);//hide the main and other way, and fill the empty places

int main()
{
//initialize characteristics
    int row_number=NUMBER,column_number=NUMBER;
    float tool=0;
    char trick_number='A';
//--------------------------------------------------------
//input number of rows and columns, build matrix of maze
    //cout<<"Enter the number of rows and columns(<=50): ";
    //cin>>row_number>>column_number;

    char maze[55][55]={0};
    for (int i=0;i<column_number;i++)
    {
        maze[0][i]='_';
        maze[row_number-1][i]='_';
    }
    for (int i=0;i<row_number;i++)
    {
        maze[i][0]='|';
        maze[i][column_number-1]='|';
    }
    //output(row_number,column_number,maze);
//------------------------------------------------------------------
//initialize rand functions
    srand((int)time(0));
//--------------------------------------------------------------------
//build main pass way
    main_way_design(1,1,row_number-2,column_number-2,maze);
//-----------------------------------------------------------------
//build other pass way
//start the loop
    do
    {
        trick_way_design(row_number,column_number,maze,trick_number);//build other ways
        trick_number++;//the umber of other trick way

        tool=0;
//calculate the rate of fill in maze
        for (int i=0;i<row_number;i++)
        {
            for (int j=0;j<column_number;j++)
            {
                if (maze[i][j]!=0)
                    tool++;
            }
        }
        //cout<<tool/(row_number*column_number)<<endl;
        if (trick_number>=95)//limit the maximum road number
            break;
    } while (tool/(row_number*column_number)<=0.5);//limit the maximum fill rate
//end the loop
//--------------------------------------------------------------

    package(row_number,column_number,maze);//finish the maze, package it
    //output(row_number,column_number,maze);
//--------------------------------------------------------------
//output maze into maze.txt
    ofstream DataFile;
    DataFile.open("maze.txt",ios::out);

    DataFile<<row_number<<" "<<column_number<<endl;

    for (int i=0;i<row_number;i++)
    {
        for (int j=0;j<column_number;j++)
        {
            //if (maze[i][j]==0)
            //    DataFile<<"  ";
            //else
                DataFile<<maze[i][j]<<" ";
        }
        DataFile<<endl;
    }

    DataFile.close();

    //system("pause");

    return 0;
}

//output matrix
void output(int row,int column,char matrix[55][55])
{
    for (int i=0;i<row;i++)
    {
        for (int j=0;j<column;j++)
            cout<<matrix[i][j]<<" ";
        cout<<endl;
    }
}

//build main pass way
void main_way_design(int begin_row,int begin_column,int end_row,int end_column,char matrix[55][55])
{
//initialize matrix
    char matrix_save[55][55];
//--------------------------------------------------
//copy the original matrix
    for (int i=begin_row;i<=end_row;i++)
    {
        for (int j=begin_column;j<=end_column;j++)
                    matrix_save[i][j]=matrix[i][j];
    }
//-------------------------------------------------------
//start the loop
    RESTART:int row=begin_row;
    int column=begin_column;
    bool up=0,down=0,left=0,right=0;

    matrix[row][column]='*';
    //cout<<"#RESTART#";
    while (row!=end_row||column!=end_column)
    {
        //cout<<"#START SWITCH#"<<endl;
        int direction;

//get the random number(ditrcion)
        direction=(rand()%4);
        //cout<<direction<<endl;
        switch(direction)
        {
        case 0://up
            row--;
            if(check(row,column,matrix,'*'))//check whether this point can step on
            {
                row++;//go back
                up=1;//up=false
            }
            else
                {up=0;down=0;left=0;right=0;}
            break;
        case 1://down
            row++;
            if(check(row,column,matrix,'*'))
            {
                row--;
                down=1;
            }
            else
                {up=0;down=0;left=0;right=0;}
            break;
        case 2://left
            column--;
            if(check(row,column,matrix,'*'))
            {
                column++;
                left=1;
            }
            else
                {up=0;down=0;left=0;right=0;}
            break;
        case 3://right
            column++;
            if(check(row,column,matrix,'*'))
            {
                column--;
                right=1;
            }
            else
                {up=0;down=0;left=0;right=0;}
            break;
        }
        //cout<<up<<down<<left<<right<<endl;

//if four directions are all blocked, than restart the way finder
        if (up*down*left*right==1)
        {
            for (int i=begin_row;i<=end_row;i++)
            {
                for (int j=begin_column;j<=end_column;j++)
                    matrix[i][j]=matrix_save[i][j];
            }
            goto RESTART;//let it back, back, and back
        }

        matrix[row][column]='*';
        //output(end_row+2,end_column+2,matrix);
    }
//end the loop
//---------------------------------------------------------------------------
    //output(end_row+2,end_column+2,matrix);
}

void trick_way_design(int row_number,int column_number,char matrix[55][55],char compare)
{
//initialize characteristics
    int begin_row, begin_column;
    int direction;
    char matrix_save[55][55];
    bool up=0,down=0,left=0,right=0;

//------------------------------------------------------
//get random start point
    do
    {
        begin_row=rand()%row_number;
        begin_column=rand()%column_number;
    } while(matrix[begin_row][begin_column]!=0);//let it start at empty point

//copy the original matrix
    for (int i=0;i<row_number;i++)
    {
        for (int j=0;j<column_number;j++)
                    matrix_save[i][j]=matrix[i][j];
    }

//--------------------------------------------------------------
//start the loop
    RRESTART:
    int row=begin_row;
    int column=begin_column;
    do
    {
    matrix[row][column]=compare;

    direction=rand()%4;
    //cout<<direction<<" ";
     switch(direction)
        {
        case 0:
            row--;
            if(check(row,column,matrix,compare))
            {
                row++;
                up=1;
            }
            else
                {up=0;down=0;left=0;right=0;}
            break;
        case 1:
            row++;
            if(check(row,column,matrix,compare))
            {
                row--;
                down=1;
            }
            else
                {up=0;down=0;left=0;right=0;}
            break;
        case 2:
            column--;
            if(check(row,column,matrix,compare))
            {
                column++;
                left=1;
            }
            else
                {up=0;down=0;left=0;right=0;}
            break;
        case 3:
            column++;
            if(check(row,column,matrix,compare))
            {
                column--;
                right=1;
            }
            else
                {up=0;down=0;left=0;right=0;}
            break;
        }
        //cout<<up<<down<<left<<right<<endl;
        if (up*down*left*right==1)
        {
            for (int i=0;i<row_number;i++)
            {
                for (int j=0;j<=column_number;j++)
                    matrix[i][j]=matrix_save[i][j];
            }
            goto RRESTART;
        }

        if(matrix[row][column]<compare&&matrix[row][column]!=0)//move on a point which already have ways
            break;
    } while(matrix[row][column]==0||matrix[row][column]==compare);//other cases
//end the loop
//-------------------------------------------------------------------------------

    //output(row_number,column_number,matrix);
}

//check whether a good place to move
bool check(int row,int column,char matrix[55][55],char compare)
{
    int tool=0;

    if (matrix[row][column]<compare&&matrix[row][column]!=0)//move on other ways
        return 0;//pass check

//find the number of ways surrounding it
    if (matrix[row][column]==0)
        {
            if (matrix[row+1][column]==compare)
                tool++;
            if (matrix[row-1][column]==compare)
                tool++;
            if (matrix[row][column+1]==compare)
                tool++;
            if (matrix[row][column-1]==compare)
                tool++;

            if (tool==1)
                return 0;//pass check
        }

    return 1;//fail the check
}

void package(int row,int column,char matrix[55][55])
{
//fill empty pints
    for (int i=0;i<row;i++)
    {
        for (int j=0;j<column;j++)
        {
            if (matrix[i][j]==0)
                matrix[i][j]='+';
        }
    }
//------------------------------------------------------
//change edges
    for (int i=0;i<column;i++)
    {
        matrix[0][i]='#';
        matrix[row-1][i]='#';
    }
    for (int i=0;i<row;i++)
    {
        matrix[i][0]='#';
        matrix[i][column-1]='#';
    }
 //---------------------------------------------------
 //clean the notes of ways
    for (int i=1;i<row-1;i++)
    {
        for (int j=1;j<column-1;j++)
        {
            if (matrix[i][j]!='+')
                matrix[i][j]=0;
        }
    }
}
